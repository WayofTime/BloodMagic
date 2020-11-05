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
		registerOres();
		registerDusts();
		registerFragments();
		registerGravels();

		registerFurnaceCells();
		registerReverters();
		registerSieves();
		registerExplosives();
		registerHydration();
		registerResonators();
		registerCuttingFluids();

		this.getOrCreateBuilder(BloodMagicTags.DUST_SULFUR).add(BloodMagicItems.SULFUR.get());
		this.getOrCreateBuilder(BloodMagicTags.DUST_SALTPETER).add(BloodMagicItems.SALTPETER.get());
		this.getOrCreateBuilder(BloodMagicTags.ARC_TOOL).addTag(BloodMagicTags.ARC_TOOL_FURNACE);
		this.getOrCreateBuilder(BloodMagicTags.ARC_TOOL).addTag(BloodMagicTags.ARC_TOOL_REVERTER);
		this.getOrCreateBuilder(BloodMagicTags.ARC_TOOL).addTag(BloodMagicTags.ARC_TOOL_SIEVE);
		this.getOrCreateBuilder(BloodMagicTags.ARC_TOOL).addTag(BloodMagicTags.ARC_TOOL_EXPLOSIVE);
		this.getOrCreateBuilder(BloodMagicTags.ARC_TOOL).addTag(BloodMagicTags.ARC_TOOL_HYDRATE);
		this.getOrCreateBuilder(BloodMagicTags.ARC_TOOL).addTag(BloodMagicTags.ARC_TOOL_RESONATOR);
		this.getOrCreateBuilder(BloodMagicTags.ARC_TOOL).addTag(BloodMagicTags.ARC_TOOL_CUTTINGFLUID);

		this.getOrCreateBuilder(BloodMagicTags.CRYSTAL_DEMON).add(BloodMagicItems.RAW_CRYSTAL.get());
		this.getOrCreateBuilder(BloodMagicTags.CRYSTAL_DEMON).add(BloodMagicItems.CORROSIVE_CRYSTAL.get());
		this.getOrCreateBuilder(BloodMagicTags.CRYSTAL_DEMON).add(BloodMagicItems.DESTRUCTIVE_CRYSTAL.get());
		this.getOrCreateBuilder(BloodMagicTags.CRYSTAL_DEMON).add(BloodMagicItems.VENGEFUL_CRYSTAL.get());
		this.getOrCreateBuilder(BloodMagicTags.CRYSTAL_DEMON).add(BloodMagicItems.STEADFAST_CRYSTAL.get());
//		this.getOrCreateBuilder(GOORESISTANT).addTag(BlockTags.DOORS);
//		this.getOrCreateBuilder(GOORESISTANT).addTag(BlockTags.BEDS);
//		this.getOrCreateBuilder(GOORESISTANT).add(Blocks.PISTON, Blocks.PISTON_HEAD, Blocks.STICKY_PISTON, Blocks.MOVING_PISTON);

	}

	private void registerOres()
	{
		getOrCreateBuilder(BloodMagicTags.ORE_COPPER);
		getOrCreateBuilder(BloodMagicTags.ORE_LEAD);
		getOrCreateBuilder(BloodMagicTags.ORE_OSMIUM);
		getOrCreateBuilder(BloodMagicTags.ORE_SILVER);
		getOrCreateBuilder(BloodMagicTags.ORE_TIN);
	}

	private void registerDusts()
	{
		getOrCreateBuilder(BloodMagicTags.DUST_IRON).add(BloodMagicItems.IRON_SAND.get());
		getOrCreateBuilder(BloodMagicTags.DUST_GOLD).add(BloodMagicItems.GOLD_SAND.get());
		getOrCreateBuilder(BloodMagicTags.DUST_COAL).add(BloodMagicItems.COAL_SAND.get());
	}

	private void registerFragments()
	{
		getOrCreateBuilder(BloodMagicTags.FRAGMENT_IRON).add(BloodMagicItems.IRON_FRAGMENT.get());
		getOrCreateBuilder(BloodMagicTags.FRAGMENT_GOLD).add(BloodMagicItems.GOLD_FRAGMENT.get());
	}

	private void registerGravels()
	{
		getOrCreateBuilder(BloodMagicTags.GRAVEL_IRON).add(BloodMagicItems.IRON_GRAVEL.get());
		getOrCreateBuilder(BloodMagicTags.GRAVEL_GOLD).add(BloodMagicItems.GOLD_GRAVEL.get());
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

	private void registerResonators()
	{
		this.registerTool(BloodMagicItems.PRIMITIVE_CRYSTALLINE_RESONATOR.get(), BloodMagicTags.ARC_TOOL_RESONATOR);
		this.registerTool(BloodMagicItems.CRYSTALLINE_RESONATOR.get(), BloodMagicTags.ARC_TOOL_RESONATOR);
	}

	private void registerCuttingFluids()
	{
		this.registerTool(BloodMagicItems.BASIC_CUTTING_FLUID.get(), BloodMagicTags.ARC_TOOL_CUTTINGFLUID);
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
