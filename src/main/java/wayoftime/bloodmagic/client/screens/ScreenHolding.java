package wayoftime.bloodmagic.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.container.item.ContainerHolding;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilHolding;

public class ScreenHolding extends ScreenBase<ContainerHolding>
{
	private static final ResourceLocation background = BloodMagic.rl("gui/sigilholding.png");
	public Container tileTable;
	private Player player;

	public ScreenHolding(ContainerHolding container, Inventory playerInventory, Component title)
	{
		super(container, playerInventory, title);
		tileTable = container.inventoryHolding;
		imageWidth = 176;
		imageHeight = 121;
		this.player = playerInventory.player;
	}

	@Override
	public ResourceLocation getBackground()
	{
		return background;
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY)
	{
//		this.font.draw(stack, new TranslationTextComponent("tile.bloodmagic.alchemytable.name"), 8, 5, 4210752);
//		this.font.draw(stack, new TranslationTextComponent("container.inventory"), 8, 111, 4210752);
		guiGraphics.drawString(this.font, Component.translatable("item.bloodmagic.sigilofholding"), 53, 4, 4210752, false);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY)
	{
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
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;
		guiGraphics.blit(background, x, y, 0, 0, imageWidth, imageHeight);
		ItemStack held = player.getItemInHand(InteractionHand.MAIN_HAND);
		if (!held.isEmpty() && held.getItem() == BloodMagicItems.HOLDING_SIGIL.get())
		{
//            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			guiGraphics.blit(background, 4 + x + 36 * ItemSigilHolding.getCurrentItemOrdinal(player.getMainHandItem()), y + 13, 0, 123, 24, 24);
		}
	}

//

}