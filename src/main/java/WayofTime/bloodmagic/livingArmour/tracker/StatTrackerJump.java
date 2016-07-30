package WayofTime.bloodmagic.livingArmour.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import WayofTime.bloodmagic.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.api.livingArmour.StatTracker;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeJump;

public class StatTrackerJump extends StatTracker
{
    public static HashMap<LivingArmour, Integer> changeMap = new HashMap<LivingArmour, Integer>();
    public static int[] jumpsRequired = new int[] { 30, 200, 400, 700, 1100, 1500, 2000, 2800, 3600, 5000 }; //testing

    public int totalJumps = 0;

    public static void incrementCounter(LivingArmour armour)
    {
        changeMap.put(armour, changeMap.containsKey(armour) ? changeMap.get(armour) + 1 : 1);
    }

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".tracker.jump";
    }

    @Override
    public void resetTracker()
    {
        this.totalJumps = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        totalJumps = tag.getInteger(Constants.Mod.MODID + ".tracker.jump");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        tag.setInteger(Constants.Mod.MODID + ".tracker.jump", totalJumps);

    }

    @Override
    public boolean onTick(World world, EntityPlayer player, LivingArmour livingArmour)
    {
        if (changeMap.containsKey(livingArmour))
        {
            int change = Math.abs(changeMap.get(livingArmour));
            if (change > 0)
            {
                totalJumps += Math.abs(changeMap.get(livingArmour));

                changeMap.put(livingArmour, 0);

                this.markDirty();

                return true;
            }
        }

        return false;
    }

    @Override
    public void onDeactivatedTick(World world, EntityPlayer player, LivingArmour livingArmour)
    {
        if (changeMap.containsKey(livingArmour))
        {
            changeMap.remove(livingArmour);
        }
    }

    @Override
    public List<LivingArmourUpgrade> getUpgrades()
    {
        List<LivingArmourUpgrade> upgradeList = new ArrayList<LivingArmourUpgrade>();

        for (int i = 0; i < 10; i++)
        {
            if (totalJumps >= jumpsRequired[i])
            {
                upgradeList.add(new LivingArmourUpgradeJump(i));
            }
        }

        return upgradeList;
    }

    @Override
    public double getProgress(LivingArmour livingArmour, int currentLevel)
    {
        return Utils.calculateStandardProgress(totalJumps, jumpsRequired, currentLevel);
    }

    @Override
    public boolean providesUpgrade(String key)
    {
        return key.equals(Constants.Mod.MODID + ".upgrade.jump");
    }
}
