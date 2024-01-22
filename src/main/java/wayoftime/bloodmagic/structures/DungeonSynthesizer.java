package wayoftime.bloodmagic.structures;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import org.apache.commons.lang3.tuple.Pair;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.tile.TileDungeonController;
import wayoftime.bloodmagic.common.tile.TileDungeonSeal;
import wayoftime.bloodmagic.common.tile.TileSpecialRoomDungeonSeal;
import wayoftime.bloodmagic.gson.Serializers;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.structures.rooms.DungeonRoomPlacement;
import wayoftime.bloodmagic.util.ChatUtil;
import wayoftime.bloodmagic.util.Constants;

import java.util.*;
import java.util.Map.Entry;

public class DungeonSynthesizer
{
	public static boolean displayDetailedInformation = false;

	public Map<String, Map<Direction, List<BlockPos>>> availableDoorMasterMap = new HashMap<>(); // Map of doors. The
																									// Direction
																									// indicates what
																									// way this
																									// door faces.

	public List<AreaDescriptor> descriptorList = new ArrayList<>();

	private int activatedDoors = 0;
//	private ResourceLocation specialRoomPool = BloodMagic.rl("room_pools/tier2/mine_entrances");

	private List<ResourceLocation> specialRoomBuffer = new ArrayList<>();
	private Map<ResourceLocation, Integer> placementsSinceLastSpecial = new HashMap<>();

	public TileDungeonController tile;

	public void setDungeonController(TileDungeonController tile)
	{
		this.tile = tile;
	}

	public boolean isAreaDescriptorInBounds(Level level, AreaDescriptor desc)
	{
		if (desc instanceof AreaDescriptor.Rectangle)
		{
			BlockPos maxOffset = ((AreaDescriptor.Rectangle) desc).getMaximumOffset();
			BlockPos minOffset = ((AreaDescriptor.Rectangle) desc).getMinimumOffset();

			return maxOffset.getY() < level.getMaxBuildHeight() && minOffset.getY() >= level.getMinBuildHeight();
		}

		return true;
	}

	public void writeToNBT(CompoundTag tag)
	{
		String json = Serializers.GSON.toJson(availableDoorMasterMap);
		tag.putString(Constants.NBT.DOOR_MAP, json);

		ListTag listnbt = new ListTag();
		for (int i = 0; i < descriptorList.size(); ++i)
		{
			AreaDescriptor desc = descriptorList.get(i);
			CompoundTag compoundnbt = new CompoundTag();
			desc.writeToNBT(compoundnbt);
			listnbt.add(compoundnbt);
		}

		if (!listnbt.isEmpty())
		{
			tag.put(Constants.NBT.AREA_DESCRIPTORS, listnbt);
		}

		ListTag bufferNBTList = new ListTag();
		for (int i = 0; i < specialRoomBuffer.size(); i++)
		{
			CompoundTag compoundnbt = new CompoundTag();
			compoundnbt.putString(Constants.NBT.ROOM_POOL, specialRoomBuffer.get(i).toString());
			bufferNBTList.add(compoundnbt);
		}

		if (!bufferNBTList.isEmpty())
		{
			tag.put(Constants.NBT.ROOM_POOL_BUFFER, bufferNBTList);
		}

		ListTag placementNBTList = new ListTag();
		for (Entry<ResourceLocation, Integer> entry : placementsSinceLastSpecial.entrySet())
		{
			CompoundTag compoundnbt = new CompoundTag();
			compoundnbt.putString(Constants.NBT.ROOM_POOL, entry.getKey().toString());
			compoundnbt.putInt(Constants.NBT.VALUE, entry.getValue());
			placementNBTList.add(compoundnbt);
		}

		if (!placementNBTList.isEmpty())
		{
			tag.put(Constants.NBT.ROOM_POOL_TRACKER, placementNBTList);
		}
	}

