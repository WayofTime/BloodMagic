package WayofTime.bloodmagic.tile.base;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Base tile class.
 * <p>
 * Handles data syncing and core data writing/reading.
 */
public class TileBase extends TileEntity {
    @Override
    public final void readFromNBT(CompoundNBT compound) {
        super.readFromNBT(compound);
        deserializeBase(compound);
        deserialize(compound);
    }

    @Override
    public final CompoundNBT writeToNBT(CompoundNBT compound) {
        super.writeToNBT(compound);
        serializeBase(compound);
        return serialize(compound);
    }

    /**
     * Called by {@link #readFromNBT(CompoundNBT)}
     * <p>
     * Internal data (such as coordinates) are handled for you. Just read the data you need.
     *
     * @param tagCompound - The tag compound to read from
     */
    public void deserialize(CompoundNBT tagCompound) {

    }

    /**
     * Package private method for reading base data from the tag compound.
     *
     * @param tagCompound - The tag compound to read from
     * @see TileTicking
     */
    void deserializeBase(CompoundNBT tagCompound) {

    }

    /**
     * Called by {@link #writeToNBT(CompoundNBT)}
     * <p>
     * Internal data (such as coordinates) are handled for you. Just read the data you need.
     *
     * @param tagCompound - The tag compound to write to.
     * @return the modified tag compound
     */
    public CompoundNBT serialize(CompoundNBT tagCompound) {
        return tagCompound;
    }


    /**
     * Package private method for writing base data to the tag compound.
     *
     * @param tagCompound - The tag compound to write to.
     * @return the modified tag compound
     * @see TileTicking
     */
    CompoundNBT serializeBase(CompoundNBT tagCompound) {
        return tagCompound;
    }

    public void notifyUpdate() {
        BlockState state = getWorld().getBlockState(getPos());
        getWorld().notifyBlockUpdate(getPos(), state, state, 3);
    }

    // Data syncing

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public final SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(getPos(), -999, writeToNBT(new CompoundNBT()));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public final void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());
        onDataPacketClientReceived();
    }

    /**
     * Hook for performing client side updates after data packets are received and processed
     */
    protected void onDataPacketClientReceived() {
        // noop
    }

    @Override
    public final CompoundNBT getUpdateTag() {
        return writeToNBT(new CompoundNBT());
    }

    @Override
    public final void handleUpdateTag(CompoundNBT tag) {
        readFromNBT(tag);
    }
}
