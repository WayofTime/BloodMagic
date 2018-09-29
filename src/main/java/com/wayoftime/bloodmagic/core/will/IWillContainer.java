package com.wayoftime.bloodmagic.core.will;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.List;

public interface IWillContainer {

    double getMaxContained(ItemStack stack);

    @Nullable
    default DemonWillHolder getDemonWill(ItemStack stack) {
        return DemonWillHolder.fromStack(stack);
    }

    default void applyDemonWill(ItemStack stack, DemonWillHolder holder) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null)
            stack.setTagCompound(tag = new NBTTagCompound());

        tag.setTag("demonWill", holder.serializeNBT());
    }

    static void appendTooltip(ItemStack stack, List<String> tooltip) {
        if (!(stack.getItem() instanceof IWillContainer))
            return;

        DemonWillHolder holder = ((IWillContainer) stack.getItem()).getDemonWill(stack);
        if (holder == null)
            return;

        // TODO - Localize
        tooltip.add("Will: " + holder.getType());
        tooltip.add("Amount: " + holder.getAmount());
    }
}
