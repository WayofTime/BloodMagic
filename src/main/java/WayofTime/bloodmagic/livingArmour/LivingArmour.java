package WayofTime.bloodmagic.livingArmour;

import WayofTime.bloodmagic.iface.IUpgradeTrainer;
import WayofTime.bloodmagic.item.armour.ItemLivingArmour;
import WayofTime.bloodmagic.util.helper.TextHelper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class LivingArmour implements ILivingArmour {
    public static String chatBase = "chat.bloodmagic.livingArmour.";
    public HashMap<String, StatTracker> trackerMap = new HashMap<>();
    public HashMap<String, LivingArmourUpgrade> upgradeMap = new HashMap<>();

    public int maxUpgradePoints = 100;
    public int totalUpgradePoints = 0;

    public StatTracker getTracker(String key) {
        return trackerMap.get(key);
    }

    public double getAdditionalDamageOnHit(double damage, PlayerEntity wearer, LivingEntity hitEntity, ItemStack weapon) {
        double total = 0;
        for (Entry<String, LivingArmourUpgrade> entry : upgradeMap.entrySet()) {
            total += entry.getValue().getAdditionalDamageOnHit(damage, wearer, hitEntity, weapon);
        }

        return total;
    }

    public double getKnockbackOnHit(PlayerEntity wearer, LivingEntity hitEntity, ItemStack weapon) {
        double total = 0;
        for (Entry<String, LivingArmourUpgrade> entry : upgradeMap.entrySet()) {
            total += entry.getValue().getKnockbackOnHit(wearer, hitEntity, weapon);
        }

        return total;
    }

    public void recalculateUpgradePoints() {
        totalUpgradePoints = 0;
        for (LivingArmourUpgrade upgrade : upgradeMap.values()) {
            totalUpgradePoints += upgrade.getCostOfUpgrade();
        }
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers() {
        HashMultimap<String, AttributeModifier> modifierMap = HashMultimap.create();

        for (Entry<String, LivingArmourUpgrade> entry : upgradeMap.entrySet()) {
            LivingArmourUpgrade upgrade = entry.getValue();
            if (upgrade == null) {
                continue;
            }
            modifierMap.putAll(upgrade.getAttributeModifiers());
        }

        return modifierMap;
    }

    @Override
    public boolean upgradeArmour(PlayerEntity user, LivingArmourUpgrade upgrade) {
        String key = upgrade.getUniqueIdentifier();
        if (upgradeMap.containsKey(key)) {
            //Check if this is a higher level than the previous upgrade
            int nextLevel = upgrade.getUpgradeLevel();
            int currentLevel = upgradeMap.get(key).getUpgradeLevel();

            if (nextLevel > currentLevel) {
                int upgradePointDifference = upgrade.getCostOfUpgrade() - upgradeMap.get(key).getCostOfUpgrade();
                if (totalUpgradePoints + upgradePointDifference <= maxUpgradePoints) {
                    upgradeMap.put(key, upgrade);
                    totalUpgradePoints += upgradePointDifference;
                    notifyPlayerOfUpgrade(user, upgrade);
                    for (StatTracker tracker : trackerMap.values()) {
                        tracker.onArmourUpgradeAdded(upgrade);
                    }

                    return true;
                }
            }
        } else {
            int upgradePoints = upgrade.getCostOfUpgrade();
            if (totalUpgradePoints + upgradePoints <= maxUpgradePoints) {
                upgradeMap.put(key, upgrade);
                totalUpgradePoints += upgradePoints;
                notifyPlayerOfUpgrade(user, upgrade);
                for (StatTracker tracker : trackerMap.values()) {
                    tracker.onArmourUpgradeAdded(upgrade);
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public boolean canApplyUpgrade(PlayerEntity user, LivingArmourUpgrade upgrade) {
        String key = upgrade.getUniqueIdentifier();
        if (upgradeMap.containsKey(key)) {
            //Check if this is a higher level than the previous upgrade
            int nextLevel = upgrade.getUpgradeLevel();
            int currentLevel = upgradeMap.get(key).getUpgradeLevel();

            if (nextLevel > currentLevel) {
                int upgradePointDifference = upgrade.getCostOfUpgrade() - upgradeMap.get(key).getCostOfUpgrade();
                if (totalUpgradePoints + upgradePointDifference <= maxUpgradePoints) {
                    return true;
                }
            }
        } else {
            int upgradePoints = upgrade.getCostOfUpgrade();
            if (totalUpgradePoints + upgradePoints <= maxUpgradePoints) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void notifyPlayerOfUpgrade(PlayerEntity user, LivingArmourUpgrade upgrade) {
        user.sendStatusMessage(new StringTextComponent(TextHelper.localizeEffect(chatBase + "newUpgrade")), true);
    }

    /**
     * Ticks the upgrades and stat trackers, passing in the world and player as
     * well as the LivingArmour
     *
     * @param world
     * @param player
     */
    @Override
    public void onTick(World world, PlayerEntity player) {
        for (Entry<String, LivingArmourUpgrade> entry : upgradeMap.entrySet()) {
            LivingArmourUpgrade upgrade = entry.getValue();

            if (upgrade == null) {
                continue;
            }

            if (!world.isRemote || upgrade.runOnClient()) {
                upgrade.onTick(world, player, this);
            }
        }

        if (world.isRemote) {
            return;
        }

        List<String> allowedUpgradesList = new ArrayList<>();
        for (ItemStack stack : player.inventory.mainInventory) {
            if (stack != null && stack.getItem() instanceof IUpgradeTrainer) {
                List<String> keyList = ((IUpgradeTrainer) stack.getItem()).getTrainedUpgrades(stack);
                if (!keyList.isEmpty()) {
                    allowedUpgradesList.addAll(keyList);
                }
            }
        }

        for (Entry<String, StatTracker> entry : trackerMap.entrySet()) {
            StatTracker tracker = entry.getValue();

            if (tracker == null) {
                continue;
            }

            if (!allowedUpgradesList.isEmpty()) {
                boolean allowed = false;

                for (String key : allowedUpgradesList) {
                    if (tracker.providesUpgrade(key)) {
                        allowed = true;
                        break;
                    }
                }

                if (!allowed) {
                    tracker.onDeactivatedTick(world, player, this);
                    continue;
                }
            }

            if (tracker.onTick(world, player, this)) {
                List<LivingArmourUpgrade> upgradeList = tracker.getUpgrades();

                for (LivingArmourUpgrade upgrade : upgradeList) //TODO: make a getNextUpgrade?
                {
                    upgradeArmour(player, upgrade);
                }
            }

        }
    }

    @Override
    public void readFromNBT(CompoundNBT tag) {
        maxUpgradePoints = Math.max(100, tag.getInteger("maxUpgradePoints"));

        ListNBT upgradeTags = tag.getTagList("upgrades", 10);
        if (upgradeTags != null) {
            for (int i = 0; i < upgradeTags.tagCount(); i++) {
                CompoundNBT upgradeTag = upgradeTags.getCompoundTagAt(i);
                String key = upgradeTag.getString("key");
                int level = upgradeTag.getInteger("level");
                CompoundNBT nbtTag = upgradeTag.getCompoundTag("upgrade");
                LivingArmourUpgrade upgrade = LivingArmourHandler.generateUpgradeFromKey(key, level, nbtTag);
                if (upgrade != null) {
                    upgradeMap.put(key, upgrade);
                    totalUpgradePoints += upgrade.getCostOfUpgrade();
                }
            }
        }

        for (Class<? extends StatTracker> clazz : LivingArmourHandler.trackers) {
            try {
                Constructor<?> ctor = clazz.getConstructor();
                Object obj = ctor.newInstance();
                if (!(obj instanceof StatTracker)) {
                    // ?????
                }
                StatTracker tracker = (StatTracker) obj;
                String key = tracker.getUniqueIdentifier();
                CompoundNBT trackerTag = tag.getCompoundTag(key);
                if (!trackerTag.isEmpty()) {
                    tracker.readFromNBT(trackerTag);
                }
                trackerMap.put(key, tracker);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void writeToNBT(CompoundNBT tag, boolean forceWrite) {
        tag.setInteger("maxUpgradePoints", maxUpgradePoints);

        ListNBT tags = new ListNBT();

        for (Entry<String, LivingArmourUpgrade> entry : upgradeMap.entrySet()) {
            CompoundNBT upgradeTag = new CompoundNBT();

            LivingArmourUpgrade upgrade = entry.getValue();
            CompoundNBT nbtTag = new CompoundNBT();
            upgrade.writeToNBT(nbtTag);

            upgradeTag.setString("key", upgrade.getUniqueIdentifier());
            upgradeTag.setInteger("level", upgrade.getUpgradeLevel());
            upgradeTag.setTag("upgrade", nbtTag);

            tags.appendTag(upgradeTag);
        }

        tag.setTag("upgrades", tags);

        for (Entry<String, StatTracker> entry : trackerMap.entrySet()) {
            StatTracker tracker = entry.getValue();

            if (tracker == null) {
                continue;
            }

            String key = tracker.getUniqueIdentifier();

            if (forceWrite || tracker.isDirty()) {
                CompoundNBT trackerTag = new CompoundNBT();
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
    public void writeDirtyToNBT(CompoundNBT tag) {
        writeToNBT(tag, false);
    }

    @Override
    public void writeToNBT(CompoundNBT tag) {
        writeToNBT(tag, true);
    }

    @Override
    public boolean removeUpgrade(PlayerEntity user, LivingArmourUpgrade upgrade) {
        String key = upgrade.getUniqueIdentifier();
        if (upgradeMap.containsKey(key)) {
            upgradeMap.remove(key);

            return true;
        }

        return false;
    }

    public static boolean hasFullSet(PlayerEntity player) {
        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            if (slot.getSlotType() != EquipmentSlotType.Type.ARMOR) {
                continue;
            }
            ItemStack slotStack = player.getItemStackFromSlot(slot);
            if (slotStack.isEmpty() || !(slotStack.getItem() instanceof ItemLivingArmour))
                return false;
        }

        return true;
    }
}
