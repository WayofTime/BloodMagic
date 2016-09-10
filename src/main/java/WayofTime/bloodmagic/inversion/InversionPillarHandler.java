package WayofTime.bloodmagic.inversion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.tile.TileInversionPillar;

public class InversionPillarHandler
{
    public static Map<Integer, Map<EnumDemonWillType, List<BlockPos>>> pillarMap = new HashMap<Integer, Map<EnumDemonWillType, List<BlockPos>>>();

    public static boolean addPillarToMap(World world, EnumDemonWillType type, BlockPos pos)
    {
        int dim = world.provider.getDimension();
        if (pillarMap.containsKey(dim))
        {
            Map<EnumDemonWillType, List<BlockPos>> willMap = pillarMap.get(dim);
            if (willMap.containsKey(type))
            {
                if (!willMap.get(type).contains(pos))
                {
                    return willMap.get(type).add(pos);
                } else
                {
                    return false;
                }
            } else
            {
                List<BlockPos> posList = new ArrayList<BlockPos>();
                posList.add(pos);
                willMap.put(type, posList);
                return true;
            }
        } else
        {
            Map<EnumDemonWillType, List<BlockPos>> willMap = new HashMap<EnumDemonWillType, List<BlockPos>>();
            List<BlockPos> posList = new ArrayList<BlockPos>();
            posList.add(pos);

            willMap.put(type, posList);
            pillarMap.put(dim, willMap);
            return true;
        }
    }

    public static List<BlockPos> getNearbyPillars(World world, EnumDemonWillType type, BlockPos pos)
    {
        int dim = world.provider.getDimension();
        List<BlockPos> posList = new ArrayList<BlockPos>();
        if (pillarMap.containsKey(dim))
        {
            Map<EnumDemonWillType, List<BlockPos>> willMap = pillarMap.get(dim);
            posList = willMap.get(type);
        }

        if (posList != null)
        {
            posList.remove(pos);

            List<BlockPos> newList = new ArrayList<BlockPos>();

            Iterator<BlockPos> itr = posList.iterator();
            while (itr.hasNext())
            {
                BlockPos newPos = itr.next();
                if (world.getTileEntity(newPos) instanceof TileInversionPillar) //Make this check... more efficient somehow.
                {
                    newList.add(newPos);
                }
            }

            return newList;
        } else
        {
            return new ArrayList<BlockPos>();
        }
    }
}
