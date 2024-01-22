package wayoftime.bloodmagic.structures.rooms;

import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.util.RandomSource;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.structures.DungeonDoor;
import wayoftime.bloodmagic.structures.DungeonRoom;

public class DungeonRoomPlacement
{
	public DungeonRoom room;
	public RandomSource rand;
	public StructurePlaceSettings settings;
	public ServerLevel world;
	public BlockPos roomLocation;

	List<AreaDescriptor> descriptorList;
	List<DungeonDoor> containedDoorList;
	Pair<Direction, BlockPos> entrance;

	public DungeonRoomPlacement(DungeonRoom room, ServerLevel world, StructurePlaceSettings settings, BlockPos roomLocation, Pair<Direction, BlockPos> entrance)
	{
		this.rand = world.random;
		this.room = room;
		this.world = world;
		this.settings = settings;
		this.roomLocation = roomLocation;
		descriptorList = room.getAreaDescriptors(settings, roomLocation);
		containedDoorList = room.getPotentialConnectedRoomTypes(settings, roomLocation);
		this.entrance = entrance;
	}

	public void placeStructure()
	{
		room.placeStructureAtPosition(rand, settings, world, roomLocation);
	}

	public List<BlockPos> getDoorOffsetsForFacing(String doorType, Direction dir)
	{
		return room.getDoorOffsetsForFacing(settings, doorType, dir, roomLocation);
	}

	public List<AreaDescriptor> getAreaDescriptors()
	{
		return descriptorList;
	}

	public List<DungeonDoor> getPotentialConnectedRoomTypes()
	{
		return containedDoorList;
	}

	public Set<String> getAllRoomTypes()
	{
		return room.doorMap.keySet();
	}

	public Pair<Direction, BlockPos> getEntrance()
	{
		return entrance;
	}
}
