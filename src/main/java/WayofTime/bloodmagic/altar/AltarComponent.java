package WayofTime.bloodmagic.altar;

import net.minecraft.util.math.BlockPos;

/**
 * Used for building the altar structure.
 */
public class AltarComponent {
    private final BlockPos offset;
    private final ComponentType component;
    private boolean upgradeSlot;

    /**
     * Sets a component location for the altar.
     *
     * @param offset    - Where the block should be in relation to the Altar
     * @param component - The type of Component the location should contain
     */
    public AltarComponent(BlockPos offset, ComponentType component) {
        this.offset = offset;
        this.component = component;
    }

    /**
     * Use for setting a location at which there must be a block, but the type
     * of block does not matter.
     *
     * @param offset - Where the block should be in relation to the Altar
     */
    public AltarComponent(BlockPos offset) {
        this(offset, ComponentType.NOTAIR);
    }

    /**
     * Sets the location to an upgrade slot.
     *
     * @return the current instance for further use.
     */
    public AltarComponent setUpgradeSlot() {
        this.upgradeSlot = true;
        return this;
    }

    public BlockPos getOffset() {
        return offset;
    }

    public boolean isUpgradeSlot() {
        return upgradeSlot;
    }

    public ComponentType getComponent() {
        return component;
    }
}
