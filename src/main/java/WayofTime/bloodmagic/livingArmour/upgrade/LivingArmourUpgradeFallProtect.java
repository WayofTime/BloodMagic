package WayofTime.bloodmagic.livingArmour.upgrade;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

public class LivingArmourUpgradeFallProtect extends LivingArmourUpgrade {
    public static final int[] costs = new int[]{2, 5, 9, 15, 25};
    public static final double[] protectionLevel = new double[]{0.2, 0.4, 0.6, 0.8, 1};

    public LivingArmourUpgradeFallProtect(int level) {
        super(level);
    }

    @Override
    public double getArmourProtection(EntityLivingBase wearer, DamageSource source) {
        if (source.equals(DamageSource.FALL)) {
            return protectionLevel[this.level];
        }

        return 0;
    }

    @Override
    public String getUniqueIdentifier() {
        return BloodMagic.MODID + ".upgrade.fallProtect";
    }

    @Override
    public int getMaxTier() {
        return 5; // Set to here until I can add more upgrades to it.
    }

    @Override
    public int getCostOfUpgrade() {
        return costs[this.level];
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        // EMPTY
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        // EMPTY
    }

    @Override
    public String getUnlocalizedName() {
        return tooltipBase + "fallProtect";
    }
}
