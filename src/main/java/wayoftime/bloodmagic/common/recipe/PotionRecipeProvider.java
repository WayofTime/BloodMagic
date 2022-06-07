package wayoftime.bloodmagic.common.recipe;

import java.util.function.Consumer;

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraftforge.common.Tags;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.data.recipe.builder.PotionEffectRecipeBuilder;
import wayoftime.bloodmagic.common.data.recipe.builder.PotionFillRecipeBuilder;
import wayoftime.bloodmagic.common.data.recipe.builder.PotionFlaskTransformRecipeBuilder;
import wayoftime.bloodmagic.common.data.recipe.builder.PotionIncreaseLengthRecipeBuilder;
import wayoftime.bloodmagic.common.data.recipe.builder.PotionIncreasePotencyRecipeBuilder;
import wayoftime.bloodmagic.common.data.recipe.builder.PotionTransformRecipeBuilder;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.potion.BloodMagicPotions;

public class PotionRecipeProvider implements ISubRecipeProvider
{
	@Override
	public void addRecipes(Consumer<IFinishedRecipe> consumer)
	{
		ItemStack waterbottleStack = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.WATER);

		String basePath = "flask/";

		PotionEffectRecipeBuilder.potionEffect(Effects.SPEED, 3600, 500, 200, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.SIMPLE_CATALYST.get())).addIngredient(Ingredient.fromItems(Items.SUGAR)).build(consumer, BloodMagic.rl(basePath + "speed_boost"));
		PotionEffectRecipeBuilder.potionEffect(Effects.JUMP_BOOST, 3600, 500, 200, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.SIMPLE_CATALYST.get())).addIngredient(Ingredient.fromItems(Items.RABBIT_FOOT)).build(consumer, BloodMagic.rl(basePath + "jump_boost"));
		PotionEffectRecipeBuilder.potionEffect(Effects.FIRE_RESISTANCE, 3600, 500, 200, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.SIMPLE_CATALYST.get())).addIngredient(Ingredient.fromItems(Items.MAGMA_CREAM)).build(consumer, BloodMagic.rl(basePath + "fire_resist"));
		PotionEffectRecipeBuilder.potionEffect(Effects.STRENGTH, 3600, 500, 200, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.SIMPLE_CATALYST.get())).addIngredient(Ingredient.fromItems(Items.BLAZE_POWDER)).build(consumer, BloodMagic.rl(basePath + "strength"));
		PotionEffectRecipeBuilder.potionEffect(Effects.REGENERATION, 3600, 500, 200, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.SIMPLE_CATALYST.get())).addIngredient(Ingredient.fromItems(Items.GHAST_TEAR)).build(consumer, BloodMagic.rl(basePath + "regen"));
		PotionEffectRecipeBuilder.potionEffect(Effects.WEAKNESS, 1800, 500, 200, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.SIMPLE_CATALYST.get())).addIngredient(Ingredient.fromItems(Items.FERMENTED_SPIDER_EYE)).build(consumer, BloodMagic.rl(basePath + "weakness"));
		PotionEffectRecipeBuilder.potionEffect(Effects.POISON, 900, 500, 200, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.SIMPLE_CATALYST.get())).addIngredient(Ingredient.fromItems(Items.SPIDER_EYE)).build(consumer, BloodMagic.rl(basePath + "poison"));
		PotionEffectRecipeBuilder.potionEffect(Effects.WATER_BREATHING, 3600, 500, 200, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.SIMPLE_CATALYST.get())).addIngredient(Ingredient.fromItems(Items.PUFFERFISH)).build(consumer, BloodMagic.rl(basePath + "water_breathing"));
		PotionEffectRecipeBuilder.potionEffect(Effects.NIGHT_VISION, 3600, 500, 200, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.SIMPLE_CATALYST.get())).addIngredient(Ingredient.fromItems(Items.GOLDEN_CARROT)).build(consumer, BloodMagic.rl(basePath + "night_vision"));
		PotionEffectRecipeBuilder.potionEffect(Effects.SLOW_FALLING, 1800, 500, 200, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.SIMPLE_CATALYST.get())).addIngredient(Ingredient.fromItems(Items.PHANTOM_MEMBRANE)).build(consumer, BloodMagic.rl(basePath + "slow_fall"));

		PotionEffectRecipeBuilder.potionEffect(BloodMagicPotions.PASSIVITY, 3600, 500, 200, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.SIMPLE_CATALYST.get())).addIngredient(Ingredient.fromItems(Items.HONEYCOMB)).build(consumer, BloodMagic.rl(basePath + "passivity"));
		PotionEffectRecipeBuilder.potionEffect(BloodMagicPotions.BOUNCE, 3600, 500, 200, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.SIMPLE_CATALYST.get())).addIngredient(Ingredient.fromItems(Items.SLIME_BALL)).build(consumer, BloodMagic.rl(basePath + "bounce"));
		PotionEffectRecipeBuilder.potionEffect(BloodMagicPotions.HARD_CLOAK, 3600, 500, 200, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.SIMPLE_CATALYST.get())).addIngredient(Ingredient.fromItems(Items.OBSIDIAN)).build(consumer, BloodMagic.rl(basePath + "hard_cloak"));

