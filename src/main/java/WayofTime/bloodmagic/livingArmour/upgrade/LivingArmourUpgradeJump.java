package WayofTime.bloodmagic.livingArmour.upgrade;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;
import net.minecraft.nbt.NBTTagCompound;

public class LivingArmourUpgradeJump extends LivingArmourUpgrade
{
    public static final int[] costs = new int[] { 7, 13, 22, 40, 65, 90, 130, 180, 250, 350 };
    public static final double[] jumpModifier = new double[] { 0.15, 0.3, 0.45, 0.6, 0.75, 0.9, 1.05, 1.2, 1.35, 1.5 };

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