	public void readFromNBT(CompoundTag tag)
	{
		String testJson = tag.getString(Constants.NBT.DOOR_MAP);
		if (!testJson.isEmpty())
		{
			availableDoorMasterMap = Serializers.GSON.fromJson(testJson, new TypeToken<Map<String, Map<Direction, List<BlockPos>>>>()
			{
			}.getType());
		}

		ListTag listnbt = tag.getList(Constants.NBT.AREA_DESCRIPTORS, 10);
		for (int i = 0; i < listnbt.size(); i++)
		{
			CompoundTag compoundnbt = listnbt.getCompound(i);
			AreaDescriptor.Rectangle rec = new AreaDescriptor.Rectangle(BlockPos.ZERO, 0);
			rec.readFromNBT(compoundnbt);
			descriptorList.add(rec);
		}

		ListTag bufferNBTList = tag.getList(Constants.NBT.ROOM_POOL_BUFFER, 10);
		for (int i = 0; i < bufferNBTList.size(); i++)
		{
			CompoundTag compoundnbt = bufferNBTList.getCompound(i);
			specialRoomBuffer.add(new ResourceLocation(compoundnbt.getString(Constants.NBT.ROOM_POOL)));
		}

		ListTag trackerNBTList = tag.getList(Constants.NBT.ROOM_POOL_TRACKER, 10);
		for (int i = 0; i < trackerNBTList.size(); i++)
		{
			CompoundTag compoundnbt = trackerNBTList.getCompound(i);
			placementsSinceLastSpecial.put(new ResourceLocation(compoundnbt.getString(Constants.NBT.ROOM_POOL)), compoundnbt.getInt(Constants.NBT.VALUE));
		}
	}

	public BlockPos[] generateInitialRoom(ResourceLocation initialType, RandomSource rand, ServerLevel world, BlockPos spawningPosition)
	{

//		String initialDoorName = "default";
		StructurePlaceSettings settings = new StructurePlaceSettings();
		Mirror mir = Mirror.NONE;

		settings.setMirror(mir);

		Rotation rot = Rotation.NONE;

		settings.setRotation(rot);
		settings.setIgnoreEntities(true);
//		settings.setChunkPos(null);
//		settings.addProcessor(new StoneToOreProcessor(0.0f));
		settings.setKnownShape(true);

//		ResourceLocation initialType = new ResourceLocation("bloodmagic:room_pools/test_pool_1");
		DungeonRoom initialRoom = DungeonRoomRegistry.getRandomDungeonRoom(initialType, rand);
		BlockPos roomPlacementPosition = initialRoom.getInitialSpawnOffsetForControllerPos(settings, spawningPosition);

//		System.out.println("Initial room offset: " + roomPlacementPosition);

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

//		System.out.println("Available door master map: " + availableDoorMasterMap);

		addNewControllerBlock(world, spawningPosition);

		// TODO: Generate door blocks based off of room's doors.
//		Map<Pair<Direction, BlockPos>, List<String>> doorTypeMap = room.getPotentialConnectedRoomTypes(settings, pos);
		List<DungeonDoor> doorTypeMap = initialRoom.getPotentialConnectedRoomTypes(settings, roomPlacementPosition);
//		System.out.println("Size of doorTypeMap: " + doorTypeMap.size());
		for (DungeonDoor dungeonDoor : doorTypeMap)
		{
			this.addNewDoorBlock(null, dungeonDoor, world, spawningPosition, dungeonDoor.doorPos, dungeonDoor.doorDir, dungeonDoor.doorType, 0, 0, dungeonDoor.getRoomList(), dungeonDoor.getSpecialRoomList());
		}

		BlockPos playerPos = initialRoom.getPlayerSpawnLocationForPlacement(settings, roomPlacementPosition);
		BlockPos portalLocation = initialRoom.getPortalOffsetLocationForPlacement(settings, roomPlacementPosition);

		return new BlockPos[] { playerPos, portalLocation };
	}

	public void addNewControllerBlock(ServerLevel world, BlockPos controllerPos)
	{
//		world.setBlockState(controllerPos, Blocks.LAPIS_BLOCK.getDefaultState(), 3);
		world.setBlock(controllerPos, BloodMagicBlocks.DUNGEON_CONTROLLER.get().defaultBlockState(), 3);
		BlockEntity tile = world.getBlockEntity(controllerPos);
		if (tile instanceof TileDungeonController)
		{
			((TileDungeonController) tile).setDungeonSynthesizer(this);
//			((TileDungeonSeal) tile).acceptDoorInformation(controllerPos, doorBlockPos, doorFacing, doorType, potentialRoomTypes);
		}
	}

	public boolean isBlockInDescriptor(BlockPos blockPos)
	{
		for (AreaDescriptor descriptor : this.descriptorList)
		{
			if (descriptor.isWithinArea(blockPos))
			{
				return true;
			}
		}

		return false;
	}

