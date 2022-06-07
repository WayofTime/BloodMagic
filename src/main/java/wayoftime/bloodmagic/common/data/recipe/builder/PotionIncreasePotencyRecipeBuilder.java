package wayoftime.bloodmagic.common.data.recipe.builder;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import wayoftime.bloodmagic.common.data.recipe.BloodMagicRecipeBuilder;
import wayoftime.bloodmagic.potion.BloodMagicPotions;
import wayoftime.bloodmagic.recipe.flask.RecipePotionIncreasePotency;
import wayoftime.bloodmagic.util.Constants;

public class PotionIncreasePotencyRecipeBuilder extends BloodMagicRecipeBuilder<PotionIncreasePotencyRecipeBuilder>
{
	private final List<Ingredient> input;
	private final Effect outputEffect;
	private final int amplifier;
	private final double ampDurationMod;
	private final int syphon;
	private final int ticks;
	private final int minimumTier;

	protected PotionIncreasePotencyRecipeBuilder(List<Ingredient> input, Effect outputEffect, int amplifier, double ampDurationMod, int syphon, int ticks, int minimumTier)
	{
		super(bmSerializer("flask_potionpotency"));
		this.outputEffect = outputEffect;
		this.amplifier = amplifier;
		this.ampDurationMod = ampDurationMod;
		this.input = input;
		this.syphon = syphon;
		this.ticks = ticks;
		this.minimumTier = minimumTier;
	}

	public static PotionIncreasePotencyRecipeBuilder potionIncreasePotency(Effect outputEffect, int amplifier, double ampDurationMod, int syphon, int ticks, int minimumTier)
	{
		List<Ingredient> inputList = new ArrayList<Ingredient>();

		return new PotionIncreasePotencyRecipeBuilder(inputList, outputEffect, amplifier, ampDurationMod, syphon, ticks, minimumTier);
	}

	public PotionIncreasePotencyRecipeBuilder addIngredient(Ingredient ing)
	{
		if (input.size() < RecipePotionIncreasePotency.MAX_INPUTS)
		{
			input.add(ing);
		}

		return this;
	}

	@Override
	protected PotionIncreasePotencyRecipeResult getResult(ResourceLocation id)
	{
		return new PotionIncreasePotencyRecipeResult(id);
	}

	public class PotionIncreasePotencyRecipeResult extends RecipeResult
	{
		protected PotionIncreasePotencyRecipeResult(ResourceLocation id)
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

			json.addProperty(Constants.JSON.SYPHON, syphon);
			json.addProperty(Constants.JSON.TICKS, ticks);
			json.addProperty(Constants.JSON.ALTAR_TIER, minimumTier);

			json.addProperty(Constants.JSON.EFFECT, BloodMagicPotions.getRegistryName(outputEffect).toString());
			json.addProperty(Constants.JSON.AMPLIFIER, amplifier);
			json.addProperty(Constants.JSON.AMP_DUR_MOD, ampDurationMod);
		}
	}
}
