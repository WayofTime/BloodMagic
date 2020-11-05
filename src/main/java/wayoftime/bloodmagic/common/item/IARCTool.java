package wayoftime.bloodmagic.common.item;

import net.minecraft.item.ItemStack;

public interface IARCTool
{
	default double getCraftingSpeedMultiplier(ItemStack stack)
	{
		return 1;
	}

	default double getAdditionalOutputChanceMultiplier(ItemStack stack)
	{
		return 1;
	}
}
