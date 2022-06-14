package wayoftime.bloodmagic.common.meteor;

import java.util.Random;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public class StaticBlockContainer extends RandomBlockContainer
{
	private Block block;

	public StaticBlockContainer(Block block)
	{
		this.block = block;
	}

	@Override
	public Block getRandomBlock(Random rand, Level world)
	{
		return block;
	}

	@Override
	public String getEntry()
	{
		return ForgeRegistries.BLOCKS.getKey(block).toString();
	}
}
