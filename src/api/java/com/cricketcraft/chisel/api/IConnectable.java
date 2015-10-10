package com.cricketcraft.chisel.api;

import net.minecraft.world.IBlockAccess;

/**
 * This extension of {@link IFacade} allows the block to say whether or not OTHER CTM blocks can connect to IT.
 */
public interface IConnectable {

	/**
	 * Determines whether other CTM blocks can connect to this one.
	 * 
	 * @param world
	 * @param x
	 *            The X position of the block that is connecting to this one. NOT the position of your block.
	 * @param y
	 *            The Y position of the block that is connecting to this one. NOT the position of your block.
	 * @param z
	 *            The Z position of the block that is connecting to this one. NOT the position of your block.
	 * @param side
	 *            The side being drawn.
	 * @return True if a block can connect to this one from the given direction. False otherwise.
	 */
	boolean canConnectCTM(IBlockAccess world, int x, int y, int z, int side);

}
