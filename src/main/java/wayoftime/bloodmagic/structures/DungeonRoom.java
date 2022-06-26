package wayoftime.bloodmagic.structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import wayoftime.bloodmagic.ritual.AreaDescriptor;

public class DungeonRoom
{
	public ResourceLocation key;
	public int dungeonWeight = 1;
	public Map<String, BlockPos> structureMap = new HashMap<>();

	public Map<String, Map<Direction, List<BlockPos>>> doorMap = new HashMap<>(); // Map of doors. The EnumFacing
																					// indicates what way
																					// this door faces.
	public List<AreaDescriptor.Rectangle> descriptorList = new ArrayList<>();

	// TODO: Add one-way doors - just needs a "connectTo" map...

	// TODO: Change this to a map with different keys to indicate other types of
	// post-processes.
	public float oreDensity = 0;
	public BlockPos spawnLocation = BlockPos.ZERO;
	public BlockPos controllerOffset = BlockPos.ZERO;
	public BlockPos portalOffset = BlockPos.ZERO;

	// Set of maps that contain the types of rooms that a given door (defined by its
	// BlockPos) can link to. Defined in this manner to reduce the file size of the
	// DungeonRoom.
//	public Map<BlockPos, Integer> doorToIndexMap = new TreeMap<>();
	public Map<Integer, List<BlockPos>> indexToDoorMap = new HashMap<>();
	public Map<Integer, List<String>> indexToRoomTypeMap = new HashMap<>();

	public Map<BlockPos, String> requiredDoorMap = new HashMap<>();

	public DungeonRoom(Map<String, BlockPos> structureMap, Map<String, Map<Direction, List<BlockPos>>> doorMap, List<AreaDescriptor.Rectangle> descriptorList)
	{
		this.structureMap = structureMap;
		this.doorMap = doorMap;
		this.descriptorList = descriptorList;
	}

	public DungeonRoom()
	{
		this(new HashMap<>(), new HashMap<>(), new ArrayList<>());
	}

	public DungeonRoom addStructure(String location, BlockPos pos)
	{
		structureMap.put(location, pos);
		return this;
	}

	public DungeonRoom addAreaDescriptor(AreaDescriptor.Rectangle descriptor)
	{
		descriptorList.add(descriptor);
		return this;
	}

	public DungeonRoom addDoor(BlockPos pos, Direction dir, String doorType, int index)
	{
		if (!doorMap.containsKey(doorType))
		{
			doorMap.put(doorType, new HashMap<>());
		}

		Map<Direction, List<BlockPos>> dirMap = doorMap.get(doorType);
		if (!dirMap.containsKey(dir))
		{
			dirMap.put(dir, new ArrayList<>());
		}

		dirMap.get(dir).add(pos);

		if (!indexToDoorMap.containsKey(index))
		{
			indexToDoorMap.put(index, new ArrayList<>());
		}

		indexToDoorMap.get(index).add(pos);

		return this;
	}

	public DungeonRoom addDoors(Direction dir, String doorType, int index, BlockPos... positions)
	{
		if (positions.length <= 0)
		{
			return this;
		}

		for (int i = 0; i < positions.length; i++)
		{
			addDoor(positions[i], dir, doorType, index);
		}

		return this;
	}

	public DungeonRoom addNormalRoomPool(int index, ResourceLocation roomPool)
	{
		return this.addRoomPool(index, roomPool.toString());
	}

	public DungeonRoom addSpecialRoomPool(int index, ResourceLocation roomPool)
	{
		return this.addRoomPool(index, "#" + roomPool.toString());
	}

	public DungeonRoom addRoomPool(int index, String roomPool)
	{
		if (!indexToRoomTypeMap.containsKey(index))
		{
			indexToRoomTypeMap.put(index, new ArrayList<>());
		}

		indexToRoomTypeMap.get(index).add(roomPool);

		return this;
	}

	public DungeonRoom setOreDensity(float oreDensity)
	{
		this.oreDensity = oreDensity;
		return this;
	}

