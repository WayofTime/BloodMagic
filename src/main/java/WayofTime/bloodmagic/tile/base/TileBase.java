package wayoftime.bloodmagic.tile.base;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Base tile class.
 * <p>
 * Handles data syncing and core data writing/reading.
 */
public abstract class TileBase extends TileEntity
{
	public TileBase(TileEntityType<?> type)
	{
		super(type);
	}

	/**
	 * read method
	 */
	@Override
	public final void read(BlockState state, CompoundNBT compound)
	{
		super.read(state, compound);
		deserializeBase(compound);
		deserialize(compound);
	}

	@Override
	public final CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		serializeBase(compound);
		return serialize(compound);
	}

	/**
	 * Called by {@link #func_230337_a_(BlockState, CompoundNBT)}
	 * <p>
	 * Internal data (such as coordinates) are handled for you. Just read the data
	 * you need.
	 *
	 * @param tagCompound - The tag compound to read from
	 */
	public void deserialize(CompoundNBT tagCompound)
	{

	}

	/**
	 * Package private method for reading base data from the tag compound.
	 *
	 * @param tagCompound - The tag compound to read from
	 * @see TileTicking
	 */
	void deserializeBase(CompoundNBT tagCompound)
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
	public CompoundNBT serialize(CompoundNBT tagCompound)
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
	CompoundNBT serializeBase(CompoundNBT tagCompound)
	{
		return tagCompound;
	}

	public void notifyUpdate()
	{
		BlockState state = getWorld().getBlockState(getPos());
		getWorld().notifyBlockUpdate(getPos(), state, state, 3);
	}

//	// Data syncing
//
//	@Override
//	public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newState)
//	{
//		return oldState.getBlock() != newState.getBlock();
//	}

	@Override
	public final SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(getPos(), -999, getUpdateTag());
	}

//	@Override
//	public void handleUpdateTag(BlockState state, CompoundNBT tag)
//	{
//		read(state, tag);
//	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public final void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		super.onDataPacket(net, pkt);
		handleUpdateTag(getBlockState(), pkt.getNbtCompound());
	}

	@Override
	public final CompoundNBT getUpdateTag()
	{
		return write(new CompoundNBT());
	}

	@Override
	public final void handleUpdateTag(BlockState state, CompoundNBT tag)
	{
		read(state, tag);
	}
}