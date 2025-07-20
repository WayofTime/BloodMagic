package wayoftime.bloodmagic.common.data;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.data.recipe.BaseRecipeProvider;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.recipe.*;
import wayoftime.bloodmagic.common.tags.BloodMagicTags;
import wayoftime.bloodmagic.core.recipe.IngredientBloodOrb;

public class GeneratorRecipes extends BaseRecipeProvider
{
	public GeneratorRecipes(PackOutput output)
	{
		super(output, BloodMagic.MODID);
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> consumer)
	{
		super.buildRecipes(consumer);
		addVanillaRecipes(consumer);
		addVanillaSmithingRecipes(consumer);
		addBloodOrbRecipes(consumer);
	}

	@Override
	protected List<ISubRecipeProvider> getSubRecipeProviders() {
		return Arrays.asList(new BloodAltarRecipeProvider(), new AlchemyArrayRecipeProvider(), new TartaricForgeRecipeProvider(), new ARCRecipeProvider(), new AlchemyTableRecipeProvider(), new LivingDowngradeRecipeProvider(), new PotionRecipeProvider(), new MeteorRecipeProvider());
	}

	private void addVanillaRecipes(Consumer<FinishedRecipe> consumer)
	{
		// TODO: REDO CATEGORIES
//		ConditionalRecipe.builder().addRecipe(ShapedRecipeBuilder.shapedRecipe(BloodMagicItems.SACRIFICIAL_DAGGER.get()).key('g', Tags.Items.GLASS).key('G', Tags.Items.INGOTS_GOLD).key('i', Tags.Items.INGOTS_IRON).patternLine("ggg").patternLine(" Gg").patternLine("i g").addCriterion("has_glass", hasItem(Items.GLASS))::build);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicItems.CORRUPTED_DUST.get()).define('s', BloodMagicTags.TINYDUST_CORRUPTED).pattern("sss").pattern("sss").pattern("sss").unlockedBy("has_tiny", has(BloodMagicItems.CORRUPTED_DUST_TINY.get())).save(consumer, BloodMagic.rl("corrupted_dust"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicItems.HELLFORGED_BLOCK.get()).define('s', BloodMagicTags.INGOT_HELLFORGED).pattern("sss").pattern("sss").pattern("sss").unlockedBy("has_hellforged", has(BloodMagicItems.HELLFORGED_INGOT.get())).save(consumer, BloodMagic.rl("hellforged_block"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicItems.SACRIFICIAL_DAGGER.get()).define('g', Tags.Items.GLASS).define('G', Tags.Items.INGOTS_GOLD).define('i', Tags.Items.INGOTS_IRON).pattern("ggg").pattern(" Gg").pattern("i g").unlockedBy("has_glass", has(Items.GLASS)).save(consumer, BloodMagic.rl("sacrificial_dagger"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicItems.BASE_RITUAL_DIVINER.get()).define('a', BloodMagicItems.AIR_INSCRIPTION_TOOL.get()).define('s', Tags.Items.RODS_WOODEN).define('d', Tags.Items.GEMS_DIAMOND).define('e', BloodMagicItems.EARTH_INSCRIPTION_TOOL.get()).define('f', BloodMagicItems.FIRE_INSCRIPTION_TOOL.get()).define('w', BloodMagicItems.WATER_INSCRIPTION_TOOL.get()).pattern("dfd").pattern("ase").pattern("dwd").unlockedBy("has_scribe", has(BloodMagicItems.AIR_INSCRIPTION_TOOL.get())).save(consumer, BloodMagic.rl("ritual_diviner_0"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicItems.DUSK_RITUAL_DIVINER.get()).define('S', BloodMagicItems.DEMONIC_SLATE.get()).define('t', BloodMagicItems.DUSK_INSCRIPTION_TOOL.get()).define('d', BloodMagicItems.BASE_RITUAL_DIVINER.get()).pattern(" S ").pattern("tdt").pattern(" S ").unlockedBy("has_demon_slate", has(BloodMagicItems.DEMONIC_SLATE.get())).save(consumer, BloodMagic.rl("ritual_diviner_1"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicBlocks.BLOODSTONE_BRICK.get(), 4).define('s', BloodMagicBlocks.BLOODSTONE.get()).pattern("ss").pattern("ss").unlockedBy("has_weak_shard", has(BloodMagicItems.WEAK_BLOOD_SHARD.get())).save(consumer, BloodMagic.rl("bloodstonebrick"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,BloodMagicBlocks.BLOODSTONE.get(), 8).requires(Tags.Items.STONE).requires(BloodMagicItems.WEAK_BLOOD_SHARD.get()).unlockedBy("has_weak_shard", has(BloodMagicItems.WEAK_BLOOD_SHARD.get())).save(consumer, BloodMagic.rl("largebloodstonebrick"));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,BloodMagicBlocks.WOOD_TILE_PATH.get(), 4).requires(BloodMagicBlocks.WOOD_PATH.get()).requires(BloodMagicBlocks.WOOD_PATH.get()).requires(BloodMagicBlocks.WOOD_PATH.get()).requires(BloodMagicBlocks.WOOD_PATH.get()).unlockedBy("has_apprentice_orb", has(BloodMagicItems.APPRENTICE_BLOOD_ORB.get())).save(consumer, BloodMagic.rl("path/path_woodtile"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,BloodMagicBlocks.STONE_TILE_PATH.get(), 4).requires(BloodMagicBlocks.STONE_PATH.get()).requires(BloodMagicBlocks.STONE_PATH.get()).requires(BloodMagicBlocks.STONE_PATH.get()).requires(BloodMagicBlocks.STONE_PATH.get()).unlockedBy("has_magician_orb", has(BloodMagicItems.MAGICIAN_BLOOD_ORB.get())).save(consumer, BloodMagic.rl("path/path_stonetile"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,BloodMagicBlocks.WORN_STONE_TILE_PATH.get(), 4).requires(BloodMagicBlocks.WORN_STONE_PATH.get()).requires(BloodMagicBlocks.WORN_STONE_PATH.get()).requires(BloodMagicBlocks.WORN_STONE_PATH.get()).requires(BloodMagicBlocks.WORN_STONE_PATH.get()).unlockedBy("has_master_orb", has(BloodMagicItems.MASTER_BLOOD_ORB.get())).save(consumer, BloodMagic.rl("path/path_wornstonetile"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,BloodMagicBlocks.OBSIDIAN_TILE_PATH.get(), 4).requires(BloodMagicBlocks.OBSIDIAN_PATH.get()).requires(BloodMagicBlocks.OBSIDIAN_PATH.get()).requires(BloodMagicBlocks.OBSIDIAN_PATH.get()).requires(BloodMagicBlocks.OBSIDIAN_PATH.get()).unlockedBy("has_archmage_orb", has(BloodMagicItems.ARCHMAGE_BLOOD_ORB.get())).save(consumer, BloodMagic.rl("path/path_obsidiantile"));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,BloodMagicItems.ALCHEMY_FLASK.get()).requires(BloodMagicItems.ALCHEMY_FLASK.get()).requires(Ingredient.of(Items.WATER_BUCKET)).unlockedBy("has_flask", has(BloodMagicItems.ALCHEMY_FLASK.get())).save(consumer, BloodMagic.rl("alchemy_flask"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,BloodMagicItems.ALCHEMY_FLASK_THROWABLE.get()).requires(BloodMagicItems.ALCHEMY_FLASK_THROWABLE.get()).requires(Ingredient.of(Items.WATER_BUCKET)).unlockedBy("has_flask", has(BloodMagicItems.ALCHEMY_FLASK.get())).save(consumer, BloodMagic.rl("alchemy_flask_throwable"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,BloodMagicItems.ALCHEMY_FLASK_LINGERING.get()).requires(BloodMagicItems.ALCHEMY_FLASK_LINGERING.get()).requires(Ingredient.of(Items.WATER_BUCKET)).unlockedBy("has_flask", has(BloodMagicItems.ALCHEMY_FLASK.get())).save(consumer, BloodMagic.rl("alchemy_flask_lingering"));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,BloodMagicItems.HELLFORGED_INGOT.get(), 9).requires(BloodMagicBlocks.HELLFORGED_BLOCK.get()).unlockedBy("has_hellforged_block", has(BloodMagicBlocks.HELLFORGED_BLOCK.get())).save(consumer, BloodMagic.rl("hellforged_block_to_ingot"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicBlocks.TELEPOSER.get()).pattern("ggg").pattern("ete").pattern("ggg").define('g', Ingredient.of(Tags.Items.INGOTS_GOLD)).define('e', Ingredient.of(Tags.Items.ENDER_PEARLS)).define('t', Ingredient.of(BloodMagicItems.TELEPOSER_FOCUS.get())).unlockedBy("has_gold", has(BloodMagicItems.TELEPOSER_FOCUS.get())).save(consumer, BloodMagic.rl("teleposer"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,BloodMagicItems.REINFORCED_TELEPOSER_FOCUS.get()).requires(BloodMagicItems.ENHANCED_TELEPOSER_FOCUS.get()).requires(BloodMagicItems.WEAK_BLOOD_SHARD.get()).unlockedBy("has_shard", has(BloodMagicItems.WEAK_BLOOD_SHARD.get())).save(consumer, BloodMagic.rl("enhanced_teleposer_focus"));

		// Changed Recipes
		{
//			ShapedRecipeBuilder.shapedRecipe(BloodMagicBlocks.BLOOD_ALTAR.get()).key('a', Tags.Items.STONE).key('b', Items.FURNACE).key('c', Tags.Items.INGOTS_GOLD).key('d', BloodMagicItems.MONSTER_SOUL_RAW.get()).patternLine("a a").patternLine("aba").patternLine("cdc").addCriterion("has_will", hasItem(BloodMagicItems.MONSTER_SOUL_RAW.get())).build(consumer, BloodMagic.rl("blood_altar"));
//			ShapedRecipeBuilder.shapedRecipe(BloodMagicItems.SOUL_SNARE.get(), 4).key('r', Tags.Items.DUSTS_REDSTONE).key('s', Tags.Items.STRING).key('i', Tags.Items.INGOTS_IRON).patternLine("sis").patternLine("iri").patternLine("sis").addCriterion("has_redstone", hasItem(Items.REDSTONE)).build(consumer, BloodMagic.rl("soul_snare"));
//			ShapedRecipeBuilder.shapedRecipe(BloodMagicBlocks.SOUL_FORGE.get()).key('s', Tags.Items.STONE).key('g', Tags.Items.INGOTS_GOLD).key('i', Tags.Items.INGOTS_IRON).key('o', Tags.Items.STORAGE_BLOCKS_IRON).patternLine("i i").patternLine("sgs").patternLine("sos").addCriterion("has_gold", hasItem(Items.GOLD_INGOT)).build(consumer, BloodMagic.rl("soul_forge"));
		}
		{
			ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicBlocks.BLOOD_ALTAR.get()).define('a', Tags.Items.STONE).define('b', Items.FURNACE).define('c', Tags.Items.INGOTS_GOLD).pattern("a a").pattern("aba").pattern("ccc").unlockedBy("has_gold", has(Items.GOLD_INGOT)).save(consumer, BloodMagic.rl("blood_altar"));
			ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicBlocks.ALCHEMY_TABLE.get()).define('b', Tags.Items.INGOTS_IRON).define('s', Tags.Items.STONE).define('w', ItemTags.PLANKS).define('g', Tags.Items.INGOTS_GOLD).define('o', BloodMagicItems.SLATE.get()).pattern("sss").pattern("wbw").pattern("gog").unlockedBy("has_blank_slate", has(BloodMagicItems.SLATE.get())).save(consumer, BloodMagic.rl("alchemy_table"));
			ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicBlocks.SOUL_FORGE.get()).define('s', Tags.Items.STONE).define('S', BloodMagicItems.SLATE.get()).define('i', Tags.Items.INGOTS_IRON).define('o', Tags.Items.STORAGE_BLOCKS_IRON).pattern("i i").pattern("sSs").pattern("sos").unlockedBy("has_blank_slate", has(BloodMagicItems.SLATE.get())).save(consumer, BloodMagic.rl("soul_forge"));
		}

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicItems.SYNTHETIC_POINT.get(), 2).pattern("ifi").pattern("frf").pattern("ifi").define('f', Ingredient.of(Items.ROTTEN_FLESH, Items.PORKCHOP, Items.MUTTON, Items.BEEF, Items.TROPICAL_FISH, Items.COD, Items.PUFFERFISH, Items.SALMON, Items.CHICKEN, Items.SPIDER_EYE, Items.RABBIT, BloodMagicItems.STRONG_TAU_ITEM.get())).define('i', Tags.Items.NUGGETS_IRON).define('r', Tags.Items.DUSTS_REDSTONE).unlockedBy("has_nugget", has(Items.IRON_NUGGET)).save(consumer, BloodMagic.rl("synthetic_point"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicBlocks.RAW_HELLFORGED_BLOCK.get()).define('s', BloodMagicItems.DEMONITE_RAW.get()).pattern("sss").pattern("sss").pattern("sss").unlockedBy("has_raw_hellforged", has(BloodMagicItems.DEMONITE_RAW.get())).save(consumer, BloodMagic.rl("raw_hellforged_block"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,BloodMagicItems.DEMONITE_RAW.get(), 9).requires(BloodMagicBlocks.RAW_HELLFORGED_BLOCK.get()).unlockedBy("has_raw_hellforged_block", has(BloodMagicBlocks.RAW_HELLFORGED_BLOCK.get())).save(consumer, BloodMagic.rl("raw_hellforged_block_to_item"));

	}

	private void addVanillaSmithingRecipes(Consumer<FinishedRecipe> consumer)
	{
		String basePath = "smelting/";
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(BloodMagicTags.DUST_IRON),RecipeCategory.MISC, Items.IRON_INGOT, 0, 200).unlockedBy("has_iron_sand", has(BloodMagicItems.IRON_SAND.get())).save(consumer, BloodMagic.rl(basePath + "ingot_iron"));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(BloodMagicTags.DUST_GOLD),RecipeCategory.MISC, Items.GOLD_INGOT, 0, 200).unlockedBy("has_gold_sand", has(BloodMagicItems.GOLD_SAND.get())).save(consumer, BloodMagic.rl(basePath + "ingot_gold"));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(BloodMagicTags.DUST_COPPER),RecipeCategory.MISC, Items.COPPER_INGOT, 0, 200).unlockedBy("has_copper_sand", has(BloodMagicItems.COPPER_SAND.get())).save(consumer, BloodMagic.rl(basePath + "ingot_copper"));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(BloodMagicTags.DUST_NETHERITE_SCRAP),RecipeCategory.MISC, Items.NETHERITE_SCRAP, 0, 200).unlockedBy("has_netherite_dust", has(BloodMagicItems.NETHERITE_SCRAP_SAND.get())).save(consumer, BloodMagic.rl(basePath + "ingot_netherite_scrap"));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(BloodMagicTags.DUST_HELLFORGED),RecipeCategory.MISC, BloodMagicItems.HELLFORGED_INGOT.get(), 0, 200).unlockedBy("has_hellforged_dust", has(BloodMagicItems.HELLFORGED_SAND.get())).save(consumer, BloodMagic.rl(basePath + "ingot_hellforged"));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(BloodMagicItems.DEMONITE_RAW.get()),RecipeCategory.MISC, BloodMagicItems.HELLFORGED_INGOT.get(), 0, 200).unlockedBy("has_raw_demonite", has(BloodMagicItems.DEMONITE_RAW.get())).save(consumer, BloodMagic.rl(basePath + "ingot_from_raw_hellforged"));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(BloodMagicBlocks.DUNGEON_ORE.get()),RecipeCategory.MISC, BloodMagicItems.HELLFORGED_INGOT.get(), 0, 200).unlockedBy("has_demonite", has(BloodMagicBlocks.DUNGEON_ORE.get())).save(consumer, BloodMagic.rl(basePath + "ingot_from_demonite"));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(BloodMagicItems.PLANT_OIL.get()),RecipeCategory.MISC, BloodMagicItems.SALTPETER.get(), 0, 200).unlockedBy("has_plant_oil", has(BloodMagicItems.PLANT_OIL.get())).save(consumer, BloodMagic.rl(basePath + "saltpeter"));

		SimpleCookingRecipeBuilder.blasting(Ingredient.of(BloodMagicTags.DUST_IRON),RecipeCategory.MISC, Items.IRON_INGOT, 0, 100).unlockedBy("has_iron_sand", has(BloodMagicItems.IRON_SAND.get())).save(consumer, BloodMagic.rl(basePath + "blasting_ingot_iron"));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(BloodMagicTags.DUST_GOLD),RecipeCategory.MISC, Items.GOLD_INGOT, 0, 100).unlockedBy("has_gold_sand", has(BloodMagicItems.GOLD_SAND.get())).save(consumer, BloodMagic.rl(basePath + "blasting_ingot_gold"));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(BloodMagicTags.DUST_COPPER),RecipeCategory.MISC, Items.COPPER_INGOT, 0, 100).unlockedBy("has_copper_sand", has(BloodMagicItems.COPPER_SAND.get())).save(consumer, BloodMagic.rl(basePath + "blasting_ingot_copper"));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(BloodMagicTags.DUST_NETHERITE_SCRAP),RecipeCategory.MISC, Items.NETHERITE_SCRAP, 0, 100).unlockedBy("has_netherite_dust", has(BloodMagicItems.NETHERITE_SCRAP_SAND.get())).save(consumer, BloodMagic.rl(basePath + "blasting_ingot_netherite_scrap"));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(BloodMagicTags.DUST_HELLFORGED),RecipeCategory.MISC, BloodMagicItems.HELLFORGED_INGOT.get(), 0, 100).unlockedBy("has_hellforged_dust", has(BloodMagicItems.HELLFORGED_SAND.get())).save(consumer, BloodMagic.rl(basePath + "blasting_ingot_hellforged"));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(BloodMagicItems.DEMONITE_RAW.get()),RecipeCategory.MISC, BloodMagicItems.HELLFORGED_INGOT.get(), 0, 100).unlockedBy("has_raw_demonite", has(BloodMagicItems.DEMONITE_RAW.get())).save(consumer, BloodMagic.rl(basePath + "blasting_ingot_from_raw_hellforged"));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(BloodMagicBlocks.DUNGEON_ORE.get()),RecipeCategory.MISC, BloodMagicItems.HELLFORGED_INGOT.get(), 0, 100).unlockedBy("has_demonite", has(BloodMagicBlocks.DUNGEON_ORE.get())).save(consumer, BloodMagic.rl(basePath + "blasting_ingot_from_demonite"));

	}

	private void addBloodOrbRecipes(Consumer<FinishedRecipe> consumer)
	{
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicBlocks.INCENSE_ALTAR.get()).define('s', Tags.Items.STONE).define('c', Tags.Items.COBBLESTONE).define('h', Items.CHARCOAL).define('o', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_WEAK.get())).pattern("s s").pattern("shs").pattern("coc").unlockedBy("has_weak_orb", has(BloodMagicItems.WEAK_BLOOD_ORB.get())).save(consumer, BloodMagic.rl("incense_altar"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicBlocks.BLANK_RUNE.get()).define('a', Tags.Items.STONE).define('s', Ingredient.of(BloodMagicItems.SLATE.get())).define('o', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_WEAK.get())).pattern("asa").pattern("aoa").pattern("aaa").unlockedBy("has_weak_orb", has(BloodMagicItems.WEAK_BLOOD_ORB.get())).save(consumer, BloodMagic.rl("blood_rune_blank"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicBlocks.SPEED_RUNE.get()).define('a', Tags.Items.STONE).define('b', Ingredient.of(BloodMagicItems.SLATE.get())).define('c', Ingredient.of(Items.SUGAR)).define('d', BloodMagicBlocks.BLANK_RUNE.get()).pattern("aba").pattern("cdc").pattern("aba").unlockedBy("has_blank_rune", has(BloodMagicItems.BLANK_RUNE_ITEM.get())).save(consumer, BloodMagic.rl("blood_rune_speed"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicBlocks.SACRIFICE_RUNE.get()).define('a', Tags.Items.STONE).define('b', BloodMagicItems.REINFORCED_SLATE.get()).define('c', Tags.Items.INGOTS_GOLD).define('d', BloodMagicBlocks.BLANK_RUNE.get()).define('e', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_APPRENTICE.get())).pattern("aba").pattern("cdc").pattern("aea").unlockedBy("has_apprentice_orb", has(BloodMagicItems.APPRENTICE_BLOOD_ORB.get())).save(consumer, BloodMagic.rl("blood_rune_sacrifice"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicBlocks.SELF_SACRIFICE_RUNE.get()).define('a', Tags.Items.STONE).define('b', Ingredient.of(BloodMagicItems.REINFORCED_SLATE.get())).define('c', Ingredient.of(Items.GLOWSTONE_DUST)).define('d', Ingredient.of(BloodMagicItems.BLANK_RUNE_ITEM.get())).define('e', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_APPRENTICE.get())).pattern("aba").pattern("cdc").pattern("aea").unlockedBy("has_apprentice_orb", has(BloodMagicItems.APPRENTICE_BLOOD_ORB.get())).save(consumer, BloodMagic.rl("blood_rune_self_sacrifice"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicBlocks.CAPACITY_RUNE.get()).define('a', Tags.Items.STONE).define('b', Items.BUCKET).define('c', BloodMagicBlocks.BLANK_RUNE.get()).define('d', BloodMagicItems.IMBUED_SLATE.get()).pattern("aba").pattern("bcb").pattern("ada").unlockedBy("has_imbued_slate", has(BloodMagicItems.IMBUED_SLATE.get())).save(consumer, BloodMagic.rl("blood_rune_capacity"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicBlocks.ORB_RUNE.get()).define('a', Tags.Items.STONE).define('b', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_WEAK.get())).define('c', BloodMagicBlocks.BLANK_RUNE.get()).define('d', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_MASTER.get())).pattern("aba").pattern("cdc").pattern("aba").unlockedBy("has_master_orb", has(BloodMagicItems.MASTER_BLOOD_ORB.get())).save(consumer, BloodMagic.rl("blood_rune_orb"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicBlocks.CHARGING_RUNE.get()).define('R', Tags.Items.DUSTS_REDSTONE).define('r', BloodMagicBlocks.BLANK_RUNE.get()).define('s', BloodMagicItems.DEMONIC_SLATE.get()).define('e', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_MASTER.get())).define('G', Tags.Items.DUSTS_GLOWSTONE).pattern("RsR").pattern("GrG").pattern("ReR").unlockedBy("has_master_orb", has(BloodMagicItems.MASTER_BLOOD_ORB.get())).save(consumer, BloodMagic.rl("blood_rune_charging"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicBlocks.DISPLACEMENT_RUNE.get()).define('a', Tags.Items.STONE).define('b', Items.WATER_BUCKET).define('c', BloodMagicBlocks.BLANK_RUNE.get()).define('d', BloodMagicItems.IMBUED_SLATE.get()).pattern("aba").pattern("bcb").pattern("ada").unlockedBy("has_imbued_slate", has(BloodMagicItems.IMBUED_SLATE.get())).save(consumer, BloodMagic.rl("blood_rune_displacement"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicBlocks.AUGMENTED_CAPACITY_RUNE.get()).define('a', Tags.Items.OBSIDIAN).define('b', BloodMagicItems.DEMONIC_SLATE.get()).define('c', Items.BUCKET).define('d', BloodMagicBlocks.CAPACITY_RUNE.get()).define('e', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_MASTER.get())).pattern("aba").pattern("cdc").pattern("aea").unlockedBy("has_master_orb", has(BloodMagicItems.MASTER_BLOOD_ORB.get())).save(consumer, BloodMagic.rl("blood_rune_aug_capacity"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicBlocks.ACCELERATION_RUNE.get()).define('a', Items.BUCKET).define('b', BloodMagicItems.DEMONIC_SLATE.get()).define('c', Tags.Items.INGOTS_GOLD).define('d', BloodMagicBlocks.SPEED_RUNE.get()).define('e', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_MASTER.get())).pattern("aba").pattern("cdc").pattern("aea").unlockedBy("has_master_orb", has(BloodMagicItems.MASTER_BLOOD_ORB.get())).save(consumer, BloodMagic.rl("blood_rune_acceleration"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicBlocks.BLANK_RITUAL_STONE.get(), 4).define('a', Tags.Items.OBSIDIAN).define('b', BloodMagicItems.REINFORCED_SLATE.get()).define('c', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_APPRENTICE.get())).pattern("aba").pattern("bcb").pattern("aba").unlockedBy("has_apprentice_orb", has(BloodMagicItems.APPRENTICE_BLOOD_ORB.get())).save(consumer, BloodMagic.rl("ritual_stone_blank"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicBlocks.MASTER_RITUAL_STONE.get()).define('a', Tags.Items.OBSIDIAN).define('b', BloodMagicBlocks.BLANK_RITUAL_STONE.get()).define('c', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_MAGICIAN.get())).pattern("aba").pattern("bcb").pattern("aba").unlockedBy("has_magician_orb", has(BloodMagicItems.MAGICIAN_BLOOD_ORB.get())).save(consumer, BloodMagic.rl("ritual_stone_master"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicBlocks.ALCHEMICAL_REACTION_CHAMBER.get()).define('s', Tags.Items.STONE).define('f', Blocks.FURNACE).define('o', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_MAGICIAN.get())).define('I', Tags.Items.STORAGE_BLOCKS_IRON).define('S', BloodMagicItems.IMBUED_SLATE.get()).pattern("sss").pattern("SoS").pattern("IfI").unlockedBy("has_magician_orb", has(BloodMagicItems.MAGICIAN_BLOOD_ORB.get())).save(consumer, BloodMagic.rl("arc"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicItems.PRIMITIVE_FURNACE_CELL.get()).define('c', Tags.Items.COBBLESTONE).define('f', Tags.Items.STORAGE_BLOCKS_COAL).define('s', Ingredient.of(BloodMagicItems.SLATE.get())).define('o', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_MAGICIAN.get())).pattern("csc").pattern("cfc").pattern("coc").unlockedBy("has_magician_orb", has(BloodMagicItems.MAGICIAN_BLOOD_ORB.get())).save(consumer, BloodMagic.rl("primitive_furnace_cell"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicItems.LAVA_CRYSTAL.get()).define('a', Tags.Items.GLASS).define('b', Items.LAVA_BUCKET).define('c', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_WEAK.get())).define('d', Tags.Items.OBSIDIAN).define('e', Tags.Items.GEMS_DIAMOND).pattern("aba").pattern("bcb").pattern("ded").unlockedBy("has_weak_orb", has(BloodMagicItems.WEAK_BLOOD_ORB.get())).save(consumer, BloodMagic.rl("lava_crystal"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicItems.PRIMITIVE_HYDRATION_CELL.get()).define('B', Items.WATER_BUCKET).define('c', Tags.Items.COBBLESTONE).define('o', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_MAGICIAN.get())).define('s', BloodMagicItems.SLATE.get()).pattern("csc").pattern("cBc").pattern("coc").unlockedBy("has_magician_orb", has(BloodMagicItems.MAGICIAN_BLOOD_ORB.get())).save(consumer, BloodMagic.rl("primitive_hydration_cell"));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,BloodMagicBlocks.WOOD_PATH.get(), 4).requires(ItemTags.PLANKS).requires(ItemTags.PLANKS).requires(ItemTags.PLANKS).requires(ItemTags.PLANKS).requires(IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_APPRENTICE.get())).unlockedBy("has_apprentice_orb", has(BloodMagicItems.APPRENTICE_BLOOD_ORB.get())).save(consumer, BloodMagic.rl("path/path_wood"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,BloodMagicBlocks.STONE_PATH.get(), 4).requires(Tags.Items.STONE).requires(Tags.Items.STONE).requires(Tags.Items.STONE).requires(Tags.Items.STONE).requires(IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_MAGICIAN.get())).unlockedBy("has_magician_orb", has(BloodMagicItems.MAGICIAN_BLOOD_ORB.get())).save(consumer, BloodMagic.rl("path/path_stone"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,BloodMagicBlocks.WORN_STONE_PATH.get(), 4).requires(BloodMagicBlocks.STONE_PATH.get()).requires(BloodMagicBlocks.STONE_PATH.get()).requires(BloodMagicBlocks.STONE_PATH.get()).requires(BloodMagicBlocks.STONE_PATH.get()).requires(IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_MASTER.get())).unlockedBy("has_master_orb", has(BloodMagicItems.MASTER_BLOOD_ORB.get())).save(consumer, BloodMagic.rl("path/path_wornstone"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,BloodMagicBlocks.OBSIDIAN_PATH.get(), 4).requires(Blocks.OBSIDIAN).requires(Blocks.OBSIDIAN).requires(Blocks.OBSIDIAN).requires(Blocks.OBSIDIAN).requires(IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_ARCHMAGE.get())).unlockedBy("has_archmage_orb", has(BloodMagicItems.ARCHMAGE_BLOOD_ORB.get())).save(consumer, BloodMagic.rl("path/path_obsidian"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicItems.RITUAL_READER.get()).define('s', BloodMagicItems.DEMONIC_SLATE.get()).define('g', Tags.Items.GLASS).define('i', Tags.Items.INGOTS_GOLD).define('o', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_MASTER.get())).pattern("gog").pattern("isi").pattern(" s ").unlockedBy("has_master_orb", has(BloodMagicItems.MASTER_BLOOD_ORB.get())).save(consumer, BloodMagic.rl("ritual_reader"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicItems.EXPERIENCE_TOME.get()).define('b', Items.ENCHANTED_BOOK).define('s', Tags.Items.STRING).define('e', Tags.Items.STORAGE_BLOCKS_LAPIS).define('g', Tags.Items.INGOTS_GOLD).define('l', BloodMagicItems.IMBUED_SLATE.get()).define('o', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_MAGICIAN.get())).pattern("ses").pattern("lbl").pattern("gog").unlockedBy("has_magician_orb", has(BloodMagicItems.MAGICIAN_BLOOD_ORB.get())).save(consumer, BloodMagic.rl("experience_tome"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicBlocks.SPEED_RUNE_2.get()).pattern("sps").pattern("drd").pattern("sos").define('r', BloodMagicBlocks.SPEED_RUNE.get()).define('s', Items.NETHERITE_SCRAP).define('p', BloodMagicItems.HELLFORGED_PARTS.get()).define('d', BloodMagicItems.BLOODSTONE_ITEM.get()).define('o', BloodMagicItems.ETHEREAL_SLATE.get()).unlockedBy("has_hellforged_parts", has(BloodMagicItems.HELLFORGED_PARTS.get())).save(consumer, BloodMagic.rl("blood_rune_speed_2"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicBlocks.ACCELERATION_RUNE_2.get()).pattern("sps").pattern("drd").pattern("sos").define('r', BloodMagicBlocks.ACCELERATION_RUNE.get()).define('s', Items.NETHERITE_SCRAP).define('p', BloodMagicItems.HELLFORGED_PARTS.get()).define('d', BloodMagicItems.BLOODSTONE_ITEM.get()).define('o', BloodMagicItems.ETHEREAL_SLATE.get()).unlockedBy("has_hellforged_parts", has(BloodMagicItems.HELLFORGED_PARTS.get())).save(consumer, BloodMagic.rl("blood_rune_acceleration_2"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicBlocks.AUGMENTED_CAPACITY_RUNE_2.get()).pattern("sps").pattern("drd").pattern("sos").define('r', BloodMagicBlocks.AUGMENTED_CAPACITY_RUNE.get()).define('s', Items.NETHERITE_SCRAP).define('p', BloodMagicItems.HELLFORGED_PARTS.get()).define('d', BloodMagicItems.BLOODSTONE_ITEM.get()).define('o', BloodMagicItems.ETHEREAL_SLATE.get()).unlockedBy("has_hellforged_parts", has(BloodMagicItems.HELLFORGED_PARTS.get())).save(consumer, BloodMagic.rl("blood_rune_aug_capacity_2"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicBlocks.CAPACITY_RUNE_2.get()).pattern("sps").pattern("drd").pattern("sos").define('r', BloodMagicBlocks.CAPACITY_RUNE.get()).define('s', Items.NETHERITE_SCRAP).define('p', BloodMagicItems.HELLFORGED_PARTS.get()).define('d', BloodMagicItems.BLOODSTONE_ITEM.get()).define('o', BloodMagicItems.ETHEREAL_SLATE.get()).unlockedBy("has_hellforged_parts", has(BloodMagicItems.HELLFORGED_PARTS.get())).save(consumer, BloodMagic.rl("blood_rune_capacity_2"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicBlocks.CHARGING_RUNE_2.get()).pattern("sps").pattern("drd").pattern("sos").define('r', BloodMagicBlocks.CHARGING_RUNE.get()).define('s', Items.NETHERITE_SCRAP).define('p', BloodMagicItems.HELLFORGED_PARTS.get()).define('d', BloodMagicItems.BLOODSTONE_ITEM.get()).define('o', BloodMagicItems.ETHEREAL_SLATE.get()).unlockedBy("has_hellforged_parts", has(BloodMagicItems.HELLFORGED_PARTS.get())).save(consumer, BloodMagic.rl("blood_rune_charging_2"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicBlocks.DISPLACEMENT_RUNE_2.get()).pattern("sps").pattern("drd").pattern("sos").define('r', BloodMagicBlocks.DISPLACEMENT_RUNE.get()).define('s', Items.NETHERITE_SCRAP).define('p', BloodMagicItems.HELLFORGED_PARTS.get()).define('d', BloodMagicItems.BLOODSTONE_ITEM.get()).define('o', BloodMagicItems.ETHEREAL_SLATE.get()).unlockedBy("has_hellforged_parts", has(BloodMagicItems.HELLFORGED_PARTS.get())).save(consumer, BloodMagic.rl("blood_rune_displacement_2"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicBlocks.ORB_RUNE_2.get()).pattern("sps").pattern("drd").pattern("sos").define('r', BloodMagicBlocks.ORB_RUNE.get()).define('s', Items.NETHERITE_SCRAP).define('p', BloodMagicItems.HELLFORGED_PARTS.get()).define('d', BloodMagicItems.BLOODSTONE_ITEM.get()).define('o', BloodMagicItems.ETHEREAL_SLATE.get()).unlockedBy("has_hellforged_parts", has(BloodMagicItems.HELLFORGED_PARTS.get())).save(consumer, BloodMagic.rl("blood_rune_orb_2"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicBlocks.SELF_SACRIFICE_RUNE_2.get()).pattern("sps").pattern("drd").pattern("sos").define('r', BloodMagicBlocks.SELF_SACRIFICE_RUNE.get()).define('s', Items.NETHERITE_SCRAP).define('p', BloodMagicItems.HELLFORGED_PARTS.get()).define('d', BloodMagicItems.BLOODSTONE_ITEM.get()).define('o', BloodMagicItems.ETHEREAL_SLATE.get()).unlockedBy("has_hellforged_parts", has(BloodMagicItems.HELLFORGED_PARTS.get())).save(consumer, BloodMagic.rl("blood_rune_self_sac_2"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BloodMagicBlocks.SACRIFICE_RUNE_2.get()).pattern("sps").pattern("drd").pattern("sos").define('r', BloodMagicBlocks.SACRIFICE_RUNE.get()).define('s', Items.NETHERITE_SCRAP).define('p', BloodMagicItems.HELLFORGED_PARTS.get()).define('d', BloodMagicItems.BLOODSTONE_ITEM.get()).define('o', BloodMagicItems.ETHEREAL_SLATE.get()).unlockedBy("has_hellforged_parts", has(BloodMagicItems.HELLFORGED_PARTS.get())).save(consumer, BloodMagic.rl("blood_rune_sac_2"));

//		ShapedRecipeBuilder.shapedRecipe(BloodMagicBlocks.MIMIC.get()).key('b', itemIn)
//		ShapedRecipeBuilder.shapedRecipe(BloodMagicBlocks.SPEED_RUNE.get()).key('s', Items.GLASS).key('o', Ingredient.fromItems(Items.DIAMOND)).patternLine("sss").patternLine("sos").patternLine("sss").addCriterion("has_diamond", hasItem(Items.DIAMOND)).build(consumer, new ResourceLocation(BloodMagic.MODID, "speed_rune_from_standard"));
//		ShapedRecipeBuilder.shapedRecipe(BloodMagicBlocks.SPEED_RUNE.get()).key('s', Items.GLASS).key('o', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_WEAK.get())).patternLine("sss").patternLine("sos").patternLine("sss").addCriterion("has_diamond", hasItem(Items.DIAMOND)).build(consumer, new ResourceLocation(BloodMagic.MODID, "speed_rune_from_orb"));

		// Changed Recipes
		{
//			ShapedRecipeBuilder.shapedRecipe(BloodMagicBlocks.ALCHEMY_TABLE.get()).key('b', Tags.Items.RODS_BLAZE).key('s', Tags.Items.STONE).key('w', ItemTags.PLANKS).key('g', Tags.Items.INGOTS_GOLD).key('o', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_WEAK.get())).patternLine("sss").patternLine("wbw").patternLine("gog").addCriterion("has_weak_orb", hasItem(BloodMagicItems.WEAK_BLOOD_ORB.get())).build(consumer, BloodMagic.rl("alchemy_table"));

		}
		{

		}
	}
}
