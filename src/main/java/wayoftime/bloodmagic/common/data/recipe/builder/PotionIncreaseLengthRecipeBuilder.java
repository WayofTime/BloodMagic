package wayoftime.bloodmagic.common.data.recipe.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.crafting.Ingredient;
import wayoftime.bloodmagic.common.data.recipe.BloodMagicRecipeBuilder;
import wayoftime.bloodmagic.potion.BloodMagicPotions;
import wayoftime.bloodmagic.recipe.flask.RecipePotionIncreaseLength;
import wayoftime.bloodmagic.util.Constants;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class PotionIncreaseLengthRecipeBuilder extends BloodMagicRecipeBuilder<PotionIncreaseLengthRecipeBuilder>
{
	private final List<Ingredient> input;
	private final MobEffect outputEffect;
	private final double lengthDurationMod;
	private final int syphon;
	private final int ticks;
	private final int minimumTier;

	protected PotionIncreaseLengthRecipeBuilder(List<Ingredient> input, MobEffect outputEffect, double lengthDurationMod, int syphon, int ticks, int minimumTier)
	{
		super(bmSerializer("flask_potionlength"));
		this.outputEffect = outputEffect;
		this.lengthDurationMod = lengthDurationMod;
		this.input = input;
		this.syphon = syphon;
		this.ticks = ticks;
		this.minimumTier = minimumTier;
	}

	public static PotionIncreaseLengthRecipeBuilder potionIncreaseLength(MobEffect outputEffect, double lengthDurationMod, int syphon, int ticks, int minimumTier)
	{
		List<Ingredient> inputList = new ArrayList<Ingredient>();

		return new PotionIncreaseLengthRecipeBuilder(inputList, outputEffect, lengthDurationMod, syphon, ticks, minimumTier);
	}

	public PotionIncreaseLengthRecipeBuilder addIngredient(Ingredient ing)
	{
		if (input.size() < RecipePotionIncreaseLength.MAX_INPUTS)
		{
			input.add(ing);
		}

		return this;
	}

	@Override
	protected PotionIncreaseLengthRecipeResult getResult(ResourceLocation id)
	{
		return new PotionIncreaseLengthRecipeResult(id);
	}

	public class PotionIncreaseLengthRecipeResult extends RecipeResult
	{
		protected PotionIncreaseLengthRecipeResult(ResourceLocation id)
		{
			super(id);
		}

		@Override
		public void serializeRecipeData(@Nonnull JsonObject json)
		{
			if (input.size() > 0)
			{
				JsonArray mainArray = new JsonArray();
				for (Ingredient ing : input)
				{
					JsonElement jsonObj = ing.toJson();

					mainArray.add(jsonObj);
				}

				json.add(Constants.JSON.INPUT, mainArray);
			}

			json.addProperty(Constants.JSON.SYPHON, syphon);
			json.addProperty(Constants.JSON.TICKS, ticks);
			json.addProperty(Constants.JSON.ALTAR_TIER, minimumTier);

			json.addProperty(Constants.JSON.EFFECT, BloodMagicPotions.getRegistryName(outputEffect).toString());
			json.addProperty(Constants.JSON.LENGTH_DUR_MOD, lengthDurationMod);
		}
	}
}
