package WayofTime.bloodmagic.incense;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class IncenseAltarHandler {
    public static Map<Integer, List<IncenseAltarComponent>> incenseComponentMap = new TreeMap<>();
    //Incense bonus maximum applied for the tier of blocks.
    public static double[] incenseBonuses = new double[]{0.2, 0.6, 1.2, 2, 3, 4.5};
    public static double[] tranquilityRequired = new double[]{0, 6, 14.14, 28, 44.09, 83.14};
    public static int[] roadsRequired = new int[]{0, 1, 4, 6, 8, 10, 12}; //TODO: Change for when the roads are fully implemented

    public static void registerIncenseComponent(int altarLevel, IncenseAltarComponent component) {
        if (incenseComponentMap.containsKey(altarLevel)) {
            incenseComponentMap.get(altarLevel).add(component);
        } else {
            List<IncenseAltarComponent> list = new ArrayList<>();
            list.add(component);
            incenseComponentMap.put(altarLevel, list);
        }
    }

    public static void registerIncenseComponent(int altarLevel, BlockPos offsetPos, Block block, IBlockState state) {
        registerIncenseComponent(altarLevel, new IncenseAltarComponent(offsetPos, block, state));
    }

    public static double getMaxIncenseBonusFromComponents(World world, BlockPos pos) {
        double accumulatedBonus = 0;
        for (int i = 0; i < incenseBonuses.length; i++) {
            double previousBonus = (i <= 0 ? 0 : incenseBonuses[i - 1]);
            double nextBonus = incenseBonuses[i];
            if (!incenseComponentMap.containsKey(i)) {
                accumulatedBonus += (nextBonus - previousBonus);
            } else {
                boolean hasAllComponentsThisTier = true;
                for (IncenseAltarComponent component : incenseComponentMap.get(i)) {
                    BlockPos offsetPos = pos.add(component.getOffset(EnumFacing.NORTH));
                    IBlockState state = world.getBlockState(offsetPos);
                    Block block = state.getBlock();
                    if (component.doesBlockMatch(block, state)) {
                        hasAllComponentsThisTier = false;
                    } else {
                        accumulatedBonus += (nextBonus - previousBonus) / incenseComponentMap.get(i).size();
                    }
                }

                if (!hasAllComponentsThisTier) {
                    break;
                }
            }
        }

        return accumulatedBonus;
    }

    public static double getMaxIncenseBonusFromRoads(int roads) {
        double previousBonus = 0;
        for (int i = 0; i < incenseBonuses.length; i++) {
            if (roads >= roadsRequired[i]) {
                previousBonus = incenseBonuses[i];
            } else {
                return previousBonus;
            }
        }

        return previousBonus;
    }

    public static double getIncenseBonusFromComponents(World world, BlockPos pos, double tranquility, int roads) {
        double maxBonus = Math.min(getMaxIncenseBonusFromComponents(world, pos), getMaxIncenseBonusFromRoads(roads));
        double possibleBonus = 0;

        for (int i = 0; i < incenseBonuses.length; i++) {
            if (tranquility >= tranquilityRequired[i]) {
                possibleBonus = incenseBonuses[i];
            } else if (i >= 1) {
                possibleBonus += (incenseBonuses[i] - possibleBonus) * (tranquility - tranquilityRequired[i - 1]) / (tranquilityRequired[i] - tranquilityRequired[i - 1]);
                break;
            }
        }

        return Math.min(maxBonus, possibleBonus);
    }
}
