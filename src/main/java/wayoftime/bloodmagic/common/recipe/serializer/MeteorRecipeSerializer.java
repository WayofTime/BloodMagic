package wayoftime.bloodmagic.common.recipe.serializer;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Pair;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Effect;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import wayoftime.bloodmagic.recipe.RecipeMeteor;
import wayoftime.bloodmagic.util.Constants;

public class MeteorRecipeSerializer<RECIPE extends RecipeMeteor> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RECIPE>
{
	private final IFactory<RECIPE> factory;

	public MeteorRecipeSerializer(IFactory<RECIPE> factory)
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

//		ItemStack output = SerializerHelper.getItemStack(json, Constants.JSON.OUTPUT);

		int syphon = JSONUtils.getInt(json, Constants.JSON.SYPHON);

		List<Pair<ITag<Block>, Integer>> weightList = new ArrayList<>();

		if (json.has(Constants.JSON.OUTPUT_LIST) && JSONUtils.isJsonArray(json, Constants.JSON.OUTPUT_LIST))
		{
			JsonArray mainArray = JSONUtils.getJsonArray(json, Constants.JSON.OUTPUT_LIST);

			arrayLoop: for (JsonElement element : mainArray)
			{
				JsonObject obj = element.getAsJsonObject();
//				BlockTags.getCollection().get(new ResourceLocation(name));
				ITag<Block> itag = TagCollectionManager.getManager().getBlockTags().get(new ResourceLocation(JSONUtils.getString(obj, Constants.JSON.TAG)));
				int weight = JSONUtils.getInt(obj, Constants.JSON.WEIGHT);

				weightList.add(Pair.of(itag, weight));

//				if (inputList.size() >= RecipeAlchemyTable.MAX_INPUTS)
//				{
//					break arrayLoop;
//				}

//				if (element.isJsonArray())
//				{
//					element = element.getAsJsonArray();
//				} else
//				{
//					element.getAsJsonObject();
//				}

//				inputList.add(Ingredient.deserialize(element));
			}
		}

//		int ticks = JSONUtils.getInt(json, Constants.JSON.TICKS);
//		int minimumTier = JSONUtils.getInt(json, Constants.JSON.ALTAR_TIER);
//
//		Effect outputEffect = BloodMagicPotions.getEffect(new ResourceLocation(JSONUtils.getString(json, Constants.JSON.EFFECT)));
//		int baseDuration = JSONUtils.getInt(json, Constants.JSON.DURATION);

		return null;
//		return this.factory.create(recipeId, inputList, outputEffect, baseDuration, syphon, ticks, minimumTier);
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

			int syphon = buffer.readInt();
			int ticks = buffer.readInt();
			int minimumTier = buffer.readInt();

			Effect outputEffect = Effect.get(buffer.readInt());
			int baseDuration = buffer.readInt();

//			return this.factory.create(recipeId, input, outputEffect, baseDuration, syphon, ticks, minimumTier);
			return null;
		} catch (Exception e)
		{
			throw e;
		}
	}

	@Override
	public void write(@Nonnull PacketBuffer buffer, @Nonnull RECIPE recipe)
	{
		try
		{
//			PotionItem d;
			recipe.write(buffer);
		} catch (Exception e)
		{
			throw e;
		}
	}

	@FunctionalInterface
	public interface IFactory<RECIPE extends RecipeMeteor>
	{
		RECIPE create(ResourceLocation id, Ingredient input, int syphon);
	}
}
