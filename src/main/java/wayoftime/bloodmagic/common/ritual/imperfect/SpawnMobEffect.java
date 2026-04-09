package wayoftime.bloodmagic.common.ritual.imperfect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import wayoftime.bloodmagic.api.ritual.ImperfectRitualEffect;

import java.util.List;
import java.util.Optional;

public record SpawnMobEffect(int cost, Holder<EntityType<?>> entityType, Optional<List<AttributeData>> attributes, Optional<List<MobEffectInstance>> mobEffects, Optional<ResourceLocation> customLootTable) implements ImperfectRitualEffect {
    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public void perform(Player player, ServerLevel level, BlockPos ritualPos) {
        Entity toSpawn = entityType.value().create(level, this::addAdditional, ritualPos.above(2), MobSpawnType.TRIGGERED, false, false);
        if (toSpawn == null) {
            // TODO spawn got probably cancelled. not sure what to do there
            return;
        }

        level.addFreshEntity(toSpawn);
    }

    public void addAdditional(Entity entity) {
        if (entity instanceof Mob mob) {
            if (attributes.isPresent()) {
                for (AttributeData data : attributes.get()) {
                    AttributeInstance attribute = mob.getAttribute(data.attribute);
                    if (attribute != null) {
                        attribute.addPermanentModifier(data.modifier);
                    }
                }
            }

            if (mobEffects.isPresent()) {
                for (MobEffectInstance effect : mobEffects.get()) {
                    mob.addEffect(effect);
                }
            }

            if (customLootTable().isPresent()) {
                CompoundTag tag = new CompoundTag();
                tag.putString("DeathLootTable", customLootTable.get().toString());
                mob.readAdditionalSaveData(tag);
            }
        }
    }

    public static final MapCodec<SpawnMobEffect> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Codec.INT.fieldOf("cost").forGetter(SpawnMobEffect::cost),
            BuiltInRegistries.ENTITY_TYPE.holderByNameCodec().fieldOf("entity_type").forGetter(SpawnMobEffect::entityType),
            AttributeData.CODEC.listOf().optionalFieldOf("attributes").forGetter(SpawnMobEffect::attributes),
            MobEffectInstance.CODEC.listOf().optionalFieldOf("mob_effects").forGetter(SpawnMobEffect::mobEffects),
            ResourceLocation.CODEC.optionalFieldOf("custom_loot_table").forGetter(SpawnMobEffect::customLootTable)
    ).apply(builder, SpawnMobEffect::new));

    @Override
    public MapCodec<? extends ImperfectRitualEffect> codec() {
        return CODEC;
    }

    public record AttributeData(Holder<Attribute> attribute, AttributeModifier modifier) {
        public static final Codec<AttributeData> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                BuiltInRegistries.ATTRIBUTE.holderByNameCodec().fieldOf("attribute").forGetter(AttributeData::attribute),
                AttributeModifier.CODEC.fieldOf("modifier").forGetter(AttributeData::modifier)
        ).apply(builder, AttributeData::new));
    }
}
