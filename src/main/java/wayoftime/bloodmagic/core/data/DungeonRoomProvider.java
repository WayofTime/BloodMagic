package wayoftime.bloodmagic.core.data;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.gson.Serializers;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.ritual.AreaDescriptor.Rectangle;
import wayoftime.bloodmagic.structures.DungeonRoom;
import wayoftime.bloodmagic.structures.ModDungeons;
import wayoftime.bloodmagic.structures.ModRoomPools;

public class DungeonRoomProvider implements DataProvider
{
	DataGenerator generator;

	public DungeonRoomProvider(DataGenerator gen)
	{
		this.generator = gen;
	}

	public void loadRoomPools(HashCache cache)
	{
		Map<ResourceLocation, Integer> connectiveCorridors = new HashMap<>();
		connectiveCorridors.put(ModDungeons.T_CORRIDOR, 2);
		connectiveCorridors.put(ModDungeons.FOUR_WAY_CORRIDOR_LOOT, 1);
		connectiveCorridors.put(ModDungeons.FOUR_WAY_CORRIDOR, 2);
		connectiveCorridors.put(ModDungeons.STRAIGHT_CORRIDOR, 4);
		connectiveCorridors.put(ModDungeons.OVERLAPPED_CORRIDOR, 3);

		Map<ResourceLocation, Integer> miniDungeonRooms = new HashMap<>();
		miniDungeonRooms.put(ModDungeons.MINI_ARMOURY, 1);
		miniDungeonRooms.put(ModDungeons.MINI_CRYPT, 1);
		miniDungeonRooms.put(ModDungeons.MINI_FARM, 1);
		miniDungeonRooms.put(ModDungeons.MINI_PORTAL, 1);
		miniDungeonRooms.put(ModDungeons.MINI_LIBRARY, 1);

		Map<ResourceLocation, Integer> standardDungeonRooms = new HashMap<>();
		standardDungeonRooms.put(ModDungeons.ORE_HOLD_1, 1);
//		standardDungeonRooms.put(ModDungeons.WATER_WAY, 1);

		Map<ResourceLocation, Integer> mineDungeonRooms = new HashMap<>();
		mineDungeonRooms.put(ModDungeons.MINE_PIT, 1);
		mineDungeonRooms.put(ModDungeons.MINE_CORNER_ZOMBIE_TRAP, 2);

		// Special RoomPools
		Map<ResourceLocation, Integer> mineEntrances = new HashMap<>();
		mineEntrances.put(ModDungeons.MINE_ENTRANCE, 1);

		// Entrances
		Map<ResourceLocation, Integer> miniDungeonEntrances = new HashMap<>();
		miniDungeonEntrances.put(ModDungeons.MINI_ENTRANCE, 1);

		Map<ResourceLocation, Integer> standardDungeonEntrances = new HashMap<>();
		standardDungeonEntrances.put(ModDungeons.STANDARD_ENTRANCE, 1);

		// Deadends
		Map<ResourceLocation, Integer> defaultDeadends = new HashMap<>();
		defaultDeadends.put(ModDungeons.DEFAULT_DEADEND, 1);

		// Registration
		addRoomPool(cache, connectiveCorridors, ModRoomPools.CONNECTIVE_CORRIDORS);
		addRoomPool(cache, miniDungeonRooms, ModRoomPools.MINI_DUNGEON);
		addRoomPool(cache, standardDungeonRooms, ModRoomPools.STANDARD_ROOMS);

		addRoomPool(cache, mineEntrances, ModRoomPools.MINE_ENTRANCES);
		addRoomPool(cache, mineDungeonRooms, ModRoomPools.MINE_ROOMS);

		addRoomPool(cache, miniDungeonEntrances, ModRoomPools.MINI_DUNGEON_ENTRANCES);
		addRoomPool(cache, standardDungeonEntrances, ModRoomPools.STANDARD_DUNGEON_ENTRANCES);

		addRoomPool(cache, defaultDeadends, ModRoomPools.DEFAULT_DEADEND);
	}

