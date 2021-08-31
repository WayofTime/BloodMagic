package wayoftime.bloodmagic.structures;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
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
			dungeon.generateInitialRoom(world.rand, (ServerWorld) world, player.getPosition());

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
