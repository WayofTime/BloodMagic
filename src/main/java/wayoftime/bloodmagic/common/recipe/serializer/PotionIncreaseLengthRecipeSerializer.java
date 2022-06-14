package wayoftime.bloodmagic.common.recipe.serializer;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Effect;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import wayoftime.bloodmagic.potion.BloodMagicPotions;
import wayoftime.bloodmagic.recipe.flask.RecipePotionIncreaseLength;
import wayoftime.bloodmagic.util.Constants;

public class PotionIncreaseLengthRecipeSerializer<RECIPE extends RecipePotionIncreaseLength> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RECIPE>
{
	private final IFactory<RECIPE> factory;

	public PotionIncreaseLengthRecipeSerializer(IFactory<RECIPE> factory)
	{
		this.factory = factory;
	}

	@Nonnull
	@Override
	public RECIPE fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json)
	{
		List<Ingredient> inputList = new ArrayList<Ingredient>();

		if (json.has(Constants.JSON.INPUT) && JSONUtils.isArrayNode(json, Constants.JSON.INPUT))
		{
			JsonArray mainArray = JSONUtils.getAsJsonArray(json, Constants.JSON.INPUT);

			arrayLoop: for (JsonElement element : mainArray)
			{
				if (inputList.size() >= RecipePotionIncreaseLength.MAX_INPUTS)
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

				inputList.add(Ingredient.fromJson(element));
			}
		}

//		ItemStack output = SerializerHelper.getItemStack(json, Constants.JSON.OUTPUT);

		int syphon = JSONUtils.getAsInt(json, Constants.JSON.SYPHON);
		int ticks = JSONUtils.getAsInt(json, Constants.JSON.TICKS);
		int minimumTier = JSONUtils.getAsInt(json, Constants.JSON.ALTAR_TIER);

		Effect outputEffect = BloodMagicPotions.getEffect(new ResourceLocation(JSONUtils.getAsString(json, Constants.JSON.EFFECT)));
		double lengthDurationMod = JSONUtils.getAsFloat(json, Constants.JSON.LENGTH_DUR_MOD);

		return this.factory.create(recipeId, inputList, outputEffect, lengthDurationMod, syphon, ticks, minimumTier);
	}

	@Override
	public RECIPE fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer)
	{
		try
		{
			int size = buffer.readInt();
			List<Ingredient> input = new ArrayList<Ingredient>(size);

			for (int i = 0; i < size; i++)
			{
				input.add(i, Ingredient.fromNetwork(buffer));
			}

			int syphon = buffer.readInt();
			int ticks = buffer.readInt();
			int minimumTier = buffer.readInt();

			Effect outputEffect = Effect.byId(buffer.readInt());
			double lengthDurationMod = buffer.readDouble();

			return this.factory.create(recipeId, input, outputEffect, lengthDurationMod, syphon, ticks, minimumTier);
		} catch (Exception e)
		{
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
			throw e;
		}
	}

	@FunctionalInterface
	public interface IFactory<RECIPE extends RecipePotionIncreaseLength>
	{
		RECIPE create(ResourceLocation id, List<Ingredient> input, Effect outputEffect, double lengthDurationMod, int syphon, int ticks, int minimumTier);
	}
}