//		PotionEffectRecipeBuilder.potionEffect(BloodMagicPotions.GRAVITY, 1800, 500, 200, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.SIMPLE_CATALYST.get())).addIngredient(Ingredient.fromItems(Items.OBSIDIAN)).build(consumer, BloodMagic.rl(basePath + "gravity"));

		PotionEffectRecipeBuilder.potionEffect(Effects.INSTANT_HEALTH, 0, 500, 200, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.SIMPLE_CATALYST.get())).addIngredient(Ingredient.fromItems(Items.GLISTERING_MELON_SLICE)).build(consumer, BloodMagic.rl(basePath + "health"));

		PotionTransformRecipeBuilder.potionTransform(Effects.SLOWNESS, 1800, Effects.SPEED, 500, 200, 1).addIngredient(Ingredient.fromItems(Items.FERMENTED_SPIDER_EYE)).build(consumer, BloodMagic.rl(basePath + "speed_to_slow"));
		PotionTransformRecipeBuilder.potionTransform(Effects.SLOWNESS, 1800, Effects.JUMP_BOOST, 500, 200, 1).addIngredient(Ingredient.fromItems(Items.FERMENTED_SPIDER_EYE)).build(consumer, BloodMagic.rl(basePath + "jump_to_slow"));
		PotionTransformRecipeBuilder.potionTransform(Effects.INSTANT_DAMAGE, 0, Effects.INSTANT_HEALTH, 500, 200, 1).addIngredient(Ingredient.fromItems(Items.FERMENTED_SPIDER_EYE)).build(consumer, BloodMagic.rl(basePath + "health_to_harm"));
		PotionTransformRecipeBuilder.potionTransform(Effects.INSTANT_DAMAGE, 0, Effects.POISON, 500, 200, 1).addIngredient(Ingredient.fromItems(Items.FERMENTED_SPIDER_EYE)).build(consumer, BloodMagic.rl(basePath + "poison_to_harm"));
		PotionTransformRecipeBuilder.potionTransform(Effects.INVISIBILITY, 3600, Effects.NIGHT_VISION, 500, 200, 1).addIngredient(Ingredient.fromItems(Items.FERMENTED_SPIDER_EYE)).build(consumer, BloodMagic.rl(basePath + "night_to_invis"));
		PotionTransformRecipeBuilder.potionTransform(Effects.LEVITATION, 3600, Effects.SLOW_FALLING, 500, 200, 1).addIngredient(Ingredient.fromItems(Items.FERMENTED_SPIDER_EYE)).build(consumer, BloodMagic.rl(basePath + "fall_to_levitation"));
		PotionTransformRecipeBuilder.potionTransform(BloodMagicPotions.SPECTRAL_SIGHT, 3600, Effects.NIGHT_VISION, 500, 200, 1).addIngredient(Ingredient.fromItems(Items.GLOWSTONE_DUST)).build(consumer, BloodMagic.rl(basePath + "night_to_spectral"));
		PotionTransformRecipeBuilder.potionTransform(BloodMagicPotions.HEAVY_HEART, 1800, BloodMagicPotions.GRAVITY, 1000, 200, 1).addInputEffect(Effects.INSTANT_HEALTH).addIngredient(Ingredient.fromItems(BloodMagicItems.COMBINATIONAL_CATALYST.get())).build(consumer, BloodMagic.rl(basePath + "gravity_to_heart"));
		PotionTransformRecipeBuilder.potionTransform(BloodMagicPotions.OBSIDIAN_CLOAK, 3600, BloodMagicPotions.HARD_CLOAK, 1000, 200, 1).addIngredient(Ingredient.fromTag(Tags.Items.GEMS_DIAMOND)).addIngredient(Ingredient.fromItems(Items.CRYING_OBSIDIAN)).build(consumer, BloodMagic.rl(basePath + "hard_to_obsidian"));

		PotionTransformRecipeBuilder.potionTransform(BloodMagicPotions.GROUNDED, 1800, Effects.JUMP_BOOST, 1000, 200, 1).addIngredient(Ingredient.fromItems(Items.COBWEB)).build(consumer, BloodMagic.rl(basePath + "jump_to_grounded"));
		PotionTransformRecipeBuilder.potionTransform(BloodMagicPotions.GRAVITY, 1800, BloodMagicPotions.GROUNDED, 1000, 200, 1).addInputEffect(Effects.SLOW_FALLING).addIngredient(Ingredient.fromItems(BloodMagicItems.COMBINATIONAL_CATALYST.get())).build(consumer, BloodMagic.rl(basePath + "gravity"));
		PotionTransformRecipeBuilder.potionTransform(BloodMagicPotions.SUSPENDED, 1800, BloodMagicPotions.GRAVITY, 1000, 200, 1).addIngredient(Ingredient.fromItems(Items.FERMENTED_SPIDER_EYE)).build(consumer, BloodMagic.rl(basePath + "gravity_to_suspended"));
		PotionTransformRecipeBuilder.potionTransform(BloodMagicPotions.FLIGHT, 3600, BloodMagicPotions.SUSPENDED, 1000, 200, 1).addInputEffect(Effects.LEVITATION).addIngredient(Ingredient.fromItems(BloodMagicItems.COMBINATIONAL_CATALYST.get())).build(consumer, BloodMagic.rl(basePath + "suspended_to_flight"));

		addPotionModifiers(consumer, Effects.SPEED, "speed_boost");
		addPotionModifiers(consumer, Effects.JUMP_BOOST, "jump_boost");
		addPotionModifiers(consumer, Effects.STRENGTH, "strength");
		addPotionModifiers(consumer, Effects.WEAKNESS, "weakness");
		addPotionModifiers(consumer, Effects.POISON, "poison");
		addPotionModifiers(consumer, Effects.REGENERATION, "regen");

		addPotionModifiers(consumer, Effects.SLOWNESS, "slowness");
