package WayofTime.bloodmagic.livingArmour.upgrade;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

public class LivingArmourUpgradeFallProtect extends LivingArmourUpgrade {
    public static final int[] costs = new int[]{2, 5, 9, 15, 25};
    public static final float[] protectionLevel = new float[]{0.2f, 0.4f, 0.6f, 0.8f, 1f};

    public LivingArmourUpgradeFallProtect(int level) {
        super(level);
    }

    
    public float getDamageMultiplier() {
        return 1-protectionLevel[this.level];
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
    public String getTranslationKey() {
        return tooltipBase + "fallProtect";
    }
}
