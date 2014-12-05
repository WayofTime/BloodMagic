package WayofTime.alchemicalWizardry.common.renderer.block.itemRender;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.common.renderer.model.ModelSpellEnhancementBlock;
import cpw.mods.fml.client.FMLClientHandler;

public class TESpellEnhancementBlockItemRenderer implements IItemRenderer
{
    private ModelSpellEnhancementBlock modelSpellBlock = new ModelSpellEnhancementBlock();

    private void renderConduitItem(RenderBlocks render, ItemStack item, float translateX, float translateY, float translateZ)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) translateX + 0.5F, (float) translateY + 1.5F, (float) translateZ + 0.5F);
        ResourceLocation test = new ResourceLocation(this.getResourceLocationForMeta(item.getItemDamage()));

        FMLClientHandler.instance().getClient().renderEngine.bindTexture(test);
        GL11.glPushMatrix();
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        this.modelSpellBlock.render((Entity) null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, ForgeDirection.DOWN, ForgeDirection.UP);
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
                renderConduitItem((RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
                break;
            case EQUIPPED:
                renderConduitItem((RenderBlocks) data[0], item, -0.4f, 0.50f, 0.35f);
                break;
            case EQUIPPED_FIRST_PERSON:
                renderConduitItem((RenderBlocks) data[0], item, -0.4f, 0.50f, 0.35f);
                break;
            case INVENTORY:
                renderConduitItem((RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
                break;
            default:
        }
    }

    public String getResourceLocationForMeta(int meta)
    {
        switch (meta)
        {
        case 0:
            return "alchemicalwizardry:textures/models/SpellEnhancementPower1.png";
        case 1:
            return "alchemicalwizardry:textures/models/SpellEnhancementPower2.png";
        case 2:
            return "alchemicalwizardry:textures/models/SpellEnhancementPower3.png";
        case 3:
        	return "alchemicalwizardry:textures/models/SpellEnhancementPower4.png";
        case 5:
            return "alchemicalwizardry:textures/models/SpellEnhancementCost1.png";
        case 6:
            return "alchemicalwizardry:textures/models/SpellEnhancementCost2.png";
        case 7:
        	return "alchemicalwizardry:textures/models/SpellEnhancementCost3.png";
        case 8:
            return "alchemicalwizardry:textures/models/SpellEnhancementCost4.png";
        case 10:
            return "alchemicalwizardry:textures/models/SpellEnhancementPotency1.png";
        case 11:
            return "alchemicalwizardry:textures/models/SpellEnhancementPotency2.png";
        case 12:
            return "alchemicalwizardry:textures/models/SpellEnhancementPotency3.png";
        case 13:
            return "alchemicalwizardry:textures/models/SpellEnhancementPotency4.png";

        }
        return "alchemicalwizardry:textures/models/SpellEnhancementPower1.png";
    }
}
