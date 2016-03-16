package WayofTime.bloodmagic.compat.thaumcraft;

import net.minecraft.nbt.NBTTagCompound;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;

public class LivingArmourUpgradeThaumRevealing extends LivingArmourUpgrade
{

    public LivingArmourUpgradeThaumRevealing(int level)
    {
        super(level);
    }

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".upgrade.revealing";
    }

    @Override
    public String getUnlocalizedName()
    {
        return tooltipBase + "revealing";
    }

    @Override
    public int getMaxTier()
    {
        return 1;
    }

    @Override
    public int getCostOfUpgrade()
    {
        return 5;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {

    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {

    }
}
