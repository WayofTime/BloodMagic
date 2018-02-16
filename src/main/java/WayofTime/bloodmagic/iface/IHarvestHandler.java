package WayofTime.bloodmagic.iface;

import WayofTime.bloodmagic.util.BlockStack;
import WayofTime.bloodmagic.ritual.data.IMasterRitualStone;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Used to define a HarvestHandler for the Harvest Ritual.
 */
public interface IHarvestHandler {
    /**
     * Called whenever the Harvest Ritual attempts to harvest a block. <br>
     * Use this to break the block, plant a new one, and drop the produced
     * items. <br>
     * Make sure to do checks so you are certain the blocks being handled are
     * the block types you want.
     *
     * @param world      - The world the
     *                   {@link IMasterRitualStone} is in.
     * @param pos        - The position of the Block being checked
     * @param blockStack - The Block being checked
     * @return If the block was successfully harvested.
     */
    boolean harvestAndPlant(World world, BlockPos pos, BlockStack blockStack);
}
