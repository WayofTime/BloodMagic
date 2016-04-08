package WayofTime.bloodmagic.livingArmour.upgrade;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;
import net.minecraft.nbt.NBTTagCompound;

public class LivingArmourUpgradeJump extends LivingArmourUpgrade
{
    public static final int[] costs = new int[] { 3, 6, 11, 23, 37, 50, 70, 100, 140, 200 };
    public static final double[] jumpModifier = new double[] { 0.10, 0.2, 0.3, 0.4, 0.5, 0.7, 0.9, 1.1, 1.3, 1.5 };

    public LivingArmourUpgradeJump(int level)
    {
        super(level);
    }

    public double getJumpModifier()
    {
        return jumpModifier[this.level];
    }

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".upgrade.jump";
    }

    @Override
    public int getMaxTier()
    {
        return 10; // Set to here until I can add more upgrades to it.
    }

    @Override
    public int getCostOfUpgrade()
    {
        return costs[this.level];
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        // EMPTY
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        // EMPTY
    }

    @Override
    public String getUnlocalizedName()
    {
        return tooltipBase + "jump";
    }
}
