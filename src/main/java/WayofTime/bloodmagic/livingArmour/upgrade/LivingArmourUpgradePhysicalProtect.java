package WayofTime.bloodmagic.livingArmour.upgrade;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

public class LivingArmourUpgradePhysicalProtect extends LivingArmourUpgrade {
    public static final int[] costs = new int[]{5, 10, 18, 35, 65, 100, 140, 190, 250, 300};
    public static final double[] protectionLevel = new double[]{0.1, 0.3, 0.4, 0.5, 0.6, 0.7, 0.75, 0.77, 0.80, 0.83};

    public LivingArmourUpgradePhysicalProtect(int level) {
        super(level);
    }

    @Override
    public double getArmourProtection(EntityLivingBase wearer, DamageSource source) {
        if (source.getTrueSource() != null && !source.isMagicDamage() && !source.isProjectile()) {
            return protectionLevel[this.level];
        }

        return 0;
    }

    @Override
    public String getUniqueIdentifier() {
        return BloodMagic.MODID + ".upgrade.physicalProtect";
    }

    @Override
    public int getMaxTier() {
        return 10; // Set to here until I can add more upgrades to it.
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
    public String getTranslationKey() {
        return tooltipBase + "physicalProtect";
    }
}
