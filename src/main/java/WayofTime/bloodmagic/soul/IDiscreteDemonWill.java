package WayofTime.bloodmagic.soul;

import net.minecraft.item.ItemStack;

public interface IDiscreteDemonWill {
    /**
     * Obtains the amount of Will an ItemStack contains.
     *
     * @param soulStack - The stack to retrieve the Will from
     * @return - The amount of Will an ItemStack contains
     */
    double getWill(ItemStack soulStack);

    /**
     * Drains the demonic will from the willStack. If all of the will is
     * drained, the willStack will be removed. Will only drain in discrete
     * amounts, determined by getDiscretization.
     *
     * @param willStack   - The ItemStack of the will
     * @param drainAmount - The amount of Will to drain
     * @return The amount of will drained.
     */
    double drainWill(ItemStack willStack, double drainAmount);

    /**
     * Gets the discrete number for this demonic will.
     *
     * @param willStack - The ItemStack of the will
     * @return - The discrete number for the given stack.
     */
    double getDiscretization(ItemStack willStack);

    /**
     * Obtains the type of will this is.
     *
     * @param willStack - The ItemStack of the will
     * @return - The type of will this is.
     */
    EnumDemonWillType getType(ItemStack willStack);
}
