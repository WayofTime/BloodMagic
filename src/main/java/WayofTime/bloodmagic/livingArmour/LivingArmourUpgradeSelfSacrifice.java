package WayofTime.bloodmagic.livingArmour;

import net.minecraft.nbt.NBTTagCompound;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;

public class LivingArmourUpgradeSelfSacrifice extends LivingArmourUpgrade
{
    public static final int[] costs = new int[] { 10, 25, 50, 80, 120 };
    public static final double[] sacrificeModifier = new double[] { 0.2, 0.4, 0.6, 0.8, 1.0 };

    public LivingArmourUpgradeSelfSacrifice(int level)
    {
        super(level);
    }

    public double getSacrificeModifier()
    {
        return sacrificeModifier[this.level];
    }

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".upgrade.selfSacrifice";
    }

    @Override
    public int getMaxTier()
    {
        return 5; // Set to here until I can add more upgrades to it.
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
        return tooltipBase + "selfSacrifice";
    }
}
