package neko.voidwarehouse.search;
import java.util.List;
import java.util.ArrayList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
public record SearchCatalogPayload(List<String> itemIds) {
 public static final ResourceLocation TYPE = new ResourceLocation("carpet_neko_addition", "search_catalog");
 public static final int MAX_IDS = 4096;
 public SearchCatalogPayload { itemIds = List.copyOf(itemIds); if (itemIds.size() > MAX_IDS) throw new IllegalArgumentException("Too many item IDs"); }
 public FriendlyByteBuf encode() {
  FriendlyByteBuf buf = PacketByteBufs.create();
  buf.writeVarInt(itemIds.size());
  for (String id : itemIds) buf.writeUtf(id);
  return buf;
 }
 public static SearchCatalogPayload decode(FriendlyByteBuf buf) {
  int size = buf.readVarInt();
  if (size < 0 || size > MAX_IDS) throw new IllegalArgumentException("Invalid item ID count");
  List<String> ids = new ArrayList<>(size);
  for (int i = 0; i < size; i++) ids.add(buf.readUtf());
  return new SearchCatalogPayload(ids);
 }
}
