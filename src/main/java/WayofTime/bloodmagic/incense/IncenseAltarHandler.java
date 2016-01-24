package WayofTime.bloodmagic.incense;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class IncenseAltarHandler
{
    public static Map<Integer, List<IncenseAltarComponent>> incenseComponentMap = new TreeMap<Integer, List<IncenseAltarComponent>>();
    //Incense bonus maximum applied for the tier of blocks.
    public static double[] incenseBonuses = new double[] { 0.2 };

    public static void registerIncenseComponent(int altarLevel, IncenseAltarComponent component)
    {
        if (incenseComponentMap.containsKey(altarLevel))
        {
            incenseComponentMap.get(altarLevel).add(component);
        } else
        {
            List<IncenseAltarComponent> list = new ArrayList<IncenseAltarComponent>();
            list.add(component);
            incenseComponentMap.put(altarLevel, list);
        }
    }

    public static void registerIncenseComponent(int altarLevel, BlockPos offsetPos, Block block, IBlockState state)
    {
        registerIncenseComponent(altarLevel, new IncenseAltarComponent(offsetPos, block, state));
    }

    public static double getIncenseBonusFromComponents(World world, BlockPos pos)
    {
        double accumulatedBonus = 0;
        for (int i = 0; i < incenseBonuses.length; i++)
        {
            double previousBonus = (i <= 0 ? 0 : incenseBonuses[i - 1]);
            double nextBonus = incenseBonuses[i];
            if (!incenseComponentMap.containsKey(i))
            {
                accumulatedBonus += (nextBonus - previousBonus);
            } else
            {
                boolean hasAllComponentsThisTier = true;
                for (IncenseAltarComponent component : incenseComponentMap.get(i))
                {
                    BlockPos offsetPos = pos.add(component.getOffset(EnumFacing.NORTH));
                    IBlockState state = world.getBlockState(offsetPos);
                    Block block = state.getBlock();
                    if (component.doesBlockMatch(block, state))
                    {
                        hasAllComponentsThisTier = false;
                    } else
                    {
                        accumulatedBonus += (nextBonus - previousBonus) / incenseComponentMap.get(i).size();
                    }
                }

                if (!hasAllComponentsThisTier)
                {
                    break;
                }
            }
        }

        return accumulatedBonus;
    }
}
