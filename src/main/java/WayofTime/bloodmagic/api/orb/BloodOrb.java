package WayofTime.bloodmagic.api.orb;

import WayofTime.bloodmagic.api.registry.OrbRegistry;

/**
 * Base object for all Blood Orbs. Makes Orb creation quite a bit easier.
 * 
 * Just create a new BloodOrb instance then register it with
 * {@link OrbRegistry#registerOrb(BloodOrb)} This will allow the use of just one
 * item ID for all orbs. If an addon dev needs more control over the intricacies
 * of their orb (custom right clicking, renderers, etc), they can just create
 * their own item as normal.
 */
public class BloodOrb
{
    private String name;
    private int tier;
    private int capacity;
    private String owner = "BloodMagic";

    /**
     * A base object for BloodOrbs. A bit cleaner than the old way through
     * EnergyItems.
     * 
     * @param name
     *        - A name for the Orb. Gets put into an unlocalized name.
     * @param tier
     *        - The tier of the Orb.
     * @param capacity
     *        - The max amount of LP the Orb can store.
     */
    public BloodOrb(String name, int tier, int capacity)
    {
        this.name = name;
        this.tier = tier;
        this.capacity = capacity;
    }

    public String getName()
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

    public String getOwner()
    {
        return owner;
    }

    /**
     * For setting the MODID of the mod that creates the Orb. Not required, but
     * preferred.
     * 
     * @return - The BloodOrb object for further use.
     */
    public BloodOrb setOwner(String owner)
    {
        this.owner = owner;
        return this;
    }

    @Override
    public String toString()
    {
        return "BloodOrb{" + "name='" + name + '\'' + ", tier=" + tier + ", capacity=" + capacity + ", owner=" + owner + '}';
    }
}
