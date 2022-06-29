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
	public static ResourceLocation MINE_ENTRANCE = BloodMagic.rl("standard/mine_entrance");
	public static ResourceLocation STRAIGHT_CORRIDOR = BloodMagic.rl("straight_corridor");
	public static ResourceLocation SPIRAL_STAIRCASE = BloodMagic.rl("spiral_staircase");
	public static ResourceLocation MINI_LIBRARY = BloodMagic.rl("mini_dungeon/library");
	public static ResourceLocation MINI_ARMOURY = BloodMagic.rl("mini_dungeon/armoury");
	public static ResourceLocation MINI_FARM = BloodMagic.rl("mini_dungeon/farm");
	public static ResourceLocation MINI_PORTAL = BloodMagic.rl("mini_dungeon/portal_nether");
	public static ResourceLocation MINI_CRYPT = BloodMagic.rl("mini_dungeon/crypt");
	public static ResourceLocation WATER_WAY = BloodMagic.rl("standard/water_way");

	public static ResourceLocation MINI_ENTRANCE = BloodMagic.rl("t3_entrance");
	public static ResourceLocation MINE_PIT = BloodMagic.rl("mines/pit");
	public static ResourceLocation MINE_CORNER_ZOMBIE_TRAP = BloodMagic.rl("mines/corner_zombie_trap");
	public static ResourceLocation STANDARD_ENTRANCE = BloodMagic.rl("standard_entrance");

	public static ResourceLocation DEFAULT_DEADEND = BloodMagic.rl("default_deadend");

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
		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINE_ENTRANCE);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(STRAIGHT_CORRIDOR);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(OVERLAPPED_CORRIDOR);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(SPIRAL_STAIRCASE);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(WATER_WAY);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINE_PIT);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINE_CORNER_ZOMBIE_TRAP);

		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINI_LIBRARY);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINI_ARMOURY);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINI_FARM);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINI_PORTAL);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINI_CRYPT);

		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINI_ENTRANCE);
		DungeonRoomRegistry.registerUnloadedDungeonRoom(STANDARD_ENTRANCE);

		DungeonRoomRegistry.registerUnloadedDungeonRoom(DEFAULT_DEADEND);
	}
}
