package wayoftime.bloodmagic.common.ritual.imperfect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.registry.BMRegistries;

import java.util.function.Function;
import java.util.function.Supplier;

// TODO should probably define cost here and have it be part of this codec, but not sure how to do that
public interface ImperfectRitualEffect {
    DeferredRegister<MapCodec<? extends ImperfectRitualEffect>> IMPERFECT_RITUAL_EFFECT_TYPE = DeferredRegister.create(BMRegistries.Keys.IMPERFECT_RITUAL_EFFECT_TYPE, BloodMagic.MODID);
    Codec<ImperfectRitualEffect> CODEC = Codec.lazyInitialized(() -> IMPERFECT_RITUAL_EFFECT_TYPE
            .getRegistry()
            .get()
            .byNameCodec()
            .dispatch(ImperfectRitualEffect::codec, Function.identity())
    );

    Supplier<MapCodec<SetTimeEffect>> SET_TIME_EFFECT = IMPERFECT_RITUAL_EFFECT_TYPE.register("set_time", () -> SetTimeEffect.CODEC);
    Supplier<MapCodec<ApplyPotionEffect>> APPLY_POTION_EFFECT = IMPERFECT_RITUAL_EFFECT_TYPE.register("apply_potion", () -> ApplyPotionEffect.CODEC);
    Supplier<MapCodec<SpawnMobEffect>> SPAWN_MOB_EFFECT = IMPERFECT_RITUAL_EFFECT_TYPE.register("spawn_mob", () -> SpawnMobEffect.CODEC);

    static void register(IEventBus modBus) {
        IMPERFECT_RITUAL_EFFECT_TYPE.makeRegistry(builder -> {});
        IMPERFECT_RITUAL_EFFECT_TYPE.register(modBus);
    }

    int getCost();

    void perform(Player player, ServerLevel level, BlockPos ritualPos);

    MapCodec<? extends ImperfectRitualEffect> codec();
}
