package wayoftime.bloodmagic.common.tile.base;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Base class for tiles that tick. Allows disabling the ticking
 * programmatically.
 */
// TODO - Move implementations that depend on existed ticks to new methods from here.
public abstract class TileTicking extends TileBase
{
	private int ticksExisted;
	private boolean shouldTick = true;

	public TileTicking(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
	}

	public final void tick()
	{
		if (shouldTick())
		{
			ticksExisted++;
			onUpdate();
		}
	}

	@Override
	void deserializeBase(CompoundTag tagCompound)
	{
		this.ticksExisted = tagCompound.getInt("ticksExisted");
		this.shouldTick = tagCompound.getBoolean("shouldTick");
	}

	@Override
	CompoundTag serializeBase(CompoundTag tagCompound)
	{
		tagCompound.putInt("ticksExisted", getTicksExisted());
		tagCompound.putBoolean("shouldTick", shouldTick());
		return tagCompound;
	}

	/**
	 * Called every tick that {@link #shouldTick()} is true.
	 */
	public abstract void onUpdate();

	public int getTicksExisted()
	{
		return ticksExisted;
	}

	public void resetLifetime()
	{
		ticksExisted = 0;
	}

	public boolean shouldTick()
	{
		return shouldTick;
	}

	public void setShouldTick(boolean shouldTick)
	{
		this.shouldTick = shouldTick;
	}
}