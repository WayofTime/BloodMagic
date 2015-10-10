package com.cricketcraft.chisel.api;

import com.cricketcraft.chisel.api.carving.IChiselMode;

import net.minecraft.item.ItemStack;

/**
 * Implement this on chisel items which require more control over chisel modes
 * (including adding new modes).
 */
public interface IAdvancedChisel {

	/**
	 * Gets the next mode the button in the GUI should switch to given the
	 * current mode.
	 * 
	 * @param stack
	 *            The {@link ItemStack} representing the chisel
	 * @param current
	 *            The {@link IChiselMode} that is currently active
	 * @return The next {@link IChiselMode} in the loop
	 */
	public IChiselMode getNextMode(ItemStack stack, IChiselMode current);

	/**
	 * Gets an {@link IChiselMode} instance from a String name
	 * 
	 * @param chisel
	 *            The {@link ItemStack} representing the chisel
	 * @param name
	 *            The name of the {@link IChiselMode mode}
	 * @return An {@link IChiselMode} that has the name {@code name}
	 */
	public IChiselMode getMode(ItemStack chisel, String name);
}