//		addPotionModifiers(consumer, Effects.SLOW_FALLING, "slow_fall");

//		addPotionModifiers(consumer, Effects.FIRE_RESISTANCE, "fire_resist");

		String potencyPath = "flask/potency_";
		PotionIncreasePotencyRecipeBuilder.potionIncreasePotency(Effects.INSTANT_HEALTH, 1, 0.5, 200, 100, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.MUNDANE_POWER_CATALYST.get())).build(consumer, BloodMagic.rl(potencyPath + "health"));
		PotionIncreasePotencyRecipeBuilder.potionIncreasePotency(Effects.INSTANT_DAMAGE, 1, 0.5, 200, 100, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.MUNDANE_POWER_CATALYST.get())).build(consumer, BloodMagic.rl(potencyPath + "harm"));

		String lengthPath = "flask/length_";
		PotionIncreaseLengthRecipeBuilder.potionIncreaseLength(Effects.FIRE_RESISTANCE, 2.6667, 200, 100, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.MUNDANE_LENGTHENING_CATALYST.get())).build(consumer, BloodMagic.rl(lengthPath + "fire_resist"));
		PotionIncreaseLengthRecipeBuilder.potionIncreaseLength(Effects.WATER_BREATHING, 2.6667, 200, 100, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.MUNDANE_LENGTHENING_CATALYST.get())).build(consumer, BloodMagic.rl(lengthPath + "water_breathing"));
		PotionIncreaseLengthRecipeBuilder.potionIncreaseLength(Effects.NIGHT_VISION, 2.6667, 200, 100, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.MUNDANE_LENGTHENING_CATALYST.get())).build(consumer, BloodMagic.rl(lengthPath + "night_vision"));
		PotionIncreaseLengthRecipeBuilder.potionIncreaseLength(Effects.INVISIBILITY, 2.6667, 200, 100, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.MUNDANE_LENGTHENING_CATALYST.get())).build(consumer, BloodMagic.rl(lengthPath + "invisibility"));
		PotionIncreaseLengthRecipeBuilder.potionIncreaseLength(Effects.SLOW_FALLING, 2.6667, 200, 100, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.MUNDANE_LENGTHENING_CATALYST.get())).build(consumer, BloodMagic.rl(lengthPath + "slow_fall"));

