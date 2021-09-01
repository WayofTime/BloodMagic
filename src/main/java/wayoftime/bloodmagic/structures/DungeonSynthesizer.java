package wayoftime.bloodmagic.structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.reflect.TypeToken;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.server.ServerWorld;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.gson.Serializers;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.tile.TileDungeonController;
import wayoftime.bloodmagic.tile.TileDungeonSeal;
import wayoftime.bloodmagic.util.Constants;

public class DungeonSynthesizer
{
	public Map<String, Map<Direction, List<BlockPos>>> availableDoorMasterMap = new HashMap<>(); // Map of doors. The
																									// Direction
																									// indicates what
																									// way this
																									// door faces.

	public List<AreaDescriptor> descriptorList = new ArrayList<>();

	public void writeToNBT(CompoundNBT tag)
	{
		String json = Serializers.GSON.toJson(availableDoorMasterMap);
		tag.putString(Constants.NBT.DOOR_MAP, json);

		ListNBT listnbt = new ListNBT();
		for (int i = 0; i < descriptorList.size(); ++i)
		{
			AreaDescriptor desc = descriptorList.get(i);
			CompoundNBT compoundnbt = new CompoundNBT();
			desc.writeToNBT(compoundnbt);
			listnbt.add(compoundnbt);

		}

		if (!listnbt.isEmpty())
		{
			tag.put(Constants.NBT.AREA_DESCRIPTORS, listnbt);
		}
	}

	public void readFromNBT(CompoundNBT tag)
	{
		String testJson = tag.getString(Constants.NBT.DOOR_MAP);
		if (!testJson.isEmpty())
		{
			availableDoorMasterMap = Serializers.GSON.fromJson(testJson, new TypeToken<Map<String, Map<Direction, List<BlockPos>>>>()
			{
			}.getType());
		}

		ListNBT listnbt = tag.getList(Constants.NBT.AREA_DESCRIPTORS, 10);

		for (int i = 0; i < listnbt.size(); ++i)
		{
			CompoundNBT compoundnbt = listnbt.getCompound(i);
			AreaDescriptor.Rectangle rec = new AreaDescriptor.Rectangle(BlockPos.ZERO, 0);
			rec.readFromNBT(compoundnbt);
			descriptorList.add(rec);
		}
	}

	public BlockPos generateInitialRoom(ResourceLocation initialType, Random rand, ServerWorld world, BlockPos spawningPosition)
	{

//		String initialDoorName = "default";
		PlacementSettings settings = new PlacementSettings();
		Mirror mir = Mirror.NONE;

		settings.setMirror(mir);

		Rotation rot = Rotation.NONE;

		settings.setRotation(rot);
		settings.setIgnoreEntities(true);
		settings.setChunk(null);
//		settings.addProcessor(new StoneToOreProcessor(0.0f));
		settings.func_215223_c(true);

//		ResourceLocation initialType = new ResourceLocation("bloodmagic:room_pools/test_pool_1");
		DungeonRoom initialRoom = DungeonRoomRegistry.getRandomDungeonRoom(initialType, rand);
		BlockPos roomPlacementPosition = initialRoom.getInitialSpawnOffsetForControllerPos(settings, spawningPosition);

//		DungeonRoom room = getRandomRoom(rand);
//		roomMap.put(pos, Pair.of(room, settings.copy()));
//		roomList.add(Pair.of(pos, Pair.of(room, settings.copy())));
		descriptorList.addAll(initialRoom.getAreaDescriptors(settings, roomPlacementPosition));

//		Map<Direction, List<BlockPos>> availableDoorMap = new HashMap<>();
//		availableDoorMasterMap.put(initialDoorName, availableDoorMap);

		for (Direction facing : Direction.values())
		{
			Map<String, List<BlockPos>> doorTypeMap = initialRoom.getAllDoorOffsetsForFacing(settings, facing, roomPlacementPosition);
//			System.out.println("doorTypeMap size: " + doorTypeMap.entrySet().size());
			for (Entry<String, List<BlockPos>> entry : doorTypeMap.entrySet())
			{
				if (!availableDoorMasterMap.containsKey(entry.getKey()))
				{
					availableDoorMasterMap.put(entry.getKey(), new HashMap<>());
				}

				Map<Direction, List<BlockPos>> doorDirectionMap = availableDoorMasterMap.get(entry.getKey());
				if (!doorDirectionMap.containsKey(facing))
				{
					doorDirectionMap.put(facing, new ArrayList<BlockPos>());
				}

				doorDirectionMap.get(facing).addAll(entry.getValue());
			}
		}

		initialRoom.placeStructureAtPosition(rand, settings, world, roomPlacementPosition);

		addNewControllerBlock(world, spawningPosition);

		// TODO: Generate door blocks based off of room's doors.
//		Map<Pair<Direction, BlockPos>, List<String>> doorTypeMap = room.getPotentialConnectedRoomTypes(settings, pos);
		List<DungeonDoor> doorTypeMap = initialRoom.getPotentialConnectedRoomTypes(settings, roomPlacementPosition);
		System.out.println("Size of doorTypeMap: " + doorTypeMap.size());
		for (DungeonDoor dungeonDoor : doorTypeMap)
		{
			this.addNewDoorBlock(world, spawningPosition, dungeonDoor.doorPos, dungeonDoor.doorDir, dungeonDoor.doorType, dungeonDoor.getRoomList());
		}

		BlockPos playerPos = initialRoom.getPlayerSpawnLocationForPlacement(settings, roomPlacementPosition);

		return playerPos;
	}

