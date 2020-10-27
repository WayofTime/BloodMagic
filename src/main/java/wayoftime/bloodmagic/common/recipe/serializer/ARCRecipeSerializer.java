package wayoftime.bloodmagic.common.recipe.serializer;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Pair;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;
import wayoftime.bloodmagic.api.SerializerHelper;
import wayoftime.bloodmagic.api.event.recipes.FluidStackIngredient;
import wayoftime.bloodmagic.api.impl.recipe.RecipeARC;
import wayoftime.bloodmagic.util.Constants;

public class ARCRecipeSerializer<RECIPE extends RecipeARC> extends ForgeRegistryEntry<IRecipeSerializer<?>>
		implements IRecipeSerializer<RECIPE>
{
	private final IFactory<RECIPE> factory;

	public ARCRecipeSerializer(IFactory<RECIPE> factory)
	{
		this.factory = factory;
	}

	@Nonnull
	@Override
	public RECIPE read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json)
	{
		JsonElement input = JSONUtils.isJsonArray(json, Constants.JSON.INPUT)
				? JSONUtils.getJsonArray(json, Constants.JSON.INPUT)
				: JSONUtils.getJsonObject(json, Constants.JSON.INPUT);

		JsonElement tool = JSONUtils.isJsonArray(json, Constants.JSON.TOOL)
				? JSONUtils.getJsonArray(json, Constants.JSON.TOOL)
				: JSONUtils.getJsonObject(json, Constants.JSON.TOOL);

		Ingredient inputIng = Ingredient.deserialize(input);
		Ingredient toolIng = Ingredient.deserialize(tool);
		ItemStack output = SerializerHelper.getItemStack(json, Constants.JSON.OUTPUT);

		List<Pair<ItemStack, Double>> addedItems = new ArrayList<Pair<ItemStack, Double>>();
		if (json.has(Constants.JSON.ADDEDOUTPUT) && JSONUtils.isJsonArray(json, Constants.JSON.ADDEDOUTPUT))
		{
			JsonArray mainArray = JSONUtils.getJsonArray(json, Constants.JSON.ADDEDOUTPUT);

			arrayLoop: for (JsonElement element : mainArray)
			{
				if (addedItems.size() >= RecipeARC.MAX_RANDOM_OUTPUTS)
				{
					break arrayLoop;
				}
				if (element.isJsonObject())
				{
					JsonObject obj = element.getAsJsonObject();
					double chance = JSONUtils.getFloat(obj, Constants.JSON.CHANCE);
					ItemStack extraDrop = SerializerHelper.getItemStack(obj, Constants.JSON.TYPE);

					addedItems.add(Pair.of(extraDrop, chance));
				}
			}
		}

		FluidStackIngredient inputFluidIng = null;

		if (json.has(Constants.JSON.INPUT_FLUID))
		{
			JsonElement inputFluid = JSONUtils.isJsonArray(json, Constants.JSON.INPUT_FLUID)
					? JSONUtils.getJsonArray(json, Constants.JSON.INPUT_FLUID)
					: JSONUtils.getJsonObject(json, Constants.JSON.INPUT_FLUID);
			inputFluidIng = FluidStackIngredient.deserialize(inputFluid);
		}

		FluidStack outputFluid = null;

		if (json.has(Constants.JSON.OUTPUT_FLUID))
		{
			outputFluid = SerializerHelper.deserializeFluid(json);
		}

		return this.factory.create(recipeId, inputIng, toolIng, inputFluidIng, output, addedItems, outputFluid);
	}

	@Override
	public RECIPE read(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer)
	{
		try
		{
			List<Pair<ItemStack, Double>> addedItems = new ArrayList<Pair<ItemStack, Double>>();
			Ingredient inputIng = Ingredient.read(buffer);
			Ingredient toolIng = Ingredient.read(buffer);
			ItemStack output = buffer.readItemStack();

			int addedItemSize = buffer.readInt();
			for (int i = 0; i < addedItemSize; i++)
			{
				ItemStack stack = buffer.readItemStack();
				double chance = buffer.readDouble();
				addedItems.add(Pair.of(stack, chance));
			}

			buffer.writeInt(addedItems.size());
			for (Pair<ItemStack, Double> pair : addedItems)
			{
				buffer.writeItemStack(pair.getLeft());
				buffer.writeDouble(pair.getValue());
			}

			FluidStackIngredient inputFluid = null;
			FluidStack outputFluid = null;

			if (buffer.readBoolean())
			{
				inputFluid = FluidStackIngredient.read(buffer);
			}

			if (buffer.readBoolean())
			{
				outputFluid = FluidStack.readFromPacket(buffer);
			}

			return this.factory.create(recipeId, inputIng, toolIng, inputFluid, output, addedItems, outputFluid);
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
	public interface IFactory<RECIPE extends RecipeARC>
	{
		RECIPE create(ResourceLocation id, Ingredient input, Ingredient arcTool, FluidStackIngredient inputFluid, ItemStack output, List<Pair<ItemStack, Double>> addedItems, FluidStack outputFluid);
	}
}