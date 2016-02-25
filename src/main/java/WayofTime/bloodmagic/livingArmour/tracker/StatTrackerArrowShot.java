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
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeArrowShot;

public class StatTrackerArrowShot extends StatTracker
{
    public int totalShots = 0;

    public static HashMap<LivingArmour, Integer> changeMap = new HashMap<LivingArmour, Integer>();
    public static int[] shotsRequired = new int[] { 50, 200, 700, 1500, 3000 };

    public static void incrementCounter(LivingArmour armour)
    {
        changeMap.put(armour, changeMap.containsKey(armour) ? changeMap.get(armour) + 1 : 1);
    }

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".tracker.trickShot";
    }

    @Override
    public void resetTracker()
    {
        this.totalShots = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        totalShots = tag.getInteger(Constants.Mod.MODID + ".tracker.trickShot");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        tag.setInteger(Constants.Mod.MODID + ".tracker.trickShot", totalShots);
    }

    @Override
    public boolean onTick(World world, EntityPlayer player, LivingArmour livingArmour)
    {
        if (changeMap.containsKey(livingArmour))
        {
            int change = Math.abs(changeMap.get(livingArmour));
            if (change > 0)
            {
                totalShots += Math.abs(changeMap.get(livingArmour));

                changeMap.put(livingArmour, 0);

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

        for (int i = 0; i < 5; i++)
        {
            if (totalShots >= shotsRequired[i])
            {
                upgradeList.add(new LivingArmourUpgradeArrowShot(i));
            }
        }

        return upgradeList;
    }

    @Override
    public boolean providesUpgrade(String key)
    {
        return key.equals(Constants.Mod.MODID + ".upgrade.arrowShot");
    }
}
