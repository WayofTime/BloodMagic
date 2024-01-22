package wayoftime.bloodmagic.structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.util.RandomSource;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.resources.ResourceLocation;

public class DungeonRoomRegistry
{
	public static Map<DungeonRoom, Integer> dungeonWeightMap = new HashMap<>();
	public static Map<String, List<DungeonRoom>> dungeonStartingRoomMap = new HashMap<>();
	private static int totalWeight = 0;

	public static Map<ResourceLocation, DungeonRoom> dungeonRoomMap = new HashMap<>();
	public static Map<ResourceLocation, List<Pair<ResourceLocation, Integer>>> roomPoolTable = new HashMap<>();
	private static Map<ResourceLocation, Integer> totalWeightMap = new HashMap<>();

	public static List<ResourceLocation> unloadedDungeonRooms = new ArrayList<>();
	public static List<ResourceLocation> unloadedDungeonRoomPools = new ArrayList<>();

	public static void registerDungeonRoom(ResourceLocation res, DungeonRoom room, int weight)
	{
		room.key = res;
		dungeonWeightMap.put(room, weight);
		totalWeight += weight;
		dungeonRoomMap.put(res, room);
	}

	public static void registerUnloadedDungeonRoom(ResourceLocation res)
	{
		unloadedDungeonRooms.add(res);
	}

	public static void registerUnloadedDungeonRoomPool(ResourceLocation res)
	{
		unloadedDungeonRoomPools.add(res);
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

	public static DungeonRoom getRandomDungeonRoom(ResourceLocation roomPoolName, RandomSource rand)
	{
//		System.out.println(totalWeightMap);
		Integer maxWeight = totalWeightMap.get(roomPoolName);
//		System.out.println("Pool name: " + roomPoolName);

//		System.out.println("Max weight: " + maxWeight);

		int wantedWeight = 0;
		if (maxWeight != null)
		{
			wantedWeight = rand.nextInt(maxWeight);
			if (DungeonSynthesizer.displayDetailedInformation)
				System.out.println("Wanted weight: " + wantedWeight);
		}
		List<Pair<ResourceLocation, Integer>> roomPool = roomPoolTable.get(roomPoolName);
		if (roomPool == null)
		{
			if (DungeonSynthesizer.displayDetailedInformation)
			{
				System.out.println("There's nothing here... No room pool!");
				System.out.println("Number of registered room pools: " + roomPoolTable.size());
			}
			return null;
		}

		if (DungeonSynthesizer.displayDetailedInformation)
			System.out.println("Pool size: " + roomPool.size());

		for (Pair<ResourceLocation, Integer> entry : roomPool)
		{
			wantedWeight -= entry.getValue();

			if (DungeonSynthesizer.displayDetailedInformation)
				System.out.println("Room name: " + entry.getLeft());

			if (wantedWeight < 0)
			{
				ResourceLocation dungeonName = entry.getKey();
				if (DungeonSynthesizer.displayDetailedInformation)
				{
					System.out.println("Dungeon name: " + dungeonName);
					System.out.println("All dungeons: " + dungeonRoomMap);
					System.out.println("Size of dungeons: " + dungeonRoomMap.size());
				}
				return dungeonRoomMap.get(dungeonName);
			}

//			System.out.println("Weight: " + entry.getValue());
		}

		return null;
	}

	public static DungeonRoom getDungeonRoom(ResourceLocation dungeonName)
	{
		return dungeonRoomMap.get(dungeonName);
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

	public static DungeonRoom getRandomDungeonRoom(RandomSource rand)
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