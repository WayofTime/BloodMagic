package wayoftime.bloodmagic.common.recipe.serializer;

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
import wayoftime.bloodmagic.recipe.RecipeBloodAltar;
import wayoftime.bloodmagic.util.Constants;

public class BloodAltarRecipeSerializer<RECIPE extends RecipeBloodAltar>
		 implements RecipeSerializer<RECIPE>
{
	private final IFactory<RECIPE> factory;

	public BloodAltarRecipeSerializer(IFactory<RECIPE> factory)
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
		ItemStack output = SerializerHelper.getItemStack(json, Constants.JSON.OUTPUT);
		int minimumTier = GsonHelper.getAsInt(json, Constants.JSON.ALTAR_TIER);
		int syphon = GsonHelper.getAsInt(json, Constants.JSON.ALTAR_SYPHON);
		int consumeRate = GsonHelper.getAsInt(json, Constants.JSON.ALTAR_CONSUMPTION_RATE);
		int drainRate = GsonHelper.getAsInt(json, Constants.JSON.ALTAR_DRAIN_RATE);

		return this.factory.create(recipeId, inputIng, output, minimumTier, syphon, consumeRate, drainRate);
	}

	@Override
	public RECIPE fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer)
	{
		try
		{
			Ingredient input = Ingredient.fromNetwork(buffer);
			ItemStack output = buffer.readItem();
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
	public void toNetwork(@Nonnull FriendlyByteBuf buffer, @Nonnull RECIPE recipe)
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
