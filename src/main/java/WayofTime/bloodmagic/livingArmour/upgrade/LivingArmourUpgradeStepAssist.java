package WayofTime.bloodmagic.livingArmour.upgrade;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import net.minecraft.nbt.NBTTagCompound;

public class LivingArmourUpgradeStepAssist extends LivingArmourUpgrade {
    public static final int[] costs = new int[]{20};
    public static final float[] assist = new float[]{Constants.Misc.ALTERED_STEP_HEIGHT};

//    public static final double[] speedModifier = new double[] { 0.1, 0.2, 0.3, 0.4, 0.5, 0.7, 0.9, 1.1, 1.3, 1.5 };
//    public static final int[] sprintSpeedTime = new int[] { 0, 0, 0, 0, 0, 20, 60, 60, 100, 200 };
//    public static final int[] sprintSpeedLevel = new int[] { 0, 0, 0, 0, 0, 0, 0, 1, 1, 2 };
//    public static final int[] healthModifier = new int[] { 0, 0, 0, 0, 0, 0, 0, 4, 10, 20 };
//    public static final int[] sprintRegenTime = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 25 };

    public LivingArmourUpgradeStepAssist(int level) {
        super(level);
    }

    @Override
    public String getUniqueIdentifier() {
        return BloodMagic.MODID + ".upgrade.stepAssist";
    }

    @Override
    public int getMaxTier() {
        return 1;
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
        return tooltipBase + "stepAssist";
    }

    public float getStepAssist() {
        return assist[this.level];
    }
}
