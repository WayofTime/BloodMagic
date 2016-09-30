package WayofTime.bloodmagic.livingArmour.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.api.livingArmour.StatTracker;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeNightSight;
import WayofTime.bloodmagic.util.Utils;

public class StatTrackerNightSight extends StatTracker
{
    public double totalDamageDealt = 0;
    public int totalNightVision = 0;

    public static HashMap<LivingArmour, Double> changeMap = new HashMap<LivingArmour, Double>();
    public static int[] damageRequired = new int[] { 0, 200, 800, 1300, 2500, 3800, 5000, 7000, 9200, 11500 };

    public static int neededNightVision = 3 * 60 * 20;

    public static void incrementCounter(LivingArmour armour, double damage)
    {
        changeMap.put(armour, changeMap.containsKey(armour) ? changeMap.get(armour) + damage : damage);
    }

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".tracker.nightSight";
    }

    @Override
    public void resetTracker()
    {
        this.totalDamageDealt = 0;
        this.totalNightVision = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        totalDamageDealt = tag.getDouble(Constants.Mod.MODID + ".tracker.nightSight");
        totalNightVision = tag.getInteger(Constants.Mod.MODID + ".tracker.nightSightVision");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        tag.setDouble(Constants.Mod.MODID + ".tracker.nightSight", totalDamageDealt);
        tag.setInteger(Constants.Mod.MODID + ".tracker.nightSightVision", totalNightVision);
    }

    @Override
    public boolean onTick(World world, EntityPlayer player, LivingArmour livingArmour)
    {
        boolean test = false;

        if (changeMap.containsKey(livingArmour))
        {
            double change = Math.abs(changeMap.get(livingArmour));
            if (change > 0)
            {
                totalDamageDealt += Math.abs(changeMap.get(livingArmour));

                changeMap.put(livingArmour, 0d);

                test = true;
            }
        }

        if (world.getLight(player.getPosition()) <= 9 && player.isPotionActive(MobEffects.NIGHT_VISION))
        {
            totalNightVision++;
            test = true;
        }

        if (test)
        {
            this.markDirty();
        }

        return test;
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

        if (totalNightVision < neededNightVision)
        {
            return upgradeList;
        }

        for (int i = 0; i < 10; i++)
        {
            if (totalDamageDealt >= damageRequired[i])
            {
                upgradeList.add(new LivingArmourUpgradeNightSight(i));
            }
        }

        return upgradeList;
    }

    @Override
    public double getProgress(LivingArmour livingArmour, int currentLevel)
    {
        return Utils.calculateStandardProgress(totalDamageDealt, damageRequired, currentLevel);
    }

    @Override
    public boolean providesUpgrade(String key)
    {
        return key.equals(Constants.Mod.MODID + ".upgrade.nightSight");
    }

    @Override
    public void onArmourUpgradeAdded(LivingArmourUpgrade upgrade)
    {
        if (upgrade instanceof LivingArmourUpgradeNightSight)
        {
            int level = upgrade.getUpgradeLevel();
            if (level < damageRequired.length)
            {
                totalDamageDealt = Math.max(totalDamageDealt, damageRequired[level]);
                totalNightVision = neededNightVision;
                this.markDirty();
            }
        }
    }
}