	public void addNewControllerBlock(ServerWorld world, BlockPos controllerPos)
	{
//		world.setBlockState(controllerPos, Blocks.LAPIS_BLOCK.getDefaultState(), 3);
		world.setBlockState(controllerPos, BloodMagicBlocks.DUNGEON_CONTROLLER.get().getDefaultState(), 3);
		TileEntity tile = world.getTileEntity(controllerPos);
		if (tile instanceof TileDungeonController)
		{
			((TileDungeonController) tile).setDungeonSynthesizer(this);
//			((TileDungeonSeal) tile).acceptDoorInformation(controllerPos, doorBlockPos, doorFacing, doorType, potentialRoomTypes);
		}
	}

	public void addNewDoorBlock(ServerWorld world, BlockPos controllerPos, BlockPos doorBlockPos, Direction doorFacing, String doorType, List<ResourceLocation> potentialRoomTypes)
	{
		BlockPos doorBlockOffsetPos = doorBlockPos.offset(doorFacing).offset(Direction.UP, 2);
//		world.setBlockState(doorBlockOffsetPos, Blocks.REDSTONE_BLOCK.getDefaultState(), 3);
		Direction rightDirection = doorFacing.rotateY();
		for (int i = -1; i <= 1; i++)
		{
			for (int j = -1; j <= 1; j++)
			{
				if (i == 0 && j == 0)
				{
					continue;
				}

				world.setBlockState(doorBlockOffsetPos.offset(rightDirection, i).offset(Direction.UP, j), BloodMagicBlocks.DUNGEON_BRICK_ASSORTED.get().getDefaultState());
			}
		}

		world.setBlockState(doorBlockOffsetPos, BloodMagicBlocks.DUNGEON_SEAL.get().getDefaultState(), 3);
		TileEntity tile = world.getTileEntity(doorBlockOffsetPos);
		if (tile instanceof TileDungeonSeal)
		{
			((TileDungeonSeal) tile).acceptDoorInformation(controllerPos, doorBlockPos, doorFacing, doorType, potentialRoomTypes);
		}
	}

