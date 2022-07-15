package wayoftime.bloodmagic.structures;

import net.minecraft.resources.ResourceLocation;
import wayoftime.bloodmagic.BloodMagic;

public class ModDungeons
{
	public static ResourceLocation T_CORRIDOR = BloodMagic.rl("t_corridor");
	public static ResourceLocation FOUR_WAY_CORRIDOR_LOOT = BloodMagic.rl("four_way_corridor_loot");
	public static ResourceLocation FOUR_WAY_CORRIDOR = BloodMagic.rl("four_way_corridor");
	public static ResourceLocation OVERLAPPED_CORRIDOR = BloodMagic.rl("overlapped_corridor");
	public static ResourceLocation ORE_HOLD_1 = BloodMagic.rl("standard/ore_hold_1");
	public static ResourceLocation STRAIGHT_CORRIDOR = BloodMagic.rl("straight_corridor");
	public static ResourceLocation SPIRAL_STAIRCASE = BloodMagic.rl("spiral_staircase");
	public static ResourceLocation MINI_LIBRARY = BloodMagic.rl("mini_dungeon/library");
	public static ResourceLocation MINI_ARMOURY = BloodMagic.rl("mini_dungeon/armoury");
	public static ResourceLocation MINI_FARM = BloodMagic.rl("mini_dungeon/farm");
	public static ResourceLocation MINI_PORTAL = BloodMagic.rl("mini_dungeon/portal_nether");
	public static ResourceLocation MINI_CRYPT = BloodMagic.rl("mini_dungeon/crypt");
	public static ResourceLocation WATER_WAY = BloodMagic.rl("standard/water_way");

	public static ResourceLocation MINI_ENTRANCE = BloodMagic.rl("t3_entrance");

	public static ResourceLocation CHALLENGE_TOWER = BloodMagic.rl("standard/challenge_tower");
	public static ResourceLocation BIG_LIBRARY = BloodMagic.rl("standard/big_library");
	public static ResourceLocation SMALL_CRANE = BloodMagic.rl("standard/small_crane");
	public static ResourceLocation SMALL_LIBRARY = BloodMagic.rl("standard/small_library");
	public static ResourceLocation SMALL_SMITHY = BloodMagic.rl("standard/small_smithy");
	public static ResourceLocation TALL_SPIRAL = BloodMagic.rl("standard/tall_spiral");
	public static ResourceLocation SMALL_ARENA = BloodMagic.rl("standard/small_arena");
	public static ResourceLocation ANTECHAMBER = BloodMagic.rl("standard/antechamber");
	public static ResourceLocation DESTROYED_END_PORTAL = BloodMagic.rl("standard/destroyed_end_portal");
	public static ResourceLocation AUG_CORRIDOR_LOOT = BloodMagic.rl("standard/four_way_corridor_loot");

	public static ResourceLocation MINE_KEY = BloodMagic.rl("mines/mine_key");
	public static ResourceLocation MINE_ENTRANCE = BloodMagic.rl("standard/mine_entrance");

	public static ResourceLocation MINE_PIT = BloodMagic.rl("mines/pit");
	public static ResourceLocation MINE_CORNER_ZOMBIE_TRAP = BloodMagic.rl("mines/corner_zombie_trap");
	public static ResourceLocation MINE_SPLIT_ROAD = BloodMagic.rl("mines/split_road");
	public static ResourceLocation MINE_STATION = BloodMagic.rl("mines/station");
	public static ResourceLocation MINE_DOWNWARD_TUNNEL = BloodMagic.rl("mines/downward_tunnel");
	public static ResourceLocation MINE_JUNCTION_STATION = BloodMagic.rl("mines/junction_station");
	public static ResourceLocation MINE_BUILT_SHAFT = BloodMagic.rl("mines/downward_shaft");
	public static ResourceLocation MINE_NATURE_CROSSROAD = BloodMagic.rl("mines/nature_crossroad");
	public static ResourceLocation MINE_WOLF_DEN = BloodMagic.rl("mines/wolf_den");
	public static ResourceLocation MINE_ORE_CAVERN = BloodMagic.rl("mines/ore_cavern");

	public static ResourceLocation MINE_STRAIGHT_CORRIDOR = BloodMagic.rl("mines/straight_corridor");
	public static ResourceLocation MINE_BENT_CORRIDOR = BloodMagic.rl("mines/bent_corridor");
	public static ResourceLocation MINE_FOURWAY_CORRIDOR = BloodMagic.rl("mines/fourway_corridor");

	public static ResourceLocation STANDARD_ENTRANCE = BloodMagic.rl("standard_entrance");

	public static ResourceLocation DEFAULT_DEADEND = BloodMagic.rl("default_deadend");
	public static ResourceLocation MINES_DEADEND = BloodMagic.rl("mines/deadend");

	public static void init()
	{
		registerDungeonRooms();
		DungeonRoomLoader.loadDungeons();
	}

	public static void registerDungeonRooms()
	{
		DungeonRoomRegistry.registerUnloadedDungeonRoom(T_CORRIDOR);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(FOUR_WAY_CORRIDOR_LOOT);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(FOUR_WAY_CORRIDOR);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(ORE_HOLD_1);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(STRAIGHT_CORRIDOR);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(OVERLAPPED_CORRIDOR);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(SPIRAL_STAIRCASE);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(WATER_WAY);

		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINE_ENTRANCE);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINE_KEY);

		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINE_PIT);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINE_CORNER_ZOMBIE_TRAP);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINE_SPLIT_ROAD);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINE_STATION);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINE_DOWNWARD_TUNNEL);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINE_JUNCTION_STATION);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINE_BUILT_SHAFT);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINE_NATURE_CROSSROAD);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINE_WOLF_DEN);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINE_ORE_CAVERN);

		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINE_STRAIGHT_CORRIDOR);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINE_BENT_CORRIDOR);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINE_FOURWAY_CORRIDOR);

		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINI_LIBRARY);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINI_ARMOURY);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINI_FARM);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINI_PORTAL);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINI_CRYPT);

		DungeonRoomRegistry.registerUnloadedDungeonRoom(CHALLENGE_TOWER);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(BIG_LIBRARY);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(SMALL_CRANE);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(SMALL_LIBRARY);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(SMALL_SMITHY);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(TALL_SPIRAL);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(SMALL_ARENA);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(ANTECHAMBER);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(DESTROYED_END_PORTAL);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(AUG_CORRIDOR_LOOT);

		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINI_ENTRANCE);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(STANDARD_ENTRANCE);

		DungeonRoomRegistry.registerUnloadedDungeonRoom(DEFAULT_DEADEND);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINES_DEADEND);
	}
}
