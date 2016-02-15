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
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeGrimReaperSprint;

public class StatTrackerGrimReaperSprint extends StatTracker
{
    public int totalDeaths = 0;

    public static HashMap<LivingArmour, Integer> changeMap = new HashMap<LivingArmour, Integer>();
    public static int[] deathsRequired = new int[] { 6, 10, 15, 25, 50, 70, 90, 120, 150, 200 }; //TODO: Modify

    public static void incrementCounter(LivingArmour armour)
    {
        StatTracker tracker = armour.getTracker(Constants.Mod.MODID + ".tracker.grimReaper");
        if (tracker instanceof StatTrackerGrimReaperSprint)
        {
            ((StatTrackerGrimReaperSprint) tracker).totalDeaths++;
            System.out.println(((StatTrackerGrimReaperSprint) tracker).totalDeaths);
            tracker.markDirty();
        }
//        changeMap.put(armour, changeMap.containsKey(armour) ? changeMap.get(armour) + 1 : 1);
    }

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".tracker.grimReaper";
    }

    @Override
    public void resetTracker()
    {
        this.totalDeaths = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        totalDeaths = tag.getInteger(Constants.Mod.MODID + ".tracker.grimReaper");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        tag.setInteger(Constants.Mod.MODID + ".tracker.grimReaper", totalDeaths);
    }

    @Override
    public boolean onTick(World world, EntityPlayer player, LivingArmour livingArmour)
    {
        if (changeMap.containsKey(livingArmour))
        {
            double change = Math.abs(changeMap.get(livingArmour));
            if (change > 0)
            {
                totalDeaths += Math.abs(changeMap.get(livingArmour));

                changeMap.put(livingArmour, 0);

                this.markDirty();

                return true;
            }
        }

        return true;
    }

    @Override
    public List<LivingArmourUpgrade> getUpgrades()
    {
        List<LivingArmourUpgrade> upgradeList = new ArrayList<LivingArmourUpgrade>();

        for (int i = 0; i < 10; i++)
        {
            if (totalDeaths >= deathsRequired[i])
            {
                upgradeList.add(new LivingArmourUpgradeGrimReaperSprint(i));
            }
        }

        return upgradeList;
    }

    @Override
    public boolean providesUpgrade(String key)
    {
        return key.equals(Constants.Mod.MODID + ".upgrade.grimReaper");
    }
}
