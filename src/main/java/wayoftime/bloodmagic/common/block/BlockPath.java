package wayoftime.bloodmagic.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wayoftime.bloodmagic.incense.IIncensePath;

public class BlockPath extends Block implements IIncensePath
{
	protected final int pathLevel;

	public BlockPath(int pathLevel, Properties properties)
	{
		super(properties);
		this.pathLevel = pathLevel;
	}

	@Override
	public int getLevelOfPath(World world, BlockPos pos, BlockState state)
	{
		return pathLevel;
	}
}
