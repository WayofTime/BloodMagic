package WayofTime.bloodmagic.api;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameData;

@Getter
@EqualsAndHashCode
public class BlockStack
{
    private final Block block;
    private final int meta;

    public BlockStack(Block block, int meta)
    {
        this.block = block;
        this.meta = meta;
    }

    public BlockStack(Block block)
    {
        this(block, 0);
    }

    public static BlockStack getStackFromPos(World world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        return new BlockStack(state.getBlock(), state.getBlock().getMetaFromState(state));
    }

    @Override
    public String toString()
    {
        return GameData.getBlockRegistry().getNameForObject(block) + ":" + meta;
    }
}
