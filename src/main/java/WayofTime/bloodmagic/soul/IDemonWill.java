package WayofTime.bloodmagic.soul;

import net.minecraft.item.ItemStack;

public interface IDemonWill {
    /**
     * Obtains the amount of Will an ItemStack contains.
     *
     * @param willStack - The stack to retrieve the Will from
     * @return - The amount of Will an ItemStack contains
     */
    double getWill(EnumDemonWillType type, ItemStack willStack);

    @Deprecated
    double getWill(ItemStack willStack);

    /**
     * Sets the amount of Will in a given ItemStack.
     *
     * @param willStack - The ItemStack of the Will
     * @param will      - The amount of will to set the stack to
     */
    void setWill(EnumDemonWillType type, ItemStack willStack, double will);

    @Deprecated
    void setWill(ItemStack willStack, double will);

    /**
     * Drains the demonic will from the willStack. If all of the will is
     * drained, the willStack will be removed.
     *
     * @param willStack   - The ItemStack of the will
     * @param drainAmount - The amount of Will to drain
     * @return The amount of will drained.
     */
    double drainWill(EnumDemonWillType type, ItemStack willStack, double drainAmount);

    @Deprecated
    double drainWill(ItemStack willStack, double drainAmount);

    /**
     * Creates a new ItemStack with the specified number of will. Implementation
     * should respect the number requested.
     *
     * @param meta   - The meta of the ItemStack to create
     * @param number - The amount of Will to create the Stack with.
     * @return - An ItemStack with the set amount of Will
     */
    ItemStack createWill(int meta, double number);

    EnumDemonWillType getType(ItemStack stack);
}
