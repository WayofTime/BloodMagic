package WayofTime.bloodmagic.api.livingArmour;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.google.common.collect.Multimap;

/**
 * An interface this is used purely for internal implementation.
 * 
 * @author WayofTime
 * 
 */
public interface ILivingArmour
{
    public Multimap<String, AttributeModifier> getAttributeModifiers();

    public boolean upgradeArmour(EntityPlayer user, LivingArmourUpgrade upgrade);

    public void notifyPlayerOfUpgrade(EntityPlayer user, LivingArmourUpgrade upgrade);

    /**
     * Ticks the upgrades and stat trackers, passing in the world and player as
     * well as the LivingArmour
     * 
     * @param world
     * @param player
     */
    public void onTick(World world, EntityPlayer player);

    public void readFromNBT(NBTTagCompound tag);

    public void writeToNBT(NBTTagCompound tag, boolean forceWrite);

    /**
     * Writes the LivingArmour to the NBTTag. This will only write the trackers
     * that are dirty.
     * 
     * @param tag
     */
    public void writeDirtyToNBT(NBTTagCompound tag);

    public void writeToNBT(NBTTagCompound tag);
}
