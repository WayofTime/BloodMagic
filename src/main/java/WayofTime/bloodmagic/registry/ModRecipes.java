package WayofTime.bloodmagic.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.tuple.Pair;

import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectArrowTurret;
import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectAttractor;
import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectBinding;
import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectBounce;
import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectFurnaceFuel;
import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectLaputa;
import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectMovement;
import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectSigil;
import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectSkeletonTurret;
import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectSpike;
import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectTeleport;
import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectUpdraft;
import WayofTime.bloodmagic.client.render.alchemyArray.AttractorAlchemyCircleRenderer;
import WayofTime.bloodmagic.client.render.alchemyArray.BindingAlchemyCircleRenderer;
import WayofTime.bloodmagic.client.render.alchemyArray.DualAlchemyCircleRenderer;
import WayofTime.bloodmagic.client.render.alchemyArray.LowAlchemyCircleRenderer;
import WayofTime.bloodmagic.client.render.alchemyArray.LowStaticAlchemyCircleRenderer;
import WayofTime.bloodmagic.client.render.alchemyArray.SingleAlchemyCircleRenderer;
import WayofTime.bloodmagic.client.render.alchemyArray.StaticAlchemyCircleRenderer;
import WayofTime.bloodmagic.client.render.alchemyArray.TurretAlchemyCircleRenderer;
import WayofTime.bloodmagic.compress.AdvancedCompressionHandler;
import WayofTime.bloodmagic.compress.BaseCompressionHandler;
import WayofTime.bloodmagic.compress.CompressionRegistry;
import WayofTime.bloodmagic.compress.StorageBlockCraftingManager;
import WayofTime.bloodmagic.core.RegistrarBloodMagic;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.core.registry.AlchemyArrayRecipeRegistry;
import WayofTime.bloodmagic.core.registry.AlchemyTableRecipeRegistry;
import WayofTime.bloodmagic.core.registry.LivingArmourDowngradeRecipeRegistry;
import WayofTime.bloodmagic.iface.ISigil;
import WayofTime.bloodmagic.item.types.ComponentTypes;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.livingArmour.downgrade.LivingArmourUpgradeBattleHungry;
import WayofTime.bloodmagic.livingArmour.downgrade.LivingArmourUpgradeCrippledArm;
import WayofTime.bloodmagic.livingArmour.downgrade.LivingArmourUpgradeDigSlowdown;
import WayofTime.bloodmagic.livingArmour.downgrade.LivingArmourUpgradeDisoriented;
import WayofTime.bloodmagic.livingArmour.downgrade.LivingArmourUpgradeMeleeDecrease;
import WayofTime.bloodmagic.livingArmour.downgrade.LivingArmourUpgradeQuenched;
import WayofTime.bloodmagic.livingArmour.downgrade.LivingArmourUpgradeSlowHeal;
import WayofTime.bloodmagic.livingArmour.downgrade.LivingArmourUpgradeSlowness;
import WayofTime.bloodmagic.livingArmour.downgrade.LivingArmourUpgradeStormTrooper;
import WayofTime.bloodmagic.potion.BMPotionUtils;
import WayofTime.bloodmagic.recipe.alchemyTable.AlchemyTableDyeableRecipe;
import WayofTime.bloodmagic.recipe.alchemyTable.AlchemyTablePotionRecipe;
import WayofTime.bloodmagic.util.BMLog;
import WayofTime.bloodmagic.util.Utils;

import com.google.common.base.Stopwatch;

public class ModRecipes
{

    static ItemStack mundaneLengtheningStack = ComponentTypes.CATALYST_LENGTH_1.getStack();
    static ItemStack mundanePowerStack = ComponentTypes.CATALYST_POWER_1.getStack();

    public static void init()
    {
        initOreDict();
        addFurnaceRecipes();
        addAltarRecipes();
        addAlchemyArrayRecipes();
        addAlchemyTableRecipes();
        addPotionRecipes();
        addLivingArmourDowngradeRecipes();
    }

