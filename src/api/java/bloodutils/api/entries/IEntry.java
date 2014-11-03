package bloodutils.api.entries;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import bloodutils.api.classes.guide.GuiEntry;

public interface IEntry {
	/**
	 * This get's called in GuiEntry, you can do whatever you want here (images, recipes, icons, text, combination of them)
	 * @param width
	 * @param height
	 * @param left
	 * @param top
	 * @param player
	 * The player who has the book open
	 */
	public void draw(GuiEntry entry, int width, int height, int left, int top, EntityPlayer player, String key, int page, int mX, int mY);
	
	@SuppressWarnings("rawtypes")
	public void initGui(int width, int height, int left, int top, EntityPlayer player, List buttonList);

	public void actionPerformed(GuiButton button);
}