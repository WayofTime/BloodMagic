package wayoftime.bloodmagic.common.data;

import java.util.function.Consumer;

import net.minecraft.block.Blocks;
import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.data.recipe.BaseRecipeProvider;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.tags.BloodMagicTags;
import wayoftime.bloodmagic.core.recipe.IngredientBloodOrb;

public class GeneratorBaseRecipes extends BaseRecipeProvider
{
	public GeneratorBaseRecipes(DataGenerator gen)
	{
		super(gen, BloodMagic.MODID);
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer)
	{
		addVanillaRecipes(consumer);
		addVanillaSmithingRecipes(consumer);
		addBloodOrbRecipes(consumer);
	}

	private void addVanillaRecipes(Consumer<IFinishedRecipe> consumer)
	{
//		ConditionalRecipe.builder().addRecipe(ShapedRecipeBuilder.shapedRecipe(BloodMagicItems.SACRIFICIAL_DAGGER.get()).key('g', Tags.Items.GLASS).key('G', Tags.Items.INGOTS_GOLD).key('i', Tags.Items.INGOTS_IRON).patternLine("ggg").patternLine(" Gg").patternLine("i g").addCriterion("has_glass", hasItem(Items.GLASS))::build);
		ShapedRecipeBuilder.shapedRecipe(BloodMagicItems.CORRUPTED_DUST.get()).key('s', BloodMagicTags.TINYDUST_CORRUPTED).patternLine("sss").patternLine("sss").patternLine("sss").addCriterion("has_tiny", hasItem(BloodMagicItems.CORRUPTED_DUST_TINY.get())).build(consumer, BloodMagic.rl("corrupted_dust"));
		ShapedRecipeBuilder.shapedRecipe(BloodMagicItems.HELLFORGED_BLOCK.get()).key('s', BloodMagicTags.INGOT_HELLFORGED).patternLine("sss").patternLine("sss").patternLine("sss").addCriterion("has_hellforged", hasItem(BloodMagicItems.HELLFORGED_INGOT.get())).build(consumer, BloodMagic.rl("hellforged_block"));
		ShapedRecipeBuilder.shapedRecipe(BloodMagicItems.SACRIFICIAL_DAGGER.get()).key('g', Tags.Items.GLASS).key('G', Tags.Items.INGOTS_GOLD).key('i', Tags.Items.INGOTS_IRON).patternLine("ggg").patternLine(" Gg").patternLine("i g").addCriterion("has_glass", hasItem(Items.GLASS)).build(consumer, BloodMagic.rl("sacrificial_dagger"));
		ShapedRecipeBuilder.shapedRecipe(BloodMagicItems.BASE_RITUAL_DIVINER.get()).key('a', BloodMagicItems.AIR_INSCRIPTION_TOOL.get()).key('s', Tags.Items.RODS_WOODEN).key('d', Tags.Items.GEMS_DIAMOND).key('e', BloodMagicItems.EARTH_INSCRIPTION_TOOL.get()).key('f', BloodMagicItems.FIRE_INSCRIPTION_TOOL.get()).key('w', BloodMagicItems.WATER_INSCRIPTION_TOOL.get()).patternLine("dfd").patternLine("ase").patternLine("dwd").addCriterion("has_scribe", hasItem(BloodMagicItems.AIR_INSCRIPTION_TOOL.get())).build(consumer, BloodMagic.rl("ritual_diviner_0"));
		ShapedRecipeBuilder.shapedRecipe(BloodMagicItems.DUSK_RITUAL_DIVINER.get()).key('S', BloodMagicItems.DEMONIC_SLATE.get()).key('t', BloodMagicItems.DUSK_INSCRIPTION_TOOL.get()).key('d', BloodMagicItems.BASE_RITUAL_DIVINER.get()).patternLine(" S ").patternLine("tdt").patternLine(" S ").addCriterion("has_demon_slate", hasItem(BloodMagicItems.DEMONIC_SLATE.get())).build(consumer, BloodMagic.rl("ritual_diviner_1"));
		ShapedRecipeBuilder.shapedRecipe(BloodMagicBlocks.BLOODSTONE_BRICK.get(), 4).key('s', BloodMagicBlocks.BLOODSTONE.get()).patternLine("ss").patternLine("ss").addCriterion("has_weak_shard", hasItem(BloodMagicItems.WEAK_BLOOD_SHARD.get())).build(consumer, BloodMagic.rl("bloodstonebrick"));
		ShapelessRecipeBuilder.shapelessRecipe(BloodMagicBlocks.BLOODSTONE.get(), 8).addIngredient(Tags.Items.STONE).addIngredient(BloodMagicItems.WEAK_BLOOD_SHARD.get()).addCriterion("has_weak_shard", hasItem(BloodMagicItems.WEAK_BLOOD_SHARD.get())).build(consumer, BloodMagic.rl("largebloodstonebrick"));

		ShapelessRecipeBuilder.shapelessRecipe(BloodMagicBlocks.WOOD_TILE_PATH.get(), 4).addIngredient(BloodMagicBlocks.WOOD_PATH.get()).addIngredient(BloodMagicBlocks.WOOD_PATH.get()).addIngredient(BloodMagicBlocks.WOOD_PATH.get()).addIngredient(BloodMagicBlocks.WOOD_PATH.get()).addCriterion("has_apprentice_orb", hasItem(BloodMagicItems.APPRENTICE_BLOOD_ORB.get())).build(consumer, BloodMagic.rl("path/path_woodtile"));
		ShapelessRecipeBuilder.shapelessRecipe(BloodMagicBlocks.STONE_TILE_PATH.get(), 4).addIngredient(BloodMagicBlocks.STONE_PATH.get()).addIngredient(BloodMagicBlocks.STONE_PATH.get()).addIngredient(BloodMagicBlocks.STONE_PATH.get()).addIngredient(BloodMagicBlocks.STONE_PATH.get()).addCriterion("has_magician_orb", hasItem(BloodMagicItems.MAGICIAN_BLOOD_ORB.get())).build(consumer, BloodMagic.rl("path/path_stonetile"));
		ShapelessRecipeBuilder.shapelessRecipe(BloodMagicBlocks.WORN_STONE_TILE_PATH.get(), 4).addIngredient(BloodMagicBlocks.WORN_STONE_PATH.get()).addIngredient(BloodMagicBlocks.WORN_STONE_PATH.get()).addIngredient(BloodMagicBlocks.WORN_STONE_PATH.get()).addIngredient(BloodMagicBlocks.WORN_STONE_PATH.get()).addCriterion("has_master_orb", hasItem(BloodMagicItems.MASTER_BLOOD_ORB.get())).build(consumer, BloodMagic.rl("path/path_wornstonetile"));

		// Changed Recipes
		{
//			ShapedRecipeBuilder.shapedRecipe(BloodMagicBlocks.BLOOD_ALTAR.get()).key('a', Tags.Items.STONE).key('b', Items.FURNACE).key('c', Tags.Items.INGOTS_GOLD).key('d', BloodMagicItems.MONSTER_SOUL_RAW.get()).patternLine("a a").patternLine("aba").patternLine("cdc").addCriterion("has_will", hasItem(BloodMagicItems.MONSTER_SOUL_RAW.get())).build(consumer, BloodMagic.rl("blood_altar"));
//			ShapedRecipeBuilder.shapedRecipe(BloodMagicItems.SOUL_SNARE.get(), 4).key('r', Tags.Items.DUSTS_REDSTONE).key('s', Tags.Items.STRING).key('i', Tags.Items.INGOTS_IRON).patternLine("sis").patternLine("iri").patternLine("sis").addCriterion("has_redstone", hasItem(Items.REDSTONE)).build(consumer, BloodMagic.rl("soul_snare"));
//			ShapedRecipeBuilder.shapedRecipe(BloodMagicBlocks.SOUL_FORGE.get()).key('s', Tags.Items.STONE).key('g', Tags.Items.INGOTS_GOLD).key('i', Tags.Items.INGOTS_IRON).key('o', Tags.Items.STORAGE_BLOCKS_IRON).patternLine("i i").patternLine("sgs").patternLine("sos").addCriterion("has_gold", hasItem(Items.GOLD_INGOT)).build(consumer, BloodMagic.rl("soul_forge"));
		}
		{
			ShapedRecipeBuilder.shapedRecipe(BloodMagicBlocks.BLOOD_ALTAR.get()).key('a', Tags.Items.STONE).key('b', Items.FURNACE).key('c', Tags.Items.INGOTS_GOLD).key('d', Items.GOLD_INGOT).patternLine("a a").patternLine("aba").patternLine("cdc").addCriterion("has_gold", hasItem(Items.GOLD_INGOT)).build(consumer, BloodMagic.rl("blood_altar"));
			ShapedRecipeBuilder.shapedRecipe(BloodMagicBlocks.ALCHEMY_TABLE.get()).key('b', Tags.Items.INGOTS_IRON).key('s', Tags.Items.STONE).key('w', ItemTags.PLANKS).key('g', Tags.Items.INGOTS_GOLD).key('o', BloodMagicItems.SLATE.get()).patternLine("sss").patternLine("wbw").patternLine("gog").addCriterion("has_blank_slate", hasItem(BloodMagicItems.SLATE.get())).build(consumer, BloodMagic.rl("alchemy_table"));
			ShapedRecipeBuilder.shapedRecipe(BloodMagicBlocks.SOUL_FORGE.get()).key('s', Tags.Items.STONE).key('S', BloodMagicItems.SLATE.get()).key('i', Tags.Items.INGOTS_IRON).key('o', Tags.Items.STORAGE_BLOCKS_IRON).patternLine("i i").patternLine("sSs").patternLine("sos").addCriterion("has_blank_slate", hasItem(BloodMagicItems.SLATE.get())).build(consumer, BloodMagic.rl("soul_forge"));
		}
	}

