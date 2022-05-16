package wayoftime.bloodmagic.common.data.recipe.builder;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import wayoftime.bloodmagic.common.data.recipe.BloodMagicRecipeBuilder;

public class MeteorRecipeBuilder extends BloodMagicRecipeBuilder<MeteorRecipeBuilder>
{
//	private final Ingredient input;

	private final int syphon;

	protected MeteorRecipeBuilder(List<Ingredient> input, Effect outputEffect, int baseDuration, int syphon, int ticks, int minimumTier)
	{
		super(bmSerializer("meteor"));
//		this.outputEffect = outputEffect;
//		this.baseDuration = baseDuration;
//		this.input = input;
		this.syphon = syphon;
//		this.ticks = ticks;
//		this.minimumTier = minimumTier;
	}

//	public static MeteorRecipeBuilder meteor(Effect outputEffect, int baseDuration, int syphon, int ticks, int minimumTier)
//	{
//		List<Ingredient> inputList = new ArrayList<Ingredient>();
//
//		return new MeteorRecipeBuilder(inputList, outputEffect, baseDuration, syphon, ticks, minimumTier);
//	}

//	public MeteorRecipeBuilder addIngredient(Ingredient ing)
//	{
//		if (input.size() < RecipeMeteor.MAX_INPUTS)
//		{
//			input.add(ing);
//		}
//
//		return this;
//	}

	@Override
	protected MeteorRecipeResult getResult(ResourceLocation id)
	{
		return new MeteorRecipeResult(id);
	}

	public class MeteorRecipeResult extends RecipeResult
	{
		protected MeteorRecipeResult(ResourceLocation id)
		{
			super(id);
		}

		@Override
		public void serialize(@Nonnull JsonObject json)
		{
//			if (input.size() > 0)
//			{
//				JsonArray mainArray = new JsonArray();
//				for (Ingredient ing : input)
//				{
//					JsonElement jsonObj = ing.serialize();
//
//					mainArray.add(jsonObj);
//				}
//
//				json.add(Constants.JSON.INPUT, mainArray);
//			}
//
//			json.addProperty(Constants.JSON.SYPHON, syphon);
//			json.addProperty(Constants.JSON.TICKS, ticks);
//			json.addProperty(Constants.JSON.ALTAR_TIER, minimumTier);
//
//			json.addProperty(Constants.JSON.EFFECT, BloodMagicPotions.getRegistryName(outputEffect).toString());
//			json.addProperty(Constants.JSON.DURATION, baseDuration);
		}
	}
}
