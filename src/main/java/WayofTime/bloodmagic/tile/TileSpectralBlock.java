package WayofTime.bloodmagic.tile;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.registry.ModBlocks;
import com.google.common.base.Strings;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileSpectralBlock extends TileEntity implements ITickable
{
    private int ticksRemaining;
    private String containedBlockName;
    private int containedBlockMeta;

    public TileSpectralBlock()
    {
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
        ticksRemaining = tagCompound.getInteger(Constants.NBT.TICKS_REMAINING);
        containedBlockName = tagCompound.getString(Constants.NBT.CONTAINED_BLOCK_NAME);
        containedBlockMeta = tagCompound.getInteger(Constants.NBT.CONTAINED_BLOCK_META);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger(Constants.NBT.TICKS_REMAINING, ticksRemaining);
        tagCompound.setString(Constants.NBT.CONTAINED_BLOCK_NAME, Strings.isNullOrEmpty(containedBlockName) ? "" : containedBlockName);
        tagCompound.setInteger(Constants.NBT.CONTAINED_BLOCK_META, containedBlockMeta);
        return tagCompound;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        return new SPacketUpdateTileEntity(getPos(), -999, nbt);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag)
    {
        readFromNBT(tag);
    }

    @Override
    public void update()
    {
        if (worldObj.isRemote)
        {
            return;
        }

        ticksRemaining--;

        if (ticksRemaining <= 0)
        {
            returnContainedBlock();
        }
    }

    private void setContainedBlockInfo(IBlockState blockState)
    {
        containedBlockName = blockState.getBlock().getRegistryName().toString();
        containedBlockMeta = blockState.getBlock().getMetaFromState(blockState);
    }

    private void setDuration(int duration)
    {
        ticksRemaining = duration;
    }

    public void resetDuration(int reset)
    {
        if (ticksRemaining < reset)
            ticksRemaining = reset;
    }

    public void returnContainedBlock()
    {
        Block block = null;

        if (!Strings.isNullOrEmpty(containedBlockName))
            block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(containedBlockName));

        if (block != null && worldObj.setBlockState(pos, block.getStateFromMeta(containedBlockMeta)))
            getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
    }

    public static void createSpectralBlock(World world, BlockPos blockPos, int duration)
    {
        if (world.isAirBlock(blockPos))
            return;
        IBlockState cachedState = world.getBlockState(blockPos);
        world.setBlockState(blockPos, ModBlocks.spectralBlock.getDefaultState());
        TileSpectralBlock tile = (TileSpectralBlock) world.getTileEntity(blockPos);
        tile.setContainedBlockInfo(cachedState);
        tile.setDuration(duration);
    }
}
