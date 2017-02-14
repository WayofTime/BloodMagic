package WayofTime.bloodmagic.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import WayofTime.bloodmagic.tile.TileBloodTank;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import org.apache.commons.lang3.tuple.Pair;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectAttractor;
import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectBinding;
import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectBounce;
import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectMovement;
import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectSigil;
import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectSkeletonTurret;
import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectUpdraft;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.altar.EnumAltarTier;
import WayofTime.bloodmagic.api.compress.CompressionRegistry;
import WayofTime.bloodmagic.api.iface.ISigil;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourHandler;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.api.recipe.AlchemyTableCustomRecipe;
import WayofTime.bloodmagic.api.recipe.ShapedBloodOrbRecipe;
import WayofTime.bloodmagic.api.recipe.ShapelessBloodOrbRecipe;
import WayofTime.bloodmagic.api.registry.AlchemyArrayRecipeRegistry;
import WayofTime.bloodmagic.api.registry.AlchemyTableRecipeRegistry;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry;
import WayofTime.bloodmagic.api.registry.LivingArmourDowngradeRecipeRegistry;
import WayofTime.bloodmagic.api.registry.OrbRegistry;
import WayofTime.bloodmagic.api.registry.TartaricForgeRecipeRegistry;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.block.enums.EnumBloodRune;
import WayofTime.bloodmagic.client.render.alchemyArray.AttractorAlchemyCircleRenderer;
import WayofTime.bloodmagic.client.render.alchemyArray.BindingAlchemyCircleRenderer;
import WayofTime.bloodmagic.client.render.alchemyArray.DualAlchemyCircleRenderer;
import WayofTime.bloodmagic.client.render.alchemyArray.SingleAlchemyCircleRenderer;
import WayofTime.bloodmagic.client.render.alchemyArray.StaticAlchemyCircleRenderer;
import WayofTime.bloodmagic.compress.AdvancedCompressionHandler;
import WayofTime.bloodmagic.compress.BaseCompressionHandler;
import WayofTime.bloodmagic.compress.StorageBlockCraftingManager;
import WayofTime.bloodmagic.item.ItemComponent;
import WayofTime.bloodmagic.item.ItemDemonCrystal;
import WayofTime.bloodmagic.item.alchemy.ItemCuttingFluid;
import WayofTime.bloodmagic.item.alchemy.ItemLivingArmourPointsUpgrade;
import WayofTime.bloodmagic.item.soul.ItemSoulGem;
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
        addPotionRecipes();
        addLivingArmourDowngradeRecipes();
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
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.SOUL_FORGE), "i i", "sgs", "sos", 'i', "ingotIron", 's', "stone", 'g', "ingotGold", 'o', "blockIron"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.SACRIFICIAL_DAGGER), "aaa", " ba", "c a", 'a', "blockGlass", 'b', "ingotGold", 'c', "ingotIron"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.ALTAR), "a a", "aba", "cdc", 'a', "stone", 'b', Blocks.FURNACE, 'c', "ingotGold", 'd', new ItemStack(ModItems.MONSTER_SOUL)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.PACK_SELF_SACRIFICE), "aba", "cdc", "aea", 'a', "blockGlass", 'b', Items.BUCKET, 'c', Items.FLINT, 'd', Items.LEATHER_CHESTPLATE, 'e', ModItems.SLATE));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.PACK_SACRIFICE), "aba", "cdc", "aea", 'a', "blockGlass", 'b', Items.BUCKET, 'c', "ingotIron", 'd', Items.LEATHER_CHESTPLATE, 'e', ModItems.SLATE));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.RITUAL_DIVINER), "dfd", "ase", "dwd", 'f', EnumRuneType.FIRE.getScribeStack(), 'a', EnumRuneType.AIR.getScribeStack(), 'w', EnumRuneType.WATER.getScribeStack(), 'e', EnumRuneType.EARTH.getScribeStack(), 'd', "gemDiamond", 's', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.RITUAL_DIVINER, 1, 1), " S ", "tdt", " S ", 'S', new ItemStack(ModItems.SLATE, 1, 3), 't', EnumRuneType.DUSK.getScribeStack(), 'd', new ItemStack(ModItems.RITUAL_DIVINER)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.BLOOD_STONE, 1, 1), "aa", "aa", 'a', new ItemStack(ModBlocks.BLOOD_STONE)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.BLOOD_STONE, 16), "stone", new ItemStack(ModItems.BLOOD_SHARD)));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.ACTIVATION_CRYSTAL, 1, 1), new ItemStack(Items.NETHER_STAR), OrbRegistry.getOrbStack(ModItems.ORB_ARCHMAGE));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.SOUL_SNARE, 4, 0), "sis", "iri", "sis", 's', new ItemStack(Items.STRING), 'i', "ingotIron", 'r', "dustRedstone"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.BLOOD_TANK), "RBR", "G G", "RRR", 'R', new ItemStack(ModBlocks.BLOOD_RUNE), 'B', ModBlocks.BLOOD_STONE, 'G', "blockGlass"));
        for (int i = 1; i < TileBloodTank.CAPACITIES.length; i++)
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.BLOOD_TANK, 1, i), "RBR", "T T", "RRR", 'R', new ItemStack(ModBlocks.BLOOD_RUNE), 'B', ModBlocks.BLOOD_STONE, 'T', new ItemStack(ModBlocks.BLOOD_TANK, 1, i - 1)));

        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.TELEPOSITION_FOCUS, 1, 2), new ItemStack(ModItems.TELEPOSITION_FOCUS, 1, 1), new ItemStack(ModItems.BLOOD_SHARD));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.TELEPOSITION_FOCUS, 1, 3), new ItemStack(ModItems.TELEPOSITION_FOCUS, 1, 2), new ItemStack(ModItems.BLOOD_SHARD, 1, 1));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.TELEPOSER), "ggg", "efe", "ggg", 'g', "ingotGold", 'e', new ItemStack(Items.ENDER_PEARL), 'f', ModItems.TELEPOSITION_FOCUS));

        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModItems.LAVA_CRYSTAL), "aba", "bcb", "ded", 'a', "blockGlass", 'b', Items.LAVA_BUCKET, 'c', OrbRegistry.getOrbStack(ModItems.ORB_WEAK), 'd', Blocks.OBSIDIAN, 'e', "gemDiamond"));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.BLOOD_RUNE), "aaa", "bcb", "aaa", 'a', "stone", 'b', ModItems.SLATE, 'c', OrbRegistry.getOrbStack(ModItems.ORB_WEAK)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.BLOOD_RUNE, 1, 1), "aba", "cdc", "aba", 'a', "stone", 'b', ModItems.SLATE, 'c', Items.SUGAR, 'd', new ItemStack(ModBlocks.BLOOD_RUNE))); //Speed
