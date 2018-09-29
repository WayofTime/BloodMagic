package com.wayoftime.bloodmagic.core;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wayoftime.bloodmagic.BloodMagic;
import com.wayoftime.bloodmagic.core.living.LivingUpgrade;
import com.wayoftime.bloodmagic.core.util.ResourceUtil;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * TODO - See checklist
 * - [-] Upgrades (Names pulled from 2.0 class names)
 *  - [x] Arrow Protect
 *  - [-] Arrow Shot
 *  - [-] Critical Strike
 *  - [ ] Digging
 *  - [ ] Elytra
 *   - This will wait for Forge to add the ability to make them properly. I'm not adding that hacky shit back in.
 *  - [ ] Experience
 *  - [ ] Fall Protect
 *  - [ ] Fire Resist
 *  - [ ] Grave Digger
 *  - [ ] Grim Reaper Sprint
 *  - [ ] Health boost
 *  - [x] Jump
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

    private static final Map<String, Path> DEFINITIONS = ResourceUtil.gatherResources("/data", "living_armor", p -> FilenameUtils.getExtension(p.toFile().getName()).equals("json")).stream().collect(Collectors.toMap(key -> FilenameUtils.getBaseName(key.toFile().getName()), value -> value));
    private static final Gson GSON = new GsonBuilder().serializeNulls().create();
    public static final Map<ResourceLocation, LivingUpgrade> UPGRADES = Maps.newHashMap();
    public static final LivingUpgrade UPGRADE_ARROW_PROTECT = parseDefinition("arrow_protect");
    public static final LivingUpgrade UPGRADE_ARROW_SHOT = parseDefinition("arrow_shot");
    public static final LivingUpgrade UPGRADE_CRITICAL_STRIKE = parseDefinition("critical_strike");
    public static final LivingUpgrade UPGRADE_JUMP = parseDefinition("jump");

    @SubscribeEvent
    public static void registerUpgrades(RegistryEvent.Register<Item> event) {
        addUpgrade(UPGRADE_ARROW_PROTECT);
        addUpgrade(UPGRADE_ARROW_SHOT);
        addUpgrade(UPGRADE_CRITICAL_STRIKE);
        addUpgrade(UPGRADE_JUMP);
    }

    private static void addUpgrade(LivingUpgrade upgrade) {
        UPGRADES.put(upgrade.getKey(), upgrade);
    }

    @Nonnull
    public static LivingUpgrade parseDefinition(String fileName) {
        Path path = DEFINITIONS.get(fileName);
        if (path == null)
            return LivingUpgrade.DUMMY;

        try {
            return GSON.fromJson(IOUtils.toString(path.toUri(), StandardCharsets.UTF_8), LivingUpgrade.class);
        } catch (Exception e) {
            e.printStackTrace();
            return LivingUpgrade.DUMMY;
        }
    }
}
