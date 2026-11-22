package neko.client.compat;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import fi.dy.masa.malilib.util.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
public class ConfigBooleanHotkeyed extends fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed {
    private String prefix;
    private String prettyName;
    public ConfigBooleanHotkeyed(String name, boolean value, String keys, KeybindSettings settings) { super(name, value, keys, settings, "", name); }
    public ConfigBooleanHotkeyed apply(String prefix) { this.prefix = prefix; return this; }
    public void setPrettyName(String name) { prettyName = name; }
    @Override public String getConfigGuiDisplayName() { return StringUtils.translate(prefix + ".name." + getName()); }
    @Override public String getPrettyName() { return StringUtils.translate(prettyName != null ? prettyName : prefix + ".prettyName." + getName()); }
    @Override public String getComment() { return StringUtils.translate(prefix + ".comment." + getName()); }
    public void setBooleanHotkeyValue(Pair<Boolean,String> value) { setBooleanValue(value.getLeft()); getKeybind().setValueFromString(value.getRight()); }
    public Pair<Boolean,String> getBooleanHotkeyValue() { return Pair.of(getBooleanValue(), getKeybind().getStringValue()); }
}
