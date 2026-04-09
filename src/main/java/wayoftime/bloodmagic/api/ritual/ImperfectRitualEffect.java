package wayoftime.bloodmagic.api.ritual;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import wayoftime.bloodmagic.common.ritual.imperfect.ImperfectRitualEffects;

import java.util.function.Function;

// TODO should probably define cost here and have it be part of this codec, but not sure how to do that
public interface ImperfectRitualEffect {
    Codec<ImperfectRitualEffect> CODEC = Codec.lazyInitialized(() -> ImperfectRitualEffects.IMPERFECT_RITUAL_EFFECT_TYPE
            .getRegistry()
            .get()
            .byNameCodec()
            .dispatch(ImperfectRitualEffect::codec, Function.identity())
    );

    int getCost();

    void perform(Player player, ServerLevel level, BlockPos ritualPos);

    MapCodec<? extends ImperfectRitualEffect> codec();
}
