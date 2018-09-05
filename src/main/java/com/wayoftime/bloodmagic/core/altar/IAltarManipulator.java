package com.wayoftime.bloodmagic.core.altar;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IAltarManipulator {

    default boolean tryManipulate(EntityPlayer player, ItemStack stack, World world, BlockPos pos) {
        return true;
    }
}
