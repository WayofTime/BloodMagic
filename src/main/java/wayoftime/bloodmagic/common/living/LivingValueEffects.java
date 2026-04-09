package wayoftime.bloodmagic.common.living;

import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.BMIdentifiers;
import wayoftime.bloodmagic.api.living.LivingValueEffect;
import wayoftime.bloodmagic.common.living.effects.*;

import java.util.function.Supplier;

public class LivingValueEffects {
    public static final DeferredRegister<MapCodec<? extends LivingValueEffect>> VALUE_BASED_EFFECT_TYPE = DeferredRegister.create(BMIdentifiers.RegistryKeys.VALUE_BASED_EFFECT_TYPE, BloodMagic.MODID);

    public static final Supplier<MapCodec<DelegateEffect>> DELEGATE = VALUE_BASED_EFFECT_TYPE.register("delegate", () -> DelegateEffect.CODEC);
    public static final Supplier<MapCodec<MultiplyReduceValue>> MULTIPLY_REDUCTION = VALUE_BASED_EFFECT_TYPE.register("multiply_reduce", () -> MultiplyReduceValue.CODEC);
    public static final Supplier<MapCodec<MultiplyIncreaseValue>> MULTIPLY = VALUE_BASED_EFFECT_TYPE.register("multiply_increase", () -> MultiplyIncreaseValue.CODEC);
    public static final Supplier<MapCodec<AddValue>> ADD = VALUE_BASED_EFFECT_TYPE.register("add_value", () -> AddValue.CODEC);

    public static final Supplier<MapCodec<ValueBasedExp>> VALUE_BASED_EXP = VALUE_BASED_EFFECT_TYPE.register("living_exp", () -> ValueBasedExp.CODEC);

    public static void register(IEventBus modBus) {
        VALUE_BASED_EFFECT_TYPE.makeRegistry(builder -> {});
        VALUE_BASED_EFFECT_TYPE.register(modBus);
    }

}
