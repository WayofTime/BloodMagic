package WayofTime.bloodmagic.apibutnotreally.orb;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Base object for all Blood Orbs. Makes Orb creation quite a bit easier.
 * <p>
 * Just create a new BloodOrb instance then register it in {@link net.minecraftforge.event.RegistryEvent.Register<BloodOrb>}
 */
public class BloodOrb extends IForgeRegistryEntry.Impl<BloodOrb> {
    private final String name;
    private final int tier;
    private final int capacity;
    @Nullable
    private ModelResourceLocation modelLocation;

    /**
     * A base object for BloodOrbs. A bit cleaner than the old way through
     * EnergyItems.
     *
     * @param name     - A name for the Orb. Gets put into an unlocalized name.
     * @param tier     - The tier of the Orb.
     * @param capacity - The max amount of LP the Orb can store.
     */
    public BloodOrb(String name, int tier, int capacity) {
        this.name = name;
        this.tier = tier;
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public int getTier() {
        return tier;
    }

    public int getCapacity() {
        return capacity;
    }

    @Nullable
    public ModelResourceLocation getModelLocation() {
        return modelLocation;
    }

    public BloodOrb withModel(@Nonnull ModelResourceLocation modelLocation) {
        this.modelLocation = modelLocation;
        return this;
    }

    @Override
    public String toString() {
        return "BloodOrb{" + "name='" + name + '\'' + ", tier=" + tier + ", capacity=" + capacity + ", owner=" + getRegistryName() + '}';
    }
}
