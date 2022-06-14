package wayoftime.bloodmagic.common.recipe.serializer;

import javax.annotation.Nonnull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import wayoftime.bloodmagic.recipe.RecipeLivingDowngrade;
import wayoftime.bloodmagic.util.Constants;

public class LivingDowngradeRecipeSerializer<RECIPE extends RecipeLivingDowngrade> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RECIPE>
{
	private final IFactory<RECIPE> factory;

	public LivingDowngradeRecipeSerializer(IFactory<RECIPE> factory)
	{
		this.factory = factory;
	}

	@Nonnull
	@Override
	public RECIPE fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json)
	{
		JsonElement input = JSONUtils.isArrayNode(json, Constants.JSON.INPUT)
				? JSONUtils.getAsJsonArray(json, Constants.JSON.INPUT)
				: JSONUtils.getAsJsonObject(json, Constants.JSON.INPUT);

		Ingredient inputIng = Ingredient.fromJson(input);
		ResourceLocation rl = new ResourceLocation(JSONUtils.getAsString(json, Constants.JSON.RESOURCE));

		return this.factory.create(recipeId, inputIng, rl);
	}

	@Override
	public RECIPE fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer)
	{
		try
		{
			Ingredient input = Ingredient.fromNetwork(buffer);
			ResourceLocation rl = buffer.readResourceLocation();

//			ItemStack output = buffer.readItemStack();
//			int minimumTier = buffer.readInt();
//			int syphon = buffer.readInt();
//			int consumeRate = buffer.readInt();
//			int drainRate = buffer.readInt();

			return this.factory.create(recipeId, input, rl);
		} catch (Exception e)
		{
//			Mekanism.logger.error("Error reading electrolysis recipe from packet.", e);
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
//			Mekanism.logger.error("Error writing electrolysis recipe to packet.", e);
			throw e;
		}
	}

	@FunctionalInterface
	public interface IFactory<RECIPE extends RecipeLivingDowngrade>
	{
		RECIPE create(ResourceLocation id, Ingredient input, ResourceLocation output);
	}
}
