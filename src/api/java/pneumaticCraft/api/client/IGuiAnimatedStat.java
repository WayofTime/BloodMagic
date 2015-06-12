package pneumaticCraft.api.client;

import java.awt.Rectangle;
import java.util.List;

/**
 * This interface doesn't have to be implemented. In PneumaticCraft there already is one class which implements this interface
 * which is used many times in PneumaticCraft (GUI stats, Pneumatic Helmet 2D and 3D stats). You can get an instance of this
 * class as well. Information about this you can find in GuiAnimatedStatSupplier.java. Implementing your own version of
 * animated stats can be implemented as well via this interface, and they will interact with the PneumaticCraft GuiAnimatedStats
 * if you implement it correctly.
 */

public interface IGuiAnimatedStat{

    /**
     * When you call this method with a set of coordinates representing the button location and dimensions, you'll get
     * these parameters back scaled to the GuiAnimatedStat's scale.
     * @param origX Button start X.
     * @param origY Button start Y.
     * @param width Button width.
     * @param height Button height.
     * @return rectangle containing the new location and dimensions.
     */
    public Rectangle getButtonScaledRectangle(int origX, int origY, int width, int height);

    /**
     * When passed 0.5F for example, the text of the stat will be half as big (so more text can fit into a certain area).
     * @param scale
     */
    public void scaleTextSize(float scale);

    /**
     * Returns true if the statistic expands to the left.
     * @return
     */
    public boolean isLeftSided();

    /**
     * Returns true if the statistic is done with expanding (when text will be displayed).
     * @return
     */
    public boolean isDoneExpanding();

    /**
     * Pass true if the statistic should expand to the left, otherwise false.
     * @param leftSided
     */
    public void setLeftSided(boolean leftSided);

    /**
     * Sets the main text of this stat. Every line should be stored in a seperate list element. Upon rendering,
     * EnumChatFormattings will be respected. When you call this method, Too long lines will be divided into multiple shorter ones
     * to fit in the GUI.
     * @param text
     * @return this, so you can chain calls.
     */
    public IGuiAnimatedStat setText(List<String> text);

    /**
     * Sets the line to a single line. Upon rendering,
     * EnumChatFormattings will be respected. When you call this method, Too long lines will be divided into multiple shorter ones
     * to fit in the GUI.
     * @param text
     * @return this, so you can chain calls.
     */
    public IGuiAnimatedStat setText(String text);

    /**
     * Sets the main text of this stat. Every line should be stored in a seperate list element. Upon rendering,
     * EnumChatFormattings will be respected. This version of the text setting doesn't handle too long lines.
     * @param text
     */
    public void setTextWithoutCuttingString(List<String> text);

    /**
     * Sets the title of this stat. It will automatically get the yellow color assigned.
     * @param title
     */
    public void setTitle(String title);

    /**
     * Returns the title of this stat (obviously without color prefix).
     * @return
     */
    public String getTitle();

    /**
     * Defines what dimensions the stat should have when it is not expanded (default 17x17) and resets the stat to these dimensions.
     * Used in PneumaticCraft by the block/entity tracker stats, they are 0x0 when not expanded so it looks like they expand
     * (and appear) from nothing.
     * @param minWidth
     * @param minHeight
     */
    public void setMinDimensionsAndReset(int minWidth, int minHeight);

    /**
     * When this stat gets a parent stat assigned, the y of this stat will be the same as the parent's plus this stat's
     * baseY. This will cause this stat to move up and down when the parent's stat expands/moves.
     * @param stat
     */
    public void setParentStat(IGuiAnimatedStat stat);

    /**
     * Sets the x location of this stat.
     * @param x
     */
    public void setBaseX(int x);

    /**
     * Sets the base Y of this stat.
     * @param y
     */
    public void setBaseY(int y);

    /**
     * Returns the real Y of this stat. This is the same as getBaseY when there is no parent stat, but if there is this method
     * returns the value described in setParentStat(IGuiAnimatedStat stat).
     * @return
     */
    public int getAffectedY();

    public int getBaseX();

    public int getBaseY();

    /**
     * Returns the Y size of this stat.
     * @return
     */
    public int getHeight();

    /**
     * Returns the X size of this stat.
     * @return
     */
    public int getWidth();

    public Rectangle getBounds();

    /**
     * This method should be called every game tick to update the logic of the stat (expanding of the stat).
     */
    public void update();

    /**
     * Should be called every render tick when and where you want to render the stat.
     * @param mouseX
     * @param mouseY
     * @param partialTicks
     */
    public void render(int mouseX, int mouseY, float partialTicks);

    /**
     * This method will handle mouse clicks. This will handle open/closing of the stat when the mouse clicks it.
     * @param x
     * @param y
     * @param button
     * @return
     */
    public void onMouseClicked(int x, int y, int button);

    /**
     * Forces the stat to close.
     */
    public void closeWindow();

    /**
     * Forces the stat to expand.
     */
    public void openWindow();

    /**
     * Returns true if the stat is expanding.
     * @return
     */
    public boolean isClicked();

}
