package neko.compat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import java.util.function.BiConsumer;
public final class ServerPlayNetworking {
 public record Context(MinecraftServer server,ServerPlayer player) {}
 public static <T extends CustomPacketPayload> void registerGlobalReceiver(CustomPacketPayload.Type<T> type,BiConsumer<T,Context> handler){
 var codec=PayloadTypeRegistry.playC2S().codec(type);
 net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.registerGlobalReceiver(type.id(),(server,player,connection,buf,sender)->{
 T payload=codec.decode(buf); server.execute(()->handler.accept(payload,new Context(server,player)));
 }); }
 public static boolean canSend(ServerPlayer player,CustomPacketPayload.Type<?> type){return net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.canSend(player,type.id());}
 public static <T extends CustomPacketPayload> void send(ServerPlayer player,T payload){sendTyped(player,payload,payload.type());}
 @SuppressWarnings("unchecked") private static <T extends CustomPacketPayload> void sendTyped(ServerPlayer player,CustomPacketPayload payload,CustomPacketPayload.Type<T> type){
 FriendlyByteBuf buf=PacketByteBufs.create();
 try { PayloadTypeRegistry.playS2C().codec(type).encode(buf,(T)payload); }
 catch(RuntimeException error){buf.release();throw error;}
 net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(player,type.id(),buf);
 }
}