package wayoftime.bloodmagic.common.recipe;

import java.util.function.Consumer;

import net.minecraft.block.Blocks;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.data.recipe.builder.MeteorRecipeBuilder;
import wayoftime.bloodmagic.common.meteor.MeteorLayer;

public class MeteorRecipeProvider implements ISubRecipeProvider
{
	@Override
	public void addRecipes(Consumer<IFinishedRecipe> consumer)
	{
		String basePath = "meteor/";

		MeteorRecipeBuilder.meteor(Ingredient.fromTag(Tags.Items.STORAGE_BLOCKS_IRON), 0, 15).addLayer(new MeteorLayer(10, 100, Blocks.STONE).addWeightedBlock(Blocks.CRACKED_STONE_BRICKS, 35).addWeightedTag(Tags.Blocks.ORES_IRON, 20).addWeightedTag(Tags.Blocks.ORES_COAL, 35)).addLayer(new MeteorLayer(4, 100, Blocks.COAL_BLOCK).addShellBlock(Blocks.GOLD_ORE).addWeightedTag(Tags.Blocks.ORES_DIAMOND, 70).addWeightedTag(Tags.Blocks.ORES_COAL, 25)).build(consumer, BloodMagic.rl(basePath + "iron"));

//		PotionTransformRecipeBuilder.potionTransform(BloodMagicPotions.FLIGHT, 2400, Effects.SPEED, 100, 20, 2).addInputEffect(Effects.FIRE_RESISTANCE).addIngredient(Ingredient.fromTag(Tags.Items.FEATHERS)).build(consumer, BloodMagic.rl(transformPath + "flight"));
	}

}