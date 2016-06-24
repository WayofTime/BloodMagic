package WayofTime.bloodmagic.client.hud;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.item.sigil.ItemSigilHolding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import javax.annotation.Nullable;

public class HUDElementHolding extends HUDElement
{

    public HUDElementHolding()
    {
        super(0, 0, RenderGameOverlayEvent.ElementType.HOTBAR);
    }

    @Override
    public void render(Minecraft minecraft, ScaledResolution resolution, float partialTicks)
    {
        ItemStack sigilHolding = minecraft.thePlayer.getHeldItemMainhand();
        // TODO - Clean this mess
        // Check mainhand for Sigil of Holding
        if (sigilHolding == null)
            return;
        if (!(sigilHolding.getItem() instanceof ItemSigilHolding))
            sigilHolding = minecraft.thePlayer.getHeldItemOffhand();
        // Check offhand for Sigil of Holding
        if (sigilHolding == null)
            return;
        if (!(sigilHolding.getItem() instanceof ItemSigilHolding))
            return;

        Gui ingameGui = minecraft.ingameGUI;

        minecraft.getTextureManager().bindTexture(new ResourceLocation(Constants.Mod.MODID, "textures/gui/widgets.png"));
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        ingameGui.drawTexturedModalRect(resolution.getScaledWidth() / 2 + 100 + getXOffset(), resolution.getScaledHeight() - 22 + getYOffset(), 0, 0, 102, 22);
        int currentSlot = ItemSigilHolding.getCurrentItemOrdinal(sigilHolding);
        ingameGui.drawTexturedModalRect(resolution.getScaledWidth() / 2 + 99 + (currentSlot * 20) + getXOffset(), resolution.getScaledHeight() - 23 + getYOffset(), 0, 22, 24, 24);

        RenderHelper.enableGUIStandardItemLighting();
        ItemStack[] holdingInv = ItemSigilHolding.getInternalInventory(sigilHolding);
        int xOffset = 0;
        if (holdingInv != null)
        {
            for (ItemStack sigil : holdingInv)
            {
                renderHotbarItem(resolution.getScaledWidth() / 2 + 103 + xOffset + getXOffset(), resolution.getScaledHeight() - 18 + getYOffset(), partialTicks, minecraft.thePlayer, sigil);
                xOffset += 20;
            }
        }

        RenderHelper.disableStandardItemLighting();
    }

    @Override
    public boolean shouldRender(Minecraft minecraft)
    {
        return true;
    }

    protected void renderHotbarItem(int x, int y, float partialTicks, EntityPlayer player, @Nullable ItemStack stack)
    {
        if (stack != null)
        {
            float animation = (float) stack.animationsToGo - partialTicks;

            if (animation > 0.0F)
            {
                GlStateManager.pushMatrix();
                float f1 = 1.0F + animation / 5.0F;
                GlStateManager.translate((float) (x + 8), (float) (y + 12), 0.0F);
                GlStateManager.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
                GlStateManager.translate((float) (-(x + 8)), (float) (-(y + 12)), 0.0F);
            }

            Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(player, stack, x, y);

            if (animation > 0.0F)
                GlStateManager.popMatrix();

            Minecraft.getMinecraft().getRenderItem().renderItemOverlays(Minecraft.getMinecraft().fontRendererObj, stack, x, y);
        }
    }
}
