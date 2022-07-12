package wayoftime.bloodmagic.structures;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import wayoftime.bloodmagic.ritual.AreaDescriptor;

public class DungeonDoor
{
	public BlockPos doorPos;
	public Direction doorDir;
	public String doorType;
	private List<String> roomList; // List of room pools

	public AreaDescriptor descriptor;

	public DungeonDoor(BlockPos doorPos, Direction doorDir, String doorType, List<String> roomList, AreaDescriptor desc)
	{
		this.doorPos = doorPos;
		this.doorDir = doorDir;
		this.doorType = doorType;
		this.roomList = roomList;
		this.descriptor = desc;
	}

	public List<ResourceLocation> getRoomList()
	{
		List<ResourceLocation> rlRoomList = new ArrayList<>();
		for (String room : roomList)
		{
			if (!room.startsWith("#") && !room.startsWith("$"))
				rlRoomList.add(new ResourceLocation(room));
		}

		return rlRoomList;
	}

	public List<ResourceLocation> getSpecialRoomList()
	{
		List<ResourceLocation> rlRoomList = new ArrayList<>();
		for (String room : roomList)
		{
			if (room.startsWith("#"))
			{
				String[] splitString = room.split("#");
				rlRoomList.add(new ResourceLocation(splitString[1]));
			}
		}

		return rlRoomList;
	}

	public List<ResourceLocation> getDeadendRoomList()
	{
		List<ResourceLocation> rlRoomList = new ArrayList<>();
		for (String room : roomList)
		{
			if (room.startsWith("$"))
			{
				String[] splitString = room.split("\\$");
				rlRoomList.add(new ResourceLocation(splitString[1]));
			}
		}

		if (rlRoomList.isEmpty())
		{
			rlRoomList.add(ModRoomPools.DEFAULT_DEADEND);
		}

		return rlRoomList;
	}

	public boolean isDeadend(int roomDepth, int maxRoomDepth)
	{
		return (roomDepth < maxRoomDepth - 1);
	}
}
