package wayoftime.bloodmagic.incense;

import net.minecraft.world.level.block.Block;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class IncenseAltarComponent
{
	public final BlockPos offsetPos;
	public final Block block;

	public IncenseAltarComponent(BlockPos offsetPos, Block block)
	{
		this.offsetPos = offsetPos;
		this.block = block;
	}

	public boolean doesBlockMatch(Block block)
	{
		return this.block == block;
	}

	/**
	 * Base rotation is north.
	 */
	public BlockPos getOffset(Direction rotation)
	{
		return new BlockPos(this.getX(rotation), offsetPos.getY(), this.getZ(rotation));
	}

	public int getX(Direction direction)
	{
		switch (direction)
		{
		case EAST:
			return -this.offsetPos.getZ();
		case SOUTH:
			return -this.offsetPos.getX();
		case WEST:
			return this.offsetPos.getZ();
		default:
			return this.offsetPos.getX();
		}
	}

	public int getZ(Direction direction)
	{
		switch (direction)
		{
		case EAST:
			return this.offsetPos.getX();
		case SOUTH:
			return -this.offsetPos.getZ();
		case WEST:
			return -this.offsetPos.getX();
		default:
			return this.offsetPos.getZ();
		}
	}
}
