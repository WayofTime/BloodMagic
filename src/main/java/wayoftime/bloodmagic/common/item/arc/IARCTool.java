package wayoftime.bloodmagic.common.item.arc;

import net.minecraft.world.item.ItemStack;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;

/**
 * Interface for items that affect ARC operation
 */
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

	default EnumDemonWillType getDominantWillType(ItemStack stack)
	{
		return EnumDemonWillType.DEFAULT;
	}
}