//		String transformPath = "flask/transform_";
//		PotionTransformRecipeBuilder.potionTransform(BloodMagicPotions.FLIGHT, 2400, Effects.SPEED, 100, 20, 2).addInputEffect(Effects.FIRE_RESISTANCE).addIngredient(Ingredient.fromTag(Tags.Items.FEATHERS)).build(consumer, BloodMagic.rl(transformPath + "flight"));

		String fillPath = "flask/fill_";
		PotionFillRecipeBuilder.potionFill(1, 1000, 200, 0).addIngredient(Ingredient.fromItems(BloodMagicItems.WEAK_FILLING_AGENT.get())).build(consumer, BloodMagic.rl(fillPath + "weak"));

		String flaskPath = "flask/flask_";
		PotionFlaskTransformRecipeBuilder.flask(new ItemStack(BloodMagicItems.ALCHEMY_FLASK_THROWABLE.get()), 1000, 200, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.SIMPLE_CATALYST.get())).addIngredient(Ingredient.fromItems(Items.GUNPOWDER)).build(consumer, BloodMagic.rl(flaskPath + "splash"));
		PotionFlaskTransformRecipeBuilder.flask(new ItemStack(BloodMagicItems.ALCHEMY_FLASK_LINGERING.get()), 1000, 200, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.SIMPLE_CATALYST.get())).addIngredient(Ingredient.fromItems(Items.DRAGON_BREATH)).build(consumer, BloodMagic.rl(flaskPath + "lingering"));
	}

	private void addPotionModifiers(Consumer<IFinishedRecipe> consumer, Effect effect, String name)
	{
		String potencyPath = "flask/potency_";
		String lengthPath = "flask/length_";

		PotionIncreasePotencyRecipeBuilder.potionIncreasePotency(effect, 1, 0.5, 200, 100, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.MUNDANE_POWER_CATALYST.get())).build(consumer, BloodMagic.rl(potencyPath + name));
		PotionIncreaseLengthRecipeBuilder.potionIncreaseLength(effect, 2.6667, 200, 100, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.MUNDANE_LENGTHENING_CATALYST.get())).build(consumer, BloodMagic.rl(lengthPath + name));
	}
}
