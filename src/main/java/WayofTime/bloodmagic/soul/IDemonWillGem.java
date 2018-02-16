package WayofTime.bloodmagic.soul;

import net.minecraft.item.ItemStack;

public interface IDemonWillGem {
    /**
     * @param willGemStack - The ItemStack for this demon will gem.
     * @param willStack    - The ItemStack for the will. Item should extend IDemonWill
     * @return - The remainder willStack after the will has been absorbed into
     * the gem. Return null if there is no will left in the stack.
     */
    ItemStack fillDemonWillGem(ItemStack willGemStack, ItemStack willStack);

    /**
     * Returns the number of souls that are left in the soul gem. Returns a
     * double because souls can be fractionally drained.
     */
    double getWill(EnumDemonWillType type, ItemStack willGemStack);

    void setWill(EnumDemonWillType type, ItemStack willGemStack, double amount);

    int getMaxWill(EnumDemonWillType type, ItemStack willGemStack);

    double drainWill(EnumDemonWillType type, ItemStack stack, double drainAmount, boolean doDrain);

    double fillWill(EnumDemonWillType type, ItemStack stack, double fillAmount, boolean doFill);
}
