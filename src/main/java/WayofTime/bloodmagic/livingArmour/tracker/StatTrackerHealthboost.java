package WayofTime.bloodmagic.livingArmour.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.api.livingArmour.StatTracker;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeHealthboost;

public class StatTrackerHealthboost extends StatTracker
{
    public double totalHealthGenned = 0;

    public static HashMap<LivingArmour, Double> changeMap = new HashMap<LivingArmour, Double>();
    public static int[] healthedRequired = new int[] { 80, 200, 340, 540, 800, 1600, 2800, 5000, 7600, 10000 };

    public static void incrementCounter(LivingArmour armour, double health)
    {
        changeMap.put(armour, changeMap.containsKey(armour) ? changeMap.get(armour) + health : health);
    }

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".tracker.health";
    }

    @Override
    public void resetTracker()
    {
        this.totalHealthGenned = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        totalHealthGenned = tag.getDouble(Constants.Mod.MODID + ".tracker.health");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        tag.setDouble(Constants.Mod.MODID + ".tracker.health", totalHealthGenned);
    }

    @Override
    public boolean onTick(World world, EntityPlayer player, LivingArmour livingArmour)
    {
        if (changeMap.containsKey(livingArmour))
        {
            double change = Math.abs(changeMap.get(livingArmour));
            if (change > 0)
            {
                totalHealthGenned += Math.abs(changeMap.get(livingArmour));

                changeMap.put(livingArmour, 0d);

                this.markDirty();

                return true;
            }
        }

        return false;
    }

    @Override
    public List<LivingArmourUpgrade> getUpgrades()
    {
        List<LivingArmourUpgrade> upgradeList = new ArrayList<LivingArmourUpgrade>();

        for (int i = 0; i < 10; i++)
        {
            if (totalHealthGenned >= healthedRequired[i])
            {
                upgradeList.add(new LivingArmourUpgradeHealthboost(i));
            }
        }

        return upgradeList;
    }

    @Override
    public boolean providesUpgrade(String key)
    {
        return key.equals(Constants.Mod.MODID + ".upgrade.health");
    }
}
