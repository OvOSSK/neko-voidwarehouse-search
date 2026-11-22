package neko.voidwarehouse.search;
import java.util.List;
import java.util.ArrayList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
public record SearchCatalogPayload(List<String> itemIds) implements neko.compat.CustomPacketPayload {
 public static final neko.compat.CustomPacketPayload.Type<SearchCatalogPayload> TYPE = new neko.compat.CustomPacketPayload.Type<>(new ResourceLocation("carpet_neko_addition", "search_catalog"));
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

 public static final neko.compat.StreamCodec<net.minecraft.network.FriendlyByteBuf,SearchCatalogPayload> CODEC = new neko.compat.StreamCodec<>() {
 public SearchCatalogPayload decode(net.minecraft.network.FriendlyByteBuf b) { return SearchCatalogPayload.decode(b); }
 public void encode(net.minecraft.network.FriendlyByteBuf b,SearchCatalogPayload value) { var encoded=value.encode(); try { b.writeBytes(encoded); } finally { encoded.release(); } }
 };
 public neko.compat.CustomPacketPayload.Type<SearchCatalogPayload> type() { return TYPE; }
}
