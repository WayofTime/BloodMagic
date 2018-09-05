package com.wayoftime.bloodmagic.core.util.register;

import net.minecraft.item.Item;

import javax.annotation.Nullable;

public interface IItemProvider {

    @Nullable
    default Item getItem() {
        return null;
    }
}
