package WayofTime.bloodmagic.soul;

import net.minecraft.item.ItemStack;

public interface ISoulBreathContainer {
    double getBreath(ItemStack stack);

    void setBreath(ItemStack stack, double amount);

    int getMaxBreath(ItemStack stack);

    double drainBreath(ItemStack stack, double drainAmount, boolean doDrain);

    double fillBreath(ItemStack stack, double fillAmount, boolean doFill);
}
