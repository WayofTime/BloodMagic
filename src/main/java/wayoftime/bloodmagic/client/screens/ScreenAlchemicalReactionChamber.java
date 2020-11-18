package wayoftime.bloodmagic.client.screens;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.tile.TileAlchemicalReactionChamber;
import wayoftime.bloodmagic.tile.container.ContainerAlchemicalReactionChamber;
import wayoftime.bloodmagic.util.handler.event.ClientHandler;

public class ScreenAlchemicalReactionChamber extends ScreenBase<ContainerAlchemicalReactionChamber>
{
	private static final ResourceLocation background = new ResourceLocation(BloodMagic.MODID, "textures/gui/arc_gui.png");
	public TileAlchemicalReactionChamber tileARC;

	public ScreenAlchemicalReactionChamber(ContainerAlchemicalReactionChamber container, PlayerInventory playerInventory, ITextComponent title)
	{
		super(container, playerInventory, title);
		tileARC = container.tileARC;
		this.xSize = 176;
		this.ySize = 205;
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
	public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks)
	{
		super.render(stack, mouseX, mouseY, partialTicks);
		List<ITextComponent> tooltip = new ArrayList<>();
//		FluidTank inputTank = new FluidTank(FluidAttributes.BUCKET_VOLUME * 2);
//		inputTank.fill(new FluidStack(Fluids.WATER, 1000), FluidAction.EXECUTE);

		ClientHandler.handleGuiTank(stack, tileARC.inputTank, this.guiLeft + 8, this.guiTop
				+ 40, 16, 63, 194, 1, 16, 63, mouseX, mouseY, background.toString(), tooltip);
		ClientHandler.handleGuiTank(stack, tileARC.outputTank, this.guiLeft + 152, this.guiTop
				+ 15, 16, 63, 194, 1, 16, 63, mouseX, mouseY, background.toString(), tooltip);

		if (!tooltip.isEmpty())
			GuiUtils.drawHoveringText(stack, tooltip, mouseX, mouseY, width, height, -1, font);
	}

//
	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack stack, int mouseX, int mouseY)
	{
		this.font.func_243248_b(stack, new TranslationTextComponent("tile.bloodmagic.arc.name"), 8, 5, 4210752);
		this.font.func_243248_b(stack, new TranslationTextComponent("container.inventory"), 8, 111, 4210752);
	}

//
	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		getMinecraft().getTextureManager().bindTexture(background);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.blit(stack, i, j, 0, 0, this.xSize, this.ySize);

//		FluidTank inputTank = new FluidTank(FluidAttributes.BUCKET_VOLUME * 2);
//		inputTank.fill(new FluidStack(Fluids.WATER, 1000), FluidAction.EXECUTE);

		ClientHandler.handleGuiTank(stack, tileARC.inputTank, this.guiLeft + 8, this.guiTop
				+ 40, 16, 63, 194, 1, 16, 63, mouseX, mouseY, background.toString(), null);
		ClientHandler.handleGuiTank(stack, tileARC.outputTank, this.guiLeft + 152, this.guiTop
				+ 15, 16, 63, 194, 1, 16, 63, mouseX, mouseY, background.toString(), null);

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