package bloodutils.api.entries;

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

import WayofTime.alchemicalWizardry.api.altarRecipeRegistry.AltarRecipe;
import bloodutils.api.classes.guide.GuiEntry;

public class EntryAltarRecipe implements IEntry{
	public EntryAltarRecipe(AltarRecipe recipes){		
		this.recipes = recipes;
		populate(recipes);
	}
	public AltarRecipe recipes;
	
	public ItemStack input;
	public ItemStack output;
	public int essence;
	
	public ArrayList<ItemIcon> icons = new ArrayList<ItemIcon>();
	
	@SuppressWarnings("unchecked")
	public void populate(AltarRecipe recipe){
		this.input = recipe.requiredItem;
		this.output = recipe.result;
		this.essence = recipe.liquidRequired;
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
		
		x = left + width / 2 - (65-45);
		y = (height/2 - 36) + (18*(4-3));
		drawIcon(this.input, x, y);
		
		/** Result */
		x = left + width / 2 - (65-(48+48)-5);
		y = (height/2 - 36) + (18*(4-3));
		drawIcon(this.output, x, y);
		
		RenderHelper.disableStandardItemLighting();

		GL11.glDisable(GL11.GL_LIGHTING);
 
		GL11.glPopMatrix();

		for(ItemIcon icon : icons){
			if(icon.stack != null)
				icon.onMouseBetween(mX, mY);
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
		tm.bindTexture(new ResourceLocation("bloodutils:textures/gui/altar.png"));
		entry.drawTexturedModalRect(left, (height/2 - 36) + (18*0) - 17, 0, 0, width, height);
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
