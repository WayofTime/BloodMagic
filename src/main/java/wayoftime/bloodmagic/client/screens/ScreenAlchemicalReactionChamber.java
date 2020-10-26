package wayoftime.bloodmagic.client.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.tile.TileAlchemicalReactionChamber;
import wayoftime.bloodmagic.tile.contailer.ContainerAlchemicalReactionChamber;

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
//	@Override
//	public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks)
//	{
//		this.drawDefaultBackground();
//		super.drawScreen(mouseX, mouseY, partialTicks);
//		this.renderHoveredToolTip(mouseX, mouseY);
//	}
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

//		int l = this.getCookProgressScaled(90);
//		this.blit(stack, i + 115, j + 14 + 90 - l, 176, 90 - l, 18, l);
	}

////
//	public int getCookProgressScaled(int scale)
//	{
//		double progress = ((TileSoulForge) tileSoulForge).getProgressForGui();
////		if (tileSoulForge != null)
////		{
////			System.out.println("Tile is NOT null");
////		}
////		double progress = ((float) this.container.data.get(0)) / ((float) this.container.data.get(1));
////		System.out.println(this.container.data.get(0));
//		return (int) (progress * scale);
//	}
}