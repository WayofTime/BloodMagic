package wayoftime.bloodmagic.structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiPredicate;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;

public class SpecialDungeonRoomPoolRegistry
{
	public static Map<ResourceLocation, BiPredicate<Integer, Integer>> predicateMap = new HashMap<>();
	public static Map<ResourceLocation, BlockState> stateMap = new HashMap<>();

	public static List<ResourceLocation> getSpecialRooms(int minimumRooms, int minimumDepth, Map<ResourceLocation, Integer> timeSincePlacement, List<ResourceLocation> bufferRoomPools)
	{
		// CHeck that it is not in the buffer first
		List<ResourceLocation> specialRoomPools = new ArrayList<>();

		for (Entry<ResourceLocation, BiPredicate<Integer, Integer>> entry : predicateMap.entrySet())
		{
			ResourceLocation roomPool = entry.getKey();
			if (bufferRoomPools.contains(roomPool) || timeSincePlacement.containsKey(roomPool))
			{
				continue;
			}

			if (entry.getValue().test(minimumRooms, minimumDepth))
			{
				System.out.println("Added special room: " + roomPool);
				specialRoomPools.add(roomPool);
			}
		}

//		specialRoomPools.add(ModRoomPools.MINE_ENTRANCES);
		return specialRoomPools;
	}

	public static void registerUniqueRoomPool(ResourceLocation roomPool, int minRooms, int minDepth)
	{
		predicateMap.put(roomPool, (x, y) -> x >= minRooms && y >= minDepth);
	}

	public static void registerUniqueRoomPool(ResourceLocation roomPool, int minRooms, int minDepth, BlockState placementState)
	{
		registerUniqueRoomPool(roomPool, minRooms, minDepth);
		stateMap.put(roomPool, placementState);
	}

	public static BlockState getSealBlockState(ResourceLocation roomPool)
	{
		if (stateMap.containsKey(roomPool))
		{
			return stateMap.get(roomPool);
		}

		return BloodMagicBlocks.SPECIAL_DUNGEON_SEAL.get().defaultBlockState();
	}
}
