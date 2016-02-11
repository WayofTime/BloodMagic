package WayofTime.bloodmagic.compat.thaumcraft;

import net.minecraft.nbt.NBTTagCompound;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;

public class LivingArmourUpgradeThaumRunicShielding extends LivingArmourUpgrade
{
    public static final int[] costs = new int[] { 5, 12, 20, 35, 49, 78, 110, 160, 215, 320 };
    public static final int[] healthModifier = new int[] { 2, 4, 6, 8, 10, 13, 16, 19, 22, 25 };

    public LivingArmourUpgradeThaumRunicShielding(int level)
    {
        super(level);
    }

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".upgrade.thaumRunicShielding";
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
        return tooltipBase + "thaumRunicShielding";
    }

    @Override
    public int getRunicShielding()
    {
        return healthModifier[this.level];
    }
}