package wayoftime.bloodmagic.common.data.recipe.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import wayoftime.bloodmagic.common.data.recipe.BloodMagicRecipeBuilder;
import wayoftime.bloodmagic.common.meteor.MeteorLayer;
import wayoftime.bloodmagic.util.Constants;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class MeteorRecipeBuilder extends BloodMagicRecipeBuilder<MeteorRecipeBuilder>
{
	private final Ingredient input;

	private final int syphon;
	private final float explosionRadius;

	private final List<MeteorLayer> layerList;

	protected MeteorRecipeBuilder(Ingredient input, int syphon, float explosionRadius, List<MeteorLayer> layerList)
	{
		super(bmSerializer("meteor"));
		this.input = input;
		this.syphon = syphon;
		this.explosionRadius = explosionRadius;
		this.layerList = layerList;
	}

	public static MeteorRecipeBuilder meteor(Ingredient input, int syphon, float explosionRadius)
	{
		return new MeteorRecipeBuilder(input, syphon, explosionRadius, new ArrayList<>());
	}

	public MeteorRecipeBuilder addLayer(MeteorLayer layer)
	{
		layerList.add(layer);
		return this;
	}

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
		public void serializeRecipeData(@Nonnull JsonObject json)
		{
			json.add(Constants.JSON.INPUT, input.toJson());
			json.addProperty(Constants.JSON.SYPHON, syphon);
			json.addProperty(Constants.JSON.EXPLOSION, explosionRadius);

			if (layerList.size() > 0)
			{
				JsonArray mainArray = new JsonArray();
				for (MeteorLayer layer : layerList)
				{
					mainArray.add(layer.serialize());
				}

				json.add(Constants.JSON.LAYER, mainArray);
			}

//			json.addProperty(Constants.JSON.TICKS, ticks);
//			json.addProperty(Constants.JSON.ALTAR_TIER, minimumTier);
//
//			json.addProperty(Constants.JSON.EFFECT, BloodMagicPotions.getRegistryName(outputEffect).toString());
//			json.addProperty(Constants.JSON.DURATION, baseDuration);
		}
	}
}
