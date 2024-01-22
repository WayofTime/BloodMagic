package wayoftime.bloodmagic.structures;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public class DungeonTester
{
	public static void testDungeonGeneration(ServerLevel world, BlockPos pos)
	{

	}

	public static void testDungeonElementWithOutput(ServerLevel world, BlockPos pos)
	{
		Dungeon.placeStructureAtPosition(world.random, world, pos);
//		ResourceLocation resource = new ResourceLocation(BloodMagic.MODID, "t_corridor");
//
////		DungeonStructure structure = new DungeonStructure(resource);
////		Map<DungeonStructure, BlockPos> structureMap = new HashMap<DungeonStructure, BlockPos>();
////		structureMap.put(structure, new BlockPos(0, 0, 0));
//
//		Map<String, BlockPos> structureMap = new HashMap();
//		structureMap.put(resource.toString(), BlockPos.ZERO);
//
////		Map<Direction, List<BlockPos>> doorMap = new HashMap<Direction, List<BlockPos>>();
////		List<AreaDescriptor.Rectangle> descriptorList = new ArrayList<AreaDescriptor.Rectangle>();
////		descriptorList.add(new AreaDescriptor.Rectangle(new BlockPos(0, 0, 0), 11, 5, 8));
////
////		DungeonUtil.addRoom(doorMap, Direction.NORTH, new BlockPos(5, 0, 0));
////		DungeonUtil.addRoom(doorMap, Direction.EAST, new BlockPos(10, 0, 5));
////		DungeonUtil.addRoom(doorMap, Direction.WEST, new BlockPos(0, 0, 5));
////
////		DungeonRoom room = new DungeonRoom(structureMap, doorMap, descriptorList);
//
//		DungeonRoom room = Dungeon.getRandomRoom(new Random());
//
//		PlacementSettings settings = new PlacementSettings();
//
//		Mirror mir = Mirror.NONE;
//		settings.setMirror(mir);
//
//		net.minecraft.util.Rotation rot = Rotation.NONE;
//		settings.setRotation(rot);
//
//		settings.setIgnoreEntities(true);
//		settings.setChunk((ChunkPos) null);
//		settings.setKnownShape(true);
////		settings.setReplacedBlock((Block) null);
////		settings.setIgnoreStructureBlock(false);
//
//		int i = 0;
//
//		for (Mirror mirror : Mirror.values())
//		{
//			System.out.print("Mirror: " + mirror + '\n');
//			int j = 0;
//			for (Rotation rotation : Rotation.values())
//			{
//				System.out.print("Rotation: " + rotation + '\n');
//				settings.setRotation(rotation);
//				settings.setMirror(mirror);
//
//				BlockPos offsetPos = pos.add(i * 32, 0, j * 32);
//				room.placeStructureAtPosition(new Random(), settings, world, offsetPos);
//
//				world.setBlockState(offsetPos, Blocks.REDSTONE_BLOCK.getDefaultState(), 3);
//
////				List<AreaDescriptor> descriptors = room.getAreaDescriptors(settings, offsetPos);
////				for (AreaDescriptor desc : descriptors)
////				{
////					List<BlockPos> posList = desc.getContainedPositions(new BlockPos(0, 0, 0));
////					for (BlockPos placePos : posList)
////					{
////						world.setBlockState(placePos, Blocks.REDSTONE_BLOCK.getDefaultState());
////					}
////				}
//
////                for (Direction facing : Direction.HORIZONTALS)
//				for (int k = 0; k < 4; k++)
//				{
//					Direction facing = Direction.byHorizontalIndex(k);
//					List<BlockPos> doorList = room.getDoorOffsetsForFacing(settings, facing, offsetPos);
//					for (BlockPos doorPos : doorList)
//					{
//						System.out.print("Door at " + doorPos + " facing " + facing + '\n');
//					}
//				}
//				j++;
//			}
//			i++;
//		}
	}
}
