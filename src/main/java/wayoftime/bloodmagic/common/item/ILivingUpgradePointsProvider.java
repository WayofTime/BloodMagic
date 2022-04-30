package wayoftime.bloodmagic.common.item;

import net.minecraft.item.ItemStack;

public interface ILivingUpgradePointsProvider
{
	// It is assumed that all contained upgrade points are available to be syphoned,
	// regardless of how much is actually drained.
	public int getContainedUpgradePoints(ItemStack stack);

	public ItemStack drainUpgradePoints(ItemStack stack, int drain);

	// Remaining points in the item after the requested points are drained.
	public int getExcessUpgradePoints(ItemStack stack, int drain);
}
