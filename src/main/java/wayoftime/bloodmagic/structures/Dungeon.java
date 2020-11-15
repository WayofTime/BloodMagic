package wayoftime.bloodmagic.structures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.server.ServerWorld;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.util.BMLog;

public class Dungeon
{
	public static boolean placeStructureAtPosition(Random rand, ServerWorld world, BlockPos pos)
	{
		long startTime = System.nanoTime();

		Map<Direction, List<BlockPos>> availableDoorMap = new HashMap<>(); // Map of doors. The EnumFacing indicates
																			// what way this door faces.
		List<AreaDescriptor> descriptorList = new ArrayList<>();
		Map<BlockPos, Pair<DungeonRoom, PlacementSettings>> roomMap = new HashMap<>(); // Placement positions in terms
																						// of actual positions

		PlacementSettings settings = new PlacementSettings();
		Mirror mir = Mirror.NONE;

		settings.setMirror(mir);

		Rotation rot = Rotation.NONE;

		settings.setRotation(rot);
		settings.setIgnoreEntities(true);
		settings.setChunk(null);

//		settings.setReplacedBlock(null);

//		settings.setIgnoreStructureBlock(false);
		settings.func_215223_c(true);

//        PlacementSettings placementsettings = (new PlacementSettings()).setMirror(this.mirror).setRotation(this.rotation).setIgnoreEntities(this.ignoreEntities).setChunk((ChunkPos)null);
//        if (this.integrity < 1.0F) {
//           placementsettings.clearProcessors().addProcessor(new IntegrityProcessor(MathHelper.clamp(this.integrity, 0.0F, 1.0F))).setRandom(func_214074_b(this.seed));
//        }
//
//        BlockPos blockpos2 = blockpos.add(this.position);
//        p_242689_3_.func_237144_a_(p_242689_1_, blockpos2, placementsettings, func_214074_b(this.seed));

		DungeonRoom room = getRandomRoom(rand);
		roomMap.put(pos, Pair.of(room, settings.copy()));
		descriptorList.addAll(room.getAreaDescriptors(settings, pos));
		for (Direction facing : Direction.values())
		{
			if (availableDoorMap.containsKey(facing))
			{
				List<BlockPos> doorList = availableDoorMap.get(facing);
				doorList.addAll(room.getDoorOffsetsForFacing(settings, facing, pos));
			} else
			{
				List<BlockPos> doorList = room.getDoorOffsetsForFacing(settings, facing, pos);
				availableDoorMap.put(facing, doorList);
			}
		}

		// Initial AreaDescriptors and door positions are initialized. Time for fun!
		for (int i = 0; i < 100; i++)
		{
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

			for (Direction doorFacing : facingList)
			{
				Direction oppositeDoorFacing = doorFacing.getOpposite();
				List<BlockPos> availableDoorList = availableDoorMap.get(doorFacing); // May need to copy here
				Collections.shuffle(availableDoorList);

				settings.setRotation(Rotation.values()[rand.nextInt(Rotation.values().length)]); // Same for the Mirror
				DungeonRoom testingRoom = getRandomRoom(rand);

				List<BlockPos> otherDoorList = testingRoom.getDoorOffsetsForFacing(settings, oppositeDoorFacing, BlockPos.ZERO);
				if (otherDoorList != null && !otherDoorList.isEmpty())
				{
					// See if one of these doors works.
					Collections.shuffle(otherDoorList);
					BlockPos testDoor = otherDoorList.get(0);
					testDoor: for (BlockPos availableDoor : availableDoorList)
					{
						// TODO: Test if it fits, then add the doors to the list.
						roomLocation = availableDoor.subtract(testDoor).add(doorFacing.getDirectionVec());

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

						roomMap.put(roomLocation, Pair.of(testingRoom, settings.copy()));
						descriptorList.addAll(descriptors);
						removedDoor1 = Pair.of(doorFacing, availableDoor);
						removedDoor2 = Pair.of(oppositeDoorFacing, testDoor.add(roomLocation));

						room = testingRoom;

					}

					break;
				}

//                Collections.shuffle(otherDoorList);
			}

			if (removedDoor1 != null)
			{
				for (Direction facing : Direction.values())
				{
					if (availableDoorMap.containsKey(facing))
					{
						List<BlockPos> doorList = availableDoorMap.get(facing);
						doorList.addAll(room.getDoorOffsetsForFacing(settings, facing, roomLocation));
					} else
					{
						List<BlockPos> doorList = room.getDoorOffsetsForFacing(settings, facing, roomLocation);
						availableDoorMap.put(facing, doorList);
					}
				}

				Direction face = removedDoor1.getKey();
				if (availableDoorMap.containsKey(face))
				{
					availableDoorMap.get(face).remove(removedDoor1.getRight());
				}
			}

			if (removedDoor2 != null)
			{
				Direction face = removedDoor2.getKey();
				if (availableDoorMap.containsKey(face))
				{
					availableDoorMap.get(face).remove(removedDoor2.getRight());
				}
			}
		}

		long endTime = System.nanoTime();

		long duration = (endTime - startTime); // divide by 1000000 to get milliseconds.
		BMLog.DEBUG.info("Duration: " + duration + "(ns), " + duration / 1000000 + "(ms)");

		// Building what I've got
		for (Entry<BlockPos, Pair<DungeonRoom, PlacementSettings>> entry : roomMap.entrySet())
		{
			BlockPos placementPos = entry.getKey();
			DungeonRoom placedRoom = entry.getValue().getKey();
			PlacementSettings placementSettings = entry.getValue().getValue();

			placedRoom.placeStructureAtPosition(rand, placementSettings, world, placementPos);
		}

		System.out.println(roomMap.size());

		return false;
	}

	public static DungeonRoom getRandomRoom(Random rand)
	{
//		System.out.println("Dungeon size: " + DungeonRoomRegistry.dungeonWeightMap.size());
		return DungeonRoomRegistry.getRandomDungeonRoom(rand);
	}
}
