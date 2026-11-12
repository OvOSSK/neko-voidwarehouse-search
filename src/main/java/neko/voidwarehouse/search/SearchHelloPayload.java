package neko.voidwarehouse.search;
import net.minecraft.resources.ResourceLocation;
public record SearchHelloPayload() {
 public static final ResourceLocation TYPE = new ResourceLocation("carpet_neko_addition", "search_hello");
 public static final SearchHelloPayload INSTANCE = new SearchHelloPayload();
}