//        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.bloodRune, 1, 2), "aba", "cdc", "aba", 'a', "stone", 'b', ModItems.slate, 'c', Items.sugar, 'd', new ItemStack(ModBlocks.bloodRune))); //Efficiency
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.BLOOD_RUNE, 1, 3), "aba", "cdc", "aea", 'a', "stone", 'b', new ItemStack(ModItems.SLATE, 1, 1), 'c', "ingotGold", 'd', new ItemStack(ModBlocks.BLOOD_RUNE), 'e', OrbRegistry.getOrbStack(ModItems.ORB_APPRENTICE))); //Sacrifice
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.BLOOD_RUNE, 1, 4), "aba", "cdc", "aea", 'a', "stone", 'b', new ItemStack(ModItems.SLATE, 1, 1), 'c', "dustGlowstone", 'd', new ItemStack(ModBlocks.BLOOD_RUNE), 'e', OrbRegistry.getOrbStack(ModItems.ORB_APPRENTICE))); //Self-Sacrifice
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.BLOOD_RUNE, 1, 5), "aba", "bcb", "ada", 'a', "stone", 'b', Items.WATER_BUCKET, 'c', new ItemStack(ModBlocks.BLOOD_RUNE), 'd', new ItemStack(ModItems.SLATE, 1, 2))); //Displacement
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.BLOOD_RUNE, 1, 6), "aba", "bcb", "ada", 'a', "stone", 'b', Items.BUCKET, 'c', new ItemStack(ModBlocks.BLOOD_RUNE), 'd', new ItemStack(ModItems.SLATE, 1, 2))); //Capacity
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.BLOOD_RUNE, 1, 7), "aba", "cdc", "aea", 'a', Blocks.OBSIDIAN, 'b', new ItemStack(ModItems.SLATE, 1, 3), 'c', Items.BUCKET, 'd', new ItemStack(ModBlocks.BLOOD_RUNE, 1, 6), 'e', OrbRegistry.getOrbStack(ModItems.ORB_MASTER))); //Augmented Capacity
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.BLOOD_RUNE, 1, 8), "aba", "cdc", "aba", 'a', "stone", 'b', OrbRegistry.getOrbStack(ModItems.ORB_WEAK), 'c', new ItemStack(ModBlocks.BLOOD_RUNE), 'd', OrbRegistry.getOrbStack(ModItems.ORB_MASTER))); //Orb
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.BLOOD_RUNE, 1, 9), "aba", "cdc", "aea", 'a', Items.BUCKET, 'b', new ItemStack(ModItems.SLATE, 1, 3), 'c', "ingotGold", 'd', new ItemStack(ModBlocks.BLOOD_RUNE, 1, 1), 'e', OrbRegistry.getOrbStack(ModItems.ORB_MASTER))); //Acceleration
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.BLOOD_RUNE, 1, 10), "RsR", "GrG", "ReR", 'G', "glowstone", 's', new ItemStack(ModItems.SLATE, 1, 3), 'R', "dustRedstone", 'r', new ItemStack(ModBlocks.BLOOD_RUNE, 1), 'e', OrbRegistry.getOrbStack(ModItems.ORB_MASTER))); //Charging
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.RITUAL_STONE, 4), "aba", "bcb", "aba", 'a', Blocks.OBSIDIAN, 'b', new ItemStack(ModItems.SLATE, 1, 1), 'c', OrbRegistry.getOrbStack(ModItems.ORB_APPRENTICE)));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.RITUAL_CONTROLLER), "aba", "bcb", "aba", 'a', Blocks.OBSIDIAN, 'b', ModBlocks.RITUAL_STONE, 'c', OrbRegistry.getOrbStack(ModItems.ORB_MAGICIAN)));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.RITUAL_CONTROLLER, 1, 1), "aba", "bcb", "aba", 'a', Blocks.OBSIDIAN, 'b', "stone", 'c', OrbRegistry.getOrbStack(ModItems.ORB_WEAK)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.RITUAL_CONTROLLER, 1, 2), new ItemStack(ModBlocks.RITUAL_CONTROLLER), new ItemStack(Blocks.REDSTONE_TORCH), new ItemStack(ModItems.SLATE)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.RITUAL_CONTROLLER), new ItemStack(ModBlocks.RITUAL_CONTROLLER, 1, 2)));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.BASE_ITEM_FILTER, 1, 0), "sgs", "gfg", "sgs", 'f', ItemComponent.getStack(ItemComponent.COMPONENT_FRAME_PART), 'g', "blockGlass", 's', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.BASE_ITEM_FILTER, 1, 1), "sgs", "gfg", "sgs", 'f', ItemComponent.getStack(ItemComponent.COMPONENT_FRAME_PART), 'g', "dyeYellow", 's', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.BASE_ITEM_FILTER, 1, 2), "sgs", "gfg", "sgs", 'f', ItemComponent.getStack(ItemComponent.COMPONENT_FRAME_PART), 'g', "dyeGreen", 's', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.BASE_ITEM_FILTER, 1, 3), "sgs", "gfg", "sgs", 'f', ItemComponent.getStack(ItemComponent.COMPONENT_FRAME_PART), 'g', "dyePurple", 's', "stickWood"));

        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModItems.UPGRADE_TRAINER), "ngn", "ioi", "ngn", 'o', OrbRegistry.getOrbStack(ModItems.ORB_MASTER), 'i', "ingotIron", 'n', "nuggetGold", 'g', "ingotGold"));

        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.INCENSE_ALTAR), "s s", "shs", "coc", 'o', OrbRegistry.getOrbStack(ModItems.ORB_WEAK), 's', "stone", 'c', "cobblestone", 'h', new ItemStack(Items.COAL, 1, 1)));
        GameRegistry.addRecipe(new ShapelessBloodOrbRecipe(new ItemStack(ModBlocks.PATH_BLOCK, 4, 0), "plankWood", "plankWood", "plankWood", "plankWood", OrbRegistry.getOrbStack(ModItems.ORB_APPRENTICE)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.PATH_BLOCK, 4, 1), new ItemStack(ModBlocks.PATH_BLOCK, 1, 0), new ItemStack(ModBlocks.PATH_BLOCK, 1, 0), new ItemStack(ModBlocks.PATH_BLOCK, 1, 0), new ItemStack(ModBlocks.PATH_BLOCK, 1, 0)));
        GameRegistry.addRecipe(new ShapelessBloodOrbRecipe(new ItemStack(ModBlocks.PATH_BLOCK, 4, 2), "stone", "stone", "stone", "stone", OrbRegistry.getOrbStack(ModItems.ORB_MAGICIAN)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.PATH_BLOCK, 4, 3), new ItemStack(ModBlocks.PATH_BLOCK, 1, 2), new ItemStack(ModBlocks.PATH_BLOCK, 1, 2), new ItemStack(ModBlocks.PATH_BLOCK, 1, 2), new ItemStack(ModBlocks.PATH_BLOCK, 1, 2)));
        GameRegistry.addRecipe(new ShapelessBloodOrbRecipe(new ItemStack(ModBlocks.PATH_BLOCK, 4, 4), new ItemStack(ModBlocks.PATH_BLOCK, 1, 2), new ItemStack(ModBlocks.PATH_BLOCK, 1, 2), new ItemStack(ModBlocks.PATH_BLOCK, 1, 2), new ItemStack(ModBlocks.PATH_BLOCK, 1, 2), OrbRegistry.getOrbStack(ModItems.ORB_MASTER)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.PATH_BLOCK, 4, 5), new ItemStack(ModBlocks.PATH_BLOCK, 1, 4), new ItemStack(ModBlocks.PATH_BLOCK, 1, 4), new ItemStack(ModBlocks.PATH_BLOCK, 1, 4), new ItemStack(ModBlocks.PATH_BLOCK, 1, 4)));
        GameRegistry.addRecipe(new ShapelessBloodOrbRecipe(new ItemStack(ModBlocks.PATH_BLOCK, 4, 6), Blocks.OBSIDIAN, Blocks.OBSIDIAN, Blocks.OBSIDIAN, Blocks.OBSIDIAN, OrbRegistry.getOrbStack(ModItems.ORB_ARCHMAGE)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.PATH_BLOCK, 4, 7), new ItemStack(ModBlocks.PATH_BLOCK, 1, 6), new ItemStack(ModBlocks.PATH_BLOCK, 1, 6), new ItemStack(ModBlocks.PATH_BLOCK, 1, 6), new ItemStack(ModBlocks.PATH_BLOCK, 1, 6)));

        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModItems.EXPERIENCE_TOME), "ses", "lbl", "gog", 'o', OrbRegistry.getOrbStack(ModItems.ORB_MAGICIAN), 'e', Blocks.LAPIS_BLOCK, 'l', new ItemStack(ModItems.SLATE, 1, 2), 'b', Items.ENCHANTED_BOOK, 's', Items.STRING, 'g', "ingotGold"));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModItems.RITUAL_READER), "gog", "isi", " s ", 's', new ItemStack(ModItems.SLATE, 1, 3), 'g', "blockGlass", 'i', "ingotGold", 'o', OrbRegistry.getOrbStack(ModItems.ORB_MASTER)));

        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.ALCHEMY_TABLE), "sss", "wbw", "gog", 's', "stone", 'w', "plankWood", 'b', Items.BLAZE_ROD, 'g', "ingotGold", 'o', OrbRegistry.getOrbStack(ModItems.ORB_WEAK)));

        for (int i = 1; i < EnumBloodRune.values().length; i++)
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.BLOOD_RUNE), new ItemStack(ModBlocks.BLOOD_RUNE, 1, i)));

        for (int i = 0; i < ItemSoulGem.names.length; i++)
        {
            for (int j = 0; j < ItemDemonCrystal.getNames().size(); j++)
            {
                ItemStack baseGemStack = new ItemStack(ModItems.SOUL_GEM, 1, i);
                ItemStack newGemStack = new ItemStack(ModItems.SOUL_GEM, 1, i);

                ItemStack crystalStack = new ItemStack(ModItems.ITEM_DEMON_CRYSTAL, 1, j);

                ((ItemSoulGem) ModItems.SOUL_GEM).setCurrentType(((ItemDemonCrystal) ModItems.ITEM_DEMON_CRYSTAL).getType(crystalStack), newGemStack);
                GameRegistry.addShapelessRecipe(newGemStack, baseGemStack, crystalStack);
            }
        }

        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.MIMIC, 4, 0), "b b", " r ", "bob", 'b', new ItemStack(ModBlocks.BLOOD_STONE), 'r', new ItemStack(ModBlocks.BLOOD_RUNE), 'o', OrbRegistry.getOrbStack(ModItems.ORB_MAGICIAN)));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.MIMIC, 4, 1), "bsb", "srs", "bob", 'b', new ItemStack(ModBlocks.BLOOD_STONE), 'r', new ItemStack(ModBlocks.BLOOD_RUNE), 's', "stone", 'o', OrbRegistry.getOrbStack(ModItems.ORB_MAGICIAN)));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.MIMIC, 4, 2), "bsb", "srs", "bob", 'b', new ItemStack(ModBlocks.BLOOD_STONE), 'r', new ItemStack(ModBlocks.BLOOD_RUNE), 's', "blockGlass", 'o', OrbRegistry.getOrbStack(ModItems.ORB_MAGICIAN)));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.MIMIC, 2, 3), "bnb", "trt", "bob", 'b', new ItemStack(ModBlocks.BLOOD_STONE), 'r', new ItemStack(ModBlocks.BLOOD_RUNE), 'n', Blocks.GLOWSTONE, 't', "torch", 'o', OrbRegistry.getOrbStack(ModItems.ORB_MAGICIAN)));

        for (int i = 0; i < 5; i++)
        {
            ItemStack crystalStack = new ItemStack(ModItems.ITEM_DEMON_CRYSTAL, 1, i);
            ItemStack baseStoneStack = new ItemStack(ModBlocks.DEMON_EXTRAS, 1, i);
            ItemStack baseStoneStackCrafted = new ItemStack(ModBlocks.DEMON_EXTRAS, 16, i);
            ItemStack polishedStoneStack = new ItemStack(ModBlocks.DEMON_EXTRAS, 1, i + 5);
            ItemStack willBrickStack = new ItemStack(ModBlocks.DEMON_BRICK_1, 1, i);
            ItemStack willBrickStackCrafted = new ItemStack(ModBlocks.DEMON_BRICK_1, 4, i);

            ItemStack willSmallBrickStack = new ItemStack(ModBlocks.DEMON_BRICK_2, 1, i);
            ItemStack willSmallBrickStackCrafted = new ItemStack(ModBlocks.DEMON_BRICK_2, 1, i);
            ItemStack pillarStack = new ItemStack(ModBlocks.DEMON_PILLAR_1, 1, i);
            ItemStack pillarStackCrafted = new ItemStack(ModBlocks.DEMON_PILLAR_1, 6, i);

            GameRegistry.addRecipe(new ShapelessOreRecipe(baseStoneStackCrafted, crystalStack, "stone", "stone", "stone", "stone", "stone", "stone", "stone", "stone"));
            GameRegistry.addRecipe(willBrickStackCrafted, "ss", "ss", 's', baseStoneStack);
            GameRegistry.addRecipe(willSmallBrickStackCrafted, "ss", "ss", 's', willBrickStack);
            GameRegistry.addRecipe(new ItemStack(ModBlocks.DEMON_BRICK_2, 9, i + 5), "scs", "ccc", "scs", 's', baseStoneStack, 'c', willBrickStack);
            GameRegistry.addRecipe(new ItemStack(ModBlocks.DEMON_BRICK_2, 9, i + 10), "scs", "coc", "scs", 's', baseStoneStack, 'c', willBrickStack, 'o', crystalStack);
            GameRegistry.addRecipe(pillarStackCrafted, "ss", "ss", "ss", 's', polishedStoneStack);
            GameRegistry.addRecipe(new ItemStack(ModBlocks.DEMON_PILLAR_2, 8, i), "ppp", "pcp", "ppp", 'p', pillarStack, 'c', crystalStack);

            GameRegistry.addRecipe(new ItemStack(ModBlocks.DEMON_WALL_1, 6, i), "sss", "sss", 's', willBrickStack);
            GameRegistry.addRecipe(new ItemStack(ModBlocks.DEMON_WALL_1, 6, i + 5), "sss", "sss", 's', willSmallBrickStack);
            GameRegistry.addRecipe(new ItemStack(ModBlocks.DEMON_WALL_1, 6, i + 10), "sss", "sss", 's', polishedStoneStack);

            GameRegistry.addRecipe(new ItemStack(i < 2 ? ModBlocks.DEMON_STAIRS_1 : (i < 4 ? ModBlocks.DEMON_STAIRS_2 : ModBlocks.DEMON_STAIRS_3), 8, i % 2), "s  ", "ss ", "sss", 's', polishedStoneStack);
            GameRegistry.addRecipe(new ItemStack(i < 2 ? ModBlocks.DEMON_PILLAR_CAP_1 : (i < 4 ? ModBlocks.DEMON_PILLAR_CAP_2 : ModBlocks.DEMON_PILLAR_CAP_3), 6, i % 2), "sss", "sss", 's', pillarStack);

            GameRegistry.addRecipe(new ItemStack(ModBlocks.DEMON_LIGHT, 5, i), "sgs", "ggg", "sgs", 's', polishedStoneStack, 'g', Blocks.GLOWSTONE);

            GameRegistry.addSmelting(baseStoneStack, polishedStoneStack, 0.15f);
        }
    }

    public static void addAltarRecipes()
    {
        // ONE
        AltarRecipeRegistry.registerFillRecipe(OrbRegistry.getOrbStack(ModItems.ORB_WEAK), EnumAltarTier.ONE, ModItems.ORB_WEAK.getCapacity(), 2, 1);
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.DIAMOND), OrbRegistry.getOrbStack(ModItems.ORB_WEAK), EnumAltarTier.ONE, 2000, 2, 1));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Blocks.STONE), new ItemStack(ModItems.SLATE), EnumAltarTier.ONE, 1000, 5, 5));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.BUCKET), new ItemStack(ForgeModContainer.getInstance().universalBucket), EnumAltarTier.ONE, 1000, 5, 0));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.BOOK), new ItemStack(ModItems.SANGUINE_BOOK), EnumAltarTier.ONE, 1000, 20, 0));

        // TWO
        AltarRecipeRegistry.registerFillRecipe(OrbRegistry.getOrbStack(ModItems.ORB_APPRENTICE), EnumAltarTier.TWO, ModItems.ORB_APPRENTICE.getCapacity(), 5, 5);
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Blocks.REDSTONE_BLOCK), OrbRegistry.getOrbStack(ModItems.ORB_APPRENTICE), EnumAltarTier.TWO, 5000, 5, 5));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModItems.SLATE), new ItemStack(ModItems.SLATE, 1, 1), EnumAltarTier.TWO, 2000, 5, 5));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.IRON_SWORD), new ItemStack(ModItems.DAGGER_OF_SACRIFICE), EnumAltarTier.TWO, 3000, 5, 5));

        // THREE
        AltarRecipeRegistry.registerFillRecipe(OrbRegistry.getOrbStack(ModItems.ORB_MAGICIAN), EnumAltarTier.THREE, ModItems.ORB_MAGICIAN.getCapacity(), 15, 15);
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Blocks.GOLD_BLOCK), OrbRegistry.getOrbStack(ModItems.ORB_MAGICIAN), EnumAltarTier.THREE, 25000, 20, 20));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModItems.SLATE, 1, 1), new ItemStack(ModItems.SLATE, 1, 2), EnumAltarTier.THREE, 5000, 15, 10));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Blocks.OBSIDIAN), EnumRuneType.EARTH.getScribeStack(), EnumAltarTier.THREE, 1000, 5, 5));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Blocks.LAPIS_BLOCK), EnumRuneType.WATER.getScribeStack(), EnumAltarTier.THREE, 1000, 5, 5));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.MAGMA_CREAM), EnumRuneType.FIRE.getScribeStack(), EnumAltarTier.THREE, 1000, 5, 5));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.GHAST_TEAR), EnumRuneType.AIR.getScribeStack(), EnumAltarTier.THREE, 1000, 5, 5));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModItems.LAVA_CRYSTAL), new ItemStack(ModItems.ACTIVATION_CRYSTAL), EnumAltarTier.THREE, 10000, 20, 10));

        // FOUR
        AltarRecipeRegistry.registerFillRecipe(OrbRegistry.getOrbStack(ModItems.ORB_MASTER), EnumAltarTier.FOUR, ModItems.ORB_MASTER.getCapacity(), 25, 25);
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModItems.BLOOD_SHARD), OrbRegistry.getOrbStack(ModItems.ORB_MASTER), EnumAltarTier.FOUR, 25000, 30, 50));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModItems.SLATE, 1, 2), new ItemStack(ModItems.SLATE, 1, 3), EnumAltarTier.FOUR, 15000, 20, 20));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Blocks.COAL_BLOCK), EnumRuneType.DUSK.getScribeStack(), EnumAltarTier.FOUR, 2000, 20, 10));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.ENDER_PEARL), new ItemStack(ModItems.TELEPOSITION_FOCUS), EnumAltarTier.FOUR, 2000, 10, 10));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModItems.TELEPOSITION_FOCUS), new ItemStack(ModItems.TELEPOSITION_FOCUS, 1, 1), EnumAltarTier.FOUR, 10000, 20, 10));

        // FIVE
        AltarRecipeRegistry.registerFillRecipe(OrbRegistry.getOrbStack(ModItems.ORB_ARCHMAGE), EnumAltarTier.FIVE, ModItems.ORB_ARCHMAGE.getCapacity(), 50, 50);
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.NETHER_STAR), OrbRegistry.getOrbStack(ModItems.ORB_ARCHMAGE), EnumAltarTier.FIVE, 80000, 50, 100));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModItems.SLATE, 1, 3), new ItemStack(ModItems.SLATE, 1, 4), EnumAltarTier.FIVE, 30000, 40, 100));

        // SIX
        AltarRecipeRegistry.registerFillRecipe(OrbRegistry.getOrbStack(ModItems.ORB_TRANSCENDENT), EnumAltarTier.SIX, ModItems.ORB_TRANSCENDENT.getCapacity(), 50, 50);
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(ModBlocks.CRYSTAL), OrbRegistry.getOrbStack(ModItems.ORB_TRANSCENDENT), EnumAltarTier.SIX, 200000, 100, 200));
        AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Blocks.GLOWSTONE), EnumRuneType.DAWN.getScribeStack(), EnumAltarTier.SIX, 200000, 100, 200));
    }

    public static void addAlchemyArrayRecipes()
    {
        AlchemyArrayRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), new ItemStack(Items.DIAMOND_SWORD), new AlchemyArrayEffectBinding("boundSword", Utils.setUnbreakable(new ItemStack(ModItems.BOUND_SWORD))), new BindingAlchemyCircleRenderer());
        AlchemyArrayRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), new ItemStack(Items.DIAMOND_AXE), new AlchemyArrayEffectBinding("boundAxe", Utils.setUnbreakable(new ItemStack(ModItems.BOUND_AXE))));
        AlchemyArrayRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), new ItemStack(Items.DIAMOND_PICKAXE), new AlchemyArrayEffectBinding("boundPickaxe", Utils.setUnbreakable(new ItemStack(ModItems.BOUND_PICKAXE))));
        AlchemyArrayRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), new ItemStack(Items.DIAMOND_SHOVEL), new AlchemyArrayEffectBinding("boundShovel", Utils.setUnbreakable(new ItemStack(ModItems.BOUND_SHOVEL))));
        AlchemyArrayRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), new ItemStack(Items.IRON_HELMET), new AlchemyArrayEffectBinding("livingHelmet", new ItemStack(ModItems.LIVING_ARMOUR_HELMET)));
        AlchemyArrayRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), new ItemStack(Items.IRON_CHESTPLATE), new AlchemyArrayEffectBinding("livingChest", new ItemStack(ModItems.LIVING_ARMOUR_CHEST)));
        AlchemyArrayRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), new ItemStack(Items.IRON_LEGGINGS), new AlchemyArrayEffectBinding("livingLegs", new ItemStack(ModItems.LIVING_ARMOUR_LEGS)));
        AlchemyArrayRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), new ItemStack(Items.IRON_BOOTS), new AlchemyArrayEffectBinding("livingBoots", new ItemStack(ModItems.LIVING_ARMOUR_BOOTS)));

        AlchemyArrayRecipeRegistry.registerCraftingRecipe(new ItemStack(Items.REDSTONE), new ItemStack(ModItems.SLATE), new ItemStack(ModItems.SIGIL_DIVINATION), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/DivinationSigil.png"));

        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_WATER), new ItemStack(ModItems.SLATE), new ItemStack(ModItems.SIGIL_WATER), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/WaterSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_LAVA), new ItemStack(ModItems.SLATE), new ItemStack(ModItems.SIGIL_LAVA), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/LavaSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_AIR), new ItemStack(ModItems.SLATE, 1, 1), new ItemStack(ModItems.SIGIL_AIR), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/AirSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_FASTMINER), new ItemStack(ModItems.SLATE, 1, 1), new ItemStack(ModItems.SIGIL_FAST_MINER), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/FastMinerSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_VOID), new ItemStack(ModItems.SLATE, 1, 1), new ItemStack(ModItems.SIGIL_VOID), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/VoidSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_GROWTH), new ItemStack(ModItems.SLATE, 1, 1), new ItemStack(ModItems.SIGIL_GREEN_GROVE), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/GrowthSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_AFFINITY), new ItemStack(ModItems.SLATE, 1, 2), new ItemStack(ModItems.SIGIL_ELEMENTAL_AFFINITY), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/ElementalAffinitySigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_SIGHT), new ItemStack(ModItems.SLATE, 1, 1), new ItemStack(ModItems.SIGIL_SEER), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/SightSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_HOLDING), new ItemStack(ModItems.SLATE, 1, 2), new ItemStack(ModItems.SIGIL_HOLDING), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/WIPArray.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BLOODLIGHT), new ItemStack(ModItems.SLATE, 1, 2), new ItemStack(ModItems.SIGIL_BLOOD_LIGHT), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/LightSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_MAGNETISM), new ItemStack(ModItems.SLATE, 1, 2), new ItemStack(ModItems.SIGIL_MAGNETISM), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/MagnetismSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_SUPPRESSION), new ItemStack(ModItems.SLATE, 1, 3), new ItemStack(ModItems.SIGIL_SUPPRESSION), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/SuppressionSigil.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_HASTE), new ItemStack(ModItems.SLATE, 1, 3), new ItemStack(ModItems.SIGIL_HASTE), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/WIPArray.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BRIDGE), new ItemStack(ModItems.SLATE, 1, 3), new ItemStack(ModItems.SIGIL_PHANTOM_BRIDGE), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/WIPArray.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_COMPRESSION), new ItemStack(ModItems.SLATE, 1, 3), new ItemStack(ModItems.SIGIL_COMPRESSION), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/WIPArray.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_SEVERANCE), new ItemStack(ModItems.SLATE, 1, 3), new ItemStack(ModItems.SIGIL_ENDER_SEVERANCE), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/WIPArray.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_TELEPOSITION), new ItemStack(ModItems.SLATE, 1, 3), new ItemStack(ModItems.SIGIL_TELEPOSITION), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/WIPArray.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_TRANSPOSITION), new ItemStack(ModItems.SLATE, 1, 3), new ItemStack(ModItems.SIGIL_TRANSPOSITION), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/WIPArray.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_CLAW), new ItemStack(ModItems.SLATE, 1, 2), new ItemStack(ModItems.SIGIL_CLAW), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/WIPArray.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BOUNCE), new ItemStack(ModItems.SLATE, 1, 1), new ItemStack(ModItems.SIGIL_BOUNCE), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/WIPArray.png"));
        AlchemyArrayRecipeRegistry.registerCraftingRecipe(ItemComponent.getStack(ItemComponent.REAGENT_FROST), new ItemStack(ModItems.SLATE, 1, 1), new ItemStack(ModItems.SIGIL_FROST), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/WIPArray.png"));

        AlchemyArrayRecipeRegistry.registerRecipe(new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.ROTTEN_FLESH), new AlchemyArrayEffectAttractor("attractor"), new AttractorAlchemyCircleRenderer());
        AlchemyArrayRecipeRegistry.registerRecipe(new ItemStack(Items.FEATHER), new ItemStack(Items.REDSTONE), new AlchemyArrayEffectMovement("movement"), new StaticAlchemyCircleRenderer(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/MovementArray.png")));
        AlchemyArrayRecipeRegistry.registerRecipe(new ItemStack(Items.FEATHER), new ItemStack(Items.GLOWSTONE_DUST), new AlchemyArrayEffectUpdraft("updraft"), new AttractorAlchemyCircleRenderer(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/UpdraftArray.png")));
        AlchemyArrayRecipeRegistry.registerRecipe(new ItemStack(Items.SLIME_BALL), new ItemStack(Items.REDSTONE), new AlchemyArrayEffectBounce("bounce"), new SingleAlchemyCircleRenderer(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/BounceArray.png")));

        AlchemyArrayRecipeRegistry.registerRecipe(new ItemStack(Items.ARROW), new ItemStack(Items.FEATHER), new AlchemyArrayEffectSkeletonTurret("skeletonTurret"), new DualAlchemyCircleRenderer(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/SkeletonTurret1.png"), new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/SkeletonTurret2.png")));

        AlchemyArrayRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_FASTMINER), new ItemStack(Items.IRON_PICKAXE), new AlchemyArrayEffectSigil("fastMiner", (ISigil) ModItems.SIGIL_FAST_MINER), new SingleAlchemyCircleRenderer(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/FastMinerSigil.png")));

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
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.SOUL_GEM), 1, 1, "dustRedstone", "ingotGold", "blockGlass", "dyeBlue");
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.SOUL_GEM, 1, 1), 60, 20, new ItemStack(ModItems.SOUL_GEM), "gemDiamond", "blockRedstone", "blockLapis");
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.SOUL_GEM, 1, 2), 240, 50, new ItemStack(ModItems.SOUL_GEM, 1, 1), "gemDiamond", "blockGold", new ItemStack(ModItems.SLATE, 1, 2));
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.SOUL_GEM, 1, 3), 1000, 100, new ItemStack(ModItems.SOUL_GEM, 1, 2), new ItemStack(ModItems.SLATE, 1, 3), new ItemStack(ModItems.BLOOD_SHARD), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DEFAULT));
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.SOUL_GEM, 1, 4), 4000, 500, new ItemStack(ModItems.SOUL_GEM, 1, 3), Items.NETHER_STAR);
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.SENTIENT_SWORD), 0, 0, new ItemStack(ModItems.SOUL_GEM), new ItemStack(Items.IRON_SWORD));
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.SENTIENT_AXE), 0, 0, new ItemStack(ModItems.SOUL_GEM), new ItemStack(Items.IRON_AXE));
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.SENTIENT_PICKAXE), 0, 0, new ItemStack(ModItems.SOUL_GEM), new ItemStack(Items.IRON_PICKAXE));
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.SENTIENT_SHOVEL), 0, 0, new ItemStack(ModItems.SOUL_GEM), new ItemStack(Items.IRON_SHOVEL));
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.SENTIENT_BOW), 70, 0, new ItemStack(Items.BOW), new ItemStack(ModItems.SOUL_GEM, 1, 1), Items.STRING, Items.STRING);
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.ARCANE_ASHES), 0, 0, "dustRedstone", "dyeWhite", new ItemStack(Items.GUNPOWDER), Items.COAL);
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_WATER), 10, 3, new ItemStack(Items.SUGAR), new ItemStack(Items.WATER_BUCKET), new ItemStack(Items.WATER_BUCKET));
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_LAVA), 32, 10, Items.LAVA_BUCKET, "dustRedstone", "cobblestone", "blockCoal");
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_VOID), 64, 10, Items.BUCKET, Items.STRING, Items.STRING, Items.GUNPOWDER);
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_GROWTH), 128, 20, "treeSapling", "treeSapling", Items.REEDS, Items.SUGAR);
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_AIR), 128, 20, Items.GHAST_TEAR, Items.FEATHER, Items.FEATHER);
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_SIGHT), 64, 0, ModItems.SIGIL_DIVINATION, "blockGlass", "blockGlass", "dustGlowstone");
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_HOLDING), 64, 20, Blocks.CHEST, "leather", "string", "string");
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_FASTMINER), 128, 10, Items.IRON_PICKAXE, Items.IRON_AXE, Items.IRON_SHOVEL, Items.GUNPOWDER);
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_AFFINITY), 300, 30, ModItems.SIGIL_WATER, ModItems.SIGIL_AIR, ModItems.SIGIL_LAVA, Blocks.OBSIDIAN);
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_SUPPRESSION), 500, 50, ModBlocks.TELEPOSER, Items.WATER_BUCKET, Items.LAVA_BUCKET, Items.BLAZE_ROD);
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BINDING), 400, 10, "dustGlowstone", "dustRedstone", "nuggetGold", Items.GUNPOWDER);
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BLOODLIGHT), 300, 10, "glowstone", Blocks.TORCH, "dustRedstone", "dustRedstone");
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_MAGNETISM), 600, 10, Items.STRING, "ingotGold", "blockIron", "ingotGold");
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_HASTE), 1400, 100, Items.COOKIE, Items.SUGAR, Items.COOKIE, "stone");
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BRIDGE), 600, 50, Blocks.SOUL_SAND, Blocks.SOUL_SAND, "stone", Blocks.OBSIDIAN);
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_SEVERANCE), 800, 70, Items.ENDER_EYE, Items.ENDER_PEARL, "ingotGold", "ingotGold");
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_COMPRESSION), 2000, 200, "blockIron", "blockGold", Blocks.OBSIDIAN, "cobblestone");
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_TELEPOSITION), 1500, 200, ModBlocks.TELEPOSER, "glowstone", "blockRedstone", "ingotGold");
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_TRANSPOSITION), 1500, 200, ModBlocks.TELEPOSER, "gemDiamond", Items.ENDER_PEARL, Blocks.OBSIDIAN);
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_CLAW), 800, 120, Items.FLINT, Items.FLINT, ItemCuttingFluid.getStack(ItemCuttingFluid.BASIC));
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_BOUNCE), 200, 20, Blocks.SLIME_BLOCK, Blocks.SLIME_BLOCK, Items.LEATHER, Items.STRING);
        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.REAGENT_FROST), 80, 10, Blocks.ICE, Items.SNOWBALL, Items.SNOWBALL, "dustRedstone");

        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.SENTIENT_ARMOUR_GEM), 240, 150, Items.DIAMOND_CHESTPLATE, new ItemStack(ModItems.SOUL_GEM, 1, 1), Blocks.IRON_BLOCK, Blocks.OBSIDIAN);

        TartaricForgeRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.COMPONENT_FRAME_PART), 400, 10, "blockGlass", "stone", new ItemStack(ModItems.SLATE));
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.NODE_ROUTER), 400, 5, "stickWood", new ItemStack(ModItems.SLATE, 1, 1), "gemLapis", "gemLapis");
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModBlocks.ITEM_ROUTING_NODE), 400, 5, "dustGlowstone", "dustRedstone", "blockGlass", "stone");
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModBlocks.OUTPUT_ROUTING_NODE), 400, 25, "dustGlowstone", "dustRedstone", "ingotIron", new ItemStack(ModBlocks.ITEM_ROUTING_NODE));
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModBlocks.INPUT_ROUTING_NODE), 400, 25, "dustGlowstone", "dustRedstone", "ingotGold", new ItemStack(ModBlocks.ITEM_ROUTING_NODE));
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModBlocks.MASTER_ROUTING_NODE), 400, 200, "blockIron", "gemDiamond", new ItemStack(ModItems.SLATE, 1, 2));

        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModBlocks.DEMON_CRYSTAL, 1, 0), 1200, 100, ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DEFAULT), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DEFAULT), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DEFAULT), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DEFAULT));
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModBlocks.DEMON_CRYSTAL, 1, 1), 1200, 100, ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_CORROSIVE), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_CORROSIVE), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_CORROSIVE), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_CORROSIVE));
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModBlocks.DEMON_CRYSTAL, 1, 2), 1200, 100, ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DESTRUCTIVE), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DESTRUCTIVE), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DESTRUCTIVE), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DESTRUCTIVE));
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModBlocks.DEMON_CRYSTAL, 1, 3), 1200, 100, ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_VENGEFUL), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_VENGEFUL), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_VENGEFUL), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_VENGEFUL));
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModBlocks.DEMON_CRYSTAL, 1, 4), 1200, 100, ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_STEADFAST), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_STEADFAST), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_STEADFAST), ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_STEADFAST));

        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModBlocks.DEMON_CRUCIBLE), 400, 100, Items.CAULDRON, "stone", "gemLapis", "gemDiamond");
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModBlocks.DEMON_PYLON), 400, 50, "blockIron", "stone", "gemLapis", ModItems.ITEM_DEMON_CRYSTAL);
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModBlocks.DEMON_CRYSTALLIZER), 500, 100, ModBlocks.SOUL_FORGE, "stone", "gemLapis", "blockGlass");
        TartaricForgeRecipeRegistry.registerRecipe(new ItemStack(ModItems.DEMON_WILL_GAUGE), 400, 50, "ingotGold", "dustRedstone", "blockGlass", ModItems.ITEM_DEMON_CRYSTAL);
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

        AlchemyTableRecipeRegistry.registerRecipe(ItemLivingArmourPointsUpgrade.getStack(ItemLivingArmourPointsUpgrade.DRAFT_ANGELUS), 20000, 400, 3, ItemComponent.getStack(ItemComponent.NEURO_TOXIN), ItemComponent.getStack(ItemComponent.ANTISEPTIC), "dustGold", Items.FERMENTED_SPIDER_EYE, new ItemStack(ModItems.BLOOD_SHARD, 1, 0), Items.GHAST_TEAR);

        AlchemyTableRecipeRegistry.registerRecipe(new AlchemyTableDyeableRecipe(0, 100, 0, new ItemStack(ModItems.SIGIL_HOLDING)));

        AlchemyTableRecipeRegistry.registerRecipe(new ItemStack(ModItems.POTION_FLASK), 1000, 200, 2, new ItemStack(Items.POTIONITEM), Items.NETHER_WART, Items.REDSTONE, Items.GLOWSTONE_DUST);
        AlchemyTableRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.CATALYST_LENGTH_1), 1000, 100, 2, Items.GUNPOWDER, Items.NETHER_WART, "gemLapis");
        AlchemyTableRecipeRegistry.registerRecipe(ItemComponent.getStack(ItemComponent.CATALYST_POWER_1), 1000, 100, 2, Items.GUNPOWDER, Items.NETHER_WART, "dustRedstone");
    }

    public static void addOreDoublingAlchemyRecipes()
    {
        String[] oreList = OreDictionary.getOreNames().clone();
        for (String ore : oreList)
        {
            if (ore.startsWith("ore") && !addedOreRecipeList.contains(ore))
            {
                String dustName = ore.replaceFirst("ore", "dust");

                List<ItemStack> discoveredOres = OreDictionary.getOres(ore);
                List<ItemStack> dustList = OreDictionary.getOres(dustName);
                if (dustList != null && !dustList.isEmpty() && discoveredOres != null && !discoveredOres.isEmpty())
                {
                    ItemStack dustStack = dustList.get(0).copy();
                    dustStack.stackSize = 2;
                    AlchemyTableRecipeRegistry.registerRecipe(new AlchemyTableCustomRecipe(dustStack, 400, 200, 1, ore, ItemCuttingFluid.getStack(ItemCuttingFluid.BASIC)));
                    addedOreRecipeList.add(ore);
                }
            }
        }
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
        addPotionRecipe(1000, 1, new ItemStack(ModItems.BLOOD_SHARD, 1, 0), new PotionEffect(MobEffects.HEALTH_BOOST, 2 * 60 * 20));
        addPotionRecipe(1000, 1, new ItemStack(Blocks.SLIME_BLOCK), new PotionEffect(ModPotions.bounce, 2 * 60 * 20));
        addPotionRecipe(1000, 1, new ItemStack(Items.STRING), new PotionEffect(ModPotions.cling, 2 * 60 * 20));

        addPotionRecipe(1000, 1, new ItemStack(Items.BEETROOT), new PotionEffect(ModPotions.deafness, 450));

//        AlchemyTableRecipeRegistry.registerRecipe(new AlchemyTablePotionRecipe(5000, 100, 4, new ItemStack(Blocks.SLIME_BLOCK), new PotionEffect(ModPotions.bounce, 15 * 60 * 20)));
//        AlchemyTableRecipeRegistry.registerRecipe(new AlchemyTablePotionRecipe(5000, 100, 4, new ItemStack(Items.STRING), new PotionEffect(ModPotions.bounce, 15 * 60 * 20)));
    }

    static ItemStack mundaneLengtheningStack = ItemComponent.getStack(ItemComponent.CATALYST_LENGTH_1);
    static ItemStack mundanePowerStack = ItemComponent.getStack(ItemComponent.CATALYST_POWER_1);

    public static void addPotionRecipe(int lpDrained, int tier, ItemStack inputStack, PotionEffect baseEffect)
    {
        AlchemyTableRecipeRegistry.registerRecipe(new AlchemyTablePotionRecipe(lpDrained, 100, tier, inputStack, baseEffect));

        List<ItemStack> lengtheningList = new ArrayList<ItemStack>();
        lengtheningList.add(inputStack);
        lengtheningList.add(mundaneLengtheningStack);
        AlchemyTableRecipeRegistry.registerRecipe(BMPotionUtils.getLengthAugmentRecipe(lpDrained, 100, tier, lengtheningList, baseEffect, 1));

        List<ItemStack> powerList = new ArrayList<ItemStack>();
        powerList.add(inputStack);
        powerList.add(mundanePowerStack);
        AlchemyTableRecipeRegistry.registerRecipe(BMPotionUtils.getPowerAugmentRecipe(lpDrained, 100, tier, powerList, baseEffect, 1));
    }

    public static void addLivingArmourDowngradeRecipes()
    {
        String messageBase = "ritual.BloodMagic.downgradeRitual.dialogue.";

        ItemStack bowStack = new ItemStack(Items.BOW);
        ItemStack bottleStack = new ItemStack(Items.POTIONITEM, 1, 0);
        ItemStack swordStack = new ItemStack(Items.STONE_SWORD);
        ItemStack goldenAppleStack = new ItemStack(Items.GOLDEN_APPLE);
        ItemStack fleshStack = new ItemStack(Items.ROTTEN_FLESH);
        ItemStack shieldStack = new ItemStack(Items.SHIELD);
        ItemStack pickStack = new ItemStack(Items.STONE_PICKAXE);
        ItemStack minecartStack = new ItemStack(Items.MINECART);
        ItemStack stringStack = new ItemStack(Items.STRING);

        Map<ItemStack, Pair<String, int[]>> dialogueMap = new HashMap<ItemStack, Pair<String, int[]>>();
        dialogueMap.put(bowStack, Pair.of("bow", new int[] { 1, 100, 300, 500 }));
        dialogueMap.put(bottleStack, Pair.of("quenched", new int[] { 1, 100, 300, 500 }));
        dialogueMap.put(swordStack, Pair.of("dulledBlade", new int[] { 1, 100, 300, 500, 700 }));
        dialogueMap.put(goldenAppleStack, Pair.of("slowHeal", new int[] { 1, 100, 300, 500, 700 }));

        for (Entry<ItemStack, Pair<String, int[]>> entry : dialogueMap.entrySet())
        {
            ItemStack keyStack = entry.getKey();
            String str = entry.getValue().getKey();
            Map<Integer, List<ITextComponent>> textMap = new HashMap<Integer, List<ITextComponent>>();
            for (int tick : entry.getValue().getValue())
            {
                List<ITextComponent> textList = new ArrayList<ITextComponent>();
                textList.add(new TextComponentTranslation("\u00A74%s", new TextComponentTranslation(messageBase + str + "." + tick)));
                textMap.put(tick, textList);
            }

            LivingArmourDowngradeRecipeRegistry.registerDialog(keyStack, textMap);
        }

        LivingArmourDowngradeRecipeRegistry.registerRecipe(new LivingArmourUpgradeStormTrooper(0), bowStack, Items.ARROW, Items.STRING, "ingotIron", "ingotIron");
        LivingArmourDowngradeRecipeRegistry.registerRecipe(new LivingArmourUpgradeStormTrooper(1), bowStack, Items.SPECTRAL_ARROW, "ingotGold", "dustRedstone", "dustGlowstone", "gemLapis");
        LivingArmourDowngradeRecipeRegistry.registerRecipe(new LivingArmourUpgradeStormTrooper(2), bowStack, "gemDiamond", Items.FIRE_CHARGE, Items.BLAZE_ROD, Items.FEATHER);
        LivingArmourDowngradeRecipeRegistry.registerRecipe(new LivingArmourUpgradeStormTrooper(3), bowStack, Items.PRISMARINE_SHARD, Items.BLAZE_ROD, Items.FEATHER, Items.FEATHER);
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
            LivingArmourDowngradeRecipeRegistry.registerRecipe(upgrade, stack, "ingotIron", new ItemStack(ModItems.SLATE, 1, 0));
            break;
        case 1:
            LivingArmourDowngradeRecipeRegistry.registerRecipe(upgrade, stack, "dustRedstone", "dustRedstone", "ingotIron", new ItemStack(ModItems.SLATE, 1, 0));
            break;
        case 2:
            LivingArmourDowngradeRecipeRegistry.registerRecipe(upgrade, stack, "ingotGold", "gemLapis", "gemLapis", new ItemStack(ModItems.SLATE, 1, 1));
            break;
        case 3:
            LivingArmourDowngradeRecipeRegistry.registerRecipe(upgrade, stack, Blocks.VINE, "dyeRed", Items.GOLDEN_CARROT, new ItemStack(ModItems.SLATE, 1, 1));
            break;
        case 4:
            LivingArmourDowngradeRecipeRegistry.registerRecipe(upgrade, stack, Items.GOLDEN_APPLE, "treeSapling", "treeSapling", new ItemStack(ModItems.SLATE, 1, 2));
            break;
        case 5:
            LivingArmourDowngradeRecipeRegistry.registerRecipe(upgrade, stack, Blocks.IRON_BLOCK, Blocks.REDSTONE_BLOCK, new ItemStack(ModItems.SLATE, 1, 2));
            break;
        case 6:
            LivingArmourDowngradeRecipeRegistry.registerRecipe(upgrade, stack, Blocks.IRON_BLOCK, Blocks.GLOWSTONE, "ingotGold", "ingotGold", new ItemStack(ModItems.SLATE, 1, 3));
            break;
        case 7:
            LivingArmourDowngradeRecipeRegistry.registerRecipe(upgrade, stack, Blocks.GOLD_BLOCK, Blocks.LAPIS_BLOCK, "gemDiamond", new ItemStack(ModItems.SLATE, 1, 3));
            break;
        case 8:
            LivingArmourDowngradeRecipeRegistry.registerRecipe(upgrade, stack, Items.DRAGON_BREATH, "gemDiamond", new ItemStack(ModItems.SLATE, 1, 4));
            break;
        case 9:
            LivingArmourDowngradeRecipeRegistry.registerRecipe(upgrade, stack, Items.NETHER_STAR, "gemDiamond", "gemDiamond", new ItemStack(ModItems.SLATE, 1, 4));
        }
    }
}
