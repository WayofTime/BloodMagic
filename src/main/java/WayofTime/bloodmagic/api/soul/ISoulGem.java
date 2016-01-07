package WayofTime.bloodmagic.api.soul;

import net.minecraft.item.ItemStack;

public interface ISoulGem
{
    /**
     * 
     * @param soulGemStack
     *        - The ItemStack for this soul gem.
     * @param soulStack
     *        - The ItemStack for the soul. Item should extend ISoul
     * @return - The remainder soulStack after the souls have been absorbed into
     *         the gem. Return null if there are no souls left in the stack.
     */
    public ItemStack fillSoulGem(ItemStack soulGemStack, ItemStack soulStack);

    /**
     * Returns the number of souls that are left in the soul gem. Returns a
     * double because souls can be fractionally drained.
     * 
     * @param soulGemStack
     * @return
     */
    public double getSouls(ItemStack soulGemStack);

    public int getMaxSouls(ItemStack soulGemStack);

    public double drainSouls(ItemStack stack, double drainAmount);
}
