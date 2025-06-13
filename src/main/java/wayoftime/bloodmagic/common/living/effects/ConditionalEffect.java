package wayoftime.bloodmagic.common.living.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;

public record ConditionalEffect<T>(T effect, Optional<LootItemCondition> requirements) {
    public static Codec<LootItemCondition> conditionCodec(LootContextParamSet params) {
        return LootItemCondition.DIRECT_CODEC
                .validate(
                        result -> {
                            ProblemReporter.Collector problemReporter = new ProblemReporter.Collector();
                            ValidationContext validationcontext = new ValidationContext(problemReporter, params);
                            result.validate(validationcontext);
                            return problemReporter.getReport()
                                    .map(report -> DataResult.<LootItemCondition>error(() -> "Validation error in living effect condition: " + report))
                                    .orElseGet(() -> DataResult.success(result));
                        }
                );
    }

    public static <T> Codec<ConditionalEffect<T>> codec(Codec<T> codec, LootContextParamSet params) {
        return RecordCodecBuilder.create(
                builder -> builder.group(
                                codec.fieldOf("effect").forGetter(ConditionalEffect::effect),
                                conditionCodec(params).optionalFieldOf("requirements").forGetter(ConditionalEffect::requirements)
                        )
                        .apply(builder, ConditionalEffect::new)
        );
    }

    public boolean matches(LootContext context) {
        return this.requirements.isEmpty() ? true : this.requirements.get().test(context);
    }
}
