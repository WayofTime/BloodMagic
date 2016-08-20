package WayofTime.bloodmagic.structures;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DungeonRoomRegistry
{
    public static Map<DungeonRoom, Integer> dungeonWeightMap = new HashMap<DungeonRoom, Integer>();

    public static void registerDungeonRoom(DungeonRoom room, int weight)
    {
        dungeonWeightMap.put(room, weight);
    }

    public static DungeonRoom getRandomDungeonRoom(Random rand)
    {
        for (DungeonRoom room : dungeonWeightMap.keySet())
        {
            return room;
        }

        return null;
    }
}
