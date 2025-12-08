package wayoftime.bloodmagic.util;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.text.DecimalFormat;

public class ChatUtil {

    public static void sendChat(Player player, Component text) {
        player.sendSystemMessage(text);
    }

    public static String posString(BlockPos pos) {
        return "%d, %d, %d".formatted(pos.getX(), pos.getY(), pos.getZ());
    }

    public static String dimensionString(ResourceKey<Level> dim) {
        return "%s:%s".formatted(dim.location().getNamespace(), dim.location().getPath());
    }

    public static MutableComponent translatableHover(String key, Object... args) {
        return Component.translatable(key, args).withStyle(ChatFormatting.GRAY);
    }

    public static MutableComponent translatableHover(String key) {
        return Component.translatable(key).withStyle(ChatFormatting.GRAY);
    }
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###,###,###.##");

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
