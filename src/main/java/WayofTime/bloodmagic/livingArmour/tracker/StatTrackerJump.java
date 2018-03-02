package WayofTime.bloodmagic.livingArmour.tracker;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.livingArmour.StatTracker;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeJump;
import WayofTime.bloodmagic.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatTrackerJump extends StatTracker {
    public static HashMap<LivingArmour, Integer> changeMap = new HashMap<>();
    public static int[] jumpsRequired = new int[]{30, 200, 400, 700, 1100, 1500, 2000, 2800, 3600, 5000}; //testing

    public int totalJumps = 0;

    @Override
    public String getUniqueIdentifier() {
        return BloodMagic.MODID + ".tracker.jump";
    }

    @Override
    public void resetTracker() {
        this.totalJumps = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        totalJumps = tag.getInteger(BloodMagic.MODID + ".tracker.jump");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        tag.setInteger(BloodMagic.MODID + ".tracker.jump", totalJumps);

    }

    @Override
    public boolean onTick(World world, EntityPlayer player, LivingArmour livingArmour) {
        if (changeMap.containsKey(livingArmour)) {
            int change = Math.abs(changeMap.get(livingArmour));
            if (change > 0) {
                totalJumps += Math.abs(changeMap.get(livingArmour));

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

        for (int i = 0; i < 10; i++) {
            if (totalJumps >= jumpsRequired[i]) {
                upgradeList.add(new LivingArmourUpgradeJump(i));
            }
        }

        return upgradeList;
    }

    @Override
    public double getProgress(LivingArmour livingArmour, int currentLevel) {
        return Utils.calculateStandardProgress(totalJumps, jumpsRequired, currentLevel);
    }

    @Override
    public boolean providesUpgrade(String key) {
        return key.equals(BloodMagic.MODID + ".upgrade.jump");
    }

    @Override
    public void onArmourUpgradeAdded(LivingArmourUpgrade upgrade) {
        if (upgrade instanceof LivingArmourUpgradeJump) {
            int level = upgrade.getUpgradeLevel();
            if (level < jumpsRequired.length) {
                totalJumps = Math.max(totalJumps, jumpsRequired[level]);
                this.markDirty();
            }
        }
    }

    public static void incrementCounter(LivingArmour armour) {
        changeMap.put(armour, changeMap.containsKey(armour) ? changeMap.get(armour) + 1 : 1);
    }
}
