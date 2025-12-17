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
import wayoftime.bloodmagic.common.sigil.SigilEffects;

import java.util.function.Function;

public interface SigilEffect {
    Codec<SigilEffect> CODEC = Codec.lazyInitialized(() -> SigilEffects.SIGIL_EFFECT_TYPE
            .getRegistry()
            .get()
            .byNameCodec()
            .dispatch(SigilEffect::codec, Function.identity())
    );

    MapCodec<? extends SigilEffect> codec();

    default String getActiveTooltip(boolean isActive) {
        return "tooltip.bloodmagic.sigil." + (isActive ? "activated" : "deactivated");
    }

    default boolean isActivatable() {
        return false;
    }

    default int activeTick(ItemStack sigil, Level level, Player player) {
        return 0;
    }

    default int useOnAir(ItemStack sigil, Player player, InteractionHand usedHand) {
        return 0;
    }

    default int useOnBlock(ItemStack sigil, Player player, UseOnContext context) {
        return 0;
    }

    default int useOnEntity(ItemStack sigil, Player player, LivingEntity target) {
        return 0;
    }

    // TODO implement these. Need Arrays first and figure out the "rules" for them
    default int arrayTick(ItemStack sigil, Level level, BlockPos arrayPos) {
        return 0;
    }

    default boolean hasArrayEffect() {
        return false;
    }
}
