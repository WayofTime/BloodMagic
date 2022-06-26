package wayoftime.bloodmagic.structures;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.common.reflect.TypeToken;

import net.minecraft.resources.ResourceLocation;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.gson.Serializers;

public class DungeonRoomLoader
{
	public static List<String> lootPoolRoomSchematics = new ArrayList<>();

	public static void saveNewDungeons()
	{
//		Map<String, BlockPos> structureMap = new HashMap<>();
//
//		Map<String, Map<Direction, List<BlockPos>>> doorMap = new HashMap<>(); // Map of doors. The EnumFacing
//																				// indicates what way
//		// this door faces.
//		List<AreaDescriptor.Rectangle> descriptorList = new ArrayList<>();
//		descriptorList.add(new AreaDescriptor.Rectangle(new BlockPos(0, 0, 0), new BlockPos(17, 7, 18)));
//
//		structureMap.put("bloodmagic:mini_dungeon/library", new BlockPos(0, 0, 0));
//
//		Map<Direction, List<BlockPos>> defaultList = new HashMap<>();
//		List<BlockPos> northList = new ArrayList<>();
//		northList.add(new BlockPos(8, 1, 0));
//
//		defaultList.put(Direction.NORTH, northList);
//
//		doorMap.put("default", defaultList);
//
//		DungeonRoom testRoom = new DungeonRoom(structureMap, doorMap, descriptorList);
//		testRoom.controllerOffset = new BlockPos(8, 4, 8);
//		testRoom.spawnLocation = new BlockPos(8, 1, 6);
//
//		HashMap<Integer, List<BlockPos>> indexToDoorMap = new HashMap<>();
//		List<BlockPos> doorList = new ArrayList<>();
//		doorList.add(new BlockPos(8, 1, 0));
//
//		indexToDoorMap.put(1, doorList);
//
//		Map<Integer, List<String>> indexToRoomTypeMap = new HashMap<>();
//		List<String> roomTypes = new ArrayList<>();
//		roomTypes.add("bloodmagic:room_pools/tier1/mini_dungeon");
//		indexToRoomTypeMap.put(1, roomTypes);
//
//		testRoom.indexToDoorMap = indexToDoorMap;
//		testRoom.indexToRoomTypeMap = indexToRoomTypeMap;
//
//		DungeonRoomLoader.saveSingleDungeon(testRoom);
	}

	public static void saveDungeons()
	{
		for (DungeonRoom room : DungeonRoomRegistry.dungeonWeightMap.keySet())
		{
			saveSingleDungeon(room);
		}
	}

	public static void saveSingleDungeon(DungeonRoom room)
	{
		String json = Serializers.GSON.toJson(room);

		Writer writer;
		try
		{
			File file = new File("config/BloodMagic/schematics");
			file.mkdirs();

			writer = new FileWriter("config/BloodMagic/schematics/" + new Random().nextInt() + ".json");
			writer.write(json);
			writer.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void writeTestPool(List<Pair<String, Integer>> roomPool)
	{
		List<String> reducedRoomPool = new ArrayList<>();
		for (Pair<String, Integer> entry : roomPool)
		{
			reducedRoomPool.add(entry.getRight() + ";" + entry.getLeft());
		}

		String json = Serializers.GSON.toJson(roomPool);

		Writer writer;
		try
		{
			File file = new File("config/BloodMagic/schematics");
			file.mkdirs();

			writer = new FileWriter("config/BloodMagic/schematics/" + Math.abs(new Random().nextInt()) + ".json");
			writer.write(json);
			writer.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void loadRoomPools()
	{
		try
		{
//			System.out.println("LOADING DEMON DUNGEONS");

//			URL schematicURL = DungeonRoomLoader.class.getResource(resLocToResourcePath(new ResourceLocation("bloodmagic:room_pools")));
//			List<String> schematics = Serializers.GSON.fromJson(Resources.toString(schematicURL, Charsets.UTF_8), new TypeToken<List<String>>()
//			{
//			}.getType());
			for (ResourceLocation schematic : DungeonRoomRegistry.unloadedDungeonRoomPools)
			{
//				ResourceLocation schematic = new ResourceLocation(schematicKey);
				URL roomPoolURL = DungeonRoomLoader.class.getResource(resLocToResourcePath(schematic));
				List<String> roomPoolList = Serializers.GSON.fromJson(Resources.toString(roomPoolURL, Charsets.UTF_8), new TypeToken<List<String>>()
				{
				}.getType());
//				System.out.println("Number of entries: " + roomPoolList.size());

				List<Pair<ResourceLocation, Integer>> roomPool = new ArrayList<>();
				for (String roomEntryString : roomPoolList)
				{
					Pair<ResourceLocation, Integer> roomEntry = parseRoomEntryString(roomEntryString);
					if (roomEntry != null)
					{
						roomPool.add(roomEntry);
					}
				}

				DungeonRoomRegistry.registerDungeomRoomPool(schematic, roomPool);
			}

//			System.out.println("# room pools: " + schematics.size());

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static Pair<ResourceLocation, Integer> parseRoomEntryString(String str)
	{
		String[] splitString = str.split(";");
		if (splitString.length == 2)
		{
			try
			{
				Integer weight = Integer.parseInt(splitString[0]);
				ResourceLocation resLoc = new ResourceLocation(splitString[1]);
				return Pair.of(resLoc, weight);
			} catch (NumberFormatException ex)
			{
				ex.printStackTrace();
			}
		}

		return null;
	}

	public static void loadDungeons()
	{

		try
		{
//			System.out.println("LOADING DEMON DUNGEONS");

			for (ResourceLocation schematic : DungeonRoomRegistry.unloadedDungeonRooms)
			{
//				ResourceLocation schematic = new ResourceLocation(schematicKey);
				URL dungeonURL = DungeonRoomLoader.class.getResource(resLocToResourcePath(schematic));
				System.out.println("Loading schematic: " + schematic);
				DungeonRoom dungeonRoom = Serializers.GSON.fromJson(Resources.toString(dungeonURL, Charsets.UTF_8), DungeonRoom.class);
				System.out.println("Resulting dungeon: " + dungeonRoom);
				DungeonRoomRegistry.registerDungeonRoom(schematic, dungeonRoom, Math.max(1, dungeonRoom.dungeonWeight));
			}

			System.out.println("# schematics: " + DungeonRoomRegistry.unloadedDungeonRooms.size());

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		saveNewDungeons();
	}

	public static void test()
	{
		ResourceLocation id = new ResourceLocation(BloodMagic.MODID, "testGson");
		String s = id.getNamespace();
		String s1 = id.getPath();
		InputStream inputstream = null;

		try
		{
			inputstream = DungeonRoomLoader.class.getResourceAsStream("/assets/" + s + "/schematics/" + s1 + ".nbt");
//            this.readTemplateFromStream(s1, inputstream);
			return;
		} catch (Throwable var10)
		{

		} finally
		{
			IOUtils.closeQuietly(inputstream);
		}
	}

	public static String resLocToResourcePath(ResourceLocation resourceLocation)
	{
		return "/assets/" + resourceLocation.getNamespace() + "/schematics/" + resourceLocation.getPath() + ".json";
	}
}