	public boolean isAnyBlockInDescriptor(List<BlockPos> posList)
	{
		for (BlockPos pos : posList)
		{
			if (isBlockInDescriptor(pos))
			{
				return true;
			}
		}

		return false;
	}

	public boolean doesDescriptorIntersect(AreaDescriptor desc)
	{
		for (AreaDescriptor descriptor : this.descriptorList)
		{
			if (descriptor.intersects(desc))
			{
				return true;
			}
		}

		return false;
	}

	public boolean addNewDoorBlock(Player player, DungeonDoor door, ServerLevel world, BlockPos controllerPos, BlockPos doorBlockPos, Direction doorFacing, String doorType, int newRoomDepth, int highestBranchRoomDepth, List<ResourceLocation> potentialRoomTypes, List<ResourceLocation> specialRoomTypes)
	{
		if (highestBranchRoomDepth < newRoomDepth)
		{
			highestBranchRoomDepth = newRoomDepth;
		}

		BlockPos doorBlockOffsetPos = doorBlockPos.relative(doorFacing).relative(Direction.UP, 2);

//		if (isBlockInDescriptor(doorBlockOffsetPos))
//		{
//			return false;
//		}
//		world.setBlockState(doorBlockOffsetPos, Blocks.REDSTONE_BLOCK.getDefaultState(), 3);

//		List<BlockPos> fillerList = new ArrayList<>();
//		Direction rightDirection = doorFacing.getClockWise();
//		for (int i = -1; i <= 1; i++)
//		{
//			for (int j = -1; j <= 1; j++)
//			{
//				if (i == 0 && j == 0)
//				{
//					continue;
//				}
//
//				fillerList.add(doorBlockOffsetPos.relative(rightDirection, i).relative(Direction.UP, j));
//			}
//		}

		AreaDescriptor desc = door.descriptor;
		List<BlockPos> fillerList = desc.getContainedPositions(doorBlockOffsetPos);

//		System.out.println("Area descriptor: " + door.descriptor.getAABB(BlockPos.ZERO) + "\nFiller list size: " + fillerList.size());

		boolean doPlaceDoor = !doesDescriptorIntersect(desc);
		if (!doPlaceDoor)
		{
			// TODO: Don't place if we're right next to an empty door.
			for (BlockPos fillerPos : fillerList)
			{
				world.setBlockAndUpdate(fillerPos.relative(doorFacing.getOpposite()), BloodMagicBlocks.DUNGEON_BRICK_ASSORTED.get().defaultBlockState());
			}

			world.setBlockAndUpdate(doorBlockOffsetPos.relative(doorFacing.getOpposite()), BloodMagicBlocks.DUNGEON_BRICK_ASSORTED.get().defaultBlockState());

			return false;
		} else
		{
			for (BlockPos fillerPos : fillerList)
			{
				world.setBlockAndUpdate(fillerPos, BloodMagicBlocks.DUNGEON_BRICK_ASSORTED.get().defaultBlockState());
			}
		}

		ResourceLocation specialRoomType = getSpecialRoom(newRoomDepth, specialRoomTypes);
		if (specialRoomType != null)
		{
			DungeonRoom randomRoom = getRandomRoom(specialRoomType, world.random);
			if (randomRoom != null)
			{
				if (checkRequiredRoom(world, controllerPos, specialRoomType, doorBlockOffsetPos, randomRoom, world.random, doorBlockPos, doorFacing, doorType, newRoomDepth, highestBranchRoomDepth))
				{
					removeSpecialRoom(specialRoomType);

					if (player != null)
					{
						List<Component> toSend = Lists.newArrayList();
//						if (!binding.getOwnerId().equals(player.getGameProfile().getId()))
//							toSend.add(Component.translatable(tooltipBase + "otherNetwork", binding.getOwnerName()));
						toSend.add(Component.translatable("tooltip.bloodmagic.specialspawn"));
						ChatUtil.sendNoSpam(player, toSend.toArray(new Component[toSend.size()]));
					}

					return true;
				}
			} else
			{
				if (displayDetailedInformation)
					System.out.println("Uh oh.");
			}
		}

		potentialRoomTypes = modifyRoomTypes(potentialRoomTypes);

		world.setBlock(doorBlockOffsetPos, BloodMagicBlocks.DUNGEON_SEAL.get().defaultBlockState(), 3);
		BlockEntity tile = world.getBlockEntity(doorBlockOffsetPos);
		if (tile instanceof TileDungeonSeal)
		{
			((TileDungeonSeal) tile).acceptDoorInformation(controllerPos, doorBlockPos, doorFacing, doorType, newRoomDepth, highestBranchRoomDepth, potentialRoomTypes);
		}

		return true;
	}

