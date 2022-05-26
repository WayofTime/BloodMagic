package wayoftime.bloodmagic.common.meteor;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RandomBlockTagContainer extends RandomBlockContainer
{
	private ITag<Block> tag;
	private int index = -1;

	public RandomBlockTagContainer(ITag<Block> tag, int index)
	{
		this.tag = tag;
		this.index = index;
	}

	@Override
	public Block getRandomBlock(Random rand, World world)
	{
		if (tag.getAllElements().size() <= 0)
		{
			return null;
		}

		if (index >= 0 && index < tag.getAllElements().size())
		{
			return tag.getAllElements().get(index);
		}

		return tag.getRandomElement(rand);
	}

	@Override
	public String getEntry()
	{
		ResourceLocation rl = TagCollectionManager.getManager().getBlockTags().getDirectIdFromTag(tag);
		String entry = "#" + rl.toString();
		if (index >= 0)
		{
			entry = entry + "#" + index;
		}

		return entry;
	}
}
