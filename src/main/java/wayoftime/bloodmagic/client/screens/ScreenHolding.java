package wayoftime.bloodmagic.client.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.item.inventory.ContainerHolding;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilHolding;

public class ScreenHolding extends ScreenBase<ContainerHolding>
{
	private static final ResourceLocation background = BloodMagic.rl("gui/sigilholding.png");
	public IInventory tileTable;
	private PlayerEntity player;

	public ScreenHolding(ContainerHolding container, PlayerInventory playerInventory, ITextComponent title)
	{
		super(container, playerInventory, title);
		tileTable = container.inventoryHolding;
		xSize = 176;
		ySize = 121;
		this.player = playerInventory.player;
	}

	@Override
	public ResourceLocation getBackground()
	{
		return background;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack stack, int mouseX, int mouseY)
	{
//		this.font.func_243248_b(stack, new TranslationTextComponent("tile.bloodmagic.alchemytable.name"), 8, 5, 4210752);
//		this.font.func_243248_b(stack, new TranslationTextComponent("container.inventory"), 8, 111, 4210752);
		this.font.func_243248_b(stack, new TranslationTextComponent("item.bloodmagic.sigilofholding"), 53, 4, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		getMinecraft().getTextureManager().bindTexture(background);
//		int i = (this.width - this.xSize) / 2;
//		int j = (this.height - this.ySize) / 2;
//		this.blit(stack, i, j, 0, 0, this.xSize, this.ySize);
//
//		int l = this.getCookProgressScaled(90);
//		this.blit(stack, i + 115, j + 14 + 90 - l, 176, 90 - l, 18, l);
//
//		for (int slotId = 0; slotId < 6; slotId++)
//		{
//			if (!((TileAlchemyTable) tileTable).isInputSlotAccessible(slotId))
//			{
//				Slot slot = this.getContainer().getSlot(slotId);
//
//				this.blit(stack, i + slot.xPos, j + slot.yPos, 195, 1, 16, 16);
//			}
//		}

		// draw your Gui here, only thing you need to change is the path
//        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//        this.mc.getTextureManager().bindTexture(texture);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.blit(stack, x, y, 0, 0, xSize, ySize);
		ItemStack held = player.getHeldItem(Hand.MAIN_HAND);
		if (!held.isEmpty() && held.getItem() == BloodMagicItems.HOLDING_SIGIL.get())
		{
//            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.blit(stack, 4 + x + 36 * ItemSigilHolding.getCurrentItemOrdinal(player.getHeldItemMainhand()), y + 13, 0, 123, 24, 24);
		}
	}

//

}