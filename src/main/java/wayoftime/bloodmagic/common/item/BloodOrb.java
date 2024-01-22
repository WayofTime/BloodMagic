package wayoftime.bloodmagic.common.item;

import net.minecraft.resources.ResourceLocation;

/**
 * Base Blood Orb class object for blood orbs
 */
public final class BloodOrb
{
	private final ResourceLocation name;
	private final int tier;
	private final int capacity;
	private final int fillRate;

	/**
	 * A base object for BloodOrbs. A bit cleaner than the old way through
	 * EnergyItems.
	 *
	 * @param name     - A name for the Orb. Gets put into an unlocalized name.
	 * @param tier     - The tier of the Orb.
	 * @param capacity - The max amount of LP the Orb can store.
	 * @param fillRate - The amount of LP per tick the Altar can fill the network
	 *                 with.
	 */
	public BloodOrb(ResourceLocation name, int tier, int capacity, int fillRate)
	{
		this.name = name;
		this.tier = tier;
		this.capacity = capacity;
		this.fillRate = fillRate;
	}

	public ResourceLocation getResourceLocation()
	{
		return name;
	}

	public int getTier()
	{
		return tier;
	}

	public int getCapacity()
	{
		return capacity;
	}

	public int getFillRate()
	{
		return fillRate;
	}

	@Override
	public String toString()
	{
//		return "BloodOrb{" + "name='" + name + '\'' + ", tier=" + tier + ", capacity=" + capacity + ", owner="
//				+ getRegistryName() + '}';
		return "BloodOrb{" + "name='" + name + '\'' + ", tier=" + tier + ", capacity=" + capacity + '}';
	}
}