package wayoftime.bloodmagic.client.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.tile.container.ContainerMasterRoutingNode;
import wayoftime.bloodmagic.tile.routing.TileMasterRoutingNode;

public class ScreenMasterRoutingNode extends ScreenBase<ContainerMasterRoutingNode>
{
	private static final ResourceLocation background = new ResourceLocation(BloodMagic.MODID, "textures/gui/masterroutingnode.png");

	public TileMasterRoutingNode tileMasterRoutingNode;

	private int left, top;

	public ScreenMasterRoutingNode(ContainerMasterRoutingNode container, PlayerInventory playerInventory, ITextComponent title)
	{
		super(container, playerInventory, title);
		tileMasterRoutingNode = container.tileMasterRoutingNode;
		this.xSize = 176;
		this.ySize = 121;
	}

	@Override
	public ResourceLocation getBackground()
	{
		return background;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack stack, int mouseX, int mouseY)
	{
		this.font.func_243248_b(stack, new TranslationTextComponent("tile.bloodmagic.masterroutingnode.name"), 8, 5, 4210752);
		this.font.func_243248_b(stack, new TranslationTextComponent("container.inventory"), 8, 27, 4210752);
	}

	@Override
	public void init()
	{
		super.init();
		left = (this.width - this.xSize) / 2;
		top = (this.height - this.ySize) / 2;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		getMinecraft().getTextureManager().bindTexture(background);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.blit(stack, i, j, 0, 0, this.xSize, this.ySize);

	}

}