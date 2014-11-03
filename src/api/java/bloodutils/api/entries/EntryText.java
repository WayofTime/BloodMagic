package bloodutils.api.entries;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import bloodutils.api.classes.guide.GuiEntry;

public class EntryText implements IEntry{
	public EntryText(){
		
	}
	
	public EntryText(String entryName){
		this.entryName = entryName;
	}
	public String entryName;
	
	@Override
	public void draw(GuiEntry entry, int width, int height, int left, int top, EntityPlayer player, String key, int page, int mX, int mY){
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
	
	@SuppressWarnings("rawtypes")
	@Override
	public void initGui(int width, int height, int left, int top,
			EntityPlayer player, List buttonList){

	}
	
	@Override
	public void actionPerformed(GuiButton button){
		
	}
}