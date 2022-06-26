package wayoftime.bloodmagic.structures;

import net.minecraft.resources.ResourceLocation;
import wayoftime.bloodmagic.BloodMagic;

public class ModRoomPools
{
	public static ResourceLocation CONNECTIVE_CORRIDORS = BloodMagic.rl("room_pools/connective_corridors");
	public static ResourceLocation MINI_DUNGEON_ENTRANCES = BloodMagic.rl("room_pools/entrances/mini_dungeon_entrances");
	public static ResourceLocation STANDARD_DUNGEON_ENTRANCES = BloodMagic.rl("room_pools/entrances/standard_dungeon_entrances");
	public static ResourceLocation MINI_DUNGEON = BloodMagic.rl("room_pools/tier1/mini_dungeon");
	public static ResourceLocation CONNECTIVE_CORRIDORS_1 = BloodMagic.rl("room_pools/tier1/connective_corridors");
	public static ResourceLocation STANDARD_ROOMS = BloodMagic.rl("room_pools/tier2/standard_rooms");
	public static ResourceLocation MINE_ENTRANCES = BloodMagic.rl("room_pools/tier2/mine_entrances");

	public static void init()
	{
		registerDungeonRooms();
		DungeonRoomLoader.loadRoomPools();
	}

	public static void registerDungeonRooms()
	{
		DungeonRoomRegistry.registerUnloadedDungeonRoomPool(CONNECTIVE_CORRIDORS);
		DungeonRoomRegistry.registerUnloadedDungeonRoomPool(MINI_DUNGEON_ENTRANCES);
		DungeonRoomRegistry.registerUnloadedDungeonRoomPool(STANDARD_DUNGEON_ENTRANCES);
		DungeonRoomRegistry.registerUnloadedDungeonRoomPool(MINI_DUNGEON);
		DungeonRoomRegistry.registerUnloadedDungeonRoomPool(CONNECTIVE_CORRIDORS_1);
		DungeonRoomRegistry.registerUnloadedDungeonRoomPool(STANDARD_ROOMS);
		DungeonRoomRegistry.registerUnloadedDungeonRoomPool(MINE_ENTRANCES);
//		DungeonRoomRegistry.registerUnloadedDungeonRoom(FOUR_WAY_CORRIDOR_LOOT);
//		DungeonRoomRegistry.registerUnloadedDungeonRoom(FOUR_WAY_CORRIDOR);
//		DungeonRoomRegistry.registerUnloadedDungeonRoom(ORE_HOLD_1);
//		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINE_ENTRANCE);
//		DungeonRoomRegistry.registerUnloadedDungeonRoom(STRAIGHT_CORRIDOR);
//		DungeonRoomRegistry.registerUnloadedDungeonRoom(OVERLAPPED_CORRIDOR);
//		DungeonRoomRegistry.registerUnloadedDungeonRoom(SPIRAL_STAIRCASE);
//
//		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINI_LIBRARY);
//		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINI_ARMOURY);
//		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINI_FARM);
//		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINI_PORTAL);
//		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINI_CRYPT);
//
//		DungeonRoomRegistry.registerUnloadedDungeonRoom(MINI_ENTRANCE);
//		DungeonRoomRegistry.registerUnloadedDungeonRoom(STANDARD_ENTRANCE);
	}
}
