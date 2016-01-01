package WayofTime.bloodmagic.registry;

import WayofTime.bloodmagic.api.Constants;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectBinding;
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
import net.minecraftforge.oredict.RecipeSorter;

public class ModRecipes
{
    public static void init()
    {
        RecipeSorter.register(Constants.Mod.DOMAIN + "shapedorb", ShapedBloodOrbRecipe.class, RecipeSorter.Category.SHAPED, "before:minecraft:shapeless");
        RecipeSorter.register(Constants.Mod.DOMAIN + ":shapelessorb", ShapelessBloodOrbRecipe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");

        addCraftingRecipes();
        addAltarRecipes();
        addAlchemyArrayRecipes();
    }

    public static void addCraftingRecipes()
    {
        GameRegistry.addRecipe(new ItemStack(ModItems.sacrificialDagger), "aaa", " ba", "c a", 'a', Blocks.glass, 'b', Items.gold_ingot, 'c', Items.iron_ingot);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.altar), "a a", "aba", "cdc", 'a', Blocks.stone, 'b', Blocks.furnace, 'c', Items.gold_ingot, 'd', Items.diamond);
        GameRegistry.addRecipe(new ItemStack(ModItems.packSelfSacrifice), "aba", "cdc", "aea", 'a', Blocks.glass, 'b', Items.bucket, 'c', Items.flint, 'd', Items.leather_chestplate, 'e', ModItems.slate);
        GameRegistry.addRecipe(new ItemStack(ModItems.packSacrifice), "aba", "cdc", "aea", 'a', Blocks.glass, 'b', Items.bucket, 'c', Items.iron_ingot, 'd', Items.leather_chestplate, 'e', ModItems.slate);
        GameRegistry.addRecipe(new ItemStack(ModItems.ritualDiviner), "dfd", "ase", "dwd", 'f', EnumRuneType.FIRE.getScribeStack(), 'a', EnumRuneType.AIR.getScribeStack(), 'w', EnumRuneType.WATER.getScribeStack(), 'e', EnumRuneType.EARTH.getScribeStack(), 'd', new ItemStack(Items.diamond), 's', new ItemStack(Items.stick));
        GameRegistry.addRecipe(new ItemStack(ModItems.ritualDiviner, 1, 1), " S ", "tdt", " S ", 'S', new ItemStack(ModItems.slate, 1, 3), 't', EnumRuneType.DUSK.getScribeStack(), 'd', new ItemStack(ModItems.ritualDiviner));
        GameRegistry.addRecipe(new ItemStack(ModBlocks.bloodStoneBrick), "aa", "aa", 'a', new ItemStack(ModBlocks.bloodStoneBrick, 1, 1));
        GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.bloodStoneBrick, 1, 1), Blocks.stone, ModItems.bloodShard);
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.arcaneAshes), new ItemStack(Items.dye, 1, 15), new ItemStack(Items.gunpowder), new ItemStack(Items.gunpowder), new ItemStack(Items.redstone), new ItemStack(Items.flint), new ItemStack(ModItems.slate));

        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModItems.lavaCrystal), "aba", "bcb", "ded", 'a', Blocks.glass, 'b', Items.lava_bucket, 'c', OrbRegistry.getOrbStack(ModItems.orbWeak), 'd', Blocks.obsidian, 'e', Items.diamond));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), "xox", "oSo", "xox", 'S', OrbRegistry.getOrbStack(ModItems.orbMagician), 'o', new ItemStack(Items.redstone), 'x', new ItemStack(Items.glowstone_dust)));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(ItemComponent.getStack(ItemComponent.REAGENT_WATER), "aaa", "aba", "aca", 'a', Items.water_bucket, 'b', Items.sugar, 'c', OrbRegistry.getOrbStack(ModItems.orbWeak)));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(ItemComponent.getStack(ItemComponent.REAGENT_LAVA), "aba", "aca", "ada", 'a', Items.lava_bucket, 'b', Items.blaze_powder, 'c', Items.redstone, 'd', ModItems.lavaCrystal));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(ItemComponent.getStack(ItemComponent.REAGENT_VOID), "aba", "aca", "ada", 'a', Items.bucket, 'b', Items.string, 'c', Items.gunpowder, 'd', OrbRegistry.getOrbStack(ModItems.orbApprentice)));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(ItemComponent.getStack(ItemComponent.REAGENT_GROWTH), "aba", "bcb", "ada", 'a', Blocks.sapling, 'b', Items.reeds, 'c', Items.sugar, 'd', OrbRegistry.getOrbStack(ModItems.orbApprentice)));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(ItemComponent.getStack(ItemComponent.REAGENT_FASTMINER), "aba", "cde", "afa", 'a', Blocks.stone, 'b', Items.iron_pickaxe, 'c', Items.iron_shovel, 'd', Items.gunpowder, 'e', Items.iron_axe, 'f', OrbRegistry.getOrbStack(ModItems.orbApprentice)));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(ItemComponent.getStack(ItemComponent.REAGENT_AFFINITY), "aba", "cde", "afa", 'a', Blocks.obsidian, 'b', ModItems.sigilAir, 'c', ModItems.sigilWater, 'd', Items.glowstone_dust, 'e', ModItems.sigilLava, 'f', OrbRegistry.getOrbStack(ModItems.orbMagician)));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(ItemComponent.getStack(ItemComponent.REAGENT_SIGHT), "aba", "aca", "ded", 'a', Blocks.glass, 'b', ModItems.sigilDivination, 'c', Items.glowstone_dust, 'd', ModItems.bucketEssence, 'e', OrbRegistry.getOrbStack(ModItems.orbApprentice)));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.bloodRune), "aaa", "bcb", "aaa", 'a', Blocks.stone, 'b', ModItems.slate, 'c', OrbRegistry.getOrbStack(ModItems.orbWeak)));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.bloodRune, 1, 1), "aba", "cdc", "aba", 'a', Blocks.stone, 'b', ModItems.slate, 'c', Items.sugar, 'd', ModBlocks.bloodRune)); //Speed
