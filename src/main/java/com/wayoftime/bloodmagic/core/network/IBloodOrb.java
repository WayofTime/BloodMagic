package com.wayoftime.bloodmagic.core.network;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IBloodOrb {

    @Nullable
    BloodOrb getOrb(@Nonnull ItemStack stack);
}
