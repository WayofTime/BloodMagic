package com.wayoftime.bloodmagic.guide.page;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import java.util.function.Supplier;

public class PageComponentSmelting extends PageComponent {

    private final ItemStack output;
    private final Supplier<ItemStack> inputGetter;
    private ItemStack input;

    public PageComponentSmelting(ItemStack output) {
        super(20);

        this.output = output;
        this.inputGetter = () -> FurnaceRecipes.instance().getSmeltingResult(output);
    }
}
