package WayofTime.bloodmagic.registry;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectAttractor;
import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectBinding;
import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectMovement;
import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectSkeletonTurret;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.altar.EnumAltarTier;
import WayofTime.bloodmagic.api.compress.CompressionRegistry;
import WayofTime.bloodmagic.api.recipe.AlchemyTableCustomRecipe;
import WayofTime.bloodmagic.api.recipe.ShapedBloodOrbRecipe;
import WayofTime.bloodmagic.api.recipe.ShapelessBloodOrbRecipe;
import WayofTime.bloodmagic.api.registry.AlchemyArrayRecipeRegistry;
import WayofTime.bloodmagic.api.registry.AlchemyTableRecipeRegistry;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry;
import WayofTime.bloodmagic.api.registry.OrbRegistry;
import WayofTime.bloodmagic.api.registry.TartaricForgeRecipeRegistry;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.block.BlockBloodRune;
import WayofTime.bloodmagic.client.render.alchemyArray.AttractorAlchemyCircleRenderer;
import WayofTime.bloodmagic.client.render.alchemyArray.BindingAlchemyCircleRenderer;
import WayofTime.bloodmagic.client.render.alchemyArray.DualAlchemyCircleRenderer;
import WayofTime.bloodmagic.client.render.alchemyArray.StaticAlchemyCircleRenderer;
import WayofTime.bloodmagic.compress.AdvancedCompressionHandler;
import WayofTime.bloodmagic.compress.BaseCompressionHandler;
import WayofTime.bloodmagic.compress.StorageBlockCraftingManager;
import WayofTime.bloodmagic.item.ItemComponent;
import WayofTime.bloodmagic.item.ItemDemonCrystal;
import WayofTime.bloodmagic.item.alchemy.ItemCuttingFluid;
import WayofTime.bloodmagic.item.alchemy.ItemLivingArmourPointsUpgrade;
import WayofTime.bloodmagic.item.soul.ItemSoulGem;
import WayofTime.bloodmagic.recipe.alchemyTable.AlchemyTableDyeableRecipe;
import WayofTime.bloodmagic.util.Utils;

import com.google.common.base.Stopwatch;

public class ModRecipes
{
    public static ArrayList<String> addedOreRecipeList = new ArrayList<String>();

    public static void init()
    {
        RecipeSorter.register(Constants.Mod.DOMAIN + "shapedorb", ShapedBloodOrbRecipe.class, RecipeSorter.Category.SHAPED, "before:minecraft:shapeless");
        RecipeSorter.register(Constants.Mod.DOMAIN + "shapelessorb", ShapelessBloodOrbRecipe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");

        initOreDict();
        addFurnaceRecipes();
        addCraftingRecipes();
        addAltarRecipes();
        addAlchemyArrayRecipes();
        addSoulForgeRecipes();
        addAlchemyTableRecipes();
        addOreDoublingAlchemyRecipes();
    }

    public static void initOreDict()
    {
        OreDictionary.registerOre("dustIron", ItemComponent.getStack(ItemComponent.SAND_IRON));
        OreDictionary.registerOre("dustGold", ItemComponent.getStack(ItemComponent.SAND_GOLD));
        OreDictionary.registerOre("dustCoal", ItemComponent.getStack(ItemComponent.SAND_COAL));
    }

    public static void addFurnaceRecipes()
    {
        FurnaceRecipes.instance().addSmeltingRecipe(ItemComponent.getStack(ItemComponent.SAND_IRON), new ItemStack(Items.IRON_INGOT), (float) 0.15);
        FurnaceRecipes.instance().addSmeltingRecipe(ItemComponent.getStack(ItemComponent.SAND_GOLD), new ItemStack(Items.GOLD_INGOT), (float) 0.15);
    }

    public static void addCraftingRecipes()
    {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.soulForge), "i i", "sgs", "sos", 'i', "ingotIron", 's', "stone", 'g', "ingotGold", 'o', "blockIron"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.sacrificialDagger), "aaa", " ba", "c a", 'a', "blockGlass", 'b', "ingotGold", 'c', "ingotIron"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.altar), "a a", "aba", "cdc", 'a', "stone", 'b', Blocks.FURNACE, 'c', "ingotGold", 'd', new ItemStack(ModItems.monsterSoul)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.packSelfSacrifice), "aba", "cdc", "aea", 'a', "blockGlass", 'b', Items.BUCKET, 'c', Items.FLINT, 'd', Items.LEATHER_CHESTPLATE, 'e', ModItems.slate));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.packSacrifice), "aba", "cdc", "aea", 'a', "blockGlass", 'b', Items.BUCKET, 'c', "ingotIron", 'd', Items.LEATHER_CHESTPLATE, 'e', ModItems.slate));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.ritualDiviner), "dfd", "ase", "dwd", 'f', EnumRuneType.FIRE.getScribeStack(), 'a', EnumRuneType.AIR.getScribeStack(), 'w', EnumRuneType.WATER.getScribeStack(), 'e', EnumRuneType.EARTH.getScribeStack(), 'd', "gemDiamond", 's', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.ritualDiviner, 1, 1), " S ", "tdt", " S ", 'S', new ItemStack(ModItems.slate, 1, 3), 't', EnumRuneType.DUSK.getScribeStack(), 'd', new ItemStack(ModItems.ritualDiviner)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.bloodStoneBrick, 1, 1), "aa", "aa", 'a', new ItemStack(ModBlocks.bloodStoneBrick)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.bloodStoneBrick, 16), "stone", new ItemStack(ModItems.bloodShard)));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.activationCrystal, 1, 1), new ItemStack(Items.NETHER_STAR), OrbRegistry.getOrbStack(ModItems.orbArchmage));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.soulSnare, 4, 0), "sis", "iri", "sis", 's', new ItemStack(Items.STRING), 'i', "ingotIron", 'r', "dustRedstone"));

        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.telepositionFocus, 1, 2), new ItemStack(ModItems.telepositionFocus, 1, 1), new ItemStack(ModItems.bloodShard));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.telepositionFocus, 1, 3), new ItemStack(ModItems.telepositionFocus, 1, 2), new ItemStack(ModItems.bloodShard, 1, 1));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.teleposer), "ggg", "efe", "ggg", 'g', "ingotGold", 'e', new ItemStack(Items.ENDER_PEARL), 'f', ModItems.telepositionFocus));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.lavaCrystal), "aba", "bcb", "ded", 'a', "blockGlass", 'b', Items.LAVA_BUCKET, 'c', OrbRegistry.getOrbStack(ModItems.orbWeak), 'd', Blocks.OBSIDIAN, 'e', "gemDiamond"));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.bloodRune), "aaa", "bcb", "aaa", 'a', "stone", 'b', ModItems.slate, 'c', OrbRegistry.getOrbStack(ModItems.orbWeak)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.bloodRune, 1, 1), "aba", "cdc", "aba", 'a', "stone", 'b', ModItems.slate, 'c', Items.SUGAR, 'd', new ItemStack(ModBlocks.bloodRune))); //Speed
