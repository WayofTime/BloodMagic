package WayofTime.bloodmagic.livingArmour.tracker;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.livingArmour.StatTracker;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradePoisonResist;
import WayofTime.bloodmagic.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class StatTrackerPoison extends StatTracker {
    public static int[] poisonTicksRequired = new int[]{60 * 20, 3 * 60 * 20, 10 * 60 * 20, 20 * 60 * 20, 25 * 60 * 20};
    public int totalPoisonTicks = 0;

    @Override
    public String getUniqueIdentifier() {
        return BloodMagic.MODID + ".tracker.poison";
    }

    @Override
    public void resetTracker() {
        this.totalPoisonTicks = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        totalPoisonTicks = tag.getInteger(BloodMagic.MODID + ".tracker.poison");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        tag.setInteger(BloodMagic.MODID + ".tracker.poison", totalPoisonTicks);
    }

    @Override
    public boolean onTick(World world, EntityPlayer player, LivingArmour livingArmour) {
        if (player.isPotionActive(MobEffects.POISON)) {
            totalPoisonTicks++;
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
            if (totalPoisonTicks >= poisonTicksRequired[i]) {
                upgradeList.add(new LivingArmourUpgradePoisonResist(i));
            }
        }

        return upgradeList;
    }

    @Override
    public double getProgress(LivingArmour livingArmour, int currentLevel) {
        return Utils.calculateStandardProgress(totalPoisonTicks, poisonTicksRequired, currentLevel);
    }

    @Override
    public boolean providesUpgrade(String key) {
        return key.equals(BloodMagic.MODID + ".upgrade.poisonResist");
    }

    @Override
    public void onArmourUpgradeAdded(LivingArmourUpgrade upgrade) {
        if (upgrade instanceof LivingArmourUpgradePoisonResist) {
            int level = upgrade.getUpgradeLevel();
            if (level < poisonTicksRequired.length) {
                totalPoisonTicks = Math.max(totalPoisonTicks, poisonTicksRequired[level]);
                this.markDirty();
            }
        }
    }
}
