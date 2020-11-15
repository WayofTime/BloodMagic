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
			System.out.println("Test");
			DungeonTester.testDungeonElementWithOutput((ServerWorld) world, player.getPosition());
		}
		return super.onItemRightClick(world, player, hand);
	}
}
