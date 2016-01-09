package WayofTime.bloodmagic.api.registry;

import WayofTime.bloodmagic.api.BlockStack;
import WayofTime.bloodmagic.api.iface.IHarvestHandler;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStem;

import java.util.*;

public class HarvestRegistry
{
    @Getter
    private static List<IHarvestHandler> handlerList = new ArrayList<IHarvestHandler>();
    @Getter
    private static Map<Block, Integer> standardCrops = new HashMap<Block, Integer>();
    @Getter
    private static Set<BlockStack> tallCrops = new HashSet<BlockStack>();
    @Getter
    private static Map<BlockStack, BlockStack> stemCrops = new HashMap<BlockStack, BlockStack>();
    @Getter
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
        if (!standardCrops.containsKey(crop))
            standardCrops.put(crop, matureMeta);
    }

    /**
     * Registers a tall crop (Sugar Cane and Cactus) for the
     * {@link WayofTime.bloodmagic.ritual.harvest.HarvestHandlerTall} handler
     * to handle.
     *
     * @param crop
     *          - The crop block to handle.
     */
    public static void registerTallCrop(BlockStack crop)
    {
        if (!tallCrops.contains(crop))
            tallCrops.add(crop);
    }

    /**
     * Registers a stem crop (Melon and Pumpkin) for the
     * {@link WayofTime.bloodmagic.ritual.harvest.HarvestHandlerStem} handler
     * to handle.
     *
     * Use {@link net.minecraftforge.oredict.OreDictionary#WILDCARD_VALUE} to accept
     * any meta for the crop block.
     *
     * The Stem must be instanceof {@link BlockStem}
     *
     * @param crop
     *          - The crop block to handle.
     * @param stem
     *          - The stem of the crop
     */
    public static void registerStemCrop(BlockStack crop, BlockStack stem)
    {
        if (!stemCrops.containsKey(crop) && stem.getBlock() instanceof BlockStem)
            stemCrops.put(stem, crop);
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
}
