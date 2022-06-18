package wayoftime.bloodmagic.common.data;

import java.nio.file.Path;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
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
		registerIngots();
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

		registerVanillaTools();
		registerModdedItems();

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

		this.copy(BloodMagicTags.Blocks.MUSHROOM_STEM, BloodMagicTags.MUSHROOM_STEM);
		this.copy(BloodMagicTags.Blocks.MUSHROOM_HYPHAE, BloodMagicTags.MUSHROOM_HYPHAE);
		//this.copy(BloodMagicTags.Blocks.ANDESITE, BloodMagicTags.ANDESITE);
		//this.copy(BloodMagicTags.Blocks.SOUL_SAND, BloodMagicTags.SOUL_SAND);
		//this.copy(BloodMagicTags.Blocks.SOUL_SOIL, BloodMagicTags.SOUL_SOIL);

//		this.getOrCreateBuilder(GOORESISTANT).addTag(BlockTags.DOORS);
//		this.getOrCreateBuilder(GOORESISTANT).addTag(BlockTags.BEDS);
//		this.getOrCreateBuilder(GOORESISTANT).add(Blocks.PISTON, Blocks.PISTON_HEAD, Blocks.STICKY_PISTON, Blocks.MOVING_PISTON);

	}

	private void registerIngots()
	{
		getOrCreateBuilder(BloodMagicTags.INGOT_HELLFORGED).add(BloodMagicItems.HELLFORGED_INGOT.get());
	}

	private void registerOres()
	{
		getOrCreateBuilder(BloodMagicTags.ORE_ALUMINUM);
		getOrCreateBuilder(BloodMagicTags.ORE_APATITE);
		getOrCreateBuilder(BloodMagicTags.ORE_CERTUS_QUARTZ);
		getOrCreateBuilder(BloodMagicTags.ORE_CINNABAR);
		getOrCreateBuilder(BloodMagicTags.ORE_COPPER);
		getOrCreateBuilder(BloodMagicTags.ORE_FLUORITE);
		getOrCreateBuilder(BloodMagicTags.ORE_INFERIUM);
		getOrCreateBuilder(BloodMagicTags.ORE_LEAD);
		getOrCreateBuilder(BloodMagicTags.ORE_NICKEL);
		getOrCreateBuilder(BloodMagicTags.ORE_NITER);
		getOrCreateBuilder(BloodMagicTags.ORE_OSMIUM);
		getOrCreateBuilder(BloodMagicTags.ORE_PROSPERITY);
		getOrCreateBuilder(BloodMagicTags.ORE_RUBY);
		getOrCreateBuilder(BloodMagicTags.ORE_SAPPHIRE);
		getOrCreateBuilder(BloodMagicTags.ORE_SILVER);
		getOrCreateBuilder(BloodMagicTags.ORE_SOULIUM);
		getOrCreateBuilder(BloodMagicTags.ORE_SULFUR);
		getOrCreateBuilder(BloodMagicTags.ORE_TIN);
		getOrCreateBuilder(BloodMagicTags.ORE_URANIUM);
		getOrCreateBuilder(BloodMagicTags.ORE_ZINC);
	}

	private void registerDusts()
	{
		getOrCreateBuilder(BloodMagicTags.DUST_IRON).add(BloodMagicItems.IRON_SAND.get());
		getOrCreateBuilder(BloodMagicTags.DUST_GOLD).add(BloodMagicItems.GOLD_SAND.get());
		getOrCreateBuilder(BloodMagicTags.DUST_COAL).add(BloodMagicItems.COAL_SAND.get());
		getOrCreateBuilder(BloodMagicTags.DUST_NETHERITE_SCRAP).add(BloodMagicItems.NETHERITE_SCRAP_SAND.get());
		getOrCreateBuilder(BloodMagicTags.DUST_HELLFORGED).add(BloodMagicItems.HELLFORGED_SAND.get());
		getOrCreateBuilder(BloodMagicTags.DUST_CORRUPTED).add(BloodMagicItems.CORRUPTED_DUST.get());
		getOrCreateBuilder(BloodMagicTags.TINYDUST_CORRUPTED).add(BloodMagicItems.CORRUPTED_DUST_TINY.get());
	}

	private void registerFragments()
	{
		getOrCreateBuilder(BloodMagicTags.FRAGMENT_IRON).add(BloodMagicItems.IRON_FRAGMENT.get());
		getOrCreateBuilder(BloodMagicTags.FRAGMENT_GOLD).add(BloodMagicItems.GOLD_FRAGMENT.get());
		getOrCreateBuilder(BloodMagicTags.FRAGMENT_NETHERITE_SCRAP).add(BloodMagicItems.NETHERITE_SCRAP_FRAGMENT.get());
	}

	private void registerGravels()
	{
		getOrCreateBuilder(BloodMagicTags.GRAVEL_IRON).add(BloodMagicItems.IRON_GRAVEL.get());
		getOrCreateBuilder(BloodMagicTags.GRAVEL_GOLD).add(BloodMagicItems.GOLD_GRAVEL.get());
		getOrCreateBuilder(BloodMagicTags.GRAVEL_NETHERITE_SCRAP).add(BloodMagicItems.NETHERITE_SCRAP_GRAVEL.get());
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
		this.registerTool(BloodMagicItems.INTERMEDIATE_CUTTING_FLUID.get(), BloodMagicTags.ARC_TOOL_CUTTINGFLUID);
	}

	public void registerTool(Item item, INamedTag<Item> tag)
	{
		this.getOrCreateBuilder(tag).add(item);
	}

	private void registerVanillaTools()
	{
		getOrCreateBuilder(BloodMagicTags.SWORDS).add(Items.DIAMOND_SWORD, Items.GOLDEN_SWORD, Items.IRON_SWORD, Items.NETHERITE_SWORD, Items.STONE_SWORD, Items.WOODEN_SWORD, BloodMagicItems.SENTIENT_SWORD.get());
		getOrCreateBuilder(BloodMagicTags.AXES).add(Items.DIAMOND_AXE, Items.GOLDEN_AXE, Items.IRON_AXE, Items.NETHERITE_AXE, Items.STONE_AXE, Items.WOODEN_AXE, BloodMagicItems.SENTIENT_AXE.get());
		getOrCreateBuilder(BloodMagicTags.SHOVELS).add(Items.DIAMOND_SHOVEL, Items.GOLDEN_SHOVEL, Items.IRON_SHOVEL, Items.NETHERITE_SHOVEL, Items.STONE_SHOVEL, Items.WOODEN_SHOVEL, BloodMagicItems.SENTIENT_SHOVEL.get());
		getOrCreateBuilder(BloodMagicTags.PICKAXES).add(Items.DIAMOND_PICKAXE, Items.GOLDEN_PICKAXE, Items.IRON_PICKAXE, Items.NETHERITE_PICKAXE, Items.STONE_PICKAXE, Items.WOODEN_PICKAXE, BloodMagicItems.SENTIENT_PICKAXE.get());
		getOrCreateBuilder(BloodMagicTags.HOES).add(Items.DIAMOND_HOE, Items.GOLDEN_HOE, Items.IRON_HOE, Items.NETHERITE_HOE, Items.STONE_HOE, Items.WOODEN_HOE, BloodMagicItems.SENTIENT_SCYTHE.get());
	}

	//Modded (for meteor rituals)
	private void registerModdedItems() {
		getOrCreateBuilder(BloodMagicTags.ADVANCED_ALLOY);
		getOrCreateBuilder(BloodMagicTags.ANDESITE_ALLOY);
		getOrCreateBuilder(BloodMagicTags.DRAGON_BONE);
		getOrCreateBuilder(BloodMagicTags.GEM_CERTUS_QUARTZ);
		getOrCreateBuilder(BloodMagicTags.PROSPERITY_SHARD);
		getOrCreateBuilder(BloodMagicTags.RF_COIL);
		getOrCreateBuilder(BloodMagicTags.WIRECOIL_COPPER);
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
