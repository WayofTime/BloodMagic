package wayoftime.bloodmagic.common.recipe.serializer;

import javax.annotation.Nonnull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import wayoftime.bloodmagic.recipe.helper.SerializerHelper;
import wayoftime.bloodmagic.recipe.RecipeAlchemyArray;
import wayoftime.bloodmagic.util.Constants;

public class AlchemyArrayRecipeSerializer<RECIPE extends RecipeAlchemyArray>
		extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RECIPE>
{
	private final IFactory<RECIPE> factory;

	public AlchemyArrayRecipeSerializer(IFactory<RECIPE> factory)
	{
		this.factory = factory;
	}

	@Nonnull
	@Override
	public RECIPE fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json)
	{
		JsonElement input1 = JSONUtils.isArrayNode(json, Constants.JSON.BASEINPUT)
				? JSONUtils.getAsJsonArray(json, Constants.JSON.BASEINPUT)
				: JSONUtils.getAsJsonObject(json, Constants.JSON.BASEINPUT);

		JsonElement input2 = JSONUtils.isArrayNode(json, Constants.JSON.ADDEDINPUT)
				? JSONUtils.getAsJsonArray(json, Constants.JSON.ADDEDINPUT)
				: JSONUtils.getAsJsonObject(json, Constants.JSON.ADDEDINPUT);

		Ingredient baseInput = Ingredient.fromJson(input1);
		Ingredient addedInput = Ingredient.fromJson(input2);
		ResourceLocation texture = null;
		if (json.has(Constants.JSON.TEXTURE))
			texture = new ResourceLocation(JSONUtils.getAsString(json, Constants.JSON.TEXTURE));
		ItemStack output = SerializerHelper.getItemStack(json, Constants.JSON.OUTPUT);

		return this.factory.create(recipeId, texture, baseInput, addedInput, output);
	}

	@Override
	public RECIPE fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer)
	{
		try
		{
			ResourceLocation texture = null;
			if (buffer.readBoolean())
				texture = buffer.readResourceLocation();
			Ingredient baseInput = Ingredient.fromNetwork(buffer);
			Ingredient addedInput = Ingredient.fromNetwork(buffer);
			ItemStack output = buffer.readItem();

			return this.factory.create(recipeId, texture, baseInput, addedInput, output);
		} catch (Exception e)
		{
//	Mekanism.logger.error("Error reading electrolysis recipe from packet.", e);
			throw e;
		}
	}

	@Override
	public void toNetwork(@Nonnull PacketBuffer buffer, @Nonnull RECIPE recipe)
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
	public interface IFactory<RECIPE extends RecipeAlchemyArray>
	{
		RECIPE create(ResourceLocation id, ResourceLocation texture, Ingredient baseInput, Ingredient addedInput, ItemStack output);
	}
}