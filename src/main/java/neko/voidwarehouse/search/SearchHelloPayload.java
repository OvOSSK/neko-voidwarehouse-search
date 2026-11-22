package neko.voidwarehouse.search;
import net.minecraft.resources.ResourceLocation;
public record SearchHelloPayload() implements neko.compat.CustomPacketPayload {
 public static final neko.compat.CustomPacketPayload.Type<SearchHelloPayload> TYPE = new neko.compat.CustomPacketPayload.Type<>(new ResourceLocation("carpet_neko_addition", "search_hello"));
 public static final SearchHelloPayload INSTANCE = new SearchHelloPayload();

 public static final neko.compat.StreamCodec<net.minecraft.network.FriendlyByteBuf,SearchHelloPayload> CODEC = neko.compat.StreamCodec.unit(INSTANCE);
 public neko.compat.CustomPacketPayload.Type<SearchHelloPayload> type() { return TYPE; }
}
