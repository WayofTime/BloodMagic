package pneumaticCraft.api.client.pneumaticHelmet;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

/**
 * The Option Page is the page you see when you press 'F' (by default) with a Pneumatic Helmet equipped. You can register this class
 * by returning a new instance of this class at {@link IUpgradeRenderHandler#getGuiOptionsPage()}
 */
public interface IOptionPage{

    /**
     * This string is used in the text of the button of this page.
     * @return
     */
    public String getPageName();

    /**
     * Here you can initialize your buttons and stuff like with a GuiScreen. For buttons, don't use button id 100 and up, as they
     * will be used as selection buttons for other option pages in the main GuiScreen.
     * @param gui
     */
    public void initGui(IGuiScreen gui);

    /**
     * Same as GuiScreen#actionPerformed(GuiButton).
     * @param button
     */
    public void actionPerformed(GuiButton button);

    /**
     * Same as {@link GuiScreen#drawScreen(int, int, float)}
     * Here you can render additional things like text.
     * @param x
     * @param y
     * @param partialTicks
     */
    public void drawScreen(int x, int y, float partialTicks);

    /**
     * Same as GuiScreen#keyTyped(char, int).
     * @param ch
     * @param key
     */
    public void keyTyped(char ch, int key);
}
