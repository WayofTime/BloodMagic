package wayoftime.bloodmagic.common.item;

import javax.annotation.Nonnull;

import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import wayoftime.bloodmagic.util.Constants;

/**
 * Interface for activatable Items
 */
public interface IActivatable
{
	default boolean getActivated(ItemStack stack)
	{
		return !stack.isEmpty() && stack.hasTag() && stack.getTag().getBoolean(Constants.NBT.ACTIVATED);
	}

	@Nonnull
	default ItemStack setActivatedState(ItemStack stack, boolean activated)
	{
		if (!stack.isEmpty())
		{
			if (!stack.hasTag())
				stack.setTag(new CompoundTag());

			stack.getTag().putBoolean(Constants.NBT.ACTIVATED, activated);
		}

		return stack;
	}
}