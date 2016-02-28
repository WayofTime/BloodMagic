package WayofTime.bloodmagic.registry;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectBinding;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.altar.EnumAltarTier;
import WayofTime.bloodmagic.api.compress.CompressionRegistry;
import WayofTime.bloodmagic.api.recipe.ShapedBloodOrbRecipe;
import WayofTime.bloodmagic.api.recipe.ShapelessBloodOrbRecipe;
import WayofTime.bloodmagic.api.registry.AlchemyArrayRecipeRegistry;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry;
import WayofTime.bloodmagic.api.registry.OrbRegistry;
import WayofTime.bloodmagic.api.registry.TartaricForgeRecipeRegistry;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.block.BlockBloodRune;
import WayofTime.bloodmagic.client.render.alchemyArray.BindingAlchemyCircleRenderer;
import WayofTime.bloodmagic.compress.AdvancedCompressionHandler;
import WayofTime.bloodmagic.compress.BaseCompressionHandler;
import WayofTime.bloodmagic.compress.StorageBlockCraftingManager;
import WayofTime.bloodmagic.item.ItemComponent;
import WayofTime.bloodmagic.item.ItemDemonCrystal;

public class ModRecipes
{
    public static void init()
    {
        RecipeSorter.register(Constants.Mod.DOMAIN + "shapedorb", ShapedBloodOrbRecipe.class, RecipeSorter.Category.SHAPED, "before:minecraft:shapeless");
        RecipeSorter.register(Constants.Mod.DOMAIN + "shapelessorb", ShapelessBloodOrbRecipe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");

        addCraftingRecipes();
        addAltarRecipes();
        addAlchemyArrayRecipes();
        addSoulForgeRecipes();
    }

    public static void addCraftingRecipes()
    {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.soulForge), "i i", "sgs", "sos", 'i', "ingotIron", 's', "stone", 'g', "ingotGold", 'o', "blockIron"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.sacrificialDagger), "aaa", " ba", "c a", 'a', "blockGlass", 'b', "ingotGold", 'c', "ingotIron"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.altar), "a a", "aba", "cdc", 'a', "stone", 'b', Blocks.furnace, 'c', "ingotGold", 'd', new ItemStack(ModItems.monsterSoul)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.packSelfSacrifice), "aba", "cdc", "aea", 'a', "blockGlass", 'b', Items.bucket, 'c', Items.flint, 'd', Items.leather_chestplate, 'e', ModItems.slate));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.packSacrifice), "aba", "cdc", "aea", 'a', "blockGlass", 'b', Items.bucket, 'c', "ingotIron", 'd', Items.leather_chestplate, 'e', ModItems.slate));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.ritualDiviner), "dfd", "ase", "dwd", 'f', EnumRuneType.FIRE.getScribeStack(), 'a', EnumRuneType.AIR.getScribeStack(), 'w', EnumRuneType.WATER.getScribeStack(), 'e', EnumRuneType.EARTH.getScribeStack(), 'd', "gemDiamond", 's', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.ritualDiviner, 1, 1), " S ", "tdt", " S ", 'S', new ItemStack(ModItems.slate, 1, 3), 't', EnumRuneType.DUSK.getScribeStack(), 'd', new ItemStack(ModItems.ritualDiviner)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.bloodStoneBrick, 1, 1), "aa", "aa", 'a', new ItemStack(ModBlocks.bloodStoneBrick)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.bloodStoneBrick), "stone", new ItemStack(ModItems.bloodShard)));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.activationCrystal, 1, 1), new ItemStack(Items.nether_star), OrbRegistry.getOrbStack(ModItems.orbArchmage));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.soulSnare, 4, 0), "sis", "iri", "sis", 's', new ItemStack(Items.string), 'i', "ingotIron", 'r', "dustRedstone"));

        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.telepositionFocus, 1, 2), new ItemStack(ModItems.telepositionFocus, 1, 1), new ItemStack(ModItems.bloodShard));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.telepositionFocus, 1, 3), new ItemStack(ModItems.telepositionFocus, 1, 2), new ItemStack(ModItems.bloodShard, 1, 1));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.teleposer), "ggg", "efe", "ggg", 'g', "ingotGold", 'e', new ItemStack(Items.ender_pearl), 'f', ModItems.telepositionFocus));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.lavaCrystal), "aba", "bcb", "ded", 'a', "blockGlass", 'b', Items.lava_bucket, 'c', OrbRegistry.getOrbStack(ModItems.orbWeak), 'd', Blocks.obsidian, 'e', "gemDiamond"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.bloodRune), "aaa", "bcb", "aaa", 'a', "stone", 'b', ModItems.slate, 'c', OrbRegistry.getOrbStack(ModItems.orbWeak)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.bloodRune, 1, 1), "aba", "cdc", "aba", 'a', "stone", 'b', ModItems.slate, 'c', Items.sugar, 'd', new ItemStack(ModBlocks.bloodRune))); //Speed
