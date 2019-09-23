package WayofTime.bloodmagic.tile;

import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.core.RegistrarBloodMagicBlocks;
import WayofTime.bloodmagic.tile.base.TileTicking;
import com.google.common.base.Strings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class TileSpectralBlock extends TileTicking {
    private int ticksRemaining;
    private String containedBlockName;
    private int containedBlockMeta;

    public TileSpectralBlock() {
    }

    @Override
    public void deserialize(CompoundNBT tagCompound) {
        ticksRemaining = tagCompound.getInt(Constants.NBT.TICKS_REMAINING);
        containedBlockName = tagCompound.getString(Constants.NBT.CONTAINED_BLOCK_NAME);
        containedBlockMeta = tagCompound.getInt(Constants.NBT.CONTAINED_BLOCK_META);
    }

    @Override
    public CompoundNBT serialize(CompoundNBT tagCompound) {
        tagCompound.putInt(Constants.NBT.TICKS_REMAINING, ticksRemaining);
        tagCompound.putString(Constants.NBT.CONTAINED_BLOCK_NAME, Strings.isNullOrEmpty(containedBlockName) ? "" : containedBlockName);
        tagCompound.putInt(Constants.NBT.CONTAINED_BLOCK_META, containedBlockMeta);
        return tagCompound;
    }

    @Override
    public void onUpdate() {
        if (getWorld().isRemote) {
            return;
        }

        ticksRemaining--;

        if (ticksRemaining <= 0) {
            returnContainedBlock();
        }
    }

    private void setContainedBlockInfo(BlockState blockState) {
        containedBlockName = blockState.getBlock().getRegistryName().toString();
        containedBlockMeta = blockState.getBlock().getMetaFromState(blockState);
    }

    private void setDuration(int duration) {
        ticksRemaining = duration;
    }

    public void resetDuration(int reset) {
        if (ticksRemaining < reset)
            ticksRemaining = reset;
    }

    public void returnContainedBlock() {
        Block block = null;

        if (!Strings.isNullOrEmpty(containedBlockName))
            block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(containedBlockName));

        if (block != null && getWorld().setBlockState(pos, block.getStateFromMeta(containedBlockMeta)))
            getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
    }

    public static void createSpectralBlock(World world, BlockPos blockPos, int duration) {
        if (world.isAirBlock(blockPos))
            return;
        BlockState cachedState = world.getBlockState(blockPos);
        world.setBlockState(blockPos, RegistrarBloodMagicBlocks.SPECTRAL.getDefaultState());
        TileSpectralBlock tile = (TileSpectralBlock) world.getTileEntity(blockPos);
        tile.setContainedBlockInfo(cachedState);
        tile.setDuration(duration);
    }
}
