package WayofTime.bloodmagic.livingArmour;

import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

/**
 * An interface this is used purely for internal implementation.
 *
 * @author WayofTime
 */
public interface ILivingArmour {
    Multimap<String, AttributeModifier> getAttributeModifiers();

    boolean canApplyUpgrade(PlayerEntity user, LivingArmourUpgrade upgrade);

    boolean upgradeArmour(PlayerEntity user, LivingArmourUpgrade upgrade);

    boolean removeUpgrade(PlayerEntity user, LivingArmourUpgrade upgrade);

    void notifyPlayerOfUpgrade(PlayerEntity user, LivingArmourUpgrade upgrade);

    /**
     * Ticks the upgrades and stat trackers, passing in the world and player as
     * well as the LivingArmour
     *
     * @param world  - The World
     * @param player - The player wearing the Armour
     */
    void onTick(World world, PlayerEntity player);

    void readFromNBT(CompoundNBT tag);

    void writeToNBT(CompoundNBT tag, boolean forceWrite);

    /**
     * Writes the LivingArmour to the NBTTag. This will only write the trackers
     * that are dirty.
     *
     * @param tag - The NBT tag to write to
     */
    void writeDirtyToNBT(CompoundNBT tag);

    void writeToNBT(CompoundNBT tag);
}
