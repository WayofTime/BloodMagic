package WayofTime.alchemicalWizardry.common.renderer.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;
import cpw.mods.fml.client.FMLClientHandler;

public class ModelBloodAltar extends ModelBase
{
    private IModelCustom modelBloodAltar;
    private IModelCustom modelBloodLevel; //TODO

    public ModelBloodAltar()
    {
        modelBloodAltar = AdvancedModelLoader.loadModel(new ResourceLocation("alchemicalwizardry:models/bloodaltar-fixeUV.obj"));
        modelBloodLevel = AdvancedModelLoader.loadModel(new ResourceLocation("alchemicalwizardry:models/bloodlevel.obj"));
    }

    public void renderBloodAltar()
    {
        modelBloodAltar.renderAll();
    }

    public void renderBloodLevel()
    {
        modelBloodLevel.renderAll();
    }

    public void renderBloodAltar(TEAltar altar, double x, double y, double z)
    {
        float scale = 0.1f;
        // Push a blank matrix onto the stack
        GL11.glPushMatrix();
        // Move the object into the correct position on the block (because the OBJ's origin is the center of the object)
        GL11.glTranslatef((float) x + 0.5f, (float) y, (float) z + 0.5f);
        // Scale our object to about half-size in all directions (the OBJ file is a little large)
        GL11.glScalef(scale, scale, scale);
        // Bind the texture, so that OpenGL properly textures our block.
        ResourceLocation test = new ResourceLocation("alchemicalwizardry:textures/models/altar.png");
        //FMLClientHandler.instance().getClient().renderEngine.bindTexture("/mods/alchemicalwizardry/textures/models/altar.png");
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(test);
        // Render the object, using modelTutBox.renderAll();
        this.renderBloodAltar();
        // Pop this matrix from the stack.
        GL11.glPopMatrix();
    }

    public void renderBloodLevel(TEAltar altar, double x, double y, double z)
    {
        float scale = 0.1f;
        // Push a blank matrix onto the stack
        GL11.glPushMatrix();
        float level = altar.getFluidAmount();
        // Move the object into the correct position on the block (because the OBJ's origin is the center of the object)
        GL11.glTranslatef((float) x + 0.5f, (float) y + 0.6499f + 0.12f * (level / altar.getCapacity()), (float) z + 0.5f);
        // Scale our object to about half-size in all directions (the OBJ file is a little large)
        GL11.glScalef(scale, scale, scale);
        // Bind the texture, so that OpenGL properly textures our block.
        ResourceLocation test = new ResourceLocation("alchemicalwizardry:textures/models/blood.png");
        //FMLClientHandler.instance().getClient().renderEngine.bindTexture("/mods/alchemicalwizardry/textures/models/altar.png");
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(test);
        // Render the object, using modelTutBox.renderAll();
        this.renderBloodLevel();
        // Pop this matrix from the stack.
        GL11.glPopMatrix();
    }
}
