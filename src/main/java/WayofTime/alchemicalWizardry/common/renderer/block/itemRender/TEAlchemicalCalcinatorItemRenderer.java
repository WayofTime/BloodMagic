package WayofTime.alchemicalWizardry.common.renderer.block.itemRender;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.fml.client.FMLClientHandler;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.common.renderer.model.ModelAlchemicalCalcinator;

public class TEAlchemicalCalcinatorItemRenderer implements IItemRenderer
{
    private ModelAlchemicalCalcinator modelConduit = new ModelAlchemicalCalcinator();

    private void renderConduitItem(ItemStack item, float translateX, float translateY, float translateZ)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(translateX + 0.5F, translateY + 1.5F, translateZ + 0.5F);
        ResourceLocation test = new ResourceLocation("alchemicalwizardry:textures/models/AlchemicalCalcinator.png");
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(test);
        GL11.glPushMatrix();
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        this.modelConduit.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    /**
     * IItemRenderer implementation *
     */
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        switch (type)
        {
            case ENTITY:
                return true;
            case EQUIPPED:
                return true;
            case EQUIPPED_FIRST_PERSON:
                return true;
            case INVENTORY:
                return true;
            default:
                return false;
        }
    }


    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return true;
    }


    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
        switch (type)
        {
            case ENTITY:
                renderConduitItem(item, -0.5f, -0.5f, -0.5f);
                break;
            case EQUIPPED:
                renderConduitItem(item, -0.4f, 0.50f, 0.35f);
                break;
            case EQUIPPED_FIRST_PERSON:
                renderConduitItem(item, -0.4f, 0.50f, 0.35f);
                break;
            case INVENTORY:
                renderConduitItem(item, -0.5f, -0.5f, -0.5f);
                break;
            default:
        }
    }
}
