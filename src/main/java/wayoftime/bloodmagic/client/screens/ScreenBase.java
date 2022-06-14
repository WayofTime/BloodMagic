package wayoftime.bloodmagic.client.screens;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import wayoftime.bloodmagic.BloodMagic;

public abstract class ScreenBase<T extends Container> extends ContainerScreen<T>
{
	private static final ResourceLocation background = new ResourceLocation(BloodMagic.MODID, "textures/gui/soulforge.png");

	protected final T container;

	public ScreenBase(T container, PlayerInventory playerInventory, ITextComponent title)
	{
		super(container, playerInventory, title);
		this.container = container;
	}

	public ResourceLocation getBackground()
	{
		return background;
	}

	@Override
	public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(stack);
		super.render(stack, mouseX, mouseY, partialTicks);

		this.renderTooltip(stack, mouseX, mouseY); // @mcp: renderTooltip = renderHoveredToolTip
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
	protected void renderBg(MatrixStack stack, float partialTicks, int mouseX, int mouseY)
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
	protected static TranslationTextComponent getTrans(String key, Object... args)
	{
		return new TranslationTextComponent(BloodMagic.MODID + "." + key, args);
	}
}
