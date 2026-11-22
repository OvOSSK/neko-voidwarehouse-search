// Derived from quickcraft v1.0.4 (MIT License, Copyright (c) 2026 yiyihehe).
// Adapted for neko-client: package renamed; optional branches are stubbed.
package neko.client.fill.litematica;

import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.litematica.schematic.LitematicaSchematic.EntityInfo;
import fi.dy.masa.malilib.gui.widgets.WidgetFileBrowserBase.DirectoryEntry;
import fi.dy.masa.malilib.util.ItemType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

final class QuickLitematicaContainerMaterialsAccess {
    private QuickLitematicaContainerMaterialsAccess() {
    }

    static QuickLitematicaContainerMaterials.ItemKey createItemKey(ItemStack stack) {
        return new NativeItemKey(stack);
    }

    private static final class NativeItemKey extends QuickLitematicaContainerMaterials.ItemKey {
        private final ItemType type;

        private NativeItemKey(ItemStack stack) {
            this.type = new ItemType(stack, true, true);
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof NativeItemKey key && this.type.equals(key.type);
        }

        @Override
        public int hashCode() {
            return this.type.hashCode();
        }

        @Override
        public String toString() {
            return this.type.toString();
        }
    }

    static Path getEntryPath(DirectoryEntry entry) {
        return entry.getFullPath().toPath();
    }

    static String getEntryFileName(DirectoryEntry entry) {
        return entry.getFullPath().getName();
    }

    static ItemStack itemStackFromNbt(HolderLookup.Provider registryLookup, CompoundTag nbt) {
        return QuickLitematicaSchematicData.decodeItem(nbt, registryLookup);
    }

    static ItemStack decodeCurrentItem(HolderLookup.Provider registryLookup, CompoundTag nbt) {
        return ItemStack.of(nbt);
    }

    static boolean containsItemsList(CompoundTag nbt) {
        return nbt.contains("Items");
    }

    static String readString(CompoundTag nbt, String key) {
        return nbt.getString(key);
    }

    static ListTag readCompoundList(CompoundTag nbt, String key) {
        return nbt.getList(key, 10);
    }

    static CompoundTag readCompound(ListTag list, int index) {
        return list.getCompound(index);
    }

    static Map<BlockPos, CompoundTag> getBlockEntities(LitematicaSchematic schematic, String regionName) {
        Map<BlockPos, ?> map = schematic.getBlockEntityMapForRegion(regionName);
        if (map == null) {
            return null;
        }
        Map<BlockPos, CompoundTag> result = new HashMap<>();
        for (Map.Entry<BlockPos, ?> entry : map.entrySet()) {
            if (entry.getValue() != null) {
                result.put(entry.getKey(), QuickLitematicaDataCompat.toVanillaNbt(entry.getValue()));
            }
        }
        return result;
    }

    static CompoundTag getEntityNbt(EntityInfo info) {
        return QuickLitematicaDataCompat.entityNbt(info);
    }

    static Vec3 getEntityPos(EntityInfo info) {
        return QuickLitematicaDataCompat.entityPos(info);
    }

    static Path schematicPath(LitematicaSchematic schematic) {
        return schematic.getFile() == null ? null : schematic.getFile().toPath();
    }
    static LitematicaSchematic copySchematic(LitematicaSchematic schematic) {
        Path path = schematicPath(schematic);
        return QuickLitematicaDataCompat.copySchematic(schematic, path == null ? Path.of("memory.litematic") : path);
    }
    static byte readByte(CompoundTag nbt, String key) { return nbt.getByte(key); }
    static CompoundTag parseNbt(String value) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        return net.minecraft.nbt.TagParser.parseTag(value);
    }
    static void setBlockEntities(LitematicaSchematic schematic, String region, Map<BlockPos, CompoundTag> values) {
        QuickLitematicaDataCompat.setBlockEntities(schematic, region, values);
    }
    static void setEntityNbt(java.util.List<EntityInfo> entities, int index, CompoundTag nbt) {
        entities.set(index, QuickLitematicaDataCompat.withEntityNbt(entities.get(index), nbt));
    }
    static CompoundTag blockEntityNbt(net.minecraft.world.level.block.entity.BlockEntity entity, HolderLookup.Provider lookup) {
        return entity.saveWithFullMetadata();
    }
    static void readBlockEntity(net.minecraft.world.level.block.entity.BlockEntity entity, CompoundTag nbt, HolderLookup.Provider lookup) {
        entity.load(nbt);
    }
    static Vec3 entityPosition(net.minecraft.world.entity.Entity entity) { return entity.position(); }
}

