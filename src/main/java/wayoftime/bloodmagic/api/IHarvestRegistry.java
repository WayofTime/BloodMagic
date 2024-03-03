package wayoftime.bloodmagic.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockState;

import wayoftime.bloodmagic.api.compat.IHarvestHandler;

public interface IHarvestRegistry
{

	/**
	 * Registers a handler for the Harvest Ritual to call.
	 *
	 * @param handler - The custom handler to register
	 */
	public static void registerHandler(IHarvestHandler handler)
    {
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
	}

	/**
	 * Registers a range amplifier for the Harvest Ritual.
	 *
	 * @param block - The block for the amplifier.
	 * @param range - The range the amplifier provides.
	 */
	public static void registerRangeAmplifier(BlockState block, int range)
	{
	}

	public static List<IHarvestHandler> getHarvestHandlers()
	{
        return Lists.newArrayList();
	}

	public static Map<Block, Integer> getStandardCrops()
	{
        return Maps.newHashMap();
	}

	public static Set<BlockState> getTallCrops()
	{
        return Sets.newHashSet();
	}

	public static Multimap<BlockState, BlockState> getStemCrops()
	{
        return ArrayListMultimap.create();
	}

	public static Map<BlockState, Integer> getAmplifiers()
	{
        return Maps.newHashMap();
	}
}
