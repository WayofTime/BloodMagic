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
import wayoftime.bloodmagic.api.SerializerHelper;
import wayoftime.bloodmagic.api.impl.recipe.RecipeBloodAltar;
import wayoftime.bloodmagic.util.Constants;

public class BloodAltarRecipeSerializer<RECIPE extends RecipeBloodAltar>
		extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RECIPE>
{
	private final IFactory<RECIPE> factory;

	public BloodAltarRecipeSerializer(IFactory<RECIPE> factory)
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

		Ingredient inputIng = Ingredient.deserialize(input);
		ItemStack output = SerializerHelper.getItemStack(json, Constants.JSON.OUTPUT);
		int minimumTier = JSONUtils.getInt(json, Constants.JSON.ALTAR_TIER);
		int syphon = JSONUtils.getInt(json, Constants.JSON.ALTAR_SYPHON);
		int consumeRate = JSONUtils.getInt(json, Constants.JSON.ALTAR_CONSUMPTION_RATE);
		int drainRate = JSONUtils.getInt(json, Constants.JSON.ALTAR_DRAIN_RATE);

		return this.factory.create(recipeId, inputIng, output, minimumTier, syphon, consumeRate, drainRate);
	}

	@Override
	public RECIPE read(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer)
	{
		try
		{
			Ingredient input = Ingredient.read(buffer);
			ItemStack output = buffer.readItemStack();
			int minimumTier = buffer.readInt();
			int syphon = buffer.readInt();
			int consumeRate = buffer.readInt();
			int drainRate = buffer.readInt();

			return this.factory.create(recipeId, input, output, minimumTier, syphon, consumeRate, drainRate);
		} catch (Exception e)
		{
//			Mekanism.logger.error("Error reading electrolysis recipe from packet.", e);
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
//			Mekanism.logger.error("Error writing electrolysis recipe to packet.", e);
			throw e;
		}
	}

	@FunctionalInterface
	public interface IFactory<RECIPE extends RecipeBloodAltar>
	{
		RECIPE create(ResourceLocation id, Ingredient input, ItemStack output, int minimumTier, int syphon, int consumeRate, int drainRate);
	}
}
