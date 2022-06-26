package wayoftime.bloodmagic.structures;

import net.minecraft.resources.ResourceLocation;
import wayoftime.bloodmagic.BloodMagic;

public class ModRoomPools
{
	public static ResourceLocation CONNECTIVE_CORRIDORS = BloodMagic.rl("room_pools/connective_corridors");
	public static ResourceLocation MINI_DUNGEON_ENTRANCES = BloodMagic.rl("room_pools/entrances/mini_dungeon_entrances");
	public static ResourceLocation STANDARD_DUNGEON_ENTRANCES = BloodMagic.rl("room_pools/entrances/standard_dungeon_entrances");
	public static ResourceLocation MINI_DUNGEON = BloodMagic.rl("room_pools/tier1/mini_dungeon");
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
		DungeonRoomRegistry.registerUnloadedDungeonRoomPool(STANDARD_ROOMS);
		DungeonRoomRegistry.registerUnloadedDungeonRoomPool(MINE_ENTRANCES);

	}
}
