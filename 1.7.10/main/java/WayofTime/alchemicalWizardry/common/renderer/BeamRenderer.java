package WayofTime.alchemicalWizardry.common.renderer;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class BeamRenderer 
{
    private static final ResourceLocation field_110629_a = new ResourceLocation("textures/entity/beacon_beam.png");
    protected static TileEntityRendererDispatcher field_147501_a;

    protected static void bindTexture(ResourceLocation p_147499_1_)
    {
        TextureManager texturemanager = BeamRenderer.field_147501_a.field_147553_e;

        if (texturemanager != null)
        {
            texturemanager.bindTexture(p_147499_1_);
        }
    }
    
	public static void render()
	{
		double d0 = 0;
		double d1 = 0;
		double d2 = 0;


		float distance = 5; //Total distance

		GL11.glPushMatrix();
		float f1 = 1.0f;
		Tessellator tessellator = Tessellator.instance;
		BeamRenderer.bindTexture(field_110629_a);
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
		tessellator.setColorRGBA(255, 0, 0, 50);
		//tessellator.setColorOpaque(255, 255, 255);
		
		double inside = 0.45d-0.5d;
		double outside = 1.0d-0.45d-0.5d;
		
		double d18 = inside;
		double d19 = inside;
		double d20 = outside;
		double d21 = inside;
		double d22 = inside;
		double d23 = outside;
		double d24 = outside;
		double d25 = outside;
		double d26 = (double)(distance * f1);// + 0.2;
		double d27 = 0.0D;
		double d28 = 1.0D;
		double d29 = (double)(-1.0F + f3);
		double d30 = (double)(distance * f1) + d29;
		
		GL11.glTranslated(d0+0.5, d1+0.5, d2+0.5);

		GL11.glRotatef(45F, 0F, 1F, 0F); //Rotate on planar axis
		GL11.glRotatef(30F, 0F, 0F, 1F); //Rotate vertical axis
		//GL11.glRotatef(tileAltar.getWorldObj().getWorldTime()*2f, 1F, 0F, 0F); //Rotate cylindrically
		
		double offset = 0;
		
		tessellator.setBrightness(240);
		float s = 1F / 16F;
//		GL11.glTranslatef(0F, s, s);
//		GL11.glScalef(1F, s * 14F, s * 14F);
		tessellator.addVertexWithUV(d26, d18, d19, d28, d30);
		tessellator.addVertexWithUV(offset, d18, d19, d28, d29);
		tessellator.addVertexWithUV(offset, d20, d21, d27, d29);
		tessellator.addVertexWithUV(d26, d20, d21, d27, d30);
		tessellator.addVertexWithUV(d26, d24, d25, d28, d30);
		tessellator.addVertexWithUV(offset, d24, d25, d28, d29);
		tessellator.addVertexWithUV(offset, d22, d23, d27, d29);
		tessellator.addVertexWithUV(d26, d22, d23, d27, d30);
		tessellator.addVertexWithUV(d26, d20, d21, d28, d30);
		tessellator.addVertexWithUV(offset, d20, d21, d28, d29);
		tessellator.addVertexWithUV(offset, d24, d25, d27, d29);
		tessellator.addVertexWithUV(d26, d24, d25, d27, d30);
		tessellator.addVertexWithUV(d26, d22, d23, d28, d30);
		tessellator.addVertexWithUV(offset, d22, d23, d28, d29);
		tessellator.addVertexWithUV(offset, d18, d19, d27, d29);
		tessellator.addVertexWithUV(d26, d18, d19, d27, d30);

		//ShaderHelper.useShaderWithProps(ShaderHelper.beam, "time", (int) tileAltar.getWorldObj().getTotalWorldTime());
		tessellator.draw();
		//ShaderHelper.releaseShader();

		GL11.glDepthMask(true);


		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
		GL11.glPopMatrix();
	}
}
