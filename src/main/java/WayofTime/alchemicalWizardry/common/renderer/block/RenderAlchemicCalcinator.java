package WayofTime.alchemicalWizardry.common.renderer.block;

import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentContainerInfo;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentStack;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelAlchemicalCalcinator;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAlchemicalCalcinator;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class RenderAlchemicCalcinator extends TileEntitySpecialRenderer
{
    private final RenderItem customRenderItem;
    private ModelAlchemicalCalcinator modelConduit = new ModelAlchemicalCalcinator();

    private ResourceLocation resourceLocation = new ResourceLocation("alchemicalwizardry:textures/models/Reagent.png");

    public RenderAlchemicCalcinator()
    {
        customRenderItem = new RenderItem()
        {
            @Override
            public boolean shouldBob()
            {
                return false;
            }
        };
        customRenderItem.setRenderManager(RenderManager.instance);
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
                EntityItem ghostEntityItem = new EntityItem(tileAltar.getWorldObj());
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

                customRenderItem.doRender(ghostEntityItem, 0, 0, 0, 0, 0);
            }


            GL11.glPopMatrix();
            GL11.glPushMatrix();

            if (tileAltar.getStackInSlot(0) != null)
            {
                float scaleFactor = getGhostItemScaleFactor(tileAltar.getStackInSlot(0));
                EntityItem ghostEntityItem = new EntityItem(tileAltar.getWorldObj());
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

                customRenderItem.doRender(ghostEntityItem, 0, 0, 0, 0, 0);
            }

            GL11.glPopMatrix();


            ReagentContainerInfo[] info = tileAltar.getContainerInfo(ForgeDirection.UNKNOWN);
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
        Tessellator tessellator = Tessellator.instance;
        this.bindTexture(resourceLocation);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glDepthMask(false);

        tessellator.startDrawingQuads();
        tessellator.setColorRGBA(colourRed, colourGreen, colourBlue, colourIntensity);
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
        tessellator.setBrightness(240);

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

        tessellator.addVertexWithUV(x1, y1, z1, resx1, resy1);
        tessellator.addVertexWithUV(x2, y1, z1, resx2, resy1);
        tessellator.addVertexWithUV(x2, y2, z1, resx2, resy2);
        tessellator.addVertexWithUV(x1, y2, z1, resx1, resy2);
        tessellator.addVertexWithUV(x1, y1, z1, resx1, resy1);
        tessellator.addVertexWithUV(x1, y1, z2, resx2, resy1);
        tessellator.addVertexWithUV(x1, y2, z2, resx2, resy2);
        tessellator.addVertexWithUV(x1, y2, z1, resx1, resy2);
        tessellator.addVertexWithUV(x1, y1, z2, resx1, resy1);
        tessellator.addVertexWithUV(x2, y1, z2, resx2, resy1);
        tessellator.addVertexWithUV(x2, y2, z2, resx2, resy2);
        tessellator.addVertexWithUV(x1, y2, z2, resx1, resy2);
        tessellator.addVertexWithUV(x2, y1, z1, resx1, resy1);
        tessellator.addVertexWithUV(x2, y1, z2, resx2, resy1);
        tessellator.addVertexWithUV(x2, y2, z2, resx2, resy2);
        tessellator.addVertexWithUV(x2, y2, z1, resx1, resy2);
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
                switch (customRenderItem.getMiniBlockCount(itemStack, (byte) 1))
                {
                    case 1:
                        return 0.90F * scaleFactor;

                    case 2:
                        return 0.90F * scaleFactor;

                    case 3:
                        return 0.90F * scaleFactor;

                    case 4:
                        return 0.90F * scaleFactor;

                    case 5:
                        return 0.80F * scaleFactor;

                    default:
                        return 0.90F * scaleFactor;
                }
            } else
            {
                switch (customRenderItem.getMiniItemCount(itemStack, (byte) 1))
                {
                    case 1:
                        return 0.65F * scaleFactor;

                    case 2:
                        return 0.65F * scaleFactor;

                    case 3:
                        return 0.65F * scaleFactor;

                    case 4:
                        return 0.65F * scaleFactor;

                    default:
                        return 0.65F * scaleFactor;
                }
            }
        }

        return scaleFactor;
    }

    private float getXDisplacementForSlot(int slot)
    {
        switch (slot)
        {
            case 0:
                return 0.0f;

            case 1:
                return -0.375f;

            case 2:
                return -0.125f;

            case 3:
                return 0.3125f;

            case 4:
                return 0.3125f;

            case 5:
                return -0.125f;

            default:
                return 0.0f;
        }
    }

    private float getYDisplacementForSlot(int slot)
    {
        switch (slot)
        {
            case 0:
                return 0.4f;

            case 1:
                return -0.35f;

            case 6:
                return 0.4f;

            default:
                return -0.35f;
        }
    }

    private float getZDisplacementForSlot(int slot)
    {
        switch (slot)
        {
            case 0:
                return 0.0f;

            case 1:
                return 0.0f;

            case 2:
                return 0.375f;

            case 3:
                return 0.25f;

            case 4:
                return -0.25f;

            case 5:
                return -0.375f;

            default:
                return 0.0f;
        }
    }

    private void translateGhostItemByOrientation(ItemStack ghostItemStack, double x, double y, double z, ForgeDirection forgeDirection)
    {
        if (ghostItemStack != null)
        {
            if (ghostItemStack.getItem() instanceof ItemBlock)
            {
                switch (forgeDirection)
                {
                    case DOWN:
                    {
                        GL11.glTranslatef((float) x + 0.5F, (float) y + 2.7F, (float) z + 0.5F);
                        return;
                    }

                    case UP:
                    {
                        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.25F, (float) z + 0.5F);
                        return;
                    }

                    case NORTH:
                    {
                        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.7F);
                        return;
                    }

                    case SOUTH:
                    {
                        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.3F);
                        return;
                    }

                    case EAST:
                    {
                        GL11.glTranslatef((float) x + 0.3F, (float) y + 0.5F, (float) z + 0.5F);
                        return;
                    }

                    case WEST:
                    {
                        GL11.glTranslatef((float) x + 0.70F, (float) y + 0.5F, (float) z + 0.5F);
                        return;
                    }

                    case UNKNOWN:
                    {
                        return;
                    }

                    default:
                    {

                    }
                }
            } else
            {
                switch (forgeDirection)
                {
                    case DOWN:
                    {
                        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.6F, (float) z + 0.5F);
                        return;
                    }

                    case UP:
                    {
                        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.20F, (float) z + 0.5F);
                        return;
                    }

                    case NORTH:
                    {
                        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.4F, (float) z + 0.7F);
                        return;
                    }

                    case SOUTH:
                    {
                        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.4F, (float) z + 0.3F);
                        return;
                    }

                    case EAST:
                    {
                        GL11.glTranslatef((float) x + 0.3F, (float) y + 0.4F, (float) z + 0.5F);
                        return;
                    }

                    case WEST:
                    {
                        GL11.glTranslatef((float) x + 0.70F, (float) y + 0.4F, (float) z + 0.5F);
                        return;
                    }

                    case UNKNOWN:
                    {
                        return;
                    }

                    default:
                    {

                    }
                }
            }
        }
    }
}