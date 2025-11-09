package wayoftime.bloodmagic.common.living;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.util.Unit;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.living.effects.*;
import wayoftime.bloodmagic.common.registry.BMRegistries;

import java.util.List;

public class LivingEffectComponents {
    public static final DeferredRegister.DataComponents LIVING_EFFECT_COMPONENTS = DeferredRegister.createDataComponents(BMRegistries.Keys.LIVING_EFFECT_COMPONENTS, BloodMagic.MODID);
    public static final Codec<DataComponentType<?>> COMPONENT_CODEC = Codec.lazyInitialized(() -> LIVING_EFFECT_COMPONENTS.getRegistry().get().byNameCodec());
    public static final Codec<DataComponentMap> CODEC = DataComponentMap.makeCodec(COMPONENT_CODEC);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<AttributeEffect>>> ATTRIBUTES = LIVING_EFFECT_COMPONENTS.registerComponentType("attributes", builder -> builder.persistent(AttributeEffect.CODEC.codec().listOf()));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<LivingValueEffect>>>> TAKING_DAMAGE = LIVING_EFFECT_COMPONENTS.registerComponentType("taking_damage", builder -> builder.persistent(ConditionalEffect.codec(LivingValueEffect.CODEC, LivingContextParamSets.DAMAGE_BASED).listOf()));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<LivingValueEffect>>>> DEALING_DAMAGE = LIVING_EFFECT_COMPONENTS.registerComponentType("dealing_damage", builder -> builder.persistent(ConditionalEffect.codec(LivingValueEffect.CODEC, LivingContextParamSets.DAMAGE_BASED).listOf()));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<LivingValueEffect>>>> KNOCKBACK = LIVING_EFFECT_COMPONENTS.registerComponentType("knockback", builder -> builder.persistent(ConditionalEffect.codec(LivingValueEffect.CODEC, LivingContextParamSets.DAMAGE_BASED).listOf()));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<LivingValueEffect>>>> DAMAGE_TAKEN_EXP = LIVING_EFFECT_COMPONENTS.registerComponentType("damage_taken_exp", builder -> builder.persistent(ConditionalEffect.codec(LivingValueEffect.CODEC, LivingContextParamSets.DAMAGE_BASED).listOf()));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<LivingValueEffect>>>> DAMAGE_DEALT_EXP = LIVING_EFFECT_COMPONENTS.registerComponentType("damage_dealt_exp", builder -> builder.persistent(ConditionalEffect.codec(LivingValueEffect.CODEC, LivingContextParamSets.DAMAGE_BASED).listOf()));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<LivingEntityEffect>>>> BREAK_BLOCK = LIVING_EFFECT_COMPONENTS.registerComponentType("break_block", builder -> builder.persistent(ConditionalEffect.codec(LivingEntityEffect.CODEC, LivingContextParamSets.BREAK_BLOCK).listOf()));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<LivingEntityEffect>>>> TICK = LIVING_EFFECT_COMPONENTS.registerComponentType("tick", builder -> builder.persistent(ConditionalEffect.codec(LivingEntityEffect.CODEC, LivingContextParamSets.TICK).listOf()));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<LivingEntityEffect>>>> PROJECTILE_SHOT = LIVING_EFFECT_COMPONENTS.registerComponentType("eating", builder -> builder.persistent(ConditionalEffect.codec(LivingEntityEffect.CODEC, LivingContextParamSets.PROJECTILE).listOf()));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<LivingValueEffect>>>> EXP_PICKUP = LIVING_EFFECT_COMPONENTS.registerComponentType("exp_pickup", builder -> builder.persistent(ConditionalEffect.codec(LivingValueEffect.CODEC, LivingContextParamSets.TICK).listOf()));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<LivingValueEffect>>>> HEALING = LIVING_EFFECT_COMPONENTS.registerComponentType("healing", builder -> builder.persistent(ConditionalEffect.codec(LivingValueEffect.CODEC, LivingContextParamSets.TICK).listOf()));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> GILDED = LIVING_EFFECT_COMPONENTS.registerComponentType("gilded", builder -> builder.persistent(Unit.CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> ELYTRA = LIVING_EFFECT_COMPONENTS.registerComponentType("elytra", builder -> builder.persistent(Unit.CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> QUENCHED = LIVING_EFFECT_COMPONENTS.registerComponentType("quenched", builder -> builder.persistent(Unit.CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> CRIPPLED_ARM = LIVING_EFFECT_COMPONENTS.registerComponentType("crippled_arm", builder -> builder.persistent(Unit.CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> WALK_ON_POWDERED_SNOW = LIVING_EFFECT_COMPONENTS.registerComponentType("walk_on_powdered_snow", builder -> builder.persistent(Unit.CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> IS_ENDER_MASK = LIVING_EFFECT_COMPONENTS.registerComponentType("is_ender_mask", builder -> builder.persistent(Unit.CODEC));

    public static void register(IEventBus modBus) {
        LIVING_EFFECT_COMPONENTS.makeRegistry(builder -> {});
        LIVING_EFFECT_COMPONENTS.register(modBus);
    }
}
