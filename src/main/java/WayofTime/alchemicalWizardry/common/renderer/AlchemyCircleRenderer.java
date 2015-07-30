package WayofTime.alchemicalWizardry.common.renderer;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.api.renderer.MRSRenderer;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;

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
    private boolean renderWithoutReagents;

    public AlchemyCircleRenderer(ResourceLocation resource, int red, int green, int blue, int intensity, double xOff, double initialY, double yOff, double zOff, double radius, boolean renderWithoutReagents)
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
        this.renderWithoutReagents = renderWithoutReagents;
    }

    @Override
    public void renderAt(IMasterRitualStone tile, double x, double y, double z)
    {
        if (tile.areTanksEmpty() && !renderWithoutReagents)
        {
            return;
        }

        GL11.glPushMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        this.bindTexture(resourceLocation);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
//        GL11.glEnable(GL11.GL_BLEND);
//        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

//        GL11.glDepthMask(false);

        WorldRenderer wr = tessellator.getWorldRenderer();
        wr.startDrawingQuads();
        wr.func_178961_b(colourRed, colourGreen, colourBlue, colourIntensity);

        GL11.glTranslated(x + 0.5 + xOffset, y + 0.5 + (yOffset - initialY) * (tile.getRunningTime() / 100d) + initialY, z + 0.5 + zOffset);

        float rotationAngle = (float) (720.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);

        GL11.glRotatef(rotationAngle, 0F, 1F, 0F); //Rotate on planar axis
//        tessellator.setBrightness(240);

        double finalRadius = (radius) * (tile.getRunningTime() / 100d);

        wr.addVertexWithUV(-finalRadius, 0, -finalRadius, 0.0d, 0.0d);
        wr.addVertexWithUV(finalRadius, 0, -finalRadius, 1.0d, 0.0d);
        wr.addVertexWithUV(finalRadius, 0, finalRadius, 1.0d, 1.0d);
        wr.addVertexWithUV(-finalRadius, 0, finalRadius, 0.0d, 1.0d);

        tessellator.draw();

//        GL11.glDepthMask(true);

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        GL11.glPopMatrix();
    }
}
