package WayofTime.bloodmagic.livingArmour.tracker;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.livingArmour.StatTracker;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeGrimReaperSprint;
import WayofTime.bloodmagic.util.BMLog;
import WayofTime.bloodmagic.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatTrackerGrimReaperSprint extends StatTracker {
    public static HashMap<LivingArmour, Integer> changeMap = new HashMap<>();
    public static int[] deathsRequired = new int[]{6, 10, 15, 25, 50, 70, 90, 120, 150, 200}; //TODO: Modify
    public int totalDeaths = 0;

    @Override
    public String getUniqueIdentifier() {
        return BloodMagic.MODID + ".tracker.grimReaper";
    }

    @Override
    public void resetTracker() {
        this.totalDeaths = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        totalDeaths = tag.getInteger(BloodMagic.MODID + ".tracker.grimReaper");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        tag.setInteger(BloodMagic.MODID + ".tracker.grimReaper", totalDeaths);
    }

    @Override
    public boolean onTick(World world, EntityPlayer player, LivingArmour livingArmour) {
        if (changeMap.containsKey(livingArmour)) {
            double change = Math.abs(changeMap.get(livingArmour));
            if (change > 0) {
                totalDeaths += Math.abs(changeMap.get(livingArmour));

                changeMap.put(livingArmour, 0);

                this.markDirty();

                return true;
            }
        }

        return true;
    }

    @Override
    public void onDeactivatedTick(World world, EntityPlayer player, LivingArmour livingArmour) {
        if (changeMap.containsKey(livingArmour)) {
            changeMap.remove(livingArmour);
        }
    }

    @Override
    public List<LivingArmourUpgrade> getUpgrades() {
        List<LivingArmourUpgrade> upgradeList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            if (totalDeaths >= deathsRequired[i]) {
                upgradeList.add(new LivingArmourUpgradeGrimReaperSprint(i));
            }
        }

        return upgradeList;
    }

    @Override
    public double getProgress(LivingArmour livingArmour, int currentLevel) {
        return Utils.calculateStandardProgress(totalDeaths, deathsRequired, currentLevel);
    }

    @Override
    public boolean providesUpgrade(String key) {
        return key.equals(BloodMagic.MODID + ".upgrade.grimReaper");
    }

    @Override
    public void onArmourUpgradeAdded(LivingArmourUpgrade upgrade) {
        if (upgrade instanceof LivingArmourUpgradeGrimReaperSprint) {
            int level = upgrade.getUpgradeLevel();
            if (level < deathsRequired.length) {
                totalDeaths = Math.max(totalDeaths, deathsRequired[level]);
                this.markDirty();
            }
        }
    }

    public static void incrementCounter(LivingArmour armour) {
        StatTracker tracker = armour.getTracker(BloodMagic.MODID + ".tracker.grimReaper");
        if (tracker instanceof StatTrackerGrimReaperSprint) {
            ((StatTrackerGrimReaperSprint) tracker).totalDeaths++;
            BMLog.DEBUG.info(String.valueOf(((StatTrackerGrimReaperSprint) tracker).totalDeaths));
            tracker.markDirty();
        }
//        changeMap.put(armour, changeMap.containsKey(armour) ? changeMap.get(armour) + 1 : 1);
    }
}
