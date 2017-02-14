package WayofTime.bloodmagic.livingArmour;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.Map.Entry;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.iface.IUpgradeTrainer;
import WayofTime.bloodmagic.api.livingArmour.ILivingArmour;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourHandler;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.api.livingArmour.StatTracker;
import WayofTime.bloodmagic.item.armour.ItemLivingArmour;
import WayofTime.bloodmagic.registry.ModPotions;
import WayofTime.bloodmagic.util.ChatUtil;
import WayofTime.bloodmagic.util.helper.TextHelper;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class LivingArmour implements ILivingArmour
{
    public static String chatBase = "chat.BloodMagic.livingArmour.";
    public HashMap<String, StatTracker> trackerMap = new HashMap<String, StatTracker>();
    public HashMap<String, LivingArmourUpgrade> upgradeMap = new HashMap<String, LivingArmourUpgrade>();

    public int maxUpgradePoints = 100;
    public int totalUpgradePoints = 0;

    public StatTracker getTracker(String key)
    {
        if (trackerMap.containsKey(key))
        {
            return trackerMap.get(key);
        } else
        {
            return null;
        }
    }

    public double getAdditionalDamageOnHit(double damage, EntityPlayer wearer, EntityLivingBase hitEntity, ItemStack weapon)
    {
        double total = 0;
        for (Entry<String, LivingArmourUpgrade> entry : upgradeMap.entrySet())
        {
            total += entry.getValue().getAdditionalDamageOnHit(damage, wearer, hitEntity, weapon);
        }

        return total;
    }

    public double getKnockbackOnHit(EntityPlayer wearer, EntityLivingBase hitEntity, ItemStack weapon)
    {
        double total = 0;
        for (Entry<String, LivingArmourUpgrade> entry : upgradeMap.entrySet())
        {
            total += entry.getValue().getKnockbackOnHit(wearer, hitEntity, weapon);
        }

        return total;
    }

    public void recalculateUpgradePoints()
    {
        totalUpgradePoints = 0;
        for (LivingArmourUpgrade upgrade : upgradeMap.values())
        {
            totalUpgradePoints += upgrade.getCostOfUpgrade();
        }
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers()
    {
        HashMultimap<String, AttributeModifier> modifierMap = HashMultimap.create();

        int count = 0;
        for (Entry<String, LivingArmourUpgrade> entry : upgradeMap.entrySet())
        {
            LivingArmourUpgrade upgrade = entry.getValue();
            if (upgrade == null)
            {
                continue;
            }

            Multimap<String, AttributeModifier> upgradeModifiers = upgrade.getAttributeModifiers();
            for (String key : upgradeModifiers.keySet())
            {
                if (modifierMap.containsKey(key))
                {
                    Collection<AttributeModifier> renamed = renameModifiers(upgradeModifiers.get(key), count);
                    modifierMap.get(key).addAll(renamed);
                    count += renamed.size();
                } else
                    modifierMap.putAll(upgradeModifiers);
            }
        }

        return modifierMap;
    }

    private static Set<AttributeModifier> renameModifiers(Collection<AttributeModifier> modifiers, int count)
    {
        Set<AttributeModifier> newModifiers = new HashSet<AttributeModifier>();
        for (AttributeModifier modifier : modifiers)
        {
            String newName = modifier.getName().substring(0, modifier.getName().length() - 1) + count;
            newModifiers.add(new AttributeModifier(UUID.nameUUIDFromBytes(new byte[] {(byte)count}), newName, modifier.getAmount(), modifier.getOperation()));
            count++;
        }

        return newModifiers;
    }

    @Override
    public boolean upgradeArmour(EntityPlayer user, LivingArmourUpgrade upgrade)
    {
        String key = upgrade.getUniqueIdentifier();
        if (upgradeMap.containsKey(key))
        {
            //Check if this is a higher level than the previous upgrade
            int nextLevel = upgrade.getUpgradeLevel();
            int currentLevel = upgradeMap.get(key).getUpgradeLevel();

            if (nextLevel > currentLevel)
            {
                int upgradePointDifference = upgrade.getCostOfUpgrade() - upgradeMap.get(key).getCostOfUpgrade();
                if (totalUpgradePoints + upgradePointDifference <= maxUpgradePoints)
                {
                    upgradeMap.put(key, upgrade);
                    totalUpgradePoints += upgradePointDifference;
                    notifyPlayerOfUpgrade(user, upgrade);
                    for (StatTracker tracker : trackerMap.values())
                    {
                        tracker.onArmourUpgradeAdded(upgrade);
                    }

                    return true;
                }
            }
        } else
        {
            int upgradePoints = upgrade.getCostOfUpgrade();
            if (totalUpgradePoints + upgradePoints <= maxUpgradePoints)
            {
                upgradeMap.put(key, upgrade);
                totalUpgradePoints += upgradePoints;
                notifyPlayerOfUpgrade(user, upgrade);
                for (StatTracker tracker : trackerMap.values())
                {
                    tracker.onArmourUpgradeAdded(upgrade);
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public boolean canApplyUpgrade(EntityPlayer user, LivingArmourUpgrade upgrade)
    {
        String key = upgrade.getUniqueIdentifier();
        if (upgradeMap.containsKey(key))
        {
            //Check if this is a higher level than the previous upgrade
            int nextLevel = upgrade.getUpgradeLevel();
            int currentLevel = upgradeMap.get(key).getUpgradeLevel();

            if (nextLevel > currentLevel)
            {
                int upgradePointDifference = upgrade.getCostOfUpgrade() - upgradeMap.get(key).getCostOfUpgrade();
                if (totalUpgradePoints + upgradePointDifference <= maxUpgradePoints)
                {
                    return true;
                }
            }
        } else
        {
            int upgradePoints = upgrade.getCostOfUpgrade();
            if (totalUpgradePoints + upgradePoints <= maxUpgradePoints)
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public void notifyPlayerOfUpgrade(EntityPlayer user, LivingArmourUpgrade upgrade)
    {
        ChatUtil.sendChat(user, TextHelper.localizeEffect(chatBase + "newUpgrade"));
    }

    /**
     * Ticks the upgrades and stat trackers, passing in the world and player as
     * well as the LivingArmour
     * 
     * @param world
     * @param player
     */
    @Override
    public void onTick(World world, EntityPlayer player)
    {
        for (Entry<String, LivingArmourUpgrade> entry : upgradeMap.entrySet())
        {
            LivingArmourUpgrade upgrade = entry.getValue();

            if (upgrade == null)
            {
                continue;
            }

            if ((world.isRemote && upgrade.runOnClient()) || !world.isRemote)
            {
                upgrade.onTick(world, player, this);
            }
        }

        if (world.isRemote)
        {
            return;
        }

        List<String> allowedUpgradesList = new ArrayList<String>();
        for (ItemStack stack : player.inventory.mainInventory)
        {
            if (stack != null && stack.getItem() instanceof IUpgradeTrainer)
            {
                List<String> keyList = ((IUpgradeTrainer) stack.getItem()).getTrainedUpgrades(stack);
                if (!keyList.isEmpty())
                {
                    allowedUpgradesList.addAll(keyList);
                }
            }
        }

        for (Entry<String, StatTracker> entry : trackerMap.entrySet())
        {
            StatTracker tracker = entry.getValue();

            if (tracker == null)
            {
                continue;
            }

            if (!allowedUpgradesList.isEmpty())
            {
                boolean allowed = false;

                for (String key : allowedUpgradesList)
                {
                    if (tracker.providesUpgrade(key))
                    {
                        allowed = true;
                        break;
                    }
                }

                if (!allowed)
                {
                    tracker.onDeactivatedTick(world, player, this);
                    continue;
                }
            }

            if (tracker.onTick(world, player, this))
            {
                List<LivingArmourUpgrade> upgradeList = tracker.getUpgrades();

                for (LivingArmourUpgrade upgrade : upgradeList) //TODO: make a getNextUpgrade?
                {
                    upgradeArmour(player, upgrade);
                }
            }

        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        maxUpgradePoints = Math.max(100, tag.getInteger("maxUpgradePoints"));

        NBTTagList upgradeTags = tag.getTagList("upgrades", 10);
        if (upgradeTags != null)
        {
            for (int i = 0; i < upgradeTags.tagCount(); i++)
            {
                NBTTagCompound upgradeTag = upgradeTags.getCompoundTagAt(i);
                String key = upgradeTag.getString("key");
                int level = upgradeTag.getInteger("level");
                NBTTagCompound nbtTag = upgradeTag.getCompoundTag("upgrade");
                LivingArmourUpgrade upgrade = LivingArmourHandler.generateUpgradeFromKey(key, level, nbtTag);
                if (upgrade != null)
                {
                    upgradeMap.put(key, upgrade);
                    totalUpgradePoints += upgrade.getCostOfUpgrade();
                }
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

    @Override
    public void writeToNBT(NBTTagCompound tag, boolean forceWrite)
    {
        tag.setInteger("maxUpgradePoints", maxUpgradePoints);

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
    @Override
    public void writeDirtyToNBT(NBTTagCompound tag)
    {
        writeToNBT(tag, false);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        writeToNBT(tag, true);
    }

    public static boolean hasFullSet(EntityPlayer player)
    {
        for (EntityEquipmentSlot slot : EntityEquipmentSlot.values())
        {
            if (slot.getSlotType() != EntityEquipmentSlot.Type.ARMOR)
            {
                continue;
            }
            ItemStack slotStack = player.getItemStackFromSlot(slot);
            if (slotStack == null || !(slotStack.getItem() instanceof ItemLivingArmour))
                return false;
        }

        return true;
    }

    @Override
    public boolean removeUpgrade(EntityPlayer user, LivingArmourUpgrade upgrade)
    {
        String key = upgrade.getUniqueIdentifier();
        if (upgradeMap.containsKey(key))
        {
            upgradeMap.remove(key);

            return true;
        }

        return false;
    }
}
