package pneumaticCraft.api.client.pneumaticHelmet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import pneumaticCraft.api.client.IGuiAnimatedStat;

/**
 * To add upgrades for in the Pneumatic Helmet implement this interface. You can add members to this class, however these can only 
 * be client sided members as this class will be used as singleton. Therefore, only one of these instances exist at the server side
 * so any member that is used server side will affect every player.
 *
 */
public interface IUpgradeRenderHandler{

    /**
     * Return here the name of the upgrade. This is displayed in the formatting [upgradeName] + " " + "found"/"not found"  on
     * initialization of the helmet.
     * @return
     */
    public String getUpgradeName();

    /**
     * Being called from PneumaticCraft's config handler, you can use this method to read settings like stat positions
     * @param config PneumaticCraft's config file.
     */
    public void initConfig(Configuration config);

    /**
     * When called this should save the settings to the config file. Called when changed a setting. When you want to use
     * PneumaticCraft's config file, save a reference of it somewhere in this class when the config gets passed in the
     * initConfig() method (this always will be called first).
     */
    public void saveToConfig();

    /**
     * This method will be called every client tick, and should be used to update logic like the tracking and velocities of stuff.
     * @param player
     * @param rangeUpgrades amount of range upgrades installed in the helmet.
     */
    public void update(EntityPlayer player, int rangeUpgrades);

    /**
     * Called in the 3D render stage (renderWorldLastEvent)
     * @param partialTicks
     */
    public void render3D(float partialTicks);

    /**
     * Called in the 2D render stage (Render Tick Handler)
     * @param partialTicks
     * @param helmetEnabled is true when isEnabled() returned true earlier. Can be used to close AnimatedStats for instance.
     * However this is already handled if you return an AnimatedStat in getAnimatedStat().
     */
    public void render2D(float partialTicks, boolean helmetEnabled);

    /**
     * You can return a GuiAnimatedStat here, that the HUDHandler will pick up and render. It also automatically opens and closes
     * the stat when needed. The GuiMoveStat uses this method to retrieve the to be moved stat.
     * @return null if no stat used.
     */
    public IGuiAnimatedStat getAnimatedStat();

    /**
     * Should return true if this upgrade handler is enabled for the given stacks placed in the helmet.
     * @param upgradeStacks
     * @return
     */
    public boolean isEnabled(ItemStack[] upgradeStacks);

    /**
     * Returns the usage in mL/tick when this upgrade handler is enabled.
     * @param rangeUpgrades amount of range upgrades installed in the helmet.
     * @param player
     * @return usage in mL/tick
     */
    public float getEnergyUsage(int rangeUpgrades, EntityPlayer player);

    /**
     * Called when (re-)equipped the helmet this method should be used to clear information like current tracked entities.
     * So clearing lists and other references as this handler should re-acquire when reinstalled.
     */
    public void reset();

    /**
     * When you have some options for your upgrade handler you could return a new instance of an IOptionsPage.
     * When you do so, it will automatically get picked up by the options handler, and it will be added to the 
     * options GUI when this upgrade returns true when calling isEnabled(). Returning null is valid.
     * @return
     */
    public IOptionPage getGuiOptionsPage();
}