	public List<ResourceLocation> modifyRoomTypes(List<ResourceLocation> potentialRoomTypes)
	{
		List<ResourceLocation> modifiedRoomTypes = new ArrayList<>(potentialRoomTypes);

		return modifiedRoomTypes;
	}

	public ResourceLocation getSpecialRoom(int currentRoomDepth, List<ResourceLocation> potentialSpecialRoomTypes)
	{
		if (potentialSpecialRoomTypes.isEmpty() || specialRoomBuffer.isEmpty())
		{
			return null;
		}

		for (ResourceLocation resource : potentialSpecialRoomTypes)
		{
			if (specialRoomBuffer.contains(resource))
			{
//				specialRoomBuffer.remove(resource);
				return resource;
			}
		}

		return potentialSpecialRoomTypes.get(0);
	}

	public void removeSpecialRoom(ResourceLocation resource)
	{
		if (specialRoomBuffer.contains(resource))
		{
			specialRoomBuffer.remove(resource);
		}

		placementsSinceLastSpecial.put(resource, 0);
		tile.setChanged();
		System.out.println("Removing: " + resource);
		System.out.println("Size of map: " + placementsSinceLastSpecial.size());
	}

	// TODO: Check the door that is going to be placed here. If the door can be
	// successfully added to the structure, add the area descriptors to the
	// synthesizer and then place a seal that contains the info to reconstruct the
	// room.

	public boolean checkRequiredRoom(ServerLevel world, BlockPos controllerPos, ResourceLocation specialRoomType, BlockPos doorBlockOffsetPos, DungeonRoom room, RandomSource rand, BlockPos activatedDoorPos, Direction doorFacing, String activatedDoorType, int newRoomDepth, int highestBranchRoomDepth)
	{
		StructurePlaceSettings settings = new StructurePlaceSettings();
		Mirror mir = Mirror.NONE;

		settings.setMirror(mir);

		Rotation rot = Rotation.NONE;

		settings.setRotation(rot);
		settings.setIgnoreEntities(false);
//		settings.setChunkPos(null);
//		settings.addProcessor(new StoneToOreProcessor(0.0f));
		settings.setKnownShape(true);

		DungeonRoom placedRoom = null;
		Pair<Direction, BlockPos> activatedDoor = Pair.of(doorFacing, activatedDoorPos);
		Pair<Direction, BlockPos> addedDoor = null;
		BlockPos roomLocation = null;

		Direction oppositeDoorFacing = doorFacing.getOpposite();

		List<Rotation> rotationList = Rotation.getShuffled(rand);
		Rotation finalRotation = null;

		// Got a random room, now test if any of the rotations have a valid door.
		rotationCheck: for (Rotation initialRotation : rotationList)
		{
			settings.setRotation(initialRotation);

			// TODO: Change this to use a "requestedDoorType".
			List<BlockPos> otherDoorList = room.getDoorOffsetsForFacing(settings, activatedDoorType, oppositeDoorFacing, BlockPos.ZERO);
			if (otherDoorList != null && !otherDoorList.isEmpty())
			{
				// Going to connect to this door! ...Hopefully.
				int doorIndex = rand.nextInt(otherDoorList.size());
				BlockPos testDoor = otherDoorList.get(doorIndex);

				roomLocation = activatedDoorPos.subtract(testDoor).offset(doorFacing.getNormal());

				List<AreaDescriptor> descriptors = room.getAreaDescriptors(settings, roomLocation);
				for (AreaDescriptor testDesc : descriptors)
				{
					if (!this.isAreaDescriptorInBounds(world, testDesc))
					{
						break rotationCheck;
					}

					for (AreaDescriptor currentDesc : descriptorList)
					{
						if (testDesc.intersects(currentDesc))
						{
							// TODO: Better exit condition?
							break rotationCheck;
						}
					}
				}

//				roomMap.put(roomLocation, Pair.of(testingRoom, settings.copy()));
				descriptorList.addAll(descriptors);
				addedDoor = Pair.of(oppositeDoorFacing, testDoor.offset(roomLocation));

				placedRoom = room;
				finalRotation = initialRotation;

				break;
			}
		}

		if (placedRoom == null)
		{
			// Did not manage to place the room.
			return false;
		}

//		placedRoom.placeStructureAtPosition(rand, settings, world, roomLocation);
		spawnDoorBlock(world, controllerPos, specialRoomType, doorBlockOffsetPos, doorFacing, activatedDoorPos, activatedDoorType, newRoomDepth, highestBranchRoomDepth, room, finalRotation, roomLocation);

		//
		tile.setChanged();

		return true;
	}

