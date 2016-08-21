package WayofTime.bloodmagic.structures;

public class ModDungeons
{
    public static void init()
    {
//        ResourceLocation resource = new ResourceLocation(Constants.Mod.MODID, "Corridor1");
//
//        Map<String, BlockPos> structureMap = new HashMap<String, BlockPos>();
//        structureMap.put(resource.toString(), new BlockPos(0, 0, 0));
//
//        Map<EnumFacing, List<BlockPos>> doorMap = new HashMap<EnumFacing, List<BlockPos>>();
//        List<AreaDescriptor.Rectangle> descriptorList = new ArrayList<AreaDescriptor.Rectangle>();
//        descriptorList.add(new AreaDescriptor.Rectangle(new BlockPos(0, 0, 0), 5, 3, 7));
//
//        DungeonUtil.addRoom(doorMap, EnumFacing.NORTH, new BlockPos(3, 0, 0));
//        DungeonUtil.addRoom(doorMap, EnumFacing.SOUTH, new BlockPos(3, 0, 6));
//        DungeonUtil.addRoom(doorMap, EnumFacing.WEST, new BlockPos(0, 0, 3));
//
//        DungeonRoom room = new DungeonRoom(structureMap, doorMap, descriptorList);
//
//        DungeonRoomRegistry.registerDungeonRoom(room, 1);
//
//        DungeonRoomLoader.saveDungeons();

        DungeonRoomLoader.loadDungeons();
    }
}
