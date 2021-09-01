package wayoftime.bloodmagic.structures;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import wayoftime.bloodmagic.BloodMagic;

public class ItemDungeonTester extends Item
{
	public ItemDungeonTester()
	{
		super(new Item.Properties().maxStackSize(1).group(BloodMagic.TAB));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		if (!world.isRemote && world instanceof ServerWorld)
		{
//			System.out.println("Test");
//			DungeonTester.testDungeonElementWithOutput((ServerWorld) world, player.getPosition());
			DungeonSynthesizer dungeon = new DungeonSynthesizer();
//			ResourceLocation initialType = new ResourceLocation("bloodmagic:room_pools/test_pool_1");
			ResourceLocation initialType = new ResourceLocation("bloodmagic:room_pools/entrances/mini_dungeon_entrances");
			dungeon.generateInitialRoom(initialType, world.rand, (ServerWorld) world, player.getPosition().offset(player.getAdjustedHorizontalFacing(), 2));

//			List<String> doorTypes = new ArrayList<>(dungeon.availableDoorMasterMap.keySet());
//			System.out.println(dungeon.availableDoorMasterMap.isEmpty());
//			String activatedDoorType = doorTypes.get(world.rand.nextInt(doorTypes.size()));
//			Map<Direction, List<BlockPos>> matchingDoorTypes = dungeon.availableDoorMasterMap.get(activatedDoorType);
//
////			List<Direction>
////			for(int i = 0; i < 4; i++)
////			{
////				Direction doorFacing = Direction.byHorizontalIndex(i);
////			}
//			List<Direction> directionList = new ArrayList<>(matchingDoorTypes.keySet());
//			for (Direction doorFacing : directionList)
//			{
////				Direction doorFacing = directionList.get(world.rand.nextInt(directionList.size()));
//
//				List<BlockPos> doorList = matchingDoorTypes.get(doorFacing);
//				if (doorList.isEmpty())
//				{
//					continue;
//				}
//
//				BlockPos activatedDoorPos = doorList.get(world.rand.nextInt(doorList.size()));
//
//				ResourceLocation roomType = new ResourceLocation("bloodmagic:room_pools/test_pool_2");
//				List<ResourceLocation> passList = new ArrayList<>();
//				passList.add(roomType);
//
//				dungeon.addNewRoomToExistingDungeon((ServerWorld) world, player.getPosition(), roomType, world.rand, activatedDoorPos, doorFacing, activatedDoorType, passList);
//				break;
//			}

//			List<Pair<String, Integer>> roomPool = new ArrayList<>();
//			roomPool.add(Pair.of("bloodmagic:schematics/ore_hold_1", 1));
//			roomPool.add(Pair.of("bloodmagic:schematics/spiral_staircase", 5));
//			roomPool.add(Pair.of("bloodmagic:schematics/straight_corridor", 3));
//
//			DungeonRoomLoader.writeTestPool(roomPool);
		}
		return super.onItemRightClick(world, player, hand);
	}
}
