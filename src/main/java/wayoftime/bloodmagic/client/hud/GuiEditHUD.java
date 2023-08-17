package wayoftime.bloodmagic.client.hud;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
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
		Button btn = Button.builder(Component.translatable("gui.bloodmagic.toggle"), b -> Minecraft.getInstance().setScreen(parent)).pos(width / 2 - 155, height - 30).size(70, 20).build();
		btn.active = false;
		addRenderableWidget(btn);

		addRenderableWidget(Button.builder(Component.translatable("gui.bloodmagic.default"), b -> {
			currentOverrides.clear();
			ElementRegistry.resetPos();
			changes = false;
		}).pos(width / 2 - 75, height - 30).size(70, 20).build());

		addRenderableWidget(Button.builder(Component.translatable("gui.bloodmagic.save"), b -> {
			ElementRegistry.save(currentOverrides);
			Minecraft.getInstance().setScreen(parent);
		}).pos(width / 2 + 5, height - 30).size(70, 20).build());

		addRenderableWidget(Button.builder(Component.translatable("gui.bloodmagic.cancel"), b -> {
			currentOverrides.clear();
			Minecraft.getInstance().setScreen(parent);
		}).pos(width / 2 + 90, height - 30).size(70, 20).build());
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);

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

			drawWithBox(guiGraphics, element, partialTicks, xPos, yPos);
		}

		if (dragged != null)
		{
			Point bounded = getBoundedDrag(window, mouseX, mouseY);
			drawWithBox(guiGraphics, dragged, partialTicks, bounded.x, bounded.y);
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

	protected void drawWithBox(GuiGraphics guiGraphics, HUDElement element, float partialTicks, int drawX, int drawY)
	{
		int color = ElementRegistry.getColor(ElementRegistry.getKey(element));
		guiGraphics.pose().pushPose();

		guiGraphics.vLine( drawX, drawY, drawY + element.getHeight() - 1, color);
		guiGraphics.vLine( drawX + element.getWidth() - 1, drawY, drawY + element.getHeight() - 1, color);
		guiGraphics.hLine( drawX, drawX + element.getWidth() - 1, drawY, color);
		guiGraphics.hLine( drawX, drawX + element.getWidth() - 1, drawY + element.getHeight() - 1, color);

		guiGraphics.pose().popPose();
		element.draw(guiGraphics, partialTicks, drawX, drawY);
	}
}
