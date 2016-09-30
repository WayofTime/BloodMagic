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
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeExperience;
import WayofTime.bloodmagic.util.Utils;

public class StatTrackerExperience extends StatTracker
{
    public double totalExperienceGained = 0;

    public static HashMap<LivingArmour, Integer> changeMap = new HashMap<LivingArmour, Integer>();
    public static int[] experienceRequired = new int[] { 100, 400, 1000, 1600, 3200, 5000, 7000, 9200, 11500, 140000 };

    public static void incrementCounter(LivingArmour armour, int exp)
    {
        changeMap.put(armour, changeMap.containsKey(armour) ? changeMap.get(armour) + exp : exp);
    }

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".tracker.experienced";
    }

    @Override
    public void resetTracker()
    {
        this.totalExperienceGained = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        totalExperienceGained = tag.getDouble(Constants.Mod.MODID + ".tracker.experienced");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        tag.setDouble(Constants.Mod.MODID + ".tracker.experienced", totalExperienceGained);
    }

    @Override
    public boolean onTick(World world, EntityPlayer player, LivingArmour livingArmour)
    {
        if (changeMap.containsKey(livingArmour))
        {
            double change = Math.abs(changeMap.get(livingArmour));
            if (change > 0)
            {
                totalExperienceGained += Math.abs(changeMap.get(livingArmour));

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
            if (totalExperienceGained >= experienceRequired[i])
            {
                upgradeList.add(new LivingArmourUpgradeExperience(i));
            }
        }

        return upgradeList;
    }

    @Override
    public double getProgress(LivingArmour livingArmour, int currentLevel)
    {
        return Utils.calculateStandardProgress(totalExperienceGained, experienceRequired, currentLevel);
    }

    @Override
    public boolean providesUpgrade(String key)
    {
        return key.equals(Constants.Mod.MODID + ".upgrade.experienced");
    }

    @Override
    public void onArmourUpgradeAdded(LivingArmourUpgrade upgrade)
    {
        if (upgrade instanceof LivingArmourUpgradeExperience)
        {
            int level = upgrade.getUpgradeLevel();
            if (level < experienceRequired.length)
            {
                totalExperienceGained = Math.max(totalExperienceGained, experienceRequired[level]);
                this.markDirty();
            }
        }
    }
}