	public void loadDungeons(HashCache cache)
	{
		DungeonRoom miniArmoury = new DungeonRoom().addStructure("bloodmagic:mini_dungeon/armoury", new BlockPos(0, 0, 0)).addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(17, 8, 17)));
		miniArmoury.addDoor(new BlockPos(8, 0, 0), Direction.NORTH, "default", 1);
		miniArmoury.addNormalRoomPool(1, ModRoomPools.MINI_DUNGEON);
		DungeonRoom miniCrypt = new DungeonRoom().addStructure("bloodmagic:mini_dungeon/crypt", new BlockPos(0, 0, 0)).addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(17, 10, 17)));
		miniCrypt.addDoor(new BlockPos(8, 0, 0), Direction.NORTH, "default", 1);
		miniCrypt.addNormalRoomPool(1, ModRoomPools.MINI_DUNGEON);
		DungeonRoom miniFarm = new DungeonRoom().addStructure("bloodmagic:mini_dungeon/farm", new BlockPos(0, 0, 0)).addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(17, 9, 17)));
		miniFarm.addDoor(new BlockPos(8, 0, 0), Direction.NORTH, "default", 1);
		miniFarm.addNormalRoomPool(1, ModRoomPools.MINI_DUNGEON);
		DungeonRoom miniLibrary = new DungeonRoom().addStructure("bloodmagic:mini_dungeon/library", new BlockPos(0, 0, 0)).addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(17, 7, 17)));
		miniLibrary.addDoor(new BlockPos(8, 0, 0), Direction.NORTH, "default", 1);
		miniLibrary.addNormalRoomPool(1, ModRoomPools.MINI_DUNGEON);
		DungeonRoom miniPortalRoom = new DungeonRoom().addStructure("bloodmagic:mini_dungeon/portal_nether", new BlockPos(0, 0, 0)).addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(17, 7, 17)));
		miniPortalRoom.addDoor(new BlockPos(8, 5, 0), Direction.NORTH, "default", 1);
		miniPortalRoom.addNormalRoomPool(1, ModRoomPools.MINI_DUNGEON);

		DungeonRoom fourWayCorridor = new DungeonRoom().addStructure("bloodmagic:four_way_corridor", new BlockPos(0, 0, 0)).addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(11, 6, 11)));
		fourWayCorridor.addDoors(Direction.NORTH, "default", 1, new BlockPos(5, 0, 0));
		fourWayCorridor.addDoors(Direction.SOUTH, "default", 1, new BlockPos(5, 0, 10));
		fourWayCorridor.addDoors(Direction.WEST, "default", 1, new BlockPos(0, 0, 5));
		fourWayCorridor.addDoors(Direction.EAST, "default", 1, new BlockPos(10, 0, 5));

		DungeonRoom fourWayCorridorLoot = new DungeonRoom().addStructure("bloodmagic:four_way_corridor_loot", BlockPos.ZERO).addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(11, 1, 11)));
		fourWayCorridorLoot.addDoors(Direction.NORTH, "default", 1, new BlockPos(5, 5, 0));
		fourWayCorridorLoot.addDoors(Direction.SOUTH, "default", 1, new BlockPos(5, 5, 10));
		fourWayCorridorLoot.addDoors(Direction.WEST, "default", 1, new BlockPos(0, 5, 5));
		fourWayCorridorLoot.addDoors(Direction.EAST, "default", 1, new BlockPos(10, 5, 5));

		DungeonRoom overlapped_corridor = new DungeonRoom().addStructure("bloodmagic:overlapped_corridor", BlockPos.ZERO).addAreaDescriptor(new Rectangle(new BlockPos(3, 0, 0), new BlockPos(8, 5, 11))).addAreaDescriptor(new Rectangle(new BlockPos(0, 4, 3), new BlockPos(11, 9, 8)));
		overlapped_corridor.addDoors(Direction.NORTH, "default", 1, new BlockPos(5, 0, 0));
		overlapped_corridor.addDoors(Direction.SOUTH, "default", 1, new BlockPos(5, 0, 10));
		overlapped_corridor.addDoors(Direction.WEST, "default", 1, new BlockPos(0, 4, 5));
		overlapped_corridor.addDoors(Direction.EAST, "default", 1, new BlockPos(10, 4, 5));

		DungeonRoom straightCorridor = new DungeonRoom().addStructure("bloodmagic:straight_corridor", BlockPos.ZERO).addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(11, 5, 5)));
		straightCorridor.addDoor(new BlockPos(0, 0, 2), Direction.WEST, "default", 1);
		straightCorridor.addDoor(new BlockPos(10, 0, 2), Direction.EAST, "default", 1);

		DungeonRoom tCorridor = new DungeonRoom().addStructure("bloodmagic:t_corridor", BlockPos.ZERO).addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(11, 5, 8)));
		tCorridor.addDoor(new BlockPos(5, 0, 0), Direction.NORTH, "default", 1);
		tCorridor.addDoor(new BlockPos(0, 0, 5), Direction.WEST, "default", 1);
		tCorridor.addDoor(new BlockPos(10, 0, 5), Direction.EAST, "default", 1);

		DungeonRoom oreHold = new DungeonRoom().addStructure("bloodmagic:ore_hold_1", new BlockPos(0, 0, 0));
		oreHold.addDoors(Direction.NORTH, "default", 1, new BlockPos(5, 0, 0), new BlockPos(2, 5, 0), new BlockPos(12, 5, 0));
		oreHold.addDoors(Direction.SOUTH, "default", 1, new BlockPos(5, 0, 14), new BlockPos(2, 5, 14), new BlockPos(12, 5, 14));
		oreHold.addDoors(Direction.WEST, "default", 1, new BlockPos(0, 5, 7));
		oreHold.addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(15, 12, 15)));
		oreHold.addNormalRoomPool(1, ModRoomPools.STANDARD_ROOMS);
		oreHold.addSpecialRoomPool(1, ModRoomPools.MINE_ENTRANCES);

		DungeonRoom mineEntrance = new DungeonRoom().addStructure("bloodmagic:standard/mine_entrance", new BlockPos(0, 0, 0)).addStructure("bloodmagic:standard/mine_entrance2", new BlockPos(0, 0, 32));
		mineEntrance.addDoor(new BlockPos(0, 12, 38), Direction.WEST, "default", 1);
		mineEntrance.addDoor(new BlockPos(22, 1, 0), Direction.NORTH, "mine", 2);
		mineEntrance.addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(21, 18, 43)));
		mineEntrance.addNormalRoomPool(1, ModRoomPools.STANDARD_ROOMS);
		mineEntrance.addNormalRoomPool(2, ModRoomPools.MINE_ROOMS);

		DungeonRoom waterway = new DungeonRoom().addStructure("bloodmagic:standard/test_waterway", new BlockPos(0, 0, 0)).addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(12, 8, 11)));
		waterway.addDoor(new BlockPos(11, 3, 5), Direction.EAST, "default", 1);
		waterway.addNonstandardDoor(new BlockPos(6, 3, 0), Direction.NORTH, "waterway_r", 2, "waterway_l");
		waterway.addNonstandardDoor(new BlockPos(6, 3, 10), Direction.SOUTH, "waterway_l", 3, "waterway_r");
		waterway.addNormalRoomPool(1, ModRoomPools.STANDARD_ROOMS);
		waterway.addSpecialRoomPool(1, ModRoomPools.MINE_ENTRANCES);
		waterway.addNormalRoomPool(2, ModRoomPools.STANDARD_ROOMS);
		waterway.addSpecialRoomPool(2, ModRoomPools.MINE_ENTRANCES);
		waterway.addNormalRoomPool(3, ModRoomPools.STANDARD_ROOMS);
		waterway.addSpecialRoomPool(3, ModRoomPools.MINE_ENTRANCES);
		waterway.registerDoorFill(2, new AreaDescriptor.Rectangle(new BlockPos(-6, -4, 0), 8, 8, 1));
		waterway.registerDoorFill(3, new AreaDescriptor.Rectangle(new BlockPos(-1, -4, 0), 8, 8, 1));

		DungeonRoom minePit = new DungeonRoom().addStructure("bloodmagic:standard/mine_pit", new BlockPos(0, 0, 0)).addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(19, 16, 22)));
		minePit.addDoor(new BlockPos(9, 8, 0), Direction.NORTH, "mine", 1);
		minePit.addDoor(new BlockPos(9, 8, 21), Direction.SOUTH, "mine", 1);
		minePit.addNormalRoomPool(1, ModRoomPools.MINE_ROOMS);
		minePit.registerDoorFill(1, new AreaDescriptor.Rectangle(new BlockPos(-2, -2, 0), 5, 5, 1));
		minePit.oreDensity = 0.1f;

		DungeonRoom mineCornerZombieTrap = new DungeonRoom().addStructure("bloodmagic:standard/corner_zombie_trap", new BlockPos(0, 0, 0)).addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(17, 9, 17))).addAreaDescriptor(new Rectangle(new BlockPos(4, 9, 2), 7, 4, 7));
		mineCornerZombieTrap.addDoor(new BlockPos(13, 2, 0), Direction.NORTH, "mine", 1);
		mineCornerZombieTrap.addDoor(new BlockPos(0, 2, 13), Direction.WEST, "mine", 1);
		mineCornerZombieTrap.addNormalRoomPool(1, ModRoomPools.MINE_ROOMS);
		mineCornerZombieTrap.registerDoorFill(1, new AreaDescriptor.Rectangle(new BlockPos(-2, -2, 0), 5, 5, 1));
		mineCornerZombieTrap.oreDensity = 0.1f;

		DungeonRoom defaultDeadend = new DungeonRoom().addStructure("bloodmagic:standard/default_deadend", new BlockPos(0, 0, 0)).addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(7, 6, 7)));
		defaultDeadend.addDoor(new BlockPos(3, 0, 0), Direction.NORTH, "default", 1);

		addDungeonRoom(cache, miniArmoury, ModDungeons.MINI_ARMOURY);
		addDungeonRoom(cache, miniCrypt, ModDungeons.MINI_CRYPT);
		addDungeonRoom(cache, miniFarm, ModDungeons.MINI_FARM);
		addDungeonRoom(cache, miniLibrary, ModDungeons.MINI_LIBRARY);
		addDungeonRoom(cache, miniPortalRoom, ModDungeons.MINI_PORTAL);

		addDungeonRoom(cache, fourWayCorridor, ModDungeons.FOUR_WAY_CORRIDOR);
		addDungeonRoom(cache, fourWayCorridorLoot, ModDungeons.FOUR_WAY_CORRIDOR_LOOT);
		addDungeonRoom(cache, overlapped_corridor, ModDungeons.OVERLAPPED_CORRIDOR);
		addDungeonRoom(cache, straightCorridor, ModDungeons.STRAIGHT_CORRIDOR);
		addDungeonRoom(cache, straightCorridor, ModDungeons.T_CORRIDOR);

		addDungeonRoom(cache, oreHold, ModDungeons.ORE_HOLD_1);
		addDungeonRoom(cache, waterway, ModDungeons.WATER_WAY);
		addDungeonRoom(cache, mineEntrance, ModDungeons.MINE_ENTRANCE);

		addDungeonRoom(cache, minePit, ModDungeons.MINE_PIT);
		addDungeonRoom(cache, mineCornerZombieTrap, ModDungeons.MINE_CORNER_ZOMBIE_TRAP);

		addDungeonRoom(cache, defaultDeadend, ModDungeons.DEFAULT_DEADEND);
