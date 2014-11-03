package bloodutils.api.entries;

import java.awt.Color;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import bloodutils.api.classes.guide.GuiEntry;

public class EntryItemText implements IEntry{
	public EntryItemText(ItemStack stack){
		this.stack = stack;
	}
	public ItemStack stack;

	public EntryItemText(ItemStack stack, String entryName){
		this.stack = stack;
		this.entryName = entryName;
	}
	public String entryName;

	@Override
	public void draw(GuiEntry entry, int width, int height, int left, int top, EntityPlayer player, String key, int page, int mX, int mY){
		drawText(entry, width, height, left, top, player, key, page, mX, mY);
		drawBlock(entry, width, height, left, top, player, key, page, mX, mY);
	}
	
	public void drawText(GuiEntry entry, int width, int height, int left, int top, EntityPlayer player, String key, int page, int mX, int mY){
		int x, y;
		
		if(this.entryName == null)
			this.entryName = key;
		
		String s = StatCollector.translateToLocal("bu.entry." + this.entryName + "." + page);
		x = left + width / 2 - 58;
		y = (top + 15);

		Minecraft.getMinecraft().fontRenderer.setUnicodeFlag(true);
		Minecraft.getMinecraft().fontRenderer.drawSplitString(s, x, y, 110, 0);	
		Minecraft.getMinecraft().fontRenderer.setUnicodeFlag(false);

	}
	
	public void drawBlock(GuiEntry entry, int width, int height, int left, int top, EntityPlayer player, String key, int page, int mX, int mY){
		
		RenderItem ri = new RenderItem();
		GL11.glPushMatrix();
		
		GL11.glScaled(3, 3, 1);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		ri.renderItemAndEffectIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), stack, left - (left/2) + 2, top + 20);
		ri.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), stack, left - (left/2) + 2, top + 20);

        RenderHelper.disableStandardItemLighting();
        
        GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void initGui(int width, int height, int left, int top,
			EntityPlayer player, List buttonList) {
		
	}

	@Override
	public void actionPerformed(GuiButton button){		
		
	}
}