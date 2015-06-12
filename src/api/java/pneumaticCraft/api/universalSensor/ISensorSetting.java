package pneumaticCraft.api.universalSensor;

import java.util.List;

import net.minecraft.client.gui.FontRenderer;

import org.lwjgl.util.Rectangle;

public interface ISensorSetting{
    /**
     * Should return the button path the player has to follow in which this setting is stored.
     * For instance, when the sensor should be located in player and is called speed, you should return "entityTracker/player/speed".
     * "entityTracker" indicates that this sensor needs an Entity Tracker upgrade to run. You can choose from the following upgrades:
     * 
     * -entityTracker
     * -blockTracker
     * -gpsTool  (so you can use a certain coordinate (within range) to measure on)
     * -volume
     * -dispenser
     * -speed
     * -itemLife
     * -itemSearch
     * -coordinateTracker
     * -range
     * -security
     * 
     * You can allow only sensors to work by more than one upgrade, by seperating the upgrades with a '_'. For example,
     * "entityTracker_speed" will only let the sensor be chosen when both an Entity Tracker and a Speed Upgrade are inserted.
     * @return
     */
    public String getSensorPath();

    /**
     * When returned true, the GUI will enable the textbox writing, otherwise not.
     * @return
     */
    public boolean needsTextBox();

    /**
     * Called by GuiScreen#drawScreen this method can be used to render additional things like status/info text.
     * @param fontRenderer
     */
    public void drawAdditionalInfo(FontRenderer fontRenderer);

    /**
     * Should return the description of this sensor displayed in the GUI stat. Information should at least include
     * when this sensor emits redstone and how (analog (1 through 15), or digital).
     * @return
     */
    public List<String> getDescription();

    /**
     * Not being used at the moment, I recommend returning null for now. It is going to be used to allow sensors to decide their
     * status on a item which can be inserted in a slot in the GUI if this method returns a rectangle with the coordinates of
     * the slot.
     * @return
     */
    public Rectangle needsSlot();
}
