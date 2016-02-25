package WayofTime.bloodmagic.tile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.registry.ModBlocks;

import com.google.common.base.Strings;

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
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger(Constants.NBT.TICKS_REMAINING, ticksRemaining);
        tagCompound.setString(Constants.NBT.CONTAINED_BLOCK_NAME, Strings.isNullOrEmpty(containedBlockName) ? "" : containedBlockName);
        tagCompound.setInteger(Constants.NBT.CONTAINED_BLOCK_META, containedBlockMeta);
    }

    @Override
    public void update()
    {
        ticksRemaining--;

        if (ticksRemaining <= 0)
        {
            returnContainedBlock();
        }
    }

    private void setContainedBlockInfo(IBlockState blockState)
    {
        containedBlockName = Block.blockRegistry.getNameForObject(blockState.getBlock()).toString();
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
            block = Block.getBlockFromName(containedBlockName);

        if (block != null && worldObj.setBlockState(pos, block.getStateFromMeta(containedBlockMeta)))
            worldObj.markBlockForUpdate(pos);
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
