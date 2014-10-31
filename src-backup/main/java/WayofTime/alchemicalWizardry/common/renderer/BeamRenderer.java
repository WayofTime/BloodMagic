package WayofTime.alchemicalWizardry.common.renderer;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.api.ColourAndCoords;

public class BeamRenderer 
{
    private static final ResourceLocation field_110629_a = new ResourceLocation("textures/entity/beacon_beam.png");
    
    public int xInit;
    public int yInit;
    public int zInit;
    
    public int xFinal;
    public int yFinal;
    public int zFinal;
    
    public int colourRed;
    public int colourGreen;
    public int colourBlue;
    public int colourIntensity;
    
    public double size;
    
    public void setInitialPosition(int x, int y, int z)
    {
    	this.xInit = x;
    	this.yInit = y;
    	this.zInit = z;
    }
    
    public void setColourAndFinalPosition(ColourAndCoords col)
    {
    	this.colourRed = col.colourRed;
    	this.colourGreen = col.colourGreen;
    	this.colourBlue = col.colourBlue;
    	this.colourIntensity = col.colourIntensity;
    	
    	this.xFinal = col.xCoord;
    	this.yFinal = col.yCoord;
    	this.zFinal = col.zCoord;
    }
    
    public void setSize(double size)
    {
    	this.size = size;
    }
    
    protected static void bindTexture(ResourceLocation p_147499_1_)
    {
        TextureManager texturemanager = TileEntityRendererDispatcher.instance.field_147553_e;

        if (texturemanager != null)
        {
            texturemanager.bindTexture(p_147499_1_);
        }
    }
    
	public void render(double d0, double d1, double d2)
	{
		int xDiff = this.xFinal - this.xInit;
		int yDiff = this.yFinal - this.yInit;
		int zDiff = this.zFinal - this.zInit;
		
		float planarAngle = (float)(Math.atan2(-zDiff, xDiff) * 180d / Math.PI); //Radians
		float verticalAngle = (float)(Math.atan2(yDiff, Math.sqrt(xDiff*xDiff + zDiff+zDiff)) * 180d / Math.PI);
		
		float distance = (float) Math.sqrt(xDiff*xDiff + yDiff*yDiff + zDiff*zDiff); //Total distance

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
		tessellator.setColorRGBA(colourRed, colourGreen, colourBlue, colourIntensity);
		//tessellator.setColorOpaque(255, 255, 255);
		
		double inside = -(this.size/2d);
		double outside = 1.0d-(0.50d - this.size/2d)-0.5d;
		
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

		GL11.glRotatef(planarAngle, 0F, 1F, 0F); //Rotate on planar axis
		GL11.glRotatef(verticalAngle, 0F, 0F, 1F); //Rotate vertical axis
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
