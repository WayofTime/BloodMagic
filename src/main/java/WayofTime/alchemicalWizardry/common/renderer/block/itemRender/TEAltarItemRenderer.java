package WayofTime.alchemicalWizardry.common.renderer.block.itemRender;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.fml.client.FMLClientHandler;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.common.renderer.model.ModelBloodAltar;

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
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
        float scale = 0.08f;
        switch (type)
        {
            case ENTITY:
                renderBloodAltar(item, 0, 0, 0, scale);
                break;
            case EQUIPPED:
                renderBloodAltar(item, 0, 0, 0.5f, scale);
                break;
            case EQUIPPED_FIRST_PERSON:
                renderBloodAltar(item, +0.5f, 0.5f, +0.5f, scale);
                break;
            case INVENTORY:
                renderBloodAltar(item, -0.5f, -0.75f, -0.5f, scale);
                break;

            default:
                return;
        }
    }

    private void renderBloodAltar(ItemStack item, float x, float y, float z, float scale)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, z);
        GL11.glScalef(scale, scale, scale);
        GL11.glRotatef(180f, 0f, 1f, 0f);
        ResourceLocation test = new ResourceLocation("alchemicalwizardry:textures/models/altar.png");
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(test);
        modelBloodAltar.renderBloodAltar();
        GL11.glPopMatrix();
    }
}