//		DungeonRoom dungeonRoom = Serializers.GSON.fromJson(Resources.toString(dungeonURL, Charsets.UTF_8), DungeonRoom.class);

		registerStarterRooms(cache);
	}

	public void registerStarterRooms(HashCache cache)
	{
		DungeonRoom miniDungeon = new DungeonRoom().addStructure("bloodmagic:t3_entrance", BlockPos.ZERO).addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(17, 8, 17)));
		miniDungeon.addDoors(Direction.NORTH, "default", 1, new BlockPos(8, 1, 0));
		miniDungeon.addDoors(Direction.SOUTH, "default", 1, new BlockPos(8, 1, 16));
		miniDungeon.addDoors(Direction.EAST, "default", 1, new BlockPos(16, 1, 8));
		miniDungeon.addDoors(Direction.WEST, "default", 1, new BlockPos(0, 1, 8));
		miniDungeon.addNormalRoomPool(1, ModRoomPools.MINI_DUNGEON);
		miniDungeon.spawnLocation = new BlockPos(8, 2, 4);
		miniDungeon.controllerOffset = new BlockPos(8, 6, 8);
		miniDungeon.portalOffset = new BlockPos(8, 4, 8);

		DungeonRoom starterDungeon = new DungeonRoom().addStructure("bloodmagic:standard/standard_entrance", BlockPos.ZERO).addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(19, 9, 19)));
		starterDungeon.addDoors(Direction.NORTH, "default", 1, new BlockPos(9, 1, 0));
		starterDungeon.addDoors(Direction.SOUTH, "default", 1, new BlockPos(9, 1, 18));
		starterDungeon.addDoors(Direction.EAST, "default", 1, new BlockPos(18, 1, 9));
		starterDungeon.addDoors(Direction.WEST, "default", 1, new BlockPos(0, 1, 9));
		starterDungeon.addNormalRoomPool(1, ModRoomPools.STANDARD_ROOMS);
		starterDungeon.spawnLocation = new BlockPos(9, 2, 4);
		starterDungeon.controllerOffset = new BlockPos(9, 6, 9);
		starterDungeon.portalOffset = new BlockPos(9, 4, 9);

		addDungeonRoom(cache, miniDungeon, ModDungeons.MINI_ENTRANCE);
		addDungeonRoom(cache, starterDungeon, ModDungeons.STANDARD_ENTRANCE);

	}

	public void addDungeonRoom(HashCache cache, DungeonRoom room, ResourceLocation schematicName)
	{
		String json = Serializers.GSON.toJson(room);

		Path mainOutput = generator.getOutputFolder();
		String pathSuffix = "assets/" + schematicName.getNamespace() + "/schematics/" + schematicName.getPath() + ".json";
		Path outputPath = mainOutput.resolve(pathSuffix);
		try
		{
			save(cache, json, outputPath);
		} catch (IOException e)
		{
			BloodMagic.LOGGER.error("Couldn't save schematic to {}", outputPath, e);
		}
	}

	public void addRoomPool(HashCache cache, Map<ResourceLocation, Integer> roomPool, ResourceLocation schematicName)
	{
		List<String> roomStringList = new ArrayList<>();
		for (Entry<ResourceLocation, Integer> roomEntry : roomPool.entrySet())
		{
			roomStringList.add(roomEntry.getValue() + ";" + roomEntry.getKey().toString());
		}

		String json = Serializers.GSON.toJson(roomStringList);

		Path mainOutput = generator.getOutputFolder();
		String pathSuffix = "assets/" + schematicName.getNamespace() + "/schematics/" + schematicName.getPath() + ".json";
		Path outputPath = mainOutput.resolve(pathSuffix);
		try
		{
			save(cache, json, outputPath);
		} catch (IOException e)
		{
			BloodMagic.LOGGER.error("Couldn't save schematic to {}", outputPath, e);
		}
	}

	static void save(HashCache p_123922_, String json, Path p_123924_)
			throws IOException
	{
		String s = json;
		String s1 = SHA1.hashUnencodedChars(s).toString();
		if (!Objects.equals(p_123922_.getHash(p_123924_), s1) || !Files.exists(p_123924_))
		{
			Files.createDirectories(p_123924_.getParent());
			BufferedWriter bufferedwriter = Files.newBufferedWriter(p_123924_);

			try
			{
				bufferedwriter.write(s);
			} catch (Throwable throwable1)
			{
				if (bufferedwriter != null)
				{
					try
					{
						bufferedwriter.close();
					} catch (Throwable throwable)
					{
						throwable1.addSuppressed(throwable);
					}
				}

				throw throwable1;
			}

			if (bufferedwriter != null)
			{
				bufferedwriter.close();
			}
		}

		p_123922_.putNew(p_123924_, s1);
	}

	@Override
	public void run(HashCache cache)
			throws IOException
	{
		loadRoomPools(cache);
		loadDungeons(cache);
	}

	@Override
	public String getName()
	{
		return "DungeonGenerator";
	}
}
