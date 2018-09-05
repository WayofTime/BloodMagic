package com.wayoftime.bloodmagic.guide.page;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import java.util.List;
import java.util.function.Consumer;

public class PageComponentText extends PageComponent {

    private final ITextComponent textComponent;

    public PageComponentText(ITextComponent text) {
        super(-1);

        this.textComponent = text;
        withComponentSplitter(new StringSplitter(textComponent));
    }

    private static class StringSplitter implements IComponentSplitter {

        private final ITextComponent component;

        public StringSplitter(ITextComponent component) {
            this.component = component;
        }

        @Override
        public void split(Consumer<PageComponent> components, int currentPosition, int pageHeight) {
            String fullText = component.getFormattedText();
            FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
            List<String> lines = fontRenderer.listFormattedStringToWidth(fullText, 300);
            int remainingSpace = pageHeight - currentPosition;
            for (String line : lines) {
                List<String> componentLines = Lists.newArrayList();
                if (remainingSpace >= fontRenderer.FONT_HEIGHT + 3) {
                    componentLines.add(line);
                    remainingSpace += fontRenderer.FONT_HEIGHT + 3;
                } else {
                    remainingSpace = pageHeight;
                    components.accept(new PageComponentText(new TextComponentString(String.join(" ", componentLines))));
                    componentLines.clear();
                }
            }
        }
    }
}
