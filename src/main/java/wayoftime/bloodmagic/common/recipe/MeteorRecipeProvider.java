package wayoftime.bloodmagic.common.recipe;

import java.util.function.Consumer;

import net.minecraft.block.Blocks;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.data.recipe.builder.MeteorRecipeBuilder;
import wayoftime.bloodmagic.common.meteor.MeteorLayer;
import wayoftime.bloodmagic.common.tags.BloodMagicTags;

public class MeteorRecipeProvider implements ISubRecipeProvider
{
	@Override
	public void addRecipes(Consumer<IFinishedRecipe> consumer)
	{
		String basePath = "meteor/";

//		MeteorRecipeBuilder.meteor(Ingredient.fromTag(Tags.Items.STORAGE_BLOCKS_IRON), 0, 15).addLayer(new MeteorLayer(10, 100, Blocks.STONE).addWeightedBlock(Blocks.CRACKED_STONE_BRICKS, 35).addWeightedTag(Tags.Blocks.ORES_IRON, 20).addWeightedTag(Tags.Blocks.ORES_COAL, 35)).addLayer(new MeteorLayer(4, 100, Blocks.COAL_BLOCK).addShellBlock(Blocks.GOLD_ORE).addWeightedTag(Tags.Blocks.ORES_DIAMOND, 70).addWeightedTag(Tags.Blocks.ORES_COAL, 25)).build(consumer, BloodMagic.rl(basePath + "iron"));
		MeteorRecipeBuilder.meteor(Ingredient.fromTag(Tags.Items.STORAGE_BLOCKS_IRON), 0, 14).addLayer(new MeteorLayer(4, 0, Blocks.IRON_ORE).addShellBlock(Tags.Blocks.COBBLESTONE).addWeightedBlock(Blocks.GOLD_ORE, 30).addWeightedTag(BloodMagicTags.BLOCK_ORE_COPPER, 200, 0).addWeightedTag(BloodMagicTags.BLOCK_ORE_TIN, 140, 0).addWeightedTag(BloodMagicTags.BLOCK_ORE_SILVER, 70, 0).addWeightedTag(BloodMagicTags.BLOCK_ORE_LEAD, 80, 0).addWeightedBlock(Blocks.LAPIS_ORE, 60).addWeightedBlock(Blocks.REDSTONE_ORE, 100)).addLayer(new MeteorLayer(7, 100, Blocks.STONE).addWeightedBlock(Blocks.IRON_ORE, 400).addWeightedBlock(Blocks.GOLD_ORE, 30).addWeightedTag(BloodMagicTags.BLOCK_ORE_COPPER, 200, 0).addWeightedTag(BloodMagicTags.BLOCK_ORE_TIN, 140, 0).addWeightedTag(BloodMagicTags.BLOCK_ORE_SILVER, 70, 0).addWeightedTag(BloodMagicTags.BLOCK_ORE_LEAD, 80, 0).addWeightedBlock(Blocks.LAPIS_ORE, 60).addWeightedBlock(Blocks.REDSTONE_ORE, 100)).build(consumer, BloodMagic.rl(basePath + "iron"));

		MeteorRecipeBuilder.meteor(Ingredient.fromTag(Tags.Items.STONE), 0, 30).addLayer(new MeteorLayer(16, 0, Blocks.STONE).addShellBlock(BloodMagicTags.BLOCK_STONE_UNPOLISHED).addWeightedTag(Tags.Blocks.ORES_COAL, 150, 0).addWeightedBlock(Blocks.IRON_ORE, 50)).build(consumer, BloodMagic.rl(basePath + "stone"));

//		PotionTransformRecipeBuilder.potionTransform(BloodMagicPotions.FLIGHT, 2400, Effects.SPEED, 100, 20, 2).addInputEffect(Effects.FIRE_RESISTANCE).addIngredient(Ingredient.fromTag(Tags.Items.FEATHERS)).build(consumer, BloodMagic.rl(transformPath + "flight"));
	}

}