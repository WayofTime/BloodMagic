package WayofTime.bloodmagic.livingArmour.upgrade;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;

public class LivingArmourUpgradePhysicalProtect extends LivingArmourUpgrade
{
    public static final int[] costs = new int[] { 5, 10, 18, 35, 65, 100, 160, 220, 280, 350 };
    public static final double[] protectionLevel = new double[] { 0.1, 0.3, 0.4, 0.5, 0.6, 0.7, 0.75, 0.77, 0.80, 0.83 };

    public LivingArmourUpgradePhysicalProtect(int level)
    {
        super(level);
    }

    @Override
    public double getArmourProtection(EntityLivingBase wearer, DamageSource source)
    {
        if (source.getEntity() != null)
        {
            return protectionLevel[this.level];
        }

        return 0;
    }

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".upgrade.physicalProtect";
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
        return tooltipBase + "physicalProtect";
    }
}
