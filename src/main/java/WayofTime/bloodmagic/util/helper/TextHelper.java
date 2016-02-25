package WayofTime.bloodmagic.util.helper;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.StatCollector;

import org.apache.commons.lang3.text.WordUtils;

public class TextHelper
{
    public static String getFormattedText(String string)
    {
        return string.replaceAll("&", "\u00A7");
    }

    public static String localize(String input, Object... format)
    {
        return StatCollector.translateToLocalFormatted(input, format);
    }

    public static String localizeEffect(String input, Object... format)
    {
        return getFormattedText(localize(input, format));
    }

    public static String[] localizeAll(String[] input)
    {
        String[] ret = new String[input.length];
        for (int i = 0; i < input.length; i++)
            ret[i] = localize(input[i]);

        return ret;
    }

    public static String[] localizeAllEffect(String[] input)
    {
        String[] ret = new String[input.length];
        for (int i = 0; i < input.length; i++)
            ret[i] = localizeEffect(input[i]);

        return ret;
    }

    public static ArrayList<String> localizeAll(List<String> input)
    {
        ArrayList<String> ret = new ArrayList<String>(input.size());
        for (int i = 0; i < input.size(); i++)
            ret.add(i, localize(input.get(i)));

        return ret;
    }

    public static ArrayList<String> localizeAllEffect(List<String> input)
    {
        ArrayList<String> ret = new ArrayList<String>(input.size());
        for (int i = 0; i < input.size(); i++)
            ret.add(i, localizeEffect(input.get(i)));

        return ret;
    }

    public static String[] cutLongString(String string, int characters)
    {
        return WordUtils.wrap(string, characters, "/cut", false).split("/cut");
    }

    public static String[] cutLongString(String string)
    {
        return cutLongString(string, 30);
    }
}
