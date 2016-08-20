package WayofTime.bloodmagic.structures;

import java.util.HashMap;
import java.util.Map;

public class DungeonRoomRegistry
{
    public static Map<DungeonRoom, Integer> dungeonWeightMap = new HashMap<DungeonRoom, Integer>();

    public static void registerDungeonRoom(DungeonRoom room, int weight)
    {
        dungeonWeightMap.put(room, weight);
    }

}
