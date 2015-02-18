package WayofTime.alchemicalWizardry.book.entries;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import WayofTime.alchemicalWizardry.book.classes.guide.GuiEntry;

public class EntryImageCustomText extends EntryImage implements IEntryCustomText
{
	public EntryImageCustomText(String resource, int iconWidth, int iconHeight)
	{
		super(resource, iconWidth, iconHeight);
	}
	
	public EntryImageCustomText(String resource, int iconWidth, int iconHeight, String entryName)
	{
		super(resource, iconWidth, iconHeight, entryName);
	}

	public String str = "";
	
	@Override
	public String getText() 
	{
		return str;
	}

	@Override
	public void setText(String str) 
	{
		this.str = str;
	}
	
	public void drawText(GuiEntry entry, int width, int height, int left, int top, EntityPlayer player, String key, int page, int mX, int mY)
	{
		int x, y;
		
		if(this.entryName == null)
			this.entryName = key;
		
		String s = this.str;
		x = left + width / 2 - 58;
		y = (top + 15) + 65;

		Minecraft.getMinecraft().fontRenderer.setUnicodeFlag(true);
		Minecraft.getMinecraft().fontRenderer.drawSplitString(s, x, y, 110, 0);	
		Minecraft.getMinecraft().fontRenderer.setUnicodeFlag(false);

	}
}
