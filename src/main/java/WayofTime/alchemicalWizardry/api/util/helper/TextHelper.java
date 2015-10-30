package WayofTime.alchemicalWizardry.api.util.helper;

import net.minecraft.util.StatCollector;

public class TextHelper {

    public static String getFormattedText(String string) {
        return string.replaceAll("&", "\u00A7");
    }

    public static String localize(String key, Object ... format) {
        return getFormattedText(StatCollector.translateToLocalFormatted(key, format));
    }
}
