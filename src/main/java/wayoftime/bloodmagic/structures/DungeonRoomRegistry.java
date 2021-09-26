package wayoftime.bloodmagic.structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.util.ResourceLocation;

public class DungeonRoomRegistry
{
	public static Map<DungeonRoom, Integer> dungeonWeightMap = new HashMap<>();
	public static Map<String, List<DungeonRoom>> dungeonStartingRoomMap = new HashMap<>();
	private static int totalWeight = 0;

	public static Map<ResourceLocation, DungeonRoom> dungeonRoomMap = new HashMap<>();
	public static Map<ResourceLocation, List<Pair<ResourceLocation, Integer>>> roomPoolTable = new HashMap<>();
	private static Map<ResourceLocation, Integer> totalWeightMap = new HashMap<>();

	public static void registerDungeonRoom(ResourceLocation res, DungeonRoom room, int weight)
	{
		dungeonWeightMap.put(room, weight);
		totalWeight += weight;
		dungeonRoomMap.put(res, room);
	}

	public static void registerDungeomRoomPool(ResourceLocation poolRes, List<Pair<ResourceLocation, Integer>> pool)
	{
		roomPoolTable.put(poolRes, pool);
		int totalWeightOfPool = 0;
		for (Pair<ResourceLocation, Integer> room : pool)
		{
			totalWeightOfPool += room.getValue();
		}
		totalWeightMap.put(poolRes, totalWeightOfPool);

//		System.out.println("Registering dungeon pool: " + poolRes);
	}

	public static DungeonRoom getRandomDungeonRoom(ResourceLocation roomPoolName, Random rand)
	{
//		System.out.println(totalWeightMap);
		Integer maxWeight = totalWeightMap.get(roomPoolName);
//		System.out.println("Pool name: " + roomPoolName);
		System.out.println("Max weight: " + maxWeight);

		int wantedWeight = 0;
		if (maxWeight != null)
		{
			wantedWeight = rand.nextInt(maxWeight);
		}
		List<Pair<ResourceLocation, Integer>> roomPool = roomPoolTable.get(roomPoolName);
		if (roomPool == null)
		{
//			System.out.println("There's nothing here...");
			return null;
		}
		for (Pair<ResourceLocation, Integer> entry : roomPool)
		{
			if (wantedWeight <= 0)
			{
				ResourceLocation dungeonName = entry.getKey();
//				System.out.println("Dungeon name: " + dungeonName);
//				System.out.println("All dungeons: " + dungeonRoomMap);
				return dungeonRoomMap.get(dungeonName);
			}

			wantedWeight -= entry.getValue();
		}

		return null;
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