package WayofTime.bloodmagic.client.hud.element;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.ITextComponent;

public class ElementString extends HUDElement {

    private ITextComponent[] display;

    public ElementString(ITextComponent... display) {
        super(getMaxStringWidth(display), (Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 2) * display.length - 2);

        this.display = display;
    }

    @Override
    public void draw(ScaledResolution resolution, float partialTicks, int drawX, int drawY) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        for (ITextComponent drawText : display) {
            fontRenderer.drawStringWithShadow(drawText.getFormattedText(), drawX, drawY, 14737632);
            drawY += fontRenderer.FONT_HEIGHT + 2;
        }
    }

    private static int getMaxStringWidth(ITextComponent... display) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        int maxWidth = 0;
        for (ITextComponent drawText : display)
            if (fontRenderer.getStringWidth(drawText.getFormattedText()) > maxWidth)
                maxWidth = fontRenderer.getStringWidth(drawText.getFormattedText());

        return maxWidth;
    }
}
