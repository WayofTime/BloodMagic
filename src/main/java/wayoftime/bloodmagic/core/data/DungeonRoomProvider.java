package wayoftime.bloodmagic.core.data;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonElement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import wayoftime.bloodmagic.gson.Serializers;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.ritual.AreaDescriptor.Rectangle;
import wayoftime.bloodmagic.structures.DungeonRoom;
import wayoftime.bloodmagic.structures.ModDungeons;
import wayoftime.bloodmagic.structures.ModRoomPools;

public class DungeonRoomProvider implements DataProvider
{
	PackOutput packOutput;

	public DungeonRoomProvider(PackOutput output)
	{
		this.packOutput = output;
	}

	public ArrayList<CompletableFuture<?>> loadRoomPools(CachedOutput cache)
	{
		Map<ResourceLocation, Integer> connectiveCorridors = new TreeMap<>();
		connectiveCorridors.put(ModDungeons.T_CORRIDOR, 2);
//		connectiveCorridors.put(ModDungeons.FOUR_WAY_CORRIDOR_LOOT, 1);
		connectiveCorridors.put(ModDungeons.FOUR_WAY_CORRIDOR, 2);
		connectiveCorridors.put(ModDungeons.STRAIGHT_CORRIDOR, 4);
		connectiveCorridors.put(ModDungeons.OVERLAPPED_CORRIDOR, 3);

		Map<ResourceLocation, Integer> miniDungeonRooms = new TreeMap<>();
		miniDungeonRooms.put(ModDungeons.MINI_ARMOURY, 1);
		miniDungeonRooms.put(ModDungeons.MINI_CRYPT, 1);
		miniDungeonRooms.put(ModDungeons.MINI_FARM, 1);
		miniDungeonRooms.put(ModDungeons.MINI_PORTAL, 1);
		miniDungeonRooms.put(ModDungeons.MINI_LIBRARY, 1);

		Map<ResourceLocation, Integer> standardDungeonRooms = new TreeMap<>();
		standardDungeonRooms.put(ModDungeons.ORE_HOLD_1, 2);
		standardDungeonRooms.put(ModDungeons.CHALLENGE_TOWER, 2);
		standardDungeonRooms.put(ModDungeons.BIG_LIBRARY, 2);
		standardDungeonRooms.put(ModDungeons.SMALL_CRANE, 4);
		standardDungeonRooms.put(ModDungeons.SMALL_LIBRARY, 3);
		standardDungeonRooms.put(ModDungeons.TALL_SPIRAL, 1);
		standardDungeonRooms.put(ModDungeons.SMALL_SMITHY, 3);
		standardDungeonRooms.put(ModDungeons.SMALL_ARENA, 4);
		standardDungeonRooms.put(ModDungeons.ANTECHAMBER, 2);
		standardDungeonRooms.put(ModDungeons.DESTROYED_END_PORTAL, 1);
		standardDungeonRooms.put(ModDungeons.AUG_CORRIDOR_LOOT, 3);

//		standardDungeonRooms.put(ModDungeons.WATER_WAY, 1);

		Map<ResourceLocation, Integer> mineDungeonRooms = new TreeMap<>();
		mineDungeonRooms.put(ModDungeons.MINE_PIT, 3);
		mineDungeonRooms.put(ModDungeons.MINE_CORNER_ZOMBIE_TRAP, 2);
		mineDungeonRooms.put(ModDungeons.MINE_SPLIT_ROAD, 2);
		mineDungeonRooms.put(ModDungeons.MINE_STATION, 1);
		mineDungeonRooms.put(ModDungeons.MINE_DOWNWARD_TUNNEL, 2);
		mineDungeonRooms.put(ModDungeons.MINE_JUNCTION_STATION, 3);
		mineDungeonRooms.put(ModDungeons.MINE_BUILT_SHAFT, 2);
		mineDungeonRooms.put(ModDungeons.MINE_NATURE_CROSSROAD, 3);
		mineDungeonRooms.put(ModDungeons.MINE_WOLF_DEN, 2);
		mineDungeonRooms.put(ModDungeons.MINE_ORE_CAVERN, 2);

		// Corridor
		Map<ResourceLocation, Integer> mineCorridors = new TreeMap<>();
		mineCorridors.put(ModDungeons.MINE_STRAIGHT_CORRIDOR, 5);
		mineCorridors.put(ModDungeons.MINE_BENT_CORRIDOR, 4);
		mineCorridors.put(ModDungeons.MINE_FOURWAY_CORRIDOR, 3);

		// Special RoomPools
		Map<ResourceLocation, Integer> mineEntrances = new TreeMap<>();
		mineEntrances.put(ModDungeons.MINE_ENTRANCE, 1);

		Map<ResourceLocation, Integer> mineKey = new TreeMap<>();
		mineKey.put(ModDungeons.MINE_KEY, 1);

		// Entrances
		Map<ResourceLocation, Integer> miniDungeonEntrances = new TreeMap<>();
		miniDungeonEntrances.put(ModDungeons.MINI_ENTRANCE, 1);

		Map<ResourceLocation, Integer> standardDungeonEntrances = new TreeMap<>();
		standardDungeonEntrances.put(ModDungeons.STANDARD_ENTRANCE, 1);

		// Deadends
		Map<ResourceLocation, Integer> defaultDeadends = new TreeMap<>();
		defaultDeadends.put(ModDungeons.DEFAULT_DEADEND, 1);

		Map<ResourceLocation, Integer> mineDeadends = new TreeMap<>();
		mineDeadends.put(ModDungeons.MINES_DEADEND, 1);

		// Registration
		return new ArrayList<>( List.of(
		addRoomPool(cache, connectiveCorridors, ModRoomPools.CONNECTIVE_CORRIDORS),
		addRoomPool(cache, miniDungeonRooms, ModRoomPools.MINI_DUNGEON),
		addRoomPool(cache, standardDungeonRooms, ModRoomPools.STANDARD_ROOMS),

		addRoomPool(cache, mineEntrances, ModRoomPools.MINE_ENTRANCES),
		addRoomPool(cache, mineKey, ModRoomPools.MINE_KEY),
		addRoomPool(cache, mineDungeonRooms, ModRoomPools.MINE_ROOMS),
		addRoomPool(cache, mineCorridors, ModRoomPools.MINE_CORRIDORS),

		addRoomPool(cache, miniDungeonEntrances, ModRoomPools.MINI_DUNGEON_ENTRANCES),
		addRoomPool(cache, standardDungeonEntrances, ModRoomPools.STANDARD_DUNGEON_ENTRANCES),

		addRoomPool(cache, defaultDeadends, ModRoomPools.DEFAULT_DEADEND),
		addRoomPool(cache, mineDeadends, ModRoomPools.MINE_DEADEND)
		));
	}

