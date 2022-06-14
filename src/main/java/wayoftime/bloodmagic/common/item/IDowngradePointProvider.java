package wayoftime.bloodmagic.common.item;

import net.minecraft.world.item.ItemStack;

public interface IDowngradePointProvider
{
	public int getTotalPoints(ItemStack stack);

	/**
	 * 
	 * @param stack
	 * @param syphonPoints
	 * @return The number of points that are available
	 */
	public int getAvailablePoints(ItemStack stack, int syphonPoints);

	// Might not need this.
	public boolean canSyphonPoints(ItemStack stack, int syphonPoints);

	public ItemStack getResultingStack(ItemStack stack, int syphonedPoints);
}
