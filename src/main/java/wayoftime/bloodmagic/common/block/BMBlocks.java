package wayoftime.bloodmagic.common.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.item.block.ItemBlockAlchemyTable;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.util.helper.BlockEntityHelper;
import wayoftime.bloodmagic.util.helper.BlockWithItemHolder;
import wayoftime.bloodmagic.util.helper.BlockWithItemRegister;

import java.util.List;
import java.util.function.Supplier;

public class BMBlocks {
    public static final DeferredRegister<Block> BASIC_BLOCKS = DeferredRegister.createBlocks(BloodMagic.MODID);
    public static final DeferredRegister<Item> BASIC_BLOCK_ITEMS = DeferredRegister.createItems(BloodMagic.MODID);
    public static final BlockWithItemRegister BASIC_REG = new BlockWithItemRegister(BASIC_BLOCKS, BASIC_BLOCK_ITEMS);

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.createBlocks(BloodMagic.MODID);
    public static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.createItems(BloodMagic.MODID);
    public static final BlockWithItemRegister BLOCK_REG = new BlockWithItemRegister(BLOCKS, BLOCK_ITEMS);

    public static final BlockWithItemHolder<BloodAltarBlock, BlockItem> BLOOD_ALTAR = BLOCK_REG.register("blood_altar", BloodAltarBlock::new);
    public static final BlockWithItemHolder<BloodTankBlock, BlockItem> BLOOD_TANK = BLOCK_REG.register("blood_tank", BloodTankBlock::new, block -> new BlockItem(block, new Item.Properties().component(BMDataComponents.CONTAINER_TIER, 1)));
    public static final BlockWithItemHolder<HellfireForgeBlock, BlockItem> HELLFIRE_FORGE = BLOCK_REG.register("hellfire_forge", HellfireForgeBlock::new);
    public static final BlockWithItemHolder<ARCBlock, BlockItem> ARC_BLOCK = BLOCK_REG.register("arc", ARCBlock::new);
    public static final BlockWithItemHolder<TeleposerBlock, BlockItem> TELEPOSER = BLOCK_REG.register("teleposer", TeleposerBlock::new);

    // TODO add model/textures for this and change registry to BASIC_REG
    public static final BlockWithItemHolder<LivingStationBlock, BlockItem> LIVING_STATION = BLOCK_REG.register("living_station", LivingStationBlock::new);

    private static final BlockBehaviour.Properties rune_properties = BlockBehaviour.Properties.of().strength(2.0F, 5.0F).sound(SoundType.STONE).requiresCorrectToolForDrops();
    private static final ItemLore save_decoration = new ItemLore(List.of(BlockEntityHelper.translatableHover("tooltip.bloodmagic.save_for_decoration").withStyle(ChatFormatting.ITALIC)));
    private static final Item.Properties decoration_item_properties = new Item.Properties().component(DataComponents.LORE, save_decoration);

    public static final BlockWithItemHolder<Block, BlockItem> RUNE_BLANK = BASIC_REG.register("rune_blank", rune_properties, decoration_item_properties);

    public static final BlockWithItemHolder<Block, BlockItem> RUNE_SACRIFICE = BASIC_REG.register("rune_sacrifice", rune_properties, decoration_item_properties);
    public static final BlockWithItemHolder<Block, BlockItem> RUNE_SELF_SACRIFICE = BASIC_REG.register("rune_sacrifice_self", rune_properties, decoration_item_properties);
    public static final BlockWithItemHolder<Block, BlockItem> RUNE_CAPACITY = BASIC_REG.register("rune_capacity", rune_properties, decoration_item_properties);
    public static final BlockWithItemHolder<Block, BlockItem> RUNE_CAPACITY_AUGMENTED = BASIC_REG.register("rune_capacity_augmented", rune_properties, decoration_item_properties);
    public static final BlockWithItemHolder<Block, BlockItem> RUNE_CHARGING = BASIC_REG.register("rune_charging", rune_properties, decoration_item_properties);
    public static final BlockWithItemHolder<Block, BlockItem> RUNE_SPEED = BASIC_REG.register("rune_speed", rune_properties, decoration_item_properties);
    public static final BlockWithItemHolder<Block, BlockItem> RUNE_ACCELERATION = BASIC_REG.register("rune_acceleration", rune_properties, decoration_item_properties);
    public static final BlockWithItemHolder<Block, BlockItem> RUNE_DISLOCATION = BASIC_REG.register("rune_dislocation", rune_properties, decoration_item_properties);
    public static final BlockWithItemHolder<Block, BlockItem> RUNE_ORB = BASIC_REG.register("rune_orb", rune_properties, decoration_item_properties);
    public static final BlockWithItemHolder<Block, BlockItem> RUNE_EFFICIENCY = BASIC_REG.register("rune_efficiency", rune_properties, decoration_item_properties);

