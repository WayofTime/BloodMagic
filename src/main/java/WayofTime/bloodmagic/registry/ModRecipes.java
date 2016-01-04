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
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.client.render.alchemyArray.BindingAlchemyCircleRenderer;
import WayofTime.bloodmagic.compress.AdvancedCompressionHandler;
import WayofTime.bloodmagic.compress.BaseCompressionHandler;
import WayofTime.bloodmagic.compress.StorageBlockCraftingManager;
import WayofTime.bloodmagic.item.ItemComponent;

public class ModRecipes
{
    public static void init()
    {
        RecipeSorter.register(Constants.Mod.DOMAIN + "shapedorb", ShapedBloodOrbRecipe.class, RecipeSorter.Category.SHAPED, "before:minecraft:shapeless");
        RecipeSorter.register(Constants.Mod.DOMAIN + "shapelessorb", ShapelessBloodOrbRecipe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");

        addCraftingRecipes();
        addAltarRecipes();
        addAlchemyArrayRecipes();
    }

    public static void addCraftingRecipes()
    {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.sacrificialDagger), "aaa", " ba", "c a", 'a', "blockGlass", 'b', "ingotGold", 'c', "ingotIron"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.altar), "a a", "aba", "cdc", 'a', "stone", 'b', Blocks.furnace, 'c', "ingotGold", 'd', "gemDiamond"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.packSelfSacrifice), "aba", "cdc", "aea", 'a', "blockGlass", 'b', Items.bucket, 'c', Items.flint, 'd', Items.leather_chestplate, 'e', ModItems.slate));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.packSacrifice), "aba", "cdc", "aea", 'a', "blockGlass", 'b', Items.bucket, 'c', "ingotIron", 'd', Items.leather_chestplate, 'e', ModItems.slate));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.ritualDiviner), "dfd", "ase", "dwd", 'f', EnumRuneType.FIRE.getScribeStack(), 'a', EnumRuneType.AIR.getScribeStack(), 'w', EnumRuneType.WATER.getScribeStack(), 'e', EnumRuneType.EARTH.getScribeStack(), 'd', "gemDiamond", 's', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.ritualDiviner, 1, 1), " S ", "tdt", " S ", 'S', new ItemStack(ModItems.slate, 1, 3), 't', EnumRuneType.DUSK.getScribeStack(), 'd', new ItemStack(ModItems.ritualDiviner)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.bloodStoneBrick), "aa", "aa", 'a', new ItemStack(ModBlocks.bloodStoneBrick, 1, 1)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.bloodStoneBrick, 1, 1), "stone", new ItemStack(ModItems.bloodShard)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.arcaneAshes), "dyeWhite", Items.gunpowder, Items.gunpowder, "dustRedstone", new ItemStack(Items.flint), new ItemStack(Items.coal, 1, 1), ModItems.slate));

        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.telepositionFocus, 1, 2), new ItemStack(ModItems.telepositionFocus, 1, 1), new ItemStack(ModItems.bloodShard));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.telepositionFocus, 1, 3), new ItemStack(ModItems.telepositionFocus, 1, 2), new ItemStack(ModItems.bloodShard, 1, 1));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.teleposer), "ggg", "efe", "ggg", 'g', "ingotGold", 'e', new ItemStack(Items.ender_pearl), 'f', ModItems.telepositionFocus));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.lavaCrystal), "aba", "bcb", "ded", 'a', "blockGlass", 'b', Items.lava_bucket, 'c', OrbRegistry.getOrbStack(ModItems.orbWeak), 'd', Blocks.obsidian, 'e', "gemDiamond"));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), "xox", "oSo", "xox", 'S', OrbRegistry.getOrbStack(ModItems.orbMagician), 'o', "dustRedstone", 'x', "dustGlowstone"));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemComponent.getStack(ItemComponent.REAGENT_WATER), "aaa", "aba", "aca", 'a', Items.water_bucket, 'b', Items.sugar, 'c', OrbRegistry.getOrbStack(ModItems.orbWeak)));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemComponent.getStack(ItemComponent.REAGENT_LAVA), "aba", "aca", "ada", 'a', Items.lava_bucket, 'b', Items.blaze_powder, 'c', "dustRedstone", 'd', ModItems.lavaCrystal));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemComponent.getStack(ItemComponent.REAGENT_VOID), "aba", "aca", "ada", 'a', Items.bucket, 'b', Items.string, 'c', Items.gunpowder, 'd', OrbRegistry.getOrbStack(ModItems.orbApprentice)));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemComponent.getStack(ItemComponent.REAGENT_GROWTH), "aba", "bcb", "ada", 'a', "treeSapling", 'b', Items.reeds, 'c', Items.sugar, 'd', OrbRegistry.getOrbStack(ModItems.orbApprentice)));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemComponent.getStack(ItemComponent.REAGENT_FASTMINER), "aba", "cde", "afa", 'a', "stone", 'b', Items.iron_pickaxe, 'c', Items.iron_shovel, 'd', Items.gunpowder, 'e', Items.iron_axe, 'f', OrbRegistry.getOrbStack(ModItems.orbApprentice)));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemComponent.getStack(ItemComponent.REAGENT_AFFINITY), "aba", "cde", "afa", 'a', Blocks.obsidian, 'b', ModItems.sigilAir, 'c', ModItems.sigilWater, 'd', "dustGlowstone", 'e', ModItems.sigilLava, 'f', OrbRegistry.getOrbStack(ModItems.orbMagician)));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemComponent.getStack(ItemComponent.REAGENT_SIGHT), "aba", "aca", "ded", 'a', "blockGlass", 'b', ModItems.sigilDivination, 'c', "dustGlowstone", 'd', ModItems.bucketEssence, 'e', OrbRegistry.getOrbStack(ModItems.orbApprentice)));
        GameRegistry.addRecipe(new ShapedOreRecipe(ItemComponent.getStack(ItemComponent.REAGENT_SUPPRESSION), "wtl", "wrl", "wol", 't', new ItemStack(ModBlocks.teleposer), 'w', new ItemStack(Items.water_bucket), 'l', new ItemStack(Items.lava_bucket), 'o', OrbRegistry.getOrbStack(ModItems.orbMaster)));
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
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.ritualStone), "aba", "bcb", "aba", 'a', Blocks.obsidian, 'b', new ItemStack(ModItems.slate, 1, 1), 'c', OrbRegistry.getOrbStack(ModItems.orbApprentice)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.ritualController), "aba", "bcb", "aba", 'a', Blocks.obsidian, 'b', ModBlocks.ritualStone, 'c', OrbRegistry.getOrbStack(ModItems.orbMagician)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.ritualController, 1, 1), "aba", "bcb", "aba", 'a', Blocks.obsidian, 'b', "stone", 'c', OrbRegistry.getOrbStack(ModItems.orbWeak)));
    }

    public static void addAltarRecipes()
    {
        // ONE
        AltarRecipeRegistry.registerFillRecipe(OrbRegistry.getOrbStack(ModItems.orbWeak), EnumAltarTier.ONE, ModItems.orbWeak.getCapacity(), 2, 1);
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.diamond), OrbRegistry.getOrbStack(ModItems.orbWeak), EnumAltarTier.ONE, 2000, 2, 1));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Blocks.stone), new ItemStack(ModItems.slate), EnumAltarTier.ONE, 1000, 5, 5));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModItems.bucketEssence), new ItemStack(Items.bucket), EnumAltarTier.ONE, 1000, 5, 0));

        // TWO
        AltarRecipeRegistry.registerFillRecipe(OrbRegistry.getOrbStack(ModItems.orbApprentice), EnumAltarTier.TWO, ModItems.orbApprentice.getCapacity(), 5, 5);
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.emerald), OrbRegistry.getOrbStack(ModItems.orbApprentice), EnumAltarTier.TWO, 5000, 5, 5));
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
    }

    public static void addCompressionHandlers()
    {
        StorageBlockCraftingManager.getInstance().addStorageBlockRecipes();
        CompressionRegistry.registerHandler(new BaseCompressionHandler(new ItemStack(Items.glowstone_dust, 4, 0), new ItemStack(Blocks.glowstone), 64));
        CompressionRegistry.registerHandler(new BaseCompressionHandler(new ItemStack(Items.snowball, 4, 0), new ItemStack(Blocks.snow), 8));
        CompressionRegistry.registerHandler(new AdvancedCompressionHandler());

        CompressionRegistry.registerItemThreshold(new ItemStack(Blocks.cobblestone), 64);
    }
}
