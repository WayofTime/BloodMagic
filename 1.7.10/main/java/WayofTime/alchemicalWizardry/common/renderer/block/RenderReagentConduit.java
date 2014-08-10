package WayofTime.alchemicalWizardry.common.renderer.block;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.common.renderer.BeamRenderer;
import WayofTime.alchemicalWizardry.common.tileEntity.TEReagentConduit;
import cpw.mods.fml.client.FMLClientHandler;

public class RenderReagentConduit extends TileEntitySpecialRenderer
{	
    private static final ResourceLocation field_110629_a = new ResourceLocation("alchemicalwizardry:textures/models/TransCircle.png");

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double d0, double d1, double d2, float f)
    {
        if (tileEntity instanceof TEReagentConduit)
        {
    		GL11.glPushMatrix();
    		float f1 = 1.0f;
    		Tessellator tessellator = Tessellator.instance;
    		this.bindTexture(field_110629_a);
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
    		tessellator.setColorRGBA(0, 0, 255, 90);
    		//tessellator.setColorOpaque(255, 255, 255);
    		
    		GL11.glTranslated(d0+0.5, d1+0.5, d2+0.5);

    		GL11.glRotatef(tileEntity.getWorldObj().getWorldTime()/3.0f, 0F, 1F, 0F); //Rotate on planar axis
    		//GL11.glRotatef(30F, 0F, 0F, 1F); //Rotate vertical axis
    		//GL11.glRotatef(tileAltar.getWorldObj().getWorldTime()*2f, 1F, 0F, 0F); //Rotate cylindrically
    		
    		double offset = 0;
    		
    		tessellator.setBrightness(240);
//    		GL11.glTranslatef(0F, s, s);
//    		GL11.glScalef(1F, s * 14F, s * 14F);
    		tessellator.addVertexWithUV(-0.5d, 0, -0.5d, 0.0d, 0.0d);
    		tessellator.addVertexWithUV(0.5d, 0, -0.5d, 1.0d, 0.0d);
    		tessellator.addVertexWithUV(0.5d, 0, 0.5d, 1.0d, 1.0d);
    		tessellator.addVertexWithUV(-0.5d, 0, 0.5d, 0.0d, 1.0d);

    		tessellator.draw();

    		GL11.glDepthMask(true);

    		GL11.glEnable(GL11.GL_LIGHTING);
    		GL11.glEnable(GL11.GL_TEXTURE_2D);
    		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
    		GL11.glPopMatrix();
        }
    }
}