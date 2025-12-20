package wayoftime.bloodmagic.util;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.text.DecimalFormat;
import java.util.List;

public class ChatUtil {
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###,###,###.##");

    public static void sendChat(Player player, List<Component> text) {
        text.forEach(player::sendSystemMessage);
    }

    // TODO implement the no-spam version. looks like that would have to be done with a mixin that likely exists on like 5 mods that will be loaded at any given time already
    public static void sendChatNoSpam(Player player, List<Component> text) {
        sendChat(player, text);
    }


    private static final char[] ones = new char[]{'I', 'X', 'C', 'M'};
    private static final char[] fives = new char[]{'V', 'L', 'D'};

    public static String toRoman(int in) {
        String input = new StringBuilder(Integer.toString(in)).reverse().toString();
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
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