	// Returns how successful the placement of the
	public int addNewRoomToExistingDungeon(ServerWorld world, BlockPos controllerPos, ResourceLocation roomType, Random rand, BlockPos activatedDoorPos, Direction doorFacing, String activatedDoorType, List<ResourceLocation> potentialRooms)
	{
		for (int i = 0; i < 10; i++)
		{
			boolean testPlacement = attemptPlacementOfRandomRoom(world, controllerPos, roomType, rand, activatedDoorPos, doorFacing, activatedDoorType, potentialRooms, false);
			if (testPlacement)
			{
				return 0;
			}
		}

		ResourceLocation pathPool = new ResourceLocation("bloodmagic:room_pools/connective_corridors");
		if (attemptPlacementOfRandomRoom(world, controllerPos, pathPool, rand, activatedDoorPos, doorFacing, activatedDoorType, potentialRooms, true))
		{
			return 1;
		}

		return 2;
	}

//	public void addNewRoomToExistingDungeon(ServerWorld world, ResourceLocation roomType, Random rand, BlockPos activatedDoorPos, Direction doorFacing, String activatedDoorType, List<ResourceLocation> potentialRooms)
//	{
//		PlacementSettings settings = new PlacementSettings();
//		Mirror mir = Mirror.NONE;
//
//		settings.setMirror(mir);
//
//		Rotation rot = Rotation.NONE;
//
//		settings.setRotation(rot);
//		settings.setIgnoreEntities(true);
//		settings.setChunk(null);
////		settings.addProcessor(new StoneToOreProcessor(0.0f));
//		settings.func_215223_c(true);
//
//		DungeonRoom placedRoom = null;
//		Pair<Direction, BlockPos> activatedDoor = Pair.of(doorFacing, activatedDoorPos);
//		Pair<Direction, BlockPos> addedDoor = null;
//		BlockPos roomLocation = null;
//		// Initial AreaDescriptors and door positions are initialized. Time for fun!
//
//		testPlacement: for (int i = 0; i < 10; i++)
//		{
//			// Already know what door we are expanding from. Therefore, do not need to grab
//			// a random door.
//
//			Direction oppositeDoorFacing = doorFacing.getOpposite();
//			DungeonRoom testingRoom = getRandomRoom(roomType, rand);
//
//			List<Rotation> rotationList = Rotation.shuffledRotations(rand);
//
//			// Got a random room, now test if any of the rotations have a valid door.
//			for (Rotation initialRotation : rotationList)
//			{
//				settings.setRotation(initialRotation);
//				List<BlockPos> otherDoorList = testingRoom.getDoorOffsetsForFacing(settings, activatedDoorType, oppositeDoorFacing, BlockPos.ZERO);
//				if (otherDoorList != null && !otherDoorList.isEmpty())
//				{
//					// Going to connect to this door! ...Hopefully.
//					int doorIndex = rand.nextInt(otherDoorList.size());
//					BlockPos testDoor = otherDoorList.get(doorIndex);
//
//					roomLocation = activatedDoorPos.subtract(testDoor).add(doorFacing.getDirectionVec());
//
//					List<AreaDescriptor> descriptors = testingRoom.getAreaDescriptors(settings, roomLocation);
//					for (AreaDescriptor testDesc : descriptors)
//					{
//						for (AreaDescriptor currentDesc : descriptorList)
//						{
//							if (testDesc.intersects(currentDesc))
//							{
//								// TODO: Better exit condition?
//								continue testPlacement;
//							}
//						}
//					}
//
//					settings.clearProcessors();
//					settings.addProcessor(new StoneToOreProcessor(testingRoom.oreDensity));
//
//					roomMap.put(roomLocation, Pair.of(testingRoom, settings.copy()));
//					descriptorList.addAll(descriptors);
//					addedDoor = Pair.of(oppositeDoorFacing, testDoor.add(roomLocation));
//
//					placedRoom = testingRoom;
//
//					break testPlacement;
//				}
//			}
//		}
//
//		if (placedRoom != null)
//		{
//			placedRoom.placeStructureAtPosition(rand, settings, world, roomLocation);
//			for (String doorType : placedRoom.doorMap.keySet())
//			{
//				if (!availableDoorMasterMap.containsKey(doorType))
//				{
//					availableDoorMasterMap.put(doorType, new HashMap<>());
//				}
//
//				Map<Direction, List<BlockPos>> availableDoorMap = availableDoorMasterMap.get(doorType);
//				for (Direction facing : Direction.values())
//				{
//					if (!availableDoorMap.containsKey(facing))
//					{
//						availableDoorMap.put(facing, new ArrayList<>());
//					}
//
//					List<BlockPos> doorList = availableDoorMap.get(facing);
//					doorList.addAll(placedRoom.getDoorOffsetsForFacing(settings, doorType, facing, roomLocation));
//				}
//
//				if (doorType.equals(activatedDoorType))
//				{
//					Direction activatedDoorFace = activatedDoor.getKey();
//					if (availableDoorMap.containsKey(activatedDoorFace))
//					{
//						availableDoorMap.get(activatedDoorFace).remove(activatedDoor.getRight());
//					}
//
//					Direction addedDoorFace = addedDoor.getKey();
//					if (availableDoorMap.containsKey(addedDoorFace))
//					{
//						availableDoorMap.get(addedDoorFace).remove(addedDoor.getRight());
//					}
//				}
//
//			}
//
//			List<DungeonDoor> doorTypeMap = placedRoom.getPotentialConnectedRoomTypes(settings, roomLocation);
//
//			for (DungeonDoor dungeonDoor : doorTypeMap)
//			{
//				if (addedDoor.getKey().equals(dungeonDoor.doorDir) && addedDoor.getRight().equals(dungeonDoor.doorPos))
//				{
//					continue;
//				}
//
//				this.addNewDoorBlock(world, dungeonDoor.doorPos, dungeonDoor.doorDir, dungeonDoor.doorType, dungeonDoor.getRoomList());
//			}
//		}
//	}

