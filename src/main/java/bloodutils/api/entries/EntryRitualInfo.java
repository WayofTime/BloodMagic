package bloodutils.api.entries;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import bloodutils.api.classes.guide.GuiEntry;

public class EntryRitualInfo implements IEntry{
	public EntryRitualInfo(int cost){
		
		this.cost = cost;
	}
	public int cost;
	
	@Override
	public void draw(GuiEntry entry, int width, int height, int left, int top, EntityPlayer player, String key, int page, int mX, int mY){
		int x, y;
		x = left + width / 2 - 58;
		y = (top + 15);
		Minecraft.getMinecraft().fontRenderer.setUnicodeFlag(true);
		Minecraft.getMinecraft().fontRenderer.drawString("Cost: " + this.cost + " LP", x, y, 0);
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
