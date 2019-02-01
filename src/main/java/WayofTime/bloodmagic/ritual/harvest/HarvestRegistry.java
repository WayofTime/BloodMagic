package WayofTime.bloodmagic.ritual.harvest;

import com.google.common.collect.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStem;
import net.minecraft.block.state.IBlockState;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class HarvestRegistry {

    private static final List<IHarvestHandler> HARVEST_HANDLERS = Lists.newArrayList();
    private static final Map<Block, Integer> STANDARD_CROPS = Maps.newHashMap();
    private static final Set<IBlockState> TALL_CROPS = Sets.newHashSet();
    private static final Multimap<IBlockState, IBlockState> STEM_CROPS = ArrayListMultimap.create();
    private static final Map<IBlockState, Integer> AMPLIFIERS = Maps.newHashMap();

    /**
     * Registers a handler for the Harvest Ritual to call.
     *
     * @param handler - The custom handler to register
     */
    public static void registerHandler(IHarvestHandler handler) {
        if (!HARVEST_HANDLERS.contains(handler))
            HARVEST_HANDLERS.add(handler);
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
        if (!STANDARD_CROPS.containsKey(crop))
            STANDARD_CROPS.put(crop, matureMeta);
    }

    /**
     * Registers a tall crop (Sugar Cane and Cactus) for the
     * {@link WayofTime.bloodmagic.ritual.harvest.HarvestHandlerTall} handler to
     * handle.
     *
     * @param crop - The crop block to handle.
     */
    public static void registerTallCrop(IBlockState crop) {
        if (!TALL_CROPS.contains(crop))
            TALL_CROPS.add(crop);
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
    public static void registerStemCrop(IBlockState crop, IBlockState stem) {
        if (!STEM_CROPS.containsKey(crop) && stem.getBlock() instanceof BlockStem)
            STEM_CROPS.put(stem, crop);
    }

    /**
     * Registers a range amplifier for the Harvest Ritual.
     *
     * @param block - The block for the amplifier.
     * @param range - The range the amplifier provides.
     */
    public static void registerRangeAmplifier(IBlockState block, int range) {
        if (!AMPLIFIERS.containsKey(block))
            AMPLIFIERS.put(block, range);
    }

    public static List<IHarvestHandler> getHarvestHandlers() {
        return ImmutableList.copyOf(HARVEST_HANDLERS);
    }

    public static Map<Block, Integer> getStandardCrops() {
        return ImmutableMap.copyOf(STANDARD_CROPS);
    }

    public static Set<IBlockState> getTallCrops() {
        return ImmutableSet.copyOf(TALL_CROPS);
    }

    public static Multimap<IBlockState, IBlockState> getStemCrops() {
        return ImmutableMultimap.copyOf(STEM_CROPS);
    }

    public static Map<IBlockState, Integer> getAmplifiers() {
        return ImmutableMap.copyOf(AMPLIFIERS);
    }
}
