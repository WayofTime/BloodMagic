package com.cricketcraft.chisel.api;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.cricketcraft.chisel.api.carving.ICarvingVariation;

/**
 * Implement this on items which can be used to chisel blocks.
 */
public interface IChiselItem {

	/**
	 * Checks whether the chisel can have its GUI opened
	 * 
	 * @param world
	 *            {@link World} object
	 * @param player
	 *            The player holding the chisel. It can always be assumed that the player's current item will be this.
	 * @param chisel
	 *            The {@link ItemStack} representing your chisel
	 * @return True if the GUI should open. False otherwise.
	 */
	boolean canOpenGui(World world, EntityPlayer player, ItemStack chisel);

	/**
	 * Called when an item is chiseled using this chisel
	 * 
	 * @param world
	 *            {@link World} object
	 * @param chisel
	 *            The {@link ItemStack} representing the chisel
	 * @param target
	 *            The {@link ICarvingVariation} representing the target item
	 * @return True if the chisel should be damaged. False otherwise.
	 */
	boolean onChisel(World world, ItemStack chisel, ICarvingVariation target);

	/**
	 * Called to check if this {@link ItemStack} can be chiseled in this chisel. If not, there will be no possible variants displayed in the GUI.
	 * <p>
	 * It is not necessary to take into account whether this item <i>has</i> any variants, this method will only be called after that check.
	 * 
	 * @param world
	 *            {@link World} object
	 * @param chisel
	 *            The {@link ItemStack} representing the chisel
	 * @param target
	 *            The {@link ICarvingVariation} representing the target item
	 * @return True if the current target can be chiseled into anything. False otherwise.
	 */
	boolean canChisel(World world, ItemStack chisel, ICarvingVariation target);

	/**
	 * Allows you to control if your item can chisel this block in the world.
	 * 
	 * @param world
	 *            World object
	 * @param player
	 *            {@link EntityPlayer The player} holding the chisel.
	 * @param x
	 *            X coord of the block being chiseled
	 * @param y
	 *            Y coord of the block being chiseled
	 * @param z
	 *            Z coord of the block being chiseled
	 * @param block
	 *            The {@link Block} being chiseled
	 * @param metadata
	 *            The blocks' metadata
	 * @return True if the chiseling should take place. False otherwise.
	 */
	boolean canChiselBlock(World world, EntityPlayer player, int x, int y, int z, Block block, int metadata);

	/**
	 * Allows you to control if your item has chiseling modes.
	 * 
	 * @param chisel
	 *            The {@link ItemStack} representing the chisel.
	 * @return True if the chisel supports modes. False otherwise.
	 */
	boolean hasModes(ItemStack chisel);
}
