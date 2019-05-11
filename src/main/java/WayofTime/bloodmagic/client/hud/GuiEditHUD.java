package WayofTime.bloodmagic.client.hud;

import WayofTime.bloodmagic.client.hud.element.HUDElement;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import javax.vecmath.Vector2f;
import java.awt.Point;
import java.util.Map;

public class GuiEditHUD extends GuiScreen {

    private static final int LINE_COLOR = 0x2D2D2D;

    private final GuiScreen parent;
    private final Map<ResourceLocation, Vector2f> currentOverrides = Maps.newHashMap();
    private HUDElement dragged;
    public boolean changes;

    public GuiEditHUD(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void initGui() {
        super.initGui();

        addButton(new GuiButtonExt(0, width / 2 - 155, height - 30, 70, 20, I18n.format("gui.bloodmagic.toggle")) {{
            enabled = false;
        }});
        addButton(new GuiButtonExt(1, width / 2 - 75, height - 30, 70, 20, I18n.format("gui.bloodmagic.default")));
        addButton(new GuiButtonExt(2, width / 2 + 5, height - 30, 70, 20, I18n.format("gui.bloodmagic.save")));
        addButton(new GuiButtonExt(3, width / 2 + 90, height - 30, 70, 20, I18n.format("gui.bloodmagic.cancel")));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        for (HUDElement element : ElementRegistry.getElements()) {
            if (dragged == element)
                continue;

            ResourceLocation key = ElementRegistry.getKey(element);
            Vector2f position = currentOverrides.getOrDefault(key, ElementRegistry.getPosition(key));
            int xPos = (int) (resolution.getScaledWidth_double() * position.x);
            int yPos = (int) (resolution.getScaledHeight_double() * position.y);

            drawWithBox(resolution, element, partialTicks, xPos, yPos);
        }

        if (dragged != null) {
            Point bounded = getBoundedDrag(resolution, mouseX, mouseY);
            drawWithBox(resolution, dragged, partialTicks, bounded.x, bounded.y);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (dragged != null)
            return;

        HUDElement element = getHoveredElement(mouseX, mouseY);
        if (element == null)
            return;

        if (clickedMouseButton == 0)
            dragged = element;
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (dragged != null) {
            ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
            Point bounded = getBoundedDrag(resolution, mouseX, mouseY);
            float xPos = (float) ((bounded.x) / resolution.getScaledWidth_double());
            float yPos = (float) ((bounded.y) / resolution.getScaledHeight_double());

            currentOverrides.put(ElementRegistry.getKey(dragged), new Vector2f(xPos, yPos));
            changes = true;
            dragged = null;
            return;
        }

        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0: {
                Minecraft.getMinecraft().displayGuiScreen(parent);
                break;
            }
            case 1: {
                currentOverrides.clear();
                ElementRegistry.resetPos();
                changes = false;
                break;
            }
            case 2: {
                ElementRegistry.save(currentOverrides);
                Minecraft.getMinecraft().displayGuiScreen(parent);
                break;
            }
            case 3: {
                currentOverrides.clear();
                Minecraft.getMinecraft().displayGuiScreen(parent);
                break;
            }
        }
    }

    @Nullable
    public HUDElement getHoveredElement(int mouseX, int mouseY) {
        for (HUDElement element : ElementRegistry.getElements()) {
            ResourceLocation key = ElementRegistry.getKey(element);
            Vector2f position = currentOverrides.getOrDefault(key, ElementRegistry.getPosition(key));
            ScaledResolution resolution = new ScaledResolution(mc);
            int xPos = (int) (resolution.getScaledWidth_double() * position.x);
            int yPos = (int) (resolution.getScaledHeight_double() * position.y);

            if (mouseX < xPos || mouseX > xPos + element.getWidth())
                continue;

            if (mouseY < yPos || mouseY > yPos + element.getHeight())
                continue;

            return element;
        }

        return null;
    }

    protected Point getBoundedDrag(ScaledResolution resolution, int mouseX, int mouseY) {
        int drawX = mouseX - dragged.getWidth() / 2;
        if (drawX + dragged.getWidth() >= resolution.getScaledWidth())
            drawX = resolution.getScaledWidth() - dragged.getWidth();
        if (drawX < 0)
            drawX = 0;

        int drawY = mouseY - dragged.getHeight() / 2;
        if (drawY + dragged.getHeight() >= resolution.getScaledHeight())
            drawY = resolution.getScaledHeight() - dragged.getHeight();
        if (drawY < 0)
            drawY = 0;

        return new Point(drawX, drawY);
    }

    protected void drawWithBox(ScaledResolution resolution, HUDElement element, float partialTicks, int drawX, int drawY) {
        int color = ElementRegistry.getColor(ElementRegistry.getKey(element));
        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        drawVerticalLine(drawX, drawY, drawY + element.getHeight() - 1, color);
        drawVerticalLine(drawX + element.getWidth() - 1, drawY, drawY + element.getHeight() - 1, color);
        drawHorizontalLine(drawX, drawX + element.getWidth() - 1, drawY, color);
        drawHorizontalLine(drawX, drawX + element.getWidth() - 1, drawY + element.getHeight() - 1, color);
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        GlStateManager.enableTexture2D();
        element.draw(resolution, partialTicks, drawX, drawY);
        GlStateManager.disableTexture2D();
    }
}
