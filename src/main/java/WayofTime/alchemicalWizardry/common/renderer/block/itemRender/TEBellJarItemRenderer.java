package WayofTime.alchemicalWizardry.common.renderer.block.itemRender;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.fml.client.FMLClientHandler;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentContainerInfo;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentStack;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelCrystalBelljar;
import WayofTime.alchemicalWizardry.common.tileEntity.TEBelljar;

public class TEBellJarItemRenderer implements IItemRenderer
{
    private ModelCrystalBelljar modelConduit = new ModelCrystalBelljar();
    private ResourceLocation mainResource = new ResourceLocation("alchemicalwizardry:textures/models/CrystalBelljar.png");
    private ResourceLocation resourceLocation = new ResourceLocation("alchemicalwizardry:textures/models/Reagent.png");

    private void renderConduitItem(ItemStack item, float translateX, float translateY, float translateZ)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) translateX + 0.5F, (float) translateY + 1.5F, (float) translateZ + 0.5F);
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(mainResource);
        GL11.glPushMatrix();
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        this.modelConduit.renderSpecialItem((Entity) null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, 0);
        GL11.glPopMatrix();
        GL11.glPopMatrix();

        ReagentContainerInfo[] info = TEBelljar.getContainerInfoFromItem(item);
        if (info.length >= 1 && info[0] != null)
        {
            ReagentStack reagentStack = info[0].reagent;
            int capacity = info[0].capacity;
            if (reagentStack != null && reagentStack.reagent != null)
            {
                Reagent reagent = reagentStack.reagent;
                this.renderTankContents(translateX, translateY, translateZ, reagent.getColourRed(), reagent.getColourGreen(), reagent.getColourBlue(), 200 * reagentStack.amount / capacity);
            }
        }
        
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glPushMatrix();
        GL11.glTranslatef((float) translateX + 0.5F, (float) translateY + 1.5F, (float) translateZ + 0.5F);
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(mainResource);
        GL11.glPushMatrix();
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        this.modelConduit.renderSpecialItem((Entity) null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, 1);
        GL11.glPopMatrix();
        GL11.glPopMatrix();

        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private void renderTankContents(double x, double y, double z, int colourRed, int colourGreen, int colourBlue, int colourIntensity)
    {
        GL11.glPushMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(resourceLocation);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glDepthMask(false);

        WorldRenderer wr = tessellator.getWorldRenderer();
        wr.startDrawingQuads();
        wr.func_178961_b(colourRed, colourGreen, colourBlue, colourIntensity);

        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);

        wr.func_178963_b(240);

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
