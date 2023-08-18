package wayoftime.bloodmagic.util.helper;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class NBTHelper
{
	public static ItemStack checkNBT(ItemStack stack)
	{
		if (stack.getTag() == null)
			stack.setTag(new CompoundTag());

		return stack;
	}
}