	// May not need doorType
	public void spawnDoorBlock(ServerLevel world, BlockPos controllerPos, ResourceLocation specialRoomType, BlockPos doorBlockOffsetPos, Direction doorFacing, BlockPos activatedDoorPos, String activatedDoorType, int roomDepth, int highestBranchRoomDepth, DungeonRoom room, Rotation rotation, BlockPos roomLocation)
	{
		// TODO: Change to a Door Block that contains this info.
		// Make sure to store the `specialRoomType` for the key to check against; the
		// '#' character is removed.

		world.setBlock(doorBlockOffsetPos, SpecialDungeonRoomPoolRegistry.getSealBlockState(specialRoomType), 3);
		BlockEntity tile = world.getBlockEntity(doorBlockOffsetPos);
		if (tile instanceof TileSpecialRoomDungeonSeal)
		{
			((TileSpecialRoomDungeonSeal) tile).acceptSpecificDoorInformation(world, controllerPos, specialRoomType, doorFacing, activatedDoorPos, activatedDoorType, roomDepth, highestBranchRoomDepth, room, rotation, roomLocation);
		}

//		forcePlacementOfRoom(world, controllerPos, doorFacing, activatedDoorPos, activatedDoorType, roomDepth, highestBranchRoomDepth, room, rotation, roomLocation);
//		world.setBlock(activatedDoorPos.below(), Blocks.REDSTONE_BLOCK.defaultBlockState(), 3);
	}

	/**
	 * Returns how successful the placement of the room was.
	 * 
	 * @param activatedRoomDepth     The depth that the Door was assigned.
	 * @param highestBranchRoomDepth The maximum depth for this path.
	 * @return
	 */
	public int addNewRoomToExistingDungeon(Player player, ServerLevel world, BlockPos controllerPos, ResourceLocation roomType, RandomSource rand, BlockPos activatedDoorPos, Direction doorFacing, String activatedDoorType, List<ResourceLocation> potentialRooms, int activatedRoomDepth, int highestBranchRoomDepth)
	{
//		System.out.println("Current room's depth info: " + activatedRoomDepth + "/" + highestBranchRoomDepth);
		for (int i = 0; i < 10; i++)
		{
			boolean testPlacement = attemptPlacementOfRandomRoom(player, world, controllerPos, roomType, rand, activatedDoorPos, doorFacing, activatedDoorType, activatedRoomDepth, highestBranchRoomDepth, potentialRooms, false);
			if (testPlacement)
			{
				tile.setChanged();
				return 0;
			}
		}

		ResourceLocation pathPool = new ResourceLocation("bloodmagic:room_pools/connective_corridors");
		if (attemptPlacementOfRandomRoom(player, world, controllerPos, pathPool, rand, activatedDoorPos, doorFacing, activatedDoorType, activatedRoomDepth, highestBranchRoomDepth, potentialRooms, true))
		{
			tile.setChanged();
			return 1;
		}

		return 2;
	}

