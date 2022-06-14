package wayoftime.bloodmagic.api.compat;

import net.minecraft.world.item.ItemStack;

/**
 * An interface for items that have custom drainage behaviour when used in
 * certain alchemy recipes.
 */
public interface ICustomAlchemyConsumable
{
	ItemStack drainUseOnAlchemyCraft(ItemStack stack);
}
