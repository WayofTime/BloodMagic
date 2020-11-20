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
	public RECIPE read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json)
	{
		JsonElement input1 = JSONUtils.isJsonArray(json, Constants.JSON.BASEINPUT)
				? JSONUtils.getJsonArray(json, Constants.JSON.BASEINPUT)
				: JSONUtils.getJsonObject(json, Constants.JSON.BASEINPUT);

		JsonElement input2 = JSONUtils.isJsonArray(json, Constants.JSON.ADDEDINPUT)
				? JSONUtils.getJsonArray(json, Constants.JSON.ADDEDINPUT)
				: JSONUtils.getJsonObject(json, Constants.JSON.ADDEDINPUT);

		Ingredient baseInput = Ingredient.deserialize(input1);
		Ingredient addedInput = Ingredient.deserialize(input2);
		ResourceLocation texture = null;
		if (json.has(Constants.JSON.TEXTURE))
			texture = new ResourceLocation(JSONUtils.getString(json, Constants.JSON.TEXTURE));
		ItemStack output = SerializerHelper.getItemStack(json, Constants.JSON.OUTPUT);

		return this.factory.create(recipeId, texture, baseInput, addedInput, output);
	}

	@Override
	public RECIPE read(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer)
	{
		try
		{
			ResourceLocation texture = null;
			if (buffer.readBoolean())
				texture = buffer.readResourceLocation();
			Ingredient baseInput = Ingredient.read(buffer);
			Ingredient addedInput = Ingredient.read(buffer);
			ItemStack output = buffer.readItemStack();

			return this.factory.create(recipeId, texture, baseInput, addedInput, output);
		} catch (Exception e)
		{
//	Mekanism.logger.error("Error reading electrolysis recipe from packet.", e);
			throw e;
		}
	}

	@Override
	public void write(@Nonnull PacketBuffer buffer, @Nonnull RECIPE recipe)
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