	public boolean forcePlacementOfRoom(Player player, ServerLevel world, BlockPos controllerPos, Direction doorFacing, BlockPos activatedDoorPos, String activatedDoorType, int previousRoomDepth, int previousMaxDepth, DungeonRoom room, Rotation rotation, BlockPos roomLocation)
	{
		if (displayDetailedInformation)
			System.out.println("Forcing room! Room is: " + room);
		if (room == null)
		{
			return false;
		}

		StructurePlaceSettings settings = new StructurePlaceSettings();
		Mirror mir = Mirror.NONE;

		settings.setMirror(mir);

		Rotation rot = Rotation.NONE;

		settings.setRotation(rot);
		settings.setIgnoreEntities(false);
//		settings.setChunkPos(null);
//		settings.addProcessor(new StoneToOreProcessor(0.0f));
		settings.setKnownShape(true);
		settings.setRotation(rotation);

		DungeonRoom placedRoom = room;
		Pair<Direction, BlockPos> activatedDoor = Pair.of(doorFacing, activatedDoorPos);
		Pair<Direction, BlockPos> addedDoor = null;

//		System.out.println("Forced placed room name: " + roomName);

		Direction oppositeDoorFacing = doorFacing.getOpposite();
		addedDoor = Pair.of(oppositeDoorFacing, activatedDoorPos.relative(doorFacing));

		// Need to save: rotation, addedDoor, roomLocation, doorFacing, roomName

		settings.clearProcessors();
		settings.addProcessor(new StoneToOreProcessor(room.oreDensity));

		placedRoom.placeStructureAtPosition(world.random, settings, world, roomLocation);
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
		Collections.shuffle(doorTypeMap);
		boolean addedHigherPath = false;

		for (DungeonDoor dungeonDoor : doorTypeMap)
		{
			if (addedDoor.getKey().equals(dungeonDoor.doorDir) && addedDoor.getRight().equals(dungeonDoor.doorPos))
			{
				continue;
			}

			int newRoomDepth = previousRoomDepth + (addedHigherPath ? world.random.nextInt(2) * 2 - 1 : 1);
			addedHigherPath = true;
			{
				if (displayDetailedInformation)
					System.out.println("Room list: " + dungeonDoor.getRoomList());
				this.addNewDoorBlock(player, dungeonDoor, world, controllerPos, dungeonDoor.doorPos, dungeonDoor.doorDir, dungeonDoor.doorType, newRoomDepth, previousMaxDepth, dungeonDoor.getRoomList(), dungeonDoor.getSpecialRoomList());
			}
		}

		tile.setChanged();

		return true;
	}

