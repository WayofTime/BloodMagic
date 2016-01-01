package WayofTime.bloodmagic.livingArmour;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourHandler;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.api.livingArmour.StatTracker;

public class LivingArmour
{
    public HashMap<String, StatTracker> trackerMap = new HashMap();
    public HashMap<String, LivingArmourUpgrade> upgradeMap = new HashMap();

    /**
     * Ticks the upgrades and stat trackers, passing in the world and player as
     * well as the LivingArmour
     * 
     * @param world
     * @param player
     */
    public void onTick(World world, EntityPlayer player)
    {
        for (Entry<String, LivingArmourUpgrade> entry : upgradeMap.entrySet())
        {
            LivingArmourUpgrade upgrade = entry.getValue();

            if (upgrade == null)
            {
                continue;
            }

            upgrade.onTick(world, player, this);
        }

        for (Entry<String, StatTracker> entry : trackerMap.entrySet())
        {
            StatTracker tracker = entry.getValue();

            if (tracker == null)
            {
                continue;
            }

            tracker.onTick(world, player, this); // TODO: Check if the upgrades
                                                 // need to be updated.
        }
    }

    public void readFromNBT(NBTTagCompound tag)
    {
        NBTTagList upgradeTags = tag.getTagList("upgrades", 10);
        if (upgradeTags != null)
        {
            for (int i = 0; i < upgradeTags.tagCount(); i++)
            {
                NBTTagCompound upgradeTag = upgradeTags.getCompoundTagAt(i);
                String key = upgradeTag.getString("key");
                int level = upgradeTag.getInteger("level");
                NBTTagCompound nbtTag = upgradeTag.getCompoundTag("upgrade");
                LivingArmourHandler.generateUpgradeFromKey(key, level, nbtTag);
            }
        }

        for (Class<? extends StatTracker> clazz : LivingArmourHandler.trackers)
        {
            try
            {
                Constructor<?> ctor = clazz.getConstructor();
                Object obj = ctor.newInstance();
                if (!(obj instanceof StatTracker))
                {
                    // ?????
                }
                StatTracker tracker = (StatTracker) obj;
                String key = tracker.getUniqueIdentifier();
                NBTTagCompound trackerTag = tag.getCompoundTag(key);
                if (!trackerTag.hasNoTags())
                {
                    tracker.readFromNBT(trackerTag);
                }
                trackerMap.put(key, tracker);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void writeToNBT(NBTTagCompound tag, boolean forceWrite)
    {
        NBTTagList tags = new NBTTagList();

        for (Entry<String, LivingArmourUpgrade> entry : upgradeMap.entrySet())
        {
            NBTTagCompound upgradeTag = new NBTTagCompound();

            LivingArmourUpgrade upgrade = entry.getValue();
            NBTTagCompound nbtTag = new NBTTagCompound();
            upgrade.writeToNBT(nbtTag);

            upgradeTag.setString("key", upgrade.getUniqueIdentifier());
            upgradeTag.setInteger("level", upgrade.getUpgradeLevel());
            upgradeTag.setTag("upgrade", nbtTag);

            tags.appendTag(upgradeTag);
        }

        tag.setTag("upgrades", tags);

        for (Entry<String, StatTracker> entry : trackerMap.entrySet())
        {
            StatTracker tracker = entry.getValue();

            if (tracker == null)
            {
                continue;
            }

            String key = tracker.getUniqueIdentifier();

            if (forceWrite || tracker.isDirty())
            {
                NBTTagCompound trackerTag = new NBTTagCompound();
                tracker.writeToNBT(trackerTag);

                tag.setTag(key, trackerTag);

                tracker.resetDirty();
            }
        }
    }

    /**
     * Writes the LivingArmour to the NBTTag. This will only write the trackers
     * that are dirty.
     * 
     * @param tag
     */
    public void writeDirtyToNBT(NBTTagCompound tag)
    {
        writeToNBT(tag, false);
    }

    public void writeToNBT(NBTTagCompound tag)
    {
        writeToNBT(tag, true);
    }
}
