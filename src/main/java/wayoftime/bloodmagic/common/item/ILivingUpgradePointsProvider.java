package wayoftime.bloodmagic.common.item;

import net.minecraft.world.item.ItemStack;

public interface ILivingUpgradePointsProvider
{
	public int getTotalUpgradePoints(ItemStack stack);

	public int getAvailableUpgradePoints(ItemStack stack, int drain);

	public boolean canSyphonPoints(ItemStack stack, int drain);

	public ItemStack getResultingStack(ItemStack stack, int drain);

	// Remaining points in the item after the requested points are drained.
	public int getExcessUpgradePoints(ItemStack stack, int drain);

	// Lower numbers are consumed first.
	public int getPriority(ItemStack stack);
}
