package wayoftime.bloodmagic.common.data;

import java.nio.file.Path;

import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.tags.Tag.Named;
import net.minecraft.resources.ResourceLocation;
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
	public void addTags()
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

		this.tag(BloodMagicTags.DUST_SULFUR).add(BloodMagicItems.SULFUR.get());
		this.tag(BloodMagicTags.DUST_SALTPETER).add(BloodMagicItems.SALTPETER.get());
		this.tag(BloodMagicTags.ARC_TOOL).addTag(BloodMagicTags.ARC_TOOL_FURNACE);
		this.tag(BloodMagicTags.ARC_TOOL).addTag(BloodMagicTags.ARC_TOOL_REVERTER);
		this.tag(BloodMagicTags.ARC_TOOL).addTag(BloodMagicTags.ARC_TOOL_SIEVE);
		this.tag(BloodMagicTags.ARC_TOOL).addTag(BloodMagicTags.ARC_TOOL_EXPLOSIVE);
		this.tag(BloodMagicTags.ARC_TOOL).addTag(BloodMagicTags.ARC_TOOL_HYDRATE);
		this.tag(BloodMagicTags.ARC_TOOL).addTag(BloodMagicTags.ARC_TOOL_RESONATOR);
		this.tag(BloodMagicTags.ARC_TOOL).addTag(BloodMagicTags.ARC_TOOL_CUTTINGFLUID);

		this.tag(BloodMagicTags.CRYSTAL_DEMON).add(BloodMagicItems.RAW_CRYSTAL.get());
		this.tag(BloodMagicTags.CRYSTAL_DEMON).add(BloodMagicItems.CORROSIVE_CRYSTAL.get());
		this.tag(BloodMagicTags.CRYSTAL_DEMON).add(BloodMagicItems.DESTRUCTIVE_CRYSTAL.get());
		this.tag(BloodMagicTags.CRYSTAL_DEMON).add(BloodMagicItems.VENGEFUL_CRYSTAL.get());
		this.tag(BloodMagicTags.CRYSTAL_DEMON).add(BloodMagicItems.STEADFAST_CRYSTAL.get());

		this.copy(BloodMagicTags.Blocks.MUSHROOM_STEM, BloodMagicTags.MUSHROOM_STEM);
		this.copy(BloodMagicTags.Blocks.MUSHROOM_HYPHAE, BloodMagicTags.MUSHROOM_HYPHAE);

//		this.getOrCreateBuilder(GOORESISTANT).addTag(BlockTags.DOORS);
//		this.getOrCreateBuilder(GOORESISTANT).addTag(BlockTags.BEDS);
//		this.getOrCreateBuilder(GOORESISTANT).add(Blocks.PISTON, Blocks.PISTON_HEAD, Blocks.STICKY_PISTON, Blocks.MOVING_PISTON);

	}

	private void registerIngots()
	{
		tag(BloodMagicTags.INGOT_HELLFORGED).add(BloodMagicItems.HELLFORGED_INGOT.get());
	}

	private void registerOres()
	{
		tag(BloodMagicTags.ORE_COPPER);
		tag(BloodMagicTags.ORE_LEAD);
		tag(BloodMagicTags.ORE_OSMIUM);
		tag(BloodMagicTags.ORE_SILVER);
		tag(BloodMagicTags.ORE_TIN);
	}

	private void registerDusts()
	{
		tag(BloodMagicTags.DUST_IRON).add(BloodMagicItems.IRON_SAND.get());
		tag(BloodMagicTags.DUST_GOLD).add(BloodMagicItems.GOLD_SAND.get());
		tag(BloodMagicTags.DUST_COAL).add(BloodMagicItems.COAL_SAND.get());
		tag(BloodMagicTags.DUST_NETHERITE_SCRAP).add(BloodMagicItems.NETHERITE_SCRAP_SAND.get());
		tag(BloodMagicTags.DUST_HELLFORGED).add(BloodMagicItems.HELLFORGED_SAND.get());
		tag(BloodMagicTags.DUST_CORRUPTED).add(BloodMagicItems.CORRUPTED_DUST.get());
		tag(BloodMagicTags.TINYDUST_CORRUPTED).add(BloodMagicItems.CORRUPTED_DUST_TINY.get());
	}

	private void registerFragments()
	{
		tag(BloodMagicTags.FRAGMENT_IRON).add(BloodMagicItems.IRON_FRAGMENT.get());
		tag(BloodMagicTags.FRAGMENT_GOLD).add(BloodMagicItems.GOLD_FRAGMENT.get());
		tag(BloodMagicTags.FRAGMENT_NETHERITE_SCRAP).add(BloodMagicItems.NETHERITE_SCRAP_FRAGMENT.get());
	}

	private void registerGravels()
	{
		tag(BloodMagicTags.GRAVEL_IRON).add(BloodMagicItems.IRON_GRAVEL.get());
		tag(BloodMagicTags.GRAVEL_GOLD).add(BloodMagicItems.GOLD_GRAVEL.get());
		tag(BloodMagicTags.GRAVEL_NETHERITE_SCRAP).add(BloodMagicItems.NETHERITE_SCRAP_GRAVEL.get());
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

	public void registerTool(Item item, Named<Item> tag)
	{
		this.tag(tag).add(item);
	}

	private void registerVanillaTools()
	{
		tag(BloodMagicTags.SWORDS).add(Items.DIAMOND_SWORD, Items.GOLDEN_SWORD, Items.IRON_SWORD, Items.NETHERITE_SWORD, Items.STONE_SWORD, Items.WOODEN_SWORD, BloodMagicItems.SENTIENT_SWORD.get());
		tag(BloodMagicTags.AXES).add(Items.DIAMOND_AXE, Items.GOLDEN_AXE, Items.IRON_AXE, Items.NETHERITE_AXE, Items.STONE_AXE, Items.WOODEN_AXE, BloodMagicItems.SENTIENT_AXE.get());
		tag(BloodMagicTags.SHOVELS).add(Items.DIAMOND_SHOVEL, Items.GOLDEN_SHOVEL, Items.IRON_SHOVEL, Items.NETHERITE_SHOVEL, Items.STONE_SHOVEL, Items.WOODEN_SHOVEL, BloodMagicItems.SENTIENT_SHOVEL.get());
		tag(BloodMagicTags.PICKAXES).add(Items.DIAMOND_PICKAXE, Items.GOLDEN_PICKAXE, Items.IRON_PICKAXE, Items.NETHERITE_PICKAXE, Items.STONE_PICKAXE, Items.WOODEN_PICKAXE, BloodMagicItems.SENTIENT_PICKAXE.get());
		tag(BloodMagicTags.HOES).add(Items.DIAMOND_HOE, Items.GOLDEN_HOE, Items.IRON_HOE, Items.NETHERITE_HOE, Items.STONE_HOE, Items.WOODEN_HOE, BloodMagicItems.SENTIENT_SCYTHE.get());
	}

	/**
	 * Resolves a Path for the location to save the given tag.
	 */
	@Override
	protected Path getPath(ResourceLocation id)
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
