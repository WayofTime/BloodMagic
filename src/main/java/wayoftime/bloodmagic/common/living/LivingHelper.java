package wayoftime.bloodmagic.common.living;

import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.*;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableFloat;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.datacomponent.LivingStats;
import wayoftime.bloodmagic.common.datacomponent.UpgradeLimits;
import wayoftime.bloodmagic.common.datacomponent.UpgradeTome;
import wayoftime.bloodmagic.common.datamap.BMDataMaps;
import wayoftime.bloodmagic.common.datamap.LivingArmourData;
import wayoftime.bloodmagic.common.event.LivingArmourEvent;
import wayoftime.bloodmagic.common.item.UpgradeHolderBase;
import wayoftime.bloodmagic.common.registry.BMRegistries;
import wayoftime.bloodmagic.common.tag.BMTags;
import wayoftime.bloodmagic.util.ChatUtil;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public class LivingHelper {
    public static boolean hasFullSet(Player player) {
        ItemStack chestStack = getChest(player);
        LivingArmourData data = chestStack.getItemHolder().getData(BMDataMaps.LIVING_ARMOUR_DATA);
        if (data == null) {
            return false;
        }
        TagKey<Item> set = data.requiredSet();
        if (chestStack.getDamageValue() +1 >= chestStack.getMaxDamage()) {
            return false;
        }

        for (ItemStack stack : player.getArmorSlots()) {
            if (!stack.is(set)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isNeverValid(Player player) {
        return isNeverValid(getChest(player));
    }

    public static boolean isNeverValid(ItemStack plate) {
        if (!(plate.getItem() instanceof UpgradeHolderBase)) {
            return true;
        }
        LivingArmourData data = plate.getItemHolder().getData(BMDataMaps.LIVING_ARMOUR_DATA);
        return data == null;
    }

    public static ItemStack getChest(Player player) {
        return player.getItemBySlot(EquipmentSlot.CHEST);
    }

    public static boolean has(ItemStack stack, DataComponentType<?> type) {
        MutableBoolean found = new MutableBoolean(false);
        runIterationOnItem(stack, (upgrade, level) -> {
            if (upgrade.value().effects().has(type)) {
                found.setTrue();
            }
        });
        return found.booleanValue();
    }

    public static boolean has(Player player, DataComponentType<?> type) {
        return has(getChest(player), type);
    }

    public static void runIterationOnPlayer(Player player, BiConsumer<Holder<LivingUpgrade>, Integer> visitor) {
        runIterationOnItem(getChest(player), visitor);
    }

    public static final Object2FloatOpenHashMap<Holder<LivingUpgrade>> EMPTY_UPGRADE_MAP = new Object2FloatOpenHashMap<>();
    public static void runIterationOnItem(ItemStack stack, BiConsumer<Holder<LivingUpgrade>, Integer> visitor) {
        Object2FloatOpenHashMap<Holder<LivingUpgrade>> upgrades = stack.getOrDefault(BMDataComponents.UPGRADES, LivingStats.EMPTY).upgrades();

        for (Object2FloatMap.Entry<Holder<LivingUpgrade>> entry : upgrades.object2FloatEntrySet()) {
            int level = getLevelFromXp(entry.getKey(), entry.getFloatValue());
            if (level < 1) {
                continue;
            }
            visitor.accept(entry.getKey(), level);
        }
    }

    public static int getLevelFromXp(Holder<LivingUpgrade> upgrade, float exp) {
        Map.Entry<Integer, Integer> level = upgrade.value().levels().expToLevel().floorEntry((int) exp);
        return level == null ? 0 : level.getValue();
    }

    public static int getLevelFromXp(ItemStack tomeStack) {
        if (tomeStack.isEmpty()) {
            return 0;
        }
        UpgradeTome tome = tomeStack.get(BMDataComponents.UPGRADE_TOME_DATA);
        if (tome == null) {
            return 0;
        }

        return getLevelFromXp(tome.upgrade(), tome.exp());
    }

    public static int nextLevelExp(Holder<LivingUpgrade> upgrade, float exp) {
        Map.Entry<Integer, Integer> level = upgrade.value().levels().expToLevel().ceilingEntry((int) exp + 1); // otherwise it'll get the same level again
        return level == null ? 0 : level.getKey();
    }

    public static float modifyKnockback(Player player, LivingEntity victim, DamageSource damageSource, float knockback) {
        MutableFloat mutablefloat = new MutableFloat(knockback);
        runIterationOnPlayer(player, (holder, level) -> holder.value().modifyKnockback(level, victim, damageSource, mutablefloat));
        return mutablefloat.floatValue();
    }

    public static int modifyExperience(Player player, int startingValue) {
        MutableFloat value = new MutableFloat(startingValue);
        runIterationOnPlayer(player, (holder, level) -> holder.value().modifyExperience(level, player, value));
        float xp = value.floatValue();
        float mod = xp % 1;
        int toAdd = player.level().random.nextFloat() < mod ? 1 : 0;
        int ret = (int) Math.floor(xp) + toAdd;
        return ret;
    }

    public static float modifyHealing(Player player, float amount) {
        MutableFloat value = new MutableFloat(amount);
        runIterationOnPlayer(player, (holder, level) -> holder.value().modifyHealing(level, player, value));
        return value.getValue();
    }

    public static float modifyDamageDealt(Player playerCauser, LivingEntity victim, DamageSource source, float originalDamage) {
        MutableFloat value = new MutableFloat(originalDamage);
        runIterationOnPlayer(playerCauser, (holder, level) -> holder.value().modifyDamageDealt(level, victim, source, value));
        return value.getValue();
    }

    public static float modifyDamageTaken(Player playerVictim, DamageSource source, float newDamage) {
        MutableFloat value = new MutableFloat(newDamage);
        runIterationOnPlayer(playerVictim, (holder, level) -> holder.value().modifyDamageTaken(level, playerVictim, source, value));
        return value.getValue();
    }

    public static void reactToDamageDealt(Player playerCauser, LivingEntity victim, DamageSource source, float newDamage) {
        runIterationOnPlayer(playerCauser, (holder, level) -> holder.value().reactToDamageDealt(level, victim, source, newDamage));
    }

    public static void reactToDamageTaken(Player playerVictim, DamageSource source, float newDamage) {
        runIterationOnPlayer(playerVictim, (holder, level) -> holder.value().reactToDamageTaken(level, playerVictim, source, newDamage));
    }

    public static void runBlockBroken(Player player, BlockState state) {
        runIterationOnPlayer(player, (holder, level) -> holder.value().blockBroken(level, player, state));
    }

    public static void runTick(Player player) {
        runIterationOnPlayer(player, (holder, level) -> holder.value().tick(level, player));
    }

    public static void runProjectile(Player player, Projectile projectile) {
        runIterationOnPlayer(player, (holder, level) -> holder.value().modifyProjectile(level, player, projectile));
    }

    public static void getAttributes(ItemStack chestStack, ItemAttributeModifiers.Builder builder) {
        runIterationOnItem(chestStack, (holder, level) -> holder.value().collectAttributes(level, builder::add));
    }

    public static float applyExpToCap(Player wearer, Holder<LivingUpgrade> upgrade, float amount, boolean fromTome) {
        float rest = amount;
        float previous;
        do {
            previous = rest;
            rest -= applyExp(wearer, upgrade, rest, fromTome);
        } while (rest != 0 && rest != previous);

        return amount - rest;
    }

    public static float applyExp(Player wearer, Holder<LivingUpgrade> upgrade, float amount) {
        return applyExp(wearer, upgrade, amount, false);
    }

    public static float applyExp(Player wearer, Holder<LivingUpgrade> upgrade, float amount, boolean fromTome) {
        if (!hasFullSet(wearer)) {
            return 0;
        }
        ItemStack chest = getChest(wearer);
        LivingArmourData data = chest.getItemHolder().getData(BMDataMaps.LIVING_ARMOUR_DATA);
        if (data == null) {
            return 0;
        }
        if (upgrade.is(data.blacklist())) {
            return 0;
        }

        Object2FloatOpenHashMap<Holder<LivingUpgrade>> upgrades = chest.getOrDefault(BMDataComponents.UPGRADES, LivingStats.EMPTY).upgrades().clone();
        UpgradeLimits limits = chest.getOrDefault(BMDataComponents.LIMITS, UpgradeLimits.EMPTY);
        UpgradeHolderBase chestBase = (UpgradeHolderBase) chest.getItem();
        int maxPoints = chestBase.getMaxUpgradePoints(chest);
        int currentPoints = chest.getOrDefault(BMDataComponents.CURRENT_UPGRADE_POINTS, 0);

        LivingArmourEvent.ExpGain event = NeoForge.EVENT_BUS.post(new LivingArmourEvent.ExpGain(wearer, upgrade, amount, fromTome));
        if (event.getCurrentAmount() <= 0) { // not dealing with negative exp gain. also dont need to calc this if we know its 0
            return 0;
        }
        MutableFloat toAdd = new MutableFloat(event.getCurrentAmount());
        upgrades.computeFloat(upgrade, (holder, exp) -> {
            exp = exp == null ? 0f : exp;
            float maxExp = limits.getLimit(upgrade);
            if (maxExp != -1) {
                toAdd.setValue(Math.min(maxExp - exp, toAdd.floatValue())); // if there is a limit, respect it
                if (toAdd.floatValue() <= 0) {
                    // limit is reached or overshot, not modifying
                    toAdd.setValue(0);
                    return exp;
                }
            }

            int currLevel = getLevelFromXp(upgrade, exp);
            int currCost = upgrade.value().levels().levelToCost().getOrDefault(currLevel, 0); // can be level 0, dont have an entry for that
            int nextCost = upgrade.value().levels().levelToCost().getOrDefault(currLevel + 1, -1); // can be level max + 1, dont have an entry for that either
            if (nextCost == -1) {
                // we have reached max level and are trying to add exp.
                // so we just do that?
                return exp + toAdd.floatValue();
            }
            float nextExp = nextLevelExp(upgrade, exp);
            if (exp + toAdd.floatValue() >= nextExp) {
                // enough exp to reach next level
                int theoreticalPoints = currentPoints - currCost + nextCost;
                BloodMagic.LOGGER.info("{} - {} + {} = {} <= {}", currentPoints, currCost, nextCost, theoreticalPoints, maxPoints);
                if (theoreticalPoints <= maxPoints) {
                    // enough points, so do it
                    toAdd.setValue(nextExp - exp);
                    BloodMagic.LOGGER.info("levelling up: stored {}, maxGain {}, limit {}, adding {}", exp, amount, maxExp, toAdd.floatValue());
                    chest.set(BMDataComponents.CURRENT_UPGRADE_POINTS, theoreticalPoints);
                    // there used to be a level up event too? TODO is this needed?
                    wearer.displayClientMessage(Component.translatable("chat.bloodmagic.living_upgrade.level_up", Component.translatable(LivingUpgrade.descriptionId(upgrade.getKey())), currLevel + 1), true);
                    return exp + toAdd.floatValue();
                }
                // if we're here we did not have enough points
                float maxAdd = (nextExp - 1) - exp; // parenthesis because I dont want to come back here later because of shenanigans with math interpretation
                toAdd.setValue(Math.min(maxAdd, toAdd.floatValue()));
            }

            return exp + toAdd.floatValue();
        });
        chest.set(BMDataComponents.UPGRADES, new LivingStats(upgrades));
        return toAdd.floatValue();
    }

    public static Component getTooltip(Holder<LivingUpgrade> upgrade, float exp, boolean hasShiftDown) {
        int level = getLevelFromXp(upgrade, exp);
        int nextExp = nextLevelExp(upgrade, exp);
        Component levelComp = Component.literal(ChatUtil.toRoman(level));
        Component expComp = Component.literal("%s/%s".formatted((int) exp, nextExp));
        if (nextExp == 0) {
            expComp = Component.literal("%s/".formatted((int) exp)).append(Component.literal(Integer.toString((int) exp)).withStyle(ChatFormatting.OBFUSCATED));
        }

        ChatFormatting colour = ChatFormatting.YELLOW;
        if (upgrade.is(BMTags.Living.IS_DOWNGRADE)) {
            colour = ChatFormatting.RED;
        }

        ChatFormatting style = colour;
        if (level < 1) {
            style = ChatFormatting.ITALIC;
            levelComp = Component.literal("0").withStyle(ChatFormatting.OBFUSCATED);
        }

        MutableComponent mutable = Component.translatable(LivingUpgrade.descriptionId(upgrade.getKey())).withStyle(style, colour);

        if (hasShiftDown) {
            mutable.append(CommonComponents.SPACE).append(expComp);
        } else {
            mutable.append(CommonComponents.SPACE).append(levelComp);
        }

        return mutable;
    }

    public static Object2FloatOpenHashMap<Holder<LivingUpgrade>> fromHolderSet(HolderSet<LivingUpgrade> template) {
        return fromHolderSet(template, 1);
    }

    public static Object2FloatOpenHashMap<Holder<LivingUpgrade>> fromHolderSet(HolderSet<LivingUpgrade> template, float val) {
        Object2FloatOpenHashMap<Holder<LivingUpgrade>> ret = new Object2FloatOpenHashMap<>();
        template.forEach(holder -> ret.put(holder, val));
        return ret;
    }

    public static void setDefaultLiving(ItemStack livingPlate, HolderLookup.Provider holders) {
        LivingArmourData data = livingPlate.getItemHolder().getData(BMDataMaps.LIVING_ARMOUR_DATA);
        if (data == null) {
            return;
        }
        HolderSet<LivingUpgrade> startingSet = holders.lookupOrThrow(BMRegistries.Keys.LIVING_UPGRADES).get(data.startingUpgrades()).orElseThrow();
        livingPlate.set(BMDataComponents.UPGRADES, new LivingStats(fromHolderSet(startingSet)));
        livingPlate.set(BMDataComponents.IS_EVOLVED, false);
    }

    public static int recalcPoints(Player player) {
        ItemStack chest = getChest(player);
        Object2FloatOpenHashMap<Holder<LivingUpgrade>> upgrades = chest.getOrDefault(BMDataComponents.UPGRADES, LivingStats.EMPTY).upgrades();

        int total = 0;
        for (Map.Entry<Holder<LivingUpgrade>, Float> entry : upgrades.object2FloatEntrySet()) {
            total += entry.getKey().value().levels().levelToCost().getOrDefault(getLevelFromXp(entry.getKey(), entry.getValue()), 0);
        }

        chest.set(BMDataComponents.CURRENT_UPGRADE_POINTS, total);

        return total;
    }

    public static Pair<Integer, Float> scrapFromTome(UpgradeTome tome) {
        Map.Entry<Integer, Integer> expEntry = tome.upgrade().value().levels().expToLevel().floorEntry((int) tome.exp());
        int scrap = 0;
        float expUsed = 0;
        if (expEntry != null) {
            scrap = tome.upgrade().value().levels().levelToCost().getOrDefault(expEntry.getValue(), 0);
            expUsed = expEntry.getKey();
        }

        return Pair.of(scrap, expUsed);
    }

    public static int getExpForLevel(Holder<LivingUpgrade> upgrade, int level) {
        AtomicInteger exp = new AtomicInteger(-1);
        upgrade.value().levels().expToLevel().forEach((k, v) -> {
            if (v == level) {
                exp.set(k);
            }
        });

        return exp.get();
    }

    public static int getMaxLevel(Holder<LivingUpgrade> upgrade) {
        return upgrade.value().levels().levelToCost().size();
    }
}
