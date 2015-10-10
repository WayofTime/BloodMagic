package com.cricketcraft.chisel.api.carving;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Represents a registry of {@link ICarvingGroup}s
 * <p>
 * To obtain chisel's instance of this class, use {@link CarvingUtils#getChiselRegistry()}
 */
public interface ICarvingRegistry {

	/* Getters */

	/**
	 * Finds the group the block/meta pair belongs to in the registry.
	 * 
	 * @param block
	 *            The block of the variation
	 * @param meta
	 *            The metadata of the variation
	 * @return The {@link ICarvingGroup} that the block/meta pair belongs to
	 */
	ICarvingGroup getGroup(Block block, int meta);

	/**
	 * Gets an {@link ICarvingGroup} by its name.
	 * 
	 * @param name
	 *            The name of the group
	 * @return An {@link ICarvingGroup}
	 */
	ICarvingGroup getGroup(String name);

	/**
	 * Gets the {@link ICarvingVariation} instance represented by this block/meta pair.
	 * 
	 * @param block
	 *            The block of the variatoin
	 * @param meta
	 *            The metadata of the variation
	 * @return The {@link ICarvingVariation} containing this block/meta pair
	 */
	ICarvingVariation getVariation(Block block, int meta);

	/**
	 * Gets the list of {@link ICarvingVariation}s from the group that contains this block/meta pair.
	 * 
	 * @param block
	 *            The block of the variation
	 * @param meta
	 *            The metadata of the variation
	 * @return All of the {@link ICarvingVariation}s in the group that contains this block/meta pair
	 */
	List<ICarvingVariation> getGroupVariations(Block block, int meta);

	/**
	 * Gets the oredict name for the group that contains this block/meta pair.
	 * 
	 * @param block
	 *            The block of the variation
	 * @param meta
	 *            The metadata of the variation
	 * @return A string oredict name for the group
	 */
	String getOreName(Block block, int meta);

	/**
	 * Gets the possible output items for this {@link ItemStack}. To be used for machines/GUIs that chisel items.
	 * 
	 * @param chiseled
	 *            The {@link ItemStack} being chiseled
	 * @return A list of stacks that can be chiseled from the passed {@link ItemStack stack}
	 */
	List<ItemStack> getItemsForChiseling(ItemStack chiseled);

	/**
	 * Gets the sound resource string for the group represented by this block/meta pair.
	 * 
	 * @param block
	 *            The block of the variation
	 * @param metadata
	 *            The metadata of the variation
	 * @return The string resource for the sound that can be used in {@link World#playSound(double, double, double, String, float, float, boolean)} and other methods.
	 */
	public String getVariationSound(Block block, int metadata);

	/**
	 * Gets the sound resource string for the group represented by this item/meta pair.
	 * <p>
	 * Mostly a convenience for calling {@link #getVariationSound(Block, int)} with {@link Block#getBlockFromItem(Item)}.
	 * 
	 * @param item
	 *            The item of the variation
	 * @param metadata
	 *            The metadata of the variation
	 * @return The string resource for the sound that can be used in {@link World#playSound(double, double, double, String, float, float, boolean)} and other methods.
	 */
	public String getVariationSound(Item item, int metadata);

	/**
	 * @return A list of all registered group names, sorted alphabetically.
	 */
	List<String> getSortedGroupNames();

	/* Setters */

	/**
	 * Adds a variation to the registry.
	 * 
	 * @param groupName
	 *            The name of the group to add to.
	 * @param block
	 *            The block of the variation
	 * @param metadata
	 *            The metadata of the variation
	 * @param order
	 *            The order of the variation in the list of all variations in the group. Higher numbers are sorted at the end.
	 */
	void addVariation(String groupName, Block block, int metadata, int order);

	/**
	 * Adds a variation to the registry.
	 * 
	 * @param groupName
	 *            The name of the group to add to
	 * @param variation
	 *            The {@link ICarvingVariation} to add
	 */
	void addVariation(String groupName, ICarvingVariation variation);

	/**
	 * Adds a group to the registry.
	 * 
	 * @param group
	 *            The {@link ICarvingGroup} to add.
	 */
	void addGroup(ICarvingGroup group);

	/**
	 * Removes a group from the registry.
	 * <p>
	 * This in effect removes all variations associated with the group, though they are not explicitly removed from the object. If you maintain a reference to the {@link ICarvingGroup} that is
	 * removed, it will still contain its variations.
	 * 
	 * @param groupName
	 *            The name of the group to remove.
	 * @return The {@link ICarvingGroup} that was removed.
	 */
	ICarvingGroup removeGroup(String groupName);

	/**
	 * Removes a varaition with the passed {@link Block} and metadata from the registry. If this variation is registered with mutiple groups, it will remove it from all of them.
	 * 
	 * @param block
	 *            The {@link Block} of the {@link ICarvingVariation variation}
	 * @param metadata
	 *            The metadata of the {@link ICarvingVariation variation}
	 * @return The ICarvingVariation that was removed. Null if nothing was removed.
	 */
	ICarvingVariation removeVariation(Block block, int metadata);

	/**
	 * Removes a varaition with the passed {@link Block} and metadata from the registry, but only from the specified {@link ICarvingGroup} name.
	 * 
	 * @param block
	 *            The {@link Block} of the {@link ICarvingVariation variation}
	 * @param metadata
	 *            The metadata of the {@link ICarvingVariation variation}
	 * @param group
	 *            The name of the group that the variation should be removed from
	 * @return The ICarvingVariation that was removed. Null if nothing was removed.
	 */
	ICarvingVariation removeVariation(Block block, int metadata, String group);

	/**
	 * Registers a group to an oredict name.
	 * <p>
	 * Doing this means that all blocks that are registered to this oredict name will act as if they are a part of this group.
	 * 
	 * @param groupName
	 *            The name of the group
	 * @param oreName
	 *            The oredict name
	 */
	void registerOre(String groupName, String oreName);

	/**
	 * Sets the sound resource for a group.
	 * <p>
	 * This is the sound that is used when a block from this group is chiseled. <br/>
	 * Does <i>not</i> need to be explicitly set.
	 * 
	 * @param name
	 *            The name of the group
	 * @param sound
	 *            The resource string for the sound
	 */
	void setVariationSound(String name, String sound);
}
