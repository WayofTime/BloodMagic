package WayofTime.bloodmagic.tile;

import WayofTime.bloodmagic.api.Constants;
import lombok.NoArgsConstructor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@NoArgsConstructor
public class TilePhantomBlock extends TileEntity implements ITickable
{
    private int ticksRemaining = 10;

    public TilePhantomBlock(int ticksRemaining)
    {
        this.ticksRemaining = ticksRemaining;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
        this.ticksRemaining = tagCompound.getInteger(Constants.NBT.TICKS_REMAINING);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger(Constants.NBT.TICKS_REMAINING, ticksRemaining);
    }

    @Override
    public void update()
    {
        ticksRemaining--;

        if (ticksRemaining <= 0)
        {
            worldObj.setBlockToAir(getPos());
            worldObj.removeTileEntity(getPos());
        }
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        return new SPacketUpdateTileEntity(getPos(), -999, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        return oldState.getBlock() != newState.getBlock();
    }
}
