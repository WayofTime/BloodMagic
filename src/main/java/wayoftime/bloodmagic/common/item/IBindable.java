package wayoftime.bloodmagic.common.item;

import javax.annotation.Nullable;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import wayoftime.bloodmagic.core.data.Binding;

/**
 * Implement this interface on any Item that can be bound to a player.
 */
public interface IBindable
{
	/**
	 * Gets an object that stores who this item is bound to.
	 * <p>
	 * If the item is not bound, this will be null.
	 *
	 * @param stack - The owned ItemStack
	 * @return - The binding object
	 */
	@Nullable
	default Binding getBinding(ItemStack stack)
	{
		Binding binding = Binding.fromStack(stack);
		return !stack.isEmpty() && binding != null ? binding : null;
	}

	/**
	 * Called when the player attempts to bind the item.
	 *
	 * @param player - The Player attempting to bind the item
	 * @param stack  - The ItemStack to attempt binding
	 * @return If binding was successful.
	 */
	default boolean onBind(Player player, ItemStack stack)
	{
		return true;
	}
}