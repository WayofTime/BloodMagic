package wayoftime.bloodmagic.common.meteor;

import java.util.Random;

import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.SerializationTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class RandomBlockTagContainer extends RandomBlockContainer
{
	private Tag<Block> tag;
	private int index = -1;

	public RandomBlockTagContainer(Tag<Block> tag, int index)
	{
		this.tag = tag;
		this.index = index;
	}

	@Override
	public Block getRandomBlock(Random rand, Level world)
	{
		if (tag.getValues().size() <= 0)
		{
			return null;
		}

		if (index >= 0 && index < tag.getValues().size())
		{
			return tag.getValues().get(index);
		}

		return tag.getRandomElement(rand);
	}

	@Override
	public String getEntry()
	{
		ResourceLocation rl = SerializationTags.getInstance().getBlocks().getId(tag);
		String entry = "#" + rl.toString();
		if (index >= 0)
		{
			entry = entry + "#" + index;
		}

		return entry;
	}
}
