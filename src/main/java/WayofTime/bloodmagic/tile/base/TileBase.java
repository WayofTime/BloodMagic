package WayofTime.bloodmagic.tile.base;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
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
    public final void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        deserializeBase(compound);
        deserialize(compound);
    }

    @Override
    public final NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        serializeBase(compound);
        return serialize(compound);
    }

    /**
     * Called by {@link #readFromNBT(NBTTagCompound)}
     * <p>
     * Internal data (such as coordinates) are handled for you. Just read the data you need.
     *
     * @param tagCompound - The tag compound to read from
     */
    public void deserialize(NBTTagCompound tagCompound) {

    }

    /**
     * Package private method for reading base data from the tag compound.
     *
     * @param tagCompound - The tag compound to read from
     * @see TileTicking
     */
    void deserializeBase(NBTTagCompound tagCompound) {

    }

    /**
     * Called by {@link #writeToNBT(NBTTagCompound)}
     * <p>
     * Internal data (such as coordinates) are handled for you. Just read the data you need.
     *
     * @param tagCompound - The tag compound to write to.
     * @return the modified tag compound
     */
    public NBTTagCompound serialize(NBTTagCompound tagCompound) {
        return tagCompound;
    }


    /**
     * Package private method for writing base data to the tag compound.
     *
     * @param tagCompound - The tag compound to write to.
     * @return the modified tag compound
     * @see TileTicking
     */
    NBTTagCompound serializeBase(NBTTagCompound tagCompound) {
        return tagCompound;
    }

    public void notifyUpdate() {
        IBlockState state = getWorld().getBlockState(getPos());
        getWorld().notifyBlockUpdate(getPos(), state, state, 3);
    }

    // Data syncing

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public final SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), -999, writeToNBT(new NBTTagCompound()));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public final void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
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
    public final NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public final void handleUpdateTag(NBTTagCompound tag) {
        readFromNBT(tag);
    }
}
