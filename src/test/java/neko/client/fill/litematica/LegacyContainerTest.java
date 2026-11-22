package neko.client.fill.litematica;

import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.*;
import net.minecraft.world.item.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LegacyContainerTest {
    @BeforeAll static void bootstrap() { SharedConstants.tryDetectVersion(); Bootstrap.bootStrap(); }
    @Test void schematicSlotsPreserveNamesAndCountsIn1201Format() {
        var stack = new ItemStack(Items.DIAMOND, 12);
        stack.setHoverName(net.minecraft.network.chat.Component.literal("Named diamond"));
        var item = stack.save(new CompoundTag());
        item.putByte("Slot", (byte) 3);
        var items = new ListTag(); items.add(item);
        var tag = new CompoundTag(); tag.put("Items", items);
        var lookup = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
        var result = QuickLitematicaContainerMaterials.readItems(tag, lookup);
        assertNotNull(result);
        assertEquals(4, result.size());
        assertTrue(result.get(0).isEmpty());
        assertEquals(12, result.get(3).getCount());
        assertTrue(ItemStack.isSameItemSameTags(stack, result.get(3)));
        assertEquals(tag, QuickLitematicaSchematicData.repair(tag, 3465));
    }
    @Test void entityCopyPreservesNbt() {
        var nbt = new CompoundTag(); nbt.putString("id", "minecraft:armor_stand");
        var info = new fi.dy.masa.litematica.schematic.LitematicaSchematic.EntityInfo(net.minecraft.world.phys.Vec3.ZERO, nbt);
        assertEquals(nbt, QuickLitematicaDataCompat.entityNbt(info));
        assertEquals(nbt, QuickLitematicaDataCompat.entityNbt(QuickLitematicaDataCompat.withEntityNbt(info, nbt.copy())));
    }
}
