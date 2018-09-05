package com.wayoftime.bloodmagic.tile.base;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Base tile class.
 * <p>
 * Handles data syncing and core data writing/reading.
 */
public class TileBase extends TileEntity {

    @Override
    public final void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if (compound.hasKey("baseData"))
            deserializeBase(compound.getCompoundTag("baseData"));

        deserialize(compound.getCompoundTag("tileData"));
    }

    @Override
    public final NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        NBTTagCompound base = serializeBase();
        if (base != null)
            compound.setTag("baseData", base);

        NBTTagCompound tileData = new NBTTagCompound();
        serialize(tileData);
        compound.setTag("tileData", tileData);
        return compound;
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
    public void serialize(NBTTagCompound tagCompound) {

    }

    /**
     * Package private method for writing base data to the tag compound.
     *
     * @return the modified tag compound
     */
    NBTTagCompound serializeBase() {
        return null;
    }

    protected final NBTTagCompound writeCurrentPos(NBTTagCompound compound) {
        compound.setString("id", getKey(getClass()).toString());
        compound.setInteger("x", this.pos.getX());
        compound.setInteger("y", this.pos.getY());
        compound.setInteger("z", this.pos.getZ());
        return compound;
    }

    protected final void readCurrentPos(NBTTagCompound tag) {
        this.pos = new BlockPos(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"));
    }

    public void notifyUpdate() {
        IBlockState state = world.getBlockState(pos);
        getWorld().notifyBlockUpdate(pos, state, state, 3);
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
    public final void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        readFromNBT(tag);
    }
}
