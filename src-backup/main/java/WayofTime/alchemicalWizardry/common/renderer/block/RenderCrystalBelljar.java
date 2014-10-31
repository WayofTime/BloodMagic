package WayofTime.alchemicalWizardry.common.renderer.block;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentContainerInfo;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentStack;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelAlchemicalCalcinator;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelCrystalBelljar;
import WayofTime.alchemicalWizardry.common.tileEntity.TEBellJar;
import cpw.mods.fml.client.FMLClientHandler;

public class RenderCrystalBelljar extends TileEntitySpecialRenderer
{
    private ModelCrystalBelljar modelConduit = new ModelCrystalBelljar();
    
    private ResourceLocation resourceLocation = new ResourceLocation("alchemicalwizardry:textures/models/Reagent.png");


    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double d0, double d1, double d2, float f)
    {
        if (tileEntity instanceof TEBellJar)
        {
            TEBellJar tileAltar = (TEBellJar) tileEntity;

            GL11.glDisable(GL11.GL_LIGHTING);
//            GL11.glDisable(GL11.GL_CULL_FACE);
            /**
             * Render the ghost item inside of the Altar, slowly spinning
             */
            GL11.glPushMatrix();
            GL11.glTranslatef((float) d0 + 0.5F, (float) d1 + 1.5F, (float) d2 + 0.5F);
            ResourceLocation test = new ResourceLocation("alchemicalwizardry:textures/models/CrystalBelljar.png");
            FMLClientHandler.instance().getClient().renderEngine.bindTexture(test);
            GL11.glPushMatrix();
            GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
            //GL11.glRotatef(90F, 0.0F, 0.0F, 1.0F);
            //A reference to your Model file. Again, very important.
            this.modelConduit.render((Entity) null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
            //Tell it to stop rendering for both the PushMatrix's
            GL11.glPopMatrix();
            GL11.glPopMatrix();
//            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_LIGHTING);
            
            

            
            ReagentContainerInfo[] info = tileAltar.getContainerInfo(ForgeDirection.UNKNOWN);
            if(info.length >= 1 && info[0] != null)
            {
            	ReagentStack reagentStack = info[0].reagent;
            	int capacity = info[0].capacity;
            	if(reagentStack != null && reagentStack.reagent != null)
            	{
            		Reagent reagent = reagentStack.reagent;
                    this.renderTankContents(d0, d1, d2, reagent.getColourRed(), reagent.getColourGreen(), reagent.getColourBlue(), 200 * reagentStack.amount / capacity);
            	}
            }
            
//            this.renderTankContents(d0, d1, d2, 255, 0, 0, 200);

            
            
            
//            for (int i = 0; i <= 1; i++)
//            {
//                GL11.glPushMatrix();
//
//                if (tileAltar.getStackInSlot(i) != null)
//                {
//                    float scaleFactor = getGhostItemScaleFactor(tileAltar.getStackInSlot(i));
//                    float rotationAngle = (float) (720.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);
//                    EntityItem ghostEntityItem = new EntityItem(tileAltar.getWorldObj());
//                    ghostEntityItem.hoverStart = 0.0F;
//                    ghostEntityItem.setEntityItemStack(tileAltar.getStackInSlot(i));
//                    //translateGhostItemByOrientation(ghostEntityItem.getEntityItem(), d0, d1, d2, ForgeDirection.DOWN);
//                    float displacementX = getXDisplacementForSlot(i);
//                    float displacementY = getYDisplacementForSlot(i);
//                    float displacementZ = getZDisplacementForSlot(i);
//
//                    if (ghostEntityItem.getEntityItem().getItem() instanceof ItemBlock)
//                    {
//                        GL11.glTranslatef((float) d0 + 0.5F + displacementX, (float) d1 + displacementY + 0.7F, (float) d2 + 0.5F + displacementZ);
//                    } else
//                    {
//                        GL11.glTranslatef((float) d0 + 0.5F + displacementX, (float) d1 + displacementY + 0.6F, (float) d2 + 0.5F + displacementZ);
//                    }
//
//                    //GL11.glTranslatef((float) tileAltar.xCoord + 0.5F, (float) tileAltar.yCoord + 2.7F, (float) tileAltar.zCoord + 0.5F);
//                    GL11.glScalef(scaleFactor, scaleFactor, scaleFactor);
//                    GL11.glRotatef(rotationAngle, 0.0F, 1.0F, 0.0F);
//                    customRenderItem.doRender(ghostEntityItem, 0, 0, 0, 0, 0);
//                }
//
//                GL11.glPopMatrix();
//            }

            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_LIGHTING);
        }
    }

    private void renderTankContents(double x, double y, double z, int colourRed, int colourGreen, int colourBlue, int colourIntensity)
    {
    	GL11.glPushMatrix();
		float f1 = 1.0f;
		Tessellator tessellator = Tessellator.instance;
		this.bindTexture(resourceLocation);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_CULL_FACE);
		float f2 = 0;
		float f3 = -f2 * 0.2F - (float)MathHelper.floor_float(-f2 * 0.1F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GL11.glDepthMask(false);

		tessellator.startDrawingQuads();
		tessellator.setColorRGBA(colourRed, colourGreen, colourBlue, colourIntensity);
			
		GL11.glTranslated(x+0.5, y+0.5, z+0.5);
		//GL11.glRotatef(30F, 0F, 0F, 1F); //Rotate vertical axis
		//GL11.glRotatef(tileAltar.getWorldObj().getWorldTime()*2f, 1F, 0F, 0F); //Rotate cylindrically
		
		tessellator.setBrightness(240);
		
		double x1 = -4d/16d;
		double x2 = 4d/16d;
		double y1 = -6d/16d;
		double y2 = 4d/16d;
		double z1 = -4d/16d;
		double z2 = 4d/16d;
		
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
		tessellator.addVertexWithUV(x1, y2, z1, resx1, resy1);
		tessellator.addVertexWithUV(x2, y2, z1, resx2, resy1);
		tessellator.addVertexWithUV(x2, y2, z2, resx2, resy2);
		tessellator.addVertexWithUV(x1, y2, z2, resx1, resy2);
		tessellator.draw();

		GL11.glDepthMask(true);

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
		GL11.glPopMatrix();
    }
}