    public static final BlockWithItemHolder<Block, BlockItem> RUNE_2_SACRIFICE = BASIC_REG.register("rune_2_sacrifice", rune_properties, decoration_item_properties);
    public static final BlockWithItemHolder<Block, BlockItem> RUNE_2_SELF_SACRIFICE = BASIC_REG.register("rune_2_sacrifice_self", rune_properties, decoration_item_properties);
    public static final BlockWithItemHolder<Block, BlockItem> RUNE_2_CAPACITY = BASIC_REG.register("rune_2_capacity", rune_properties, decoration_item_properties);
    public static final BlockWithItemHolder<Block, BlockItem> RUNE_2_CAPACITY_AUGMENTED = BASIC_REG.register("rune_2_capacity_augmented", rune_properties, decoration_item_properties);
    public static final BlockWithItemHolder<Block, BlockItem> RUNE_2_CHARGING = BASIC_REG.register("rune_2_charging", rune_properties, decoration_item_properties);
    public static final BlockWithItemHolder<Block, BlockItem> RUNE_2_SPEED = BASIC_REG.register("rune_2_speed", rune_properties, decoration_item_properties);
    public static final BlockWithItemHolder<Block, BlockItem> RUNE_2_ACCELERATION = BASIC_REG.register("rune_2_acceleration", rune_properties, decoration_item_properties);
    public static final BlockWithItemHolder<Block, BlockItem> RUNE_2_DISLOCATION = BASIC_REG.register("rune_2_dislocation", rune_properties, decoration_item_properties);
    public static final BlockWithItemHolder<Block, BlockItem> RUNE_2_ORB = BASIC_REG.register("rune_2_orb", rune_properties, decoration_item_properties);
    public static final BlockWithItemHolder<Block, BlockItem> RUNE_2_EFFICIENCY = BASIC_REG.register("rune_2_efficiency", rune_properties, decoration_item_properties);

    public static final BlockWithItemHolder<Block, BlockItem> BLOODSTONE = BASIC_REG.register("bloodstone", rune_properties, decoration_item_properties);
    public static final BlockWithItemHolder<Block, BlockItem> BLOODSTONE_BRICK = BASIC_REG.register("bloodstone_brick", rune_properties, decoration_item_properties);

    public static final BlockWithItemHolder<Block, BlockItem> HELLFORGED_BLOCK = BASIC_REG.register("hellforged_block", BlockBehaviour.Properties.of().strength(5, 6).sound(SoundType.METAL).requiresCorrectToolForDrops(), new Item.Properties());

    public static final BlockWithItemHolder<Block, BlockItem> RAW_DEMONITE_BLOCK = BASIC_REG.register("rawdemoniteblock", BlockBehaviour.Properties.of().strength(5, 6).sound(SoundType.METAL).requiresCorrectToolForDrops(), new Item.Properties());
    public static final BlockWithItemHolder<Block, BlockItem> CRYSTAL_CLUSTER = BASIC_REG.register("crystal_cluster", rune_properties, decoration_item_properties);
    public static final BlockWithItemHolder<Block, BlockItem> CRYSTAL_CLUSTER_BRICK = BASIC_REG.register("crystal_cluster_brick", rune_properties, decoration_item_properties);

