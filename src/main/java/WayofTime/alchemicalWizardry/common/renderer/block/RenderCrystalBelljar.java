package WayofTime.alchemicalWizardry.common.renderer.block;

import WayofTime.alchemicalWizardry.common.tileEntity.TEBelljar;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentContainerInfo;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentStack;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelCrystalBelljar;

public class RenderCrystalBelljar extends TileEntitySpecialRenderer
{
    private ModelCrystalBelljar modelConduit = new ModelCrystalBelljar();

    private ResourceLocation resourceLocation = new ResourceLocation("alchemicalwizardry:textures/models/Reagent.png");

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double d0, double d1, double d2, float f, int i)
    {
        if (tileEntity instanceof TEBelljar)
        {
            TEBelljar tileAltar = (TEBelljar) tileEntity;
            GL11.glPushMatrix();
            GL11.glTranslatef((float) d0 + 0.5F, (float) d1 + 1.5F, (float) d2 + 0.5F);
            ResourceLocation test = new ResourceLocation("alchemicalwizardry:textures/models/CrystalBelljar.png");
            FMLClientHandler.instance().getClient().renderEngine.bindTexture(test);
            GL11.glPushMatrix();
            GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
            this.modelConduit.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
            GL11.glPopMatrix();
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
        wr.func_178961_b(colourRed, colourGreen, colourBlue, colourIntensity); //setColourRGBA
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
        wr.func_178963_b(240); //setBrightness

        double x1 = -4d / 16d;
        double x2 = 4d / 16d;
        double y1 = -6d / 16d;
        double y2 = 4d / 16d;
        double z1 = -4d / 16d;
        double z2 = 4d / 16d;

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
        wr.addVertexWithUV(x1, y2, z1, resx1, resy1);
        wr.addVertexWithUV(x2, y2, z1, resx2, resy1);
        wr.addVertexWithUV(x2, y2, z2, resx2, resy2);
        wr.addVertexWithUV(x1, y2, z2, resx1, resy2);
        tessellator.draw();

        GL11.glDepthMask(true);

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        GL11.glPopMatrix();
    }
}