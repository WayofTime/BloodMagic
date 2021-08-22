package wayoftime.bloodmagic.structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class DungeonRoomRegistry
{
	public static Map<DungeonRoom, Integer> dungeonWeightMap = new HashMap<>();
	public static Map<String, List<DungeonRoom>> dungeonStartingRoomMap = new HashMap<>();
	private static int totalWeight = 0;

	public static void registerDungeonRoom(DungeonRoom room, int weight)
	{
		dungeonWeightMap.put(room, weight);
		totalWeight += weight;
	}

	public static void registerStarterDungeonRoom(DungeonRoom room, String key)
	{
		if (dungeonStartingRoomMap.containsKey(key))
		{
			dungeonStartingRoomMap.get(key).add(room);
		} else
		{
			List<DungeonRoom> roomList = new ArrayList<>();
			roomList.add(room);
			dungeonStartingRoomMap.put(key, roomList);
		}
	}

	public static DungeonRoom getRandomDungeonRoom(Random rand)
	{
		int wantedWeight = rand.nextInt(totalWeight);
		for (Entry<DungeonRoom, Integer> entry : dungeonWeightMap.entrySet())
		{
			wantedWeight -= entry.getValue();
			if (wantedWeight < 0)
			{
				return entry.getKey();
			}
		}

		return null;
	}

	public static DungeonRoom getRandomStarterDungeonRoom(Random rand, String key)
	{
		if (dungeonStartingRoomMap.containsKey(key))
		{
			List<DungeonRoom> roomList = dungeonStartingRoomMap.get(key);
			return roomList.get(rand.nextInt(roomList.size()));
		}

		return null;
	}
}