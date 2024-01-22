package wayoftime.bloodmagic.common.recipe.serializer;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import wayoftime.bloodmagic.common.meteor.MeteorLayer;
import wayoftime.bloodmagic.recipe.RecipeMeteor;
import wayoftime.bloodmagic.util.Constants;

public class MeteorRecipeSerializer<RECIPE extends RecipeMeteor>  implements RecipeSerializer<RECIPE>
{
	private final IFactory<RECIPE> factory;

	public MeteorRecipeSerializer(IFactory<RECIPE> factory)
	{
		this.factory = factory;
	}

	@Nonnull
	@Override
	public RECIPE fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json)
	{

		JsonElement input = GsonHelper.isArrayNode(json, Constants.JSON.INPUT)
				? GsonHelper.getAsJsonArray(json, Constants.JSON.INPUT)
				: GsonHelper.getAsJsonObject(json, Constants.JSON.INPUT);

		Ingredient inputIng = Ingredient.fromJson(input);

		int syphon = GsonHelper.getAsInt(json, Constants.JSON.SYPHON);
		float explosionRadius = GsonHelper.getAsInt(json, Constants.JSON.EXPLOSION);

		List<MeteorLayer> layerList = new ArrayList<>();
		if (json.has(Constants.JSON.LAYER) && GsonHelper.isArrayNode(json, Constants.JSON.LAYER))
		{
			JsonArray mainArray = GsonHelper.getAsJsonArray(json, Constants.JSON.LAYER);

			for (JsonElement element : mainArray)
			{
				JsonObject obj = element.getAsJsonObject();
				MeteorLayer layer = MeteorLayer.deserialize(obj);

				layerList.add(layer);
			}
		}

		return this.factory.create(recipeId, inputIng, syphon, explosionRadius, layerList);
	}

	@Override
	public RECIPE fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer)
	{
		try
		{
			Ingredient input = Ingredient.fromNetwork(buffer);
			int syphon = buffer.readInt();
			float explosionRadius = buffer.readFloat();

			int listSize = buffer.readInt();
			List<MeteorLayer> layerList = new ArrayList<>();
			for (int i = 0; i < listSize; i++)
			{
				MeteorLayer layer = MeteorLayer.read(buffer);
				layerList.add(layer);
			}

			return this.factory.create(recipeId, input, syphon, explosionRadius, layerList);
		} catch (Exception e)
		{
			throw e;
		}
	}

	@Override
	public void toNetwork(@Nonnull FriendlyByteBuf buffer, @Nonnull RECIPE recipe)
	{
		try
		{
			recipe.write(buffer);
		} catch (Exception e)
		{
			throw e;
		}
	}

	@FunctionalInterface
	public interface IFactory<RECIPE extends RecipeMeteor>
	{
		RECIPE create(ResourceLocation id, Ingredient input, int syphon, float explosionRadius, List<MeteorLayer> layerList);
	}
}
