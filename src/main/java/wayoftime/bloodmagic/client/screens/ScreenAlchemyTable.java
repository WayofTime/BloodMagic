package wayoftime.bloodmagic.client.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.tile.TileAlchemyTable;
import wayoftime.bloodmagic.tile.container.ContainerAlchemyTable;

public class ScreenAlchemyTable extends ScreenBase<ContainerAlchemyTable>
{
	private static final ResourceLocation background = new ResourceLocation(BloodMagic.MODID, "textures/gui/alchemytable.png");
	public IInventory tileTable;

	public ScreenAlchemyTable(ContainerAlchemyTable container, PlayerInventory playerInventory, ITextComponent title)
	{
		super(container, playerInventory, title);
		tileTable = container.tileTable;
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
//		super(new ContainerAlchemyTable(playerInventory, tileSoulForge));
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
		this.font.func_243248_b(stack, new TranslationTextComponent("tile.bloodmagic.alchemytable.name"), 8, 5, 4210752);
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

		int l = this.getCookProgressScaled(90);
		this.blit(stack, i + 115, j + 14 + 90 - l, 176, 90 - l, 18, l);

		for (int slotId = 0; slotId < 6; slotId++)
		{
			if (!((TileAlchemyTable) tileTable).isInputSlotAccessible(slotId))
			{
				Slot slot = this.getContainer().getSlot(slotId);

				this.blit(stack, i + slot.xPos, j + slot.yPos, 195, 1, 16, 16);
			}
		}
	}

//
	public int getCookProgressScaled(int scale)
	{
		double progress = ((TileAlchemyTable) tileTable).getProgressForGui();
//		if (tileSoulForge != null)
//		{
//			System.out.println("Tile is NOT null");
//		}
//		double progress = ((float) this.container.data.get(0)) / ((float) this.container.data.get(1));
//		System.out.println(this.container.data.get(0));
		return (int) (progress * scale);
	}
}