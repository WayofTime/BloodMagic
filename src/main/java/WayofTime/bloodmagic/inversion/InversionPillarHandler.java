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
    public static final double farthestDistanceSquared = 16 * 16;
    public static Map<Integer, Map<EnumDemonWillType, List<BlockPos>>> pillarMap = new HashMap<Integer, Map<EnumDemonWillType, List<BlockPos>>>();
    public static Map<Integer, Map<EnumDemonWillType, Map<BlockPos, List<BlockPos>>>> nearPillarMap = new HashMap<Integer, Map<EnumDemonWillType, Map<BlockPos, List<BlockPos>>>>();

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

    public static boolean removePillarFromMap(World world, EnumDemonWillType type, BlockPos pos)
    {
        int dim = world.provider.getDimension();
        if (pillarMap.containsKey(dim))
        {
            Map<EnumDemonWillType, List<BlockPos>> willMap = pillarMap.get(dim);
            if (willMap.containsKey(type))
            {
                if (willMap.get(type).contains(pos))
                {
                    return willMap.get(type).remove(pos);
                } else
                {
                    return false;
                }
            } else
            {
                return false;
            }
        } else
        {
            return false;
        }
    }

    private static void onPillarRemoved(World world, EnumDemonWillType type, BlockPos pos)
    {
        int dim = world.provider.getDimension();
        if (nearPillarMap.containsKey(dim))
        {
            Map<EnumDemonWillType, Map<BlockPos, List<BlockPos>>> willMap = nearPillarMap.get(dim);
            if (willMap.containsKey(type))
            {
                Map<BlockPos, List<BlockPos>> posMap = willMap.get(type);
                List<BlockPos> posList = posMap.get(pos);
                if (posList != null)
                {
                    Iterator<BlockPos> itr = posList.iterator();
                    while (itr.hasNext())
                    {
                        BlockPos checkPos = itr.next();
                        List<BlockPos> checkPosList = posMap.get(checkPos);
                        if (checkPosList != null)
                        {
                            checkPosList.remove(pos);
                        }
                    }

                    posMap.remove(pos);
                }
            }
        }
    }

    //TODO: Change to use the nearPillarMap.
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
            List<BlockPos> newList = new ArrayList<BlockPos>();

            Iterator<BlockPos> itr = posList.iterator();
            while (itr.hasNext())
            {
                BlockPos newPos = itr.next();
                if (newPos.equals(pos))
                {
                    continue;
                }
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
