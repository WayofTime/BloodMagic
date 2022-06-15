package wayoftime.bloodmagic.ritual.harvest;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockState;

public class HarvestRegistry
{

	private static final List<IHarvestHandler> HARVEST_HANDLERS = Lists.newArrayList();
	private static final Map<Block, Integer> STANDARD_CROPS = Maps.newHashMap();
	private static final Set<BlockState> TALL_CROPS = Sets.newHashSet();
	private static final Multimap<BlockState, BlockState> STEM_CROPS = ArrayListMultimap.create();
	private static final Map<BlockState, Integer> AMPLIFIERS = Maps.newHashMap();

	/**
	 * Registers a handler for the Harvest Ritual to call.
	 *
	 * @param handler - The custom handler to register
	 */
	public static void registerHandler(IHarvestHandler handler)
	{
		if (!HARVEST_HANDLERS.contains(handler))
			HARVEST_HANDLERS.add(handler);
	}

	/**
	 * Registers a standard crop (IE: Wheat, Carrots, Potatoes, Netherwart, etc) for
	 * the {@link WayofTime.bloodmagic.ritual.harvest.HarvestHandlerPlantable}
	 * handler to handle.
	 *
	 * @param crop       - The crop block to handle.
	 * @param matureMeta - The meta value at which the crop is considered mature and
	 *                   ready to be harvested.
	 */
	public static void registerStandardCrop(Block crop, int matureMeta)
	{
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
	public static void registerTallCrop(BlockState crop)
	{
		if (!TALL_CROPS.contains(crop))
			TALL_CROPS.add(crop);
	}

	/**
	 * Registers a stem crop (Melon and Pumpkin) for the
	 * {@link WayofTime.bloodmagic.ritual.harvest.HarvestHandlerStem} handler to
	 * handle.
	 * <p>
	 * Use {@link net.minecraftforge.oredict.OreDictionary#WILDCARD_VALUE} to accept
	 * any meta for the crop block.
	 * <p>
	 * The Stem must be instanceof {@link StemBlock}
	 *
	 * @param crop - The crop block to handle.
	 * @param stem - The stem of the crop
	 */
	public static void registerStemCrop(BlockState crop, BlockState stem)
	{
		if (!STEM_CROPS.containsKey(crop) && stem.getBlock() instanceof AttachedStemBlock)
			STEM_CROPS.put(stem, crop);
	}

	/**
	 * Registers a range amplifier for the Harvest Ritual.
	 *
	 * @param block - The block for the amplifier.
	 * @param range - The range the amplifier provides.
	 */
	public static void registerRangeAmplifier(BlockState block, int range)
	{
		if (!AMPLIFIERS.containsKey(block))
			AMPLIFIERS.put(block, range);
	}

	public static List<IHarvestHandler> getHarvestHandlers()
	{
		return ImmutableList.copyOf(HARVEST_HANDLERS);
	}

	public static Map<Block, Integer> getStandardCrops()
	{
		return ImmutableMap.copyOf(STANDARD_CROPS);
	}

	public static Set<BlockState> getTallCrops()
	{
		return ImmutableSet.copyOf(TALL_CROPS);
	}

	public static Multimap<BlockState, BlockState> getStemCrops()
	{
		return ImmutableMultimap.copyOf(STEM_CROPS);
	}

	public static Map<BlockState, Integer> getAmplifiers()
	{
		return ImmutableMap.copyOf(AMPLIFIERS);
	}
}
