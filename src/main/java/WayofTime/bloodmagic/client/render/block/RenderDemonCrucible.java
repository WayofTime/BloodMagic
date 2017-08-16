package WayofTime.bloodmagic.client.render.block;

import WayofTime.bloodmagic.tile.TileDemonCrucible;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RenderDemonCrucible extends TileEntitySpecialRenderer<TileDemonCrucible> {
    public static Minecraft mc = Minecraft.getMinecraft();
    public static ResourceLocation resource = new ResourceLocation("bloodmagic", "textures/blocks/lifeEssenceStill.png");
    public static float minHeight = 0.6497f;
    public static float maxHeight = 0.79f;

    @Override
    public void render(TileDemonCrucible tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        ItemStack inputStack = tile.getStackInSlot(0);

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        this.renderItem(tile.getWorld(), inputStack, partialTicks);
        GlStateManager.popMatrix();
    }

    private void renderItem(World world, ItemStack stack, float partialTicks) {
        RenderItem itemRenderer = mc.getRenderItem();
        if (!stack.isEmpty()) {
            GlStateManager.translate(0.5, 1.5, 0.5);
            EntityItem entityitem = new EntityItem(world, 0.0D, 0.0D, 0.0D, stack);
            entityitem.getItem().setCount(1);
            entityitem.hoverStart = 0.0F;
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();

            float rotation = (float) (720.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);

            GlStateManager.rotate(rotation, 0.0F, 1.0F, 0);
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.pushAttrib();
            RenderHelper.enableStandardItemLighting();
            itemRenderer.renderItem(entityitem.getItem(), ItemCameraTransforms.TransformType.FIXED);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popAttrib();

            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }
}
