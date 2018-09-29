package com.wayoftime.bloodmagic.core.util;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class InventoryUtil {

    public static final Predicate<ItemStack> NO_EMPTY = stack -> !stack.isEmpty();

    @Nonnull
    public static List<ItemStack> findMatchingItems(IItemHandler inventory, Predicate<ItemStack> filter) {
        List<ItemStack> found = null;
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (filter.test(stack)) {
                if (found == null)
                    found = Lists.newArrayList();

                found.add(stack);
            }
        }

        return found == null ? Collections.emptyList() : found;
    }
}
