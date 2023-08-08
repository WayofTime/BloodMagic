package wayoftime.bloodmagic.common.meteor;

import java.util.Optional;
import java.util.Random;

import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import org.apache.commons.lang3.tuple.Pair;

import com.google.gson.JsonObject;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import wayoftime.bloodmagic.util.Constants;

public abstract class RandomBlockContainer
{
	public RandomBlockContainer()
	{

	}

	public abstract Block getRandomBlock(RandomSource rand, Level world);

	public static RandomBlockContainer parseEntry(String str)
	{
		if (str.startsWith("#"))
		{
			String[] splitStr = str.split("#");

			int index = -1;
			String tagName = splitStr[1];
//			ForgeRegistries.BLOCKS.tags().tag
			TagKey<Block> tag = TagKey.create(Registries.BLOCK, new ResourceLocation(tagName));
//			TagKey<Block> tag = SerializationTags.getInstance().getBlocks().getTag(new ResourceLocation(tagName));

			if (splitStr.length > 2)
			{
				try
				{
					index = Integer.parseInt(splitStr[2]);
				} catch (NumberFormatException ex)
				{

				}
			}

			return new RandomBlockTagContainer(tag, index);
		} else if (str.startsWith(";"))
		{
			String[] splitStr = str.split(";");
			String fluidName = splitStr[1];

			return parseFluidEntry(fluidName);
		} else
		{
			return parseBlockEntry(str);
		}
	}

	public abstract JsonObject serialize(int weight);

	public abstract JsonObject serialize();

	public static Pair<RandomBlockContainer, Integer> deserializeWeightedPair(JsonObject obj)
	{
		if (obj.has(Constants.JSON.TAG))
		{
			String entry = GsonHelper.getAsString(obj, Constants.JSON.TAG);
			int weight = GsonHelper.getAsInt(obj, Constants.JSON.WEIGHT);
			if (obj.has(Constants.JSON.INDEX))
			{
				// Using new method
				int tagIndex = GsonHelper.getAsInt(obj, Constants.JSON.INDEX);
				RandomBlockContainer container = RandomBlockContainer.parseTagEntry(entry, tagIndex);

				if (container != null)
				{
					return Pair.of(container, weight);
				}
			} else
			{
				// Using the old method of parsing the entry
				RandomBlockContainer container = RandomBlockContainer.parseEntry(entry);
//				ITag<Block> itag = TagCollectionManager.getManager().getBlockTags().get(new ResourceLocation(JSONUtils.getString(obj, Constants.JSON.TAG)));

				if (container != null)
				{
					return Pair.of(container, weight);
				}
			}
		} else if (obj.has(Constants.JSON.FLUID))
		{
			String entry = GsonHelper.getAsString(obj, Constants.JSON.FLUID);
			int weight = GsonHelper.getAsInt(obj, Constants.JSON.WEIGHT);
			RandomBlockContainer container = RandomBlockContainer.parseFluidEntry(entry);

			if (container != null)
			{
				return Pair.of(container, weight);
			}
		} else if (obj.has(Constants.JSON.BLOCK))
		{
			String entry = GsonHelper.getAsString(obj, Constants.JSON.BLOCK);
			int weight = GsonHelper.getAsInt(obj, Constants.JSON.WEIGHT);
			RandomBlockContainer container = RandomBlockContainer.parseBlockEntry(entry);

			if (container != null)
			{
				return Pair.of(container, weight);
			}
		}

		return null;
	}

	public static RandomBlockContainer deserializeContainer(JsonObject obj)
	{
		if (obj.has(Constants.JSON.TAG))
		{
			String entry = GsonHelper.getAsString(obj, Constants.JSON.TAG);
			if (obj.has(Constants.JSON.INDEX))
			{
				// Using new method
				int tagIndex = GsonHelper.getAsInt(obj, Constants.JSON.INDEX);
				RandomBlockContainer container = RandomBlockContainer.parseTagEntry(entry, tagIndex);

				if (container != null)
				{
					return container;
				}
			} else
			{
				// Using the old method of parsing the entry
				RandomBlockContainer container = RandomBlockContainer.parseEntry(entry);
//				ITag<Block> itag = TagCollectionManager.getManager().getBlockTags().get(new ResourceLocation(JSONUtils.getString(obj, Constants.JSON.TAG)));

				if (container != null)
				{
					return container;
				}
			}
		} else if (obj.has(Constants.JSON.FLUID))
		{
			String entry = GsonHelper.getAsString(obj, Constants.JSON.FLUID);
			RandomBlockContainer container = RandomBlockContainer.parseFluidEntry(entry);

			if (container != null)
			{
				return container;
			}
		} else if (obj.has(Constants.JSON.BLOCK))
		{
			String entry = GsonHelper.getAsString(obj, Constants.JSON.BLOCK);
			RandomBlockContainer container = RandomBlockContainer.parseBlockEntry(entry);

			if (container != null)
			{
				return container;
			}
		}

		return null;
	}

	public static RandomBlockContainer parseTagEntry(String str, int index)
	{
		TagKey<Block> tag = TagKey.create(Registries.BLOCK, new ResourceLocation(str));
		return new RandomBlockTagContainer(tag, index);
	}

	public static RandomBlockContainer parseBlockEntry(String str)
	{
		Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(str));
		if (block == null)
		{
			return null;
		}

		return new StaticBlockContainer(block);
	}

	public static RandomBlockContainer parseFluidEntry(String str)
	{
		String fluidName = str;
		Optional<Holder<Fluid>> holderOptional = ForgeRegistries.FLUIDS.getHolder(new ResourceLocation(fluidName));
		if (holderOptional.isPresent())
		{
			Fluid fluid = holderOptional.get().value();
			return new FluidBlockContainer(fluid);
		}

		return null;
	}

	public abstract String getEntry();
}
