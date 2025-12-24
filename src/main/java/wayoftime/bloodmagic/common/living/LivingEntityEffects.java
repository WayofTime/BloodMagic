package wayoftime.bloodmagic.common.living;

import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.BMIdentifiers;
import wayoftime.bloodmagic.api.living.LivingEntityEffect;
import wayoftime.bloodmagic.common.living.effects.*;

import java.util.function.Supplier;

public class LivingEntityEffects {
    public static final DeferredRegister<MapCodec<? extends LivingEntityEffect>> ENTITY_EFFECT_TYPE = DeferredRegister.create(BMIdentifiers.RegistryKeys.ENTITY_EFFECT_TYPE, BloodMagic.MODID);

    public static final Supplier<MapCodec<CooldownEffect>> COOLDOWN = ENTITY_EFFECT_TYPE.register("cooldown", () -> CooldownEffect.CODEC);
    public static final Supplier<MapCodec<ResetCooldownEffect>> RESET_COOLDOWN = ENTITY_EFFECT_TYPE.register("reset_cooldown", () -> ResetCooldownEffect.CODEC);
    public static final Supplier<MapCodec<MovementModifier>> MOVEMENT_MODIFIER = ENTITY_EFFECT_TYPE.register("movement_modifier", () -> MovementModifier.CODEC);
    public static final Supplier<MapCodec<AddMobEffect>> ADD_MOB_EFFECT = ENTITY_EFFECT_TYPE.register("add_mob_effect", () -> AddMobEffect.CODEC);
    public static final Supplier<MapCodec<RemoveMobEffect>> REMOVE_MOB_EFFECT = ENTITY_EFFECT_TYPE.register("remove_mob_effect", () -> RemoveMobEffect.CODEC);
    public static final Supplier<MapCodec<RandomArmourDamageEffect>> RANDOM_ARMOUR_DAMAGE = ENTITY_EFFECT_TYPE.register("random_armour_damage", () -> RandomArmourDamageEffect.CODEC);
    public static final Supplier<MapCodec<CauseExhaustionEffect>> EXHAUST = ENTITY_EFFECT_TYPE.register("exhaust", () -> CauseExhaustionEffect.CODEC);

    public static final Supplier<MapCodec<EntityBasedExp>> ENTITY_BASED_EXP = ENTITY_EFFECT_TYPE.register("living_exp", () -> EntityBasedExp.CODEC);
    public static final Supplier<MapCodec<DistanceExpGain>> DISTANCE_EXP = ENTITY_EFFECT_TYPE.register("distance_living_exp", () -> DistanceExpGain.CODEC);
    public static final Supplier<MapCodec<EatingExpEffect>> EATING_EXP = ENTITY_EFFECT_TYPE.register("eating_living_exp", () -> EatingExpEffect.CODEC);
    public static final Supplier<MapCodec<ItemDamageBasedExpGain>> REPAIR_EXP = ENTITY_EFFECT_TYPE.register("repairing_living_exp", () -> ItemDamageBasedExpGain.CODEC);

    public static void register(IEventBus modBus) {
        ENTITY_EFFECT_TYPE.makeRegistry(builder -> {});
        ENTITY_EFFECT_TYPE.register(modBus);
    }

}
