package wayoftime.bloodmagic.common.block;

import net.minecraft.block.*;
import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.block.enums.BloodRuneType;
import wayoftime.bloodmagic.common.block.base.BlockPillarCap;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.item.inventory.ContainerFilter;
import wayoftime.bloodmagic.common.item.inventory.ContainerHolding;
import wayoftime.bloodmagic.common.item.inventory.ContainerTrainingBracelet;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.tile.container.ContainerAlchemicalReactionChamber;
import wayoftime.bloodmagic.tile.container.ContainerAlchemyTable;
import wayoftime.bloodmagic.tile.container.ContainerItemRoutingNode;
import wayoftime.bloodmagic.tile.container.ContainerSoulForge;

public class BloodMagicBlocks
{
	public static final ResourceLocation FLUID_STILL = new ResourceLocation("bloodmagic:block/lifeessencestill");
	public static final ResourceLocation FLUID_FLOWING = new ResourceLocation("bloodmagic:block/lifeessenceflowing");

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BloodMagic.MODID);
	public static final DeferredRegister<Block> BASICBLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BloodMagic.MODID);
	public static final DeferredRegister<Block> DUNGEONBLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BloodMagic.MODID);
	public static final DeferredRegister<Item> ITEMS = BloodMagicItems.ITEMS;
	public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, BloodMagic.MODID);
	public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, BloodMagic.MODID);

