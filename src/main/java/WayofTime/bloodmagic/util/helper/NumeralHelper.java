package WayofTime.bloodmagic.util.helper;

import java.util.TreeMap;

public class NumeralHelper {

    private static final TreeMap<Integer, String> romanNumerals = new TreeMap<>();

    static {
        romanNumerals.put(1000, "M");
        romanNumerals.put(900, "CM");
        romanNumerals.put(500, "D");
        romanNumerals.put(400, "CD");
        romanNumerals.put(100, "C");
        romanNumerals.put(90, "XC");
        romanNumerals.put(50, "L");
        romanNumerals.put(40, "XL");
        romanNumerals.put(10, "X");
        romanNumerals.put(9, "IX");
        romanNumerals.put(5, "V");
        romanNumerals.put(4, "IV");
        romanNumerals.put(1, "I");
    }

    public static String toRoman(int arabic) {
        int convert = romanNumerals.floorKey(arabic);
        if (arabic == convert)
            return romanNumerals.get(convert);

        return romanNumerals.get(convert) + toRoman(arabic - convert);
    }
}