    // Alchemy Array - placed by Arcane Ashes, no block item needed
    public static final DeferredHolder<Block, AlchemyArrayBlock> ALCHEMY_ARRAY = BLOCKS.register("alchemyarray", (Supplier<AlchemyArrayBlock>) AlchemyArrayBlock::new);

    // Alchemy Table
    public static final BlockWithItemHolder<AlchemyTableBlock, ItemBlockAlchemyTable> ALCHEMY_TABLE = BLOCK_REG.register("alchemytable", AlchemyTableBlock::new, block -> new ItemBlockAlchemyTable(block, new Item.Properties()));

    // Tau Blocks (TODO: Implement crop-like growth mechanics - these are placeholder blocks for now)
    private static final BlockBehaviour.Properties tau_properties = BlockBehaviour.Properties.of().noCollission().instabreak().sound(SoundType.CROP).pushReaction(PushReaction.DESTROY);
    public static final BlockWithItemHolder<Block, BlockItem> WEAK_TAU = BASIC_REG.register("weak_tau", tau_properties, new Item.Properties());
    public static final BlockWithItemHolder<Block, BlockItem> STRONG_TAU = BASIC_REG.register("strong_tau", tau_properties, new Item.Properties());

    // Ritual Stones (TODO: Full ritual system implementation pending)
    public static final BlockWithItemHolder<BlockRitualStone, BlockItem> BLANK_RITUAL_STONE = BLOCK_REG.register("ritualstone", () -> new BlockRitualStone(EnumRuneType.BLANK));
    public static final BlockWithItemHolder<BlockRitualStone, BlockItem> AIR_RITUAL_STONE = BLOCK_REG.register("airritualstone", () -> new BlockRitualStone(EnumRuneType.AIR));
    public static final BlockWithItemHolder<BlockRitualStone, BlockItem> WATER_RITUAL_STONE = BLOCK_REG.register("waterritualstone", () -> new BlockRitualStone(EnumRuneType.WATER));
    public static final BlockWithItemHolder<BlockRitualStone, BlockItem> FIRE_RITUAL_STONE = BLOCK_REG.register("fireritualstone", () -> new BlockRitualStone(EnumRuneType.FIRE));
    public static final BlockWithItemHolder<BlockRitualStone, BlockItem> EARTH_RITUAL_STONE = BLOCK_REG.register("earthritualstone", () -> new BlockRitualStone(EnumRuneType.EARTH));
    public static final BlockWithItemHolder<BlockRitualStone, BlockItem> DUSK_RITUAL_STONE = BLOCK_REG.register("duskritualstone", () -> new BlockRitualStone(EnumRuneType.DUSK));
    public static final BlockWithItemHolder<BlockRitualStone, BlockItem> DAWN_RITUAL_STONE = BLOCK_REG.register("lightritualstone", () -> new BlockRitualStone(EnumRuneType.DAWN));
    public static final BlockWithItemHolder<BlockMasterRitualStone, BlockItem> MASTER_RITUAL_STONE = BLOCK_REG.register("masterritualstone", () -> new BlockMasterRitualStone(false));

