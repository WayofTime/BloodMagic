package WayofTime.alchemicalWizardry.common.renderer.block.itemRender;

import WayofTime.alchemicalWizardry.common.renderer.model.ModelWritingTable;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class TEWritingTableItemRenderer implements IItemRenderer
{
    private ModelWritingTable model;

    public TEWritingTableItemRenderer()
    {
        model = new ModelWritingTable();
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
        ResourceLocation test = new ResourceLocation("alchemicalwizardry:textures/models/WritingTable.png");
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(test);
        model.render((Entity) null, 0, 0, 0, 0, 0, 0);
        // Re-enable Lighting Calculations
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }
}