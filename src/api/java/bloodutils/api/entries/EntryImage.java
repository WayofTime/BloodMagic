package bloodutils.api.entries;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import bloodutils.api.classes.guide.GuiEntry;
import bloodutils.api.helpers.GuiHelper;

public class EntryImage implements IEntry{
	public EntryImage(String resource, int iconWidth, int iconHeight){
		this.resource = new ResourceLocation(resource);
		this.iconWidth = iconWidth;
		this.iconHeight = iconHeight;
	}
	public ResourceLocation resource;
	public int iconWidth;
	public int iconHeight;
	
	public EntryImage(String resource, int iconWidth, int iconHeight, String entryName){
		this.resource = new ResourceLocation(resource);
		this.iconWidth = iconWidth;
		this.iconHeight = iconHeight;
		this.entryName = entryName;
	}
	public String entryName;


	@Override
	public void draw(GuiEntry entry, int width, int height, int left, int top, EntityPlayer player, String key, int page, int mX, int mY){
		drawImage(entry, width, height, left, top, player, key, page, mX, mY);
		drawText(entry, width, height, left, top, player, key, page, mX, mY);
	}
	
	public void drawImage(GuiEntry entry, int width, int height, int left, int top, EntityPlayer player, String key, int page, int mX, int mY){
		int x = left + 32;
		int y = top + 10;
		Minecraft.getMinecraft().getTextureManager().bindTexture(this.resource);

		GuiHelper.drawScaledIconWithoutColor(x, y, this.iconWidth, this.iconHeight, 0);
	}
	
	public void drawText(GuiEntry entry, int width, int height, int left, int top, EntityPlayer player, String key, int page, int mX, int mY){
		int x, y;
		
		if(this.entryName == null)
			this.entryName = key;
		
		String s = StatCollector.translateToLocal("bu.entry." + this.entryName + "." + page);
		x = left + width / 2 - 58;
		y = (top + 15) + 65;

		Minecraft.getMinecraft().fontRenderer.setUnicodeFlag(true);
		Minecraft.getMinecraft().fontRenderer.drawSplitString(s, x, y, 110, 0);	
		Minecraft.getMinecraft().fontRenderer.setUnicodeFlag(false);

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