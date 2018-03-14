package WayofTime.bloodmagic.livingArmour;

import WayofTime.bloodmagic.util.BMLog;
import net.minecraft.nbt.NBTTagCompound;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LivingArmourHandler {
    public static List<Class<? extends StatTracker>> trackers = new ArrayList<>();
    public static HashMap<String, Class<? extends LivingArmourUpgrade>> upgradeMap = new HashMap<>();
    public static HashMap<String, Constructor<? extends LivingArmourUpgrade>> upgradeConstructorMap = new HashMap<>();
    public static HashMap<String, Integer> upgradeMaxLevelMap = new HashMap<>();

    public static void registerStatTracker(Class<? extends StatTracker> tracker) {
        trackers.add(tracker);
    }

    /**
     * Registers a LivingArmourUpgrade using its unique identifier and class.
     * This is done to more easily load upgrades
     *
     * @param upgrade
     */
    public static void registerArmourUpgrade(LivingArmourUpgrade upgrade) {
        Class<? extends LivingArmourUpgrade> clazz = upgrade.getClass();
        upgradeMap.put(upgrade.getUniqueIdentifier(), clazz);
        upgradeMaxLevelMap.put(upgrade.getUniqueIdentifier(), upgrade.getMaxTier());
        try {
            Constructor<? extends LivingArmourUpgrade> ctor = clazz.getConstructor(int.class);
            if (ctor == null) {
                BMLog.DEFAULT.error("Error adding living armour upgrade {} as it doesn't have a valid constructor.", upgrade.getUniqueIdentifier());
            } else {
                upgradeConstructorMap.put(upgrade.getUniqueIdentifier(), ctor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static LivingArmourUpgrade generateUpgradeFromKey(String key, int level) {
        return generateUpgradeFromKey(key, level, null);
    }

    public static LivingArmourUpgrade generateUpgradeFromKey(String key, int level, NBTTagCompound tag) {
        Constructor<? extends LivingArmourUpgrade> ctor = upgradeConstructorMap.get(key);
        if (ctor != null) {
            try {
                LivingArmourUpgrade upgrade = ctor.newInstance(level);
                if (upgrade != null && tag != null) {
                    upgrade.readFromNBT(tag);
                }
                return upgrade;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
