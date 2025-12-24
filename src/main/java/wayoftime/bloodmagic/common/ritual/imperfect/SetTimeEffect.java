package wayoftime.bloodmagic.common.ritual.imperfect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import wayoftime.bloodmagic.api.ritual.ImperfectRitualEffect;

public record SetTimeEffect(int cost, int dayTime) implements ImperfectRitualEffect {

    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public void perform(Player player, ServerLevel level, BlockPos ritualPos) {
        level.setDayTime(dayTime);
    }

    public static final MapCodec<SetTimeEffect> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Codec.INT.fieldOf("cost").forGetter(SetTimeEffect::cost),
            Codec.INT.fieldOf("day_time").forGetter(SetTimeEffect::dayTime)
    ).apply(builder, SetTimeEffect::new));

    @Override
    public MapCodec<? extends ImperfectRitualEffect> codec() {
        return CODEC;
    }
}
