package wayoftime.bloodmagic.common.recipe.serializer;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import wayoftime.bloodmagic.api.SerializerHelper;
import wayoftime.bloodmagic.api.impl.recipe.RecipeAlchemyTable;
import wayoftime.bloodmagic.util.Constants;

public class AlchemyTableRecipeSerializer<RECIPE extends RecipeAlchemyTable>
		extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RECIPE>
{

	private final IFactory<RECIPE> factory;

	public AlchemyTableRecipeSerializer(IFactory<RECIPE> factory)
	{
		this.factory = factory;
	}

	@Nonnull
	@Override
	public RECIPE read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json)
	{
		List<Ingredient> inputList = new ArrayList<Ingredient>();

		if (json.has(Constants.JSON.INPUT) && JSONUtils.isJsonArray(json, Constants.JSON.INPUT))
		{
			JsonArray mainArray = JSONUtils.getJsonArray(json, Constants.JSON.INPUT);

			arrayLoop: for (JsonElement element : mainArray)
			{
				if (inputList.size() >= RecipeAlchemyTable.MAX_INPUTS)
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

				inputList.add(Ingredient.deserialize(element));
			}
		}

		ItemStack output = SerializerHelper.getItemStack(json, Constants.JSON.OUTPUT);

		int syphon = JSONUtils.getInt(json, Constants.JSON.SYPHON);
		int ticks = JSONUtils.getInt(json, Constants.JSON.TICKS);
		int minimumTier = JSONUtils.getInt(json, Constants.JSON.ALTAR_TIER);

		return this.factory.create(recipeId, inputList, output, syphon, ticks, minimumTier);
	}

	@Override
	public RECIPE read(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer)
	{
		try
		{
			int size = buffer.readInt();
			List<Ingredient> input = new ArrayList<Ingredient>(size);

			for (int i = 0; i < size; i++)
			{
				input.add(i, Ingredient.read(buffer));
			}

			buffer.writeInt(input.size());
			for (int i = 0; i < input.size(); i++)
			{
				input.get(i).write(buffer);
			}

			ItemStack output = buffer.readItemStack();
			int syphon = buffer.readInt();
			int ticks = buffer.readInt();
			int minimumTier = buffer.readInt();

			return this.factory.create(recipeId, input, output, syphon, ticks, minimumTier);
		} catch (Exception e)
		{
//Mekanism.logger.error("Error reading electrolysis recipe from packet.", e);
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
//Mekanism.logger.error("Error writing electrolysis recipe to packet.", e);
			throw e;
		}
	}

	@FunctionalInterface
	public interface IFactory<RECIPE extends RecipeAlchemyTable>
	{
		RECIPE create(ResourceLocation id, List<Ingredient> input, ItemStack output, int syphon, int ticks, int minimumTier);
	}
}
