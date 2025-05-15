package wayoftime.bloodmagic.util;

import java.text.DecimalFormat;

public class ChatUtil {
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###,###,###.##");

    private static final char[] ones = new char[]{'I', 'X', 'C', 'M'};
    private static final char[] fives = new char[]{'V', 'L', 'D'};

    public static String toRoman(int in) {
        String input = Integer.toString(in);
        StringBuilder output = new StringBuilder();
        for (int i = input.length() -1; i >= 0; i--) {
            output.append(romanDigit(input.charAt(i), ones[i], fives[i], ones[i+1]));
        }
        return output.toString();
    }

    private static String romanDigit(char in, char one, char five, char ten) {
        return switch (in) {
            case '0' -> "";
            case '1' -> "" + one;
            case '2' -> "" + one + one;
            case '3' -> "" + one + one + one;
            case '4' -> "" + one + five;
            case '5' -> "" + five;
            case '6' -> "" + five + one;
            case '7' -> "" + five + one + one;
            case '8' -> "" + five + one + one + one;
            case '9' -> "" + one + ten;
            default -> "[%s not found]".formatted(in);
        };
    }
}
