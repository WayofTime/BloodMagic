package wayoftime.bloodmagic.common.recipe.serializer;

import javax.annotation.Nonnull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import wayoftime.bloodmagic.recipe.RecipeLivingDowngrade;
import wayoftime.bloodmagic.util.Constants;

public class LivingDowngradeRecipeSerializer<RECIPE extends RecipeLivingDowngrade>  implements RecipeSerializer<RECIPE>
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
		JsonElement input = GsonHelper.isArrayNode(json, Constants.JSON.INPUT)
				? GsonHelper.getAsJsonArray(json, Constants.JSON.INPUT)
				: GsonHelper.getAsJsonObject(json, Constants.JSON.INPUT);

		Ingredient inputIng = Ingredient.fromJson(input);
		ResourceLocation rl = new ResourceLocation(GsonHelper.getAsString(json, Constants.JSON.RESOURCE));

		return this.factory.create(recipeId, inputIng, rl);
	}

	@Override
	public RECIPE fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer)
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
	public interface IFactory<RECIPE extends RecipeLivingDowngrade>
	{
		RECIPE create(ResourceLocation id, Ingredient input, ResourceLocation output);
	}
}
