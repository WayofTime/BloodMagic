package WayofTime.alchemicalWizardry.client.renderer;

import java.util.List;
/**
 * This class is a utility class that was created by bspkrs.
 * https://github.com/bspkrs/bspkrsCore/blob/master/src/main/java/bspkrs/client/util/ColorThreshold.java
 */
public class ColourThreshold implements Comparable<ColourThreshold>
{
    public int    threshold;
    public String colorCode;

    public ColourThreshold(int t, String c)
    {
        threshold = t;
        colorCode = c;
    }

    @Override
    public String toString()
    {
        return String.valueOf(threshold) + ", " + colorCode;
    }

    @Override
    public int compareTo(ColourThreshold o)
    {
        if (this.threshold > o.threshold)
            return 1;
        else if (this.threshold < o.threshold)
            return -1;
        else
            return 0;
    }

    /**
     * Returns the colorCode attached to the first threshold in the list that is
     * >= value. Expects that the list has been sorted by threshold ascending.
     */
    public static String getColorCode(List<ColourThreshold> colorList, int value)
    {
        for (ColourThreshold ct : colorList)
            if (value <= ct.threshold)
                return ct.colorCode;

        return "f";
    }
}

