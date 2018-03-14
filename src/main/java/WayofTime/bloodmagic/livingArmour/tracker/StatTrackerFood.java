package WayofTime.bloodmagic.livingArmour.tracker;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.livingArmour.StatTracker;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeKnockbackResist;
import WayofTime.bloodmagic.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatTrackerFood extends StatTracker {
    public static Map<EntityPlayer, Integer> lastFoodEatenMap = new HashMap<>();

    public static int[] foodRequired = new int[]{100, 200, 300, 500, 1000};

    public int foodEaten = 0;

    @Override
    public String getUniqueIdentifier() {
        return BloodMagic.MODID + ".tracker.foodEaten";
    }

    @Override
    public void resetTracker() {
        this.foodEaten = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        foodEaten = tag.getInteger(BloodMagic.MODID + ".tracker.foodEaten");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        tag.setInteger(BloodMagic.MODID + ".tracker.foodEaten", foodEaten);

    }

    @Override
    public boolean onTick(World world, EntityPlayer player, LivingArmour livingArmour) {
        if (!lastFoodEatenMap.containsKey(player)) {
            lastFoodEatenMap.put(player, 20);
            return false;
        }

        int currentFood = player.getFoodStats().getFoodLevel();
        int prevFood = lastFoodEatenMap.get(player);
        lastFoodEatenMap.put(player, currentFood);

        if (currentFood > prevFood) {
            foodEaten += (currentFood - prevFood);

            markDirty();

            return true;
        }

        return false;
    }

    @Override
    public void onDeactivatedTick(World world, EntityPlayer player, LivingArmour livingArmour) {
        if (lastFoodEatenMap.containsKey(player)) {
            lastFoodEatenMap.remove(player);
        }
    }

    @Override
    public List<LivingArmourUpgrade> getUpgrades() {
        List<LivingArmourUpgrade> upgradeList = new ArrayList<>();

        for (int i = 0; i < foodRequired.length; i++) {
            if (foodEaten >= foodRequired[i]) {
                upgradeList.add(new LivingArmourUpgradeKnockbackResist(i));
            }
        }

        return upgradeList;
    }

    @Override
    public double getProgress(LivingArmour livingArmour, int currentLevel) {
        return Utils.calculateStandardProgress(foodEaten, foodRequired, currentLevel);
    }

    @Override
    public boolean providesUpgrade(String key) {
        return key.equals(BloodMagic.MODID + ".upgrade.knockback");
    }

    @Override
    public void onArmourUpgradeAdded(LivingArmourUpgrade upgrade) {
        if (upgrade instanceof LivingArmourUpgradeKnockbackResist) {
            int level = upgrade.getUpgradeLevel();
            if (level < foodRequired.length) {
                foodEaten = Math.max(foodEaten, foodRequired[level]);
                this.markDirty();
            }
        }
    }
}
