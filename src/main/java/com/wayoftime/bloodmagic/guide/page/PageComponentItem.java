package com.wayoftime.bloodmagic.guide.page;

import net.minecraft.item.ItemStack;

public class PageComponentItem extends PageComponent {

    private final ItemStack item;

    public PageComponentItem(ItemStack item) {
        super(18);

        this.item = item;
    }
}
