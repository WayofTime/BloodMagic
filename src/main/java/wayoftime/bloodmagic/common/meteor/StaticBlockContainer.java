package wayoftime.bloodmagic.common.meteor;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

public class StaticBlockContainer extends RandomBlockContainer
{
	private Block block;

	public StaticBlockContainer(Block block)
	{
		this.block = block;
	}

	@Override
	public Block getRandomBlock(Random rand, World world)
	{
		return block;
	}

	@Override
	public String getEntry()
	{
		return ForgeRegistries.BLOCKS.getKey(block).toString();
	}
}
