package com.wayoftime.bloodmagic.core.altar;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Allows items to interact with the altar via right click without being inserted.
 */
public interface IAltarManipulator {

    /**
     * Allows an item to decide whether or not it should be inserted into the altar.
     *
     * @param player The player interacting with the altar
     * @param stack The ItemStack being tested
     * @param world The World the altar is in
     * @param pos The position in the world of the altar
     * @return false if the item should be inserted, true if not.
     */
    default boolean tryManipulate(EntityPlayer player, ItemStack stack, World world, BlockPos pos) {
        return true;
    }
}
