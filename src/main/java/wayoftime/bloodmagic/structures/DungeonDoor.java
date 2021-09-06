package wayoftime.bloodmagic.structures;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class DungeonDoor
{
	public BlockPos doorPos;
	public Direction doorDir;
	public String doorType;
	private List<String> roomList;

	public DungeonDoor(BlockPos doorPos, Direction doorDir, String doorType, List<String> roomList)
	{
		this.doorPos = doorPos;
		this.doorDir = doorDir;
		this.doorType = doorType;
		this.roomList = roomList;
	}

	public List<ResourceLocation> getRoomList()
	{
		List<ResourceLocation> rlRoomList = new ArrayList<>();
		for (String room : roomList)
		{
			rlRoomList.add(new ResourceLocation(room));
		}

		return rlRoomList;
	}
}
