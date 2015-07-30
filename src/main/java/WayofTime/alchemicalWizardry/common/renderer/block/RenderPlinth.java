package WayofTime.alchemicalWizardry.common.renderer.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.common.renderer.model.ModelPlinth;
import WayofTime.alchemicalWizardry.common.tileEntity.TEPlinth;

public class RenderPlinth extends TileEntitySpecialRenderer
{
    private ModelPlinth modelPlinth = new ModelPlinth();
    private final RenderItem customRenderItem;

    public RenderPlinth()
    {
        customRenderItem = Minecraft.getMinecraft().getRenderItem();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double d0, double d1, double d2, float f, int i)
    {
        if (tileEntity instanceof TEPlinth)
        {
            TEPlinth tileAltar = (TEPlinth) tileEntity;
            GL11.glPushMatrix();
            GL11.glTranslatef((float) d0 + 0.5F, (float) d1 + 1.5F, (float) d2 + 0.5F);
            ResourceLocation test = new ResourceLocation("alchemicalwizardry:textures/models/Plinth.png");
            FMLClientHandler.instance().getClient().renderEngine.bindTexture(test);
            GL11.glPushMatrix();
            GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
            this.modelPlinth.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
            GL11.glPopMatrix();
            GL11.glPopMatrix();
            GL11.glPushMatrix();

            if (tileAltar.getStackInSlot(0) != null)
            {
                float scaleFactor = getGhostItemScaleFactor(tileAltar.getStackInSlot(0));
                EntityItem ghostEntityItem = new EntityItem(tileAltar.getWorld());
                ghostEntityItem.hoverStart = 0.0F;
                ghostEntityItem.setEntityItemStack(tileAltar.getStackInSlot(0));
                float displacement = 0.2F;

                if (ghostEntityItem.getEntityItem().getItem() instanceof ItemBlock)
                {
                    GL11.glTranslatef((float) d0 + 0.5F, (float) d1 + displacement + 0.7F, (float) d2 + 0.5F);
                } else
                {
                    GL11.glTranslatef((float) d0 + 0.5F, (float) d1 + displacement + 10.4f / 16.0f, (float) d2 + 0.5F - 0.1875f);
                }
                GL11.glScalef(scaleFactor, scaleFactor, scaleFactor);

                if (!(ghostEntityItem.getEntityItem().getItem() instanceof ItemBlock))
                {
                    GL11.glRotatef(90f, 1.0f, 0.0f, 0.0F);
                }

                customRenderItem.func_175043_b(ghostEntityItem.getEntityItem()); //renderItemModel
            }

            GL11.glPopMatrix();
        }
    }

    private float getGhostItemScaleFactor(ItemStack itemStack)
    {
        float scaleFactor = 2.0F / 0.9F;

        if (itemStack != null)
        {
            if (itemStack.getItem() instanceof ItemBlock)
            {
                return 0.9f * scaleFactor;
            } else
            {
                return 0.65f * scaleFactor;
            }
        }

        return scaleFactor;
    }
}
