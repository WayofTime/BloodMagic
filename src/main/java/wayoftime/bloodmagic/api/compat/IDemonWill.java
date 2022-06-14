package wayoftime.bloodmagic.api.compat;

import net.minecraft.world.item.ItemStack;

/**
 * Interface for Items that contain Will
 */
public interface IDemonWill
{
	/**
	 * Obtains the amount of Will an ItemStack contains.
	 *
	 * @param willStack - The stack to retrieve the Will from
	 * @return - The amount of Will an ItemStack contains
	 */
	double getWill(EnumDemonWillType type, ItemStack willStack);

	/**
	 * Sets the amount of Will in a given ItemStack.
	 *
	 * @param willStack - The ItemStack of the Will
	 * @param will      - The amount of will to set the stack to
	 * @return True if successfully set.
	 */
	boolean setWill(EnumDemonWillType type, ItemStack willStack, double will);

	/**
	 * Drains the demonic will from the willStack. If all of the will is drained,
	 * the willStack will be removed.
	 *
	 * @param willStack   - The ItemStack of the will
	 * @param drainAmount - The amount of Will to drain
	 * @return The amount of will drained.
	 */
	double drainWill(EnumDemonWillType type, ItemStack willStack, double drainAmount);

	/**
	 * Creates a new ItemStack with the specified number of will. Implementation
	 * should respect the number requested.
	 *
	 * @param number - The amount of Will to create the Stack with.
	 * @return - An ItemStack with the set amount of Will
	 */
	ItemStack createWill(double number);

	EnumDemonWillType getType(ItemStack stack);
}