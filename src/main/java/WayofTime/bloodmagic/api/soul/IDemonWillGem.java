package WayofTime.bloodmagic.api.soul;

import net.minecraft.item.ItemStack;

public interface IDemonWillGem
{
    /**
     * 
     * @param willGemStack
     *        - The ItemStack for this demon will gem.
     * @param willStack
     *        - The ItemStack for the will. Item should extend IDemonWill
     * @return - The remainder willStack after the will has been absorbed into
     *         the gem. Return null if there is no will left in the stack.
     */
    public ItemStack fillDemonWillGem(ItemStack willGemStack, ItemStack willStack);

    /**
     * Returns the number of souls that are left in the soul gem. Returns a
     * double because souls can be fractionally drained.
     * 
     * @param willGemStack
     * @return
     */
    public double getWill(ItemStack willGemStack);

    public void setWill(ItemStack willGemStack, double amount);

    public int getMaxWill(ItemStack willGemStack);

    public double drainWill(ItemStack stack, double drainAmount);
}
