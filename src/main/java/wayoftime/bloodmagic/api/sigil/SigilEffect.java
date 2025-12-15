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
