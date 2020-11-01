package wayoftime.bloodmagic.common.data;

import java.nio.file.Path;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.tags.BloodMagicTags;

public class GeneratorItemTags extends ItemTagsProvider
{
	public GeneratorItemTags(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, ExistingFileHelper existingFileHelper)
	{
		super(dataGenerator, blockTagProvider, BloodMagic.MODID, existingFileHelper);
	}

	@Override
	public void registerTags()
	{
		registerFurnaceCells();
		registerReverters();
		registerSieves();
		registerExplosives();
		registerHydration();

		this.getOrCreateBuilder(BloodMagicTags.DUST_IRON).add(BloodMagicItems.IRON_SAND.get());
		this.getOrCreateBuilder(BloodMagicTags.DUST_GOLD).add(BloodMagicItems.GOLD_SAND.get());
		this.getOrCreateBuilder(BloodMagicTags.DUST_COAL).add(BloodMagicItems.COAL_SAND.get());
		this.getOrCreateBuilder(BloodMagicTags.DUST_SULFUR).add(BloodMagicItems.SULFUR.get());
		this.getOrCreateBuilder(BloodMagicTags.DUST_SALTPETER).add(BloodMagicItems.SALTPETER.get());
		this.getOrCreateBuilder(BloodMagicTags.ARC_TOOL).addTag(BloodMagicTags.ARC_TOOL_FURNACE);
		this.getOrCreateBuilder(BloodMagicTags.ARC_TOOL).addTag(BloodMagicTags.ARC_TOOL_REVERTER);
		this.getOrCreateBuilder(BloodMagicTags.ARC_TOOL).addTag(BloodMagicTags.ARC_TOOL_SIEVE);
		this.getOrCreateBuilder(BloodMagicTags.ARC_TOOL).addTag(BloodMagicTags.ARC_TOOL_EXPLOSIVE);
		this.getOrCreateBuilder(BloodMagicTags.ARC_TOOL).addTag(BloodMagicTags.ARC_TOOL_HYDRATE);
//		this.getOrCreateBuilder(GOORESISTANT).addTag(BlockTags.DOORS);
//		this.getOrCreateBuilder(GOORESISTANT).addTag(BlockTags.BEDS);
//		this.getOrCreateBuilder(GOORESISTANT).add(Blocks.PISTON, Blocks.PISTON_HEAD, Blocks.STICKY_PISTON, Blocks.MOVING_PISTON);

	}

	private void registerFurnaceCells()
	{
		this.registerTool(BloodMagicItems.PRIMITIVE_FURNACE_CELL.get(), BloodMagicTags.ARC_TOOL_FURNACE);
		this.registerTool(BloodMagicItems.LAVA_CRYSTAL.get(), BloodMagicTags.ARC_TOOL_FURNACE);
	}

	private void registerReverters()
	{
		this.registerTool(BloodMagicItems.SANGUINE_REVERTER.get(), BloodMagicTags.ARC_TOOL_REVERTER);
	}

	private void registerSieves()
	{
		this.registerTool(BloodMagicItems.AIR_INSCRIPTION_TOOL.get(), BloodMagicTags.ARC_TOOL_SIEVE);
	}

	private void registerExplosives()
	{
		this.registerTool(BloodMagicItems.EXPLOSIVE_POWDER.get(), BloodMagicTags.ARC_TOOL_EXPLOSIVE);
		this.registerTool(BloodMagicItems.PRIMITIVE_EXPLOSIVE_CELL.get(), BloodMagicTags.ARC_TOOL_EXPLOSIVE);
	}

	private void registerHydration()
	{
		this.registerTool(BloodMagicItems.PRIMITIVE_HYDRATION_CELL.get(), BloodMagicTags.ARC_TOOL_HYDRATE);
	}

	public void registerTool(Item item, INamedTag<Item> tag)
	{
		this.getOrCreateBuilder(tag).add(item);
	}

	/**
	 * Resolves a Path for the location to save the given tag.
	 */
	@Override
	protected Path makePath(ResourceLocation id)
	{
		return this.generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/tags/items/" + id.getPath() + ".json");
	}

	/**
	 * Gets a name for this provider, to use in logging.
	 */
	@Override
	public String getName()
	{
		return "Item Tags";
	}
}
