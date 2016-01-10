package WayofTime.bloodmagic.api.livingArmour;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;

public class LivingArmourHandler
{
    public static List<Class<? extends StatTracker>> trackers = new ArrayList<Class<? extends StatTracker>>();
    public static HashMap<String, Class<? extends LivingArmourUpgrade>> upgradeMap = new HashMap<String, Class<? extends LivingArmourUpgrade>>();
    public static HashMap<String, Constructor<? extends LivingArmourUpgrade>> upgradeConstructorMap = new HashMap<String, Constructor<? extends LivingArmourUpgrade>>();
    public static HashMap<String, Integer> upgradeMaxLevelMap = new HashMap<String, Integer>();

    public static void registerStatTracker(Class<? extends StatTracker> tracker)
    {
        trackers.add(tracker);
    }

    /**
     * Registers a LivingArmourUpgrade using its unique identifier and class.
     * This is done to more easily load upgrades
     * 
     * @param upgrade
     */
    public static void registerArmourUpgrade(LivingArmourUpgrade upgrade)
    {
        Class<? extends LivingArmourUpgrade> clazz = upgrade.getClass();
        upgradeMap.put(upgrade.getUniqueIdentifier(), clazz);
        upgradeMaxLevelMap.put(upgrade.getUniqueIdentifier(), upgrade.getMaxTier());
        try
        {
            Constructor<? extends LivingArmourUpgrade> ctor = clazz.getConstructor(int.class);
            if (ctor == null)
            {
                // TODO: This is bad - add something to the log
            } else
            {
                upgradeConstructorMap.put(upgrade.getUniqueIdentifier(), ctor);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public static LivingArmourUpgrade generateUpgradeFromKey(String key, int level)
    {
        return generateUpgradeFromKey(key, level, null);
    }

    public static LivingArmourUpgrade generateUpgradeFromKey(String key, int level, NBTTagCompound tag)
    {
        Constructor<? extends LivingArmourUpgrade> ctor = upgradeConstructorMap.get(key);
        if (ctor != null)
        {
            try
            {
                LivingArmourUpgrade upgrade = ctor.newInstance(level);
                if (upgrade != null && tag != null)
                {
                    upgrade.readFromNBT(tag);
                }
                return upgrade;
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return null;
    }
}
