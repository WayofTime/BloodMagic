package WayofTime.bloodmagic.livingArmour;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public abstract class LivingArmourUpgrade {
    public static String chatBase = "chat.bloodmagic.livingArmour.upgrade.";
    public static String tooltipBase = "tooltip.bloodmagic.livingArmour.upgrade.";

    /**
     * Upgrade level 0 is the first upgrade. Upgrade goes from 0 to getMaxTier()
     * - 1.
     */
    protected int level = 0;

    /**
     * The LivingArmourUpgrade must have a constructor that has a single integer
     * parameter. Upgrades may have other constructors, but must have one of
     * these.
     *
     * @param level The level of the upgrade
     */
    public LivingArmourUpgrade(int level) {
        this.level = Math.min(level, getMaxTier() - 1);
    }

    public double getAdditionalDamageOnHit(double damage, EntityPlayer wearer, EntityLivingBase hitEntity, ItemStack weapon) {
        return 0;
    }

    public double getKnockbackOnHit(EntityPlayer wearer, EntityLivingBase hitEntity, ItemStack weapon) {
        return 0;
    }

    /**
     * Percentage of damage blocked. This stacks multiplicities with other
     * upgrades.
     *
     * @return 0 for no damage blocked, 1 for full damage blocked
     */
    public double getArmourProtection(EntityLivingBase wearer, DamageSource source) {
        return 0;
    }

    public int getUpgradeLevel() {
        return this.level;
    }

    public abstract String getUniqueIdentifier();

    public abstract String getTranslationKey();

    public abstract int getMaxTier();

    public abstract int getCostOfUpgrade();

    public void onTick(World world, EntityPlayer player, ILivingArmour livingArmour) {
    }

    public Multimap<String, AttributeModifier> getAttributeModifiers() {
        return HashMultimap.create();
    }

    public double getMiningSpeedModifier(EntityPlayer player) {
        return 1;
    }

    public abstract void writeToNBT(NBTTagCompound tag);

    public abstract void readFromNBT(NBTTagCompound tag);

    public int getRunicShielding() {
        return 0;
    }

    public boolean runOnClient() {
        return false;
    }

    public boolean isDowngrade() {
        return false;
    }
}
