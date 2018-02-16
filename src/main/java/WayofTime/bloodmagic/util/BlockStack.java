package WayofTime.bloodmagic.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Deprecated
public class BlockStack {
    private final Block block;
    private final int meta;
    private final IBlockState state;

    public BlockStack(Block block, int meta) {
        this.block = block;
        this.meta = meta;
        this.state = block.getStateFromMeta(meta);
    }

    public BlockStack(Block block) {
        this(block, 0);
    }

    public ItemStack getItemStack() {
        return new ItemStack(block, 1, meta);
    }

    public Block getBlock() {
        return block;
    }

    public int getMeta() {
        return meta;
    }

    public IBlockState getState() {
        return state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BlockStack)) return false;

        BlockStack that = (BlockStack) o;

        if (meta != that.meta) return false;
        return block != null ? block.equals(that.block) : that.block == null;
    }

    @Override
    public int hashCode() {
        int result = block != null ? block.hashCode() : 0;
        result = 31 * result + meta;
        return result;
    }

    @Override
    public String toString() {
        return getBlock().getRegistryName() + ":" + getMeta();
    }

    public static BlockStack getStackFromPos(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return new BlockStack(state.getBlock(), state.getBlock().getMetaFromState(state));
    }
}
