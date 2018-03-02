package WayofTime.bloodmagic.livingArmour.tracker;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.livingArmour.StatTracker;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeStepAssist;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatTrackerStepAssist extends StatTracker {
    public static Map<EntityPlayer, Double> lastPosX = new HashMap<>();
    public static Map<EntityPlayer, Double> lastPosZ = new HashMap<>();

    public static int blocksRequired = 1000;

    public double totalMovement = 0;

    @Override
    public String getUniqueIdentifier() {
        return BloodMagic.MODID + ".tracker.stepAssist";
    }

    @Override
    public void resetTracker() {
        this.totalMovement = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        totalMovement = tag.getDouble(BloodMagic.MODID + ".tracker.stepAssist");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        tag.setDouble(BloodMagic.MODID + ".tracker.stepAssist", totalMovement);

    }

    @Override
    public boolean onTick(World world, EntityPlayer player, LivingArmour livingArmour) {
        if (!lastPosX.containsKey(player)) {
            lastPosX.put(player, player.posX);
            lastPosZ.put(player, player.posZ);
            return false;
        }

        if (!player.onGround || player.stepHeight < 1) {
            return false;
        }

        double distanceTravelled = Math.sqrt(Math.pow(lastPosX.get(player) - player.posX, 2) + Math.pow(lastPosZ.get(player) - player.posZ, 2));

        if (distanceTravelled > 0.0001 && distanceTravelled < 2) {
            double previousMovement = totalMovement;
            totalMovement += distanceTravelled;

            lastPosX.put(player, player.posX);
            lastPosZ.put(player, player.posZ);

            markDirty();

            return previousMovement < blocksRequired && totalMovement >= blocksRequired;
        }

        lastPosX.put(player, player.posX);
        lastPosZ.put(player, player.posZ);

        return false;
    }

    @Override
    public void onDeactivatedTick(World world, EntityPlayer player, LivingArmour livingArmour) {

    }

    @Override
    public List<LivingArmourUpgrade> getUpgrades() {
        List<LivingArmourUpgrade> upgradeList = new ArrayList<>();

        if (totalMovement >= blocksRequired) {
            upgradeList.add(new LivingArmourUpgradeStepAssist(0));
        }

        return upgradeList;
    }

    @Override
    public double getProgress(LivingArmour livingArmour, int currentLevel) {
        if (currentLevel == 1)
            return 1.0D;

        return totalMovement / (double) blocksRequired;
    }

    @Override
    public boolean providesUpgrade(String key) {
        return key.equals(BloodMagic.MODID + ".upgrade.stepAssist");
    }

    @Override
    public void onArmourUpgradeAdded(LivingArmourUpgrade upgrade) {
        if (upgrade instanceof LivingArmourUpgradeStepAssist) {
            totalMovement = Math.max(totalMovement, blocksRequired);
            this.markDirty();
        }
    }
}
