package wayoftime.bloodmagic.structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

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

	public Map<Direction, List<BlockPos>> doorMap = new HashMap<>(); // Map of doors. The EnumFacing indicates what way
																		// this door faces.
	public List<AreaDescriptor.Rectangle> descriptorList = new ArrayList<>();

	public DungeonRoom(Map<String, BlockPos> structureMap, Map<Direction, List<BlockPos>> doorMap, List<AreaDescriptor.Rectangle> descriptorList)
	{
		this.structureMap = structureMap;
		this.doorMap = doorMap;
		this.descriptorList = descriptorList;
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

	public List<BlockPos> getDoorOffsetsForFacing(PlacementSettings settings, Direction facing, BlockPos offset)
	{
		List<BlockPos> offsetList = new ArrayList<>();

//		Direction originalFacing = DungeonUtil.rotate(settings.getMirror(), settings.getRotation(), facing);
		Direction originalFacing = DungeonUtil.reverseRotate(settings.getMirror(), settings.getRotation(), facing);
//		Direction originalFacing = facing;
		if (doorMap.containsKey(originalFacing))
		{
			List<BlockPos> doorList = doorMap.get(originalFacing);
			for (BlockPos doorPos : doorList)
			{
				offsetList.add(Template.transformedBlockPos(settings, doorPos).add(offset));
			}
		}

		return offsetList;
	}

	public boolean placeStructureAtPosition(Random rand, PlacementSettings settings, ServerWorld world, BlockPos pos)
	{
		for (Entry<String, BlockPos> entry : structureMap.entrySet())
		{
			ResourceLocation location = new ResourceLocation(entry.getKey());
			DungeonStructure structure = new DungeonStructure(location);
			BlockPos offsetPos = Template.transformedBlockPos(settings, entry.getValue());

			structure.placeStructureAtPosition(rand, settings, world, pos.add(offsetPos));
		}

		return true;
	}
}
