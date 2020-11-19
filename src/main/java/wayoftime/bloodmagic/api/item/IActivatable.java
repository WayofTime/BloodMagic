package wayoftime.bloodmagic.api.item;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

/**
 * Interface for activatable Items
 * To find the NBT Tag, use -> Constants.NBT.ACTIVATED
 */
public interface IActivatable
{
	default boolean getActivated(ItemStack stack)
	{
		return !stack.isEmpty() && stack.hasTag() && stack.getTag().getBoolean("activated");
	}

	@Nonnull
	default ItemStack setActivatedState(ItemStack stack, boolean activated)
	{
		if (!stack.isEmpty())
		{
			if (!stack.hasTag())
				stack.setTag(new CompoundNBT());

			stack.getTag().putBoolean("activated", activated);
		}

		return stack;
	}
}