package wayoftime.bloodmagic.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.container.tile.ContainerMasterRoutingNode;
import wayoftime.bloodmagic.common.tile.routing.TileMasterRoutingNode;

public class ScreenMasterRoutingNode extends ScreenBase<ContainerMasterRoutingNode>
{
	private static final ResourceLocation background = new ResourceLocation(BloodMagic.MODID, "textures/gui/masterroutingnode.png");

	public TileMasterRoutingNode tileMasterRoutingNode;

	private int left, top;

	public ScreenMasterRoutingNode(ContainerMasterRoutingNode container, Inventory playerInventory, Component title)
	{
		super(container, playerInventory, title);
		tileMasterRoutingNode = container.tileMasterRoutingNode;
		this.imageWidth = 176;
		this.imageHeight = 121;
	}

	@Override
	public ResourceLocation getBackground()
	{
		return background;
	}

	@Override
	protected void renderLabels(PoseStack stack, int mouseX, int mouseY)
	{
		this.font.draw(stack, new TranslatableComponent("tile.bloodmagic.masterroutingnode.name"), 8, 5, 4210752);
		this.font.draw(stack, new TranslatableComponent("container.inventory"), 8, 27, 4210752);
	}

	@Override
	public void init()
	{
		super.init();
		left = (this.width - this.imageWidth) / 2;
		top = (this.height - this.imageHeight) / 2;
	}

	@Override
	protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		getMinecraft().getTextureManager().bindForSetup(background);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(stack, i, j, 0, 0, this.imageWidth, this.imageHeight);

	}

}