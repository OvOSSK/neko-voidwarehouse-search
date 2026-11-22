package neko.voidwarehouse.search;
import java.util.List;
import java.util.ArrayList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
public record SearchApplyPayload(List<String> itemIds, boolean clearFilter) implements neko.compat.CustomPacketPayload {
 public static final neko.compat.CustomPacketPayload.Type<SearchApplyPayload> TYPE = new neko.compat.CustomPacketPayload.Type<>(new ResourceLocation("carpet_neko_addition", "search_apply"));
 public static final int MAX_IDS = 4096;
 // Legacy constructor retains the original empty-list-means-clear semantics.
 public SearchApplyPayload(List<String> itemIds) { this(itemIds, itemIds.isEmpty()); }
 public static SearchApplyPayload forQuery(String query, List<String> matchedIds) {
  return new SearchApplyPayload(matchedIds, query == null || query.isBlank());
 }
 public SearchApplyPayload { itemIds = List.copyOf(itemIds); if (itemIds.size() > MAX_IDS) throw new IllegalArgumentException("Too many item IDs"); }
 public FriendlyByteBuf encode() {
  FriendlyByteBuf buf = PacketByteBufs.create();
  buf.writeVarInt(itemIds.size());
  for (String id : itemIds) buf.writeUtf(id);
  buf.writeBoolean(clearFilter);
  return buf;
 }
 public static SearchApplyPayload decode(FriendlyByteBuf buf) {
  int size = buf.readVarInt();
  if (size < 0 || size > MAX_IDS) throw new IllegalArgumentException("Invalid item ID count");
  List<String> ids = new ArrayList<>(size);
  for (int i = 0; i < size; i++) ids.add(buf.readUtf());
  // Older clients have no trailing flag and retain their original behavior.
  boolean clear = buf.isReadable() ? buf.readBoolean() : ids.isEmpty();
  return new SearchApplyPayload(ids, clear);
 }

 public static final neko.compat.StreamCodec<net.minecraft.network.FriendlyByteBuf,SearchApplyPayload> CODEC = new neko.compat.StreamCodec<>() {
 public SearchApplyPayload decode(net.minecraft.network.FriendlyByteBuf b) { return SearchApplyPayload.decode(b); }
 public void encode(net.minecraft.network.FriendlyByteBuf b,SearchApplyPayload value) { var encoded=value.encode(); try { b.writeBytes(encoded); } finally { encoded.release(); } }
 };
 public neko.compat.CustomPacketPayload.Type<SearchApplyPayload> type() { return TYPE; }
}
