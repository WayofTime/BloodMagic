package com.cricketcraft.chisel.api.carving;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import com.google.common.collect.Lists;

public class CarvingUtils {

	/**
	 * A simple way to compare two {@link ICarvingVariation} objects based on the {@link ICarvingVariation#getOrder() getOrder()} method.
	 * 
	 * @param v1
	 *            The first {@link ICarvingVariation variation}.
	 * @param v2
	 *            The second {@link ICarvingVariation variation}.
	 * @return A positive integer if the first's order is greater, a negative integer if the second's is greater, and 0 if they are equal.
	 */
	public static int compare(ICarvingVariation v1, ICarvingVariation v2) {
		return v1.getOrder() - v2.getOrder();
	}

	/**
	 * Gets an {@link ItemStack} representing the passed {@link ICarvingVariation}.
	 * 
	 * @param variation
	 *            An {@link ICarvingVariation}
	 * @return An {@link ItemStack}
	 */
	public static ItemStack getStack(ICarvingVariation variation) {
		return new ItemStack(variation.getBlock(), 1, variation.getItemMeta());
	}

	public static ICarvingRegistry chisel;

	/**
	 * @return The instance of the chisel carving registry from the chisel mod.
	 *         <p>
	 *         If chisel is not installed this will return null.
	 */
	public static ICarvingRegistry getChiselRegistry() {
		return chisel;
	}

	/**
	 * Creates a standard {@link ICarvingVariation} for the given data. Use this if you do not need any custom behavior in your own variation.
	 * 
	 * @param block
	 *            The block of the variation
	 * @param metadata
	 *            The metadata of both the block and item
	 * @param order
	 *            The sorting order.
	 * @return A standard {@link ICarvingVariation} instance.
	 */
	public static ICarvingVariation getDefaultVariationFor(Block block, int metadata, int order) {
		return new SimpleCarvingVariation(block, metadata, order);
	}

	/**
	 * Creates a standard {@link ICarvingGroup} for the given name. Use this if you do not need any custom behavior in your own group.
	 * 
	 * @param name
	 *            The name of the group.
	 * @return A standard {@link ICarvingGroup} instance.
	 */
	public static ICarvingGroup getDefaultGroupFor(String name) {
		return new SimpleCarvingGroup(name);
	}

	public static class SimpleCarvingVariation implements ICarvingVariation {

		private int order;
		private Block block;
		private int meta;
		private int damage;

		public SimpleCarvingVariation(Block block, int metadata, int order) {
			this.order = order;
			this.block = block;
			this.meta = metadata;
			this.damage = metadata;
		}

		@Override
		public Block getBlock() {
			return block;
		}

		@Override
		public int getBlockMeta() {
			return meta;
		}

		@Override
		public int getItemMeta() {
			return damage;
		}

		@Override
		public int getOrder() {
			return order;
		}
	}

	public static class SimpleCarvingGroup implements ICarvingGroup {

		private String name;
		private String sound;
		private String oreName;

		private List<ICarvingVariation> variations = Lists.newArrayList();

		public SimpleCarvingGroup(String name) {
			this.name = name;
		}

		public List<ICarvingVariation> getVariations() {
			return Lists.newArrayList(variations);
		}

		@Override
		public void addVariation(ICarvingVariation variation) {
			variations.add(variation);
			Collections.sort(variations, new Comparator<ICarvingVariation>() {

				@Override
				public int compare(ICarvingVariation o1, ICarvingVariation o2) {
					return CarvingUtils.compare(o1, o2);
				}
			});
		}

		@Override
		public boolean removeVariation(ICarvingVariation variation) {
			ICarvingVariation toRemove = null;
			for (ICarvingVariation v : variations) {
				if (v.getBlock() == variation.getBlock() && v.getBlockMeta() == variation.getBlockMeta()) {
					toRemove = v;
				}
			}
			return toRemove == null ? false : variations.remove(toRemove);
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String getSound() {
			return sound;
		}

		@Override
		public void setSound(String sound) {
			this.sound = sound;
		}

		@Override
		public String getOreName() {
			return oreName;
		}

		@Override
		public void setOreName(String oreName) {
			this.oreName = oreName;
		}
	}

}
