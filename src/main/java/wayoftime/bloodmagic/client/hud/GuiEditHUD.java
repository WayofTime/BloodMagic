package wayoftime.bloodmagic.client.hud;

import java.awt.Point;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import wayoftime.bloodmagic.client.hud.element.HUDElement;

public class GuiEditHUD extends Screen
{
	private static final int LINE_COLOR = 0x2D2D2D;

	private final Screen parent;
	private final Map<ResourceLocation, Vector2f> currentOverrides = Maps.newHashMap();
	private HUDElement dragged;
	public boolean changes;

	public GuiEditHUD(Screen parent)
	{
		super(new StringTextComponent("Testing GuiEditHUD"));
		this.parent = parent;
	}

	@Override
	public void init()
	{
		super.init();

		addButton(new Button(width / 2 - 155, height - 30, 70, 20, new TranslationTextComponent("gui.bloodmagic.toggle"), b -> {
			Minecraft.getInstance().displayGuiScreen(parent);
		})
		{
			{
				active = false;
			}
		});
		addButton(new Button(width / 2 - 75, height - 30, 70, 20, new TranslationTextComponent("gui.bloodmagic.default"), b -> {
			currentOverrides.clear();
			ElementRegistry.resetPos();
			changes = false;
		}));
		addButton(new Button(width / 2 + 5, height - 30, 70, 20, new TranslationTextComponent("gui.bloodmagic.save"), b -> {
			ElementRegistry.save(currentOverrides);
			Minecraft.getInstance().displayGuiScreen(parent);
		}));
		addButton(new Button(width / 2 + 90, height - 30, 70, 20, new TranslationTextComponent("gui.bloodmagic.cancel"), b -> {
			currentOverrides.clear();
			Minecraft.getInstance().displayGuiScreen(parent);
		}));
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);

//		ScaledResolution resolution = new ScaledResolution(Minecraft.getInstance());
		MainWindow window = Minecraft.getInstance().getMainWindow();
		for (HUDElement element : ElementRegistry.getElements())
		{
			if (dragged == element)
				continue;

			ResourceLocation key = ElementRegistry.getKey(element);
			Vector2f position = currentOverrides.getOrDefault(key, ElementRegistry.getPosition(key));
			int xPos = (int) (window.getScaledWidth() * position.x);
			int yPos = (int) (window.getScaledHeight() * position.y);

			drawWithBox(matrixStack, element, partialTicks, xPos, yPos);
		}

		if (dragged != null)
		{
			Point bounded = getBoundedDrag(window, mouseX, mouseY);
			drawWithBox(matrixStack, dragged, partialTicks, bounded.x, bounded.y);
		}
	}

	@Override
	public boolean isPauseScreen()
	{
		return true;
	}

	@Override
//	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY)
	{
		if (dragged == null)
		{
			HUDElement element = getHoveredElement(mouseX, mouseY);
			if (element != null)
			{
				if (button == 0)
					dragged = element;
			}

		}

		return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
//		if (dragged != null)
//			return false;
//
//		HUDElement element = getHoveredElement(mouseX, mouseY);
//		if (element == null)
//			return false;
//
//		if (button == 0)
//			dragged = element;
//
//		return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int state)
	{
		if (dragged != null)
		{
			MainWindow window = Minecraft.getInstance().getMainWindow();
			Point bounded = getBoundedDrag(window, mouseX, mouseY);
			float xPos = (float) ((bounded.x) / window.getScaledWidth());
			float yPos = (float) ((bounded.y) / window.getScaledHeight());

			currentOverrides.put(ElementRegistry.getKey(dragged), new Vector2f(xPos, yPos));
			changes = true;
			dragged = null;
//			return super;
		}

		return super.mouseReleased(mouseX, mouseY, state);
	}

//	@Override
//	protected void actionPerformed(Button button)
//	{
//		switch (button.id)
//		{
//		case 0:
//		{
//			Minecraft.getInstance().displayGuiScreen(parent);
//			break;
//		}
//		case 1:
//		{
//			currentOverrides.clear();
//			ElementRegistry.resetPos();
//			changes = false;
//			break;
//		}
//		case 2:
//		{
//			ElementRegistry.save(currentOverrides);
//			Minecraft.getInstance().displayGuiScreen(parent);
//			break;
//		}
//		case 3:
//		{
//			currentOverrides.clear();
//			Minecraft.getInstance().displayGuiScreen(parent);
//			break;
//		}
//		}
//	}

	@Nullable
	public HUDElement getHoveredElement(double mouseX, double mouseY)
	{
		MainWindow window = Minecraft.getInstance().getMainWindow();
		for (HUDElement element : ElementRegistry.getElements())
		{
			ResourceLocation key = ElementRegistry.getKey(element);
			Vector2f position = currentOverrides.getOrDefault(key, ElementRegistry.getPosition(key));

			int xPos = (int) (window.getScaledWidth() * position.x);
			int yPos = (int) (window.getScaledHeight() * position.y);

			if (mouseX < xPos || mouseX > xPos + element.getWidth())
				continue;

			if (mouseY < yPos || mouseY > yPos + element.getHeight())
				continue;

			return element;
		}

		return null;
	}

	protected Point getBoundedDrag(MainWindow window, double mouseX, double mouseY)
	{
		int drawX = (int) (mouseX - dragged.getWidth() / 2);
		if (drawX + dragged.getWidth() >= window.getScaledWidth())
			drawX = window.getScaledWidth() - dragged.getWidth();
		if (drawX < 0)
			drawX = 0;

		int drawY = (int) (mouseY - dragged.getHeight() / 2);
		if (drawY + dragged.getHeight() >= window.getScaledHeight())
			drawY = window.getScaledHeight() - dragged.getHeight();
		if (drawY < 0)
			drawY = 0;

		return new Point(drawX, drawY);
	}

	protected void drawWithBox(MatrixStack matrixStack, HUDElement element, float partialTicks, int drawX, int drawY)
	{
		int color = ElementRegistry.getColor(ElementRegistry.getKey(element));
		matrixStack.push();
//		GlStateManager.enableAlpha();
//		GlStateManager.enableBlend();
//		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

		this.vLine(matrixStack, drawX, drawY, drawY + element.getHeight() - 1, color);
		this.vLine(matrixStack, drawX + element.getWidth() - 1, drawY, drawY + element.getHeight() - 1, color);
		this.hLine(matrixStack, drawX, drawX + element.getWidth() - 1, drawY, color);
		this.hLine(matrixStack, drawX, drawX + element.getWidth() - 1, drawY + element.getHeight() - 1, color);
//		GlStateManager.disableBlend();
//		GlStateManager.disableAlpha();
		matrixStack.pop();
//		GlStateManager.color(1.0F, 1.0F, 1.0F);
//		GlStateManager.enableTexture2D();
		element.draw(matrixStack, partialTicks, drawX, drawY);
//		GlStateManager.disableTexture2D();
	}
}
