package wayoftime.bloodmagic.common.recipe.serializer;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import wayoftime.bloodmagic.recipe.flask.RecipePotionEffect;
import wayoftime.bloodmagic.recipe.flask.RecipePotionFlaskTransform;
import wayoftime.bloodmagic.recipe.helper.SerializerHelper;
import wayoftime.bloodmagic.util.Constants;

public class PotionFlaskTransformRecipeSerializer<RECIPE extends RecipePotionFlaskTransform>  implements RecipeSerializer<RECIPE>
{
	private final IFactory<RECIPE> factory;

	public PotionFlaskTransformRecipeSerializer(IFactory<RECIPE> factory)
	{
		this.factory = factory;
	}

	@Nonnull
	@Override
	public RECIPE fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json)
	{
		List<Ingredient> inputList = new ArrayList<Ingredient>();

		if (json.has(Constants.JSON.INPUT) && GsonHelper.isArrayNode(json, Constants.JSON.INPUT))
		{
			JsonArray mainArray = GsonHelper.getAsJsonArray(json, Constants.JSON.INPUT);

			arrayLoop: for (JsonElement element : mainArray)
			{
				if (inputList.size() >= RecipePotionEffect.MAX_INPUTS)
				{
					break arrayLoop;
				}

				if (element.isJsonArray())
				{
					element = element.getAsJsonArray();
				} else
				{
					element.getAsJsonObject();
				}

				inputList.add(Ingredient.fromJson(element));
			}
		}

		ItemStack output = SerializerHelper.getItemStack(json, Constants.JSON.OUTPUT);

		int syphon = GsonHelper.getAsInt(json, Constants.JSON.SYPHON);
		int ticks = GsonHelper.getAsInt(json, Constants.JSON.TICKS);
		int minimumTier = GsonHelper.getAsInt(json, Constants.JSON.ALTAR_TIER);

		return this.factory.create(recipeId, inputList, output, syphon, ticks, minimumTier);
	}

	@Override
	public RECIPE fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer)
	{
		try
		{
			int size = buffer.readInt();
			List<Ingredient> input = new ArrayList<Ingredient>(size);

			for (int i = 0; i < size; i++)
			{
				input.add(i, Ingredient.fromNetwork(buffer));
			}

			int syphon = buffer.readInt();
			int ticks = buffer.readInt();
			int minimumTier = buffer.readInt();

			ItemStack output = buffer.readItem();

//
//			Effect outputEffect = Effect.get(buffer.readInt());
//			int baseDuration = buffer.readInt();

			return this.factory.create(recipeId, input, output, syphon, ticks, minimumTier);
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
	public interface IFactory<RECIPE extends RecipePotionFlaskTransform>
	{
		RECIPE create(ResourceLocation id, List<Ingredient> input, ItemStack output, int syphon, int ticks, int minimumTier);
	}
}
