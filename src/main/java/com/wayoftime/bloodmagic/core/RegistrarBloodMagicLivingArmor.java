package com.wayoftime.bloodmagic.core;

import com.google.common.collect.Maps;
import com.wayoftime.bloodmagic.BloodMagic;
import com.wayoftime.bloodmagic.core.living.LivingUpgrade;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;

/*
 * TODO - See checklist
 * - [-] Upgrades (Names pulled from 2.0 class names)
 *  - [ ] Arrow Protect
 *  - [ ] Arrow Shot
 *  - [ ] Critical Strike
 *  - [ ] Digging
 *  - [ ] Elytra
 *   - This will wait for Forge to add the ability to make them properly. I'm not adding that hacky shit back in.
 *  - [ ] Experience
 *  - [ ] Fall Protect
 *  - [ ] Fire Resist
 *  - [ ] Grave Digger
 *  - [ ] Grim Reaper Sprint
 *  - [ ] Health boost
 *  - [-] Jump
 *  - [ ] Knockback Resist
 *  - [ ] Melee Damage
 *  - [ ] Night Sight
 *  - [ ] Physical Protect
 *  - [ ] Poison Resist
 *  - [ ] Repairing
 *  - [ ] Self Sacrifice
 *  - [ ] Solar Powered
 *  - [ ] Speed
 *  - [ ] Sprint Attack
 *  - [ ] Step Assist
 * - [ ] Downgrades (Names pulled from 2.0 class names)
 *  - [ ] Battle Hungry
 *  - [ ] Crippled Arm
 *  - [ ] Dig Slowdown
 *  - [ ] Disoriented
 *  - [ ] Melee Decrease
 *  - [ ] Quenched
 *  - [ ] Slippery
 *  - [ ] Slow Heal
 *  - [ ] Slowness
 *  - [ ] Storm Trooper
 * - [-] Equipment
 *  - [x] Living Helmet
 *  - [x] Living Chestplate
 *  - [x] Living Leggings
 *  - [x] Living Boots
 * - [ ] Tools (Replacements for Bound equipment. Need their own (up|down)grade sets once implemented.)
 *  - [ ] Living Sword
 *  - [ ] Living Pickaxe
 *  - [ ] Living Axe
 *  - [ ] Living Shovel
 */
@Mod.EventBusSubscriber(modid = BloodMagic.MODID)
public class RegistrarBloodMagicLivingArmor {

    public static final Map<ResourceLocation, LivingUpgrade> UPGRADES = Maps.newHashMap();
    public static final LivingUpgrade JUMP = new LivingUpgrade(new ResourceLocation(BloodMagic.MODID, "jump"), levels -> {
        levels.add(new LivingUpgrade.UpgradeLevel(10, 1));
        levels.add(new LivingUpgrade.UpgradeLevel(20, 5));
        levels.add(new LivingUpgrade.UpgradeLevel(30, 25));
        levels.add(new LivingUpgrade.UpgradeLevel(40, 125));
    });

    @SubscribeEvent
    public static void registerUpgrades(RegistryEvent.Register<Item> event) {
        addUpgrade(JUMP);
    }

    private static void addUpgrade(LivingUpgrade upgrade) {
        UPGRADES.put(upgrade.getKey(), upgrade);
    }
}
