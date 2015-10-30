package WayofTime.alchemicalWizardry.api.util.helper;

public class TextHelper {
    public static String getFormattedText(String string) {
        return string.replaceAll("&", "\u00A7");
    }
}