	public boolean attemptPlacementOfRandomRoom(ServerWorld world, BlockPos controllerPos, ResourceLocation roomType, Random rand, BlockPos activatedDoorPos, Direction doorFacing, String activatedDoorType, List<ResourceLocation> potentialRooms, boolean extendCorriDoors)
	{
		PlacementSettings settings = new PlacementSettings();
		Mirror mir = Mirror.NONE;

		settings.setMirror(mir);

		Rotation rot = Rotation.NONE;

		settings.setRotation(rot);
		settings.setIgnoreEntities(true);
		settings.setChunk(null);
//		settings.addProcessor(new StoneToOreProcessor(0.0f));
		settings.func_215223_c(true);

		DungeonRoom placedRoom = null;
		Pair<Direction, BlockPos> activatedDoor = Pair.of(doorFacing, activatedDoorPos);
		Pair<Direction, BlockPos> addedDoor = null;
		BlockPos roomLocation = null;

		Direction oppositeDoorFacing = doorFacing.getOpposite();
		DungeonRoom testingRoom = getRandomRoom(roomType, rand);
//		System.out.println("Room type: " + roomType);

		List<Rotation> rotationList = Rotation.shuffledRotations(rand);

		// Got a random room, now test if any of the rotations have a valid door.
		rotationCheck: for (Rotation initialRotation : rotationList)
		{
			settings.setRotation(initialRotation);
			List<BlockPos> otherDoorList = testingRoom.getDoorOffsetsForFacing(settings, activatedDoorType, oppositeDoorFacing, BlockPos.ZERO);
			if (otherDoorList != null && !otherDoorList.isEmpty())
			{
				// Going to connect to this door! ...Hopefully.
				int doorIndex = rand.nextInt(otherDoorList.size());
				BlockPos testDoor = otherDoorList.get(doorIndex);

				roomLocation = activatedDoorPos.subtract(testDoor).add(doorFacing.getDirectionVec());

				List<AreaDescriptor> descriptors = testingRoom.getAreaDescriptors(settings, roomLocation);
				for (AreaDescriptor testDesc : descriptors)
				{
					for (AreaDescriptor currentDesc : descriptorList)
					{
						if (testDesc.intersects(currentDesc))
						{
							// TODO: Better exit condition?
							break rotationCheck;
						}
					}
				}

				settings.clearProcessors();
				settings.addProcessor(new StoneToOreProcessor(testingRoom.oreDensity));

//				roomMap.put(roomLocation, Pair.of(testingRoom, settings.copy()));
				descriptorList.addAll(descriptors);
				addedDoor = Pair.of(oppositeDoorFacing, testDoor.add(roomLocation));

				placedRoom = testingRoom;

				break;
			}
		}

		if (placedRoom == null)
		{
			// Did not manage to place the room.
			return false;
		}

		System.out.println("Room spawn location: " + roomLocation);

		placedRoom.placeStructureAtPosition(rand, settings, world, roomLocation);
		for (String doorType : placedRoom.doorMap.keySet())
		{
			if (!availableDoorMasterMap.containsKey(doorType))
			{
				availableDoorMasterMap.put(doorType, new HashMap<>());
			}

			Map<Direction, List<BlockPos>> availableDoorMap = availableDoorMasterMap.get(doorType);
			for (Direction facing : Direction.values())
			{
				if (!availableDoorMap.containsKey(facing))
				{
					availableDoorMap.put(facing, new ArrayList<>());
				}

				List<BlockPos> doorList = availableDoorMap.get(facing);
				doorList.addAll(placedRoom.getDoorOffsetsForFacing(settings, doorType, facing, roomLocation));
			}

			if (doorType.equals(activatedDoorType))
			{
				Direction activatedDoorFace = activatedDoor.getKey();
				if (availableDoorMap.containsKey(activatedDoorFace))
				{
					availableDoorMap.get(activatedDoorFace).remove(activatedDoor.getRight());
				}

				Direction addedDoorFace = addedDoor.getKey();
				if (availableDoorMap.containsKey(addedDoorFace))
				{
					availableDoorMap.get(addedDoorFace).remove(addedDoor.getRight());
				}
			}
		}

		List<DungeonDoor> doorTypeMap = placedRoom.getPotentialConnectedRoomTypes(settings, roomLocation);

		for (DungeonDoor dungeonDoor : doorTypeMap)
		{
			if (addedDoor.getKey().equals(dungeonDoor.doorDir) && addedDoor.getRight().equals(dungeonDoor.doorPos))
			{
				continue;
			}

			if (extendCorriDoors)
			{
				this.addNewDoorBlock(world, controllerPos, dungeonDoor.doorPos, dungeonDoor.doorDir, activatedDoorType, potentialRooms);
			} else
			{
				this.addNewDoorBlock(world, controllerPos, dungeonDoor.doorPos, dungeonDoor.doorDir, dungeonDoor.doorType, dungeonDoor.getRoomList());
			}
		}

		return true;
	}

