package wayoftime.bloodmagic.common.recipe;

import java.util.function.Consumer;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.data.recipe.builder.PotionCycleRecipeBuilder;
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
	public void addRecipes(Consumer<FinishedRecipe> consumer)
	{
		String basePath = "flask/";

		PotionEffectRecipeBuilder.potionEffect(MobEffects.MOVEMENT_SPEED, 3600, 500, 200, 1).addIngredient(Ingredient.of(BloodMagicItems.SIMPLE_CATALYST.get())).addIngredient(Ingredient.of(Items.SUGAR)).build(consumer, BloodMagic.rl(basePath + "speed_boost"));
		PotionEffectRecipeBuilder.potionEffect(MobEffects.JUMP, 3600, 500, 200, 1).addIngredient(Ingredient.of(BloodMagicItems.SIMPLE_CATALYST.get())).addIngredient(Ingredient.of(Items.RABBIT_FOOT)).build(consumer, BloodMagic.rl(basePath + "jump_boost"));
		PotionEffectRecipeBuilder.potionEffect(MobEffects.FIRE_RESISTANCE, 3600, 500, 200, 1).addIngredient(Ingredient.of(BloodMagicItems.SIMPLE_CATALYST.get())).addIngredient(Ingredient.of(Items.MAGMA_CREAM)).build(consumer, BloodMagic.rl(basePath + "fire_resist"));
		PotionEffectRecipeBuilder.potionEffect(MobEffects.DAMAGE_BOOST, 3600, 500, 200, 1).addIngredient(Ingredient.of(BloodMagicItems.SIMPLE_CATALYST.get())).addIngredient(Ingredient.of(Items.BLAZE_POWDER)).build(consumer, BloodMagic.rl(basePath + "strength"));
		PotionEffectRecipeBuilder.potionEffect(MobEffects.REGENERATION, 900, 500, 200, 1).addIngredient(Ingredient.of(BloodMagicItems.SIMPLE_CATALYST.get())).addIngredient(Ingredient.of(Items.GHAST_TEAR)).build(consumer, BloodMagic.rl(basePath + "regen"));
		PotionEffectRecipeBuilder.potionEffect(MobEffects.WEAKNESS, 1800, 500, 200, 1).addIngredient(Ingredient.of(BloodMagicItems.SIMPLE_CATALYST.get())).addIngredient(Ingredient.of(Items.FERMENTED_SPIDER_EYE)).build(consumer, BloodMagic.rl(basePath + "weakness"));
		PotionEffectRecipeBuilder.potionEffect(MobEffects.POISON, 900, 500, 200, 1).addIngredient(Ingredient.of(BloodMagicItems.SIMPLE_CATALYST.get())).addIngredient(Ingredient.of(Items.SPIDER_EYE)).build(consumer, BloodMagic.rl(basePath + "poison"));
		PotionEffectRecipeBuilder.potionEffect(MobEffects.WATER_BREATHING, 3600, 500, 200, 1).addIngredient(Ingredient.of(BloodMagicItems.SIMPLE_CATALYST.get())).addIngredient(Ingredient.of(Items.PUFFERFISH)).build(consumer, BloodMagic.rl(basePath + "water_breathing"));
		PotionEffectRecipeBuilder.potionEffect(MobEffects.NIGHT_VISION, 3600, 500, 200, 1).addIngredient(Ingredient.of(BloodMagicItems.SIMPLE_CATALYST.get())).addIngredient(Ingredient.of(Items.GOLDEN_CARROT)).build(consumer, BloodMagic.rl(basePath + "night_vision"));
		PotionEffectRecipeBuilder.potionEffect(MobEffects.SLOW_FALLING, 1800, 500, 200, 1).addIngredient(Ingredient.of(BloodMagicItems.SIMPLE_CATALYST.get())).addIngredient(Ingredient.of(Items.PHANTOM_MEMBRANE)).build(consumer, BloodMagic.rl(basePath + "slow_fall"));

		PotionEffectRecipeBuilder.potionEffect(BloodMagicPotions.PASSIVITY.get(), 3600, 500, 200, 1).addIngredient(Ingredient.of(BloodMagicItems.SIMPLE_CATALYST.get())).addIngredient(Ingredient.of(Items.HONEYCOMB)).build(consumer, BloodMagic.rl(basePath + "passivity"));
		PotionEffectRecipeBuilder.potionEffect(BloodMagicPotions.BOUNCE.get(), 3600, 500, 200, 1).addIngredient(Ingredient.of(BloodMagicItems.SIMPLE_CATALYST.get())).addIngredient(Ingredient.of(Items.SLIME_BALL)).build(consumer, BloodMagic.rl(basePath + "bounce"));
		PotionEffectRecipeBuilder.potionEffect(BloodMagicPotions.HARD_CLOAK.get(), 3600, 500, 200, 1).addIngredient(Ingredient.of(BloodMagicItems.SIMPLE_CATALYST.get())).addIngredient(Ingredient.of(Items.OBSIDIAN)).build(consumer, BloodMagic.rl(basePath + "hard_cloak"));

//		PotionEffectRecipeBuilder.potionEffect(BloodMagicPotions.GRAVITY, 1800, 500, 200, 1).addIngredient(Ingredient.fromItems(BloodMagicItems.SIMPLE_CATALYST.get())).addIngredient(Ingredient.fromItems(Items.OBSIDIAN)).build(consumer, BloodMagic.rl(basePath + "gravity"));

		PotionEffectRecipeBuilder.potionEffect(MobEffects.HEAL, 0, 500, 200, 1).addIngredient(Ingredient.of(BloodMagicItems.SIMPLE_CATALYST.get())).addIngredient(Ingredient.of(Items.GLISTERING_MELON_SLICE)).build(consumer, BloodMagic.rl(basePath + "health"));

		PotionTransformRecipeBuilder.potionTransform(MobEffects.MOVEMENT_SLOWDOWN, 1800, MobEffects.MOVEMENT_SPEED, 500, 200, 1).addIngredient(Ingredient.of(Items.FERMENTED_SPIDER_EYE)).build(consumer, BloodMagic.rl(basePath + "speed_to_slow"));
		PotionTransformRecipeBuilder.potionTransform(MobEffects.MOVEMENT_SLOWDOWN, 1800, MobEffects.JUMP, 500, 200, 1).addIngredient(Ingredient.of(Items.FERMENTED_SPIDER_EYE)).build(consumer, BloodMagic.rl(basePath + "jump_to_slow"));
		PotionTransformRecipeBuilder.potionTransform(MobEffects.HARM, 0, MobEffects.HEAL, 500, 200, 1).addIngredient(Ingredient.of(Items.FERMENTED_SPIDER_EYE)).build(consumer, BloodMagic.rl(basePath + "health_to_harm"));
		PotionTransformRecipeBuilder.potionTransform(MobEffects.HARM, 0, MobEffects.POISON, 500, 200, 1).addIngredient(Ingredient.of(Items.FERMENTED_SPIDER_EYE)).build(consumer, BloodMagic.rl(basePath + "poison_to_harm"));
		PotionTransformRecipeBuilder.potionTransform(MobEffects.INVISIBILITY, 3600, MobEffects.NIGHT_VISION, 500, 200, 1).addIngredient(Ingredient.of(Items.FERMENTED_SPIDER_EYE)).build(consumer, BloodMagic.rl(basePath + "night_to_invis"));
		PotionTransformRecipeBuilder.potionTransform(MobEffects.LEVITATION, 3600, MobEffects.SLOW_FALLING, 500, 200, 1).addIngredient(Ingredient.of(Items.FERMENTED_SPIDER_EYE)).build(consumer, BloodMagic.rl(basePath + "fall_to_levitation"));
		PotionTransformRecipeBuilder.potionTransform(BloodMagicPotions.SPECTRAL_SIGHT.get(), 3600, MobEffects.NIGHT_VISION, 500, 200, 1).addIngredient(Ingredient.of(Items.GLOWSTONE_DUST)).build(consumer, BloodMagic.rl(basePath + "night_to_spectral"));
		PotionTransformRecipeBuilder.potionTransform(BloodMagicPotions.HEAVY_HEART.get(), 1800, BloodMagicPotions.GRAVITY.get(), 1000, 200, 1).addInputEffect(MobEffects.HEAL).addIngredient(Ingredient.of(BloodMagicItems.COMBINATIONAL_CATALYST.get())).build(consumer, BloodMagic.rl(basePath + "gravity_to_heart"));
		PotionTransformRecipeBuilder.potionTransform(BloodMagicPotions.OBSIDIAN_CLOAK.get(), 3600, BloodMagicPotions.HARD_CLOAK.get(), 1000, 200, 1).addIngredient(Ingredient.of(Tags.Items.GEMS_DIAMOND)).addIngredient(Ingredient.of(Items.CRYING_OBSIDIAN)).build(consumer, BloodMagic.rl(basePath + "hard_to_obsidian"));

		PotionTransformRecipeBuilder.potionTransform(BloodMagicPotions.GROUNDED.get(), 1800, MobEffects.JUMP, 1000, 200, 1).addIngredient(Ingredient.of(Items.COBWEB)).build(consumer, BloodMagic.rl(basePath + "jump_to_grounded"));
		PotionTransformRecipeBuilder.potionTransform(BloodMagicPotions.GRAVITY.get(), 1800, BloodMagicPotions.GROUNDED.get(), 1000, 200, 1).addInputEffect(MobEffects.SLOW_FALLING).addIngredient(Ingredient.of(BloodMagicItems.COMBINATIONAL_CATALYST.get())).build(consumer, BloodMagic.rl(basePath + "gravity"));
		PotionTransformRecipeBuilder.potionTransform(BloodMagicPotions.SUSPENDED.get(), 1800, BloodMagicPotions.GRAVITY.get(), 1000, 200, 1).addIngredient(Ingredient.of(Items.FERMENTED_SPIDER_EYE)).build(consumer, BloodMagic.rl(basePath + "gravity_to_suspended"));
		PotionTransformRecipeBuilder.potionTransform(BloodMagicPotions.FLIGHT.get(), 3600, BloodMagicPotions.SUSPENDED.get(), 1000, 200, 1).addInputEffect(MobEffects.LEVITATION).addIngredient(Ingredient.of(BloodMagicItems.COMBINATIONAL_CATALYST.get())).build(consumer, BloodMagic.rl(basePath + "suspended_to_flight"));

		addPotionModifiers(consumer, MobEffects.MOVEMENT_SPEED, "speed_boost");
		addPotionModifiers(consumer, MobEffects.JUMP, "jump_boost");
		addPotionModifiers(consumer, MobEffects.DAMAGE_BOOST, "strength");
		addPotionModifiers(consumer, MobEffects.WEAKNESS, "weakness");
		addPotionModifiers(consumer, MobEffects.POISON, "poison");
		addPotionModifiers(consumer, MobEffects.REGENERATION, "regen");
		addPotionModifiers(consumer, MobEffects.LEVITATION, "levitation");

		addPotionModifiers(consumer, MobEffects.MOVEMENT_SLOWDOWN, "slowness");
		addPotionModifiers(consumer, BloodMagicPotions.HARD_CLOAK.get(), "hard_cloak");
		addPotionModifiers(consumer, BloodMagicPotions.HEAVY_HEART.get(), "heavy_heart");
		addPotionModifiers(consumer, BloodMagicPotions.OBSIDIAN_CLOAK.get(), "obsidian_cloak");
		addPotionModifiers(consumer, BloodMagicPotions.GRAVITY.get(), "gravity");
		addPotionModifiers(consumer, BloodMagicPotions.FLIGHT.get(), "flight");
		addPotionModifiers(consumer, BloodMagicPotions.SPECTRAL_SIGHT.get(), "spectral_sight");

//		addPotionModifiers(consumer, Effects.SLOW_FALLING, "slow_fall");

//		addPotionModifiers(consumer, Effects.FIRE_RESISTANCE, "fire_resist");

		String potencyPath = "flask/potency_";
		PotionIncreasePotencyRecipeBuilder.potionIncreasePotency(MobEffects.HEAL, 1, 0.5, 200, 100, 1).addIngredient(Ingredient.of(BloodMagicItems.MUNDANE_POWER_CATALYST.get())).build(consumer, BloodMagic.rl(potencyPath + "health"));
		PotionIncreasePotencyRecipeBuilder.potionIncreasePotency(MobEffects.HARM, 1, 0.5, 200, 100, 1).addIngredient(Ingredient.of(BloodMagicItems.MUNDANE_POWER_CATALYST.get())).build(consumer, BloodMagic.rl(potencyPath + "harm"));

		PotionIncreasePotencyRecipeBuilder.potionIncreasePotency(MobEffects.HEAL, 2, 0.25, 500, 100, 3).addIngredient(Ingredient.of(BloodMagicItems.AVERAGE_POWER_CATALYST.get())).build(consumer, BloodMagic.rl(potencyPath + "average_health"));
		PotionIncreasePotencyRecipeBuilder.potionIncreasePotency(MobEffects.HARM, 2, 0.25, 500, 100, 3).addIngredient(Ingredient.of(BloodMagicItems.AVERAGE_POWER_CATALYST.get())).build(consumer, BloodMagic.rl(potencyPath + "average_harm"));

		String lengthPath = "flask/length_";
		PotionIncreaseLengthRecipeBuilder.potionIncreaseLength(MobEffects.FIRE_RESISTANCE, 2.6667, 200, 100, 1).addIngredient(Ingredient.of(BloodMagicItems.MUNDANE_LENGTHENING_CATALYST.get())).build(consumer, BloodMagic.rl(lengthPath + "fire_resist"));
		PotionIncreaseLengthRecipeBuilder.potionIncreaseLength(MobEffects.WATER_BREATHING, 2.6667, 200, 100, 1).addIngredient(Ingredient.of(BloodMagicItems.MUNDANE_LENGTHENING_CATALYST.get())).build(consumer, BloodMagic.rl(lengthPath + "water_breathing"));
		PotionIncreaseLengthRecipeBuilder.potionIncreaseLength(MobEffects.NIGHT_VISION, 2.6667, 200, 100, 1).addIngredient(Ingredient.of(BloodMagicItems.MUNDANE_LENGTHENING_CATALYST.get())).build(consumer, BloodMagic.rl(lengthPath + "night_vision"));
		PotionIncreaseLengthRecipeBuilder.potionIncreaseLength(MobEffects.INVISIBILITY, 2.6667, 200, 100, 1).addIngredient(Ingredient.of(BloodMagicItems.MUNDANE_LENGTHENING_CATALYST.get())).build(consumer, BloodMagic.rl(lengthPath + "invisibility"));
		PotionIncreaseLengthRecipeBuilder.potionIncreaseLength(MobEffects.SLOW_FALLING, 2.6667, 200, 100, 1).addIngredient(Ingredient.of(BloodMagicItems.MUNDANE_LENGTHENING_CATALYST.get())).build(consumer, BloodMagic.rl(lengthPath + "slow_fall"));
		PotionIncreaseLengthRecipeBuilder.potionIncreaseLength(BloodMagicPotions.PASSIVITY.get(), 2.6667, 200, 100, 1).addIngredient(Ingredient.of(BloodMagicItems.MUNDANE_LENGTHENING_CATALYST.get())).build(consumer, BloodMagic.rl(lengthPath + "passivity"));
		PotionIncreaseLengthRecipeBuilder.potionIncreaseLength(BloodMagicPotions.BOUNCE.get(), 2.6667, 200, 100, 1).addIngredient(Ingredient.of(BloodMagicItems.MUNDANE_LENGTHENING_CATALYST.get())).build(consumer, BloodMagic.rl(lengthPath + "bounce"));
		PotionIncreaseLengthRecipeBuilder.potionIncreaseLength(BloodMagicPotions.GROUNDED.get(), 2.6667, 200, 100, 1).addIngredient(Ingredient.of(BloodMagicItems.MUNDANE_LENGTHENING_CATALYST.get())).build(consumer, BloodMagic.rl(lengthPath + "grounded"));
		PotionIncreaseLengthRecipeBuilder.potionIncreaseLength(BloodMagicPotions.SUSPENDED.get(), 2.6667, 200, 100, 1).addIngredient(Ingredient.of(BloodMagicItems.MUNDANE_LENGTHENING_CATALYST.get())).build(consumer, BloodMagic.rl(lengthPath + "suspended"));

		PotionIncreaseLengthRecipeBuilder.potionIncreaseLength(MobEffects.FIRE_RESISTANCE, 7.1112, 500, 100, 4).addIngredient(Ingredient.of(BloodMagicItems.AVERAGE_LENGTHENING_CATALYST.get())).build(consumer, BloodMagic.rl(lengthPath + "average_fire_resist"));
		PotionIncreaseLengthRecipeBuilder.potionIncreaseLength(MobEffects.WATER_BREATHING, 7.1112, 500, 100, 4).addIngredient(Ingredient.of(BloodMagicItems.AVERAGE_LENGTHENING_CATALYST.get())).build(consumer, BloodMagic.rl(lengthPath + "average_water_breathing"));
		PotionIncreaseLengthRecipeBuilder.potionIncreaseLength(MobEffects.NIGHT_VISION, 7.1112, 500, 100, 4).addIngredient(Ingredient.of(BloodMagicItems.AVERAGE_LENGTHENING_CATALYST.get())).build(consumer, BloodMagic.rl(lengthPath + "average_night_vision"));
		PotionIncreaseLengthRecipeBuilder.potionIncreaseLength(MobEffects.INVISIBILITY, 7.1112, 500, 100, 4).addIngredient(Ingredient.of(BloodMagicItems.AVERAGE_LENGTHENING_CATALYST.get())).build(consumer, BloodMagic.rl(lengthPath + "average_invisibility"));
		PotionIncreaseLengthRecipeBuilder.potionIncreaseLength(MobEffects.SLOW_FALLING, 7.1112, 500, 100, 4).addIngredient(Ingredient.of(BloodMagicItems.AVERAGE_LENGTHENING_CATALYST.get())).build(consumer, BloodMagic.rl(lengthPath + "average_slow_fall"));
		PotionIncreaseLengthRecipeBuilder.potionIncreaseLength(BloodMagicPotions.PASSIVITY.get(), 7.1112, 500, 100, 4).addIngredient(Ingredient.of(BloodMagicItems.AVERAGE_LENGTHENING_CATALYST.get())).build(consumer, BloodMagic.rl(lengthPath + "average_passivity"));
		PotionIncreaseLengthRecipeBuilder.potionIncreaseLength(BloodMagicPotions.BOUNCE.get(), 7.1112, 500, 100, 4).addIngredient(Ingredient.of(BloodMagicItems.AVERAGE_LENGTHENING_CATALYST.get())).build(consumer, BloodMagic.rl(lengthPath + "average_bounce"));
		PotionIncreaseLengthRecipeBuilder.potionIncreaseLength(BloodMagicPotions.GROUNDED.get(), 7.1112, 500, 100, 4).addIngredient(Ingredient.of(BloodMagicItems.AVERAGE_LENGTHENING_CATALYST.get())).build(consumer, BloodMagic.rl(lengthPath + "average_grounded"));
		PotionIncreaseLengthRecipeBuilder.potionIncreaseLength(BloodMagicPotions.SUSPENDED.get(), 7.1112, 500, 100, 4).addIngredient(Ingredient.of(BloodMagicItems.AVERAGE_LENGTHENING_CATALYST.get())).build(consumer, BloodMagic.rl(lengthPath + "average_suspended"));

//		String transformPath = "flask/transform_";
//		PotionTransformRecipeBuilder.potionTransform(BloodMagicPotions.FLIGHT, 2400, Effects.SPEED, 100, 20, 2).addInputEffect(Effects.FIRE_RESISTANCE).addIngredient(Ingredient.fromTag(Tags.Items.FEATHERS)).build(consumer, BloodMagic.rl(transformPath + "flight"));

		String fillPath = "flask/fill_";
		PotionFillRecipeBuilder.potionFill(1, 1000, 200, 0).addIngredient(Ingredient.of(BloodMagicItems.WEAK_FILLING_AGENT.get())).build(consumer, BloodMagic.rl(fillPath + "weak"));
		PotionFillRecipeBuilder.potionFill(3, 3000, 200, 0).addIngredient(Ingredient.of(BloodMagicItems.AVERAGE_FILLING_AGENT.get())).build(consumer, BloodMagic.rl(fillPath + "standard"));

		String flaskPath = "flask/flask_";
		PotionFlaskTransformRecipeBuilder.flask(new ItemStack(BloodMagicItems.ALCHEMY_FLASK_THROWABLE.get()), 1000, 200, 1).addIngredient(Ingredient.of(BloodMagicItems.SIMPLE_CATALYST.get())).addIngredient(Ingredient.of(Items.GUNPOWDER)).build(consumer, BloodMagic.rl(flaskPath + "splash"));
		PotionFlaskTransformRecipeBuilder.flask(new ItemStack(BloodMagicItems.ALCHEMY_FLASK_LINGERING.get()), 1000, 200, 1).addIngredient(Ingredient.of(BloodMagicItems.SIMPLE_CATALYST.get())).addIngredient(Ingredient.of(Items.DRAGON_BREATH)).build(consumer, BloodMagic.rl(flaskPath + "lingering"));

		String cyclePath = "flask/cycle_";
		PotionCycleRecipeBuilder.potionCycle(1, 500, 50, 1).addIngredient(Ingredient.of(BloodMagicItems.CYCLING_CATALYST.get())).build(consumer, BloodMagic.rl(cyclePath + "basic"));
	}

	private void addPotionModifiers(Consumer<FinishedRecipe> consumer, MobEffect effect, String name)
	{
		String potencyPath = "flask/potency_";
		String lengthPath = "flask/length_";

		PotionIncreasePotencyRecipeBuilder.potionIncreasePotency(effect, 1, 0.5, 200, 100, 1).addIngredient(Ingredient.of(BloodMagicItems.MUNDANE_POWER_CATALYST.get())).build(consumer, BloodMagic.rl(potencyPath + name));
		PotionIncreaseLengthRecipeBuilder.potionIncreaseLength(effect, 2.6667, 200, 100, 1).addIngredient(Ingredient.of(BloodMagicItems.MUNDANE_LENGTHENING_CATALYST.get())).build(consumer, BloodMagic.rl(lengthPath + name));

		PotionIncreasePotencyRecipeBuilder.potionIncreasePotency(effect, 2, 0.25, 500, 100, 4).addIngredient(Ingredient.of(BloodMagicItems.AVERAGE_POWER_CATALYST.get())).build(consumer, BloodMagic.rl(potencyPath + "average_" + name));
		PotionIncreaseLengthRecipeBuilder.potionIncreaseLength(effect, 7.1112, 500, 100, 4).addIngredient(Ingredient.of(BloodMagicItems.AVERAGE_LENGTHENING_CATALYST.get())).build(consumer, BloodMagic.rl(lengthPath + "average_" + name));
	}
}
