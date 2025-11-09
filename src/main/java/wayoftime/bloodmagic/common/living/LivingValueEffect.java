package wayoftime.bloodmagic.common.living;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.storage.loot.LootContext;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.living.effects.*;
import wayoftime.bloodmagic.common.registry.BMRegistries;

import java.util.function.Function;
import java.util.function.Supplier;

public interface LivingValueEffect {
    DeferredRegister<MapCodec<? extends LivingValueEffect>> VALUE_BASED_EFFECT_TYPE = DeferredRegister.create(BMRegistries.Keys.VALUE_BASED_EFFECT_TYPE, BloodMagic.MODID);
    Codec<LivingValueEffect> CODEC = Codec.lazyInitialized(() -> VALUE_BASED_EFFECT_TYPE
            .getRegistry()
            .get()
            .byNameCodec()
            .dispatch(LivingValueEffect::codec, Function.identity())
    );

    Supplier<MapCodec<DelegateEffect>> DELEGATE = VALUE_BASED_EFFECT_TYPE.register("delegate", () -> DelegateEffect.CODEC);
    Supplier<MapCodec<MultiplyReduceValue>> MULTIPLY_REDUCTION = VALUE_BASED_EFFECT_TYPE.register("multiply_reduce", () -> MultiplyReduceValue.CODEC);
    Supplier<MapCodec<MultiplyIncreaseValue>> MULTIPLY = VALUE_BASED_EFFECT_TYPE.register("multiply_increase", () -> MultiplyIncreaseValue.CODEC);
    Supplier<MapCodec<AddValue>> ADD = VALUE_BASED_EFFECT_TYPE.register("add_value", () -> AddValue.CODEC);

    Supplier<MapCodec<ValueBasedExp>> VALUE_BASED_EXP = VALUE_BASED_EFFECT_TYPE.register("living_exp", () -> ValueBasedExp.CODEC);

    static void register(IEventBus modBus) {
        VALUE_BASED_EFFECT_TYPE.makeRegistry(builder -> {});
        VALUE_BASED_EFFECT_TYPE.register(modBus);
    }

    float process(int level, LootContext lootContext, float value);

    MapCodec<? extends LivingValueEffect> codec();
}