	public static boolean placeStructureAtPosition(Random rand, ServerWorld world, BlockPos pos)
	{
		return false;
//		String initialDoorName = "default";
//
//		long startTime = System.nanoTime();
//
//		// TODO: Change this
//		Map<String, Map<Direction, List<BlockPos>>> availableDoorMasterMap = new HashMap<>(); // Map of doors. The
//																								// EnumFacing indicates
//		// what way this door faces.
////		Map<Direction, List<BlockPos>> availableDoorMap = new HashMap<>(); // Map of doors. The EnumFacing indicates
//		// what way this door faces.
//		List<AreaDescriptor> descriptorList = new ArrayList<>();
//		Map<BlockPos, Pair<DungeonRoom, PlacementSettings>> roomMap = new HashMap<>(); // Placement positions in terms
////																						// of actual positions
//
////		List<Pair<BlockPos, Pair<DungeonRoom, PlacementSettings>>> roomList = new ArrayList<>();
//
//		PlacementSettings settings = new PlacementSettings();
//		Mirror mir = Mirror.NONE;
//
//		settings.setMirror(mir);
//
//		Rotation rot = Rotation.NONE;
//
//		settings.setRotation(rot);
//		settings.setIgnoreEntities(true);
//		settings.setChunk(null);
//
//		settings.addProcessor(new StoneToOreProcessor(0.0f));
//
////		settings.setReplacedBlock(null);
//
////		settings.setIgnoreStructureBlock(false);
//		settings.func_215223_c(true);
//
////        PlacementSettings placementsettings = (new PlacementSettings()).setMirror(this.mirror).setRotation(this.rotation).setIgnoreEntities(this.ignoreEntities).setChunk((ChunkPos)null);
////        if (this.integrity < 1.0F) {
////           placementsettings.clearProcessors().addProcessor(new IntegrityProcessor(MathHelper.clamp(this.integrity, 0.0F, 1.0F))).setRandom(func_214074_b(this.seed));
////        }
////
////        BlockPos blockpos2 = blockpos.add(this.position);
////        p_242689_3_.func_237144_a_(p_242689_1_, blockpos2, placementsettings, func_214074_b(this.seed));
//
////		List<Rotation> rotationInfo = new ArrayList();
//
////		int n = 1;
//		DungeonRoom room = getRandomRoom(rand);
//		roomMap.put(pos, Pair.of(room, settings.copy()));
////		roomList.add(Pair.of(pos, Pair.of(room, settings.copy())));
//		descriptorList.addAll(room.getAreaDescriptors(settings, pos));
//
//		Map<Direction, List<BlockPos>> availableDoorMap = new HashMap<>();
//		availableDoorMasterMap.put(initialDoorName, availableDoorMap);
//		for (Direction facing : Direction.values())
//		{
//			if (availableDoorMap.containsKey(facing))
//			{
//				List<BlockPos> doorList = availableDoorMap.get(facing);
//				doorList.addAll(room.getDoorOffsetsForFacing(settings, initialDoorName, facing, pos));
//			} else
//			{
//				List<BlockPos> doorList = room.getDoorOffsetsForFacing(settings, initialDoorName, facing, pos);
//				availableDoorMap.put(facing, doorList);
//			}
//		}
//
////		rotationInfo.add(settings.getRotation());
//
//		// Initial AreaDescriptors and door positions are initialized. Time for fun!
//		for (int i = 0; i < 100; i++)
//		{
//			List<String> typeList = new ArrayList<>(availableDoorMasterMap.keySet());
//			String doorName = typeList.get(rand.nextInt(typeList.size()));
//			availableDoorMap = null;
//
//			if (availableDoorMasterMap.containsKey(doorName))
//			{
//				availableDoorMap = availableDoorMasterMap.get(doorName);
//			} else
//			{
//				availableDoorMap = new HashMap<>();
//				availableDoorMasterMap.put(doorName, availableDoorMap);
//			}
//
//			// Get which facing of doors are available.
//			List<Direction> facingList = new ArrayList<>();
//			for (Entry<Direction, List<BlockPos>> entry : availableDoorMap.entrySet())
//			{
//				if (entry.getValue() != null && !entry.getValue().isEmpty())
//				{
//					facingList.add(entry.getKey());
//				}
//			}
//
//			Collections.shuffle(facingList); // Shuffle the list so that it is random what is chosen
//
//			Pair<Direction, BlockPos> removedDoor1 = null;
//			Pair<Direction, BlockPos> removedDoor2 = null;
//			BlockPos roomLocation = null;
//
//			testDirection: for (Direction doorFacing : facingList)
//			{
//				Direction oppositeDoorFacing = doorFacing.getOpposite();
//				List<BlockPos> availableDoorList = availableDoorMap.get(doorFacing); // May need to copy here
//				Collections.shuffle(availableDoorList);
//
//				Rotation randRotation = Rotation.values()[rand.nextInt(Rotation.values().length)];
////				Rotation randRotation = Rotation.CLOCKWISE_90;
//				settings.setRotation(randRotation); // Same for the Mirror
//				DungeonRoom testingRoom = getRandomRoom(rand);
//
////				String doorType = testingRoom.getRandomDoorType(rand);
//
//				List<BlockPos> otherDoorList = testingRoom.getDoorOffsetsForFacing(settings, doorName, oppositeDoorFacing, BlockPos.ZERO);
//				if (otherDoorList != null && !otherDoorList.isEmpty())
//				{
//					// See if one of these doors works.
//					Collections.shuffle(otherDoorList);
//					BlockPos testDoor = otherDoorList.get(0);
//					testDoor: for (BlockPos availableDoor : availableDoorList)
//					{
//						// TODO: Test if it fits, then add the doors to the list.
//						roomLocation = availableDoor.subtract(testDoor).add(doorFacing.getDirectionVec());
//
//						List<AreaDescriptor> descriptors = testingRoom.getAreaDescriptors(settings, roomLocation);
//						for (AreaDescriptor testDesc : descriptors)
//						{
//							for (AreaDescriptor currentDesc : descriptorList)
//							{
//								if (testDesc.intersects(currentDesc))
//								{
//									break testDoor;
//								}
//							}
//						}
//
//						settings.clearProcessors();
//						settings.addProcessor(new StoneToOreProcessor(testingRoom.oreDensity));
//
//						roomMap.put(roomLocation, Pair.of(testingRoom, settings.copy()));
////						roomList.add(Pair.of(roomLocation, Pair.of(testingRoom, settings.copy())));
//						descriptorList.addAll(descriptors);
//						removedDoor1 = Pair.of(doorFacing, availableDoor);
//						removedDoor2 = Pair.of(oppositeDoorFacing, testDoor.add(roomLocation));
//
//						room = testingRoom;
////						n++;
////						rotationInfo.add(randRotation);
////						System.out.println("Placement: " + n);
////
////						for (Direction facing : Direction.values())
////						{
////							List<BlockPos> testingDoorList = testingRoom.getDoorOffsetsForFacing(settings, facing, BlockPos.ZERO);
////							System.out.println("Door Facing: " + facing + ", Door List: " + testingDoorList);
////						}
//
//						break testDirection;
//					}
//
//					break;
//				}
//
////                Collections.shuffle(otherDoorList);
//			}
//
//			if (removedDoor1 != null)
//			{
//				for (String doorType : room.doorMap.keySet())
//				{
//					availableDoorMap = null;
//
//					if (availableDoorMasterMap.containsKey(doorType))
//					{
//						availableDoorMap = availableDoorMasterMap.get(doorType);
//					} else
//					{
//						availableDoorMap = new HashMap<>();
//						availableDoorMasterMap.put(doorType, availableDoorMap);
//					}
//
//					for (Direction facing : Direction.values())
//					{
//						if (availableDoorMap.containsKey(facing))
//						{
//							List<BlockPos> doorList = availableDoorMap.get(facing);
//							doorList.addAll(room.getDoorOffsetsForFacing(settings, doorType, facing, roomLocation));
//						} else
//						{
//							List<BlockPos> doorList = room.getDoorOffsetsForFacing(settings, doorType, facing, roomLocation);
//							availableDoorMap.put(facing, doorList);
//						}
//					}
//
//					Direction face = removedDoor1.getKey();
//					if (availableDoorMap.containsKey(face))
//					{
//						availableDoorMap.get(face).remove(removedDoor1.getRight());
//					}
//				}
//			}
//
//			if (removedDoor2 != null)
//			{
//				Direction face = removedDoor2.getKey();
//				for (Entry<String, Map<Direction, List<BlockPos>>> entry : availableDoorMasterMap.entrySet())
//				{
//					availableDoorMap = entry.getValue();
//					if (availableDoorMap.containsKey(face))
//					{
//						availableDoorMap.get(face).remove(removedDoor2.getRight());
//					}
//				}
//			}
//		}
//
//		long endTime = System.nanoTime();
//
//		long duration = (endTime - startTime); // divide by 1000000 to get milliseconds.
//		BMLog.DEBUG.info("Duration: " + duration + "(ns), " + duration / 1000000 + "(ms)");
//
//		// Building what I've got
////		n = 0;
//		for (Entry<BlockPos, Pair<DungeonRoom, PlacementSettings>> entry : roomMap.entrySet())
////		for (Pair<BlockPos, Pair<DungeonRoom, PlacementSettings>> entry : roomList)
//		{
////			n++;
//			BlockPos placementPos = entry.getKey();
//			DungeonRoom placedRoom = entry.getValue().getKey();
//			PlacementSettings placementSettings = entry.getValue().getValue();
//
//			placedRoom.placeStructureAtPosition(rand, placementSettings, world, placementPos);
//
////			world.setBlockState(placementPos, Blocks.REDSTONE_BLOCK.getDefaultState(), 3);
////			System.out.println("Supposed Rotation for " + n + ": " + rotationInfo.get(n - 1));
////			System.out.println("Placement: " + n + ", BlockPos: " + placementPos + ", Rotation: " + placementSettings.getRotation());
//		}
//
////		System.out.println(roomMap.size());
////		System.out.println(roomList.size());
//
//		return false;
	}

	public static DungeonRoom getRandomRoom(ResourceLocation roomType, Random rand)
	{
//		System.out.println("Dungeon size: " + DungeonRoomRegistry.dungeonWeightMap.size());
		return DungeonRoomRegistry.getRandomDungeonRoom(roomType, rand);
	}
}