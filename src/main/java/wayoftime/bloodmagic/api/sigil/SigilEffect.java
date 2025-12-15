package wayoftime.bloodmagic.api.sigil;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.BMIdentifiers;
import wayoftime.bloodmagic.common.sigil.ApplyPotionEffect;
import wayoftime.bloodmagic.common.sigil.DivinationEffect;
import wayoftime.bloodmagic.common.sigil.FluidPlaceEffect;
import wayoftime.bloodmagic.common.sigil.FluidRemoveEffect;

import java.util.function.Function;
import java.util.function.Supplier;

public interface SigilEffect {
    DeferredRegister<MapCodec<? extends SigilEffect>> SIGIL_EFFECT_TYPE = DeferredRegister.create(BMIdentifiers.RegistryKeys.SIGIL_EFFECT_TYPES, BloodMagic.MODID);
    Codec<SigilEffect> CODEC = Codec.lazyInitialized(() -> SIGIL_EFFECT_TYPE
            .getRegistry()
            .get()
            .byNameCodec()
            .dispatch(SigilEffect::codec, Function.identity())
    );

    Supplier<MapCodec<ApplyPotionEffect>> APPLY_POTION_EFFECT = SIGIL_EFFECT_TYPE.register("apply_potion_effect", () -> ApplyPotionEffect.CODEC);
    Supplier<MapCodec<DivinationEffect>> DIVINATION_EFFECT = SIGIL_EFFECT_TYPE.register("divination_effect", () -> DivinationEffect.CODEC);
    Supplier<MapCodec<FluidPlaceEffect>> FLUID_PLACE_EFFECT = SIGIL_EFFECT_TYPE.register("fluid_place_effect", () -> FluidPlaceEffect.CODEC);
    Supplier<MapCodec<FluidRemoveEffect>> FLUID_REMOVE_EFFECT = SIGIL_EFFECT_TYPE.register("fluid_remove_effect", () -> FluidRemoveEffect.CODEC);

    static void register(IEventBus modBus) {
        SIGIL_EFFECT_TYPE.makeRegistry(builder -> {});
        SIGIL_EFFECT_TYPE.register(modBus);
    }


    MapCodec<? extends SigilEffect> codec();

    default void activeTick(ItemStack sigil, Level level, Player player) {}

    default boolean useOnAir(ItemStack sigil, Player player, InteractionHand usedHand) {
        return false;
    }

    default boolean useOnBlock(ItemStack sigil, Player player, UseOnContext context) {
        return false;
    }

    default boolean useOnEntity(ItemStack sigil, Player player, LivingEntity target) {
        return false;
    }

    // TODO implement these. Need Arrays first and figure out the "rules" for them
    default boolean arrayTick(ItemStack sigil, Level level, BlockPos arrayPos) {
        return false;
    }

    default boolean hasArrayEffect() {
        return false;
    }
}
