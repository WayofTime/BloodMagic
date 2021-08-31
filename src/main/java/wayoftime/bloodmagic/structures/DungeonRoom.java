package wayoftime.bloodmagic.structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.server.ServerWorld;
import wayoftime.bloodmagic.ritual.AreaDescriptor;

public class DungeonRoom
{
	public int dungeonWeight = 1;
	public Map<String, BlockPos> structureMap = new HashMap<>();

	public Map<String, Map<Direction, List<BlockPos>>> doorMap = new HashMap<>(); // Map of doors. The EnumFacing
																					// indicates what way
																					// this door faces.
	public List<AreaDescriptor.Rectangle> descriptorList = new ArrayList<>();

	public float oreDensity = 0;
	public BlockPos spawnLocation = BlockPos.ZERO;

	// Set of maps that contain the types of rooms that a given door (defined by its
	// BlockPos) can link to. Defined in this manner to reduce the file size of the
	// DungeonRoom.
	public Map<BlockPos, Integer> doorToIndexMap = new HashMap<>();
	public Map<Integer, List<String>> indexToRoomTypeMap = new HashMap<>();

	public String type = "base";

	public DungeonRoom(Map<String, BlockPos> structureMap, Map<String, Map<Direction, List<BlockPos>>> doorMap, List<AreaDescriptor.Rectangle> descriptorList)
	{
		this.structureMap = structureMap;
		this.doorMap = doorMap;
		this.descriptorList = descriptorList;
	}

	// REEEEEEEEEEEEEEEEE
	public Map<Pair<Direction, BlockPos>, List<String>> getPotentialConnectedRoomTypes(PlacementSettings settings, BlockPos offset)
	{
		// This String list is stored in the door block.
		Map<Pair<Direction, BlockPos>, List<String>> offsetMap = new HashMap<>();

		for (Entry<String, Map<Direction, List<BlockPos>>> entry : doorMap.entrySet())
		{

			Map<Direction, List<BlockPos>> doorDirMap = entry.getValue();
//				Direction originalFacing = DungeonUtil.rotate(settings.getMirror(), settings.getRotation(), facing);
//			Direction originalFacing = DungeonUtil.reverseRotate(settings.getMirror(), settings.getRotation(), facing);
//				Direction originalFacing = facing;

			for (int i = 0; i < 4; i++)
			{
				Direction originalFacing = Direction.byHorizontalIndex(i);
				if (doorDirMap.containsKey(originalFacing))
				{
					Direction rotatedFacing = DungeonUtil.getFacingForSettings(settings, originalFacing);
					List<BlockPos> doorList = doorDirMap.get(originalFacing);
					for (BlockPos doorPos : doorList)
					{
						int roomTypeIndex = doorToIndexMap == null ? -1 : doorToIndexMap.get(doorPos);
						if (roomTypeIndex == -1)
						{
							offsetMap.put(Pair.of(rotatedFacing, Template.transformedBlockPos(settings, doorPos).add(offset)), new ArrayList<>());
							continue;
						}
						List<String> roomTypeList = indexToRoomTypeMap.get(roomTypeIndex);
						offsetMap.put(Pair.of(rotatedFacing, Template.transformedBlockPos(settings, doorPos).add(offset)), roomTypeList);
					}
				}
			}
		}

		return offsetMap;
	}

	public List<AreaDescriptor> getAreaDescriptors(PlacementSettings settings, BlockPos offset)
	{
		List<AreaDescriptor> newList = new ArrayList<>();

		for (AreaDescriptor desc : descriptorList)
		{
			newList.add(desc.rotateDescriptor(settings).offset(offset));
		}

		return newList;
	}

	public BlockPos getPlayerSpawnLocationForPlacement(PlacementSettings settings, BlockPos offset)
	{
		return Template.transformedBlockPos(settings, spawnLocation).add(offset);
	}

	public List<BlockPos> getDoorOffsetsForFacing(PlacementSettings settings, String doorType, Direction facing, BlockPos offset)
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
					offsetList.add(Template.transformedBlockPos(settings, doorPos).add(offset));
				}
			}
		}

		return offsetList;
	}

	public Map<String, List<BlockPos>> getAllDoorOffsetsForFacing(PlacementSettings settings, Direction facing, BlockPos offset)
	{
		Map<String, List<BlockPos>> offsetMap = new HashMap<>();

		for (String type : offsetMap.keySet())
		{
			offsetMap.put(type, getDoorOffsetsForFacing(settings, type, facing, offset));
		}

		return offsetMap;
	}

	public boolean placeStructureAtPosition(Random rand, PlacementSettings settings, ServerWorld world, BlockPos pos)
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
			BlockPos offsetPos = Template.transformedBlockPos(settings, entry.getKey());
			DungeonStructure structure = new DungeonStructure(location);

			structure.placeStructureAtPosition(rand, settings, world, pos.add(offsetPos));
		}

		return true;
	}
}
