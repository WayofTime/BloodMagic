package WayofTime.bloodmagic.livingArmour.upgrade;

import net.minecraft.nbt.NBTTagCompound;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;

public class LivingArmourUpgradeGraveDigger extends LivingArmourUpgrade
{
    public static final int[] costs = new int[] { 5, 12, 20, 35, 49, 78, 110, 160, 215, 320 };
    public static final double[] damageBoost = new double[] { 0.2, 0.4, 0.6, 0.8, 1, 1.2, 1.4, 1.6, 1.8, 2 };

    public LivingArmourUpgradeGraveDigger(int level)
    {
        super(level);
    }

    public double getDamageModifier()
    {
        return damageBoost[this.level];
    }

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".upgrade.graveDigger";
    }

    @Override
    public int getMaxTier()
    {
        return 10;
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
        return tooltipBase + "graveDigger";
    }
}