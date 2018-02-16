package WayofTime.bloodmagic.livingArmour;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.List;

public abstract class StatTracker {
    private boolean isDirty = false;

    public abstract String getUniqueIdentifier();

    /**
     * When called the StatTracker should reset all of its data, including
     * upgrades.
     */
    public abstract void resetTracker();

    public abstract void readFromNBT(NBTTagCompound tag);

    public abstract void writeToNBT(NBTTagCompound tag);

    /**
     * Called each tick to update the tracker's information. Called in
     * LivingArmour
     *
     * @param world        World the player is in
     * @param player       The player that has the armour equipped
     * @param livingArmour The equipped LivingArmour
     * @return True if there is a new upgrade unlocked this tick.
     */
    public abstract boolean onTick(World world, EntityPlayer player, LivingArmour livingArmour);

    public abstract void onDeactivatedTick(World world, EntityPlayer player, LivingArmour livingArmour);

    public abstract List<LivingArmourUpgrade> getUpgrades();

    /**
     * Used to obtain the progress from the current level to the next level.
     * <p>
     * 0.0 being 0% - 1.0 being 100%.
     *
     * @param livingArmour The equipped LivingArmour
     * @return the progress from the current level to the next level.
     */
    public double getProgress(LivingArmour livingArmour, int currentLevel) {
        return 1.0D;
    }

    public final boolean isDirty() {
        return isDirty;
    }

    public final void markDirty() {
        this.isDirty = true;
    }

    public final void resetDirty() {
        this.isDirty = false;
    }

    public abstract boolean providesUpgrade(String key);

    public abstract void onArmourUpgradeAdded(LivingArmourUpgrade upgrade);
}
