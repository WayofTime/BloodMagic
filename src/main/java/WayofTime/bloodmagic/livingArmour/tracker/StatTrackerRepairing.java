package WayofTime.bloodmagic.livingArmour.tracker;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.livingArmour.StatTracker;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeRepairing;
import WayofTime.bloodmagic.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatTrackerRepairing extends StatTracker {
    public static HashMap<LivingArmour, Integer> changeMap = new HashMap<>();
    public static int[] damageRequired = new int[]{500};
    public double totalDamage = 0;

    @Override
    public String getUniqueIdentifier() {
        return BloodMagic.MODID + ".tracker.repair";
    }

    @Override
    public void resetTracker() {
        this.totalDamage = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        totalDamage = tag.getDouble(BloodMagic.MODID + ".tracker.repair");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        tag.setDouble(BloodMagic.MODID + ".tracker.repair", totalDamage);
    }

    @Override
    public boolean onTick(World world, EntityPlayer player, LivingArmour livingArmour) {
        if (changeMap.containsKey(livingArmour)) {
            double change = Math.abs(changeMap.get(livingArmour));
            if (change > 0) {
                totalDamage += Math.abs(changeMap.get(livingArmour));

                changeMap.put(livingArmour, 0);

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

        for (int i = 0; i < 1; i++) {
            if (totalDamage >= damageRequired[i]) {
                upgradeList.add(new LivingArmourUpgradeRepairing(i));
            }
        }

        return upgradeList;
    }

    @Override
    public double getProgress(LivingArmour livingArmour, int currentLevel) {
        return Utils.calculateStandardProgress(totalDamage, damageRequired, currentLevel);
    }

    @Override
    public boolean providesUpgrade(String key) {
        return key.equals(BloodMagic.MODID + ".upgrade.repair");
    }

    @Override
    public void onArmourUpgradeAdded(LivingArmourUpgrade upgrade) {
        if (upgrade instanceof LivingArmourUpgradeRepairing) {
            int level = upgrade.getUpgradeLevel();
            if (level < damageRequired.length) {
                totalDamage = Math.max(totalDamage, damageRequired[level]);
                this.markDirty();
            }
        }
    }

    public static void incrementCounter(LivingArmour armour, int receivedDamage) {
        changeMap.put(armour, changeMap.containsKey(armour) ? changeMap.get(armour) + receivedDamage : receivedDamage);
    }
}
