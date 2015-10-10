package com.cricketcraft.chisel.api;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * To be implemented on blocks that "hide" another block inside, so connected textuers can still be
 * accomplished.
 */
public interface IFacade
{
    /**
     * Gets the block this facade is acting as.
     * 
     * @param world {@link World}
     * @param x X coord of your block
     * @param y Y coord of your block
     * @param z Z coord of your block
     * @param side The side being rendered, NOT the side being connected from.
     *            <p>
     *            This value can be -1 if no side is specified. Please handle this appropriately.
     * @return The block inside of your facade block.
     */
    Block getFacade(IBlockAccess world, int x, int y, int z, int side);

    /**
     * Gets the metadata of the block that this facade is acting as.
     * 
     * @param world {@link World}
     * @param x X coord of your block
     * @param y Y coord of your block
     * @param z Z coord of your block
     * @param side The side being rendered, NOT the side being connected from.
     *            <p>
     *            This value can be -1 if no side is specified. Please handle this appropriately.
     * @return The metadata of your facade block.
     */
    int getFacadeMetadata(IBlockAccess world, int x, int y, int z, int side);
}