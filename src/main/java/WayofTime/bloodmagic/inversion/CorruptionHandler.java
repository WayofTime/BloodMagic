package WayofTime.bloodmagic.inversion;

import WayofTime.bloodmagic.soul.EnumDemonWillType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

public class CorruptionHandler {
    public static Map<Pair<Block, Integer>, Map<EnumDemonWillType, IBlockState>> corruptBlockMap = new HashMap<>();

    public static void registerBlockCorruption(EnumDemonWillType type, Block block, int meta, IBlockState corruptedState) {
        Pair<Block, Integer> pair = Pair.of(block, meta);
        if (corruptBlockMap.containsKey(pair)) {
            Map<EnumDemonWillType, IBlockState> stateMap = corruptBlockMap.get(pair);
            stateMap.put(type, corruptedState);
        } else {
            Map<EnumDemonWillType, IBlockState> stateMap = new HashMap<>();
            stateMap.put(type, corruptedState);
            corruptBlockMap.put(pair, stateMap);
        }
    }

    public static boolean isBlockCorruptible(World world, EnumDemonWillType type, BlockPos pos, IBlockState state, Block block) {
        int meta = block.getMetaFromState(state);
        Pair<Block, Integer> pair = Pair.of(block, meta);
        if (corruptBlockMap.containsKey(pair)) {
            Map<EnumDemonWillType, IBlockState> stateMap = corruptBlockMap.get(pair);
            return stateMap.containsKey(type);
        }

        return false;
    }

    public static boolean corruptBlock(World world, EnumDemonWillType type, BlockPos pos, IBlockState state, Block block) {
        int meta = block.getMetaFromState(state);
        Pair<Block, Integer> pair = Pair.of(block, meta);
        if (corruptBlockMap.containsKey(pair)) {
            Map<EnumDemonWillType, IBlockState> stateMap = corruptBlockMap.get(pair);
            if (stateMap.containsKey(type)) {
                return world.setBlockState(pos, stateMap.get(type));
            }
        }

        return false;
    }

    /**
     * @param world
     * @param type
     * @param centerPos
     * @param radius
     * @param featheringChance Chance that the block within the featheringDepth is NOT altered.
     * @param featheringDepth
     * @return
     */
    public static boolean corruptSurroundingBlocks(World world, EnumDemonWillType type, BlockPos centerPos, int radius, double featheringChance, double featheringDepth) {
        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                for (int k = -radius; k <= radius; k++) {
                    if (i * i + j * j + k * k > (radius + 0.5) * (radius + 0.5)) {
                        continue;
                    }

                    if (featheringChance > 0 && i * i + j * j + k * k > (radius - featheringDepth + 0.5) * (radius - featheringDepth + 0.5) && world.rand.nextDouble() < featheringChance) {
                        continue;
                    }

                    if (world.isAirBlock(centerPos)) {
                        continue;
                    }

                    BlockPos offsetPos = centerPos.add(i, j, k);
                    IBlockState offsetState = world.getBlockState(offsetPos);
                    Block offsetBlock = offsetState.getBlock();
                    corruptBlock(world, type, offsetPos, offsetState, offsetBlock);
                }
            }
        }
        return false;
    }
}
