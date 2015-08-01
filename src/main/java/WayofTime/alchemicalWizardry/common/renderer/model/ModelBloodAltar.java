package WayofTime.alchemicalWizardry.common.renderer.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;

public class ModelBloodAltar extends ModelBase
{
	private static final ResourceLocation altar_texture = new ResourceLocation("alchemicalwizardry:textures/models/altar.png");
	
//    private IModelCustom modelBloodAltar;

    public ModelBloodAltar()
    {
//        modelBloodAltar = AdvancedModelLoader.loadModel(new ResourceLocation("alchemicalwizardry:models/bloodaltar-fixeUV.obj"));
    }

    public void renderBloodAltar()
    {
//        modelBloodAltar.renderAll();
    }

    public void renderBloodAltar(TEAltar altar, double x, double y, double z)
    {
//        float scale = 0.1f;
//        // Push a blank matrix onto the stack
//        GL11.glPushMatrix();
//        // Move the object into the correct position on the block (because the OBJ's origin is the center of the object)
//        GL11.glTranslatef((float) x + 0.5f, (float) y, (float) z + 0.5f);
//        // Scale our object to about half-size in all directions (the OBJ file is a little large)
//        GL11.glScalef(scale, scale, scale);
//        // Bind the texture, so that OpenGL properly textures our block.
//        FMLClientHandler.instance().getClient().renderEngine.bindTexture(altar_texture);
//        // Render the object, using modelTutBox.renderAll();
//        this.renderBloodAltar();
//        // Pop this matrix from the stack.
//        GL11.glPopMatrix();
    }

    public void renderBloodLevel(TEAltar altar, double x, double y, double z)
    {
//        GL11.glPushMatrix();
//        float level = altar.getFluidAmount();
//        GL11.glTranslatef((float) x , (float) y + 0.6499f + 0.12f * (level / altar.getCapacity()), (float) z);
//        FMLClientHandler.instance().getClient().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
//        renderBloodLevel(AlchemicalWizardry.lifeEssenceFluid.getStillIcon());
//        GL11.glPopMatrix();
    }
    
    public void renderBloodLevel(TextureAtlasSprite icon)
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer wr = tessellator.getWorldRenderer();

        double minU = (double) icon.getInterpolatedU(0);
        double maxU = (double) icon.getInterpolatedU(16);
        double minV = (double) icon.getInterpolatedV(0);
        double maxV = (double) icon.getInterpolatedV(16);

        wr.startDrawingQuads();
        wr.func_178980_d(0, 1, 0); //setNormal
        wr.addVertexWithUV(1, 0, 1, maxU, maxV);
        wr.addVertexWithUV(1, 0, 0, maxU, minV);
        wr.addVertexWithUV(0, 0, 0, minU, minV);
        wr.addVertexWithUV(0, 0, 1, minU, maxV);
        tessellator.draw();
    }
}