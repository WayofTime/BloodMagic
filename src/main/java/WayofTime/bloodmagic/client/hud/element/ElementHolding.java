package WayofTime.bloodmagic.client.hud.element;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.Sprite;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.item.sigil.ItemSigilHolding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class ElementHolding extends HUDElement {

    private static final Sprite HOLDING_BAR = new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 0, 0, 102, 22);
    private static final Sprite SELECTED_OVERLAY = new Sprite(new ResourceLocation(BloodMagic.MODID, "textures/gui/widgets.png"), 0, 22, 24, 24);

    public ElementHolding() {
        super(HOLDING_BAR.getTextureWidth(), HOLDING_BAR.getTextureHeight());
    }

    @Override
    public void draw(ScaledResolution resolution, float partialTicks, int drawX, int drawY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        HOLDING_BAR.draw(drawX, drawY);

        Minecraft minecraft = Minecraft.getMinecraft();
        ItemStack sigilHolding = minecraft.player.getHeldItemMainhand();
        // Check mainhand for Sigil of Holding
        if (!(sigilHolding.getItem() == RegistrarBloodMagicItems.SIGIL_HOLDING))
            sigilHolding = minecraft.player.getHeldItemOffhand();
        // Check offhand for Sigil of Holding
        if (!(sigilHolding.getItem() == RegistrarBloodMagicItems.SIGIL_HOLDING))
            return;

        int currentSlot = ItemSigilHolding.getCurrentItemOrdinal(sigilHolding);
        SELECTED_OVERLAY.draw(drawX - 1 + (currentSlot * 20), drawY - 1);

        RenderHelper.enableGUIStandardItemLighting();
        List<ItemStack> inventory = ItemSigilHolding.getInternalInventory(sigilHolding);
        int xOffset = 0;
        for (ItemStack stack : inventory) {
            renderHotbarItem(drawX + 3 + xOffset, drawY + 3, partialTicks, minecraft.player, stack);
            xOffset += 20;
        }
    }

    @Override
    public boolean shouldRender(Minecraft minecraft) {
        ItemStack sigilHolding = minecraft.player.getHeldItemMainhand();
        // Check mainhand for Sigil of Holding
        if (!(sigilHolding.getItem() == RegistrarBloodMagicItems.SIGIL_HOLDING))
            sigilHolding = minecraft.player.getHeldItemOffhand();
        // Check offhand for Sigil of Holding
        if (!(sigilHolding.getItem() == RegistrarBloodMagicItems.SIGIL_HOLDING))
            return false;

        return true;
    }

    protected void renderHotbarItem(int x, int y, float partialTicks, EntityPlayer player, ItemStack stack) {
        if (!stack.isEmpty()) {
            float animation = (float) stack.getAnimationsToGo() - partialTicks;

            if (animation > 0.0F) {
                GlStateManager.pushMatrix();
                float f1 = 1.0F + animation / 5.0F;
                GlStateManager.translate((float) (x + 8), (float) (y + 12), 0.0F);
                GlStateManager.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
                GlStateManager.translate((float) (-(x + 8)), (float) (-(y + 12)), 0.0F);
            }

            Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(player, stack, x, y);

            if (animation > 0.0F)
                GlStateManager.popMatrix();

            Minecraft.getMinecraft().getRenderItem().renderItemOverlays(Minecraft.getMinecraft().fontRenderer, stack, x, y);
        }
    }
}
