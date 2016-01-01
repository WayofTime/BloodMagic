package WayofTime.bloodmagic.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import WayofTime.bloodmagic.tile.TileAltar;

public class RenderAltar extends TileEntitySpecialRenderer<TileAltar>
{
    public static Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void renderTileEntityAt(TileAltar tileAltar, double x, double y, double z, float partialTicks, int destroyStage)
    {
        ItemStack inputStack = tileAltar.getStackInSlot(0);

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        this.renderItem(tileAltar.getWorld(), inputStack, partialTicks);
        GlStateManager.popMatrix();
    }

    private void renderItem(World world, ItemStack stack, float partialTicks)
    {
        RenderItem itemRenderer = mc.getRenderItem();
        if (stack != null)
        {
            GlStateManager.translate(0.5, 1, 0.5);
            EntityItem entityitem = new EntityItem(world, 0.0D, 0.0D, 0.0D, stack);
            entityitem.getEntityItem().stackSize = 1;
            entityitem.hoverStart = 0.0F;
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();

            boolean fancyRender = mc.gameSettings.fancyGraphics;
            float rotation = fancyRender ? (float) (720.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL) : 0;

            if (fancyRender)
            {
                GlStateManager.rotate(rotation, 0.0F, 1.0F, 0);
            }

            GlStateManager.scale(0.5F, 0.5F, 0.5F);

            //TODO: Render the item non-fancy if requested.
            if (!itemRenderer.shouldRenderItemIn3D(entityitem.getEntityItem()))
            {
                GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            }

            GlStateManager.pushAttrib();
            RenderHelper.enableStandardItemLighting();
            itemRenderer.func_181564_a(entityitem.getEntityItem(), ItemCameraTransforms.TransformType.FIXED);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popAttrib();

            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }
}
