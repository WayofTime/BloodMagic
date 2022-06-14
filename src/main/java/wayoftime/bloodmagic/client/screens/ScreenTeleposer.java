package wayoftime.bloodmagic.client.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.tile.TileTeleposer;
import wayoftime.bloodmagic.tile.container.ContainerTeleposer;

public class ScreenTeleposer extends ScreenBase<ContainerTeleposer>
{
	private static final ResourceLocation background = new ResourceLocation(BloodMagic.MODID, "textures/gui/teleposer.png");

	public TileTeleposer tileTeleposer;

	private int left, top;

	public ScreenTeleposer(ContainerTeleposer container, PlayerInventory playerInventory, ITextComponent title)
	{
		super(container, playerInventory, title);
		tileTeleposer = container.tileTeleposer;
		this.imageWidth = 176;
		this.imageHeight = 121;
	}

	@Override
	public ResourceLocation getBackground()
	{
		return background;
	}

	@Override
	protected void renderLabels(MatrixStack stack, int mouseX, int mouseY)
	{
		this.font.draw(stack, new TranslationTextComponent("tile.bloodmagic.teleposer.name"), 8, 5, 4210752);
		this.font.draw(stack, new TranslationTextComponent("container.inventory"), 8, 27, 4210752);
	}

	@Override
	public void init()
	{
		super.init();
		left = (this.width - this.imageWidth) / 2;
		top = (this.height - this.imageHeight) / 2;
	}

	@Override
	protected void renderBg(MatrixStack stack, float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		getMinecraft().getTextureManager().bind(background);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(stack, i, j, 0, 0, this.imageWidth, this.imageHeight);

	}

}