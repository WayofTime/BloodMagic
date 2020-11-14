package wayoftime.bloodmagic.common.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.Block;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.block.enums.BloodRuneType;
import wayoftime.bloodmagic.common.block.base.BlockPillarCap;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.tile.contailer.ContainerAlchemicalReactionChamber;
import wayoftime.bloodmagic.tile.contailer.ContainerAlchemyTable;
import wayoftime.bloodmagic.tile.contailer.ContainerSoulForge;
import wayoftime.bloodmagic.will.EnumDemonWillType;

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

	public static final RegistryObject<Block> BLOODSTONE = BASICBLOCKS.register("largebloodstonebrick", () -> new Block(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(1)));
	public static final RegistryObject<Block> BLOODSTONE_BRICK = BASICBLOCKS.register("bloodstonebrick", () -> new Block(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(1)));

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

	public static final RegistryObject<Block> WOOD_PATH = BASICBLOCKS.register("woodbrickpath", () -> new BlockPath(2, AbstractBlock.Properties.create(Material.WOOD).hardnessAndResistance(2.0F, 5.0F).harvestTool(ToolType.AXE).harvestLevel(0)));
	public static final RegistryObject<Block> WOOD_TILE_PATH = BASICBLOCKS.register("woodtilepath", () -> new BlockPath(2, AbstractBlock.Properties.create(Material.WOOD).hardnessAndResistance(2.0F, 5.0F).harvestTool(ToolType.AXE).harvestLevel(0)));
	public static final RegistryObject<Block> STONE_PATH = BASICBLOCKS.register("stonebrickpath", () -> new BlockPath(4, AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)));
	public static final RegistryObject<Block> STONE_TILE_PATH = BASICBLOCKS.register("stonetilepath", () -> new BlockPath(4, AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)));
	public static final RegistryObject<Block> WORN_STONE_PATH = BASICBLOCKS.register("wornstonebrickpath", () -> new BlockPath(6, AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)));
	public static final RegistryObject<Block> WORN_STONE_TILE_PATH = BASICBLOCKS.register("wornstonetilepath", () -> new BlockPath(6, AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)));
	public static final RegistryObject<Block> OBSIDIAN_PATH = BASICBLOCKS.register("obsidianbrickpath", () -> new BlockPath(8, AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));
	public static final RegistryObject<Block> OBSIDIAN_TILE_PATH = BASICBLOCKS.register("obsidiantilepath", () -> new BlockPath(8, AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).harvestTool(ToolType.PICKAXE).harvestLevel(3)));

	private static ForgeFlowingFluid.Properties makeProperties()
	{
		return new ForgeFlowingFluid.Properties(LIFE_ESSENCE_FLUID, LIFE_ESSENCE_FLUID_FLOWING, FluidAttributes.builder(FLUID_STILL, FLUID_FLOWING)).bucket(LIFE_ESSENCE_BUCKET).block(LIFE_ESSENCE_BLOCK);
	}

	public static RegistryObject<FlowingFluid> LIFE_ESSENCE_FLUID = FLUIDS.register("life_essence_fluid", () -> new ForgeFlowingFluid.Source(makeProperties()));
	public static RegistryObject<FlowingFluid> LIFE_ESSENCE_FLUID_FLOWING = FLUIDS.register("life_essence_fluid_flowing", () -> new ForgeFlowingFluid.Flowing(makeProperties()));

	public static RegistryObject<FlowingFluidBlock> LIFE_ESSENCE_BLOCK = BLOCKS.register("life_essence_block", () -> new FlowingFluidBlock(LIFE_ESSENCE_FLUID, Block.Properties.create(net.minecraft.block.material.Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops()));
	public static RegistryObject<Item> LIFE_ESSENCE_BUCKET = ITEMS.register("life_essence_bucket", () -> new BucketItem(LIFE_ESSENCE_FLUID, new Item.Properties().containerItem(Items.BUCKET).maxStackSize(1).group(BloodMagic.TAB)));

	public static final RegistryObject<ContainerType<ContainerSoulForge>> SOUL_FORGE_CONTAINER = CONTAINERS.register("soul_forge_container", () -> IForgeContainerType.create(ContainerSoulForge::new));
	public static final RegistryObject<ContainerType<ContainerAlchemicalReactionChamber>> ARC_CONTAINER = CONTAINERS.register("arc_container", () -> IForgeContainerType.create(ContainerAlchemicalReactionChamber::new));
	public static final RegistryObject<ContainerType<ContainerAlchemyTable>> ALCHEMY_TABLE_CONTAINER = CONTAINERS.register("alchemy_table_container", () -> IForgeContainerType.create(ContainerAlchemyTable::new));

	// Dungeon Blocks
	public static final RegistryObject<Block> DUNGEON_BRICK_1 = DUNGEONBLOCKS.register("dungeon_brick1", () -> new Block(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool()));
	public static final RegistryObject<Block> DUNGEON_BRICK_2 = DUNGEONBLOCKS.register("dungeon_brick2", () -> new Block(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool()));
	public static final RegistryObject<Block> DUNGEON_BRICK_3 = DUNGEONBLOCKS.register("dungeon_brick3", () -> new Block(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool()));

	public static final RegistryObject<Block> DUNGEON_STONE = BLOCKS.register("dungeon_stone", () -> new Block(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool()));
	public static final RegistryObject<Block> DUNGEON_EYE = DUNGEONBLOCKS.register("dungeon_eye", () -> new Block(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool().setLightLevel((state) -> {
		return 15;
	})));
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
