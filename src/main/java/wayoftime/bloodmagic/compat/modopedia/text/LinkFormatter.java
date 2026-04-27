package wayoftime.bloodmagic.compat.modopedia.text;

import net.favouriteless.modopedia.api.text.StyleStack;
import net.favouriteless.modopedia.api.text.TextFormatter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;

import java.util.function.UnaryOperator;

public class LinkFormatter implements TextFormatter {

    public final String prefix;
    public final String type;

    public LinkFormatter(String prefix, String type) {
        this.prefix = prefix;
        this.type = type;
    }

    public UnaryOperator<Style> linkStyle(String id) {
        return style -> {
            style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/modopedia open %s \"%s\"", type, id)));
            style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable(String.format("guide.bloodmagic.%s.%s", type, id))));
            style.applyFormat(ChatFormatting.UNDERLINE);
            style.withColor(ChatFormatting.BLUE);

            return style;
        };
    }

    @Override
    public boolean matches(String tag) {
        return tag.startsWith(prefix);
    }

    @Override
    public void apply(StyleStack stack, String tag) {
        String id = tag.substring(prefix.length());
        stack.modify(linkStyle(id));
    }
}
