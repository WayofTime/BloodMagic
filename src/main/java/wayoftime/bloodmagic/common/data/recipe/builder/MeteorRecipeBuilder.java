package wayoftime.bloodmagic.common.data.recipe.builder;

import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Pair;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ResourceLocation;
import wayoftime.bloodmagic.common.data.recipe.BloodMagicRecipeBuilder;
import wayoftime.bloodmagic.util.Constants;

public class MeteorRecipeBuilder extends BloodMagicRecipeBuilder<MeteorRecipeBuilder>
{
	private final Ingredient input;

	private final int syphon;

	private final List<Pair<ITag<Block>, Integer>> weightList;

	protected MeteorRecipeBuilder(Ingredient input, int syphon, List<Pair<ITag<Block>, Integer>> weightList)
	{
		super(bmSerializer("meteor"));
//		this.outputEffect = outputEffect;
//		this.baseDuration = baseDuration;
		this.input = input;
		this.syphon = syphon;
		this.weightList = weightList;
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
			json.add(Constants.JSON.INPUT, input.serialize());

			json.addProperty(Constants.JSON.SYPHON, syphon);

			if (weightList.size() > 0)
			{
				JsonArray mainArray = new JsonArray();
				for (Pair<ITag<Block>, Integer> entry : weightList)
				{
					JsonObject jsonObj = new JsonObject();
					ResourceLocation rl = TagCollectionManager.getManager().getBlockTags().getDirectIdFromTag(entry.getKey());
					if (rl == null)
					{
						continue;
					}
					jsonObj.addProperty(Constants.JSON.TAG, rl.toString());
					jsonObj.addProperty(Constants.JSON.WEIGHT, entry.getValue());

					mainArray.add(jsonObj);
				}

				json.add(Constants.JSON.OUTPUT_LIST, mainArray);
			}
//			json.addProperty(Constants.JSON.TICKS, ticks);
//			json.addProperty(Constants.JSON.ALTAR_TIER, minimumTier);
//
//			json.addProperty(Constants.JSON.EFFECT, BloodMagicPotions.getRegistryName(outputEffect).toString());
//			json.addProperty(Constants.JSON.DURATION, baseDuration);
		}
	}
}
