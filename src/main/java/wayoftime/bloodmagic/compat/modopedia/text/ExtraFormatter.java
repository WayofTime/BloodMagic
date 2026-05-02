package wayoftime.bloodmagic.compat.modopedia.text;

import com.google.common.collect.ImmutableMap;
import net.favouriteless.modopedia.api.text.StyleStack;
import net.favouriteless.modopedia.api.text.TextFormatter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

import java.util.function.UnaryOperator;

public class ExtraFormatter implements TextFormatter {
    public static final ImmutableMap<String, UnaryOperator<Style>> STYLE_MAP = new ImmutableMap.Builder<String, UnaryOperator<Style>>()
            .put("red", style -> style.withColor(0xFF_00_00))
            .put("water", style -> style.withColor(0x00_00_AA))
            .put("air", style -> style.withColor(0xAA_AA_00))
            .put("fire", style -> style.withColor(0xAA_00_00))
            .put("earth", style -> style.withColor(0x00_AA_00))
            .put("blank", style -> style.withColor(0x88_88_88))
            .put("dusk", style -> style.withColor(0x94_00_D3))
            .put("dawn", style -> style.withColor(ChatFormatting.GOLD))
            .put("steadfast", style -> style.withColor(0x00_00_AA))
            .put("destructive", style -> style.withColor(0xAA_AA_00))
            .put("vengeful", style -> style.withColor(0xAA_00_00))
            .put("corrosive", style -> style.withColor(0x00_AA_00))
            .put("raw", style -> style.withColor(0x36_C6_C6))
            .put("blood", style -> style.withColor(0xAA_00_00))
            .build();

    @Override
    public boolean matches(String tag) {
        return STYLE_MAP.containsKey(tag);
    }

    @Override
    public void apply(StyleStack stack, String tag) {
        stack.modify(STYLE_MAP.get(tag));
    }
}
