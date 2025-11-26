package wayoftime.bloodmagic.common.datacomponent;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.living.LivingUpgrade;
import wayoftime.bloodmagic.common.registry.BMRegistries;

import java.util.function.Function;
import java.util.function.Supplier;

public class BMDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, BloodMagic.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Binding>> BINDING = DATA_COMPONENTS.registerComponentType("binding", builder -> builder.persistent(Binding.BASIC_CODEC).networkSynchronized(Binding.STREAM_CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> INCENSE = DATA_COMPONENTS.registerComponentType("incense", builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Double>> DEMON_WILL_AMOUNT = DATA_COMPONENTS.registerComponentType("will_amount", builder -> builder.persistent(Codec.DOUBLE).networkSynchronized(ByteBufCodecs.DOUBLE));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<EnumWillType>> DEMON_WILL_TYPE = DATA_COMPONENTS.registerComponentType("will_type", builder -> builder.persistent(EnumWillType.CODEC).networkSynchronized(EnumWillType.STREAM_CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Double>> ARC_CHANCE = DATA_COMPONENTS.registerComponentType("arc_chance", builder -> builder.persistent(Codec.DOUBLE));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Double>> ARC_SPEED = DATA_COMPONENTS.registerComponentType("arc_speed", builder -> builder.persistent(Codec.DOUBLE));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<GlobalPos>> STORED_POSITION = DATA_COMPONENTS.registerComponentType("stored_position", builder -> builder.persistent(GlobalPos.CODEC).networkSynchronized(GlobalPos.STREAM_CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> CONTAINER_TIER = DATA_COMPONENTS.registerComponentType("container_tier", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SimpleFluidContent>> FLUID_CONTENT = DATA_COMPONENTS.registerComponentType("fluid_content", builder -> builder.persistent(SimpleFluidContent.CODEC).networkSynchronized(SimpleFluidContent.STREAM_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> ENERGY_CONTENT = DATA_COMPONENTS.registerComponentType("energy_content", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<TagKey<Item>>> REQUIRED_SET = DATA_COMPONENTS.registerComponentType("required_set", builder -> builder.persistent(TagKey.codec(Registries.ITEM)));

    public static final Codec<Object2FloatOpenHashMap<Holder<LivingUpgrade>>> UPGRADE_HOLDER_CODEC = Codec.unboundedMap(RegistryFixedCodec.create(BMRegistries.Keys.LIVING_UPGRADES), Codec.FLOAT).xmap(Object2FloatOpenHashMap::new, Function.identity());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UpgradeLimits>> LIMITS = DATA_COMPONENTS.registerComponentType("limits", builder -> builder.persistent(UpgradeLimits.CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<LivingStats>> UPGRADES = DATA_COMPONENTS.registerComponentType("upgrades", builder -> builder.persistent(LivingStats.CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> CURRENT_MAX_UPGRADE_POINTS = DATA_COMPONENTS.registerComponentType("max_upgrade_points", builder -> builder.persistent(Codec.INT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> CURRENT_UPGRADE_POINTS = DATA_COMPONENTS.registerComponentType("current_upgrade_points", builder -> builder.persistent(Codec.INT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> FULL_SET_MARKER = DATA_COMPONENTS.registerComponentType("full_set_marker", builder -> builder.persistent(Codec.BOOL));
    // doesnt work with stream codec, doesnt work without
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UpgradeTome>> UPGRADE_TOME_DATA = DATA_COMPONENTS.registerComponentType("upgrade_tome_data", builder -> builder.persistent(UpgradeTome.CODEC).networkSynchronized(UpgradeTome.STREAM_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Object2FloatOpenHashMap<Holder<LivingUpgrade>>>> STORED_UPGRADES = DATA_COMPONENTS.registerComponentType("stored_upgrades", builder -> builder.persistent(UPGRADE_HOLDER_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> UPGRADE_SCRAP = DATA_COMPONENTS.registerComponentType("upgrade_scrap", builder -> builder.persistent(Codec.INT));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> PREVIOUS_DAMAGE = DATA_COMPONENTS.registerComponentType("previous_damage", builder -> builder.persistent(Codec.INT));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemContainerContents>> FILTER_INVENTORY = DATA_COMPONENTS.registerComponentType("filter_inventory", builder -> builder.persistent(ItemContainerContents.CODEC).networkSynchronized(ItemContainerContents.STREAM_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> GHOST_SLOT = DATA_COMPONENTS.registerComponentType("ghost_slot", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> FILTER_BLACKWHITELIST = DATA_COMPONENTS.registerComponentType("filter_blackwhitelist", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> FILTER_ENCHANT_LEVEL = DATA_COMPONENTS.registerComponentType("filter_enchant_level", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> FILTER_ENCHANT_INDEX = DATA_COMPONENTS.registerComponentType("filter_enchant_index", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> FILTER_TAG_INDEX = DATA_COMPONENTS.registerComponentType("filter_tag_index", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<TagKey<Item>>> FILTER_TAG = DATA_COMPONENTS.registerComponentType("filter_tag", builder -> builder.persistent(TagKey.codec(Registries.ITEM)).networkSynchronized(ByteBufCodecs.fromCodecWithRegistries(TagKey.codec(Registries.ITEM))));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> GHOST_AMOUNT = DATA_COMPONENTS.registerComponentType("ghost_amount", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT));

    public static void register(IEventBus modBus) {
        DATA_COMPONENTS.register(modBus);
    }
}
