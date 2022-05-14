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
import wayoftime.bloodmagic.recipe.RecipePotionEffect;
import wayoftime.bloodmagic.util.Constants;

public class PotionEffectRecipeBuilder extends BloodMagicRecipeBuilder<PotionEffectRecipeBuilder>
{
	private final List<Ingredient> input;
	private final Effect outputEffect;
	private final int baseDuration;
	private final int syphon;
	private final int ticks;
	private final int minimumTier;

	protected PotionEffectRecipeBuilder(List<Ingredient> input, Effect outputEffect, int baseDuration, int syphon, int ticks, int minimumTier)
	{
		super(bmSerializer("flask_potioneffect"));
		this.outputEffect = outputEffect;
		this.baseDuration = baseDuration;
		this.input = input;
		this.syphon = syphon;
		this.ticks = ticks;
		this.minimumTier = minimumTier;
	}

	public static PotionEffectRecipeBuilder PotionEffect(Effect outputEffect, int baseDuration, int syphon, int ticks, int minimumTier)
	{
		List<Ingredient> inputList = new ArrayList<Ingredient>();

		return new PotionEffectRecipeBuilder(inputList, outputEffect, baseDuration, syphon, ticks, minimumTier);
	}

	public PotionEffectRecipeBuilder addIngredient(Ingredient ing)
	{
		if (input.size() < RecipePotionEffect.MAX_INPUTS)
		{
			input.add(ing);
		}

		return this;
	}

	@Override
	protected PotionEffectRecipeResult getResult(ResourceLocation id)
	{
		return new PotionEffectRecipeResult(id);
	}

	public class PotionEffectRecipeResult extends RecipeResult
	{
		protected PotionEffectRecipeResult(ResourceLocation id)
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
			json.addProperty(Constants.JSON.DURATION, baseDuration);
		}
	}
}
