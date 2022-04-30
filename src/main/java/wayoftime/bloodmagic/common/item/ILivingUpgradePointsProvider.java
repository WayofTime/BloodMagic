package wayoftime.bloodmagic.common.item;

import net.minecraft.item.ItemStack;

public interface ILivingUpgradePointsProvider
{
	public int getContainedUpgradePoints(ItemStack stack);

	public ItemStack drainUpgradePoints(ItemStack stack, int drain);

	public int getExcessUpgradePoints(ItemStack stack, int drain);
}
