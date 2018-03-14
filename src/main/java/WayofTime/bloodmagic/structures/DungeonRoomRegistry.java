package WayofTime.bloodmagic.structures;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class DungeonRoomRegistry {
    public static Map<DungeonRoom, Integer> dungeonWeightMap = new HashMap<>();
    private static int totalWeight = 0;

    public static void registerDungeonRoom(DungeonRoom room, int weight) {
        dungeonWeightMap.put(room, weight);
        totalWeight += weight;
    }

    public static DungeonRoom getRandomDungeonRoom(Random rand) {
        int wantedWeight = rand.nextInt(totalWeight);
        for (Entry<DungeonRoom, Integer> entry : dungeonWeightMap.entrySet()) {
            wantedWeight -= entry.getValue();
            if (wantedWeight < 0) {
                return entry.getKey();
            }
        }

        return null;
    }
}
