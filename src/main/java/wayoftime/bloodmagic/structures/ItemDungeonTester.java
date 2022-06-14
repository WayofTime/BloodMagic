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
		super(new Item.Properties().stacksTo(1).tab(BloodMagic.TAB));
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
	{
		if (!world.isClientSide && world instanceof ServerWorld)
		{
//			System.out.println("Test");
//			DungeonTester.testDungeonElementWithOutput((ServerWorld) world, player.getPosition());
			DungeonSynthesizer dungeon = new DungeonSynthesizer();
//			ResourceLocation initialType = new ResourceLocation("bloodmagic:room_pools/test_pool_1");
			ResourceLocation initialType = new ResourceLocation("bloodmagic:room_pools/entrances/mini_dungeon_entrances");
//			BlockPos safePlayerPosition = dungeon.generateInitialRoom(initialType, world.rand, (ServerWorld) world, player.getPosition().offset(player.getAdjustedHorizontalFacing(), 2));

//			player.setPosition(safePlayerPosition.getX(), safePlayerPosition.getY(), safePlayerPosition.getZ());
		}
		return super.use(world, player, hand);
	}
}
