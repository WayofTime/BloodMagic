package WayofTime.alchemicalWizardry.api.altar;

import WayofTime.alchemicalWizardry.api.BlockStack;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

/**
 * Used for building the altar structure.
 */
@Getter
public class AltarComponent {

    private BlockPos offset;
    private BlockStack blockStack;
    private boolean bloodRune;
    private boolean upgradeSlot;

    /**
     * @param offset     - The position in the world relative to the MasterRitualStone
     * @param blockStack - The block and meta combination expected
     */
    public AltarComponent(BlockPos offset, BlockStack blockStack) {
        this.offset = offset;
        this.blockStack = blockStack;
    }

    /**
     * Non-meta based variant for ease of use.
     */
    public AltarComponent(BlockPos offset, Block block) {
        this(offset, new BlockStack(block));
    }

    /**
     * Use for setting a location at which there must be a block, but the type
     * of block does not matter.
     */
    public AltarComponent(BlockPos offset) {
        this(offset, new BlockStack(Blocks.air));
    }

    /**
     * Sets the location to a Blood Rune. This does not mean that the location
     * can be used as an upgrade.
     *
     * @return the current instance for further use.
     */
    public AltarComponent setBloodRune() {
        this.bloodRune = true;
        return this;
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
}
