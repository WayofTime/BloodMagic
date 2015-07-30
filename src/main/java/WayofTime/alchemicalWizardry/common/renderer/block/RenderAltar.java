package WayofTime.alchemicalWizardry.common.renderer.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.common.renderer.model.ModelBloodAltar;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;

public class RenderAltar extends TileEntitySpecialRenderer
{
    private ModelBloodAltar modelBloodAltar = new ModelBloodAltar();
    private final RenderItem customRenderItem;

    public RenderAltar()
    {
        customRenderItem = Minecraft.getMinecraft().getRenderItem();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double d0, double d1, double d2, float f, int i)
    {
        modelBloodAltar.renderBloodAltar((TEAltar) tileEntity, d0, d1, d2);
        modelBloodAltar.renderBloodLevel((TEAltar) tileEntity, d0, d1, d2);

        if (tileEntity instanceof TEAltar)
        {
            TEAltar tileAltar = (TEAltar) tileEntity;
            GL11.glPushMatrix();

            if (tileAltar.getStackInSlot(0) != null)
            {
                float scaleFactor = getGhostItemScaleFactor(tileAltar.getStackInSlot(0));
                float rotationAngle = Minecraft.getMinecraft().gameSettings.fancyGraphics ? (float) (720.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL) : 0;
                EntityItem ghostEntityItem = new EntityItem(tileAltar.getWorld());
                ghostEntityItem.hoverStart = 0.0F;
                ghostEntityItem.setEntityItemStack(tileAltar.getStackInSlot(0));
                float displacement = 0.2F;

                if (ghostEntityItem.getEntityItem().getItem() instanceof ItemBlock)
                {
                    GL11.glTranslatef((float) d0 + 0.5F, (float) d1 + displacement + 0.7F, (float) d2 + 0.5F);
                } else
                {
                    GL11.glTranslatef((float) d0 + 0.5F, (float) d1 + displacement + 0.6F, (float) d2 + 0.5F);
                }
                GL11.glScalef(scaleFactor, scaleFactor, scaleFactor);
                GL11.glRotatef(rotationAngle, 0.0F, 1.0F, 0.0F);
                customRenderItem.func_175043_b(ghostEntityItem.getEntityItem()); //renderItemModel
            }

            GL11.glPopMatrix();
        }
    }

    private float getGhostItemScaleFactor(ItemStack itemStack)
    {
        float scaleFactor = 1.0F;

        if (itemStack != null)
        {
            if (itemStack.getItem() instanceof ItemBlock)
            {
                return 0.9f;
            } else
            {
                return 0.65f;
            }
        }

        return scaleFactor;
    }
}