    public static void initOreDict()
    {
        OreDictionary.registerOre("dustIron", ComponentTypes.SAND_IRON.getStack());
        OreDictionary.registerOre("dustGold", ComponentTypes.SAND_GOLD.getStack());
        OreDictionary.registerOre("dustCoal", ComponentTypes.SAND_COAL.getStack());
        OreDictionary.registerOre("dustSulfur", ComponentTypes.SULFUR.getStack());
        OreDictionary.registerOre("dustSaltpeter", ComponentTypes.SALTPETER.getStack());
    }

    public static void addFurnaceRecipes()
    {
        FurnaceRecipes.instance().addSmeltingRecipe(ComponentTypes.SAND_IRON.getStack(), new ItemStack(Items.IRON_INGOT), (float) 0.15);
        FurnaceRecipes.instance().addSmeltingRecipe(ComponentTypes.SAND_GOLD.getStack(), new ItemStack(Items.GOLD_INGOT), (float) 0.15);
    }

    public static void addAltarRecipes()
    {

    }

    public static void addAlchemyArrayRecipes()
    {
        AlchemyArrayRecipeRegistry.registerRecipe(ComponentTypes.REAGENT_BINDING.getStack(), new ItemStack(Items.DIAMOND_SWORD), new AlchemyArrayEffectBinding("boundSword", Utils.setUnbreakable(new ItemStack(RegistrarBloodMagicItems.BOUND_SWORD))), new BindingAlchemyCircleRenderer());
        AlchemyArrayRecipeRegistry.registerRecipe(ComponentTypes.REAGENT_BINDING.getStack(), new ItemStack(Items.DIAMOND_AXE), new AlchemyArrayEffectBinding("boundAxe", Utils.setUnbreakable(new ItemStack(RegistrarBloodMagicItems.BOUND_AXE))));
        AlchemyArrayRecipeRegistry.registerRecipe(ComponentTypes.REAGENT_BINDING.getStack(), new ItemStack(Items.DIAMOND_PICKAXE), new AlchemyArrayEffectBinding("boundPickaxe", Utils.setUnbreakable(new ItemStack(RegistrarBloodMagicItems.BOUND_PICKAXE))));
        AlchemyArrayRecipeRegistry.registerRecipe(ComponentTypes.REAGENT_BINDING.getStack(), new ItemStack(Items.DIAMOND_SHOVEL), new AlchemyArrayEffectBinding("boundShovel", Utils.setUnbreakable(new ItemStack(RegistrarBloodMagicItems.BOUND_SHOVEL))));
        AlchemyArrayRecipeRegistry.registerRecipe(ComponentTypes.REAGENT_BINDING.getStack(), new ItemStack(Items.IRON_HELMET), new AlchemyArrayEffectBinding("livingHelmet", new ItemStack(RegistrarBloodMagicItems.LIVING_ARMOUR_HELMET)));
        AlchemyArrayRecipeRegistry.registerRecipe(ComponentTypes.REAGENT_BINDING.getStack(), new ItemStack(Items.IRON_CHESTPLATE), new AlchemyArrayEffectBinding("livingChest", new ItemStack(RegistrarBloodMagicItems.LIVING_ARMOUR_CHEST)));
        AlchemyArrayRecipeRegistry.registerRecipe(ComponentTypes.REAGENT_BINDING.getStack(), new ItemStack(Items.IRON_LEGGINGS), new AlchemyArrayEffectBinding("livingLegs", new ItemStack(RegistrarBloodMagicItems.LIVING_ARMOUR_LEGGINGS)));
        AlchemyArrayRecipeRegistry.registerRecipe(ComponentTypes.REAGENT_BINDING.getStack(), new ItemStack(Items.IRON_BOOTS), new AlchemyArrayEffectBinding("livingBoots", new ItemStack(RegistrarBloodMagicItems.LIVING_ARMOUR_BOOTS)));

        AlchemyArrayRecipeRegistry.registerRecipe(new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.ROTTEN_FLESH), new AlchemyArrayEffectAttractor("attractor"), new AttractorAlchemyCircleRenderer());
        AlchemyArrayRecipeRegistry.registerRecipe(new ItemStack(Items.FEATHER), new ItemStack(Items.REDSTONE), new AlchemyArrayEffectMovement("movement"), new StaticAlchemyCircleRenderer(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/MovementArray.png")));
        AlchemyArrayRecipeRegistry.registerRecipe(new ItemStack(Items.FEATHER), new ItemStack(Items.GLOWSTONE_DUST), new AlchemyArrayEffectUpdraft("updraft"), new AttractorAlchemyCircleRenderer(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/UpdraftArray.png")));
        AlchemyArrayRecipeRegistry.registerRecipe(new ItemStack(Items.SLIME_BALL), new ItemStack(Items.REDSTONE), new AlchemyArrayEffectBounce("bounce"), new SingleAlchemyCircleRenderer(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/BounceArray.png")));
        AlchemyArrayRecipeRegistry.registerRecipe(new ItemStack(Items.COAL), new ItemStack(Items.REDSTONE), new AlchemyArrayEffectFurnaceFuel("furnace"), new LowAlchemyCircleRenderer(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/FurnaceArray.png")));

        AlchemyArrayRecipeRegistry.registerRecipe(new ItemStack(Items.ARROW), new ItemStack(Items.FEATHER), new AlchemyArrayEffectSkeletonTurret("skeletonTurret"), new DualAlchemyCircleRenderer(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/SkeletonTurret1.png"), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/SkeletonTurret2.png")));

        AlchemyArrayRecipeRegistry.registerRecipe(new ItemStack(Items.ENDER_PEARL), new ItemStack(Items.REDSTONE), new AlchemyArrayEffectTeleport("teleport"), new StaticAlchemyCircleRenderer(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/teleportation.png")));
        AlchemyArrayRecipeRegistry.registerRecipe(new ItemStack(Items.BOW), new ItemStack(Items.ARROW), new AlchemyArrayEffectArrowTurret("turret"), new TurretAlchemyCircleRenderer(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/SkeletonTurret1.png")));
        AlchemyArrayRecipeRegistry.registerRecipe(new ItemStack(Items.REDSTONE), new ItemStack(Blocks.LAPIS_BLOCK), new AlchemyArrayEffectLaputa("laputa"), new AttractorAlchemyCircleRenderer(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/shardoflaputa.png")));
        AlchemyArrayRecipeRegistry.registerRecipe(new ItemStack(Blocks.COBBLESTONE), new ItemStack(Items.IRON_INGOT), new AlchemyArrayEffectSpike("spike"), new LowStaticAlchemyCircleRenderer(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/spikearray.png")));

        AlchemyArrayRecipeRegistry.registerRecipe(ComponentTypes.REAGENT_FAST_MINER.getStack(), new ItemStack(Items.IRON_PICKAXE), new AlchemyArrayEffectSigil("fastMiner", (ISigil) RegistrarBloodMagicItems.SIGIL_FAST_MINER), new SingleAlchemyCircleRenderer(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/FastMinerSigil.png")));

    }

    public static void addCompressionHandlers()
    {
        Stopwatch stopwatch = Stopwatch.createStarted();
        StorageBlockCraftingManager.getInstance().addStorageBlockRecipes();
        CompressionRegistry.registerHandler(new BaseCompressionHandler(new ItemStack(Items.GLOWSTONE_DUST, 4, 0), new ItemStack(Blocks.GLOWSTONE), 64));
        CompressionRegistry.registerHandler(new BaseCompressionHandler(new ItemStack(Items.SNOWBALL, 4, 0), new ItemStack(Blocks.SNOW), 8));
        CompressionRegistry.registerHandler(new AdvancedCompressionHandler());

        CompressionRegistry.registerItemThreshold(new ItemStack(Blocks.COBBLESTONE), 64);
        stopwatch.stop();

        BMLog.DEBUG.info("Added compression recipes in {}", stopwatch);
    }

    public static void addAlchemyTableRecipes()
    {
        AlchemyTableRecipeRegistry.registerRecipe(new AlchemyTableDyeableRecipe(0, 100, 0, new ItemStack(RegistrarBloodMagicItems.SIGIL_HOLDING)));
    }

    public static void addPotionRecipes()
    {
        addPotionRecipe(1000, 1, new ItemStack(Items.GHAST_TEAR), new PotionEffect(MobEffects.REGENERATION, 450));
        addPotionRecipe(1000, 1, new ItemStack(Items.GOLDEN_CARROT), new PotionEffect(MobEffects.NIGHT_VISION, 2 * 60 * 20));
        addPotionRecipe(1000, 1, new ItemStack(Items.MAGMA_CREAM), new PotionEffect(MobEffects.FIRE_RESISTANCE, 2 * 60 * 20));
        addPotionRecipe(1000, 1, new ItemStack(Items.WATER_BUCKET), new PotionEffect(MobEffects.WATER_BREATHING, 2 * 60 * 20));
        addPotionRecipe(1000, 1, new ItemStack(Items.SUGAR), new PotionEffect(MobEffects.SPEED, 2 * 60 * 20));
        addPotionRecipe(1000, 1, new ItemStack(Items.SPECKLED_MELON), new PotionEffect(MobEffects.INSTANT_HEALTH, 1));
        addPotionRecipe(1000, 1, new ItemStack(Items.SPIDER_EYE), new PotionEffect(MobEffects.POISON, 450));
        addPotionRecipe(1000, 1, new ItemStack(Items.DYE, 1, 0), new PotionEffect(MobEffects.BLINDNESS, 450));
        addPotionRecipe(1000, 1, new ItemStack(Items.FERMENTED_SPIDER_EYE), new PotionEffect(MobEffects.WEAKNESS, 450));
        addPotionRecipe(1000, 1, new ItemStack(Items.BLAZE_POWDER), new PotionEffect(MobEffects.STRENGTH, 2 * 60 * 20));
        addPotionRecipe(1000, 1, new ItemStack(Items.FEATHER), new PotionEffect(MobEffects.JUMP_BOOST, 2 * 60 * 20));
        addPotionRecipe(1000, 1, new ItemStack(Items.CLAY_BALL), new PotionEffect(MobEffects.SLOWNESS, 450));
        addPotionRecipe(1000, 1, new ItemStack(Items.REDSTONE), new PotionEffect(MobEffects.HASTE, 2 * 60 * 20));
        addPotionRecipe(1000, 1, new ItemStack(Items.GLASS_BOTTLE), new PotionEffect(MobEffects.INVISIBILITY, 2 * 60 * 20));
        addPotionRecipe(1000, 1, new ItemStack(Items.POISONOUS_POTATO), new PotionEffect(MobEffects.SATURATION, 1));
        addPotionRecipe(1000, 1, new ItemStack(RegistrarBloodMagicItems.BLOOD_SHARD, 1, 0), new PotionEffect(MobEffects.HEALTH_BOOST, 2 * 60 * 20));
        addPotionRecipe(1000, 1, new ItemStack(Blocks.SLIME_BLOCK), new PotionEffect(RegistrarBloodMagic.BOUNCE, 2 * 60 * 20));
        addPotionRecipe(1000, 1, new ItemStack(Items.STRING), new PotionEffect(RegistrarBloodMagic.CLING, 2 * 60 * 20));

        addPotionRecipe(1000, 1, new ItemStack(Items.BEETROOT), new PotionEffect(RegistrarBloodMagic.DEAFNESS, 450));
    }

    public static void addPotionRecipe(int lpDrained, int tier, ItemStack inputStack, PotionEffect baseEffect)
    {
        AlchemyTableRecipeRegistry.registerRecipe(new AlchemyTablePotionRecipe(lpDrained, 100, tier, inputStack, baseEffect));

        List<ItemStack> lengtheningList = new ArrayList<>();
        lengtheningList.add(inputStack);
        lengtheningList.add(mundaneLengtheningStack);
        AlchemyTableRecipeRegistry.registerRecipe(BMPotionUtils.getLengthAugmentRecipe(lpDrained, 100, tier, lengtheningList, baseEffect, 1));

        List<ItemStack> powerList = new ArrayList<>();
        powerList.add(inputStack);
        powerList.add(mundanePowerStack);
        AlchemyTableRecipeRegistry.registerRecipe(BMPotionUtils.getPowerAugmentRecipe(lpDrained, 100, tier, powerList, baseEffect, 1));
    }

    public static void addLivingArmourDowngradeRecipes()
    {
        String messageBase = "ritual.bloodmagic.downgradeRitual.dialogue.";

        ItemStack bowStack = new ItemStack(Items.BOW);
        ItemStack bottleStack = new ItemStack(Items.POTIONITEM, 1, 0);
        ItemStack swordStack = new ItemStack(Items.STONE_SWORD);
        ItemStack goldenAppleStack = new ItemStack(Items.GOLDEN_APPLE);
        ItemStack fleshStack = new ItemStack(Items.ROTTEN_FLESH);
        ItemStack shieldStack = new ItemStack(Items.SHIELD);
        ItemStack pickStack = new ItemStack(Items.STONE_PICKAXE);
        ItemStack minecartStack = new ItemStack(Items.MINECART);
        ItemStack stringStack = new ItemStack(Items.STRING);

        Map<ItemStack, Pair<String, int[]>> dialogueMap = new HashMap<>();
        dialogueMap.put(bowStack, Pair.of("bow", new int[] { 1, 100, 300, 500 }));
        dialogueMap.put(bottleStack, Pair.of("quenched", new int[] { 1, 100, 300, 500 }));
        dialogueMap.put(swordStack, Pair.of("dulledBlade", new int[] { 1, 100, 300, 500, 700 }));
        dialogueMap.put(goldenAppleStack, Pair.of("slowHeal", new int[] { 1, 100, 300, 500, 700 }));

        for (Entry<ItemStack, Pair<String, int[]>> entry : dialogueMap.entrySet())
        {
            ItemStack keyStack = entry.getKey();
            String str = entry.getValue().getKey();
            Map<Integer, List<ITextComponent>> textMap = new HashMap<>();
            for (int tick : entry.getValue().getValue())
            {
                List<ITextComponent> textList = new ArrayList<>();
                textList.add(new TextComponentTranslation("\u00A74%s", new TextComponentTranslation(messageBase + str + "." + tick)));
                textMap.put(tick, textList);
            }

            LivingArmourDowngradeRecipeRegistry.registerDialog(keyStack, textMap);
        }

        LivingArmourDowngradeRecipeRegistry.registerRecipe(new LivingArmourUpgradeStormTrooper(0), bowStack, Items.ARROW, "string", "ingotIron", "ingotIron");
        LivingArmourDowngradeRecipeRegistry.registerRecipe(new LivingArmourUpgradeStormTrooper(1), bowStack, Items.SPECTRAL_ARROW, "ingotGold", "dustRedstone", "dustGlowstone", "gemLapis");
        LivingArmourDowngradeRecipeRegistry.registerRecipe(new LivingArmourUpgradeStormTrooper(2), bowStack, "gemDiamond", Items.FIRE_CHARGE, Items.BLAZE_ROD, "feather");
        LivingArmourDowngradeRecipeRegistry.registerRecipe(new LivingArmourUpgradeStormTrooper(3), bowStack, Items.PRISMARINE_SHARD, Items.BLAZE_ROD, "feather", "feather");
        LivingArmourDowngradeRecipeRegistry.registerRecipe(new LivingArmourUpgradeStormTrooper(4), bowStack, new ItemStack(Items.TIPPED_ARROW, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(Items.TIPPED_ARROW, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(Items.TIPPED_ARROW, 1, OreDictionary.WILDCARD_VALUE));
//        LivingArmourDowngradeRecipeRegistry.registerDialog(bowStack, bowMap);
        LivingArmourDowngradeRecipeRegistry.registerRecipe(new LivingArmourUpgradeQuenched(0), bottleStack, Items.DRAGON_BREATH);
        LivingArmourDowngradeRecipeRegistry.registerRecipe(new LivingArmourUpgradeCrippledArm(0), shieldStack, "gemDiamond");

        for (int i = 0; i < 10; i++)
        {
            addRecipeForTieredDowngrade(new LivingArmourUpgradeMeleeDecrease(i), swordStack, i);
            addRecipeForTieredDowngrade(new LivingArmourUpgradeSlowHeal(i), goldenAppleStack, i);
            addRecipeForTieredDowngrade(new LivingArmourUpgradeBattleHungry(i), fleshStack, i);
            addRecipeForTieredDowngrade(new LivingArmourUpgradeDigSlowdown(i), pickStack, i);
            addRecipeForTieredDowngrade(new LivingArmourUpgradeDisoriented(i), minecartStack, i);
            addRecipeForTieredDowngrade(new LivingArmourUpgradeSlowness(i), stringStack, i);
        }
    }

    public static void addRecipeForTieredDowngrade(LivingArmourUpgrade upgrade, ItemStack stack, int tier)
    {
        switch (tier)
        {
        case 0:
            LivingArmourDowngradeRecipeRegistry.registerRecipe(upgrade, stack, "ingotIron", new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 0));
            break;
        case 1:
            LivingArmourDowngradeRecipeRegistry.registerRecipe(upgrade, stack, "dustRedstone", "dustRedstone", "ingotIron", new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 0));
            break;
        case 2:
            LivingArmourDowngradeRecipeRegistry.registerRecipe(upgrade, stack, "ingotGold", "gemLapis", "gemLapis", new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 1));
            break;
        case 3:
            LivingArmourDowngradeRecipeRegistry.registerRecipe(upgrade, stack, Blocks.VINE, "dyeRed", Items.GOLDEN_CARROT, new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 1));
            break;
        case 4:
            LivingArmourDowngradeRecipeRegistry.registerRecipe(upgrade, stack, Items.GOLDEN_APPLE, "treeSapling", "treeSapling", new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 2));
            break;
        case 5:
            LivingArmourDowngradeRecipeRegistry.registerRecipe(upgrade, stack, Blocks.IRON_BLOCK, Blocks.REDSTONE_BLOCK, new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 2));
            break;
        case 6:
            LivingArmourDowngradeRecipeRegistry.registerRecipe(upgrade, stack, Blocks.IRON_BLOCK, Blocks.GLOWSTONE, "ingotGold", "ingotGold", new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 3));
            break;
        case 7:
            LivingArmourDowngradeRecipeRegistry.registerRecipe(upgrade, stack, Blocks.GOLD_BLOCK, Blocks.LAPIS_BLOCK, "gemDiamond", new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 3));
            break;
        case 8:
            LivingArmourDowngradeRecipeRegistry.registerRecipe(upgrade, stack, Items.DRAGON_BREATH, "gemDiamond", new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 4));
            break;
        case 9:
            LivingArmourDowngradeRecipeRegistry.registerRecipe(upgrade, stack, Items.NETHER_STAR, "gemDiamond", "gemDiamond", new ItemStack(RegistrarBloodMagicItems.SLATE, 1, 4));
        }
    }
}
