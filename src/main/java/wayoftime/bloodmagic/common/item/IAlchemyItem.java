package wayoftime.bloodmagic.common.item;

import net.minecraft.world.item.ItemStack;

public interface IAlchemyItem
{
	ItemStack onConsumeInput(ItemStack stack);

	boolean isStackChangedOnUse(ItemStack stack);
}
