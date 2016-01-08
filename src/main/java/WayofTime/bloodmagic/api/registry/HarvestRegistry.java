package WayofTime.bloodmagic.api.registry;

import WayofTime.bloodmagic.api.BlockStack;
import WayofTime.bloodmagic.api.iface.IHarvestHandler;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HarvestRegistry
{
    private static List<IHarvestHandler> handlerList = new ArrayList<IHarvestHandler>();
    private static Map<Block, Integer> validBlocks = new HashMap<Block, Integer>();
    private static Map<BlockStack, Integer> amplifierMap = new HashMap<BlockStack, Integer>();

    /**
     * Registers a handler for the Harvest Ritual to call.
     *
     * @param handler
     *          - The custom handler to register
     */
    public static void registerHandler(IHarvestHandler handler)
    {
        if (!handlerList.contains(handler))
            handlerList.add(handler);
    }

    /**
     * Registers a standard crop (IE: Wheat, Carrots, Potatoes, Netherwart, etc) for
     * the {@link WayofTime.bloodmagic.ritual.harvest.HarvestHandlerPlantable} handler
     * to handle.
     *
     * @param crop
     *          - The crop block to handle.
     * @param matureMeta
     *          - The meta value at which the crop is considered mature
     *          and ready to be harvested.
     */
    public static void registerStandardCrop(Block crop, int matureMeta)
    {
        if (!validBlocks.containsKey(crop))
            validBlocks.put(crop, matureMeta);
    }

    /**
     * Registers a range amplifier for the Harvest Ritual.
     *
     * @param blockStack
     *          - The block for the amplifier.
     * @param range
     *          - The range the amplifier provides.
     */
    public static void registerRangeAmplifier(BlockStack blockStack, int range)
    {
        if (!amplifierMap.containsKey(blockStack))
            amplifierMap.put(blockStack, range);
    }

    public static List<IHarvestHandler> getHandlerList() {
        return handlerList;
    }

    public static Map<Block, Integer> getValidBlocks() {
        return validBlocks;
    }

    public static Map<BlockStack, Integer> getAmplifierMap() {
        return amplifierMap;
    }
}
