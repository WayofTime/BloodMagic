package wayoftime.bloodmagic.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.block.enums.BloodRuneType;
import wayoftime.bloodmagic.common.block.base.BlockPillarCap;
import wayoftime.bloodmagic.common.container.item.ContainerFilter;
import wayoftime.bloodmagic.common.container.item.ContainerHolding;
import wayoftime.bloodmagic.common.container.item.ContainerTrainingBracelet;
import wayoftime.bloodmagic.common.container.tile.*;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.ritual.EnumRuneType;

import java.util.function.Consumer;

public class BloodMagicBlocks
{

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BloodMagic.MODID);
	public static final DeferredRegister<Block> BASICBLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BloodMagic.MODID);
	public static final DeferredRegister<Block> DUNGEONBLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BloodMagic.MODID);
	public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, BloodMagic.MODID);

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

	public static final RegistryObject<Block> SELF_SACRIFICE_RUNE_2 = BASICBLOCKS.register("selfsacrificerune2", () -> new BlockBloodRune(BloodRuneType.SELF_SACRIFICE, 2));
	public static final RegistryObject<Block> SPEED_RUNE_2 = BASICBLOCKS.register("speedrune2", () -> new BlockBloodRune(BloodRuneType.SPEED, 2));
	public static final RegistryObject<Block> SACRIFICE_RUNE_2 = BASICBLOCKS.register("sacrificerune2", () -> new BlockBloodRune(BloodRuneType.SACRIFICE, 2));
	public static final RegistryObject<Block> DISPLACEMENT_RUNE_2 = BASICBLOCKS.register("dislocationrune2", () -> new BlockBloodRune(BloodRuneType.DISPLACEMENT, 2));
	public static final RegistryObject<Block> CAPACITY_RUNE_2 = BASICBLOCKS.register("altarcapacityrune2", () -> new BlockBloodRune(BloodRuneType.CAPACITY, 2));
	public static final RegistryObject<Block> AUGMENTED_CAPACITY_RUNE_2 = BASICBLOCKS.register("bettercapacityrune2", () -> new BlockBloodRune(BloodRuneType.AUGMENTED_CAPACITY, 2));
	public static final RegistryObject<Block> ORB_RUNE_2 = BASICBLOCKS.register("orbcapacityrune2", () -> new BlockBloodRune(BloodRuneType.ORB, 2));
	public static final RegistryObject<Block> ACCELERATION_RUNE_2 = BASICBLOCKS.register("accelerationrune2", () -> new BlockBloodRune(BloodRuneType.ACCELERATION, 2));
	public static final RegistryObject<Block> CHARGING_RUNE_2 = BASICBLOCKS.register("chargingrune2", () -> new BlockBloodRune(BloodRuneType.CHARGING, 2));

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
	public static final RegistryObject<Block> DEMON_PYLON = BLOCKS.register("demonpylon", () -> new BlockDemonPylon());

	public static final RegistryObject<Block> RAW_CRYSTAL_BLOCK = BLOCKS.register("rawdemoncrystal", () -> new BlockDemonCrystal(EnumDemonWillType.DEFAULT));
	public static final RegistryObject<Block> CORROSIVE_CRYSTAL_BLOCK = BLOCKS.register("corrosivedemoncrystal", () -> new BlockDemonCrystal(EnumDemonWillType.CORROSIVE));
	public static final RegistryObject<Block> DESTRUCTIVE_CRYSTAL_BLOCK = BLOCKS.register("destructivedemoncrystal", () -> new BlockDemonCrystal(EnumDemonWillType.DESTRUCTIVE));
	public static final RegistryObject<Block> VENGEFUL_CRYSTAL_BLOCK = BLOCKS.register("vengefuldemoncrystal", () -> new BlockDemonCrystal(EnumDemonWillType.VENGEFUL));
	public static final RegistryObject<Block> STEADFAST_CRYSTAL_BLOCK = BLOCKS.register("steadfastdemoncrystal", () -> new BlockDemonCrystal(EnumDemonWillType.STEADFAST));

	public static final RegistryObject<Block> ROUTING_NODE_BLOCK = BLOCKS.register("itemroutingnode", () -> new BlockItemRoutingNode());
	public static final RegistryObject<Block> INPUT_ROUTING_NODE_BLOCK = BLOCKS.register("inputroutingnode", () -> new BlockInputRoutingNode());
	public static final RegistryObject<Block> OUTPUT_ROUTING_NODE_BLOCK = BLOCKS.register("outputroutingnode", () -> new BlockOutputRoutingNode());
	public static final RegistryObject<Block> MASTER_ROUTING_NODE_BLOCK = BLOCKS.register("masterroutingnode", () -> new BlockMasterRoutingNode());

	public static final RegistryObject<Block> TELEPOSER = BLOCKS.register("teleposer", () -> new BlockTeleposer());

	public static final RegistryObject<Block> WOOD_PATH = BASICBLOCKS.register("woodbrickpath", () -> new BlockPath(2, BlockBehaviour.Properties.of().strength(2.0F, 5.0F).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> WOOD_TILE_PATH = BASICBLOCKS.register("woodtilepath", () -> new BlockPath(2, BlockBehaviour.Properties.of().strength(2.0F, 5.0F).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> STONE_PATH = BASICBLOCKS.register("stonebrickpath", () -> new BlockPath(4, BlockBehaviour.Properties.of().strength(2.0F, 5.0F).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> STONE_TILE_PATH = BASICBLOCKS.register("stonetilepath", () -> new BlockPath(4, BlockBehaviour.Properties.of().strength(2.0F, 5.0F).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> WORN_STONE_PATH = BASICBLOCKS.register("wornstonebrickpath", () -> new BlockPath(6, BlockBehaviour.Properties.of().strength(2.0F, 5.0F).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> WORN_STONE_TILE_PATH = BASICBLOCKS.register("wornstonetilepath", () -> new BlockPath(6, BlockBehaviour.Properties.of().strength(2.0F, 5.0F).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> OBSIDIAN_PATH = BASICBLOCKS.register("obsidianbrickpath", () -> new BlockPath(8, BlockBehaviour.Properties.of().strength(2.0F, 5.0F).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> OBSIDIAN_TILE_PATH = BASICBLOCKS.register("obsidiantilepath", () -> new BlockPath(8, BlockBehaviour.Properties.of().strength(2.0F, 5.0F).requiresCorrectToolForDrops()));

	public static final RegistryObject<Block> MIMIC = BLOCKS.register("mimic", () -> new BlockMimic(Properties.of().requiresCorrectToolForDrops().sound(SoundType.METAL).strength(2.0f).isRedstoneConductor(BloodMagicBlocks::isntSolid).isSuffocating(BloodMagicBlocks::isntSolid).isViewBlocking(BloodMagicBlocks::isntSolid).noOcclusion()));
	public static final RegistryObject<Block> ETHEREAL_MIMIC = BLOCKS.register("ethereal_mimic", () -> new BlockMimic(Properties.of().requiresCorrectToolForDrops().sound(SoundType.METAL).strength(2.0f).isRedstoneConductor(BloodMagicBlocks::isntSolid).isSuffocating(BloodMagicBlocks::isntSolid).isViewBlocking(BloodMagicBlocks::isntSolid).noOcclusion().noCollission()));

	public static final RegistryObject<Block> SPECTRAL = BLOCKS.register("spectral", () -> new BlockSpectral(Properties.of().sound(SoundType.METAL).strength(100.0f).isRedstoneConductor(BloodMagicBlocks::isntSolid).isSuffocating(BloodMagicBlocks::isntSolid).isViewBlocking(BloodMagicBlocks::isntSolid).noOcclusion().noCollission()));

	// TODO: Move these to a dedicated class?
	public static final RegistryObject<MenuType<ContainerSoulForge>> SOUL_FORGE_CONTAINER = CONTAINERS.register("soul_forge_container", () -> IForgeMenuType.create(ContainerSoulForge::new));
	public static final RegistryObject<MenuType<ContainerAlchemicalReactionChamber>> ARC_CONTAINER = CONTAINERS.register("arc_container", () -> IForgeMenuType.create(ContainerAlchemicalReactionChamber::new));
	public static final RegistryObject<MenuType<ContainerAlchemyTable>> ALCHEMY_TABLE_CONTAINER = CONTAINERS.register("alchemy_table_container", () -> IForgeMenuType.create(ContainerAlchemyTable::new));
	public static final RegistryObject<MenuType<ContainerHolding>> HOLDING_CONTAINER = CONTAINERS.register("holding_container", () -> IForgeMenuType.create(ContainerHolding::new));
	public static final RegistryObject<MenuType<ContainerTeleposer>> TELEPOSER_CONTAINER = CONTAINERS.register("teleposer_container", () -> IForgeMenuType.create(ContainerTeleposer::new));
	public static final RegistryObject<MenuType<ContainerFilter>> FILTER_CONTAINER = CONTAINERS.register("filter_container", () -> IForgeMenuType.create(ContainerFilter::new));
	public static final RegistryObject<MenuType<ContainerItemRoutingNode>> ROUTING_NODE_CONTAINER = CONTAINERS.register("routing_node_container", () -> IForgeMenuType.create(ContainerItemRoutingNode::new));
	public static final RegistryObject<MenuType<ContainerTrainingBracelet>> TRAINING_BRACELET_CONTAINER = CONTAINERS.register("training_bracelet_container", () -> IForgeMenuType.create(ContainerTrainingBracelet::new));
	public static final RegistryObject<MenuType<ContainerMasterRoutingNode>> MASTER_ROUTING_NODE_CONTAINER = CONTAINERS.register("master_routing_node_container", () -> IForgeMenuType.create(ContainerMasterRoutingNode::new));

	// Dungeon Blocks
	public static final RegistryObject<Block> DUNGEON_BRICK_1 = DUNGEONBLOCKS.register("dungeon_brick1", () -> new Block(Properties.of().strength(2.0F, 5.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> DUNGEON_BRICK_2 = DUNGEONBLOCKS.register("dungeon_brick2", () -> new Block(Properties.of().strength(2.0F, 5.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> DUNGEON_BRICK_3 = DUNGEONBLOCKS.register("dungeon_brick3", () -> new Block(Properties.of().strength(2.0F, 5.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> DUNGEON_ORE = BLOCKS.register("dungeon_ore", () -> new Block(Properties.of().strength(3.0F, 3.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));

	public static final RegistryObject<Block> DUNGEON_STONE = BLOCKS.register("dungeon_stone", () -> new Block(Properties.of().strength(2.0F, 5.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> DUNGEON_EYE = DUNGEONBLOCKS.register("dungeon_eye", () -> new Block(Properties.of().strength(2.0F, 5.0F).sound(SoundType.STONE).requiresCorrectToolForDrops().lightLevel((state) -> {
		return 15;
	})));
	public static final RegistryObject<Block> DUNGEON_EMITTER = DUNGEONBLOCKS.register("dungeon_emitter", () -> new PoweredBlock(Properties.of().strength(2.0F, 5.0F).sound(SoundType.STONE).requiresCorrectToolForDrops().lightLevel((state) -> {
		return 8;
	})));
	public static final RegistryObject<Block> DUNGEON_ALTERNATOR = DUNGEONBLOCKS.register("dungeon_alternator", () -> new BlockAlternator(Properties.of().strength(2.0F, 5.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> DUNGEON_POLISHED_STONE = DUNGEONBLOCKS.register("dungeon_polished", () -> new Block(Properties.of().strength(2.0F, 5.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> DUNGEON_TILE = DUNGEONBLOCKS.register("dungeon_tile", () -> new Block(Properties.of().strength(2.0F, 5.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> DUNGEON_SMALL_BRICK = DUNGEONBLOCKS.register("dungeon_smallbrick", () -> new Block(Properties.of().strength(2.0F, 5.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> DUNGEON_TILE_SPECIAL = DUNGEONBLOCKS.register("dungeon_tilespecial", () -> new Block(Properties.of().strength(2.0F, 5.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));

	public static final RegistryObject<Block> DUNGEON_BRICK_ASSORTED = BLOCKS.register("dungeon_brick_assorted", () -> new Block(Properties.of().strength(20.0F, 50.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));

	public static final RegistryObject<Block> DUNGEON_BRICK_STAIRS = BLOCKS.register("dungeon_brick_stairs", () -> new StairBlock(() -> DUNGEON_BRICK_1.get().defaultBlockState(), Properties.of().strength(2.0F, 5.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> DUNGEON_POLISHED_STAIRS = BLOCKS.register("dungeon_polished_stairs", () -> new StairBlock(() -> DUNGEON_POLISHED_STONE.get().defaultBlockState(), Properties.of().strength(2.0F, 5.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));

	public static final RegistryObject<Block> DUNGEON_PILLAR_CENTER = BLOCKS.register("dungeon_pillar_center", () -> new RotatedPillarBlock(Properties.of().strength(2.0F, 5.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> DUNGEON_PILLAR_SPECIAL = BLOCKS.register("dungeon_pillar_special", () -> new RotatedPillarBlock(Properties.of().strength(2.0F, 5.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> DUNGEON_PILLAR_CAP = BLOCKS.register("dungeon_pillar_cap", () -> new BlockPillarCap(Properties.of().strength(2.0F, 5.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));

	public static final RegistryObject<Block> DUNGEON_BRICK_WALL = BLOCKS.register("dungeon_brick_wall", () -> new WallBlock(Properties.of().strength(2.0F, 5.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> DUNGEON_POLISHED_WALL = BLOCKS.register("dungeon_polished_wall", () -> new WallBlock(Properties.of().strength(2.0F, 5.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> DUNGEON_BRICK_GATE = BLOCKS.register("dungeon_brick_gate", () -> new FenceGateBlock(Properties.of().strength(2.0F, 5.0F).sound(SoundType.STONE).requiresCorrectToolForDrops() , WoodType.OAK ));
	public static final RegistryObject<Block> DUNGEON_POLISHED_GATE = BLOCKS.register("dungeon_polished_gate", () -> new FenceGateBlock(Properties.of().strength(2.0F, 5.0F).sound(SoundType.STONE).requiresCorrectToolForDrops(), WoodType.OAK));

	public static final RegistryObject<Block> DUNGEON_BRICK_SLAB = BLOCKS.register("dungeon_brick_slab", () -> new SlabBlock(Properties.of().strength(2.0F, 5.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> DUNGEON_TILE_SLAB = BLOCKS.register("dungeon_tile_slab", () -> new SlabBlock(Properties.of().strength(2.0F, 5.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));

	public static final RegistryObject<Block> HELLFORGED_BLOCK = DUNGEONBLOCKS.register("dungeon_metal", () -> new Block(Properties.of().strength(5.0F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> RAW_HELLFORGED_BLOCK = BASICBLOCKS.register("rawdemoniteblock", () -> new Block(Properties.of().strength(5.0F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops()));

	public static final RegistryObject<Block> DUNGEON_CRACKED_BRICK_1 = DUNGEONBLOCKS.register("dungeon_regular_cracked_brick1", () -> new Block(Properties.of().strength(2.0F, 5.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> DUNGEON_GLOWING_CRACKED_BRICK_1 = DUNGEONBLOCKS.register("dungeon_cracked_brick1", () -> new Block(Properties.of().strength(2.0F, 5.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));

	public static final RegistryObject<Block> DUNGEON_CONTROLLER = BLOCKS.register("dungeon_controller", () -> new BlockDungeonController());
	public static final RegistryObject<Block> DUNGEON_SEAL = BLOCKS.register("dungeon_seal", () -> new BlockDungeonSeal());
	public static final RegistryObject<Block> SPECIAL_DUNGEON_SEAL = BLOCKS.register("special_dungeon_seal", () -> new BlockSpecialDungeonSeal());
	public static final RegistryObject<Block> SPIKES = BLOCKS.register("spikes", () -> new BlockSpikes(Properties.of().strength(2.0F, 5.0F).sound(SoundType.CHAIN).noOcclusion().noCollission().requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> DUNGEON_SPIKE_TRAP = BLOCKS.register("dungeon_spike_trap", () -> new BlockSpikeTrap(Properties.of().strength(2.0F, 5.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));

	public static final RegistryObject<Block> NETHER_SOIL = BLOCKS.register("nether_soil", () -> new BlockNetherrackSoil(Properties.of().strength(0.4F, 0.4F).sound(SoundType.NETHERRACK).randomTicks()));

	public static final RegistryObject<Block> GROWING_DOUBT = BLOCKS.register("creeping_doubt", () -> new BlockGrowingDoubt(Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.CROP)));
	public static final RegistryObject<Block> WEAK_TAU = BLOCKS.register("weak_tau", () -> new BlockTau(Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.CROP), false));
	public static final RegistryObject<Block> STRONG_TAU = BLOCKS.register("strong_tau", () -> new BlockTau(Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.CROP), true));

	public static final RegistryObject<Block> INVERSION_PILLAR = BLOCKS.register("inversion_pillar", () -> new BlockInversionPillar(Properties.of().strength(100.0F, 500000.0F).sound(SoundType.STONE).requiresCorrectToolForDrops().isViewBlocking(BloodMagicBlocks::isntSolid).noOcclusion().isRedstoneConductor(BloodMagicBlocks::isntSolid)));
	public static final RegistryObject<Block> INVERSION_PILLAR_CAP = BLOCKS.register("inversion_pillar_cap", () -> new BlockInversionPillarEnd(Properties.of().strength(2.0F, 5.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));

	private static boolean isntSolid(BlockState state, BlockGetter reader, BlockPos pos)
	{
		return false;
	}

	public static final RegistryObject<Block> SHAPED_CHARGE = BLOCKS.register("shaped_charge", () -> new BlockShapedExplosive(2, Properties.of().strength(2.0F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> DEFORESTER_CHARGE = BLOCKS.register("deforester_charge", () -> new BlockDeforesterCharge(64 * 2, Properties.of().strength(2.0F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> VEINMINE_CHARGE = BLOCKS.register("veinmine_charge", () -> new BlockVeinMineCharge(64 * 2, Properties.of().strength(2.0F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> FUNGAL_CHARGE = BLOCKS.register("fungal_charge", () -> new BlockFungalCharge(64 * 2, Properties.of().strength(2.0F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops()));

	public static final RegistryObject<Block> AUG_SHAPED_CHARGE = BLOCKS.register("aug_shaped_charge", () -> new BlockShapedExplosive(3, Properties.of().strength(2.0F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> DEFORESTER_CHARGE_2 = BLOCKS.register("deforester_charge_2", () -> new BlockDeforesterCharge(64 * 8, Properties.of().strength(2.0F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> VEINMINE_CHARGE_2 = BLOCKS.register("veinmine_charge_2", () -> new BlockVeinMineCharge(64 * 8, Properties.of().strength(2.0F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> FUNGAL_CHARGE_2 = BLOCKS.register("fungal_charge_2", () -> new BlockFungalCharge(64 * 8, Properties.of().strength(2.0F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops()));

	public static final RegistryObject<Block> SHAPED_CHARGE_DEEP = BLOCKS.register("shaped_charge_deep", () -> new BlockRectangularShapedExplosive(2, 20, Properties.of().strength(2.0F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops()));

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