	public ArrayList<CompletableFuture<?>> loadDungeons(CachedOutput cache)
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

		DungeonRoom oreHold = new DungeonRoom().addStructure("bloodmagic:standard/ore_hold_1", new BlockPos(0, 0, 0));
		oreHold.addDoors(Direction.NORTH, "default", 1, new BlockPos(12, 5, 0));
		oreHold.addDoors(Direction.SOUTH, "default", 1, new BlockPos(5, 0, 14), new BlockPos(12, 5, 14));
		oreHold.addDoors(Direction.WEST, "default", 1, new BlockPos(0, 5, 7));
		oreHold.addDoors(Direction.EAST, "default", 1, new BlockPos(14, 0, 7));
		oreHold.addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(15, 12, 15)));
		oreHold.addNormalRoomPool(1, ModRoomPools.STANDARD_ROOMS);
//		oreHold.addSpecialRoomPool(1, ModRoomPools.MINE_ENTRANCES);
		addDefaultSpecialRoomPools(oreHold, 1);
		oreHold.oreDensity = 0.1f;

		DungeonRoom challengeTower = new DungeonRoom().addStructure("bloodmagic:standard/challenge_tower_1", new BlockPos(0, 0, 0)).addStructure("bloodmagic:standard/challenge_tower_2", new BlockPos(0, 32, 0));
		challengeTower.addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(25, 55, 25)));
		challengeTower.addDoor(new BlockPos(24, 0, 12), Direction.EAST, "default", 1);
		challengeTower.addNonstandardDoor(new BlockPos(12, 0, 0), Direction.NORTH, "none", 2, "default");
		challengeTower.addNonstandardDoor(new BlockPos(12, 5, 24), Direction.SOUTH, "none", 2, "default");
		challengeTower.addNonstandardDoor(new BlockPos(0, 17, 12), Direction.WEST, "none", 2, "default");
		challengeTower.addNonstandardDoor(new BlockPos(24, 25, 12), Direction.EAST, "none", 2, "default");
		challengeTower.addNonstandardDoor(new BlockPos(12, 29, 0), Direction.NORTH, "none", 2, "default");
		challengeTower.addNormalRoomPool(1, ModRoomPools.STANDARD_ROOMS);
		challengeTower.addNormalRoomPool(2, ModRoomPools.STANDARD_ROOMS);
		addDefaultSpecialRoomPools(challengeTower, 2);

		DungeonRoom bigLibrary = new DungeonRoom().addStructure("bloodmagic:standard/big_library_1", new BlockPos(0, 0, 0)).addStructure("bloodmagic:standard/big_library_2", new BlockPos(32, 0, 0));
		bigLibrary.addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(33, 15, 25)));
		bigLibrary.addDoors(Direction.NORTH, "default", 1, new BlockPos(16, 0, 0));
		bigLibrary.addDoors(Direction.WEST, "default", 1, new BlockPos(0, 8, 8));
		bigLibrary.addDoors(Direction.SOUTH, "default", 1, new BlockPos(8, 0, 24), new BlockPos(24, 0, 24));
		bigLibrary.addNormalRoomPool(1, ModRoomPools.STANDARD_ROOMS);
		addDefaultSpecialRoomPools(bigLibrary, 1);

		DungeonRoom smallCrane = new DungeonRoom().addStructure("bloodmagic:standard/small_crane", new BlockPos(0, 0, 0));
		smallCrane.addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(17, 11, 17)));
		smallCrane.addDoors(Direction.NORTH, "default", 1, new BlockPos(8, 0, 0));
		smallCrane.addDoors(Direction.EAST, "default", 1, new BlockPos(16, 0, 8));
		smallCrane.addDoors(Direction.WEST, "default", 1, new BlockPos(0, 0, 8));
		smallCrane.addNormalRoomPool(1, ModRoomPools.STANDARD_ROOMS);
		addDefaultSpecialRoomPools(smallCrane, 1);

		DungeonRoom smallLibrary = new DungeonRoom().addStructure("bloodmagic:standard/small_library", new BlockPos(0, 0, 0));
		smallLibrary.addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(17, 6, 18)));
		smallLibrary.addDoors(Direction.NORTH, "default", 1, new BlockPos(8, 0, 0));
		smallLibrary.addDoors(Direction.EAST, "default", 1, new BlockPos(16, 0, 8));
		smallLibrary.addDoors(Direction.WEST, "default", 1, new BlockPos(0, 0, 8));
		smallLibrary.addNormalRoomPool(1, ModRoomPools.STANDARD_ROOMS);
		addDefaultSpecialRoomPools(smallLibrary, 1);

		DungeonRoom smallSmithy = new DungeonRoom().addStructure("bloodmagic:standard/small_smithy", new BlockPos(0, 0, 0));
		smallSmithy.addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(20, 11, 20)));
		smallSmithy.addDoors(Direction.NORTH, "default", 1, new BlockPos(9, 0, 0));
		smallSmithy.addDoors(Direction.EAST, "default", 1, new BlockPos(19, 0, 10));
		smallSmithy.addNormalRoomPool(1, ModRoomPools.STANDARD_ROOMS);
		addDefaultSpecialRoomPools(smallSmithy, 1);

		DungeonRoom tallSpiral = new DungeonRoom().addStructure("bloodmagic:standard/tall_spiral_staircase", new BlockPos(0, 0, 0));
		tallSpiral.addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(17, 30, 17)));
		tallSpiral.addDoors(Direction.NORTH, "default", 1, new BlockPos(8, 0, 0), new BlockPos(8, 12, 0), new BlockPos(8, 24, 0));
		tallSpiral.addDoors(Direction.EAST, "default", 1, new BlockPos(16, 0, 8), new BlockPos(16, 12, 8), new BlockPos(16, 24, 8));
		tallSpiral.addDoors(Direction.WEST, "default", 1, new BlockPos(0, 0, 8), new BlockPos(0, 12, 8), new BlockPos(0, 24, 8));
		tallSpiral.addDoors(Direction.SOUTH, "default", 1, new BlockPos(8, 0, 16), new BlockPos(8, 12, 16), new BlockPos(8, 24, 16));
		tallSpiral.addNormalRoomPool(1, ModRoomPools.STANDARD_ROOMS);
		addDefaultSpecialRoomPools(tallSpiral, 1);

		DungeonRoom smallArena = new DungeonRoom().addStructure("bloodmagic:standard/small_arena", new BlockPos(0, 0, 0));
		smallArena.addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(17, 13, 17)));
		smallArena.addDoors(Direction.NORTH, "default", 1, new BlockPos(8, 2, 0));
		smallArena.addDoors(Direction.EAST, "default", 1, new BlockPos(16, 2, 8));
		smallArena.addDoors(Direction.WEST, "default", 1, new BlockPos(0, 2, 8));
		smallArena.addDoors(Direction.SOUTH, "default", 1, new BlockPos(8, 2, 16));
		smallArena.addNormalRoomPool(1, ModRoomPools.STANDARD_ROOMS);
		addDefaultSpecialRoomPools(smallArena, 1);

		DungeonRoom antechamber = new DungeonRoom().addStructure("bloodmagic:standard/antechamber", new BlockPos(0, 0, 0));
		antechamber.addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(31, 26, 31)));
		antechamber.addDoors(Direction.NORTH, "default", 1, new BlockPos(15, 13, 0));
		antechamber.addDoors(Direction.EAST, "default", 1, new BlockPos(30, 13, 15));
		antechamber.addDoors(Direction.WEST, "default", 1, new BlockPos(0, 13, 15));
		antechamber.addDoors(Direction.SOUTH, "default", 1, new BlockPos(15, 13, 30));
		antechamber.addNormalRoomPool(1, ModRoomPools.STANDARD_ROOMS);
		addDefaultSpecialRoomPools(antechamber, 1);

		DungeonRoom destroyedEndPortal = new DungeonRoom().addStructure("bloodmagic:standard/destroyed_portal", new BlockPos(0, 0, 0));
		destroyedEndPortal.addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(16, 9, 13)));
		destroyedEndPortal.addDoor(new BlockPos(15, 0, 6), Direction.EAST, "default", 1);
		destroyedEndPortal.addNormalRoomPool(1, ModRoomPools.STANDARD_ROOMS);
		addDefaultSpecialRoomPools(destroyedEndPortal, 1);

		DungeonRoom augCorridorLoot = new DungeonRoom().addStructure("bloodmagic:standard/four_way_corridor_loot", BlockPos.ZERO).addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(21, 11, 11)));
		augCorridorLoot.addDoors(Direction.NORTH, "default", 1, new BlockPos(10, 5, 0));
		augCorridorLoot.addDoors(Direction.SOUTH, "default", 1, new BlockPos(10, 5, 10));
		augCorridorLoot.addDoors(Direction.WEST, "default", 1, new BlockPos(0, 5, 5));
		augCorridorLoot.addDoors(Direction.EAST, "default", 1, new BlockPos(20, 5, 5));
		augCorridorLoot.addNormalRoomPool(1, ModRoomPools.STANDARD_ROOMS);
		addDefaultSpecialRoomPools(augCorridorLoot, 1);

		DungeonRoom mineEntrance = new DungeonRoom().addStructure("bloodmagic:standard/mine_entrance", new BlockPos(0, 0, 0)).addStructure("bloodmagic:standard/mine_entrance2", new BlockPos(0, 0, 32));
		mineEntrance.addDoor(new BlockPos(0, 12, 38), Direction.WEST, "default", 1);
		mineEntrance.addDoor(new BlockPos(22, 1, 0), Direction.NORTH, "mine", 2);
		mineEntrance.addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(21, 18, 43)));
		mineEntrance.addNormalRoomPool(1, ModRoomPools.STANDARD_ROOMS);
		mineEntrance.addNormalRoomPool(2, ModRoomPools.MINE_CORRIDORS);

		DungeonRoom mineKey = new DungeonRoom().addStructure("bloodmagic:mines/mine_key", new BlockPos(0, 0, 0)).addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(11, 8, 11)));
		mineKey.addDoor(new BlockPos(5, 1, 0), Direction.NORTH, "default", 1);
		mineKey.oreDensity = 0.2f;

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
		addMinesRoomPools(minePit, 1);
		minePit.registerDoorFill(1, new AreaDescriptor.Rectangle(new BlockPos(-2, -2, 0), 5, 5, 1));
		minePit.oreDensity = 0.1f;
		minePit.addDeadendRoomPool(1, ModRoomPools.MINE_DEADEND);

		DungeonRoom mineSplitRoad = new DungeonRoom().addStructure("bloodmagic:standard/mine_split_road", new BlockPos(0, 0, 0)).addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(21, 28, 21)));
		mineSplitRoad.addDoor(new BlockPos(17, 10, 0), Direction.NORTH, "mine", 1);
		mineSplitRoad.addDoor(new BlockPos(0, 10, 17), Direction.WEST, "mine", 1);
		mineSplitRoad.addDoor(new BlockPos(3, 19, 20), Direction.SOUTH, "mine", 1);
		mineSplitRoad.addDoor(new BlockPos(20, 19, 3), Direction.EAST, "mine", 1);
		addMinesRoomPools(mineSplitRoad, 1);
		mineSplitRoad.registerDoorFill(1, new AreaDescriptor.Rectangle(new BlockPos(-2, -2, 0), 5, 5, 1));
		mineSplitRoad.oreDensity = 0.1f;
		mineSplitRoad.addDeadendRoomPool(1, ModRoomPools.MINE_DEADEND);

		DungeonRoom mineCornerZombieTrap = new DungeonRoom().addStructure("bloodmagic:standard/corner_zombie_trap", new BlockPos(0, 0, 0)).addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(17, 9, 17))).addAreaDescriptor(new Rectangle(new BlockPos(4, 9, 2), 7, 4, 7));
		mineCornerZombieTrap.addDoor(new BlockPos(13, 2, 0), Direction.NORTH, "mine", 1);
		mineCornerZombieTrap.addDoor(new BlockPos(0, 2, 13), Direction.WEST, "mine", 1);
		addMinesRoomPools(mineCornerZombieTrap, 1);
		mineCornerZombieTrap.registerDoorFill(1, new AreaDescriptor.Rectangle(new BlockPos(-2, -2, 0), 5, 5, 1));
		mineCornerZombieTrap.oreDensity = 0.1f;
		mineCornerZombieTrap.addDeadendRoomPool(1, ModRoomPools.MINE_DEADEND);

		DungeonRoom mineStation = new DungeonRoom().addStructure("bloodmagic:standard/mine_station_1", new BlockPos(0, 0, 0));
		mineStation.addStructure("bloodmagic:standard/mine_station_2", new BlockPos(23, 4, 32));
		mineStation.addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(32, 13, 32)));
		mineStation.addAreaDescriptor(new Rectangle(new BlockPos(23, 4, 32), 9, 8, 9));
		mineStation.addDoors(Direction.WEST, "mine", 1, new BlockPos(0, 5, 4), new BlockPos(0, 5, 16));
		mineStation.addDoor(new BlockPos(3, 5, 31), Direction.SOUTH, "mine", 1);
		mineStation.addDoor(new BlockPos(31, 5, 36), Direction.EAST, "mine", 1);
		addMinesRoomPools(mineStation, 1);
		mineStation.registerDoorFill(1, new AreaDescriptor.Rectangle(new BlockPos(-2, -2, 0), 5, 5, 1));
		mineStation.oreDensity = 0.1f;
		mineStation.addDeadendRoomPool(1, ModRoomPools.MINE_DEADEND);

		DungeonRoom mineDownwardTunnel = new DungeonRoom().addStructure("bloodmagic:standard/mine_downward_tunnel_1", new BlockPos(0, 0, 0));
		mineDownwardTunnel.addStructure("bloodmagic:standard/mine_downward_tunnel_2", new BlockPos(0, 9, 2));
		mineDownwardTunnel.addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(18, 9, 25)));
		mineDownwardTunnel.addAreaDescriptor(new Rectangle(new BlockPos(0, 9, 2), 18, 10, 17));
		mineDownwardTunnel.addDoors(Direction.WEST, "mine", 1, new BlockPos(0, 1, 4), new BlockPos(0, 13, 5));
		mineDownwardTunnel.addDoor(new BlockPos(8, 1, 0), Direction.NORTH, "mine", 1);
		mineDownwardTunnel.addDoor(new BlockPos(8, 1, 24), Direction.SOUTH, "mine", 1);
		mineDownwardTunnel.addDoor(new BlockPos(17, 1, 16), Direction.EAST, "mine", 1);
		addMinesRoomPools(mineDownwardTunnel, 1);
		mineDownwardTunnel.registerDoorFill(1, new AreaDescriptor.Rectangle(new BlockPos(-2, -2, 0), 5, 5, 1));
		mineDownwardTunnel.oreDensity = 0.1f;
		mineDownwardTunnel.addDeadendRoomPool(1, ModRoomPools.MINE_DEADEND);

		DungeonRoom mineJunctionStation = new DungeonRoom().addStructure("bloodmagic:standard/mine_junction_station", new BlockPos(0, 0, 0));
		mineJunctionStation.addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(21, 7, 18)));
		mineJunctionStation.addDoor(new BlockPos(13, 1, 0), Direction.NORTH, "mine", 1);
		mineJunctionStation.addDoor(new BlockPos(20, 1, 7), Direction.EAST, "mine", 1);
		mineJunctionStation.addDoor(new BlockPos(13, 1, 17), Direction.SOUTH, "mine", 1);
		addMinesRoomPools(mineJunctionStation, 1);
		mineJunctionStation.registerDoorFill(1, new AreaDescriptor.Rectangle(new BlockPos(-2, -2, 0), 5, 5, 1));
		mineJunctionStation.oreDensity = 0.1f;
		mineJunctionStation.addDeadendRoomPool(1, ModRoomPools.MINE_DEADEND);

		DungeonRoom mineBuiltShaft = new DungeonRoom().addStructure("bloodmagic:standard/mine_built_shaft_1", new BlockPos(0, 0, 0));
		mineBuiltShaft.addStructure("bloodmagic:standard/mine_built_shaft_2", new BlockPos(0, 32, 0));
		mineBuiltShaft.addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(31, 34, 21)));
		mineBuiltShaft.addDoor(new BlockPos(22, 25, 0), Direction.NORTH, "mine", 1);
		mineBuiltShaft.addDoor(new BlockPos(22, 25, 20), Direction.SOUTH, "mine", 1);
		mineBuiltShaft.addDoor(new BlockPos(30, 1, 4), Direction.EAST, "mine", 1);
		addMinesRoomPools(mineBuiltShaft, 1);
		mineBuiltShaft.registerDoorFill(1, new AreaDescriptor.Rectangle(new BlockPos(-2, -2, 0), 5, 5, 1));
		mineBuiltShaft.oreDensity = 0.06f;
		mineBuiltShaft.addDeadendRoomPool(1, ModRoomPools.MINE_DEADEND);

		DungeonRoom mineNatureCrossroad = new DungeonRoom().addStructure("bloodmagic:standard/mine_nature_crossroad", new BlockPos(0, 0, 0));
		mineNatureCrossroad.addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(15, 9, 19)));
		mineNatureCrossroad.addDoor(new BlockPos(6, 1, 0), Direction.NORTH, "mine", 1);
		mineNatureCrossroad.addDoor(new BlockPos(14, 1, 7), Direction.EAST, "mine", 1);
		mineNatureCrossroad.addDoor(new BlockPos(6, 1, 18), Direction.SOUTH, "mine", 1);
		mineNatureCrossroad.addDoor(new BlockPos(0, 1, 11), Direction.WEST, "mine", 1);
		addMinesRoomPools(mineNatureCrossroad, 1);
		mineNatureCrossroad.registerDoorFill(1, new AreaDescriptor.Rectangle(new BlockPos(-2, -2, 0), 5, 5, 1));
		mineNatureCrossroad.oreDensity = 0.15f;
		mineNatureCrossroad.addDeadendRoomPool(1, ModRoomPools.MINE_DEADEND);

		DungeonRoom mineWolfDen = new DungeonRoom().addStructure("bloodmagic:standard/mine_wolf_den", new BlockPos(0, 0, 0));
		mineWolfDen.addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(31, 24, 31)));
		mineWolfDen.addDoor(new BlockPos(12, 10, 0), Direction.NORTH, "mine", 1);
		mineWolfDen.addDoor(new BlockPos(30, 10, 16), Direction.EAST, "mine", 1);
		mineWolfDen.addDoor(new BlockPos(18, 10, 30), Direction.SOUTH, "mine", 1);
		mineWolfDen.addDoor(new BlockPos(0, 16, 19), Direction.WEST, "mine", 1);
		addMinesRoomPools(mineWolfDen, 1);
		mineWolfDen.registerDoorFill(1, new AreaDescriptor.Rectangle(new BlockPos(-2, -2, 0), 5, 5, 1));
		mineWolfDen.oreDensity = 0.1f;
		mineWolfDen.addDeadendRoomPool(1, ModRoomPools.MINE_DEADEND);

		DungeonRoom mineOreCavern = new DungeonRoom().addStructure("bloodmagic:standard/mine_ore_cavern", new BlockPos(0, 0, 0));
		mineOreCavern.addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(23, 19, 25)));
		mineOreCavern.addDoor(new BlockPos(15, 8, 0), Direction.NORTH, "mine", 1);
		mineOreCavern.addDoor(new BlockPos(22, 2, 13), Direction.EAST, "mine", 1);
		mineOreCavern.addDoor(new BlockPos(15, 8, 24), Direction.SOUTH, "mine", 1);
		addMinesRoomPools(mineOreCavern, 1);
		mineOreCavern.registerDoorFill(1, new AreaDescriptor.Rectangle(new BlockPos(-2, -2, 0), 5, 5, 1));
		mineOreCavern.oreDensity = 0.1f;
		mineOreCavern.addDeadendRoomPool(1, ModRoomPools.MINE_DEADEND);

		DungeonRoom defaultDeadend = new DungeonRoom().addStructure("bloodmagic:standard/default_deadend", new BlockPos(0, 0, 0)).addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(7, 6, 7)));
		defaultDeadend.addDoor(new BlockPos(3, 0, 0), Direction.NORTH, "default", 1);

		DungeonRoom mineStraightCorridor = new DungeonRoom().addStructure("bloodmagic:standard/mine_straight_corridor", new BlockPos(0, 0, 0));
		mineStraightCorridor.addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(7, 7, 17)));
		mineStraightCorridor.addDoor(new BlockPos(3, 1, 0), Direction.NORTH, "mine", 1);
		mineStraightCorridor.addDoor(new BlockPos(3, 1, 16), Direction.SOUTH, "mine", 1);
		mineStraightCorridor.addNormalRoomPool(1, ModRoomPools.MINE_ROOMS);
		mineStraightCorridor.registerDoorFill(1, new AreaDescriptor.Rectangle(new BlockPos(-2, -2, 0), 5, 5, 1));
		mineStraightCorridor.oreDensity = 0.05f;
		mineStraightCorridor.addDeadendRoomPool(1, ModRoomPools.MINE_DEADEND);

		DungeonRoom mineBentCorridor = new DungeonRoom().addStructure("bloodmagic:standard/mine_bent_corridor", new BlockPos(0, 0, 0));
		mineBentCorridor.addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(16, 14, 19)));
		mineBentCorridor.addDoor(new BlockPos(12, 1, 0), Direction.NORTH, "mine", 1);
		mineBentCorridor.addDoor(new BlockPos(0, 8, 3), Direction.WEST, "mine", 1);
		mineBentCorridor.addDoor(new BlockPos(9, 4, 18), Direction.SOUTH, "mine", 1);
		mineBentCorridor.addNormalRoomPool(1, ModRoomPools.MINE_ROOMS);
		mineBentCorridor.registerDoorFill(1, new AreaDescriptor.Rectangle(new BlockPos(-2, -2, 0), 5, 5, 1));
		mineBentCorridor.oreDensity = 0.05f;
		mineBentCorridor.addDeadendRoomPool(1, ModRoomPools.MINE_DEADEND);

		DungeonRoom mineFourwayCorridor = new DungeonRoom().addStructure("bloodmagic:standard/mine_fourway", new BlockPos(0, 0, 0));
		mineFourwayCorridor.addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(19, 7, 20)));
		mineFourwayCorridor.addDoor(new BlockPos(9, 1, 0), Direction.NORTH, "mine", 1);
		mineFourwayCorridor.addDoor(new BlockPos(0, 1, 14), Direction.WEST, "mine", 1);
		mineFourwayCorridor.addDoor(new BlockPos(9, 1, 19), Direction.SOUTH, "mine", 1);
		mineFourwayCorridor.addDoor(new BlockPos(18, 1, 5), Direction.EAST, "mine", 1);
		mineFourwayCorridor.addNormalRoomPool(1, ModRoomPools.MINE_ROOMS);
		mineFourwayCorridor.registerDoorFill(1, new AreaDescriptor.Rectangle(new BlockPos(-2, -2, 0), 5, 5, 1));
		mineFourwayCorridor.oreDensity = 0.05f;
		mineFourwayCorridor.addDeadendRoomPool(1, ModRoomPools.MINE_DEADEND);

		DungeonRoom mineDeadend = new DungeonRoom().addStructure("bloodmagic:standard/mine_deadend", new BlockPos(0, 0, 0)).addAreaDescriptor(new Rectangle(new BlockPos(0, 0, 0), new BlockPos(11, 9, 9)));
		mineDeadend.addDoor(new BlockPos(5, 1, 0), Direction.NORTH, "mine", 1);
		mineDeadend.oreDensity = 0.2f;

		ArrayList<CompletableFuture<?>> futures =  new ArrayList<>( List.of(
		addDungeonRoom(cache, miniArmoury, ModDungeons.MINI_ARMOURY),
		addDungeonRoom(cache, miniCrypt, ModDungeons.MINI_CRYPT),
		addDungeonRoom(cache, miniFarm, ModDungeons.MINI_FARM),
		addDungeonRoom(cache, miniLibrary, ModDungeons.MINI_LIBRARY),
		addDungeonRoom(cache, miniPortalRoom, ModDungeons.MINI_PORTAL),

		addDungeonRoom(cache, fourWayCorridor, ModDungeons.FOUR_WAY_CORRIDOR),
		addDungeonRoom(cache, fourWayCorridorLoot, ModDungeons.FOUR_WAY_CORRIDOR_LOOT),
		addDungeonRoom(cache, overlapped_corridor, ModDungeons.OVERLAPPED_CORRIDOR),
		addDungeonRoom(cache, straightCorridor, ModDungeons.STRAIGHT_CORRIDOR),
		addDungeonRoom(cache, straightCorridor, ModDungeons.T_CORRIDOR),

		addDungeonRoom(cache, challengeTower, ModDungeons.CHALLENGE_TOWER),
		addDungeonRoom(cache, bigLibrary, ModDungeons.BIG_LIBRARY),
		addDungeonRoom(cache, smallCrane, ModDungeons.SMALL_CRANE),
		addDungeonRoom(cache, smallLibrary, ModDungeons.SMALL_LIBRARY),
		addDungeonRoom(cache, smallSmithy, ModDungeons.SMALL_SMITHY),
		addDungeonRoom(cache, tallSpiral, ModDungeons.TALL_SPIRAL),
		addDungeonRoom(cache, smallArena, ModDungeons.SMALL_ARENA),
		addDungeonRoom(cache, antechamber, ModDungeons.ANTECHAMBER),
		addDungeonRoom(cache, destroyedEndPortal, ModDungeons.DESTROYED_END_PORTAL),
		addDungeonRoom(cache, augCorridorLoot, ModDungeons.AUG_CORRIDOR_LOOT),

		addDungeonRoom(cache, oreHold, ModDungeons.ORE_HOLD_1),
		addDungeonRoom(cache, waterway, ModDungeons.WATER_WAY),
		addDungeonRoom(cache, mineEntrance, ModDungeons.MINE_ENTRANCE),
		addDungeonRoom(cache, mineKey, ModDungeons.MINE_KEY),

		addDungeonRoom(cache, minePit, ModDungeons.MINE_PIT),
		addDungeonRoom(cache, mineCornerZombieTrap, ModDungeons.MINE_CORNER_ZOMBIE_TRAP),
		addDungeonRoom(cache, mineSplitRoad, ModDungeons.MINE_SPLIT_ROAD),
		addDungeonRoom(cache, mineStation, ModDungeons.MINE_STATION),
		addDungeonRoom(cache, mineDownwardTunnel, ModDungeons.MINE_DOWNWARD_TUNNEL),
		addDungeonRoom(cache, mineJunctionStation, ModDungeons.MINE_JUNCTION_STATION),
		addDungeonRoom(cache, mineBuiltShaft, ModDungeons.MINE_BUILT_SHAFT),
		addDungeonRoom(cache, mineNatureCrossroad, ModDungeons.MINE_NATURE_CROSSROAD),
		addDungeonRoom(cache, mineWolfDen, ModDungeons.MINE_WOLF_DEN),
		addDungeonRoom(cache, mineOreCavern, ModDungeons.MINE_ORE_CAVERN),

		addDungeonRoom(cache, mineStraightCorridor, ModDungeons.MINE_STRAIGHT_CORRIDOR),
		addDungeonRoom(cache, mineBentCorridor, ModDungeons.MINE_BENT_CORRIDOR),
		addDungeonRoom(cache, mineFourwayCorridor, ModDungeons.MINE_FOURWAY_CORRIDOR),

		addDungeonRoom(cache, defaultDeadend, ModDungeons.DEFAULT_DEADEND),
		addDungeonRoom(cache, mineDeadend, ModDungeons.MINES_DEADEND)

		));
		futures.addAll(registerStarterRooms(cache));
		return futures;
	}

	public void addDefaultSpecialRoomPools(DungeonRoom room, int index)
	{
		room.addSpecialRoomPool(index, ModRoomPools.MINE_ENTRANCES);
		room.addSpecialRoomPool(index, ModRoomPools.MINE_KEY);
	}

	public void addMinesRoomPools(DungeonRoom room, int index)
	{
		room.addNormalRoomPool(index, ModRoomPools.MINE_CORRIDORS);
		room.addNormalRoomPool(index, ModRoomPools.MINE_ROOMS);
	}

	public ArrayList<CompletableFuture<?>> registerStarterRooms(CachedOutput cache)
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

		return new ArrayList<>(List.of(addDungeonRoom(cache, miniDungeon, ModDungeons.MINI_ENTRANCE),
				addDungeonRoom(cache, starterDungeon, ModDungeons.STANDARD_ENTRANCE)));
	}

	public CompletableFuture<?> addDungeonRoom(CachedOutput cache, DungeonRoom room, ResourceLocation schematicName)
	{
		JsonElement json = Serializers.GSON.toJsonTree(room);

		Path mainOutput = packOutput.getOutputFolder();
		String pathSuffix = "assets/" + schematicName.getNamespace() + "/schematics/" + schematicName.getPath() + ".json";
		Path outputPath = mainOutput.resolve(pathSuffix);
		return DataProvider.saveStable(cache, json, outputPath);
	}

	public CompletableFuture<?> addRoomPool(CachedOutput cache, Map<ResourceLocation, Integer> roomPool, ResourceLocation schematicName)
	{
		List<String> roomStringList = new ArrayList<>();
		for (Entry<ResourceLocation, Integer> roomEntry : roomPool.entrySet())
		{
			roomStringList.add(roomEntry.getValue() + ";" + roomEntry.getKey().toString());
		}

		JsonElement json = Serializers.GSON.toJsonTree(roomStringList);

		Path mainOutput = packOutput.getOutputFolder();
		String pathSuffix = "assets/" + schematicName.getNamespace() + "/schematics/" + schematicName.getPath() + ".json";
		Path outputPath = mainOutput.resolve(pathSuffix);
		return DataProvider.saveStable(cache, json, outputPath);
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		ArrayList<CompletableFuture<?>> futures = loadRoomPools(cache);
		futures.addAll(loadDungeons(cache));
		return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
	}

	@Override
	public String getName()
	{
		return "DungeonGenerator";
	}
}
