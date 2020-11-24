package wayoftime.bloodmagic.common.recipe.serializer;

import java.util.ArrayList;
import java.util.List;

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
import wayoftime.bloodmagic.recipe.RecipeTartaricForge;
import wayoftime.bloodmagic.util.Constants;

public class TartaricForgeRecipeSerializer<RECIPE extends RecipeTartaricForge>
		extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RECIPE>
{

	private final IFactory<RECIPE> factory;

	public TartaricForgeRecipeSerializer(IFactory<RECIPE> factory)
	{
		this.factory = factory;
	}

	@Nonnull
	@Override
	public RECIPE read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json)
	{
		List<Ingredient> inputList = new ArrayList<Ingredient>();
		for (int i = 0; i < 4; i++)
		{
			if (json.has(Constants.JSON.INPUT + i))
			{
				JsonElement input = JSONUtils.isJsonArray(json, Constants.JSON.INPUT + i)
						? JSONUtils.getJsonArray(json, Constants.JSON.INPUT + i)
						: JSONUtils.getJsonObject(json, Constants.JSON.INPUT + i);
				inputList.add(Ingredient.deserialize(input));
			}
		}

		ItemStack output = SerializerHelper.getItemStack(json, Constants.JSON.OUTPUT);

		float minimumSouls = JSONUtils.getFloat(json, Constants.JSON.TARTARIC_MINIMUM);
		float soulDrain = JSONUtils.getFloat(json, Constants.JSON.TARTARIC_DRAIN);

		return this.factory.create(recipeId, inputList, output, minimumSouls, soulDrain);
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

			ItemStack output = buffer.readItemStack();
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
	public interface IFactory<RECIPE extends RecipeTartaricForge>
	{
		RECIPE create(ResourceLocation id, List<Ingredient> input, ItemStack output, double minimumSouls, double soulDrain);
	}
}
