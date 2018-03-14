package WayofTime.bloodmagic.livingArmour.tracker;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.livingArmour.StatTracker;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeSpeed;
import WayofTime.bloodmagic.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatTrackerMovement extends StatTracker {
    public static Map<EntityPlayer, Double> lastPosX = new HashMap<>();
    public static Map<EntityPlayer, Double> lastPosZ = new HashMap<>();

    public static int[] blocksRequired = new int[]{200, 1000, 2000, 4000, 7000, 15000, 25000, 35000, 50000, 70000};

    public double totalMovement = 0;

    @Override
    public String getUniqueIdentifier() {
        return BloodMagic.MODID + ".tracker.movement";
    }

    @Override
    public void resetTracker() {
        this.totalMovement = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        totalMovement = tag.getDouble(BloodMagic.MODID + ".tracker.movement");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        tag.setDouble(BloodMagic.MODID + ".tracker.movement", totalMovement);

    }

    @Override
    public boolean onTick(World world, EntityPlayer player, LivingArmour livingArmour) {
        if (!lastPosX.containsKey(player)) {
            lastPosX.put(player, player.posX);
            lastPosZ.put(player, player.posZ);
            return false;
        }

        if (!player.onGround) {
            return false;
        }

        double distanceTravelled = Math.sqrt(Math.pow(lastPosX.get(player) - player.posX, 2) + Math.pow(lastPosZ.get(player) - player.posZ, 2));

        if (distanceTravelled > 0.0001 && distanceTravelled < 2) {
            totalMovement += distanceTravelled;

            lastPosX.put(player, player.posX);
            lastPosZ.put(player, player.posZ);

            markDirty();

            return true;
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

        for (int i = 0; i < 10; i++) {
            if (totalMovement >= blocksRequired[i]) {
                upgradeList.add(new LivingArmourUpgradeSpeed(i));
            }
        }

        return upgradeList;
    }

    @Override
    public double getProgress(LivingArmour livingArmour, int currentLevel) {
        return Utils.calculateStandardProgress(totalMovement, blocksRequired, currentLevel);
    }

    @Override
    public boolean providesUpgrade(String key) {
        return key.equals(BloodMagic.MODID + ".upgrade.movement");
    }

    @Override
    public void onArmourUpgradeAdded(LivingArmourUpgrade upgrade) {
        if (upgrade instanceof LivingArmourUpgradeSpeed) {
            int level = upgrade.getUpgradeLevel();
            if (level < blocksRequired.length) {
                totalMovement = Math.max(totalMovement, blocksRequired[level]);
                this.markDirty();
            }
        }
    }
}
