package wayoftime.bloodmagic.api.item;

import net.minecraft.item.ItemStack;
import wayoftime.bloodmagic.will.EnumDemonWillType;

public interface IMultiWillTool
{
	EnumDemonWillType getCurrentType(ItemStack stack);
}