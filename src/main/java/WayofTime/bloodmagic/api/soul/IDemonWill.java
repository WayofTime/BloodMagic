package WayofTime.bloodmagic.api.soul;

import net.minecraft.item.ItemStack;

public interface IDemonWill
{
    public double getWill(ItemStack soulStack);

    public void setWill(ItemStack willStack, double will);

    /**
     * Drains the demonic will from the willStack. If all of the will is
     * drained, the willStack will be removed.
     * 
     * @param willStack
     * @param drainAmount
     * @return The amount of will drained.
     */
    public double drainWill(ItemStack willStack, double drainAmount);

    /**
     * Creates a new ItemStack with the specified number of will. Implementation
     * should respect the number requested.
     * 
     * @param meta
     * @param number
     * @return
     */
    public ItemStack createWill(int meta, double number);
}
