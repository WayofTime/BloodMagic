package wayoftime.bloodmagic.client.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import wayoftime.bloodmagic.BloodMagic;

public abstract class ScreenBase<T extends AbstractContainerMenu> extends AbstractContainerScreen<T>
{
	private static final ResourceLocation background = new ResourceLocation(BloodMagic.MODID, "textures/gui/soulforge.png");

	protected final T container;

	public ScreenBase(T container, Inventory playerInventory, Component title)
	{
		super(container, playerInventory, title);
		this.container = container;
	}

	public ResourceLocation getBackground()
	{
		return background;
	}


	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);

		this.renderTooltip(guiGraphics, mouseX, mouseY); // @mcp: renderTooltip = renderHoveredToolTip
//		if (mouseX > (guiLeft + 7) && mouseX < (guiLeft + 7) + 18 && mouseY > (guiTop + 7)
//				&& mouseY < (guiTop + 7) + 73)
//			this.renderTooltip(stack, LanguageMap.getInstance().getVisualOrder(Arrays.asList(new TranslationTextComponent("screen.diregoo.energy", MagicHelpers.withSuffix(this.container.getEnergy()), MagicHelpers.withSuffix(this.container.getMaxPower())))), mouseX, mouseY);
	}

	@Override
	public void init()
	{
		super.init();
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY)
	{
//		RenderSystem.color4f(1, 1, 1, 1);
//		getMinecraft().getTextureManager().bindTexture(getBackground());
//		this.blit(stack, guiLeft, guiTop, 0, 0, xSize, ySize);

//		int maxEnergy = this.container.getMaxPower(), height = 70;
//		if (maxEnergy > 0)
//		{
//			int remaining = (this.container.getEnergy() * height) / maxEnergy;
//			this.blit(stack, guiLeft + 8, guiTop + 78 - remaining, 176, 84 - remaining, 16, remaining + 1);
//		}
	}

//
	protected static Component getTrans(String key, Object... args)
	{
		return Component.translatable(BloodMagic.MODID + "." + key, args);
	}
}