//        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.bloodRune, 1, 2), "aba", "cdc", "aba", 'a', Blocks.stone, 'b', ModItems.slate, 'c', Items.sugar, 'd', ModBlocks.bloodRune)); //Efficiency
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.bloodRune, 1, 3), "aba", "cdc", "aea", 'a', Blocks.stone, 'b', new ItemStack(ModItems.slate, 1, 1), 'c', Items.gold_ingot, 'd', ModBlocks.bloodRune, 'e', OrbRegistry.getOrbStack(ModItems.orbApprentice))); //Sacrifice
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.bloodRune, 1, 4), "aba", "cdc", "aea", 'a', Blocks.stone, 'b', new ItemStack(ModItems.slate, 1, 1), 'c', Items.glowstone_dust, 'd', ModBlocks.bloodRune, 'e', OrbRegistry.getOrbStack(ModItems.orbApprentice))); //Self-Sacrifice
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.bloodRune, 1, 5), "aba", "bcb", "ada", 'a', Blocks.stone, 'b', Items.water_bucket, 'c', ModBlocks.bloodRune, 'd', new ItemStack(ModItems.slate, 1, 2))); //Displacement
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.bloodRune, 1, 6), "aba", "bcb", "ada", 'a', Blocks.stone, 'b', Items.bucket, 'c', ModBlocks.bloodRune, 'd', new ItemStack(ModItems.slate, 1, 2))); //Capacity
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.bloodRune, 1, 7), "aba", "cdc", "aea", 'a', Blocks.obsidian, 'b', new ItemStack(ModItems.slate, 1, 3), 'c', Items.bucket, 'd', new ItemStack(ModBlocks.bloodRune, 1, 6), 'e', OrbRegistry.getOrbStack(ModItems.orbMaster))); //Augmented Capacity
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.bloodRune, 1, 8), "aba", "cdc", "aba", 'a', Blocks.stone, 'b', Items.diamond, 'c', ModBlocks.bloodRune, 'd', OrbRegistry.getOrbStack(ModItems.orbMaster))); //Orb
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.bloodRune, 1, 9), "aba", "cdc", "aea", 'a', Items.bucket, 'b', new ItemStack(ModItems.slate, 1, 4), 'c', Items.gold_ingot, 'd', new ItemStack(ModBlocks.bloodRune, 1, 1), 'e', OrbRegistry.getOrbStack(ModItems.orbArchmage))); //Acceleration
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.ritualStone), "aba", "bcb", "aba", 'a', Blocks.obsidian, 'b', new ItemStack(ModItems.slate, 1, 1), 'c', OrbRegistry.getOrbStack(ModItems.orbApprentice)));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.ritualController), "aba", "bcb", "aba", 'a', Blocks.obsidian, 'b', ModBlocks.ritualStone, 'c', OrbRegistry.getOrbStack(ModItems.orbMagician)));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.ritualController, 1, 1), "aba", "bcb", "aba", 'a', Blocks.obsidian, 'b', Blocks.stone, 'c', OrbRegistry.getOrbStack(ModItems.orbWeak)));
    }

    public static void addAltarRecipes()
    {
        // ONE
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(OrbRegistry.getOrbStack(ModItems.orbWeak), OrbRegistry.getOrbStack(ModItems.orbWeak), EnumAltarTier.ONE, 5000, 2, 1, true));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.diamond), OrbRegistry.getOrbStack(ModItems.orbWeak), EnumAltarTier.ONE, 2000, 2, 1));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Blocks.stone), new ItemStack(ModItems.slate), EnumAltarTier.ONE, 1000, 5, 5));

        // TWO
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.emerald), OrbRegistry.getOrbStack(ModItems.orbApprentice), EnumAltarTier.TWO, 5000, 2, 1));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModItems.slate), new ItemStack(ModItems.slate, 1, 1), EnumAltarTier.TWO, 2000, 5, 5));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.iron_sword), new ItemStack(ModItems.daggerOfSacrifice), EnumAltarTier.TWO, 3000, 5, 5));

        // THREE
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Blocks.gold_block), OrbRegistry.getOrbStack(ModItems.orbMagician), EnumAltarTier.THREE, 25000, 2, 1));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModItems.slate, 1, 1), new ItemStack(ModItems.slate, 1, 2), EnumAltarTier.THREE, 5000, 15, 10));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Blocks.obsidian), EnumRuneType.EARTH.getScribeStack(), EnumAltarTier.THREE, 1000, 5, 5));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Blocks.lapis_block), EnumRuneType.WATER.getScribeStack(), EnumAltarTier.THREE, 1000, 5, 5));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.magma_cream), EnumRuneType.FIRE.getScribeStack(), EnumAltarTier.THREE, 1000, 5, 5));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.ghast_tear), EnumRuneType.AIR.getScribeStack(), EnumAltarTier.THREE, 1000, 5, 5));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModItems.lavaCrystal), new ItemStack(ModItems.activationCrystal), EnumAltarTier.THREE, 10000, 20, 10));

        // FOUR
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModItems.slate, 1, 2), new ItemStack(ModItems.slate, 1, 3), EnumAltarTier.FOUR, 15000, 20, 20));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Blocks.coal_block), EnumRuneType.DUSK.getScribeStack(), EnumAltarTier.FOUR, 2000, 20, 10));

        // FIVE
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModItems.slate, 1, 3), new ItemStack(ModItems.slate, 1, 4), EnumAltarTier.FIVE, 30000, 40, 100));

        // SIX
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModBlocks.crystal), OrbRegistry.getOrbStack(ModItems.orbTranscendent), EnumAltarTier.SIX, 200000, 100, 200));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Blocks.glowstone), EnumRuneType.DAWN.getScribeStack(), EnumAltarTier.SIX, 200000, 100, 200));
    }

    public static void addAlchemyArrayRecipes()
    {
        AlchemyArrayRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), new ItemStack(Items.diamond_sword), new AlchemyArrayEffectBinding(new ItemStack(ModItems.boundSword)), new BindingAlchemyCircleRenderer());
        AlchemyArrayRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), new ItemStack(Items.diamond_axe), new AlchemyArrayEffectBinding(new ItemStack(ModItems.boundAxe)));
        AlchemyArrayRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), new ItemStack(Items.diamond_pickaxe), new AlchemyArrayEffectBinding(new ItemStack(ModItems.boundPickaxe)));
        AlchemyArrayRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), new ItemStack(Items.diamond_shovel), new AlchemyArrayEffectBinding(new ItemStack(ModItems.boundShovel)));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_WATER), new ItemStack(ModItems.slate), new ItemStack(ModItems.sigilWater), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/WaterSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_LAVA), new ItemStack(ModItems.slate), new ItemStack(ModItems.sigilLava), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/LavaSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_AIR), new ItemStack(ModItems.slate, 1, 1), new ItemStack(ModItems.sigilAir), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/AirSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_FASTMINER), new ItemStack(ModItems.slate, 1, 1), new ItemStack(ModItems.sigilFastMiner), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/FastMinerSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_VOID), new ItemStack(ModItems.slate, 1, 1), new ItemStack(ModItems.sigilVoid), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/VoidSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_GROWTH), new ItemStack(ModItems.slate, 1, 1), new ItemStack(ModItems.sigilGreenGrove), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/GrowthSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_AFFINITY), new ItemStack(ModItems.slate, 1, 2), new ItemStack(ModItems.sigilElementalAffinity), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/ElementalAffinitySigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_SIGHT), new ItemStack(ModItems.slate, 1, 1), new ItemStack(ModItems.sigilSeer), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/SightSigil.png"));
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
