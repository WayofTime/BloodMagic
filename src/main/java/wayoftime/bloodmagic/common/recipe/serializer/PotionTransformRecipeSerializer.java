package wayoftime.bloodmagic.common.recipe.serializer;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Pair;

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
import wayoftime.bloodmagic.recipe.flask.RecipePotionEffect;
import wayoftime.bloodmagic.recipe.flask.RecipePotionTransform;
import wayoftime.bloodmagic.util.Constants;

public class PotionTransformRecipeSerializer<RECIPE extends RecipePotionTransform> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RECIPE>
{
	private final IFactory<RECIPE> factory;

	public PotionTransformRecipeSerializer(IFactory<RECIPE> factory)
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
				if (inputList.size() >= RecipePotionEffect.MAX_INPUTS)
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

		List<Pair<Effect, Integer>> outputEffectList = new ArrayList<>();
		if (json.has(Constants.JSON.OUTPUT_EFFECT) && JSONUtils.isArrayNode(json, Constants.JSON.OUTPUT_EFFECT))
		{
			JsonArray mainArray = JSONUtils.getAsJsonArray(json, Constants.JSON.OUTPUT_EFFECT);

			for (JsonElement element : mainArray)
			{
				JsonObject obj = element.getAsJsonObject();
				Effect outputEffect = BloodMagicPotions.getEffect(new ResourceLocation(JSONUtils.getAsString(obj, Constants.JSON.EFFECT)));
				int baseDuration = JSONUtils.getAsInt(obj, Constants.JSON.DURATION);

				outputEffectList.add(Pair.of(outputEffect, baseDuration));
			}
		}

		List<Effect> inputEffectList = new ArrayList<>();
		if (json.has(Constants.JSON.INPUT_EFFECT) && JSONUtils.isArrayNode(json, Constants.JSON.INPUT_EFFECT))
		{
			JsonArray mainArray = JSONUtils.getAsJsonArray(json, Constants.JSON.INPUT_EFFECT);

			for (JsonElement element : mainArray)
			{
				Effect inputEffect = BloodMagicPotions.getEffect(new ResourceLocation(JSONUtils.convertToString(element, Constants.JSON.EFFECT)));

				inputEffectList.add(inputEffect);
			}
		}

		int syphon = JSONUtils.getAsInt(json, Constants.JSON.SYPHON);
		int ticks = JSONUtils.getAsInt(json, Constants.JSON.TICKS);
		int minimumTier = JSONUtils.getAsInt(json, Constants.JSON.ALTAR_TIER);

		return this.factory.create(recipeId, inputList, outputEffectList, inputEffectList, syphon, ticks, minimumTier);
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

			int outputEffectSize = buffer.readInt();
			List<Pair<Effect, Integer>> outputEffectList = new ArrayList<>(outputEffectSize);

			for (int i = 0; i < outputEffectSize; i++)
			{
				int effectId = buffer.readInt();
				outputEffectList.add(i, Pair.of(Effect.byId(effectId), buffer.readInt()));
			}

			int inputEffectSize = buffer.readInt();
			List<Effect> inputEffectList = new ArrayList<>();

			for (int i = 0; i < inputEffectSize; i++)
			{
				inputEffectList.add(i, Effect.byId(buffer.readInt()));
			}

//
//			Effect outputEffect = Effect.get(buffer.readInt());
//			int baseDuration = buffer.readInt();

			return this.factory.create(recipeId, input, outputEffectList, inputEffectList, syphon, ticks, minimumTier);
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
	public interface IFactory<RECIPE extends RecipePotionTransform>
	{
		RECIPE create(ResourceLocation id, List<Ingredient> input, List<Pair<Effect, Integer>> outputEffectList, List<Effect> inputEffectList, int syphon, int ticks, int minimumTier);
	}
}
