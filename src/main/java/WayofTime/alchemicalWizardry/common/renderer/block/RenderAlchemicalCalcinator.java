package WayofTime.alchemicalWizardry.common.renderer.block;

import WayofTime.alchemicalWizardry.common.tileEntity.TEAlchemicalCalcinator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentContainerInfo;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentStack;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelAlchemicalCalcinator;

public class RenderAlchemicalCalcinator extends TileEntitySpecialRenderer
{
    private final RenderItem customRenderItem;
    private ModelAlchemicalCalcinator modelConduit = new ModelAlchemicalCalcinator();

    private ResourceLocation resourceLocation = new ResourceLocation("alchemicalwizardry:textures/models/Reagent.png");

    public RenderAlchemicalCalcinator()
    {
        customRenderItem = Minecraft.getMinecraft().getRenderItem();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double d0, double d1, double d2, float f, int i)
    {
        if (tileEntity instanceof TEAlchemicalCalcinator)
        {
            TEAlchemicalCalcinator tileAltar = (TEAlchemicalCalcinator) tileEntity;

            GL11.glPushMatrix();
            GL11.glTranslatef((float) d0 + 0.5F, (float) d1 + 1.5F, (float) d2 + 0.5F);
            ResourceLocation test = new ResourceLocation("alchemicalwizardry:textures/models/AlchemicalCalcinator.png");
            FMLClientHandler.instance().getClient().renderEngine.bindTexture(test);
            GL11.glPushMatrix();
            GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
            this.modelConduit.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
            GL11.glPopMatrix();
            GL11.glPopMatrix();

            GL11.glPushMatrix();

            if (tileAltar.getStackInSlot(1) != null)
            {
                float scaleFactor = getGhostItemScaleFactor(tileAltar.getStackInSlot(1));
                EntityItem ghostEntityItem = new EntityItem(tileAltar.getWorld());
                ghostEntityItem.hoverStart = 0.0F;
                ghostEntityItem.setEntityItemStack(tileAltar.getStackInSlot(1));
                float displacement = 0.2F;

                if (ghostEntityItem.getEntityItem().getItem() instanceof ItemBlock)
                {
                    GL11.glTranslatef((float) d0 + 0.5F, (float) d1 + displacement + 0.7F, (float) d2 + 0.5F);
                } else
                {
                    GL11.glTranslatef((float) d0 + 0.5F, (float) d1 + displacement + 10.4f / 16.0f, (float) d2 + 0.5F - 0.0625f * 2f);
                }
                GL11.glScalef(scaleFactor, scaleFactor, scaleFactor);

                if (!(ghostEntityItem.getEntityItem().getItem() instanceof ItemBlock))
                {
                    GL11.glRotatef(90f, 1.0f, 0.0f, 0.0F);
                }

                customRenderItem.func_175043_b(ghostEntityItem.getEntityItem()); //renderItemModel
            }


            GL11.glPopMatrix();
            GL11.glPushMatrix();

            if (tileAltar.getStackInSlot(0) != null)
            {
                float scaleFactor = getGhostItemScaleFactor(tileAltar.getStackInSlot(0));
                EntityItem ghostEntityItem = new EntityItem(tileAltar.getWorld());
                ghostEntityItem.hoverStart = 0.0F;
                ghostEntityItem.setEntityItemStack(tileAltar.getStackInSlot(0));
                float displacement = -0.5F;

                if (ghostEntityItem.getEntityItem().getItem() instanceof ItemBlock)
                {
                    GL11.glTranslatef((float) d0 + 0.5F, (float) d1 + displacement + 0.7F, (float) d2 + 0.5F);
                } else
                {
                    GL11.glTranslatef((float) d0 + 0.5F, (float) d1 + displacement + 10.4f / 16.0f, (float) d2 + 0.5F - 0.0625f * 2f);
                }
                GL11.glScalef(scaleFactor, scaleFactor, scaleFactor);

                if (!(ghostEntityItem.getEntityItem().getItem() instanceof ItemBlock))
                {
                    GL11.glRotatef(90f, 1.0f, 0.0f, 0.0F);
                }

                customRenderItem.func_175043_b(ghostEntityItem.getEntityItem()); //renderItemModel
            }

            GL11.glPopMatrix();


            ReagentContainerInfo[] info = tileAltar.getContainerInfo(EnumFacing.UP);
            if (info.length >= 1 && info[0] != null)
            {
                ReagentStack reagentStack = info[0].reagent;
                int capacity = info[0].capacity;
                if (reagentStack != null && reagentStack.reagent != null)
                {
                    Reagent reagent = reagentStack.reagent;
                    this.renderTankContents(d0, d1, d2, reagent.getColourRed(), reagent.getColourGreen(), reagent.getColourBlue(), 200 * reagentStack.amount / capacity);
                }
            }
        }
    }

    private void renderTankContents(double x, double y, double z, int colourRed, int colourGreen, int colourBlue, int colourIntensity)
    {
        GL11.glPushMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        this.bindTexture(resourceLocation);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glDepthMask(false);

        WorldRenderer wr = tessellator.getWorldRenderer();
        wr.startDrawingQuads();
        wr.func_178961_b(colourRed, colourGreen, colourBlue, colourIntensity); //setCoulourRGBA
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
        wr.func_178963_b(240); //setBrightness

        double x1 = -7d / 16d;
        double x2 = 7d / 16d;
        double y1 = 1d / 16d;
        double y2 = 5d / 16d;
        double z1 = -7d / 16d;
        double z2 = 7d / 16d;

        double resx1 = 0.0d;
        double resx2 = 0.0d;
        double resy1 = 1.0d;
        double resy2 = 1.0d;

        wr.addVertexWithUV(x1, y1, z1, resx1, resy1);
        wr.addVertexWithUV(x2, y1, z1, resx2, resy1);
        wr.addVertexWithUV(x2, y2, z1, resx2, resy2);
        wr.addVertexWithUV(x1, y2, z1, resx1, resy2);
        wr.addVertexWithUV(x1, y1, z1, resx1, resy1);
        wr.addVertexWithUV(x1, y1, z2, resx2, resy1);
        wr.addVertexWithUV(x1, y2, z2, resx2, resy2);
        wr.addVertexWithUV(x1, y2, z1, resx1, resy2);
        wr.addVertexWithUV(x1, y1, z2, resx1, resy1);
        wr.addVertexWithUV(x2, y1, z2, resx2, resy1);
        wr.addVertexWithUV(x2, y2, z2, resx2, resy2);
        wr.addVertexWithUV(x1, y2, z2, resx1, resy2);
        wr.addVertexWithUV(x2, y1, z1, resx1, resy1);
        wr.addVertexWithUV(x2, y1, z2, resx2, resy1);
        wr.addVertexWithUV(x2, y2, z2, resx2, resy2);
        wr.addVertexWithUV(x2, y2, z1, resx1, resy2);
        tessellator.draw();

        GL11.glDepthMask(true);

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        GL11.glPopMatrix();
    }

    private float getGhostItemScaleFactor(ItemStack itemStack)
    {
        float scaleFactor = 1.5F;

        if (itemStack != null)
        {
            if (itemStack.getItem() instanceof ItemBlock)
            {
            	return 0.90F * scaleFactor;
            } else
            {
                return 0.65F * scaleFactor;
            }
        }

        return scaleFactor;
    }
}