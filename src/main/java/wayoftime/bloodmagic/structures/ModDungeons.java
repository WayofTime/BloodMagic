package wayoftime.bloodmagic.structures;

import wayoftime.bloodmagic.BloodMagic;

public class ModDungeons
{
	public static void init()
	{
//        ResourceLocation resource = new ResourceLocation(Constants.Mod.MODID, "HallChest1");
//
//        Map<String, BlockPos> structureMap = new HashMap<String, BlockPos>();
//        structureMap.put(resource.toString(), new BlockPos(0, 0, 0));
//
//        Map<EnumFacing, List<BlockPos>> doorMap = new HashMap<EnumFacing, List<BlockPos>>();
//        List<AreaDescriptor.Rectangle> descriptorList = new ArrayList<AreaDescriptor.Rectangle>();
//        descriptorList.add(new AreaDescriptor.Rectangle(new BlockPos(0, 0, 0), 12, 5, 9));
//
//        DungeonUtil.addRoom(doorMap, EnumFacing.EAST, new BlockPos(11, 0, 4));
//        DungeonUtil.addRoom(doorMap, EnumFacing.WEST, new BlockPos(0, 0, 4));
//
//        DungeonRoom room = new DungeonRoom(structureMap, doorMap, descriptorList);
//        DungeonRoomLoader.saveSingleDungeon(room);
//
//        DungeonRoomRegistry.registerDungeonRoom(room, 1);
//
//        DungeonRoomLoader.saveDungeons();

		registerDungeonRooms();
		DungeonRoomLoader.loadDungeons();
	}

	public static void registerDungeonRooms()
	{
		DungeonRoomRegistry.registerUnloadedDungeonRoom(BloodMagic.rl("t_corridor"));
		DungeonRoomRegistry.registerUnloadedDungeonRoom(BloodMagic.rl("four_way_corridor_loot"));
		DungeonRoomRegistry.registerUnloadedDungeonRoom(BloodMagic.rl("four_way_corridor"));
		DungeonRoomRegistry.registerUnloadedDungeonRoom(BloodMagic.rl("standard/ore_hold_1"));
		DungeonRoomRegistry.registerUnloadedDungeonRoom(BloodMagic.rl("standard/mine_entrance"));
		DungeonRoomRegistry.registerUnloadedDungeonRoom(BloodMagic.rl("straight_corridor"));
		DungeonRoomRegistry.registerUnloadedDungeonRoom(BloodMagic.rl("spiral_staircase"));
		DungeonRoomRegistry.registerUnloadedDungeonRoom(BloodMagic.rl("t3_entrance"));
		DungeonRoomRegistry.registerUnloadedDungeonRoom(BloodMagic.rl("standard_entrance"));
		DungeonRoomRegistry.registerUnloadedDungeonRoom(BloodMagic.rl("mini_dungeon/library"));
		DungeonRoomRegistry.registerUnloadedDungeonRoom(BloodMagic.rl("mini_dungeon/armoury"));
		DungeonRoomRegistry.registerUnloadedDungeonRoom(BloodMagic.rl("mini_dungeon/farm"));
		DungeonRoomRegistry.registerUnloadedDungeonRoom(BloodMagic.rl("mini_dungeon/portal_nether"));
		DungeonRoomRegistry.registerUnloadedDungeonRoom(BloodMagic.rl("mini_dungeon/crypt"));
	}
}
