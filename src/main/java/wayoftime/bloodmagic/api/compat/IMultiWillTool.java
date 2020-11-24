package wayoftime.bloodmagic.api.compat;

import net.minecraft.item.ItemStack;

/**
 * Interface for Items that can contain multiple Will types
 */
public interface IMultiWillTool
{
	EnumDemonWillType getCurrentType(ItemStack stack);
}