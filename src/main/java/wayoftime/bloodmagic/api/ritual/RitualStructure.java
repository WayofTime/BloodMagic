package wayoftime.bloodmagic.api.ritual;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.function.Predicate;

public record RitualStructure(List<Pair<BlockPos, Block>> positions) {
    public static final Codec<RitualStructure> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.pair(BlockPos.CODEC, BuiltInRegistries.BLOCK.byNameCodec()).listOf().fieldOf("positions").forGetter(RitualStructure::positions)
    ).apply(builder, RitualStructure::new));

    public static class Tester implements Predicate<RitualStructure> {

        @Override
        public boolean test(RitualStructure ritualStructure) {
            return false;
        }
    }
}
