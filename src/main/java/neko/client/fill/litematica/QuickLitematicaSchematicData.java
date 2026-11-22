// Derived from quickcraft v1.0.4 (MIT License, Copyright (c) 2026 yiyihehe).
// Adapted for neko-client: package renamed; optional branches are stubbed.
package neko.client.fill.litematica;

import com.mojang.serialization.Dynamic;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.malilib.gui.Message.MessageType;
import fi.dy.masa.malilib.util.InfoUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.core.HolderLookup;
import org.slf4j.LoggerFactory;
import java.util.HashSet;
import java.util.Set;

/** 在投影库存进入原生解析前补救遗留物品；失败保留原记录，写入方必须拒绝不完整库存。 */
public final class QuickLitematicaSchematicData {
    private static final int LAST_LEGACY_ITEM_VERSION = 3700; // 1.20.4，Count/tag 格式的最后正式版本。
    private static final Set<String> WARNED = new HashSet<>();

    private QuickLitematicaSchematicData() { }

    public static Tag repair(Tag input, int sourceVersion) {
        if (input instanceof CompoundTag compound) {
            CompoundTag result = compound;
            for (String key : result.getAllKeys()) {
                Tag value = result.get(key);
                if (value == null || key.equals("minecraft:custom_data")) { continue; }
                Tag repaired = repair(value, sourceVersion);
                if (repaired != value) {
                    if (result == compound) { result = compound.copy(); }
                    result.put(key, repaired);
                }
            }
            return result;
        }
        if (input instanceof ListTag list) {
            ListTag result = list;
            for (int i = 0; i < list.size(); i++) {
                Tag value = list.get(i);
                Tag repaired = repair(value, sourceVersion);
                if (repaired != value) {
                    if (result == list) { result = list.copy(); }
                    result.set(i, repaired);
                }
            }
            return result;
        }
        return input;
    }

    // 旧原理图可省略 id；从实际方块类型恢复，不能把所有库存猜成箱子，也不能覆盖已存在的类型。
    static CompoundTag prepareBlockEntity(CompoundTag input, BlockState state, BlockPos pos) {
        CompoundTag nbt = (CompoundTag) repair(input, LAST_LEGACY_ITEM_VERSION);
        if (!QuickLitematicaContainerMaterialsAccess.readString(nbt, "id").isEmpty()
                || !(state.getBlock() instanceof EntityBlock provider)) { return nbt; }
        var entity = provider.newBlockEntity(pos, state);
        if (entity == null) { return nbt; }
        var id = BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(entity.getType());
        if (id == null) { return nbt; }
        CompoundTag prepared = nbt.copy();
        prepared.putString("id", id.toString());
        return prepared;
    }

    public static Object repairLitematicaList(Object data, int sourceVersion) {
        ListTag list = (ListTag) data;
        ListTag repaired = (ListTag) repair(list, sourceVersion);
        if (repaired == list) { return data; }
        return repaired;
    }

    public static Object repairLitematicaCompound(Object data, int sourceVersion) {
        CompoundTag nbt = QuickLitematicaDataCompat.toVanillaNbt(data);
        CompoundTag repaired = (CompoundTag) repair(nbt, sourceVersion);
        if (repaired == nbt) { return data; }
        return repaired;
    }

    public static ItemStack decodeItem(CompoundTag input, HolderLookup.Provider lookup) {
        CompoundTag nbt = (CompoundTag) repair(input, LAST_LEGACY_ITEM_VERSION);
        if (nbt.contains("components") || nbt.contains("count")) {
            warn(input, "Item data is newer than Minecraft 1.20.1");
            return ItemStack.EMPTY;
        }
        try {
            ItemStack stack = QuickLitematicaContainerMaterialsAccess.decodeCurrentItem(lookup, nbt);
            if (stack.isEmpty() && !isEmptyItem(input)) { warn(input, "ItemStack decode failed"); }
            return stack;
        } catch (RuntimeException exception) {
            warn(input, exception.getMessage());
            return ItemStack.EMPTY;
        }
    }

    static boolean isEmptyItem(CompoundTag nbt) {
        return nbt.isEmpty() || QuickLitematicaContainerMaterialsAccess.readString(nbt, "id").equals("minecraft:air");
    }

    private static boolean isLegacyItem(CompoundTag nbt) {
        return nbt.contains("id") && nbt.contains("Count") && !nbt.contains("Items")
                && !nbt.contains("x") && !nbt.contains("Pos");
    }

    static void unreadableInventory(CompoundTag nbt) { warn(nbt, "Invalid or duplicate inventory slot"); }

    private static void warn(CompoundTag nbt, String reason) {
        String id = QuickLitematicaContainerMaterialsAccess.readString(nbt, "id");
        synchronized (WARNED) {
            if (WARNED.contains(id) || WARNED.size() >= 64) { return; }
            WARNED.add(id);
        }
        LoggerFactory.getLogger(QuickLitematicaSchematicData.class).warn("Unresolved schematic item {}: {}; preserving original NBT", id, reason);
        var client = Minecraft.getInstance();
        if (client != null) {
            client.execute(() -> InfoUtils.showGuiOrInGameMessage(MessageType.WARNING,
                    "neko_client.fill.litematica.error.container_data_incomplete", id));
        }
    }
}
