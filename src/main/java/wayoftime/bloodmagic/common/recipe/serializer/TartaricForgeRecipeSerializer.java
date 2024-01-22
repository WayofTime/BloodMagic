package wayoftime.bloodmagic.common.recipe.serializer;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import wayoftime.bloodmagic.recipe.helper.SerializerHelper;
import wayoftime.bloodmagic.recipe.RecipeTartaricForge;
import wayoftime.bloodmagic.util.Constants;

public class TartaricForgeRecipeSerializer<RECIPE extends RecipeTartaricForge>
		 implements RecipeSerializer<RECIPE>
{

	private final IFactory<RECIPE> factory;

	public TartaricForgeRecipeSerializer(IFactory<RECIPE> factory)
	{
		this.factory = factory;
	}

	@Nonnull
	@Override
	public RECIPE fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json)
	{
		List<Ingredient> inputList = new ArrayList<Ingredient>();
		for (int i = 0; i < 4; i++)
		{
			if (json.has(Constants.JSON.INPUT + i))
			{
				JsonElement input = GsonHelper.isArrayNode(json, Constants.JSON.INPUT + i)
						? GsonHelper.getAsJsonArray(json, Constants.JSON.INPUT + i)
						: GsonHelper.getAsJsonObject(json, Constants.JSON.INPUT + i);
				inputList.add(Ingredient.fromJson(input));
			}
		}

		ItemStack output = SerializerHelper.getItemStack(json, Constants.JSON.OUTPUT);

		float minimumSouls = GsonHelper.getAsFloat(json, Constants.JSON.TARTARIC_MINIMUM);
		float soulDrain = GsonHelper.getAsFloat(json, Constants.JSON.TARTARIC_DRAIN);

		return this.factory.create(recipeId, inputList, output, minimumSouls, soulDrain);
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

			ItemStack output = buffer.readItem();
			double minimumSouls = buffer.readDouble();
			double soulDrain = buffer.readDouble();

			return this.factory.create(recipeId, input, output, minimumSouls, soulDrain);
		} catch (Exception e)
		{
//	Mekanism.logger.error("Error reading electrolysis recipe from packet.", e);
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
//	Mekanism.logger.error("Error writing electrolysis recipe to packet.", e);
			throw e;
		}
	}

	@FunctionalInterface
	public interface IFactory<RECIPE extends RecipeTartaricForge>
	{
		RECIPE create(ResourceLocation id, List<Ingredient> input, ItemStack output, double minimumSouls, double soulDrain);
	}
}
