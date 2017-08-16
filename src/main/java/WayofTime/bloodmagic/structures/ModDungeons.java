package WayofTime.bloodmagic.structures;

public class ModDungeons {
    public static void init() {
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

        DungeonRoomLoader.loadDungeons();
    }
}