    // Demon Will Blocks (placeholder - functionality to be added later)
    private static final BlockBehaviour.Properties demon_block_properties = BlockBehaviour.Properties.of().strength(5.0F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops();
    public static final BlockWithItemHolder<Block, BlockItem> DEMON_CRUCIBLE = BLOCK_REG.register("demoncrucible", demon_block_properties, new Item.Properties());
    public static final BlockWithItemHolder<Block, BlockItem> DEMON_CRYSTALLIZER = BLOCK_REG.register("demoncrystallizer", demon_block_properties, new Item.Properties());
    public static final BlockWithItemHolder<Block, BlockItem> DEMON_PYLON = BLOCK_REG.register("demonpylon", demon_block_properties, new Item.Properties());

    // Crystal Blocks (placeholder)
    private static final BlockBehaviour.Properties crystal_block_properties = BlockBehaviour.Properties.of().strength(3.0F, 3.0F).sound(SoundType.AMETHYST).requiresCorrectToolForDrops().lightLevel(state -> 7);
    public static final BlockWithItemHolder<Block, BlockItem> RAW_CRYSTAL_BLOCK = BASIC_REG.register("rawdemoncrystal", crystal_block_properties, new Item.Properties());
    public static final BlockWithItemHolder<Block, BlockItem> CORROSIVE_CRYSTAL_BLOCK = BASIC_REG.register("corrosivedemoncrystal", crystal_block_properties, new Item.Properties());
    public static final BlockWithItemHolder<Block, BlockItem> DESTRUCTIVE_CRYSTAL_BLOCK = BASIC_REG.register("destructivedemoncrystal", crystal_block_properties, new Item.Properties());
    public static final BlockWithItemHolder<Block, BlockItem> VENGEFUL_CRYSTAL_BLOCK = BASIC_REG.register("vengefuldemoncrystal", crystal_block_properties, new Item.Properties());
    public static final BlockWithItemHolder<Block, BlockItem> STEADFAST_CRYSTAL_BLOCK = BASIC_REG.register("steadfastdemoncrystal", crystal_block_properties, new Item.Properties());

    // Routing Nodes (placeholder)
    private static final BlockBehaviour.Properties routing_node_properties = BlockBehaviour.Properties.of().strength(2.0F, 5.0F).sound(SoundType.METAL).requiresCorrectToolForDrops();
    public static final BlockWithItemHolder<Block, BlockItem> ROUTING_NODE = BLOCK_REG.register("itemroutingnode", routing_node_properties, new Item.Properties());
    public static final BlockWithItemHolder<Block, BlockItem> INPUT_ROUTING_NODE = BLOCK_REG.register("inputroutingnode", routing_node_properties, new Item.Properties());
    public static final BlockWithItemHolder<Block, BlockItem> OUTPUT_ROUTING_NODE = BLOCK_REG.register("outputroutingnode", routing_node_properties, new Item.Properties());
    public static final BlockWithItemHolder<Block, BlockItem> MASTER_ROUTING_NODE = BLOCK_REG.register("masterroutingnode", routing_node_properties, new Item.Properties());

    // Explosive Charges (placeholder - functionality to be added later)
    private static final BlockBehaviour.Properties charge_properties = BlockBehaviour.Properties.of().strength(2.0F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops();
    public static final BlockWithItemHolder<Block, BlockItem> SHAPED_CHARGE = BASIC_REG.register("shaped_charge", charge_properties, new Item.Properties());
    public static final BlockWithItemHolder<Block, BlockItem> DEFORESTER_CHARGE = BASIC_REG.register("deforester_charge", charge_properties, new Item.Properties());
    public static final BlockWithItemHolder<Block, BlockItem> VEINMINE_CHARGE = BASIC_REG.register("veinmine_charge", charge_properties, new Item.Properties());
    public static final BlockWithItemHolder<Block, BlockItem> FUNGAL_CHARGE = BASIC_REG.register("fungal_charge", charge_properties, new Item.Properties());
    public static final BlockWithItemHolder<Block, BlockItem> AUG_SHAPED_CHARGE = BASIC_REG.register("aug_shaped_charge", charge_properties, new Item.Properties());
    public static final BlockWithItemHolder<Block, BlockItem> DEFORESTER_CHARGE_2 = BASIC_REG.register("deforester_charge_2", charge_properties, new Item.Properties());
    public static final BlockWithItemHolder<Block, BlockItem> VEINMINE_CHARGE_2 = BASIC_REG.register("veinmine_charge_2", charge_properties, new Item.Properties());
    public static final BlockWithItemHolder<Block, BlockItem> FUNGAL_CHARGE_2 = BASIC_REG.register("fungal_charge_2", charge_properties, new Item.Properties());
    public static final BlockWithItemHolder<Block, BlockItem> SHAPED_CHARGE_DEEP = BASIC_REG.register("shaped_charge_deep", charge_properties, new Item.Properties());

    public static void register(IEventBus modBus) {
        BASIC_BLOCKS.register(modBus);
        BASIC_BLOCK_ITEMS.register(modBus);
        BLOCKS.register(modBus);
        BLOCK_ITEMS.register(modBus);
    }
}
