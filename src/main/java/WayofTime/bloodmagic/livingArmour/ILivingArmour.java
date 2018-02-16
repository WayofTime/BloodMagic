package WayofTime.bloodmagic.livingArmour;

import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * An interface this is used purely for internal implementation.
 *
 * @author WayofTime
 */
public interface ILivingArmour {
    Multimap<String, AttributeModifier> getAttributeModifiers();

    boolean canApplyUpgrade(EntityPlayer user, LivingArmourUpgrade upgrade);

    boolean upgradeArmour(EntityPlayer user, LivingArmourUpgrade upgrade);

    boolean removeUpgrade(EntityPlayer user, LivingArmourUpgrade upgrade);

    void notifyPlayerOfUpgrade(EntityPlayer user, LivingArmourUpgrade upgrade);

    /**
     * Ticks the upgrades and stat trackers, passing in the world and player as
     * well as the LivingArmour
     *
     * @param world  - The World
     * @param player - The player wearing the Armour
     */
    void onTick(World world, EntityPlayer player);

    void readFromNBT(NBTTagCompound tag);

    void writeToNBT(NBTTagCompound tag, boolean forceWrite);

    /**
     * Writes the LivingArmour to the NBTTag. This will only write the trackers
     * that are dirty.
     *
     * @param tag - The NBT tag to write to
     */
    void writeDirtyToNBT(NBTTagCompound tag);

    void writeToNBT(NBTTagCompound tag);
}
