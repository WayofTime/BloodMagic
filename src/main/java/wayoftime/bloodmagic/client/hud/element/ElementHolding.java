package wayoftime.bloodmagic.client.hud.element;

import java.util.List;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.client.Sprite;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilHolding;

public class ElementHolding extends HUDElement
{
	private static final Sprite HOLDING_BAR = new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 0, 0, 102, 22);
	private static final Sprite SELECTED_OVERLAY = new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 0, 22, 24, 24);

	public ElementHolding()
	{
		super(HOLDING_BAR.getTextureWidth(), HOLDING_BAR.getTextureHeight());
	}

	@Override
	public void draw(GuiGraphics guiGraphics, float partialTicks, int drawX, int drawY)
	{
//		GlStateManager.color(1.0F, 1.0F, 1.0F);
		HOLDING_BAR.draw(guiGraphics, drawX, drawY);

		Minecraft minecraft = Minecraft.getInstance();
		ItemStack sigilHolding = minecraft.player.getMainHandItem();
		// Check mainhand for Sigil of Holding
		if (!(sigilHolding.getItem() == BloodMagicItems.HOLDING_SIGIL.get()))
			sigilHolding = minecraft.player.getOffhandItem();
		// Check offhand for Sigil of Holding
		if (!(sigilHolding.getItem() == BloodMagicItems.HOLDING_SIGIL.get()))
			return;

		int currentSlot = ItemSigilHolding.getCurrentItemOrdinal(sigilHolding);
		SELECTED_OVERLAY.draw(guiGraphics, drawX - 1 + (currentSlot * 20), drawY - 1);

		Lighting.setupForFlatItems();
//		Lighting.turnBackOn();
		List<ItemStack> inventory = ItemSigilHolding.getInternalInventory(sigilHolding);
		int xOffset = 0;
		for (ItemStack stack : inventory)
		{
			renderHotbarItem(guiGraphics, drawX + 3 + xOffset, drawY + 3, partialTicks, minecraft.player, stack);
			xOffset += 20;
		}
	}

	@Override
	public boolean shouldRender(Minecraft minecraft)
	{
		ItemStack sigilHolding = minecraft.player.getMainHandItem();
		// Check mainhand for Sigil of Holding
		if (!(sigilHolding.getItem() == BloodMagicItems.HOLDING_SIGIL.get()))
			sigilHolding = minecraft.player.getOffhandItem();
		// Check offhand for Sigil of Holding
		if (!(sigilHolding.getItem() == BloodMagicItems.HOLDING_SIGIL.get()))
			return false;

		return true;
	}

	protected void renderHotbarItem(GuiGraphics guiGraphics, int x, int y, float partialTicks, Player player, ItemStack stack)
	{
		if (!stack.isEmpty())
		{
			float animation = (float) stack.getPopTime() - partialTicks;

			PoseStack poseStack = guiGraphics.pose();
			if (animation > 0.0F)
			{
				poseStack.pushPose();
				float f1 = 1.0F + animation / 5.0F;
				poseStack.translate((float) (x + 8), (float) (y + 12), 0.0F);
				poseStack.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
				poseStack.translate((float) (-(x + 8)), (float) (-(y + 12)), 0.0F);
//				RenderSystem.translatef((float) (x + 8), (float) (y + 12), 0.0F);
//				RenderSystem.scalef(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
//				RenderSystem.translatef((float) (-(x + 8)), (float) (-(y + 12)), 0.0F);
			}
			guiGraphics.renderItem(player, stack, x, y, 1);

			if (animation > 0.0F)
				poseStack.popPose();

			guiGraphics.renderItemDecorations(Minecraft.getInstance().font, stack, x, y);
		}
	}

//	   private void drawItemStack(ItemStack stack, int x, int y, String altText) {
//		      RenderSystem.translatef(0.0F, 0.0F, 32.0F);
//		      this.setBlitOffset(200);
//		      this.itemRenderer.zLevel = 200.0F;
//		      net.minecraft.client.gui.FontRenderer font = stack.getItem().getFontRenderer(stack);
//		      if (font == null) font = this.font;
//		      this.itemRenderer.renderItemAndEffectIntoGUI(stack, x, y);
//		      this.itemRenderer.renderItemOverlayIntoGUI(font, stack, x, y - (this.draggedStack.isEmpty() ? 0 : 8), altText);
//		      this.setBlitOffset(0);
//		      this.itemRenderer.zLevel = 0.0F;
//		   }
}