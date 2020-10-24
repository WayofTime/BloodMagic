package wayoftime.bloodmagic.util.helper;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class NBTHelper
{
	public static ItemStack checkNBT(ItemStack stack)
	{
		if (stack.getTag() == null)
			stack.setTag(new CompoundNBT());

		return stack;
	}
}