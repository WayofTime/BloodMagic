package wayoftime.bloodmagic.common.recipe;

import java.util.function.Consumer;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.data.recipe.builder.MeteorRecipeBuilder;
import wayoftime.bloodmagic.common.meteor.MeteorLayer;
import wayoftime.bloodmagic.common.tags.BloodMagicTags;

public class MeteorRecipeProvider implements ISubRecipeProvider
{
	@Override
	public void addRecipes(Consumer<FinishedRecipe> consumer)
	{
		String basePath = "meteor/";

//		MeteorRecipeBuilder.meteor(Ingredient.of(Tags.Items.INGOTS_COPPER), 0, 10).addLayer(new MeteorLayer(6, 0, Blocks.IRON_ORE).addShellBlock(Tags.Blocks.COBBLESTONE, 0).addWeightedBlock(BloodMagicBlocks.LIFE_ESSENCE_BLOCK.get(), 30)).build(consumer, BloodMagic.rl(basePath + "copper"));

//		MeteorRecipeBuilder.meteor(Ingredient.of(Items.ICE), 0, 10).addLayer(new MeteorLayer(6, 0, Fluids.WATER).addShellBlock(Blocks.ICE).addWeightedFluid(Fluids.WATER, 30)).build(consumer, BloodMagic.rl(basePath + "ice"));

//		MeteorRecipeBuilder.meteor(Ingredient.of(Tags.Items.STORAGE_BLOCKS_IRON), 0, 15).addLayer(new MeteorLayer(10, 100, Blocks.STONE).addWeightedBlock(Blocks.CRACKED_STONE_BRICKS, 35).addWeightedTag(Tags.Blocks.ORES_IRON, 20).addWeightedTag(Tags.Blocks.ORES_COAL, 35)).addLayer(new MeteorLayer(4, 100, Blocks.COAL_BLOCK).addShellBlock(Blocks.GOLD_ORE).addWeightedTag(Tags.Blocks.ORES_DIAMOND, 70).addWeightedTag(Tags.Blocks.ORES_COAL, 25)).build(consumer, BloodMagic.rl(basePath + "iron"));

		/*
		 * iron block meteor - 4-block thick outer shell is low level ores like iron and
		 * copper, then a cobblestone shell, then an inner core with slightly higher
		 * odds of rarer ores.
		 */

		MeteorRecipeBuilder.meteor(Ingredient.of(Tags.Items.STORAGE_BLOCKS_IRON), 1000000, 14).addLayer(new MeteorLayer(4, 0, Blocks.IRON_ORE).addShellBlock(Tags.Blocks.COBBLESTONE).addWeightedBlock(Blocks.GOLD_ORE, 30).addWeightedTag(BloodMagicTags.BLOCK_ORE_COPPER, 200, 0).addWeightedTag(BloodMagicTags.BLOCK_ORE_TIN, 140, 0).addWeightedTag(BloodMagicTags.BLOCK_ORE_SILVER, 70, 0).addWeightedTag(BloodMagicTags.BLOCK_ORE_LEAD, 80, 0).addWeightedBlock(Blocks.LAPIS_ORE, 60).addWeightedBlock(Blocks.REDSTONE_ORE, 100)).addLayer(new MeteorLayer(7, 100, Blocks.STONE).setMinWeight(1000).addWeightedBlock(Blocks.IRON_ORE, 400).addWeightedBlock(Blocks.GOLD_ORE, 30).addWeightedTag(BloodMagicTags.BLOCK_ORE_COPPER, 200, 0).addWeightedTag(BloodMagicTags.BLOCK_ORE_TIN, 140, 0).addWeightedTag(BloodMagicTags.BLOCK_ORE_SILVER, 70, 0).addWeightedTag(BloodMagicTags.BLOCK_ORE_LEAD, 80, 0).addWeightedBlock(Blocks.LAPIS_ORE, 60).addWeightedBlock(Blocks.REDSTONE_ORE, 100)).build(consumer, BloodMagic.rl(basePath + "iron"));

		/*
		 * stone meteor. much bigger than the iron meteor, mostly coal, some iron,
		 * chance at apatite. Roughly equivalent to mining at high y levels.
		 */

		MeteorRecipeBuilder.meteor(Ingredient.of(Tags.Items.STONE), 1000000, 30).addLayer(new MeteorLayer(16, 0, Blocks.STONE).setMinWeight(400).addShellBlock(BloodMagicTags.BLOCK_STONE_UNPOLISHED).addWeightedTag(BloodMagicTags.BLOCK_ORE_APATITE, 50, 0).addWeightedTag(Tags.Blocks.ORES_COAL, 150, 0).addWeightedBlock(Blocks.IRON_ORE, 50)).build(consumer, BloodMagic.rl(basePath + "stone"));

		/*
		 * diamond meteor. tiny core of solid diamond ore, then an outer core of misc.
		 * other gems.
		 */

		MeteorRecipeBuilder.meteor(Ingredient.of(Tags.Items.GEMS_DIAMOND), 1000000, 8).addLayer(new MeteorLayer(2, 0, Blocks.DIAMOND_ORE)).addLayer(new MeteorLayer(5, 0, Blocks.COBBLESTONE).setMinWeight(1000).addWeightedTag(BloodMagicTags.BLOCK_ORE_SAPPHIRE, 100, 0).addWeightedTag(BloodMagicTags.BLOCK_ORE_RUBY, 100, 0).addWeightedBlock(Blocks.DIAMOND_ORE, 100).addWeightedBlock(Blocks.EMERALD_ORE, 75).addWeightedTag(BloodMagicTags.BLOCK_ORE_CINNABAR, 200, 0)).build(consumer, BloodMagic.rl(basePath + "diamond"));

		/*
		 * Nether meteor. inner core of blackstone and ancient debris. Outer made of
		 * netherrack with a good sprinkling good odds of various useful nether
		 * materials.
		 */

		MeteorRecipeBuilder.meteor(Ingredient.of(Tags.Items.DUSTS_GLOWSTONE), 1000000, 12).addLayer(new MeteorLayer(8, 0, Blocks.NETHERRACK).setMinWeight(500).addWeightedBlock(Blocks.GLOWSTONE, 100).addWeightedBlock(Blocks.NETHER_QUARTZ_ORE, 150).addWeightedBlock(Blocks.NETHER_GOLD_ORE, 60)).addLayer(new MeteorLayer(5, 0, Blocks.BLACKSTONE).addShellBlock(Blocks.GLOWSTONE).addWeightedBlock(Blocks.ANCIENT_DEBRIS, 60).setMinWeight(1000).addWeightedBlock(Blocks.CHISELED_POLISHED_BLACKSTONE, 300).addWeightedBlock(Blocks.GILDED_BLACKSTONE, 200).addWeightedBlock(Blocks.POLISHED_BLACKSTONE, 400)).build(consumer, BloodMagic.rl(basePath + "nether"));

		/*
		 * Mekanism - as above, but with a core of uranium, because...why not?
		 */

		MeteorRecipeBuilder.meteor(Ingredient.of(BloodMagicTags.ADVANCED_ALLOY), 500000, 24).addLayer(new MeteorLayer(2, 0, BloodMagicTags.BLOCK_URANIUM)).addLayer(new MeteorLayer(8, 200, Blocks.STONE).setMinWeight(0).addWeightedTag(BloodMagicTags.BLOCK_ORE_OSMIUM, 100, 0).addWeightedTag(BloodMagicTags.BLOCK_ORE_COPPER, 100, 0).addWeightedTag(BloodMagicTags.BLOCK_ORE_TIN, 80, 0).addWeightedTag(BloodMagicTags.BLOCK_ORE_LEAD, 60, 0).addWeightedTag(BloodMagicTags.BLOCK_ORE_URANIUM, 80, 0).addWeightedTag(BloodMagicTags.BLOCK_ORE_FLUORITE, 50, 0)).build(consumer, BloodMagic.rl(basePath + "mekanism"));

		/*
		 * Ice and Fire
		 */

		MeteorRecipeBuilder.meteor(Ingredient.of(BloodMagicTags.DRAGON_BONE), 250000, 12).addLayer(new MeteorLayer(6, 200, Blocks.STONE).setMinWeight(0).addWeightedTag(BloodMagicTags.BLOCK_ORE_COPPER, 100, 0).addWeightedTag(BloodMagicTags.BLOCK_ORE_SILVER, 100, 0)).build(consumer, BloodMagic.rl(basePath + "ice_fire"));

		// all below this point are disabled due to requiring items rather than tags and
		// i don't know how to do that or if it's even possible aaa

		/*
		 * //Immersive Engineering - adds blocks mostly used in IE.
		 * MeteorRecipeBuilder.meteor(Ingredient.of(BloodMagicTags.WIRECOIL_COPPER),
		 * 500000, 24).addLayer(new MeteorLayer(8, 200,
		 * Blocks.STONE).setMinWeight(0).addWeightedTag(BloodMagicTags.BLOCK_ORE_COPPER,
		 * 100, 0).addWeightedTag(BloodMagicTags.BLOCK_ORE_LEAD, 60,
		 * 0).addWeightedTag(BloodMagicTags.BLOCK_ORE_NICKEL, 50,
		 * 0).addWeightedTag(BloodMagicTags.BLOCK_ORE_ALUMINUM, 50,
		 * 0).addWeightedTag(BloodMagicTags.BLOCK_ORE_SILVER, 50,
		 * 0).addWeightedTag(BloodMagicTags.BLOCK_ORE_URANIUM, 50, 0)).build(consumer,
		 * BloodMagic.rl(basePath + "immersive_engineering")); //Thermal Suite - as
		 * above MeteorRecipeBuilder.meteor(Ingredient.of(BloodMagicTags.RF_COIL),
		 * 500000, 30).addLayer(new MeteorLayer(9, 200,
		 * Blocks.STONE).setMinWeight(0).addShellBlock(BloodMagicTags.BLOCK_SLAG,0).
		 * addWeightedTag(BloodMagicTags.BLOCK_ORE_COPPER, 100,
		 * 0).addWeightedTag(BloodMagicTags.BLOCK_ORE_TIN, 80,
		 * 0).addWeightedTag(BloodMagicTags.BLOCK_ORE_LEAD, 60,
		 * 0).addWeightedTag(BloodMagicTags.BLOCK_ORE_SULFUR, 60,
		 * 0).addWeightedTag(BloodMagicTags.BLOCK_ORE_APATITE, 50,
		 * 0).addWeightedTag(BloodMagicTags.BLOCK_ORE_SILVER, 50,
		 * 0).addWeightedTag(BloodMagicTags.BLOCK_ORE_NICKEL, 40,
		 * 0).addWeightedTag(BloodMagicTags.BLOCK_ORE_CINNABAR, 30,
		 * 0).addWeightedTag(BloodMagicTags.BLOCK_ORE_NITER, 30,
		 * 0).addWeightedTag(BloodMagicTags.BLOCK_ORE_RUBY, 20, 0)).build(consumer,
		 * BloodMagic.rl(basePath + "thermal")); //Mystical Agriculture
		 * MeteorRecipeBuilder.meteor(Ingredient.of(BloodMagicTags.PROSPERITY_SHARD),
		 * 500000, 24).addLayer(new MeteorLayer(7, 200,
		 * Blocks.STONE).setMinWeight(0).addWeightedTag(BloodMagicTags.
		 * BLOCK_ORE_PROSPERITY, 100,
		 * 0).addWeightedTag(BloodMagicTags.BLOCK_ORE_INFERIUM, 100, 0)).addLayer(new
		 * MeteorLayer(3,0,
		 * Blocks.STONE).addWeightedTag(BloodMagicTags.BLOCK_ORE_SOULIUM, 100,
		 * 0).addWeightedBlock(Blocks.SOUL_SAND, 50).addWeightedBlock(Blocks.SOUL_SOIL,
		 * 50)).build(consumer, BloodMagic.rl(basePath + "myst_ag")); //Create
		 * MeteorRecipeBuilder.meteor(Ingredient.of(BloodMagicTags.ANDESITE_ALLOY),
		 * 500000, 24).addLayer(new MeteorLayer(6, 200,
		 * Blocks.ANDESITE).setMinWeight(0).addWeightedTag(BloodMagicTags.
		 * BLOCK_ORE_COPPER, 100, 0).addWeightedTag(BloodMagicTags.BLOCK_ORE_ZINC, 100,
		 * 0).addWeightedBlock(Blocks.IRON_ORE, 50)).build(consumer,
		 * BloodMagic.rl(basePath + "create")); //Applied Energistics meteor. Inner core
		 * of skystone and fluix, then stone, certus quartz and Quartz blocks, then an
		 * outer shell of skystone. //note: doesn't work right now, waiting to be able
		 * to add strings so i can put in skystone and fluix blocks.
		 * //MeteorRecipeBuilder.meteor(Ingredient.of(BloodMagicTags.GEM_CERTUS_QUARTZ),
		 * 500000, 24).addLayer(new MeteorLayer(4,0,
		 * BloodMagicTags.BLOCK_SKY_STONE,0).setMinWeight(0).addWeightedTag(
		 * BloodMagicTags.BLOCK_FLUIX,
		 * 100,0).addShellBlock(BloodMagicTags.BLOCK_SKY_STONE,0)).addLayer(new
		 * MeteorLayer(6, 200,
		 * Blocks.STONE).setMinWeight(0).addWeightedTag(BloodMagicTags.
		 * BLOCK_ORE_CERTUS_QUARTZ,
		 * 100,0).addWeightedTag(BloodMagicTags.BLOCK_QUARTZ,50,0).addShellBlock(
		 * BloodMagicTags.BLOCK_SKY_STONE,0)).build(consumer, BloodMagic.rl(basePath +
		 * "ae2"));
		 */
	}

}