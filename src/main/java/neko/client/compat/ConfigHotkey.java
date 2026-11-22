package neko.client.compat;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import fi.dy.masa.malilib.util.StringUtils;
public class ConfigHotkey extends fi.dy.masa.malilib.config.options.ConfigHotkey {
    private String prefix;
    public ConfigHotkey(String name, String keys, KeybindSettings settings) { super(name, keys, settings, ""); }
    public ConfigHotkey apply(String prefix) { this.prefix = prefix; return this; }
    @Override public String getConfigGuiDisplayName() { return StringUtils.translate(prefix + ".name." + getName()); }
    @Override public String getPrettyName() { return StringUtils.translate(prefix + ".prettyName." + getName()); }
    @Override public String getComment() { return StringUtils.translate(prefix + ".comment." + getName()); }
    public void setHotkeyStringValue(String value) { getKeybind().setValueFromString(value); }
    public String getHotkeyStringValue() { return getKeybind().getStringValue(); }
}