	private void addVanillaSmithingRecipes(Consumer<IFinishedRecipe> consumer)
	{
		String basePath = "smelting/";
		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(BloodMagicTags.DUST_IRON), Items.IRON_INGOT, 0, 200).addCriterion("has_iron_sand", hasItem(BloodMagicItems.IRON_SAND.get())).build(consumer, BloodMagic.rl(basePath + "ingot_iron"));
		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(BloodMagicTags.DUST_GOLD), Items.GOLD_INGOT, 0, 200).addCriterion("has_gold_sand", hasItem(BloodMagicItems.GOLD_SAND.get())).build(consumer, BloodMagic.rl(basePath + "ingot_gold"));
		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(BloodMagicTags.DUST_NETHERITE_SCRAP), Items.NETHERITE_SCRAP, 0, 200).addCriterion("has_netherite_dust", hasItem(BloodMagicItems.NETHERITE_SCRAP_SAND.get())).build(consumer, BloodMagic.rl(basePath + "ingot_netherite_scrap"));
		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(BloodMagicTags.DUST_HELLFORGED), BloodMagicItems.HELLFORGED_INGOT.get(), 0, 200).addCriterion("has_hellforged_dust", hasItem(BloodMagicItems.HELLFORGED_SAND.get())).build(consumer, BloodMagic.rl(basePath + "ingot_hellforged"));
	}

	private void addBloodOrbRecipes(Consumer<IFinishedRecipe> consumer)
	{
		ShapedRecipeBuilder.shapedRecipe(BloodMagicBlocks.INCENSE_ALTAR.get()).key('s', Tags.Items.STONE).key('c', Tags.Items.COBBLESTONE).key('h', Items.CHARCOAL).key('o', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_WEAK.get())).patternLine("s s").patternLine("shs").patternLine("coc").addCriterion("has_weak_orb", hasItem(BloodMagicItems.WEAK_BLOOD_ORB.get())).build(consumer, BloodMagic.rl("incense_altar"));

		ShapedRecipeBuilder.shapedRecipe(BloodMagicBlocks.BLANK_RUNE.get()).key('a', Tags.Items.STONE).key('s', Ingredient.fromItems(BloodMagicItems.SLATE.get())).key('o', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_WEAK.get())).patternLine("asa").patternLine("aoa").patternLine("aaa").addCriterion("has_weak_orb", hasItem(BloodMagicItems.WEAK_BLOOD_ORB.get())).build(consumer, BloodMagic.rl("blood_rune_blank"));
		ShapedRecipeBuilder.shapedRecipe(BloodMagicBlocks.SPEED_RUNE.get()).key('a', Tags.Items.STONE).key('b', Ingredient.fromItems(BloodMagicItems.SLATE.get())).key('c', Ingredient.fromItems(Items.SUGAR)).key('d', BloodMagicBlocks.BLANK_RUNE.get()).patternLine("aba").patternLine("cdc").patternLine("aba").addCriterion("has_blank_rune", hasItem(BloodMagicItems.BLANK_RUNE_ITEM.get())).build(consumer, BloodMagic.rl("blood_rune_speed"));
		ShapedRecipeBuilder.shapedRecipe(BloodMagicBlocks.SACRIFICE_RUNE.get()).key('a', Tags.Items.STONE).key('b', BloodMagicItems.REINFORCED_SLATE.get()).key('c', Tags.Items.INGOTS_GOLD).key('d', BloodMagicBlocks.BLANK_RUNE.get()).key('e', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_APPRENTICE.get())).patternLine("aba").patternLine("cdc").patternLine("aea").addCriterion("has_apprentice_orb", hasItem(BloodMagicItems.APPRENTICE_BLOOD_ORB.get())).build(consumer, BloodMagic.rl("blood_rune_sacrifice"));
		ShapedRecipeBuilder.shapedRecipe(BloodMagicBlocks.SELF_SACRIFICE_RUNE.get()).key('a', Tags.Items.STONE).key('b', Ingredient.fromItems(BloodMagicItems.REINFORCED_SLATE.get())).key('c', Ingredient.fromItems(Items.GLOWSTONE_DUST)).key('d', Ingredient.fromItems(BloodMagicItems.BLANK_RUNE_ITEM.get())).key('e', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_APPRENTICE.get())).patternLine("aba").patternLine("cdc").patternLine("aea").addCriterion("has_apprentice_orb", hasItem(BloodMagicItems.APPRENTICE_BLOOD_ORB.get())).build(consumer, BloodMagic.rl("blood_rune_self_sacrifice"));
		ShapedRecipeBuilder.shapedRecipe(BloodMagicBlocks.CAPACITY_RUNE.get()).key('a', Tags.Items.STONE).key('b', Items.BUCKET).key('c', BloodMagicBlocks.BLANK_RUNE.get()).key('d', BloodMagicItems.IMBUED_SLATE.get()).patternLine("aba").patternLine("bcb").patternLine("ada").addCriterion("has_imbued_slate", hasItem(BloodMagicItems.IMBUED_SLATE.get())).build(consumer, BloodMagic.rl("blood_rune_capacity"));
		ShapedRecipeBuilder.shapedRecipe(BloodMagicBlocks.ORB_RUNE.get()).key('a', Tags.Items.STONE).key('b', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_WEAK.get())).key('c', BloodMagicBlocks.BLANK_RUNE.get()).key('d', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_MASTER.get())).patternLine("aba").patternLine("cdc").patternLine("aba").addCriterion("has_master_orb", hasItem(BloodMagicItems.MASTER_BLOOD_ORB.get())).build(consumer, BloodMagic.rl("blood_rune_orb"));
		ShapedRecipeBuilder.shapedRecipe(BloodMagicBlocks.CHARGING_RUNE.get()).key('R', Tags.Items.DUSTS_REDSTONE).key('r', BloodMagicBlocks.BLANK_RUNE.get()).key('s', BloodMagicItems.DEMONIC_SLATE.get()).key('e', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_MASTER.get())).key('G', Tags.Items.DUSTS_GLOWSTONE).patternLine("RsR").patternLine("GrG").patternLine("ReR").addCriterion("has_master_orb", hasItem(BloodMagicItems.MASTER_BLOOD_ORB.get())).build(consumer, BloodMagic.rl("blood_rune_charging"));
		ShapedRecipeBuilder.shapedRecipe(BloodMagicBlocks.DISPLACEMENT_RUNE.get()).key('a', Tags.Items.STONE).key('b', Items.WATER_BUCKET).key('c', BloodMagicBlocks.BLANK_RUNE.get()).key('d', BloodMagicItems.IMBUED_SLATE.get()).patternLine("aba").patternLine("bcb").patternLine("ada").addCriterion("has_imbued_slate", hasItem(BloodMagicItems.IMBUED_SLATE.get())).build(consumer, BloodMagic.rl("blood_rune_displacement"));
		ShapedRecipeBuilder.shapedRecipe(BloodMagicBlocks.AUGMENTED_CAPACITY_RUNE.get()).key('a', Tags.Items.OBSIDIAN).key('b', BloodMagicItems.DEMONIC_SLATE.get()).key('c', Items.BUCKET).key('d', BloodMagicBlocks.CAPACITY_RUNE.get()).key('e', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_MASTER.get())).patternLine("aba").patternLine("cdc").patternLine("aea").addCriterion("has_master_orb", hasItem(BloodMagicItems.MASTER_BLOOD_ORB.get())).build(consumer, BloodMagic.rl("blood_rune_aug_capacity"));
		ShapedRecipeBuilder.shapedRecipe(BloodMagicBlocks.ACCELERATION_RUNE.get()).key('a', Items.BUCKET).key('b', BloodMagicItems.DEMONIC_SLATE.get()).key('c', Tags.Items.INGOTS_GOLD).key('d', BloodMagicBlocks.SPEED_RUNE.get()).key('e', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_MASTER.get())).patternLine("aba").patternLine("cdc").patternLine("aea").addCriterion("has_master_orb", hasItem(BloodMagicItems.MASTER_BLOOD_ORB.get())).build(consumer, BloodMagic.rl("blood_rune_acceleration"));

		ShapedRecipeBuilder.shapedRecipe(BloodMagicBlocks.BLANK_RITUAL_STONE.get(), 4).key('a', Tags.Items.OBSIDIAN).key('b', BloodMagicItems.REINFORCED_SLATE.get()).key('c', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_APPRENTICE.get())).patternLine("aba").patternLine("bcb").patternLine("aba").addCriterion("has_apprentice_orb", hasItem(BloodMagicItems.APPRENTICE_BLOOD_ORB.get())).build(consumer, BloodMagic.rl("ritual_stone_blank"));
		ShapedRecipeBuilder.shapedRecipe(BloodMagicBlocks.MASTER_RITUAL_STONE.get()).key('a', Tags.Items.OBSIDIAN).key('b', BloodMagicBlocks.BLANK_RITUAL_STONE.get()).key('c', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_MAGICIAN.get())).patternLine("aba").patternLine("bcb").patternLine("aba").addCriterion("has_magician_orb", hasItem(BloodMagicItems.MAGICIAN_BLOOD_ORB.get())).build(consumer, BloodMagic.rl("ritual_stone_master"));

		ShapedRecipeBuilder.shapedRecipe(BloodMagicBlocks.ALCHEMICAL_REACTION_CHAMBER.get()).key('s', Tags.Items.STONE).key('f', Blocks.FURNACE).key('o', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_MAGICIAN.get())).key('I', Tags.Items.STORAGE_BLOCKS_IRON).key('S', BloodMagicItems.IMBUED_SLATE.get()).patternLine("sss").patternLine("SoS").patternLine("IfI").addCriterion("has_magician_orb", hasItem(BloodMagicItems.MAGICIAN_BLOOD_ORB.get())).build(consumer, BloodMagic.rl("arc"));

		ShapedRecipeBuilder.shapedRecipe(BloodMagicItems.PRIMITIVE_FURNACE_CELL.get()).key('c', Tags.Items.COBBLESTONE).key('f', Tags.Items.STORAGE_BLOCKS_COAL).key('s', Ingredient.fromItems(BloodMagicItems.SLATE.get())).key('o', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_MAGICIAN.get())).patternLine("csc").patternLine("cfc").patternLine("coc").addCriterion("has_magician_orb", hasItem(BloodMagicItems.MAGICIAN_BLOOD_ORB.get())).build(consumer, BloodMagic.rl("primitive_furnace_cell"));
		ShapedRecipeBuilder.shapedRecipe(BloodMagicItems.LAVA_CRYSTAL.get()).key('a', Tags.Items.GLASS).key('b', Items.LAVA_BUCKET).key('c', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_WEAK.get())).key('d', Tags.Items.OBSIDIAN).key('e', Tags.Items.GEMS_DIAMOND).patternLine("aba").patternLine("bcb").patternLine("ded").addCriterion("has_weak_orb", hasItem(BloodMagicItems.WEAK_BLOOD_ORB.get())).build(consumer, BloodMagic.rl("lava_crystal"));

		ShapedRecipeBuilder.shapedRecipe(BloodMagicItems.PRIMITIVE_HYDRATION_CELL.get()).key('B', Items.WATER_BUCKET).key('c', Tags.Items.COBBLESTONE).key('o', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_MAGICIAN.get())).key('s', BloodMagicItems.SLATE.get()).patternLine("csc").patternLine("cBc").patternLine("coc").addCriterion("has_magician_orb", hasItem(BloodMagicItems.MAGICIAN_BLOOD_ORB.get())).build(consumer, BloodMagic.rl("primitive_hydration_cell"));

		ShapelessRecipeBuilder.shapelessRecipe(BloodMagicBlocks.WOOD_PATH.get(), 4).addIngredient(ItemTags.PLANKS).addIngredient(ItemTags.PLANKS).addIngredient(ItemTags.PLANKS).addIngredient(ItemTags.PLANKS).addIngredient(IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_APPRENTICE.get())).addCriterion("has_apprentice_orb", hasItem(BloodMagicItems.APPRENTICE_BLOOD_ORB.get())).build(consumer, BloodMagic.rl("path/path_wood"));
		ShapelessRecipeBuilder.shapelessRecipe(BloodMagicBlocks.STONE_PATH.get(), 4).addIngredient(Tags.Items.STONE).addIngredient(Tags.Items.STONE).addIngredient(Tags.Items.STONE).addIngredient(Tags.Items.STONE).addIngredient(IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_MAGICIAN.get())).addCriterion("has_magician_orb", hasItem(BloodMagicItems.MAGICIAN_BLOOD_ORB.get())).build(consumer, BloodMagic.rl("path/path_stone"));
		ShapelessRecipeBuilder.shapelessRecipe(BloodMagicBlocks.WORN_STONE_PATH.get(), 4).addIngredient(BloodMagicBlocks.STONE_PATH.get()).addIngredient(BloodMagicBlocks.STONE_PATH.get()).addIngredient(BloodMagicBlocks.STONE_PATH.get()).addIngredient(BloodMagicBlocks.STONE_PATH.get()).addIngredient(IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_MASTER.get())).addCriterion("has_master_orb", hasItem(BloodMagicItems.MASTER_BLOOD_ORB.get())).build(consumer, BloodMagic.rl("path/path_wornstone"));

		ShapedRecipeBuilder.shapedRecipe(BloodMagicItems.RITUAL_READER.get()).key('s', BloodMagicItems.DEMONIC_SLATE.get()).key('g', Tags.Items.GLASS).key('i', Tags.Items.INGOTS_GOLD).key('o', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_MASTER.get())).patternLine("gog").patternLine("isi").patternLine(" s ").addCriterion("has_master_orb", hasItem(BloodMagicItems.MASTER_BLOOD_ORB.get())).build(consumer, BloodMagic.rl("ritual_reader"));

		ShapedRecipeBuilder.shapedRecipe(BloodMagicItems.EXPERIENCE_TOME.get()).key('b', Items.ENCHANTED_BOOK).key('s', Tags.Items.STRING).key('e', Tags.Items.STORAGE_BLOCKS_LAPIS).key('g', Tags.Items.INGOTS_GOLD).key('l', BloodMagicItems.IMBUED_SLATE.get()).key('o', IngredientBloodOrb.fromOrb(BloodMagicItems.ORB_MAGICIAN.get())).patternLine("ses").patternLine("lbl").patternLine("gog").addCriterion("has_magician_orb", hasItem(BloodMagicItems.MAGICIAN_BLOOD_ORB.get())).build(consumer, BloodMagic.rl("experience_tome"));
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
