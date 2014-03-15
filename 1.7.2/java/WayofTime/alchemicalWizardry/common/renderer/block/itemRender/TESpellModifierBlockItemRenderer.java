package WayofTime.alchemicalWizardry.common.renderer.block.itemRender;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelSpellModifierBlock;
import cpw.mods.fml.client.FMLClientHandler;

public class TESpellModifierBlockItemRenderer implements IItemRenderer 
{
	private ModelSpellModifierBlock modelSpellBlock = new ModelSpellModifierBlock();

	private void renderConduitItem(RenderBlocks render, ItemStack item, float translateX, float translateY, float translateZ) 
	{
		Tessellator tessellator = Tessellator.instance;

		Block block = ModBlocks.blockSpellEffect;
		//Icon icon = item.getItem().getIconFromDamage(0);
		
		GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        /**
         * Render the ghost item inside of the Altar, slowly spinning
         */
        GL11.glPushMatrix();
        GL11.glTranslatef((float) translateX + 0.5F, (float) translateY + 1.5F, (float) translateZ + 0.5F);
        ResourceLocation test = new ResourceLocation(this.getResourceLocationForMeta(item.getItemDamage()));
        
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(test);
        GL11.glPushMatrix();
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        //GL11.glRotatef(90F, 0.0F, 0.0F, 1.0F);
        //A reference to your Model file. Again, very important.
        this.modelSpellBlock.render((Entity) null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, ForgeDirection.DOWN, ForgeDirection.UP);
        //Tell it to stop rendering for both the PushMatrix's
        GL11.glPopMatrix();
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_LIGHTING);
	}


	/**
	 * IItemRenderer implementation *
	 */
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		switch (type) {
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
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}


	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		switch (type) {
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
		switch(meta)
		{
		case 0: return "alchemicalwizardry:textures/models/SpellModifierDefault.png";
		case 1: return "alchemicalwizardry:textures/models/SpellModifierOffensive.png";
		case 2: return "alchemicalwizardry:textures/models/SpellModifierDefensive.png";
		case 3: return "alchemicalwizardry:textures/models/SpellModifierEnvironmental.png";
		}
    	return "alchemicalwizardry:textures/models/SpellModifierDefault.png";
    }
}
