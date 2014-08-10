package WayofTime.alchemicalWizardry.common.renderer;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;

public class AlchemyCircleRenderer extends MRSRenderer
{
    private ResourceLocation resourceLocation = new ResourceLocation("alchemicalwizardry:textures/models/TransCircle.png");
    private int colourRed;
    private int colourGreen;
    private int colourBlue;
    private int colourIntensity;
    private double xOffset;
    private double yOffset;
    private double zOffset;
    private double radius;
    private double initialY;
    
    public AlchemyCircleRenderer(ResourceLocation resource, int red, int green, int blue, int intensity, double xOff, double initialY, double yOff, double zOff, double radius)
    {
    	this.resourceLocation = resource;
    	this.colourRed = red;
    	this.colourGreen = green;
    	this.colourBlue = blue;
    	this.colourIntensity = intensity;
    	this.xOffset = xOff;
    	this.initialY = initialY;
    	this.yOffset = yOff;
    	this.zOffset = zOff;
    	this.radius = radius;
    }
    
	@Override
	public void renderAt(TEMasterStone tile, double x, double y, double z) 
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
			
		GL11.glTranslated(x+0.5+xOffset, y+0.5+(yOffset-initialY)*(tile.runningTime/100d)+initialY, z+0.5+zOffset);
		
		float rotationAngle = (float) (720.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);

		GL11.glRotatef(rotationAngle, 0F, 1F, 0F); //Rotate on planar axis
		//GL11.glRotatef(30F, 0F, 0F, 1F); //Rotate vertical axis
		//GL11.glRotatef(tileAltar.getWorldObj().getWorldTime()*2f, 1F, 0F, 0F); //Rotate cylindrically
		
		tessellator.setBrightness(240);
		
		double finalRadius = (radius)*(tile.runningTime/100d);

		tessellator.addVertexWithUV(-finalRadius, 0, -finalRadius, 0.0d, 0.0d);
		tessellator.addVertexWithUV(finalRadius, 0, -finalRadius, 1.0d, 0.0d);
		tessellator.addVertexWithUV(finalRadius, 0, finalRadius, 1.0d, 1.0d);
		tessellator.addVertexWithUV(-finalRadius, 0, finalRadius, 0.0d, 1.0d);

		tessellator.draw();

		GL11.glDepthMask(true);

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
		GL11.glPopMatrix();
	}	
}
