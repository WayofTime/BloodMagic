package WayofTime.alchemicalWizardry.common.renderer.block.itemRender;

import WayofTime.alchemicalWizardry.common.renderer.model.ModelBloodAltar;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class TEAltarItemRenderer implements IItemRenderer
{
    private ModelBloodAltar modelBloodAltar;

    public TEAltarItemRenderer()
    {
        modelBloodAltar = new ModelBloodAltar();
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
        float scale = 0.08f;

        // TODO Auto-generated method stub
        switch (type)
        {
            case ENTITY:
            {
                renderBloodAltar(0f, 0f, 0f, scale);
                return;
            }

            case EQUIPPED:
            {
                renderBloodAltar(0f, 0f, 0f, scale);
                return;
            }

            case INVENTORY:
            {
                renderBloodAltar(0f, -0.25f, 0f, scale);
                return;
            }

            default:
                return;
        }
    }

    private void renderBloodAltar(float x, float y, float z, float scale)
    {
        GL11.glPushMatrix();
        // Disable Lighting Calculations
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glTranslatef(x, y, z);
        GL11.glScalef(scale, scale, scale);
        GL11.glRotatef(180f, 0f, 1f, 0f);
        //FMLClientHandler.instance().getClient().renderEngine.bindTexture("/mods/OBJTutorial/textures/models/TutBox.png");
        ResourceLocation test = new ResourceLocation("alchemicalwizardry:textures/models/altar.png");
        //FMLClientHandler.instance().getClient().renderEngine.bindTexture("/mods/alchemicalwizardry/textures/models/altar.png");
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(test);
        modelBloodAltar.renderBloodAltar();
        // Re-enable Lighting Calculations
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }
}
