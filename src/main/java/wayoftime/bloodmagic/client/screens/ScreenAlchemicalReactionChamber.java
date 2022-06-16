package wayoftime.bloodmagic.client.screens;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.container.tile.ContainerAlchemicalReactionChamber;
import wayoftime.bloodmagic.common.tile.TileAlchemicalReactionChamber;
import wayoftime.bloodmagic.util.handler.event.ClientHandler;

public class ScreenAlchemicalReactionChamber extends ScreenBase<ContainerAlchemicalReactionChamber>
{
	private static final ResourceLocation background = new ResourceLocation(BloodMagic.MODID, "textures/gui/arc_gui.png");
	public TileAlchemicalReactionChamber tileARC;

	public ScreenAlchemicalReactionChamber(ContainerAlchemicalReactionChamber container, Inventory playerInventory, Component title)
	{
		super(container, playerInventory, title);
		tileARC = container.tileARC;
		this.imageWidth = 176;
		this.imageHeight = 205;
	}

	@Override
	public ResourceLocation getBackground()
	{
		return background;
	}

//	public 

//	public ScreenSoulForge(InventoryPlayer playerInventory, IInventory tileSoulForge)
//	{
//		super(new ContainerSoulForge(playerInventory, tileSoulForge));
//		this.tileSoulForge = tileSoulForge;
//		this.xSize = 176;
//		this.ySize = 205;
//	}
//
	@Override
	public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks)
	{
		super.render(stack, mouseX, mouseY, partialTicks);
		List<Component> tooltip = new ArrayList<>();
//		FluidTank inputTank = new FluidTank(FluidAttributes.BUCKET_VOLUME * 2);
//		inputTank.fill(new FluidStack(Fluids.WATER, 1000), FluidAction.EXECUTE);

		ClientHandler.handleGuiTank(stack, tileARC.inputTank, this.leftPos + 8, this.topPos + 40, 16, 63, 194, 1, 16, 63, mouseX, mouseY, background.toString(), tooltip);
		ClientHandler.handleGuiTank(stack, tileARC.outputTank, this.leftPos + 152, this.topPos + 15, 16, 63, 194, 1, 16, 63, mouseX, mouseY, background.toString(), tooltip);

		if (!tooltip.isEmpty())
			this.renderTooltip(stack, tooltip, Optional.empty(), mouseX, mouseY, font);
//			GuiUtils.drawHoveringText(stack, tooltip, mouseX, mouseY, width, height, -1, font);
	}

//
	@Override
	protected void renderLabels(PoseStack stack, int mouseX, int mouseY)
	{
		this.font.draw(stack, new TranslatableComponent("tile.bloodmagic.arc.name"), 8, 5, 4210752);
		this.font.draw(stack, new TranslatableComponent("container.inventory"), 8, 111, 4210752);
	}

//
	@Override
	protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		getMinecraft().getTextureManager().bindForSetup(background);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(stack, i, j, 0, 0, this.imageWidth, this.imageHeight);

//		FluidTank inputTank = new FluidTank(FluidAttributes.BUCKET_VOLUME * 2);
//		inputTank.fill(new FluidStack(Fluids.WATER, 1000), FluidAction.EXECUTE);

		ClientHandler.handleGuiTank(stack, tileARC.inputTank, this.leftPos + 8, this.topPos + 40, 16, 63, 194, 1, 16, 63, mouseX, mouseY, background.toString(), null);
		ClientHandler.handleGuiTank(stack, tileARC.outputTank, this.leftPos + 152, this.topPos + 15, 16, 63, 194, 1, 16, 63, mouseX, mouseY, background.toString(), null);

		int w = this.getCookProgressScaled(38);
//		FurnaceTileEntity d;
		this.blit(stack, i + 63, j + 44, 176, 90, w, 23);
	}

////
	public int getCookProgressScaled(int scale)
	{
		double progress = ((TileAlchemicalReactionChamber) tileARC).getProgressForGui();
//		if (tileSoulForge != null)
//		{
//			System.out.println("Tile is NOT null");
//		}
//		double progress = ((float) this.container.data.get(0)) / ((float) this.container.data.get(1));
//		System.out.println(this.container.data.get(0));
		return (int) (progress * scale);
	}
}