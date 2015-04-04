package WayofTime.alchemicalWizardry.common.renderer.block;

import WayofTime.alchemicalWizardry.client.renderer.BMRenderItem;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelBloodAltar;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

public class TEAltarRenderer extends TileEntitySpecialRenderer
{
    private ModelBloodAltar modelBloodAltar = new ModelBloodAltar();
    private final RenderItem customRenderItem;

    public TEAltarRenderer()
    {
        customRenderItem = new BMRenderItem()
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
    public void renderTileEntityAt(TileEntity tileEntity, double d0, double d1, double d2, float f)
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
                float rotationAngle = (float) (720.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);
                EntityItem ghostEntityItem = new EntityItem(tileAltar.getWorldObj());
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
                customRenderItem.doRender(ghostEntityItem, 0, 0, 0, 0, 0);
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
                switch (customRenderItem.getMiniBlockCount(itemStack, (byte) 1))
                {
                    case 1:
                        return 0.90F;

                    case 2:
                        return 0.90F;

                    case 3:
                        return 0.90F;

                    case 4:
                        return 0.90F;

                    case 5:
                        return 0.80F;

                    default:
                        return 0.90F;
                }
            } else
            {
                switch (customRenderItem.getMiniItemCount(itemStack, (byte) 1))
                {
                    case 1:
                        return 0.65F;

                    case 2:
                        return 0.65F;

                    case 3:
                        return 0.65F;

                    case 4:
                        return 0.65F;

                    default:
                        return 0.65F;
                }
            }
        }

        return scaleFactor;
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
                        return;
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
                        return;
                    }
                }
            }
        }
    }
}
