package wayoftime.bloodmagic.common.meteor;

import java.util.Random;

import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.SerializationTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class RandomBlockContainer
{
	public RandomBlockContainer()
	{

	}

	public abstract Block getRandomBlock(Random rand, Level world);

	public static RandomBlockContainer parseEntry(String str)
	{
		if (str.startsWith("#"))
		{
			String[] splitStr = str.split("#");

			int index = -1;
			String tagName = splitStr[1];
			Tag<Block> tag = SerializationTags.getInstance().getBlocks().getTag(new ResourceLocation(tagName));

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
		} else
		{
			Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(str));
			if (block == null)
			{
				return null;
			}

			return new StaticBlockContainer(block);
		}
	}

	public abstract String getEntry();
}
