package WayofTime.alchemicalWizardry.book.entries;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.api.items.ShapedBloodOrbRecipe;
import WayofTime.alchemicalWizardry.book.classes.guide.GuiEntry;

public class EntryCraftingRecipe implements IEntry{
	public EntryCraftingRecipe(IRecipe recipes){		
		this.recipes = recipes;
		populate(recipes);
	}
	public IRecipe recipes;
	
	public ItemStack[] recipe;
	public ItemStack output;
	
	public ArrayList<ItemIcon> icons = new ArrayList<ItemIcon>();
	
	@SuppressWarnings("unchecked")
	public void populate(IRecipe recipe){
		if(recipe instanceof ShapedRecipes){
			ShapedRecipes rec = (ShapedRecipes)recipe;
			if(rec != null && rec.recipeItems != null && rec.recipeItems.length > 0){
				this.recipe = rec.recipeItems;
				this.output = rec.getRecipeOutput();
			}
		}else if(recipe instanceof ShapedOreRecipe){
			ShapedOreRecipe rec = (ShapedOreRecipe)recipe;
			this.recipe = new ItemStack[rec.getInput().length];;
			for(int i = 0; i < rec.getInput().length; i++){
				ItemStack s = null;
				if(rec.getInput()[i] instanceof ItemStack){
					s = (ItemStack)rec.getInput()[i];
				}else{		
					s = ((ArrayList<ItemStack>)rec.getInput()[i]).get(0);
				}
				this.recipe[i] = s;
				this.output = rec.getRecipeOutput();
			}
		}else if(recipe instanceof ShapedBloodOrbRecipe){
			ShapedBloodOrbRecipe rec = (ShapedBloodOrbRecipe)recipe;
			this.recipe = new ItemStack[rec.getInput().length];;
			for(int i = 0; i < rec.getInput().length; i++){
				ItemStack s = null;
				if(rec.getInput()[i] instanceof ItemStack){
					s = (ItemStack)rec.getInput()[i];
				}else if(rec.getInput()[i] instanceof Object){
					s = new ItemStack(ModItems.masterBloodOrb);
				}else{
					s = ((ArrayList<ItemStack>)rec.getInput()[i]).get(0);
				}
				this.recipe[i] = s;
				this.output = rec.getRecipeOutput();
			}
		}
	}
	
	@Override
	public void draw(GuiEntry entry, int width, int height, int left, int top, EntityPlayer player, String key, int page, int mX, int mY){
		int x, y;

		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		renderOverlay(entry, width, height, left, top);
		
		/** Row 1 */
		x = (left + width / 2) - (65-31);
		y = (height/2 - 18) + (18*0);
		drawIcon(0, x, y);
		
		x = left + width / 2 - (65-48) + 1;
		y = (height/2 - 18) + (18*(3-3));
		drawIcon(1, x, y);
		
		x = left + width / 2 - (65-(48+16)-3);
		y = (height/2 - 18) + (18*(6-6));
		drawIcon(2, x, y);
		
		/** Row 2 */
		x = (left + width / 2) - (65-31);
		y = (height/2 - 18) + (18*1);
		drawIcon(3, x, y);
		
		x = left + width / 2 - (65-48) + 1;
		y = (height/2 - 18) + (18*(4-3));
		drawIcon(4, x, y);
		
		x = left + width / 2 - (65-(48+16)-3);
		y = (height/2 - 18) + (18*(7-6));
		drawIcon(5, x, y);
		
		/** Row 3 */
		x = (left + width / 2) - (65-31);
		y = (height/2 - 18) + (18*2);
		drawIcon(6, x, y);

		x = left + width / 2 - (65-48) + 1;
		y = (height/2 - 18) + (18*(5-3));
		drawIcon(7, x, y);
		
		x = left + width / 2 - (65-(48+16)-3);
		y = (height/2 - 18) + (18*(8-6));
		drawIcon(8, x, y);
		
		/** Result */
		x = left + width / 2 - (65-(48+48)-5);
		y = (height/2 - 18) + (18*(4-3));
		drawIcon(this.output, x, y);
		
		RenderHelper.disableStandardItemLighting();

		GL11.glDisable(GL11.GL_LIGHTING);
 
		GL11.glPopMatrix();

		for(ItemIcon icon : icons){
			if(icon.stack != null)
				icon.onMouseBetween(mX, mY);
		}
	}

	public void drawIcon(int entry, int x, int y){
		RenderItem ri = new RenderItem();
		if(recipe != null && recipe.length > 0 && recipe[entry] != null){
			ri.renderItemAndEffectIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), recipe[entry], x, y);
			ri.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), recipe[entry], x, y);

			icons.add(new ItemIcon(recipe[entry], x, y));
		}
	}
	
	public void drawIcon(ItemStack stack, int x, int y){
		RenderItem ri = new RenderItem();
		ri.renderItemAndEffectIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), stack, x, y);
		ri.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), stack, x, y);

		icons.add(new ItemIcon(stack, x, y));
	}
	
	public void renderOverlay(GuiEntry entry, int width, int height, int left, int top){
		TextureManager tm = Minecraft.getMinecraft().getTextureManager();
		tm.bindTexture(new ResourceLocation("bloodutils:textures/gui/crafting.png"));
		entry.drawTexturedModalRect(left, (height/2 - 18) + (18*0) - 17, 0, 0, width, height);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void initGui(int width, int height, int left, int top,
			EntityPlayer player, List buttonList){
		
	}
	
	@Override
	public void actionPerformed(GuiButton button){
		
	}
	
	static class ItemIcon {
		public ItemIcon(ItemStack stack, int x, int y){
			this.stack = stack;
			this.x = x;
			this.y = y;
		}
		public ItemStack stack;
		public int x, y;
		
		public void onMouseBetween(int mX, int mY){
			int xSize = x + 16;
			int ySize = y + 16;
			
			
			if(mX > x && mX < xSize && mY > y && mY < ySize){
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				if(stack != null && stack.getDisplayName() != null)
					Minecraft.getMinecraft().fontRenderer.drawString(stack.getDisplayName(), mX + 6, mY, new Color(139, 137, 137).getRGB());
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}
		}		
	}
}