	public DungeonRoomPlacement getRandomPlacement(ServerLevel world, BlockPos controllerPos, ResourceLocation roomType, RandomSource rand, BlockPos activatedDoorPos, Direction doorFacing, String activatedDoorType, int previousRoomDepth, int previousMaxDepth, List<ResourceLocation> potentialRooms, boolean extendCorriDoors)
	{
		StructurePlaceSettings settings = new StructurePlaceSettings();
		Mirror mir = Mirror.NONE;

		settings.setMirror(mir);

		Rotation rot = Rotation.NONE;

		settings.setRotation(rot);
		settings.setIgnoreEntities(false);
//		settings.setChunkPos(null);
//		settings.addProcessor(new StoneToOreProcessor(0.0f));
		settings.setKnownShape(true);

		DungeonRoom placedRoom = null;

		Pair<Direction, BlockPos> addedDoor = null;
		BlockPos roomLocation = null;

		Direction oppositeDoorFacing = doorFacing.getOpposite();
		DungeonRoom testingRoom = getRandomRoom(roomType, rand);

		if (displayDetailedInformation)
			System.out.println("Room type: " + roomType);

		List<Rotation> rotationList = Rotation.getShuffled(rand);

		// Got a random room, now test if any of the rotations have a valid door.
		rotationCheck: for (Rotation initialRotation : rotationList)
		{
			settings.setRotation(initialRotation);

			// TODO: Change this to use a "requestedDoorType".
			List<BlockPos> otherDoorList = testingRoom.getDoorOffsetsForFacing(settings, activatedDoorType, oppositeDoorFacing, BlockPos.ZERO);
			if (otherDoorList != null && !otherDoorList.isEmpty())
			{
				// Going to connect to this door! ...Hopefully.
				int doorIndex = rand.nextInt(otherDoorList.size());
				BlockPos testDoor = otherDoorList.get(doorIndex);

				roomLocation = activatedDoorPos.subtract(testDoor).offset(doorFacing.getNormal());

				List<AreaDescriptor> descriptors = testingRoom.getAreaDescriptors(settings, roomLocation);
				for (AreaDescriptor testDesc : descriptors)
				{
					if (!this.isAreaDescriptorInBounds(world, testDesc))
					{
						break rotationCheck;
					}

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

				addedDoor = Pair.of(oppositeDoorFacing, testDoor.offset(roomLocation));

				placedRoom = testingRoom;

				return new DungeonRoomPlacement(testingRoom, world, settings, roomLocation, addedDoor);
//				break;
			}
		}

		if (placedRoom == null)
		{
			// Did not manage to place the room.
			return null;
		}

		return null;
	}

	public boolean attemptPlacementOfRandomRoom(Player player, ServerLevel world, BlockPos controllerPos, ResourceLocation roomType, RandomSource rand, BlockPos activatedDoorPos, Direction doorFacing, String activatedDoorType, int previousRoomDepth, int previousMaxDepth, List<ResourceLocation> potentialRooms, boolean extendCorriDoors)
	{
		Pair<Direction, BlockPos> activatedDoor = Pair.of(doorFacing, activatedDoorPos);

		DungeonRoomPlacement placement = this.getRandomPlacement(world, controllerPos, roomType, rand, activatedDoorPos, doorFacing, activatedDoorType, previousRoomDepth, previousMaxDepth, potentialRooms, extendCorriDoors);
		if (placement == null)
		{
			return false;
		}

		Pair<Direction, BlockPos> addedDoor = placement.getEntrance();

		descriptorList.addAll(placement.getAreaDescriptors());

		placement.placeStructure();

		activatedDoors++;
		checkSpecialRoomRequirements(previousRoomDepth);

		for (String doorType : placement.getAllRoomTypes())
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
//				doorList.addAll(placedRoom.getDoorOffsetsForFacing(settings, doorType, facing, roomLocation));
				doorList.addAll(placement.getDoorOffsetsForFacing(doorType, facing));
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

//		List<DungeonDoor> doorTypeMap = placedRoom.getPotentialConnectedRoomTypes(settings, roomLocation);
		List<DungeonDoor> doorTypeMap = placement.getPotentialConnectedRoomTypes();

		Collections.shuffle(doorTypeMap);
		boolean addedHigherPath = false;

		for (DungeonDoor dungeonDoor : doorTypeMap)
		{
			if (addedDoor.getKey().equals(dungeonDoor.doorDir) && addedDoor.getRight().equals(dungeonDoor.doorPos))
			{
				continue;
			}

			if (extendCorriDoors)
			{
				this.addNewDoorBlock(player, dungeonDoor, world, controllerPos, dungeonDoor.doorPos, dungeonDoor.doorDir, activatedDoorType, previousRoomDepth, previousMaxDepth, potentialRooms, new ArrayList<>());
			} else
			{
				int newRoomDepth = previousRoomDepth + (addedHigherPath ? world.random.nextInt(2) * 2 - 1 : 1);

				if (displayDetailedInformation)
					System.out.println("Room list: " + dungeonDoor.getRoomList());

				List<ResourceLocation> roomList = dungeonDoor.isDeadend(newRoomDepth, previousMaxDepth)
						? dungeonDoor.getDeadendRoomList()
						: dungeonDoor.getRoomList();

				if (this.addNewDoorBlock(player, dungeonDoor, world, controllerPos, dungeonDoor.doorPos, dungeonDoor.doorDir, dungeonDoor.doorType, newRoomDepth, previousMaxDepth, roomList, dungeonDoor.getSpecialRoomList()))
				{
					addedHigherPath = true;
				} else
				{
					// Door was blocked...

				}
			}
		}

		return true;
	}

	public void checkSpecialRoomRequirements(int currentRoomDepth)
	{
		for (ResourceLocation res : this.placementsSinceLastSpecial.keySet())
		{
			placementsSinceLastSpecial.put(res, placementsSinceLastSpecial.get(res) + 1);
		}

//		if (displayDetailedInformation)
//			System.out.println("Number of activated doors: " + activatedDoors);
//		if (activatedDoors == 3)
//		{
////			specialRoomBuffer.add(specialRoomPool);
//		}
		List<ResourceLocation> newSpecialPools = SpecialDungeonRoomPoolRegistry.getSpecialRooms(activatedDoors, currentRoomDepth, placementsSinceLastSpecial, specialRoomBuffer);
//		if (displayDetailedInformation)
//			System.out.println("Number of added pools: " + newSpecialPools.size());

		for (ResourceLocation newSpecialPool : newSpecialPools)
		{
			if (!specialRoomBuffer.contains(newSpecialPool))
			{
				specialRoomBuffer.add(newSpecialPool);
				tile.setChanged();
			}
		}

//		specialRoomBuffer.addAll(newSpecialPools);
	}

	public static DungeonRoom getRandomRoom(ResourceLocation roomType, RandomSource rand)
	{
//		System.out.println("Dungeon size: " + DungeonRoomRegistry.dungeonWeightMap.size());
		return DungeonRoomRegistry.getRandomDungeonRoom(roomType, rand);
	}

	public static DungeonRoom getDungeonRoom(ResourceLocation dungeonName)
	{
		return DungeonRoomRegistry.getDungeonRoom(dungeonName);
	}
}