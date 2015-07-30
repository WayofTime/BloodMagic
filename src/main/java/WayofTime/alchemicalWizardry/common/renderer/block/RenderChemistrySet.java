package WayofTime.alchemicalWizardry.common.renderer.block;

import WayofTime.alchemicalWizardry.common.tileEntity.TEChemistrySet;
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

import WayofTime.alchemicalWizardry.common.renderer.model.ModelChemistrySet;

public class RenderChemistrySet extends TileEntitySpecialRenderer
{
    private ModelChemistrySet modelChemistrySet = new ModelChemistrySet();
    private final RenderItem customRenderItem;

    public RenderChemistrySet()
    {
        customRenderItem = Minecraft.getMinecraft().getRenderItem();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double d0, double d1, double d2, float f, int a)
    {
        if (tileEntity instanceof TEChemistrySet)
        {
            TEChemistrySet tileAltar = (TEChemistrySet) tileEntity;
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glPushMatrix();
            GL11.glTranslatef((float) d0 + 0.5F, (float) d1 + 1.5F, (float) d2 + 0.5F);
            ResourceLocation test = new ResourceLocation("alchemicalwizardry:textures/models/WritingTable.png");
            FMLClientHandler.instance().getClient().renderEngine.bindTexture(test);
            GL11.glPushMatrix();
            GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
            this.modelChemistrySet.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
            GL11.glPopMatrix();
            GL11.glPopMatrix();

            for (int i = 1; i <= 6; i++)
            {
                GL11.glPushMatrix();
                GL11.glEnable(GL11.GL_LIGHTING);
                if (tileAltar.getStackInSlot(i) != null)
                {
                    float scaleFactor = getGhostItemScaleFactor(tileAltar.getStackInSlot(i));
                    float rotationAngle = Minecraft.getMinecraft().gameSettings.fancyGraphics ? (float) (720.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL) : 0;
                    EntityItem ghostEntityItem = new EntityItem(tileAltar.getWorld());
                    ghostEntityItem.hoverStart = 0.0F;
                    ghostEntityItem.setEntityItemStack(tileAltar.getStackInSlot(i));
                    float displacementX = getXDisplacementForSlot(i);
                    float displacementY = getYDisplacementForSlot(i);
                    float displacementZ = getZDisplacementForSlot(i);

                    if (ghostEntityItem.getEntityItem().getItem() instanceof ItemBlock)
                    {
                        GL11.glTranslatef((float) d0 + 0.5F + displacementX, (float) d1 + displacementY + 0.7F, (float) d2 + 0.5F + displacementZ);
                    } else
                    {
                        GL11.glTranslatef((float) d0 + 0.5F + displacementX, (float) d1 + displacementY + 0.6F, (float) d2 + 0.5F + displacementZ);
                    }
                    GL11.glScalef(scaleFactor, scaleFactor, scaleFactor);
                    GL11.glRotatef(rotationAngle, 0.0F, 1.0F, 0.0F);
                    customRenderItem.func_175043_b(ghostEntityItem.getEntityItem()); //renderItemModel
                }

                GL11.glPopMatrix();
            }
        }
    }

    private float getGhostItemScaleFactor(ItemStack itemStack)
    {
        float scaleFactor = 0.8F;

        if (itemStack != null)
        {
            if (itemStack.getItem() instanceof ItemBlock)
            {
                return 0.9f * scaleFactor;
            } else
            {
            	return 0.65F * scaleFactor;
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
}