	// REEEEEEEEEEEEEEEEE
	public List<DungeonDoor> getPotentialConnectedRoomTypes(StructurePlaceSettings settings, BlockPos offset)
	{
		// This DungeonDoor is stored in the door block.
		List<DungeonDoor> dungeonDoorList = new ArrayList<>();

//		Map<Integer, List<BlockPos>> rotatedIndexToDoorMap = new HashMap<>();
//		for (Entry<Integer, List<BlockPos>> entry : indexToDoorMap.entrySet())
//		{
//			List<BlockPos> rotatedBlockPosList = new ArrayList<>();
//			
//		}

		for (Entry<String, Map<Direction, List<BlockPos>>> entry : doorMap.entrySet())
		{

			Map<Direction, List<BlockPos>> doorDirMap = entry.getValue();

			// TODO: Change this to instead store the door type that you want to connect to?
			String doorType = entry.getKey();

			for (int i = 0; i < 4; i++)
			{
				Direction originalFacing = Direction.from2DDataValue(i);
				if (doorDirMap.containsKey(originalFacing))
				{
					Direction rotatedFacing = DungeonUtil.getFacingForSettings(settings, originalFacing);
					List<BlockPos> doorList = doorDirMap.get(originalFacing);
					if (indexToDoorMap == null || indexToDoorMap.isEmpty())
					{
						List<String> roomTypeList = new ArrayList<String>();
						for (BlockPos doorPos : doorList)
						{
							dungeonDoorList.add(new DungeonDoor(StructureTemplate.calculateRelativePosition(settings, doorPos).offset(offset), rotatedFacing, doorType, roomTypeList));
						}

						continue;
					}
					for (Entry<Integer, List<BlockPos>> rotatedIndexEntry : indexToDoorMap.entrySet())
					{
						int index = rotatedIndexEntry.getKey();
						List<String> roomTypeList = indexToRoomTypeMap.get(index);
						List<BlockPos> indexedDoorList = rotatedIndexEntry.getValue();
						for (BlockPos indexPos : indexedDoorList)
						{
							if (doorList.contains(indexPos))
							{
								dungeonDoorList.add(new DungeonDoor(StructureTemplate.calculateRelativePosition(settings, indexPos).offset(offset), rotatedFacing, doorType, roomTypeList));
							}
						}
					}

//					for (BlockPos doorPos : doorList)
//					{
//						int roomTypeIndex = doorToIndexMap == null ? -1 : doorToIndexMap.get(doorPos);
//						if (roomTypeIndex == -1)
//						{
//							dungeonDoorList.add(new DungeonDoor(Template.transformedBlockPos(settings, doorPos).add(offset), rotatedFacing, doorType, new ArrayList<>()));
////							offsetMap.put(Pair.of(rotatedFacing, Template.transformedBlockPos(settings, doorPos).add(offset)), new ArrayList<>());
//							continue;
//						}
//						List<String> roomTypeList = indexToRoomTypeMap.get(roomTypeIndex);
////						offsetMap.put(Pair.of(rotatedFacing, Template.transformedBlockPos(settings, doorPos).add(offset)), roomTypeList);
//						dungeonDoorList.add(new DungeonDoor(Template.transformedBlockPos(settings, doorPos).add(offset), rotatedFacing, doorType, roomTypeList));
//					}
				}
			}
		}

		return dungeonDoorList;
	}

//	public Pair<Rotation, DungeonDoor> getRandomDoor(BlockPos doorPos, Direction doorDir, String doorType)
//	{
//
//	}

	public List<AreaDescriptor> getAreaDescriptors(StructurePlaceSettings settings, BlockPos offset)
	{
		List<AreaDescriptor> newList = new ArrayList<>();

		for (AreaDescriptor desc : descriptorList)
		{
			newList.add(desc.rotateDescriptor(settings).offset(offset));
		}

		return newList;
	}

	public BlockPos getPlayerSpawnLocationForPlacement(StructurePlaceSettings settings, BlockPos offset)
	{
		return StructureTemplate.calculateRelativePosition(settings, spawnLocation).offset(offset);
	}

	public BlockPos getPortalOffsetLocationForPlacement(StructurePlaceSettings settings, BlockPos offset)
	{
		return StructureTemplate.calculateRelativePosition(settings, portalOffset).offset(offset);
	}

	public BlockPos getInitialSpawnOffsetForControllerPos(StructurePlaceSettings settings, BlockPos controllerPos)
	{
		if (controllerOffset == null)
		{
			return controllerPos;
		}

		return controllerPos.subtract(StructureTemplate.calculateRelativePosition(settings, controllerOffset));
	}

	public List<BlockPos> getDoorOffsetsForFacing(StructurePlaceSettings settings, String doorType, Direction facing, BlockPos offset)
	{
		List<BlockPos> offsetList = new ArrayList<>();

		if (doorMap.containsKey(doorType))
		{
			Map<Direction, List<BlockPos>> doorDirMap = doorMap.get(doorType);
//			Direction originalFacing = DungeonUtil.rotate(settings.getMirror(), settings.getRotation(), facing);
			Direction originalFacing = DungeonUtil.reverseRotate(settings.getMirror(), settings.getRotation(), facing);
//			Direction originalFacing = facing;
			if (doorDirMap.containsKey(originalFacing))
			{
				List<BlockPos> doorList = doorDirMap.get(originalFacing);
				for (BlockPos doorPos : doorList)
				{
					offsetList.add(StructureTemplate.calculateRelativePosition(settings, doorPos).offset(offset));
				}
			}
		}

		return offsetList;
	}

	public Map<String, List<BlockPos>> getAllDoorOffsetsForFacing(StructurePlaceSettings settings, Direction facing, BlockPos offset)
	{
		Map<String, List<BlockPos>> offsetMap = new HashMap<>();

		for (String type : doorMap.keySet())
		{
			offsetMap.put(type, getDoorOffsetsForFacing(settings, type, facing, offset));
		}

		return offsetMap;
	}

	public boolean placeStructureAtPosition(Random rand, StructurePlaceSettings settings, ServerLevel world, BlockPos pos)
	{
		Map<BlockPos, List<String>> compositeMap = new HashMap<>();
		for (Entry<String, BlockPos> entry : structureMap.entrySet())
		{
			BlockPos key = entry.getValue();
			String structure = entry.getKey();

			if (compositeMap.containsKey(key))
			{
				compositeMap.get(key).add(structure);
			} else
			{
				compositeMap.put(key, new ArrayList<>());
				compositeMap.get(key).add(structure);
			}
		}

		for (Entry<BlockPos, List<String>> entry : compositeMap.entrySet())
		{
			ResourceLocation location = new ResourceLocation(entry.getValue().get(rand.nextInt(entry.getValue().size())));
			BlockPos offsetPos = StructureTemplate.calculateRelativePosition(settings, entry.getKey());
			DungeonStructure structure = new DungeonStructure(location);

			structure.placeStructureAtPosition(rand, settings, world, pos.offset(offsetPos));
		}

		return true;
	}
}