//        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.bloodRune, 1, 2), "aba", "cdc", "aba", 'a', "stone", 'b', ModItems.slate, 'c', Items.sugar, 'd', new ItemStack(ModBlocks.bloodRune))); //Efficiency
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.bloodRune, 1, 3), "aba", "cdc", "aea", 'a', "stone", 'b', new ItemStack(ModItems.slate, 1, 1), 'c', "ingotGold", 'd', new ItemStack(ModBlocks.bloodRune), 'e', OrbRegistry.getOrbStack(ModItems.orbApprentice))); //Sacrifice
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.bloodRune, 1, 4), "aba", "cdc", "aea", 'a', "stone", 'b', new ItemStack(ModItems.slate, 1, 1), 'c', "dustGlowstone", 'd', new ItemStack(ModBlocks.bloodRune), 'e', OrbRegistry.getOrbStack(ModItems.orbApprentice))); //Self-Sacrifice
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.bloodRune, 1, 5), "aba", "bcb", "ada", 'a', "stone", 'b', Items.water_bucket, 'c', new ItemStack(ModBlocks.bloodRune), 'd', new ItemStack(ModItems.slate, 1, 2))); //Displacement
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.bloodRune, 1, 6), "aba", "bcb", "ada", 'a', "stone", 'b', Items.bucket, 'c', new ItemStack(ModBlocks.bloodRune), 'd', new ItemStack(ModItems.slate, 1, 2))); //Capacity
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.bloodRune, 1, 7), "aba", "cdc", "aea", 'a', Blocks.obsidian, 'b', new ItemStack(ModItems.slate, 1, 3), 'c', Items.bucket, 'd', new ItemStack(ModBlocks.bloodRune, 1, 6), 'e', OrbRegistry.getOrbStack(ModItems.orbMaster))); //Augmented Capacity
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.bloodRune, 1, 8), "aba", "cdc", "aba", 'a', "stone", 'b', OrbRegistry.getOrbStack(ModItems.orbWeak), 'c', new ItemStack(ModBlocks.bloodRune), 'd', OrbRegistry.getOrbStack(ModItems.orbMaster))); //Orb
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.bloodRune, 1, 9), "aba", "cdc", "aea", 'a', Items.bucket, 'b', new ItemStack(ModItems.slate, 1, 4), 'c', "ingotGold", 'd', new ItemStack(ModBlocks.bloodRune, 1, 1), 'e', OrbRegistry.getOrbStack(ModItems.orbArchmage))); //Acceleration
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.ritualStone, 4), "aba", "bcb", "aba", 'a', Blocks.obsidian, 'b', new ItemStack(ModItems.slate, 1, 1), 'c', OrbRegistry.getOrbStack(ModItems.orbApprentice)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.ritualController), "aba", "bcb", "aba", 'a', Blocks.obsidian, 'b', ModBlocks.ritualStone, 'c', OrbRegistry.getOrbStack(ModItems.orbMagician)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.ritualController, 1, 1), "aba", "bcb", "aba", 'a', Blocks.obsidian, 'b', "stone", 'c', OrbRegistry.getOrbStack(ModItems.orbWeak)));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.baseItemFilter, 1, 0), "sgs", "gfg", "sgs", 'f', ItemComponent.getStack(ItemComponent.COMPONENT_FRAME_PART), 'g', new ItemStack(Blocks.glass), 's', Items.stick));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.baseItemFilter, 1, 1), "sgs", "gfg", "sgs", 'f', ItemComponent.getStack(ItemComponent.COMPONENT_FRAME_PART), 'g', "dyeYellow", 's', Items.stick));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.baseItemFilter, 1, 2), "sgs", "gfg", "sgs", 'f', ItemComponent.getStack(ItemComponent.COMPONENT_FRAME_PART), 'g', "dyeGreen", 's', Items.stick));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.baseItemFilter, 1, 3), "sgs", "gfg", "sgs", 'f', ItemComponent.getStack(ItemComponent.COMPONENT_FRAME_PART), 'g', "dyePurple", 's', Items.stick));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.upgradeTrainer), "ngn", "ioi", "ngn", 'o', OrbRegistry.getOrbStack(ModItems.orbMaster), 'i', "ingotIron", 'n', "nuggetGold", 'g', "ingotGold"));

        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.incenseAltar), "s s", "shs", "coc", 'o', OrbRegistry.getOrbStack(ModItems.orbWeak), 's', "stone", 'c', "cobblestone", 'h', new ItemStack(Items.coal, 1, 1)));
        GameRegistry.addRecipe(new ShapelessBloodOrbRecipe(new ItemStack(ModBlocks.pathBlock, 4, 0), "plankWood", "plankWood", "plankWood", "plankWood", OrbRegistry.getOrbStack(ModItems.orbApprentice)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.pathBlock, 4, 1), new ItemStack(ModBlocks.pathBlock, 1, 0), new ItemStack(ModBlocks.pathBlock, 1, 0), new ItemStack(ModBlocks.pathBlock, 1, 0), new ItemStack(ModBlocks.pathBlock, 1, 0)));
        GameRegistry.addRecipe(new ShapelessBloodOrbRecipe(new ItemStack(ModBlocks.pathBlock, 4, 2), "stone", "stone", "stone", "stone", OrbRegistry.getOrbStack(ModItems.orbMagician)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.pathBlock, 4, 3), new ItemStack(ModBlocks.pathBlock, 1, 2), new ItemStack(ModBlocks.pathBlock, 1, 2), new ItemStack(ModBlocks.pathBlock, 1, 2), new ItemStack(ModBlocks.pathBlock, 1, 2)));
        GameRegistry.addRecipe(new ShapelessBloodOrbRecipe(new ItemStack(ModBlocks.pathBlock, 4, 4), new ItemStack(ModBlocks.pathBlock, 1, 2), new ItemStack(ModBlocks.pathBlock, 1, 2), new ItemStack(ModBlocks.pathBlock, 1, 2), new ItemStack(ModBlocks.pathBlock, 1, 2), OrbRegistry.getOrbStack(ModItems.orbMaster)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.pathBlock, 4, 5), new ItemStack(ModBlocks.pathBlock, 1, 4), new ItemStack(ModBlocks.pathBlock, 1, 4), new ItemStack(ModBlocks.pathBlock, 1, 4), new ItemStack(ModBlocks.pathBlock, 1, 4)));
        GameRegistry.addRecipe(new ShapelessBloodOrbRecipe(new ItemStack(ModBlocks.pathBlock, 4, 6), Blocks.obsidian, Blocks.obsidian, Blocks.obsidian, Blocks.obsidian, OrbRegistry.getOrbStack(ModItems.orbArchmage)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.pathBlock, 4, 7), new ItemStack(ModBlocks.pathBlock, 1, 6), new ItemStack(ModBlocks.pathBlock, 1, 6), new ItemStack(ModBlocks.pathBlock, 1, 6), new ItemStack(ModBlocks.pathBlock, 1, 6)));

        for (int i = 1; i < BlockBloodRune.names.length; i++)
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.bloodRune), new ItemStack(ModBlocks.bloodRune, 1, i)));
    }

    public static void addAltarRecipes()
    {
        // ONE
        AltarRecipeRegistry.registerFillRecipe(OrbRegistry.getOrbStack(ModItems.orbWeak), EnumAltarTier.ONE, ModItems.orbWeak.getCapacity(), 2, 1);
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.diamond), OrbRegistry.getOrbStack(ModItems.orbWeak), EnumAltarTier.ONE, 2000, 2, 1));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Blocks.stone), new ItemStack(ModItems.slate), EnumAltarTier.ONE, 1000, 5, 5));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.bucket), new ItemStack(ModItems.bucketEssence), EnumAltarTier.ONE, 1000, 5, 0));

        // TWO
        AltarRecipeRegistry.registerFillRecipe(OrbRegistry.getOrbStack(ModItems.orbApprentice), EnumAltarTier.TWO, ModItems.orbApprentice.getCapacity(), 5, 5);
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Blocks.redstone_block), OrbRegistry.getOrbStack(ModItems.orbApprentice), EnumAltarTier.TWO, 5000, 5, 5));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModItems.slate), new ItemStack(ModItems.slate, 1, 1), EnumAltarTier.TWO, 2000, 5, 5));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.iron_sword), new ItemStack(ModItems.daggerOfSacrifice), EnumAltarTier.TWO, 3000, 5, 5));

        // THREE
        AltarRecipeRegistry.registerFillRecipe(OrbRegistry.getOrbStack(ModItems.orbMagician), EnumAltarTier.THREE, ModItems.orbMagician.getCapacity(), 15, 15);
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Blocks.gold_block), OrbRegistry.getOrbStack(ModItems.orbMagician), EnumAltarTier.THREE, 25000, 20, 20));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModItems.slate, 1, 1), new ItemStack(ModItems.slate, 1, 2), EnumAltarTier.THREE, 5000, 15, 10));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Blocks.obsidian), EnumRuneType.EARTH.getScribeStack(), EnumAltarTier.THREE, 1000, 5, 5));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Blocks.lapis_block), EnumRuneType.WATER.getScribeStack(), EnumAltarTier.THREE, 1000, 5, 5));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.magma_cream), EnumRuneType.FIRE.getScribeStack(), EnumAltarTier.THREE, 1000, 5, 5));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.ghast_tear), EnumRuneType.AIR.getScribeStack(), EnumAltarTier.THREE, 1000, 5, 5));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModItems.lavaCrystal), new ItemStack(ModItems.activationCrystal), EnumAltarTier.THREE, 10000, 20, 10));

        // FOUR
        AltarRecipeRegistry.registerFillRecipe(OrbRegistry.getOrbStack(ModItems.orbMaster), EnumAltarTier.FOUR, ModItems.orbMaster.getCapacity(), 25, 25);
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModItems.bloodShard), OrbRegistry.getOrbStack(ModItems.orbMaster), EnumAltarTier.FOUR, 25000, 30, 50));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModItems.slate, 1, 2), new ItemStack(ModItems.slate, 1, 3), EnumAltarTier.FOUR, 15000, 20, 20));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Blocks.coal_block), EnumRuneType.DUSK.getScribeStack(), EnumAltarTier.FOUR, 2000, 20, 10));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.ender_pearl), new ItemStack(ModItems.telepositionFocus), EnumAltarTier.FOUR, 2000, 10, 10));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModItems.telepositionFocus), new ItemStack(ModItems.telepositionFocus, 1, 1), EnumAltarTier.FOUR, 10000, 20, 10));

        // FIVE
        AltarRecipeRegistry.registerFillRecipe(OrbRegistry.getOrbStack(ModItems.orbArchmage), EnumAltarTier.FIVE, ModItems.orbArchmage.getCapacity(), 50, 50);
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.nether_star), OrbRegistry.getOrbStack(ModItems.orbArchmage), EnumAltarTier.FIVE, 80000, 50, 100));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModItems.slate, 1, 3), new ItemStack(ModItems.slate, 1, 4), EnumAltarTier.FIVE, 30000, 40, 100));

        // SIX
        AltarRecipeRegistry.registerFillRecipe(OrbRegistry.getOrbStack(ModItems.orbTranscendent), EnumAltarTier.SIX, ModItems.orbTranscendent.getCapacity(), 50, 50);
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModBlocks.crystal), OrbRegistry.getOrbStack(ModItems.orbTranscendent), EnumAltarTier.SIX, 200000, 100, 200));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Blocks.glowstone), EnumRuneType.DAWN.getScribeStack(), EnumAltarTier.SIX, 200000, 100, 200));
    }

    public static void addAlchemyArrayRecipes()
    {
        AlchemyArrayRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), new ItemStack(Items.diamond_sword), new AlchemyArrayEffectBinding(new ItemStack(ModItems.boundSword)), new BindingAlchemyCircleRenderer());
        AlchemyArrayRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), new ItemStack(Items.diamond_axe), new AlchemyArrayEffectBinding(new ItemStack(ModItems.boundAxe)));
        AlchemyArrayRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), new ItemStack(Items.diamond_pickaxe), new AlchemyArrayEffectBinding(new ItemStack(ModItems.boundPickaxe)));
        AlchemyArrayRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), new ItemStack(Items.diamond_shovel), new AlchemyArrayEffectBinding(new ItemStack(ModItems.boundShovel)));
        AlchemyArrayRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), new ItemStack(Items.iron_helmet), new AlchemyArrayEffectBinding(new ItemStack(ModItems.livingArmourHelmet)));
        AlchemyArrayRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), new ItemStack(Items.iron_chestplate), new AlchemyArrayEffectBinding(new ItemStack(ModItems.livingArmourChest)));
        AlchemyArrayRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), new ItemStack(Items.iron_leggings), new AlchemyArrayEffectBinding(new ItemStack(ModItems.livingArmourLegs)));
        AlchemyArrayRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), new ItemStack(Items.iron_boots), new AlchemyArrayEffectBinding(new ItemStack(ModItems.livingArmourBoots)));

        AlchemyArrayRecipeRegistry.registerCraftingRecipe(new ItemStack(Items.redstone), new ItemStack(ModItems.slate), new ItemStack(ModItems.sigilDivination), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/DivinationSigil.png"));

        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_WATER), new ItemStack(ModItems.slate), new ItemStack(ModItems.sigilWater), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/WaterSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_LAVA), new ItemStack(ModItems.slate), new ItemStack(ModItems.sigilLava), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/LavaSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_AIR), new ItemStack(ModItems.slate, 1, 1), new ItemStack(ModItems.sigilAir), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/AirSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_FASTMINER), new ItemStack(ModItems.slate, 1, 1), new ItemStack(ModItems.sigilFastMiner), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/FastMinerSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_VOID), new ItemStack(ModItems.slate, 1, 1), new ItemStack(ModItems.sigilVoid), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/VoidSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_GROWTH), new ItemStack(ModItems.slate, 1, 1), new ItemStack(ModItems.sigilGreenGrove), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/GrowthSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_AFFINITY), new ItemStack(ModItems.slate, 1, 2), new ItemStack(ModItems.sigilElementalAffinity), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/ElementalAffinitySigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_SIGHT), new ItemStack(ModItems.slate, 1, 1), new ItemStack(ModItems.sigilSeer), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/SightSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_SUPPRESSION), new ItemStack(ModItems.slate, 1, 3), new ItemStack(ModItems.sigilSuppression), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/SuppressionSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BLOODLIGHT), new ItemStack(ModItems.slate, 1, 2), new ItemStack(ModItems.sigilBloodLight), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/LightSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_MAGNETISM), new ItemStack(ModItems.slate, 1, 2), new ItemStack(ModItems.sigilMagnetism), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/MagnetismSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_HASTE), new ItemStack(ModItems.slate, 1, 3), new ItemStack(ModItems.sigilHaste), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/WIPArray.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BRIDGE), new ItemStack(ModItems.slate, 1, 3), new ItemStack(ModItems.sigilPhantomBridge), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/WIPArray.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_COMPRESSION), new ItemStack(ModItems.slate, 1, 3), new ItemStack(ModItems.sigilCompression), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/WIPArray.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_SEVERANCE), new ItemStack(ModItems.slate, 1, 3), new ItemStack(ModItems.sigilEnderSeverance), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/WIPArray.png"));

        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_TELEPOSITION), new ItemStack(ModItems.slate, 1, 3), new ItemStack(ModItems.sigilTeleposition), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/WIPArray.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_TRANSPOSITION), new ItemStack(ModItems.slate, 1, 3), new ItemStack(ModItems.sigilTransposition), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/WIPArray.png"));

    }

    public static void addCompressionHandlers()
    {
        StorageBlockCraftingManager.getInstance().addStorageBlockRecipes();
        CompressionRegistry.registerHandler(new BaseCompressionHandler(new ItemStack(Items.glowstone_dust, 4, 0), new ItemStack(Blocks.glowstone), 64));
        CompressionRegistry.registerHandler(new BaseCompressionHandler(new ItemStack(Items.snowball, 4, 0), new ItemStack(Blocks.snow), 8));
        CompressionRegistry.registerHandler(new AdvancedCompressionHandler());

        CompressionRegistry.registerItemThreshold(new ItemStack(Blocks.cobblestone), 64);
    }

    public static void addSoulForgeRecipes()
    {
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.soulGem), 1, 1, "dustRedstone", "ingotGold", "blockGlass", "dyeBlue");
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.soulGem, 1, 1), 60, 20, new ItemStack(ModItems.soulGem), "gemDiamond", "blockRedstone", "blockLapis");
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.soulGem, 1, 2), 240, 50, new ItemStack(ModItems.soulGem, 1, 1), "gemDiamond", "blockGold", new ItemStack(ModItems.slate, 1, 2));
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.soulGem, 1, 3), 1000, 100, new ItemStack(ModItems.soulGem, 1, 2), new ItemStack(ModItems.slate, 1, 3), new ItemStack(ModItems.bloodShard), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DEFAULT));
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.soulGem, 1, 4), 4000, 500, new ItemStack(ModItems.soulGem, 1, 3), Items.nether_star);
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.sentientSword), 0, 0, new ItemStack(ModItems.soulGem), new ItemStack(Items.iron_sword));
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.sentientBow), 70, 0, new ItemStack(Items.bow), new ItemStack(ModItems.soulGem, 1, 1), Items.string, Items.string);
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.arcaneAshes), 0, 0, "dustRedstone", "dyeWhite", new ItemStack(Items.gunpowder), Items.coal);
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_WATER), 10, 3, new ItemStack(Items.sugar), new ItemStack(Items.water_bucket), new ItemStack(Items.water_bucket));
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_LAVA), 32, 10, Items.lava_bucket, "dustRedstone", "cobblestone", "blockCoal");
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_VOID), 64, 10, Items.bucket, Items.string, Items.string, Items.gunpowder);
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_GROWTH), 128, 20, "treeSapling", "treeSapling", Items.reeds, Items.sugar);
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_AIR), 128, 20, Items.ghast_tear, Items.feather, Items.feather);
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_SIGHT), 64, 0, ModItems.sigilDivination, "blockGlass", "blockGlass", "dustGlowstone");
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_FASTMINER), 128, 10, Items.iron_pickaxe, Items.iron_axe, Items.iron_shovel, Items.gunpowder);
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_AFFINITY), 300, 30, ModItems.sigilWater, ModItems.sigilAir, ModItems.sigilLava, Blocks.obsidian);
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_SUPPRESSION), 500, 50, ModBlocks.teleposer, Items.water_bucket, Items.lava_bucket, Items.blaze_rod);
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), 400, 10, "dustGlowstone", "dustRedstone", "nuggetGold", Items.gunpowder);
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BLOODLIGHT), 300, 10, "glowstone", Blocks.torch, "dustRedstone", "dustRedstone");
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_MAGNETISM), 600, 10, Items.string, "ingotGold", "blockIron", "ingotGold");
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_HASTE), 1400, 100, Items.cookie, Items.sugar, Items.cookie, "stone");
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BRIDGE), 600, 50, Blocks.soul_sand, Blocks.soul_sand, "stone", Blocks.obsidian);
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_SEVERANCE), 800, 70, Items.ender_eye, Items.ender_pearl, "ingotGold", "ingotGold");
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_COMPRESSION), 2000, 200, "blockIron", "blockGold", Blocks.obsidian, "cobblestone");
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_TELEPOSITION), 1500, 200, ModBlocks.teleposer, "glowstone", "blockRedstone", "ingotGold");
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_TRANSPOSITION), 1500, 200, ModBlocks.teleposer, "gemDiamond", Items.ender_pearl, Blocks.obsidian);

        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.sentientArmourGem), 240, 150, Items.diamond_chestplate, new ItemStack(ModItems.soulGem, 1, 1), Blocks.iron_block, Blocks.obsidian);

        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.COMPONENT_FRAME_PART), 400, 10, "blockGlass", "stone", new ItemStack(ModItems.slate));
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.nodeRouter), 400, 5, Items.stick, new ItemStack(ModItems.slate, 1, 1), "gemLapis", "gemLapis");
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModBlocks.itemRoutingNode), 400, 5, "dustGlowstone", "dustRedstone", "blockGlass", "stone");
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModBlocks.outputRoutingNode), 400, 25, "dustGlowstone", "dustRedstone", "ingotIron", new ItemStack(ModBlocks.itemRoutingNode));
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModBlocks.inputRoutingNode), 400, 25, "dustGlowstone", "dustRedstone", "ingotGold", new ItemStack(ModBlocks.itemRoutingNode));
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModBlocks.masterRoutingNode), 400, 200, "blockIron", "gemDiamond", new ItemStack(ModItems.slate, 1, 2));

        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModBlocks.demonCrystal, 1, 0), 1200, 100, ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DEFAULT), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DEFAULT), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DEFAULT), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DEFAULT));
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModBlocks.demonCrystal, 1, 1), 1200, 100, ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_CORROSIVE), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_CORROSIVE), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_CORROSIVE), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_CORROSIVE));
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModBlocks.demonCrystal, 1, 2), 1200, 100, ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DESTRUCTIVE), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DESTRUCTIVE), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DESTRUCTIVE), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DESTRUCTIVE));
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModBlocks.demonCrystal, 1, 4), 1200, 100, ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_STEADFAST), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_STEADFAST), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_STEADFAST), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_STEADFAST));

        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModBlocks.demonCrucible), 400, 100, Items.cauldron, "stone", "gemLapis", "gemDiamond");
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModBlocks.demonPylon), 400, 50, "blockIron", "stone", "gemLapis", ModItems.itemDemonCrystal);
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModBlocks.demonCrystallizer), 500, 100, ModBlocks.soulForge, "stone", "gemLapis", "blockGlass");
    }
}
