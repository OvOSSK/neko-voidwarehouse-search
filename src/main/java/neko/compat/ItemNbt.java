package neko.compat;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
public final class ItemNbt {
 public static void setLore(ItemStack stack,List<Component> lore) {
  ListTag tags=new ListTag();
  for(Component line:lore) tags.add(StringTag.valueOf(Component.Serializer.toJson(line)));
  stack.getOrCreateTagElement("display").put("Lore",tags);
 }
}
