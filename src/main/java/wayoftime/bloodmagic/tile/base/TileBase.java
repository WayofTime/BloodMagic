package wayoftime.bloodmagic.tile.base;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Base tile class.
 * <p>
 * Handles data syncing and core data writing/reading.
 */
public abstract class TileBase extends BlockEntity
{
	public TileBase(BlockEntityType<?> type)
	{
		super(type);
	}

	/**
	 * read method
	 */
	@Override
	public final void load(BlockState state, CompoundTag compound)
	{
		super.load(state, compound);
		deserializeBase(compound);
		deserialize(compound);
	}

	@Override
	public final CompoundTag save(CompoundTag compound)
	{
		super.save(compound);
		serializeBase(compound);
		return serialize(compound);
	}

	/**
	 * Called by {@link #load(BlockState, CompoundNBT)}
	 * <p>
	 * Internal data (such as coordinates) are handled for you. Just read the data
	 * you need.
	 *
	 * @param tagCompound - The tag compound to read from
	 */
	public void deserialize(CompoundTag tagCompound)
	{

	}

	/**
	 * Package private method for reading base data from the tag compound.
	 *
	 * @param tagCompound - The tag compound to read from
	 * @see TileTicking
	 */
	void deserializeBase(CompoundTag tagCompound)
	{

	}

	/**
	 * Called by {@link #writeToNBT(CompoundNBT)}
	 * <p>
	 * Internal data (such as coordinates) are handled for you. Just read the data
	 * you need.
	 *
	 * @param tagCompound - The tag compound to write to.
	 * @return the modified tag compound
	 */
	public CompoundTag serialize(CompoundTag tagCompound)
	{
		return tagCompound;
	}

	/**
	 * Package private method for writing base data to the tag compound.
	 *
	 * @param tagCompound - The tag compound to write to.
	 * @return the modified tag compound
	 * @see TileTicking
	 */
	CompoundTag serializeBase(CompoundTag tagCompound)
	{
		return tagCompound;
	}

	public void notifyUpdate()
	{
		BlockState state = getLevel().getBlockState(getBlockPos());
		getLevel().sendBlockUpdated(getBlockPos(), state, state, 3);
	}

//	// Data syncing
//
//	@Override
//	public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newState)
//	{
//		return oldState.getBlock() != newState.getBlock();
//	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket()
	{
		return new ClientboundBlockEntityDataPacket(getBlockPos(), -999, getUpdateTag());
	}

//	@Override
//	public void handleUpdateTag(BlockState state, CompoundNBT tag)
//	{
//		read(state, tag);
//	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt)
	{
		super.onDataPacket(net, pkt);
		handleUpdateTag(getBlockState(), pkt.getTag());
	}

	@Override
	public CompoundTag getUpdateTag()
	{
		return save(new CompoundTag());
	}

	@Override
	public void handleUpdateTag(BlockState state, CompoundTag tag)
	{
		load(state, tag);
	}
}