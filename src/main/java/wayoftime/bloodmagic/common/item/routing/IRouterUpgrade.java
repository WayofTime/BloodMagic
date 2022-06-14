package wayoftime.bloodmagic.common.item.routing;

import net.minecraft.world.item.ItemStack;

public interface IRouterUpgrade
{
	// Gets the total transfer increase of the entire stack. If there are multiple
	// items in the stack, should respect the stack size.
	public int getMaxTransferIncrease(ItemStack stack);
}
