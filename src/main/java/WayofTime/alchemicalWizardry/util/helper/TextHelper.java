package WayofTime.alchemicalWizardry.util.helper;

import com.google.common.collect.Lists;
import net.minecraft.util.StatCollector;
import scala.actors.threadpool.Arrays;

import java.util.List;

public class TextHelper {

    public static String getFormattedText(String string) {
        return string.replaceAll("&", "\u00A7");
    }

    public static String localize(String key, Object ... format) {
        return getFormattedText(StatCollector.translateToLocalFormatted(key, format));
    }

    /**
     * Localizes all strings in a list, using the prefix.
     *
     * @param unloc
     *          The list of unlocalized strings.
     * @return A list of localized versions of the passed strings.
     */
    public static List<String> localizeAll(List<String> unloc) {
        List<String> ret = Lists.newArrayList();
        for (String s : unloc)
            ret.add(localize(s));

        return ret;
    }

    public static String[] localizeAll(String... unloc) {
        String[] ret = new String[unloc.length];
        for (int i = 0; i < ret.length; i++)
            ret[i] = localize(unloc[i]);

        return ret;
    }
}
