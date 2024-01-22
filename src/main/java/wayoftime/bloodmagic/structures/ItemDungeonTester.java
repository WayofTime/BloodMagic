package wayoftime.bloodmagic.structures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import wayoftime.bloodmagic.BloodMagic;

public class ItemDungeonTester extends Item
{
	public ItemDungeonTester()
	{
		super(new Item.Properties().stacksTo(1));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
	{
		if (!world.isClientSide && world instanceof ServerLevel)
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
