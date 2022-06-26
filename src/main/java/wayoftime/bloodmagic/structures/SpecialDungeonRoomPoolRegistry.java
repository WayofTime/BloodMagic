package wayoftime.bloodmagic.structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

import net.minecraft.resources.ResourceLocation;

public class SpecialDungeonRoomPoolRegistry
{
	public static Map<ResourceLocation, BiPredicate<Integer, Integer>> predicateMap = new HashMap<>();

	public static List<ResourceLocation> getSpecialRooms(int minimumRooms, int minimumDepth, Map<ResourceLocation, Integer> timeSincePlacement, List<ResourceLocation> bufferRoomPools)
	{
		List<ResourceLocation> specialRoomPools = new ArrayList<>();
		specialRoomPools.add(ModRoomPools.MINE_ENTRANCES);
		return specialRoomPools;
	}

	public static void registerUniqueRoomPool(ResourceLocation roomPool, int minRooms, int minDepth)
	{
		predicateMap.put(roomPool, (x, y) -> x >= minRooms && y >= minDepth);
	}
}
