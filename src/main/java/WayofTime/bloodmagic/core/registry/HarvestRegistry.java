package WayofTime.bloodmagic.core.registry;

import WayofTime.bloodmagic.util.BlockStack;
import WayofTime.bloodmagic.iface.IHarvestHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStem;

import java.util.*;

public class HarvestRegistry {
    private static List<IHarvestHandler> handlerList = new ArrayList<>();
    private static Map<Block, Integer> standardCrops = new HashMap<>();
    private static Set<BlockStack> tallCrops = new HashSet<>();
    private static Map<BlockStack, BlockStack> stemCrops = new HashMap<>();
    private static Map<BlockStack, Integer> amplifierMap = new HashMap<>();

    /**
     * Registers a handler for the Harvest Ritual to call.
     *
     * @param handler - The custom handler to register
     */
    public static void registerHandler(IHarvestHandler handler) {
        if (!handlerList.contains(handler))
            handlerList.add(handler);
    }

    /**
     * Registers a standard crop (IE: Wheat, Carrots, Potatoes, Netherwart, etc)
     * for the
     * {@link WayofTime.bloodmagic.ritual.harvest.HarvestHandlerPlantable}
     * handler to handle.
     *
     * @param crop       - The crop block to handle.
     * @param matureMeta - The meta value at which the crop is considered mature and ready
     *                   to be harvested.
     */
    public static void registerStandardCrop(Block crop, int matureMeta) {
        if (!standardCrops.containsKey(crop))
            standardCrops.put(crop, matureMeta);
    }

    /**
     * Registers a tall crop (Sugar Cane and Cactus) for the
     * {@link WayofTime.bloodmagic.ritual.harvest.HarvestHandlerTall} handler to
     * handle.
     *
     * @param crop - The crop block to handle.
     */
    public static void registerTallCrop(BlockStack crop) {
        if (!tallCrops.contains(crop))
            tallCrops.add(crop);
    }

    /**
     * Registers a stem crop (Melon and Pumpkin) for the
     * {@link WayofTime.bloodmagic.ritual.harvest.HarvestHandlerStem} handler to
     * handle.
     * <p>
     * Use {@link net.minecraftforge.oredict.OreDictionary#WILDCARD_VALUE} to
     * accept any meta for the crop block.
     * <p>
     * The Stem must be instanceof {@link BlockStem}
     *
     * @param crop - The crop block to handle.
     * @param stem - The stem of the crop
     */
    public static void registerStemCrop(BlockStack crop, BlockStack stem) {
        if (!stemCrops.containsKey(crop) && stem.getBlock() instanceof BlockStem)
            stemCrops.put(stem, crop);
    }

    /**
     * Registers a range amplifier for the Harvest Ritual.
     *
     * @param blockStack - The block for the amplifier.
     * @param range      - The range the amplifier provides.
     */
    public static void registerRangeAmplifier(BlockStack blockStack, int range) {
        if (!amplifierMap.containsKey(blockStack))
            amplifierMap.put(blockStack, range);
    }

    public static List<IHarvestHandler> getHandlerList() {
        return new ArrayList<>(handlerList);
    }

    public static Map<Block, Integer> getStandardCrops() {
        return new HashMap<>(standardCrops);
    }

    public static Set<BlockStack> getTallCrops() {
        return new HashSet<>(tallCrops);
    }

    public static Map<BlockStack, BlockStack> getStemCrops() {
        return new HashMap<>(stemCrops);
    }

    public static Map<BlockStack, Integer> getAmplifierMap() {
        return new HashMap<>(amplifierMap);
    }
}