//        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.bloodRune, 1, 2), "aba", "cdc", "aba", 'a', "stone", 'b', ModItems.slate, 'c', Items.sugar, 'd', new ItemStack(ModBlocks.bloodRune))); //Efficiency
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.bloodRune, 1, 3), "aba", "cdc", "aea", 'a', "stone", 'b', new ItemStack(ModItems.slate, 1, 1), 'c', "ingotGold", 'd', new ItemStack(ModBlocks.bloodRune), 'e', OrbRegistry.getOrbStack(ModItems.orbApprentice))); //Sacrifice
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.bloodRune, 1, 4), "aba", "cdc", "aea", 'a', "stone", 'b', new ItemStack(ModItems.slate, 1, 1), 'c', "dustGlowstone", 'd', new ItemStack(ModBlocks.bloodRune), 'e', OrbRegistry.getOrbStack(ModItems.orbApprentice))); //Self-Sacrifice
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.bloodRune, 1, 5), "aba", "bcb", "ada", 'a', "stone", 'b', Items.WATER_BUCKET, 'c', new ItemStack(ModBlocks.bloodRune), 'd', new ItemStack(ModItems.slate, 1, 2))); //Displacement
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.bloodRune, 1, 6), "aba", "bcb", "ada", 'a', "stone", 'b', Items.BUCKET, 'c', new ItemStack(ModBlocks.bloodRune), 'd', new ItemStack(ModItems.slate, 1, 2))); //Capacity
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.bloodRune, 1, 7), "aba", "cdc", "aea", 'a', Blocks.OBSIDIAN, 'b', new ItemStack(ModItems.slate, 1, 3), 'c', Items.BUCKET, 'd', new ItemStack(ModBlocks.bloodRune, 1, 6), 'e', OrbRegistry.getOrbStack(ModItems.orbMaster))); //Augmented Capacity
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.bloodRune, 1, 8), "aba", "cdc", "aba", 'a', "stone", 'b', OrbRegistry.getOrbStack(ModItems.orbWeak), 'c', new ItemStack(ModBlocks.bloodRune), 'd', OrbRegistry.getOrbStack(ModItems.orbMaster))); //Orb
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.bloodRune, 1, 9), "aba", "cdc", "aea", 'a', Items.BUCKET, 'b', new ItemStack(ModItems.slate, 1, 4), 'c', "ingotGold", 'd', new ItemStack(ModBlocks.bloodRune, 1, 1), 'e', OrbRegistry.getOrbStack(ModItems.orbArchmage))); //Acceleration
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.ritualStone, 4), "aba", "bcb", "aba", 'a', Blocks.OBSIDIAN, 'b', new ItemStack(ModItems.slate, 1, 1), 'c', OrbRegistry.getOrbStack(ModItems.orbApprentice)));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.ritualController), "aba", "bcb", "aba", 'a', Blocks.OBSIDIAN, 'b', ModBlocks.ritualStone, 'c', OrbRegistry.getOrbStack(ModItems.orbMagician)));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.ritualController, 1, 1), "aba", "bcb", "aba", 'a', Blocks.OBSIDIAN, 'b', "stone", 'c', OrbRegistry.getOrbStack(ModItems.orbWeak)));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.baseItemFilter, 1, 0), "sgs", "gfg", "sgs", 'f', ItemComponent.getStack(ItemComponent.COMPONENT_FRAME_PART), 'g', "blockGlass", 's', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.baseItemFilter, 1, 1), "sgs", "gfg", "sgs", 'f', ItemComponent.getStack(ItemComponent.COMPONENT_FRAME_PART), 'g', "dyeYellow", 's', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.baseItemFilter, 1, 2), "sgs", "gfg", "sgs", 'f', ItemComponent.getStack(ItemComponent.COMPONENT_FRAME_PART), 'g', "dyeGreen", 's', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.baseItemFilter, 1, 3), "sgs", "gfg", "sgs", 'f', ItemComponent.getStack(ItemComponent.COMPONENT_FRAME_PART), 'g', "dyePurple", 's', "stickWood"));

        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModItems.upgradeTrainer), "ngn", "ioi", "ngn", 'o', OrbRegistry.getOrbStack(ModItems.orbMaster), 'i', "ingotIron", 'n', "nuggetGold", 'g', "ingotGold"));

        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.incenseAltar), "s s", "shs", "coc", 'o', OrbRegistry.getOrbStack(ModItems.orbWeak), 's', "stone", 'c', "cobblestone", 'h', new ItemStack(Items.COAL, 1, 1)));
        GameRegistry.addRecipe(new ShapelessBloodOrbRecipe(new ItemStack(ModBlocks.pathBlock, 4, 0), "plankWood", "plankWood", "plankWood", "plankWood", OrbRegistry.getOrbStack(ModItems.orbApprentice)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.pathBlock, 4, 1), new ItemStack(ModBlocks.pathBlock, 1, 0), new ItemStack(ModBlocks.pathBlock, 1, 0), new ItemStack(ModBlocks.pathBlock, 1, 0), new ItemStack(ModBlocks.pathBlock, 1, 0)));
        GameRegistry.addRecipe(new ShapelessBloodOrbRecipe(new ItemStack(ModBlocks.pathBlock, 4, 2), "stone", "stone", "stone", "stone", OrbRegistry.getOrbStack(ModItems.orbMagician)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.pathBlock, 4, 3), new ItemStack(ModBlocks.pathBlock, 1, 2), new ItemStack(ModBlocks.pathBlock, 1, 2), new ItemStack(ModBlocks.pathBlock, 1, 2), new ItemStack(ModBlocks.pathBlock, 1, 2)));
        GameRegistry.addRecipe(new ShapelessBloodOrbRecipe(new ItemStack(ModBlocks.pathBlock, 4, 4), new ItemStack(ModBlocks.pathBlock, 1, 2), new ItemStack(ModBlocks.pathBlock, 1, 2), new ItemStack(ModBlocks.pathBlock, 1, 2), new ItemStack(ModBlocks.pathBlock, 1, 2), OrbRegistry.getOrbStack(ModItems.orbMaster)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.pathBlock, 4, 5), new ItemStack(ModBlocks.pathBlock, 1, 4), new ItemStack(ModBlocks.pathBlock, 1, 4), new ItemStack(ModBlocks.pathBlock, 1, 4), new ItemStack(ModBlocks.pathBlock, 1, 4)));
        GameRegistry.addRecipe(new ShapelessBloodOrbRecipe(new ItemStack(ModBlocks.pathBlock, 4, 6), Blocks.OBSIDIAN, Blocks.OBSIDIAN, Blocks.OBSIDIAN, Blocks.OBSIDIAN, OrbRegistry.getOrbStack(ModItems.orbArchmage)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.pathBlock, 4, 7), new ItemStack(ModBlocks.pathBlock, 1, 6), new ItemStack(ModBlocks.pathBlock, 1, 6), new ItemStack(ModBlocks.pathBlock, 1, 6), new ItemStack(ModBlocks.pathBlock, 1, 6)));

        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModItems.experienceTome), "ses", "lbl", "gog", 'o', OrbRegistry.getOrbStack(ModItems.orbMagician), 'e', Blocks.LAPIS_BLOCK, 'l', new ItemStack(ModItems.slate, 1, 2), 'b', Items.ENCHANTED_BOOK, 's', Items.STRING, 'g', "ingotGold"));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModItems.ritualReader), "gog", "isi", " s ", 's', new ItemStack(ModItems.slate, 1, 3), 'g', "blockGlass", 'i', "ingotGold", 'o', OrbRegistry.getOrbStack(ModItems.orbMaster)));

        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.alchemyTable), "sss", "wbw", "gog", 's', "stone", 'w', "plankWood", 'b', Items.BLAZE_ROD, 'g', "ingotGold", 'o', OrbRegistry.getOrbStack(ModItems.orbWeak)));

        for (int i = 1; i < BlockBloodRune.names.length; i++)
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.bloodRune), new ItemStack(ModBlocks.bloodRune, 1, i)));

        for (int i = 0; i < ItemSoulGem.names.length; i++)
        {
            for (int j = 0; j < ItemDemonCrystal.getNames().size(); j++)
            {
                ItemStack baseGemStack = new ItemStack(ModItems.soulGem, 1, i);
                ItemStack newGemStack = new ItemStack(ModItems.soulGem, 1, i);

                ItemStack crystalStack = new ItemStack(ModItems.itemDemonCrystal, 1, j);

                ((ItemSoulGem) ModItems.soulGem).setCurrentType(((ItemDemonCrystal) ModItems.itemDemonCrystal).getType(crystalStack), newGemStack);
                GameRegistry.addShapelessRecipe(newGemStack, baseGemStack, crystalStack);
            }
        }
    }

    public static void addAltarRecipes()
    {
        // ONE
        AltarRecipeRegistry.registerFillRecipe(OrbRegistry.getOrbStack(ModItems.orbWeak), EnumAltarTier.ONE, ModItems.orbWeak.getCapacity(), 2, 1);
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.DIAMOND), OrbRegistry.getOrbStack(ModItems.orbWeak), EnumAltarTier.ONE, 2000, 2, 1));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Blocks.STONE), new ItemStack(ModItems.slate), EnumAltarTier.ONE, 1000, 5, 5));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.BUCKET), new ItemStack(ForgeModContainer.getInstance().universalBucket), EnumAltarTier.ONE, 1000, 5, 0));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.BOOK), new ItemStack(ModItems.sanguineBook), EnumAltarTier.ONE, 1000, 20, 0));

        // TWO
        AltarRecipeRegistry.registerFillRecipe(OrbRegistry.getOrbStack(ModItems.orbApprentice), EnumAltarTier.TWO, ModItems.orbApprentice.getCapacity(), 5, 5);
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Blocks.REDSTONE_BLOCK), OrbRegistry.getOrbStack(ModItems.orbApprentice), EnumAltarTier.TWO, 5000, 5, 5));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModItems.slate), new ItemStack(ModItems.slate, 1, 1), EnumAltarTier.TWO, 2000, 5, 5));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.IRON_SWORD), new ItemStack(ModItems.daggerOfSacrifice), EnumAltarTier.TWO, 3000, 5, 5));

        // THREE
        AltarRecipeRegistry.registerFillRecipe(OrbRegistry.getOrbStack(ModItems.orbMagician), EnumAltarTier.THREE, ModItems.orbMagician.getCapacity(), 15, 15);
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Blocks.GOLD_BLOCK), OrbRegistry.getOrbStack(ModItems.orbMagician), EnumAltarTier.THREE, 25000, 20, 20));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModItems.slate, 1, 1), new ItemStack(ModItems.slate, 1, 2), EnumAltarTier.THREE, 5000, 15, 10));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Blocks.OBSIDIAN), EnumRuneType.EARTH.getScribeStack(), EnumAltarTier.THREE, 1000, 5, 5));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Blocks.LAPIS_BLOCK), EnumRuneType.WATER.getScribeStack(), EnumAltarTier.THREE, 1000, 5, 5));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.MAGMA_CREAM), EnumRuneType.FIRE.getScribeStack(), EnumAltarTier.THREE, 1000, 5, 5));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.GHAST_TEAR), EnumRuneType.AIR.getScribeStack(), EnumAltarTier.THREE, 1000, 5, 5));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModItems.lavaCrystal), new ItemStack(ModItems.activationCrystal), EnumAltarTier.THREE, 10000, 20, 10));

        // FOUR
        AltarRecipeRegistry.registerFillRecipe(OrbRegistry.getOrbStack(ModItems.orbMaster), EnumAltarTier.FOUR, ModItems.orbMaster.getCapacity(), 25, 25);
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModItems.bloodShard), OrbRegistry.getOrbStack(ModItems.orbMaster), EnumAltarTier.FOUR, 25000, 30, 50));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModItems.slate, 1, 2), new ItemStack(ModItems.slate, 1, 3), EnumAltarTier.FOUR, 15000, 20, 20));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Blocks.COAL_BLOCK), EnumRuneType.DUSK.getScribeStack(), EnumAltarTier.FOUR, 2000, 20, 10));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.ENDER_PEARL), new ItemStack(ModItems.telepositionFocus), EnumAltarTier.FOUR, 2000, 10, 10));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModItems.telepositionFocus), new ItemStack(ModItems.telepositionFocus, 1, 1), EnumAltarTier.FOUR, 10000, 20, 10));

        // FIVE
        AltarRecipeRegistry.registerFillRecipe(OrbRegistry.getOrbStack(ModItems.orbArchmage), EnumAltarTier.FIVE, ModItems.orbArchmage.getCapacity(), 50, 50);
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.NETHER_STAR), OrbRegistry.getOrbStack(ModItems.orbArchmage), EnumAltarTier.FIVE, 80000, 50, 100));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModItems.slate, 1, 3), new ItemStack(ModItems.slate, 1, 4), EnumAltarTier.FIVE, 30000, 40, 100));

        // SIX
        AltarRecipeRegistry.registerFillRecipe(OrbRegistry.getOrbStack(ModItems.orbTranscendent), EnumAltarTier.SIX, ModItems.orbTranscendent.getCapacity(), 50, 50);
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModBlocks.crystal), OrbRegistry.getOrbStack(ModItems.orbTranscendent), EnumAltarTier.SIX, 200000, 100, 200));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Blocks.GLOWSTONE), EnumRuneType.DAWN.getScribeStack(), EnumAltarTier.SIX, 200000, 100, 200));
    }

    public static void addAlchemyArrayRecipes()
    {
        AlchemyArrayRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), new ItemStack(Items.DIAMOND_SWORD), new AlchemyArrayEffectBinding("boundSword", Utils.setUnbreakable(new ItemStack(ModItems.boundSword))), new BindingAlchemyCircleRenderer());
        AlchemyArrayRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), new ItemStack(Items.DIAMOND_AXE), new AlchemyArrayEffectBinding("boundAxe", Utils.setUnbreakable(new ItemStack(ModItems.boundAxe))));
        AlchemyArrayRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), new ItemStack(Items.DIAMOND_PICKAXE), new AlchemyArrayEffectBinding("boundPickaxe", Utils.setUnbreakable(new ItemStack(ModItems.boundPickaxe))));
        AlchemyArrayRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), new ItemStack(Items.DIAMOND_SHOVEL), new AlchemyArrayEffectBinding("boundShovel", Utils.setUnbreakable(new ItemStack(ModItems.boundShovel))));
        AlchemyArrayRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), new ItemStack(Items.IRON_HELMET), new AlchemyArrayEffectBinding("livingHelmet", new ItemStack(ModItems.livingArmourHelmet)));
        AlchemyArrayRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), new ItemStack(Items.IRON_CHESTPLATE), new AlchemyArrayEffectBinding("livingChest", new ItemStack(ModItems.livingArmourChest)));
        AlchemyArrayRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), new ItemStack(Items.IRON_LEGGINGS), new AlchemyArrayEffectBinding("livingLegs", new ItemStack(ModItems.livingArmourLegs)));
        AlchemyArrayRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), new ItemStack(Items.IRON_BOOTS), new AlchemyArrayEffectBinding("livingBoots", new ItemStack(ModItems.livingArmourBoots)));

        AlchemyArrayRecipeRegistry.registerCraftingRecipe(new ItemStack(Items.REDSTONE), new ItemStack(ModItems.slate), new ItemStack(ModItems.sigilDivination), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/DivinationSigil.png"));

        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_WATER), new ItemStack(ModItems.slate), new ItemStack(ModItems.sigilWater), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/WaterSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_LAVA), new ItemStack(ModItems.slate), new ItemStack(ModItems.sigilLava), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/LavaSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_AIR), new ItemStack(ModItems.slate, 1, 1), new ItemStack(ModItems.sigilAir), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/AirSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_FASTMINER), new ItemStack(ModItems.slate, 1, 1), new ItemStack(ModItems.sigilFastMiner), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/FastMinerSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_VOID), new ItemStack(ModItems.slate, 1, 1), new ItemStack(ModItems.sigilVoid), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/VoidSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_GROWTH), new ItemStack(ModItems.slate, 1, 1), new ItemStack(ModItems.sigilGreenGrove), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/GrowthSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_AFFINITY), new ItemStack(ModItems.slate, 1, 2), new ItemStack(ModItems.sigilElementalAffinity), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/ElementalAffinitySigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_SIGHT), new ItemStack(ModItems.slate, 1, 1), new ItemStack(ModItems.sigilSeer), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/SightSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_HOLDING), new ItemStack(ModItems.slate, 1, 2), new ItemStack(ModItems.sigilHolding), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/WIPArray.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BLOODLIGHT), new ItemStack(ModItems.slate, 1, 2), new ItemStack(ModItems.sigilBloodLight), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/LightSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_MAGNETISM), new ItemStack(ModItems.slate, 1, 2), new ItemStack(ModItems.sigilMagnetism), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/MagnetismSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_SUPPRESSION), new ItemStack(ModItems.slate, 1, 3), new ItemStack(ModItems.sigilSuppression), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/SuppressionSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_HASTE), new ItemStack(ModItems.slate, 1, 3), new ItemStack(ModItems.sigilHaste), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/WIPArray.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BRIDGE), new ItemStack(ModItems.slate, 1, 3), new ItemStack(ModItems.sigilPhantomBridge), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/WIPArray.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_COMPRESSION), new ItemStack(ModItems.slate, 1, 3), new ItemStack(ModItems.sigilCompression), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/WIPArray.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_SEVERANCE), new ItemStack(ModItems.slate, 1, 3), new ItemStack(ModItems.sigilEnderSeverance), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/WIPArray.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_TELEPOSITION), new ItemStack(ModItems.slate, 1, 3), new ItemStack(ModItems.sigilTeleposition), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/WIPArray.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_TRANSPOSITION), new ItemStack(ModItems.slate, 1, 3), new ItemStack(ModItems.sigilTransposition), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/WIPArray.png"));

        AlchemyArrayRecipeRegistry.registerRecipe(new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.ROTTEN_FLESH), new AlchemyArrayEffectAttractor("attractor"), new AttractorAlchemyCircleRenderer());
        AlchemyArrayRecipeRegistry.registerRecipe(new ItemStack(Items.FEATHER), new ItemStack(Items.REDSTONE), new AlchemyArrayEffectMovement("movement"), new StaticAlchemyCircleRenderer(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/MovementArray.png")));
        AlchemyArrayRecipeRegistry.registerRecipe(new ItemStack(Items.ARROW), new ItemStack(Items.FEATHER), new AlchemyArrayEffectSkeletonTurret("skeletonTurret"), new DualAlchemyCircleRenderer(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/SkeletonTurret1.png"), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/SkeletonTurret2.png")));
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

        BloodMagic.instance.getLogger().info("Added compression recipes in {}", stopwatch);
    }

    public static void addSoulForgeRecipes()
    {
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.soulGem), 1, 1, "dustRedstone", "ingotGold", "blockGlass", "dyeBlue");
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.soulGem, 1, 1), 60, 20, new ItemStack(ModItems.soulGem), "gemDiamond", "blockRedstone", "blockLapis");
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.soulGem, 1, 2), 240, 50, new ItemStack(ModItems.soulGem, 1, 1), "gemDiamond", "blockGold", new ItemStack(ModItems.slate, 1, 2));
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.soulGem, 1, 3), 1000, 100, new ItemStack(ModItems.soulGem, 1, 2), new ItemStack(ModItems.slate, 1, 3), new ItemStack(ModItems.bloodShard), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DEFAULT));
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.soulGem, 1, 4), 4000, 500, new ItemStack(ModItems.soulGem, 1, 3), Items.NETHER_STAR);
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.sentientSword), 0, 0, new ItemStack(ModItems.soulGem), new ItemStack(Items.IRON_SWORD));
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.sentientAxe), 0, 0, new ItemStack(ModItems.soulGem), new ItemStack(Items.IRON_AXE));
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.sentientPickaxe), 0, 0, new ItemStack(ModItems.soulGem), new ItemStack(Items.IRON_PICKAXE));
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.sentientShovel), 0, 0, new ItemStack(ModItems.soulGem), new ItemStack(Items.IRON_SHOVEL));
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.sentientBow), 70, 0, new ItemStack(Items.BOW), new ItemStack(ModItems.soulGem, 1, 1), Items.STRING, Items.STRING);
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.arcaneAshes), 0, 0, "dustRedstone", "dyeWhite", new ItemStack(Items.GUNPOWDER), Items.COAL);
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_WATER), 10, 3, new ItemStack(Items.SUGAR), new ItemStack(Items.WATER_BUCKET), new ItemStack(Items.WATER_BUCKET));
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_LAVA), 32, 10, Items.LAVA_BUCKET, "dustRedstone", "cobblestone", "blockCoal");
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_VOID), 64, 10, Items.BUCKET, Items.STRING, Items.STRING, Items.GUNPOWDER);
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_GROWTH), 128, 20, "treeSapling", "treeSapling", Items.REEDS, Items.SUGAR);
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_AIR), 128, 20, Items.GHAST_TEAR, Items.FEATHER, Items.FEATHER);
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_SIGHT), 64, 0, ModItems.sigilDivination, "blockGlass", "blockGlass", "dustGlowstone");
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_HOLDING), 64, 20, Blocks.CHEST, "leather", "string", "string");
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_FASTMINER), 128, 10, Items.IRON_PICKAXE, Items.IRON_AXE, Items.IRON_SHOVEL, Items.GUNPOWDER);
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_AFFINITY), 300, 30, ModItems.sigilWater, ModItems.sigilAir, ModItems.sigilLava, Blocks.OBSIDIAN);
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_SUPPRESSION), 500, 50, ModBlocks.teleposer, Items.WATER_BUCKET, Items.LAVA_BUCKET, Items.BLAZE_ROD);
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), 400, 10, "dustGlowstone", "dustRedstone", "nuggetGold", Items.GUNPOWDER);
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BLOODLIGHT), 300, 10, "glowstone", Blocks.TORCH, "dustRedstone", "dustRedstone");
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_MAGNETISM), 600, 10, Items.STRING, "ingotGold", "blockIron", "ingotGold");
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_HASTE), 1400, 100, Items.COOKIE, Items.SUGAR, Items.COOKIE, "stone");
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BRIDGE), 600, 50, Blocks.SOUL_SAND, Blocks.SOUL_SAND, "stone", Blocks.OBSIDIAN);
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_SEVERANCE), 800, 70, Items.ENDER_EYE, Items.ENDER_PEARL, "ingotGold", "ingotGold");
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_COMPRESSION), 2000, 200, "blockIron", "blockGold", Blocks.OBSIDIAN, "cobblestone");
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_TELEPOSITION), 1500, 200, ModBlocks.teleposer, "glowstone", "blockRedstone", "ingotGold");
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_TRANSPOSITION), 1500, 200, ModBlocks.teleposer, "gemDiamond", Items.ENDER_PEARL, Blocks.OBSIDIAN);

        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.sentientArmourGem), 240, 150, Items.DIAMOND_CHESTPLATE, new ItemStack(ModItems.soulGem, 1, 1), Blocks.IRON_BLOCK, Blocks.OBSIDIAN);

        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.COMPONENT_FRAME_PART), 400, 10, "blockGlass", "stone", new ItemStack(ModItems.slate));
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.nodeRouter), 400, 5, "stickWood", new ItemStack(ModItems.slate, 1, 1), "gemLapis", "gemLapis");
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModBlocks.itemRoutingNode), 400, 5, "dustGlowstone", "dustRedstone", "blockGlass", "stone");
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModBlocks.outputRoutingNode), 400, 25, "dustGlowstone", "dustRedstone", "ingotIron", new ItemStack(ModBlocks.itemRoutingNode));
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModBlocks.inputRoutingNode), 400, 25, "dustGlowstone", "dustRedstone", "ingotGold", new ItemStack(ModBlocks.itemRoutingNode));
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModBlocks.masterRoutingNode), 400, 200, "blockIron", "gemDiamond", new ItemStack(ModItems.slate, 1, 2));

        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModBlocks.demonCrystal, 1, 0), 1200, 100, ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DEFAULT), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DEFAULT), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DEFAULT), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DEFAULT));
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModBlocks.demonCrystal, 1, 1), 1200, 100, ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_CORROSIVE), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_CORROSIVE), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_CORROSIVE), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_CORROSIVE));
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModBlocks.demonCrystal, 1, 2), 1200, 100, ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DESTRUCTIVE), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DESTRUCTIVE), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DESTRUCTIVE), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DESTRUCTIVE));
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModBlocks.demonCrystal, 1, 3), 1200, 100, ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_VENGEFUL), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_VENGEFUL), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_VENGEFUL), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_VENGEFUL));
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModBlocks.demonCrystal, 1, 4), 1200, 100, ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_STEADFAST), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_STEADFAST), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_STEADFAST), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_STEADFAST));

        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModBlocks.demonCrucible), 400, 100, Items.CAULDRON, "stone", "gemLapis", "gemDiamond");
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModBlocks.demonPylon), 400, 50, "blockIron", "stone", "gemLapis", ModItems.itemDemonCrystal);
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModBlocks.demonCrystallizer), 500, 100, ModBlocks.soulForge, "stone", "gemLapis", "blockGlass");
    }

    public static void addAlchemyTableRecipes()
    {
        AlchemyTableRecipeRegistry.registerRecipe(new ItemStack(Items.STRING, 4), 0, 100, 0, Blocks.WOOL, Items.FLINT);
        AlchemyTableRecipeRegistry.registerRecipe(new ItemStack(Items.FLINT, 2), 0, 20, 0, Blocks.GRAVEL, Items.FLINT);
        AlchemyTableRecipeRegistry.registerRecipe(new ItemStack(Items.LEATHER, 4), 100, 200, 1, Items.ROTTEN_FLESH, Items.ROTTEN_FLESH, Items.ROTTEN_FLESH, Items.ROTTEN_FLESH, Items.FLINT, Items.WATER_BUCKET);

        AlchemyTableRecipeRegistry.registerRecipe(ItemCuttingFluid.getStack(ItemCuttingFluid.EXPLOSIVE), 500, 200, 1, Items.GUNPOWDER, Items.GUNPOWDER, "dustCoal");

        AlchemyTableRecipeRegistry.registerRecipe(new ItemStack(Items.BREAD), 100, 200, 1, Items.WHEAT, Items.SUGAR);
        AlchemyTableRecipeRegistry.registerRecipe(new ItemStack(Blocks.GRASS), 200, 200, 1, Blocks.DIRT, new ItemStack(Items.DYE, 1, 15), Items.WHEAT_SEEDS);
        AlchemyTableRecipeRegistry.registerRecipe(new ItemStack(Items.CLAY_BALL, 4), 50, 100, 2, Items.WATER_BUCKET, "sand");
        AlchemyTableRecipeRegistry.registerRecipe(new ItemStack(Blocks.CLAY, 5), 200, 200, 1, Items.WATER_BUCKET, Blocks.HARDENED_CLAY, Blocks.HARDENED_CLAY, Blocks.HARDENED_CLAY, Blocks.HARDENED_CLAY, Blocks.HARDENED_CLAY);
        AlchemyTableRecipeRegistry.registerRecipe(new ItemStack(Blocks.OBSIDIAN), 50, 50, 1, Items.WATER_BUCKET, Items.LAVA_BUCKET);

        AlchemyTableRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.SULFUR, 8), 0, 100, 0, Items.LAVA_BUCKET);
        AlchemyTableRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.SALTPETER, 4), 0, 100, 0, ItemComponent.getStack(ItemComponent.PLANT_OIL), ItemComponent.getStack(ItemComponent.PLANT_OIL), "dustCoal");
        AlchemyTableRecipeRegistry.registerRecipe(new ItemStack(Items.GUNPOWDER, 3), 0, 100, 0, ItemComponent.getStack(ItemComponent.SALTPETER), ItemComponent.getStack(ItemComponent.SULFUR), new ItemStack(Items.COAL, 1, 1));

        AlchemyTableRecipeRegistry.registerRecipe(new AlchemyTableCustomRecipe(ItemComponent.getStack(ItemComponent.SAND_COAL, 4), 100, 100, 1, new ItemStack(Items.COAL, 1, 0), new ItemStack(Items.COAL, 1, 0), Items.FLINT));

        AlchemyTableRecipeRegistry.registerRecipe(ItemCuttingFluid.getStack(ItemCuttingFluid.BASIC), 1000, 400, 1, "dustCoal", Items.GUNPOWDER, Items.REDSTONE, Items.SUGAR, ItemComponent.getStack(ItemComponent.PLANT_OIL), new ItemStack(Items.POTIONITEM));

        AlchemyTableRecipeRegistry.registerRecipe(new AlchemyTableCustomRecipe(ItemComponent.getStack(ItemComponent.SAND_IRON, 2), 400, 200, 1, "oreIron", ItemCuttingFluid.getStack(ItemCuttingFluid.BASIC)));
        AlchemyTableRecipeRegistry.registerRecipe(new AlchemyTableCustomRecipe(ItemComponent.getStack(ItemComponent.SAND_GOLD, 2), 400, 200, 1, "oreGold", ItemCuttingFluid.getStack(ItemCuttingFluid.BASIC)));

        AlchemyTableRecipeRegistry.registerRecipe(new AlchemyTableCustomRecipe(new ItemStack(Items.REDSTONE, 8), 400, 200, 1, "oreRedstone", ItemCuttingFluid.getStack(ItemCuttingFluid.BASIC)));

        addedOreRecipeList.add("oreIron");
        addedOreRecipeList.add("oreGold");
        addedOreRecipeList.add("oreCoal");
        addedOreRecipeList.add("oreRedstone");

        AlchemyTableRecipeRegistry.registerRecipe(new AlchemyTableCustomRecipe(new ItemStack(Blocks.GRAVEL), 50, 50, 1, "cobblestone", ItemCuttingFluid.getStack(ItemCuttingFluid.EXPLOSIVE)));
        AlchemyTableRecipeRegistry.registerRecipe(new AlchemyTableCustomRecipe(new ItemStack(Blocks.SAND), 50, 50, 1, Blocks.GRAVEL, ItemCuttingFluid.getStack(ItemCuttingFluid.EXPLOSIVE)));

        AlchemyTableRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.PLANT_OIL), 100, 100, 1, Items.CARROT, Items.CARROT, Items.CARROT, new ItemStack(Items.DYE, 1, 15));
        AlchemyTableRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.PLANT_OIL), 100, 100, 1, Items.POTATO, Items.POTATO, new ItemStack(Items.DYE, 1, 15));
        AlchemyTableRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.PLANT_OIL), 100, 100, 1, Items.WHEAT, Items.WHEAT, new ItemStack(Items.DYE, 1, 15));
        AlchemyTableRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.PLANT_OIL), 100, 100, 1, Items.BEETROOT, Items.BEETROOT, Items.BEETROOT, new ItemStack(Items.DYE, 1, 15));

        AlchemyTableRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.NEURO_TOXIN), 1000, 100, 2, new ItemStack(Items.FISH, 1, 3));
        AlchemyTableRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.ANTISEPTIC, 2), 1000, 200, 2, ItemComponent.getStack(ItemComponent.PLANT_OIL), "nuggetGold", Items.WHEAT, Items.SUGAR, Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM);

        AlchemyTableRecipeRegistry.registerRecipe(ItemLivingArmourPointsUpgrade.getStack(ItemLivingArmourPointsUpgrade.DRAFT_ANGELUS), 20000, 400, 3, ItemComponent.getStack(ItemComponent.NEURO_TOXIN), ItemComponent.getStack(ItemComponent.ANTISEPTIC), ItemComponent.getStack(ItemComponent.SAND_GOLD), Items.FERMENTED_SPIDER_EYE, new ItemStack(ModItems.bloodShard, 1, 0), Items.GHAST_TEAR);

        AlchemyTableRecipeRegistry.registerRecipe(new AlchemyTableDyeableRecipe(0, 100, 0, new ItemStack(ModItems.sigilHolding)));
    }

    public static void addOreDoublingAlchemyRecipes()
    {
        String[] oreList = OreDictionary.getOreNames().clone();
        for (String ore : oreList)
        {
            if (ore.startsWith("ore") && !addedOreRecipeList.contains(ore))
            {
                String dustName = ore.replaceFirst("ore", "dust");

                List<ItemStack> dustList = OreDictionary.getOres(dustName);
                if (dustList != null && dustList.size() > 0)
                {
                    ItemStack dustStack = dustList.get(0).copy();
                    dustStack.stackSize = 2;
                    AlchemyTableRecipeRegistry.registerRecipe(new AlchemyTableCustomRecipe(dustStack, 400, 200, 1, ore, ItemCuttingFluid.getStack(ItemCuttingFluid.BASIC)));
                    addedOreRecipeList.add(ore);
                }
            }
        }
    }
}
