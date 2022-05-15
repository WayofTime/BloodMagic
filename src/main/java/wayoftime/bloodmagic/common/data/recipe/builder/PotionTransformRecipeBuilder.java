package wayoftime.bloodmagic.common.data.recipe.builder;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Pair;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import wayoftime.bloodmagic.common.data.recipe.BloodMagicRecipeBuilder;
import wayoftime.bloodmagic.potion.BloodMagicPotions;
import wayoftime.bloodmagic.recipe.flask.RecipePotionTransform;
import wayoftime.bloodmagic.util.Constants;

public class PotionTransformRecipeBuilder extends BloodMagicRecipeBuilder<PotionTransformRecipeBuilder>
{
	private final List<Ingredient> input;
	private final List<Pair<Effect, Integer>> outputEffectList;
	private final List<Effect> inputEffectList;
	private final int syphon;
	private final int ticks;
	private final int minimumTier;

	protected PotionTransformRecipeBuilder(List<Ingredient> input, Effect outputEffect, int baseDuration, Effect inputEffect, int syphon, int ticks, int minimumTier)
	{
		this(input, new ArrayList<>(), new ArrayList<>(), syphon, ticks, minimumTier);
		outputEffectList.add(Pair.of(outputEffect, baseDuration));
		addInputEffect(inputEffect);
	}

	protected PotionTransformRecipeBuilder(List<Ingredient> input, List<Pair<Effect, Integer>> outputEffectList, List<Effect> inputEffectList, int syphon, int ticks, int minimumTier)
	{
		super(bmSerializer("flask_potiontransform"));
		this.outputEffectList = outputEffectList;
		this.inputEffectList = inputEffectList;
		this.input = input;
		this.syphon = syphon;
		this.ticks = ticks;
		this.minimumTier = minimumTier;
	}

	public static PotionTransformRecipeBuilder potionTransform(Effect outputEffect, int baseDuration, Effect inputEffect, int syphon, int ticks, int minimumTier)
	{
		List<Ingredient> inputList = new ArrayList<Ingredient>();

		return new PotionTransformRecipeBuilder(inputList, outputEffect, baseDuration, inputEffect, syphon, ticks, minimumTier);
	}

	public PotionTransformRecipeBuilder addIngredient(Ingredient ing)
	{
		if (input.size() < RecipePotionTransform.MAX_INPUTS)
		{
			input.add(ing);
		}

		return this;
	}

	public PotionTransformRecipeBuilder addInputEffect(Effect effect)
	{
		if (!inputEffectList.contains(effect))
			inputEffectList.add(effect);

		return this;
	}

	@Override
	protected PotionTransformRecipeResult getResult(ResourceLocation id)
	{
		return new PotionTransformRecipeResult(id);
	}

	public class PotionTransformRecipeResult extends RecipeResult
	{
		protected PotionTransformRecipeResult(ResourceLocation id)
		{
			super(id);
		}

		@Override
		public void serialize(@Nonnull JsonObject json)
		{
			if (input.size() > 0)
			{
				JsonArray mainArray = new JsonArray();
				for (Ingredient ing : input)
				{
					JsonElement jsonObj = ing.serialize();

					mainArray.add(jsonObj);
				}

				json.add(Constants.JSON.INPUT, mainArray);
			}

			if (outputEffectList.size() > 0)
			{
				JsonArray mainArray = new JsonArray();
				for (Pair<Effect, Integer> outputEffect : outputEffectList)
				{
					JsonObject jsonObj = new JsonObject();
					jsonObj.addProperty(Constants.JSON.EFFECT, BloodMagicPotions.getRegistryName(outputEffect.getKey()).toString());
					jsonObj.addProperty(Constants.JSON.DURATION, outputEffect.getValue());

					mainArray.add(jsonObj);
				}

				json.add(Constants.JSON.OUTPUT_EFFECT, mainArray);
			}

			if (inputEffectList.size() > 0)
			{
				JsonArray mainArray = new JsonArray();
				for (int i = 0; i < inputEffectList.size(); i++)
				{
					String effectStr = BloodMagicPotions.getRegistryName(inputEffectList.get(i)).toString();
					mainArray.add(effectStr);
				}

				json.add(Constants.JSON.INPUT_EFFECT, mainArray);
			}

			json.addProperty(Constants.JSON.SYPHON, syphon);
			json.addProperty(Constants.JSON.TICKS, ticks);
			json.addProperty(Constants.JSON.ALTAR_TIER, minimumTier);
		}
	}
}
