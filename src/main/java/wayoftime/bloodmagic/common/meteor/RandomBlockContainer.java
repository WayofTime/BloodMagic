package wayoftime.bloodmagic.common.meteor;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class RandomBlockContainer
{
	public RandomBlockContainer()
	{

	}

	public abstract Block getRandomBlock(Random rand, World world);

	public static RandomBlockContainer parseEntry(String str)
	{
		if (str.startsWith("#"))
		{
			String[] splitStr = str.split("#");

			int index = -1;
			String tagName = splitStr[1];
			ITag<Block> tag = TagCollectionManager.getInstance().getBlocks().getTag(new ResourceLocation(tagName));

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
