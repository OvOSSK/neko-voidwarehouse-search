package neko.voidwarehouse.search;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
public final class VoidWarehouseSearchClient implements ClientModInitializer {
 public void onInitializeClient() {
  ClientPlayNetworking.registerGlobalReceiver(SearchCatalogPayload.TYPE, (client, handler, buf, sender) -> {
   SearchCatalogPayload payload = SearchCatalogPayload.decode(buf);
   client.execute(() -> SearchClientState.onCatalog(payload.itemIds()));
  });
  ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> SearchClientState.clear());
 }
}