//	public static final RegistryObject<Block> BLOODSTONE = BASICBLOCKS.register("ruby_block", BloodstoneBlock::new);
	public static final RegistryObject<Block> SOUL_FORGE = BLOCKS.register("soulforge", BlockSoulForge::new);
	public static final RegistryObject<Block> INCENSE_ALTAR = BLOCKS.register("incensealtar", BlockIncenseAltar::new);
	public static final RegistryObject<Block> ALCHEMY_ARRAY = BLOCKS.register("alchemyarray", BlockAlchemyArray::new);
	public static final RegistryObject<Block> BLANK_RUNE = BASICBLOCKS.register("blankrune", () -> new BlockBloodRune(BloodRuneType.BLANK));
	public static final RegistryObject<Block> SPEED_RUNE = BASICBLOCKS.register("speedrune", () -> new BlockBloodRune(BloodRuneType.SPEED));
	public static final RegistryObject<Block> SACRIFICE_RUNE = BASICBLOCKS.register("sacrificerune", () -> new BlockBloodRune(BloodRuneType.SACRIFICE));
	public static final RegistryObject<Block> SELF_SACRIFICE_RUNE = BASICBLOCKS.register("selfsacrificerune", () -> new BlockBloodRune(BloodRuneType.SELF_SACRIFICE));
	public static final RegistryObject<Block> DISPLACEMENT_RUNE = BASICBLOCKS.register("dislocationrune", () -> new BlockBloodRune(BloodRuneType.DISPLACEMENT));
	public static final RegistryObject<Block> CAPACITY_RUNE = BASICBLOCKS.register("altarcapacityrune", () -> new BlockBloodRune(BloodRuneType.CAPACITY));
	public static final RegistryObject<Block> AUGMENTED_CAPACITY_RUNE = BASICBLOCKS.register("bettercapacityrune", () -> new BlockBloodRune(BloodRuneType.AUGMENTED_CAPACITY));
	public static final RegistryObject<Block> ORB_RUNE = BASICBLOCKS.register("orbcapacityrune", () -> new BlockBloodRune(BloodRuneType.ORB));
	public static final RegistryObject<Block> ACCELERATION_RUNE = BASICBLOCKS.register("accelerationrune", () -> new BlockBloodRune(BloodRuneType.ACCELERATION));
	public static final RegistryObject<Block> CHARGING_RUNE = BASICBLOCKS.register("chargingrune", () -> new BlockBloodRune(BloodRuneType.CHARGING));

	public static final RegistryObject<Block> BLOOD_ALTAR = BLOCKS.register("altar", () -> new BlockAltar());
	public static final RegistryObject<Block> BLOOD_LIGHT = BLOCKS.register("bloodlight", () -> new BlockBloodLight());

	public static final RegistryObject<Block> BLANK_RITUAL_STONE = BLOCKS.register("ritualstone", () -> new BlockRitualStone(EnumRuneType.BLANK));
	public static final RegistryObject<Block> AIR_RITUAL_STONE = BLOCKS.register("airritualstone", () -> new BlockRitualStone(EnumRuneType.AIR));
	public static final RegistryObject<Block> WATER_RITUAL_STONE = BLOCKS.register("waterritualstone", () -> new BlockRitualStone(EnumRuneType.WATER));
	public static final RegistryObject<Block> FIRE_RITUAL_STONE = BLOCKS.register("fireritualstone", () -> new BlockRitualStone(EnumRuneType.FIRE));
	public static final RegistryObject<Block> EARTH_RITUAL_STONE = BLOCKS.register("earthritualstone", () -> new BlockRitualStone(EnumRuneType.EARTH));
	public static final RegistryObject<Block> DUSK_RITUAL_STONE = BLOCKS.register("duskritualstone", () -> new BlockRitualStone(EnumRuneType.DUSK));
	public static final RegistryObject<Block> DAWN_RITUAL_STONE = BLOCKS.register("lightritualstone", () -> new BlockRitualStone(EnumRuneType.DAWN));

	public static final RegistryObject<Block> BLOODSTONE = BASICBLOCKS.register("largebloodstonebrick", () -> new BloodstoneBlock());
	public static final RegistryObject<Block> BLOODSTONE_BRICK = BASICBLOCKS.register("bloodstonebrick", () -> new BloodstoneBlock());

	public static final RegistryObject<Block> MASTER_RITUAL_STONE = BASICBLOCKS.register("masterritualstone", () -> new BlockMasterRitualStone(false));

	public static final RegistryObject<Block> ALCHEMICAL_REACTION_CHAMBER = BLOCKS.register("alchemicalreactionchamber", () -> new BlockAlchemicalReactionChamber());
	public static final RegistryObject<Block> ALCHEMY_TABLE = BLOCKS.register("alchemytable", () -> new BlockAlchemyTable());

	public static final RegistryObject<Block> DEMON_CRUCIBLE = BLOCKS.register("demoncrucible", () -> new BlockDemonCrucible());
	public static final RegistryObject<Block> DEMON_CRYSTALLIZER = BLOCKS.register("demoncrystallizer", () -> new BlockDemonCrystallizer());

	public static final RegistryObject<Block> RAW_CRYSTAL_BLOCK = BLOCKS.register("rawdemoncrystal", () -> new BlockDemonCrystal(EnumDemonWillType.DEFAULT));
	public static final RegistryObject<Block> CORROSIVE_CRYSTAL_BLOCK = BLOCKS.register("corrosivedemoncrystal", () -> new BlockDemonCrystal(EnumDemonWillType.CORROSIVE));
	public static final RegistryObject<Block> DESTRUCTIVE_CRYSTAL_BLOCK = BLOCKS.register("destructivedemoncrystal", () -> new BlockDemonCrystal(EnumDemonWillType.DESTRUCTIVE));
	public static final RegistryObject<Block> VENGEFUL_CRYSTAL_BLOCK = BLOCKS.register("vengefuldemoncrystal", () -> new BlockDemonCrystal(EnumDemonWillType.VENGEFUL));
	public static final RegistryObject<Block> STEADFAST_CRYSTAL_BLOCK = BLOCKS.register("steadfastdemoncrystal", () -> new BlockDemonCrystal(EnumDemonWillType.STEADFAST));

	public static final RegistryObject<Block> ROUTING_NODE_BLOCK = BLOCKS.register("itemroutingnode", () -> new BlockItemRoutingNode());
	public static final RegistryObject<Block> INPUT_ROUTING_NODE_BLOCK = BLOCKS.register("inputroutingnode", () -> new BlockInputRoutingNode());
	public static final RegistryObject<Block> OUTPUT_ROUTING_NODE_BLOCK = BLOCKS.register("outputroutingnode", () -> new BlockOutputRoutingNode());
	public static final RegistryObject<Block> MASTER_ROUTING_NODE_BLOCK = BLOCKS.register("masterroutingnode", () -> new BlockMasterRoutingNode());

	public static final RegistryObject<Block> WOOD_PATH = BASICBLOCKS.register("woodbrickpath", () -> new BlockPath(2, AbstractBlock.Properties.create(Material.WOOD).hardnessAndResistance(2.0F, 5.0F).harvestTool(ToolType.AXE).harvestLevel(0)));
	public static final RegistryObject<Block> WOOD_TILE_PATH = BASICBLOCKS.register("woodtilepath", () -> new BlockPath(2, AbstractBlock.Properties.create(Material.WOOD).hardnessAndResistance(2.0F, 5.0F).harvestTool(ToolType.AXE).harvestLevel(0)));
	public static final RegistryObject<Block> STONE_PATH = BASICBLOCKS.register("stonebrickpath", () -> new BlockPath(4, AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)));
	public static final RegistryObject<Block> STONE_TILE_PATH = BASICBLOCKS.register("stonetilepath", () -> new BlockPath(4, AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)));
	public static final RegistryObject<Block> WORN_STONE_PATH = BASICBLOCKS.register("wornstonebrickpath", () -> new BlockPath(6, AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)));
	public static final RegistryObject<Block> WORN_STONE_TILE_PATH = BASICBLOCKS.register("wornstonetilepath", () -> new BlockPath(6, AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)));
	public static final RegistryObject<Block> OBSIDIAN_PATH = BASICBLOCKS.register("obsidianbrickpath", () -> new BlockPath(8, AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
	public static final RegistryObject<Block> OBSIDIAN_TILE_PATH = BASICBLOCKS.register("obsidiantilepath", () -> new BlockPath(8, AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));

	public static final RegistryObject<Block> MIMIC = BLOCKS.register("mimic", () -> new BlockMimic(Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(2.0f).setOpaque(BloodMagicBlocks::isntSolid).setSuffocates(BloodMagicBlocks::isntSolid).setBlocksVision(BloodMagicBlocks::isntSolid).notSolid()));
	public static final RegistryObject<Block> ETHEREAL_MIMIC = BLOCKS.register("ethereal_mimic", () -> new BlockMimic(Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(2.0f).setOpaque(BloodMagicBlocks::isntSolid).setSuffocates(BloodMagicBlocks::isntSolid).setBlocksVision(BloodMagicBlocks::isntSolid).notSolid().doesNotBlockMovement()));

	private static ForgeFlowingFluid.Properties makeLifeEssenceProperties()
	{
		return new ForgeFlowingFluid.Properties(LIFE_ESSENCE_FLUID, LIFE_ESSENCE_FLUID_FLOWING, FluidAttributes.builder(FLUID_STILL, FLUID_FLOWING).overlay(new ResourceLocation("block/water_overlay")).viscosity(1).sound(SoundEvents.ITEM_BUCKET_FILL, SoundEvents.ITEM_BUCKET_EMPTY)).bucket(LIFE_ESSENCE_BUCKET).block(LIFE_ESSENCE_BLOCK);
	}

	public static final ResourceLocation DOUBT_STILL_RESOURCE = new ResourceLocation("bloodmagic:block/liquid_doubt_still");
	public static final ResourceLocation DOUBT_FLOWING_RESOURCE = new ResourceLocation("bloodmagic:block/liquid_doubt_flowing");

	private static ForgeFlowingFluid.Properties makeDoubtProperties()
	{
		return new ForgeFlowingFluid.Properties(DOUBT_FLUID, DOUBT_FLUID_FLOWING, FluidAttributes.builder(DOUBT_STILL_RESOURCE, DOUBT_FLOWING_RESOURCE).overlay(new ResourceLocation("block/water_overlay")).viscosity(1).sound(SoundEvents.ITEM_BUCKET_FILL, SoundEvents.ITEM_BUCKET_EMPTY)).bucket(DOUBT_BUCKET).block(DOUBT_BLOCK);
	}

	public static RegistryObject<FlowingFluid> LIFE_ESSENCE_FLUID = FLUIDS.register("life_essence_fluid", () -> new ForgeFlowingFluid.Source(makeLifeEssenceProperties()));
	public static RegistryObject<FlowingFluid> LIFE_ESSENCE_FLUID_FLOWING = FLUIDS.register("life_essence_fluid_flowing", () -> new ForgeFlowingFluid.Flowing(makeLifeEssenceProperties()));
	public static RegistryObject<FlowingFluid> DOUBT_FLUID = FLUIDS.register("doubt_fluid", () -> new ForgeFlowingFluid.Source(makeDoubtProperties()));
	public static RegistryObject<FlowingFluid> DOUBT_FLUID_FLOWING = FLUIDS.register("doubt_fluid_flowing", () -> new ForgeFlowingFluid.Flowing(makeDoubtProperties()));

	public static RegistryObject<FlowingFluidBlock> LIFE_ESSENCE_BLOCK = BLOCKS.register("life_essence_block", () -> new FlowingFluidBlock(LIFE_ESSENCE_FLUID, Block.Properties.create(net.minecraft.block.material.Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops()));
	public static RegistryObject<FlowingFluidBlock> DOUBT_BLOCK = BLOCKS.register("doubt_block", () -> new FlowingFluidBlock(DOUBT_FLUID, Block.Properties.create(net.minecraft.block.material.Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops()));
	public static RegistryObject<Item> LIFE_ESSENCE_BUCKET = ITEMS.register("life_essence_bucket", () -> new BucketItem(LIFE_ESSENCE_FLUID, new Item.Properties().containerItem(Items.BUCKET).maxStackSize(1).group(BloodMagic.TAB)));
	public static RegistryObject<Item> DOUBT_BUCKET = ITEMS.register("doubt_bucket", () -> new BucketItem(DOUBT_FLUID, new Item.Properties().containerItem(Items.BUCKET).maxStackSize(1).group(BloodMagic.TAB)));

	public static final RegistryObject<ContainerType<ContainerSoulForge>> SOUL_FORGE_CONTAINER = CONTAINERS.register("soul_forge_container", () -> IForgeContainerType.create(ContainerSoulForge::new));
	public static final RegistryObject<ContainerType<ContainerAlchemicalReactionChamber>> ARC_CONTAINER = CONTAINERS.register("arc_container", () -> IForgeContainerType.create(ContainerAlchemicalReactionChamber::new));
	public static final RegistryObject<ContainerType<ContainerAlchemyTable>> ALCHEMY_TABLE_CONTAINER = CONTAINERS.register("alchemy_table_container", () -> IForgeContainerType.create(ContainerAlchemyTable::new));
	public static final RegistryObject<ContainerType<ContainerHolding>> HOLDING_CONTAINER = CONTAINERS.register("holding_container", () -> IForgeContainerType.create(ContainerHolding::new));
	public static final RegistryObject<ContainerType<ContainerFilter>> FILTER_CONTAINER = CONTAINERS.register("filter_container", () -> IForgeContainerType.create(ContainerFilter::new));
	public static final RegistryObject<ContainerType<ContainerItemRoutingNode>> ROUTING_NODE_CONTAINER = CONTAINERS.register("routing_node_container", () -> IForgeContainerType.create(ContainerItemRoutingNode::new));
	public static final RegistryObject<ContainerType<ContainerTrainingBracelet>> TRAINING_BRACELET_CONTAINER = CONTAINERS.register("training_bracelet_container", () -> IForgeContainerType.create(ContainerTrainingBracelet::new));

	// Dungeon Blocks
	public static final RegistryObject<Block> DUNGEON_BRICK_1 = DUNGEONBLOCKS.register("dungeon_brick1", () -> new Block(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool()));
	public static final RegistryObject<Block> DUNGEON_BRICK_2 = DUNGEONBLOCKS.register("dungeon_brick2", () -> new Block(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool()));
	public static final RegistryObject<Block> DUNGEON_BRICK_3 = DUNGEONBLOCKS.register("dungeon_brick3", () -> new Block(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool()));
	public static final RegistryObject<Block> DUNGEON_ORE = DUNGEONBLOCKS.register("dungeon_ore", () -> new Block(Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool()));

	public static final RegistryObject<Block> DUNGEON_STONE = BLOCKS.register("dungeon_stone", () -> new Block(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool()));
	public static final RegistryObject<Block> DUNGEON_EYE = DUNGEONBLOCKS.register("dungeon_eye", () -> new Block(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool().setLightLevel((state) -> {
		return 15;
	})));
	public static final RegistryObject<Block> DUNGEON_EMITTER = DUNGEONBLOCKS.register("dungeon_emitter", () -> new RedstoneBlock(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool().setLightLevel((state) -> {
		return 8;
	})));
	public static final RegistryObject<Block> DUNGEON_ALTERNATOR = DUNGEONBLOCKS.register("dungeon_alternator", () -> new BlockAlternator(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool()));
	public static final RegistryObject<Block> DUNGEON_POLISHED_STONE = DUNGEONBLOCKS.register("dungeon_polished", () -> new Block(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool()));
	public static final RegistryObject<Block> DUNGEON_TILE = DUNGEONBLOCKS.register("dungeon_tile", () -> new Block(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool()));
	public static final RegistryObject<Block> DUNGEON_SMALL_BRICK = DUNGEONBLOCKS.register("dungeon_smallbrick", () -> new Block(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool()));
	public static final RegistryObject<Block> DUNGEON_TILE_SPECIAL = DUNGEONBLOCKS.register("dungeon_tilespecial", () -> new Block(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool()));

	public static final RegistryObject<Block> DUNGEON_BRICK_ASSORTED = BLOCKS.register("dungeon_brick_assorted", () -> new Block(Properties.create(Material.ROCK).hardnessAndResistance(20.0F, 50.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(3).setRequiresTool()));

	public static final RegistryObject<Block> DUNGEON_BRICK_STAIRS = BLOCKS.register("dungeon_brick_stairs", () -> new StairsBlock(() -> DUNGEON_BRICK_1.get().getDefaultState(), Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool()));
	public static final RegistryObject<Block> DUNGEON_POLISHED_STAIRS = BLOCKS.register("dungeon_polished_stairs", () -> new StairsBlock(() -> DUNGEON_POLISHED_STONE.get().getDefaultState(), Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool()));

	public static final RegistryObject<Block> DUNGEON_PILLAR_CENTER = BLOCKS.register("dungeon_pillar_center", () -> new RotatedPillarBlock(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool()));
	public static final RegistryObject<Block> DUNGEON_PILLAR_SPECIAL = BLOCKS.register("dungeon_pillar_special", () -> new RotatedPillarBlock(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool()));
	public static final RegistryObject<Block> DUNGEON_PILLAR_CAP = BLOCKS.register("dungeon_pillar_cap", () -> new BlockPillarCap(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool()));

	public static final RegistryObject<Block> DUNGEON_BRICK_WALL = BLOCKS.register("dungeon_brick_wall", () -> new WallBlock(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool()));
	public static final RegistryObject<Block> DUNGEON_POLISHED_WALL = BLOCKS.register("dungeon_polished_wall", () -> new WallBlock(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool()));
	public static final RegistryObject<Block> DUNGEON_BRICK_GATE = BLOCKS.register("dungeon_brick_gate", () -> new FenceGateBlock(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool()));
	public static final RegistryObject<Block> DUNGEON_POLISHED_GATE = BLOCKS.register("dungeon_polished_gate", () -> new FenceGateBlock(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool()));

	public static final RegistryObject<Block> DUNGEON_BRICK_SLAB = BLOCKS.register("dungeon_brick_slab", () -> new SlabBlock(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool()));
	public static final RegistryObject<Block> DUNGEON_TILE_SLAB = BLOCKS.register("dungeon_tile_slab", () -> new SlabBlock(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool()));

	public static final RegistryObject<Block> HELLFORGED_BLOCK = DUNGEONBLOCKS.register("dungeon_metal", () -> new Block(Properties.create(Material.IRON).hardnessAndResistance(5.0F, 6.0F).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool()));

	public static final RegistryObject<Block> DUNGEON_CRACKED_BRICK_1 = DUNGEONBLOCKS.register("dungeon_regular_cracked_brick1", () -> new Block(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool()));
	public static final RegistryObject<Block> DUNGEON_GLOWING_CRACKED_BRICK_1 = DUNGEONBLOCKS.register("dungeon_cracked_brick1", () -> new Block(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool()));

	public static final RegistryObject<Block> DUNGEON_CONTROLLER = BLOCKS.register("dungeon_controller", () -> new BlockDungeonController());
	public static final RegistryObject<Block> DUNGEON_SEAL = BLOCKS.register("dungeon_seal", () -> new BlockDungeonSeal());
	public static final RegistryObject<Block> SPIKES = BLOCKS.register("spikes", () -> new BlockSpikes(Properties.create(Material.IRON).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.CHAIN).notSolid().doesNotBlockMovement().harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool()));
	public static final RegistryObject<Block> DUNGEON_SPIKE_TRAP = BLOCKS.register("dungeon_spike_trap", () -> new BlockSpikeTrap(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool()));


	public static final RegistryObject<Block> NETHER_SOIL = BLOCKS.register("nether_soil", () -> new BlockNetherrackSoil(Properties.create(Material.EARTH).hardnessAndResistance(0.4F, 0.4F).sound(SoundType.NETHERRACK).harvestTool(ToolType.PICKAXE).tickRandomly()));

	public static final RegistryObject<Block> GROWING_DOUBT = BLOCKS.register("creeping_doubt", () -> new BlockGrowingDoubt(Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().zeroHardnessAndResistance().sound(SoundType.CROP)));
	public static final RegistryObject<Block> WEAK_TAU = BLOCKS.register("weak_tau", () -> new BlockTau(Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().zeroHardnessAndResistance().sound(SoundType.CROP), false));
	public static final RegistryObject<Block> STRONG_TAU = BLOCKS.register("strong_tau", () -> new BlockTau(Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().zeroHardnessAndResistance().sound(SoundType.CROP), true));

	public static final RegistryObject<Block> INVERSION_PILLAR = BLOCKS.register("inversion_pillar", () -> new BlockInversionPillar(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool().setBlocksVision(BloodMagicBlocks::isntSolid).notSolid().setOpaque(BloodMagicBlocks::isntSolid)));
	public static final RegistryObject<Block> INVERSION_PILLAR_CAP = BLOCKS.register("inversion_pillar_cap", () -> new BlockInversionPillarEnd(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool()));

	private static boolean isntSolid(BlockState state, IBlockReader reader, BlockPos pos)
	{
		return false;
	}

	public static final RegistryObject<Block> SHAPED_CHARGE = BLOCKS.register("shaped_charge", () -> new BlockShapedExplosive(3, Properties.create(Material.IRON).hardnessAndResistance(5.0F, 6.0F).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(1).setRequiresTool()));
	public static final RegistryObject<Block> DEFORESTER_CHARGE = BLOCKS.register("deforester_charge", () -> new BlockDeforesterCharge(3, Properties.create(Material.IRON).hardnessAndResistance(5.0F, 6.0F).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(1).setRequiresTool()));
	public static final RegistryObject<Block> VEINMINE_CHARGE = BLOCKS.register("veinmine_charge", () -> new BlockVeinMineCharge(3, Properties.create(Material.IRON).hardnessAndResistance(5.0F, 6.0F).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(1).setRequiresTool()));
	public static final RegistryObject<Block> FUNGAL_CHARGE = BLOCKS.register("fungal_charge", () -> new BlockFungalCharge(3, Properties.create(Material.IRON).hardnessAndResistance(5.0F, 6.0F).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(1).setRequiresTool()));

	//
////	private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> sup, Function<RegistryObject<T>, Supplier<? extends Item>> itemCreator) 
////	{
////	    RegistryObject<T> ret = registerNoItem(name, sup);
////	    ITEMS.register(name, itemCreator.apply(ret));
////	    return ret;
////	}
//	
//	private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> sup, Function<RegistryObject<T>, Supplier<? extends Item>> itemCreator)
//	{
//		RegistryObject<T> ret = registerNoItem(name, sup);
//	    ITEMS.register(name, itemCreator.apply(ret));
//	    return ret;
//	}
//
//	private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<? extends T> sup) 
//	{
//	    return BLOCKS.register(name, sup);
//	}

//	  private static Supplier<BlockItem> item(final RegistryObject<? extends Block> block, final Supplier<Callable<ItemStackTileEntityRenderer>> renderMethod) 
//	  {
//	    return () -> new BlockItem(block.get(), new Item.Properties().group(IronChests.IRONCHESTS_ITEM_GROUP).setISTER(renderMethod));
//	  }
}
