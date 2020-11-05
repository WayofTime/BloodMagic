package wayoftime.bloodmagic.common.block;

import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.SoundType;
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
	public static final DeferredRegister<Item> ITEMS = BloodMagicItems.ITEMS;
	public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, BloodMagic.MODID);
	public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, BloodMagic.MODID);

//	public static final RegistryObject<Block> BLOODSTONE = BASICBLOCKS.register("ruby_block", BloodstoneBlock::new);
	public static final RegistryObject<Block> SOUL_FORGE = BLOCKS.register("soulforge", BlockSoulForge::new);
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
//	public static final RegistryObject<BloodstoneBlock> BLOOD_STONE = registerNoItem("blood_stone", () -> new BloodstoneBlock());
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
