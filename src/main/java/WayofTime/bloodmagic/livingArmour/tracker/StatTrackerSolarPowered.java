package WayofTime.bloodmagic.livingArmour.tracker;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.livingArmour.StatTracker;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeSolarPowered;
import WayofTime.bloodmagic.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatTrackerSolarPowered extends StatTracker {
    public static HashMap<LivingArmour, Double> changeMap = new HashMap<>();
    public static int[] healthedRequired = new int[]{70, 150, 300, 500, 700, 1400, 2400, 4000, 7000, 9000};
    public double totalHealthGenned = 0;

    @Override
    public String getUniqueIdentifier() {
        return BloodMagic.MODID + ".tracker.solarPowered";
    }

    @Override
    public void resetTracker() {
        this.totalHealthGenned = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        totalHealthGenned = tag.getDouble(BloodMagic.MODID + ".tracker.solarPowered");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        tag.setDouble(BloodMagic.MODID + ".tracker.solarPowered", totalHealthGenned);
    }

    @Override
    public boolean onTick(World world, EntityPlayer player, LivingArmour livingArmour) {
        if (changeMap.containsKey(livingArmour)) {
            double change = Math.abs(changeMap.get(livingArmour));
            if (change > 0) {
                totalHealthGenned += Math.abs(changeMap.get(livingArmour));

                changeMap.put(livingArmour, 0d);

                this.markDirty();

                return true;
            }
        }

        return false;
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
            if (totalHealthGenned >= healthedRequired[i]) {
                upgradeList.add(new LivingArmourUpgradeSolarPowered(i));
            }
        }

        return upgradeList;
    }

    @Override
    public double getProgress(LivingArmour livingArmour, int currentLevel) {
        return Utils.calculateStandardProgress(totalHealthGenned, healthedRequired, currentLevel);
    }

    @Override
    public boolean providesUpgrade(String key) {
        return key.equals(BloodMagic.MODID + ".upgrade.solarPowered");
    }

    @Override
    public void onArmourUpgradeAdded(LivingArmourUpgrade upgrade) {
        if (upgrade instanceof LivingArmourUpgradeSolarPowered) {
            int level = upgrade.getUpgradeLevel();
            if (level < healthedRequired.length) {
                totalHealthGenned = Math.max(totalHealthGenned, healthedRequired[level]);
                this.markDirty();
            }
        }
    }

    public static void incrementCounter(LivingArmour armour, double health) {
        changeMap.put(armour, changeMap.containsKey(armour) ? changeMap.get(armour) + health : health);
    }
}
