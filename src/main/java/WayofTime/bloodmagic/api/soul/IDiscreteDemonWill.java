package WayofTime.bloodmagic.api.soul;

import net.minecraft.item.ItemStack;

public interface IDiscreteDemonWill
{
    public double getWill(ItemStack soulStack);

    /**
     * Drains the demonic will from the willStack. If all of the will is
     * drained, the willStack will be removed. Will only drain in discrete
     * amounts, determined by getDiscretization.
     * 
     * @param willStack
     * @param drainAmount
     * @return The amount of will drained.
     */
    public double drainWill(ItemStack willStack, double drainAmount);

    /**
     * Gets the discrete number for this demonic will.
     * 
     * @param willStack
     * @return
     */
    public double getDiscretization(ItemStack willStack);

    public EnumDemonWillType getType(ItemStack willStack);
}
