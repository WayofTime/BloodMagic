package pneumaticCraft.api.client.pneumaticHelmet;

import java.util.List;

import net.minecraft.client.gui.FontRenderer;

/**
 * Just an interface to give access to GuiSreen#buttonList and GuiScreen#fontRenderer. An instance of this class can
 * safely be casted to GuiSreen if needed.
 */
public interface IGuiScreen{
    public List getButtonList();

    public FontRenderer getFontRenderer();
}
