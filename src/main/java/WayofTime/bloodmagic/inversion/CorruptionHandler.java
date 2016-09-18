package WayofTime.bloodmagic.inversion;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.apache.commons.lang3.tuple.Pair;

import WayofTime.bloodmagic.api.soul.EnumDemonWillType;

public class CorruptionHandler
{
    public static Map<Pair<Block, Integer>, Map<EnumDemonWillType, IBlockState>> corruptBlockMap = new HashMap<Pair<Block, Integer>, Map<EnumDemonWillType, IBlockState>>();

    public static void registerBlockCorruption(EnumDemonWillType type, Block block, int meta, IBlockState corruptedState)
    {
        Pair<Block, Integer> pair = Pair.of(block, meta);
        if (corruptBlockMap.containsKey(pair))
        {
            Map<EnumDemonWillType, IBlockState> stateMap = corruptBlockMap.get(pair);
            stateMap.put(type, corruptedState);
        } else
        {
            Map<EnumDemonWillType, IBlockState> stateMap = new HashMap<EnumDemonWillType, IBlockState>();
            stateMap.put(type, corruptedState);
            corruptBlockMap.put(pair, stateMap);
        }
    }

    public static boolean isBlockCorruptible(World world, EnumDemonWillType type, BlockPos pos, IBlockState state, Block block)
    {
        int meta = block.getMetaFromState(state);
        Pair<Block, Integer> pair = Pair.of(block, meta);
        if (corruptBlockMap.containsKey(pair))
        {
            Map<EnumDemonWillType, IBlockState> stateMap = corruptBlockMap.get(pair);
            return stateMap.containsKey(type);
        }

        return false;
    }

    public static boolean corruptBlock(World world, EnumDemonWillType type, BlockPos pos, IBlockState state, Block block)
    {
        int meta = block.getMetaFromState(state);
        Pair<Block, Integer> pair = Pair.of(block, meta);
        if (corruptBlockMap.containsKey(pair))
        {
            Map<EnumDemonWillType, IBlockState> stateMap = corruptBlockMap.get(pair);
            if (stateMap.containsKey(type))
            {
                return world.setBlockState(pos, stateMap.get(type));
            }
        }

        return false;
    }

    public static boolean corruptSurroundingBlocks(World world, EnumDemonWillType type, BlockPos centerPos, int radius)
    {
        return false;
    }
}
