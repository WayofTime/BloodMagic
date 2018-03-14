package WayofTime.bloodmagic.livingArmour.tracker;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.livingArmour.StatTracker;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeFireResist;
import WayofTime.bloodmagic.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class StatTrackerFireResist extends StatTracker {
    public static int[] fireTicksRequired = new int[]{60 * 20, 3 * 60 * 20, 10 * 60 * 20, 20 * 60 * 20, 25 * 60 * 20};
    public int totalFireTicks = 0;

    @Override
    public String getUniqueIdentifier() {
        return BloodMagic.MODID + ".tracker.fire";
    }

    @Override
    public void resetTracker() {
        this.totalFireTicks = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        totalFireTicks = tag.getInteger(BloodMagic.MODID + ".tracker.fire");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        tag.setInteger(BloodMagic.MODID + ".tracker.fire", totalFireTicks);
    }

    @Override
    public boolean onTick(World world, EntityPlayer player, LivingArmour livingArmour) {
        if (player.isBurning()) {
            totalFireTicks++;
            this.markDirty();
            return true;
        }

        return false;
    }

    @Override
    public void onDeactivatedTick(World world, EntityPlayer player, LivingArmour livingArmour) {

    }

    @Override
    public List<LivingArmourUpgrade> getUpgrades() {
        List<LivingArmourUpgrade> upgradeList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            if (totalFireTicks >= fireTicksRequired[i]) {
                upgradeList.add(new LivingArmourUpgradeFireResist(i));
            }
        }

        return upgradeList;
    }

    @Override
    public double getProgress(LivingArmour livingArmour, int currentLevel) {
        return Utils.calculateStandardProgress(totalFireTicks, fireTicksRequired, currentLevel);
    }

    @Override
    public boolean providesUpgrade(String key) {
        return key.equals(BloodMagic.MODID + ".upgrade.fireResist");
    }

    @Override
    public void onArmourUpgradeAdded(LivingArmourUpgrade upgrade) {
        if (upgrade instanceof LivingArmourUpgradeFireResist) {
            int level = upgrade.getUpgradeLevel();
            if (level < fireTicksRequired.length) {
                totalFireTicks = Math.max(totalFireTicks, fireTicksRequired[level]);
                this.markDirty();
            }
        }
    }
}
