package wayoftime.bloodmagic.common.recipe;

import java.util.function.Consumer;

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraftforge.common.Tags;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.data.recipe.builder.PotionEffectRecipeBuilder;
import wayoftime.bloodmagic.common.data.recipe.builder.PotionIncreaseLengthRecipeBuilder;
import wayoftime.bloodmagic.common.data.recipe.builder.PotionIncreasePotencyRecipeBuilder;
import wayoftime.bloodmagic.common.data.recipe.builder.PotionTransformRecipeBuilder;
import wayoftime.bloodmagic.potion.BloodMagicPotions;

public class PotionRecipeProvider implements ISubRecipeProvider
{
	@Override
	public void addRecipes(Consumer<IFinishedRecipe> consumer)
	{
		ItemStack waterbottleStack = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.WATER);

		String basePath = "flask/";

		PotionEffectRecipeBuilder.PotionEffect(Effects.SPEED, 3600, 200, 200, 1).addIngredient(Ingredient.fromItems(Items.SUGAR)).build(consumer, BloodMagic.rl(basePath + "speed_boost"));
		PotionEffectRecipeBuilder.PotionEffect(Effects.FIRE_RESISTANCE, 3600, 200, 200, 1).addIngredient(Ingredient.fromItems(Items.BLAZE_POWDER)).build(consumer, BloodMagic.rl(basePath + "fire_resist"));

		String potencyPath = "flask/potency_";
		PotionIncreasePotencyRecipeBuilder.potionIncreasePotency(Effects.SPEED, 1, 0.5, 100, 200, 1).addIngredient(Ingredient.fromItems(Items.SUGAR)).addIngredient(Ingredient.fromItems(Items.SUGAR)).build(consumer, BloodMagic.rl(potencyPath + "speed_boost"));

		String lengthPath = "flask/length_";
		PotionIncreaseLengthRecipeBuilder.potionIncreaseLength(Effects.SPEED, 2.6667, 100, 200, 1).addIngredient(Ingredient.fromItems(Items.SUGAR)).addIngredient(Ingredient.fromItems(Items.APPLE)).build(consumer, BloodMagic.rl(lengthPath + "speed_boost"));

		String transformPath = "flask/transform_";
		PotionTransformRecipeBuilder.potionTransform(BloodMagicPotions.FLIGHT, 2400, Effects.SPEED, 100, 20, 2).addInputEffect(Effects.FIRE_RESISTANCE).addIngredient(Ingredient.fromTag(Tags.Items.FEATHERS)).build(consumer, BloodMagic.rl(transformPath + "flight"));
	}
}
