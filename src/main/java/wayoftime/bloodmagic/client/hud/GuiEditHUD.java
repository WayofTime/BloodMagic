package wayoftime.bloodmagic.client.hud;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import wayoftime.bloodmagic.client.hud.element.HUDElement;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Map;

public class GuiEditHUD extends Screen
{
	private static final int LINE_COLOR = 0x2D2D2D;

	private final Screen parent;
	private final Map<ResourceLocation, Vec2> currentOverrides = Maps.newHashMap();
	private HUDElement dragged;
	public boolean changes;

	public GuiEditHUD(Screen parent)
	{
		super(Component.literal("Testing GuiEditHUD"));
		this.parent = parent;
	}

	@Override
	public void init()
	{
		super.init();

		addRenderableWidget(new Button(width / 2 - 155, height - 30, 70, 20, new TranslatableComponent("gui.bloodmagic.toggle"), b -> {
			Minecraft.getInstance().setScreen(parent);
		})
		{
			{
				active = false;
			}
		});
		addRenderableWidget(new Button(width / 2 - 75, height - 30, 70, 20, new TranslatableComponent("gui.bloodmagic.default"), b -> {
			currentOverrides.clear();
			ElementRegistry.resetPos();
			changes = false;
		}));
		addRenderableWidget(new Button(width / 2 + 5, height - 30, 70, 20, new TranslatableComponent("gui.bloodmagic.save"), b -> {
			ElementRegistry.save(currentOverrides);
			Minecraft.getInstance().setScreen(parent);
		}));
		addRenderableWidget(new Button(width / 2 + 90, height - 30, 70, 20, new TranslatableComponent("gui.bloodmagic.cancel"), b -> {
			currentOverrides.clear();
			Minecraft.getInstance().setScreen(parent);
		}));
	}

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);

//		ScaledResolution resolution = new ScaledResolution(Minecraft.getInstance());
		Window window = Minecraft.getInstance().getWindow();
		for (HUDElement element : ElementRegistry.getElements())
		{
			if (dragged == element)
				continue;

			ResourceLocation key = ElementRegistry.getKey(element);
			Vec2 position = currentOverrides.getOrDefault(key, ElementRegistry.getPosition(key));
			int xPos = (int) (window.getGuiScaledWidth() * position.x);
			int yPos = (int) (window.getGuiScaledHeight() * position.y);

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
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int state)
	{
		if (dragged != null)
		{
			Window window = Minecraft.getInstance().getWindow();
			Point bounded = getBoundedDrag(window, mouseX, mouseY);
			float xPos = (((float) bounded.x) / window.getGuiScaledWidth());
			float yPos = (((float) bounded.y) / window.getGuiScaledHeight());

			currentOverrides.put(ElementRegistry.getKey(dragged), new Vec2(xPos, yPos));
//			System.out.println("Size of overrides: " + currentOverrides.size());
			changes = true;
			dragged = null;
//			return super;
		}

		return super.mouseReleased(mouseX, mouseY, state);
	}

	@Nullable
	public HUDElement getHoveredElement(double mouseX, double mouseY)
	{
		Window window = Minecraft.getInstance().getWindow();
		for (HUDElement element : ElementRegistry.getElements())
		{
			ResourceLocation key = ElementRegistry.getKey(element);
			Vec2 position = currentOverrides.getOrDefault(key, ElementRegistry.getPosition(key));

			int xPos = (int) (window.getGuiScaledWidth() * position.x);
			int yPos = (int) (window.getGuiScaledHeight() * position.y);

			if (mouseX < xPos || mouseX > xPos + element.getWidth())
				continue;

			if (mouseY < yPos || mouseY > yPos + element.getHeight())
				continue;

			return element;
		}

		return null;
	}

	protected Point getBoundedDrag(Window window, double mouseX, double mouseY)
	{
		int drawX = (int) (mouseX - dragged.getWidth() / 2);
		if (drawX + dragged.getWidth() >= window.getGuiScaledWidth())
			drawX = window.getGuiScaledWidth() - dragged.getWidth();
		if (drawX < 0)
			drawX = 0;

		int drawY = (int) (mouseY - dragged.getHeight() / 2);
		if (drawY + dragged.getHeight() >= window.getGuiScaledHeight())
			drawY = window.getGuiScaledHeight() - dragged.getHeight();
		if (drawY < 0)
			drawY = 0;

		return new Point(drawX, drawY);
	}

	protected void drawWithBox(PoseStack matrixStack, HUDElement element, float partialTicks, int drawX, int drawY)
	{
		int color = ElementRegistry.getColor(ElementRegistry.getKey(element));
		matrixStack.pushPose();
//		GlStateManager.enableAlpha();
//		GlStateManager.enableBlend();
//		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

		this.vLine(matrixStack, drawX, drawY, drawY + element.getHeight() - 1, color);
		this.vLine(matrixStack, drawX + element.getWidth() - 1, drawY, drawY + element.getHeight() - 1, color);
		this.hLine(matrixStack, drawX, drawX + element.getWidth() - 1, drawY, color);
		this.hLine(matrixStack, drawX, drawX + element.getWidth() - 1, drawY + element.getHeight() - 1, color);
//		GlStateManager.disableBlend();
//		GlStateManager.disableAlpha();
		matrixStack.popPose();
//		GlStateManager.color(1.0F, 1.0F, 1.0F);
//		GlStateManager.enableTexture2D();
		element.draw(matrixStack, partialTicks, drawX, drawY);
//		GlStateManager.disableTexture2D();
	}
}
