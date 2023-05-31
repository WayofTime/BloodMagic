package wayoftime.bloodmagic.structures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.util.RandomSource;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.util.BMLog;

public class Dungeon
{
	public static boolean placeStructureAtPosition(RandomSource rand, ServerLevel world, BlockPos pos)
	{
		String initialDoorName = "default";

		long startTime = System.nanoTime();

		// TODO: Change this
		Map<String, Map<Direction, List<BlockPos>>> availableDoorMasterMap = new HashMap<>(); // Map of doors. The
																								// EnumFacing indicates
		// what way this door faces.
//		Map<Direction, List<BlockPos>> availableDoorMap = new HashMap<>(); // Map of doors. The EnumFacing indicates
		// what way this door faces.
		List<AreaDescriptor> descriptorList = new ArrayList<>();
		Map<BlockPos, Pair<DungeonRoom, StructurePlaceSettings>> roomMap = new HashMap<>(); // Placement positions in
																							// terms
//																						// of actual positions

//		List<Pair<BlockPos, Pair<DungeonRoom, PlacementSettings>>> roomList = new ArrayList<>();

		StructurePlaceSettings settings = new StructurePlaceSettings();
		Mirror mir = Mirror.NONE;

		settings.setMirror(mir);

		Rotation rot = Rotation.NONE;

		settings.setRotation(rot);
		settings.setIgnoreEntities(true);
//		settings.setChunkPos(null);

		settings.addProcessor(new StoneToOreProcessor(0.0f));

//		settings.setReplacedBlock(null);

//		settings.setIgnoreStructureBlock(false);
		settings.setKnownShape(true);

//        PlacementSettings placementsettings = (new PlacementSettings()).setMirror(this.mirror).setRotation(this.rotation).setIgnoreEntities(this.ignoreEntities).setChunk((ChunkPos)null);
//        if (this.integrity < 1.0F) {
//           placementsettings.clearProcessors().addProcessor(new IntegrityProcessor(MathHelper.clamp(this.integrity, 0.0F, 1.0F))).setRandom(createRandom(this.seed));
//        }
//
//        BlockPos blockpos2 = blockpos.add(this.position);
//        p_242689_3_.placeInWorldChunk(p_242689_1_, blockpos2, placementsettings, createRandom(this.seed));

//		List<Rotation> rotationInfo = new ArrayList();

//		int n = 1;
		DungeonRoom room = getRandomRoom(rand);
		roomMap.put(pos, Pair.of(room, settings.copy()));
//		roomList.add(Pair.of(pos, Pair.of(room, settings.copy())));
		descriptorList.addAll(room.getAreaDescriptors(settings, pos));

		Map<Direction, List<BlockPos>> availableDoorMap = new HashMap<>();
		availableDoorMasterMap.put(initialDoorName, availableDoorMap);
		for (Direction facing : Direction.values())
		{
			if (availableDoorMap.containsKey(facing))
			{
				List<BlockPos> doorList = availableDoorMap.get(facing);
				doorList.addAll(room.getDoorOffsetsForFacing(settings, initialDoorName, facing, pos));
			} else
			{
				List<BlockPos> doorList = room.getDoorOffsetsForFacing(settings, initialDoorName, facing, pos);
				availableDoorMap.put(facing, doorList);
			}
		}

//		rotationInfo.add(settings.getRotation());

		// Initial AreaDescriptors and door positions are initialized. Time for fun!
		for (int i = 0; i < 100; i++)
		{
			List<String> typeList = new ArrayList<>(availableDoorMasterMap.keySet());
			String doorName = typeList.get(rand.nextInt(typeList.size()));
			availableDoorMap = null;

			if (availableDoorMasterMap.containsKey(doorName))
			{
				availableDoorMap = availableDoorMasterMap.get(doorName);
			} else
			{
				availableDoorMap = new HashMap<>();
				availableDoorMasterMap.put(doorName, availableDoorMap);
			}

			// Get which facing of doors are available.
			List<Direction> facingList = new ArrayList<>();
			for (Entry<Direction, List<BlockPos>> entry : availableDoorMap.entrySet())
			{
				if (entry.getValue() != null && !entry.getValue().isEmpty())
				{
					facingList.add(entry.getKey());
				}
			}

			Collections.shuffle(facingList); // Shuffle the list so that it is random what is chosen

			Pair<Direction, BlockPos> removedDoor1 = null;
			Pair<Direction, BlockPos> removedDoor2 = null;
			BlockPos roomLocation = null;

			testDirection: for (Direction doorFacing : facingList)
			{
				Direction oppositeDoorFacing = doorFacing.getOpposite();
				List<BlockPos> availableDoorList = availableDoorMap.get(doorFacing); // May need to copy here
				Collections.shuffle(availableDoorList);

				Rotation randRotation = Rotation.values()[rand.nextInt(Rotation.values().length)];
//				Rotation randRotation = Rotation.CLOCKWISE_90;
				settings.setRotation(randRotation); // Same for the Mirror
				DungeonRoom testingRoom = getRandomRoom(rand);

//				String doorType = testingRoom.getRandomDoorType(rand);

				List<BlockPos> otherDoorList = testingRoom.getDoorOffsetsForFacing(settings, doorName, oppositeDoorFacing, BlockPos.ZERO);
				if (otherDoorList != null && !otherDoorList.isEmpty())
				{
					// See if one of these doors works.
					Collections.shuffle(otherDoorList);
					BlockPos testDoor = otherDoorList.get(0);
					testDoor: for (BlockPos availableDoor : availableDoorList)
					{
						// TODO: Test if it fits, then add the doors to the list.
						roomLocation = availableDoor.subtract(testDoor).offset(doorFacing.getNormal());

						List<AreaDescriptor> descriptors = testingRoom.getAreaDescriptors(settings, roomLocation);
						for (AreaDescriptor testDesc : descriptors)
						{
							for (AreaDescriptor currentDesc : descriptorList)
							{
								if (testDesc.intersects(currentDesc))
								{
									break testDoor;
								}
							}
						}

						settings.clearProcessors();
						settings.addProcessor(new StoneToOreProcessor(testingRoom.oreDensity));

						roomMap.put(roomLocation, Pair.of(testingRoom, settings.copy()));
//						roomList.add(Pair.of(roomLocation, Pair.of(testingRoom, settings.copy())));
						descriptorList.addAll(descriptors);
						removedDoor1 = Pair.of(doorFacing, availableDoor);
						removedDoor2 = Pair.of(oppositeDoorFacing, testDoor.offset(roomLocation));

						room = testingRoom;
//						n++;
//						rotationInfo.add(randRotation);
//						System.out.println("Placement: " + n);
//
//						for (Direction facing : Direction.values())
//						{
//							List<BlockPos> testingDoorList = testingRoom.getDoorOffsetsForFacing(settings, facing, BlockPos.ZERO);
//							System.out.println("Door Facing: " + facing + ", Door List: " + testingDoorList);
//						}

						break testDirection;
					}

					break;
				}

//                Collections.shuffle(otherDoorList);
			}

			if (removedDoor1 != null)
			{
				for (String doorType : room.doorMap.keySet())
				{
					availableDoorMap = null;

					if (availableDoorMasterMap.containsKey(doorType))
					{
						availableDoorMap = availableDoorMasterMap.get(doorType);
					} else
					{
						availableDoorMap = new HashMap<>();
						availableDoorMasterMap.put(doorType, availableDoorMap);
					}

					for (Direction facing : Direction.values())
					{
						if (availableDoorMap.containsKey(facing))
						{
							List<BlockPos> doorList = availableDoorMap.get(facing);
							doorList.addAll(room.getDoorOffsetsForFacing(settings, doorType, facing, roomLocation));
						} else
						{
							List<BlockPos> doorList = room.getDoorOffsetsForFacing(settings, doorType, facing, roomLocation);
							availableDoorMap.put(facing, doorList);
						}
					}

					Direction face = removedDoor1.getKey();
					if (availableDoorMap.containsKey(face))
					{
						availableDoorMap.get(face).remove(removedDoor1.getRight());
					}
				}
			}

			if (removedDoor2 != null)
			{
				Direction face = removedDoor2.getKey();
				for (Entry<String, Map<Direction, List<BlockPos>>> entry : availableDoorMasterMap.entrySet())
				{
					availableDoorMap = entry.getValue();
					if (availableDoorMap.containsKey(face))
					{
						availableDoorMap.get(face).remove(removedDoor2.getRight());
					}
				}
			}
		}

		long endTime = System.nanoTime();

		long duration = (endTime - startTime); // divide by 1000000 to get milliseconds.
		BMLog.DEBUG.info("Duration: " + duration + "(ns), " + duration / 1000000 + "(ms)");

		// Building what I've got
//		n = 0;
		for (Entry<BlockPos, Pair<DungeonRoom, StructurePlaceSettings>> entry : roomMap.entrySet())
//		for (Pair<BlockPos, Pair<DungeonRoom, PlacementSettings>> entry : roomList)
		{
//			n++;
			BlockPos placementPos = entry.getKey();
			DungeonRoom placedRoom = entry.getValue().getKey();
			StructurePlaceSettings placementSettings = entry.getValue().getValue();

			placedRoom.placeStructureAtPosition(rand, placementSettings, world, placementPos);

//			world.setBlockState(placementPos, Blocks.REDSTONE_BLOCK.getDefaultState(), 3);
//			System.out.println("Supposed Rotation for " + n + ": " + rotationInfo.get(n - 1));
//			System.out.println("Placement: " + n + ", BlockPos: " + placementPos + ", Rotation: " + placementSettings.getRotation());
		}

//		System.out.println(roomMap.size());
//		System.out.println(roomList.size());

		return false;
	}

	public static DungeonRoom getRandomRoom(RandomSource rand)
	{
//		System.out.println("Dungeon size: " + DungeonRoomRegistry.dungeonWeightMap.size());
		return DungeonRoomRegistry.getRandomDungeonRoom(rand);
	}
}
