package wayoftime.bloodmagic.common.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.blockentity.HellfireForgeTile;
import wayoftime.bloodmagic.common.caps.BMCaps;
import wayoftime.bloodmagic.common.caps.IBloodRune;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.datamap.BMDataMaps;
import wayoftime.bloodmagic.common.datamap.BloodRune;
import wayoftime.bloodmagic.util.EnumRuneType;
import wayoftime.bloodmagic.util.helper.BlockEntityHelper;
import wayoftime.bloodmagic.util.helper.BlockWithItemHolder;
import wayoftime.bloodmagic.util.helper.BlockWithItemRegister;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static final BlockWithItemHolder<Block, BlockItem> CRYSTAL_CLUSTER = BASIC_REG.register("crystal_cluster", rune_properties, decoration_item_properties);
    public static final BlockWithItemHolder<Block, BlockItem> CRYSTAL_CLUSTER_BRICK = BASIC_REG.register("crystal_cluster_brick", rune_properties, decoration_item_properties);

    private static void registerBlockCapability(RegisterCapabilitiesEvent event) {
        event.registerBlock(
                BMCaps.BLOOD_RUNE,
                (level, pos, state, blockEntity, context) -> () -> {
                    List<BloodRune> runes = state.getBlockHolder().getData(BMDataMaps.BLOOD_RUNES);
                    Map<EnumRuneType, Integer> upgrades = new HashMap<>();
                    if (runes == null) {
                        return upgrades;
                    }

                    for (BloodRune rune : runes) {
                        upgrades.compute(rune.type(), (k, v) -> v == null ? rune.amount() : v + rune.amount());
                    }
                    return upgrades;
                },
                RUNE_ACCELERATION.block().get(), RUNE_SPEED.block().get(), RUNE_CHARGING.block().get(),
                RUNE_SACRIFICE.block().get(), RUNE_SELF_SACRIFICE.block().get(), RUNE_ORB.block().get(),
                RUNE_CAPACITY.block().get(), RUNE_CAPACITY_AUGMENTED.block().get(), RUNE_DISLOCATION.block().get(),
                RUNE_EFFICIENCY.block().get(),
                RUNE_2_ACCELERATION.block().get(), RUNE_2_SPEED.block().get(), RUNE_2_CHARGING.block().get(),
                RUNE_2_SACRIFICE.block().get(), RUNE_2_SELF_SACRIFICE.block().get(), RUNE_2_ORB.block().get(),
                RUNE_2_CAPACITY.block().get(), RUNE_2_CAPACITY_AUGMENTED.block().get(), RUNE_2_DISLOCATION.block().get(),
                RUNE_2_EFFICIENCY.block().get()
        );
    }

    public static void register(IEventBus modBus) {
        BASIC_BLOCKS.register(modBus);
        BASIC_BLOCK_ITEMS.register(modBus);
        BLOCKS.register(modBus);
        BLOCK_ITEMS.register(modBus);
        modBus.addListener(BMBlocks::registerBlockCapability);
    }
}
