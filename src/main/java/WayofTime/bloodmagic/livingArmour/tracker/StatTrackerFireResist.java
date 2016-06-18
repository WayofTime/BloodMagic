package WayofTime.bloodmagic.livingArmour.tracker;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.api.livingArmour.StatTracker;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeFireResist;

public class StatTrackerFireResist extends StatTracker
{
    public int totalFireTicks = 0;

    public static int[] fireTicksRequired = new int[] { 60 * 20, 3 * 60 * 20, 10 * 60 * 20, 20 * 60 * 20, 25 * 60 * 20 };

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".tracker.fire";
    }

    @Override
    public void resetTracker()
    {
        this.totalFireTicks = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        totalFireTicks = tag.getInteger(Constants.Mod.MODID + ".tracker.fire");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        tag.setInteger(Constants.Mod.MODID + ".tracker.fire", totalFireTicks);
    }

    @Override
    public boolean onTick(World world, EntityPlayer player, LivingArmour livingArmour)
    {
        if (player.isBurning())
        {
            totalFireTicks++;
            this.markDirty();
            return true;
        }

        return false;
    }

    @Override
    public void onDeactivatedTick(World world, EntityPlayer player, LivingArmour livingArmour)
    {

    }

    @Override
    public List<LivingArmourUpgrade> getUpgrades()
    {
        List<LivingArmourUpgrade> upgradeList = new ArrayList<LivingArmourUpgrade>();

        for (int i = 0; i < 5; i++)
        {
            if (totalFireTicks >= fireTicksRequired[i])
            {
                upgradeList.add(new LivingArmourUpgradeFireResist(i));
            }
        }

        return upgradeList;
    }

    @Override
    public boolean providesUpgrade(String key)
    {
        return key.equals(Constants.Mod.MODID + ".upgrade.fireResist");
    }
}
