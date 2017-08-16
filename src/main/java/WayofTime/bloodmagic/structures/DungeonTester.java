package WayofTime.bloodmagic.structures;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

import java.util.Random;

public class DungeonTester {
    public static void testDungeonGeneration(WorldServer world, BlockPos pos) {

    }

    public static void testDungeonElementWithOutput(WorldServer world, BlockPos pos) {
        Dungeon.placeStructureAtPosition(new Random(), world, pos);
//        ResourceLocation resource = new ResourceLocation(Constants.Mod.MODID, "Corridor1");
//
//        DungeonStructure structure = new DungeonStructure(resource);
//        Map<DungeonStructure, BlockPos> structureMap = new HashMap<DungeonStructure, BlockPos>();
//        structureMap.put(structure, new BlockPos(0, 0, 0));
//
//        Map<EnumFacing, List<BlockPos>> doorMap = new HashMap<EnumFacing, List<BlockPos>>();
//        List<AreaDescriptor> descriptorList = new ArrayList<AreaDescriptor>();
//        descriptorList.add(new AreaDescriptor.Rectangle(new BlockPos(0, 0, 0), 5, 3, 7));
//
//        DungeonUtil.addRoom(doorMap, EnumFacing.NORTH, new BlockPos(3, 0, 0));
//        DungeonUtil.addRoom(doorMap, EnumFacing.SOUTH, new BlockPos(3, 0, 6));
//        DungeonUtil.addRoom(doorMap, EnumFacing.WEST, new BlockPos(0, 0, 3));
//
//        DungeonRoom room = new DungeonRoom(structureMap, doorMap, descriptorList);
//
//        PlacementSettings settings = new PlacementSettings();
//
//        Mirror mir = Mirror.NONE;
//        settings.setMirror(mir);
//
//        Rotation rot = Rotation.NONE;
//        settings.setRotation(rot);
//
//        settings.setIgnoreEntities(true);
//        settings.setChunk((ChunkPos) null);
//        settings.setReplacedBlock((Block) null);
//        settings.setIgnoreStructureBlock(false);
//
//        int i = 0;
//
//        for (Mirror mirror : Mirror.values())
//        {
//            System.out.print("Mirror: " + mirror + '\n');
//            int j = 0;
//            for (Rotation rotation : Rotation.values())
//            {
//                System.out.print("Rotation: " + rot + '\n');
//                settings.setRotation(rotation);
//                settings.setMirror(mirror);
//
//                BlockPos offsetPos = pos.add(i * 16, 0, j * 16);
//                room.placeStructureAtPosition(new Random(), settings, world, offsetPos);
//
////                List<AreaDescriptor> descriptors = structure.getAreaDescriptors(settings, offsetPos);
////                for (AreaDescriptor desc : descriptors)
////                {
////                    List<BlockPos> posList = desc.getContainedPositions(new BlockPos(0, 0, 0));
////                    for (BlockPos placePos : posList)
////                    {
////                        world.setBlockState(placePos, Blocks.REDSTONE_BLOCK.getDefaultState());
////                    }
////                }
//
////                for (EnumFacing facing : EnumFacing.HORIZONTALS)
////                {
////                    List<BlockPos> doorList = structure.getDoorOffsetsForFacing(settings, facing);
////                    for (BlockPos doorPos : doorList)
////                    {
////                        System.out.print("Door at " + doorPos + " facing " + facing + '\n');
////                    }
////                }
//                j++;
//            }
//            i++;
//        }
    }
}
