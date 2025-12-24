package wayoftime.bloodmagic.common.living;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.logging.log4j.util.TriConsumer;
import wayoftime.bloodmagic.api.BMIdentifiers;
import wayoftime.bloodmagic.api.living.LivingValueEffect;
import wayoftime.bloodmagic.common.living.effects.AttributeEffect;
import wayoftime.bloodmagic.common.living.effects.ConditionalEffect;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public record LivingUpgrade(Levels levels, DataComponentMap effects) {
    public static final Codec<LivingUpgrade> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Levels.CODEC.fieldOf("levels").forGetter(LivingUpgrade::levels),
            LivingEffectComponents.CODEC.fieldOf("effects").forGetter(LivingUpgrade::effects)
    ).apply(builder, LivingUpgrade::new));

    public static final Codec<LivingUpgrade> CLIENT_CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Levels.CODEC.fieldOf("levels").forGetter(LivingUpgrade::levels),
            Codec.unit(DataComponentMap.EMPTY).fieldOf("effects").forGetter(LivingUpgrade::effects)
    ).apply(builder, LivingUpgrade::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<LivingUpgrade>> HOLDER_STREAM_CODEC = ByteBufCodecs.holderRegistry(BMIdentifiers.RegistryKeys.LIVING_UPGRADES);

    public static String descriptionId(ResourceKey<LivingUpgrade> key) {
        return Util.makeDescriptionId("living_upgrade", key.location());
    }

    public static final Codec<Holder<LivingUpgrade>> HOLDER_CODEC = RegistryFixedCodec.create(BMIdentifiers.RegistryKeys.LIVING_UPGRADES);

    public void modifyKnockback(Integer level, LivingEntity victim, DamageSource source, MutableFloat mutablefloat) {
        modifyFilteredDamageValue(LivingEffectComponents.KNOCKBACK.get(), level, victim, source, mutablefloat);
    }

    private void modifyFilteredDamageValue(DataComponentType<List<ConditionalEffect<LivingValueEffect>>> type, Integer level, LivingEntity victim, DamageSource source, MutableFloat mutablefloat) {
        LootContext context = LivingContextParamSets.damageBased(victim, source, level);
        applyEffects(
                getEffects(type),
                context,
                effect -> mutablefloat.setValue(effect.process(level, context, mutablefloat.floatValue()))
        );
    }

    public void modifyExperience(Integer level, Player player, MutableFloat value) {
        modifyFilteredValue(LivingEffectComponents.EXP_PICKUP.get(), level, player, value);
    }

    private void modifyFilteredValue(DataComponentType<List<ConditionalEffect<LivingValueEffect>>> type, int level, Player player, MutableFloat value) {
        LootContext context = LivingContextParamSets.tick(player, level);
        applyEffects(
                getEffects(type),
                context,
                effect -> value.setValue(effect.process(level, context, value.getValue()))
        );
    }

    public static <T> void applyEffects(List<ConditionalEffect<T>> effects, LootContext context, Consumer<T> applier) {
        for (ConditionalEffect<T> conditionaleffect : effects) {
            if (conditionaleffect.matches(context)) {
                applier.accept(conditionaleffect.effect());
            }
        }
    }

    public void modifyHealing(Integer level, Player player, MutableFloat value) {
        modifyFilteredValue(LivingEffectComponents.HEALING.get(), level, player, value);
    }

    public void modifyDamageDealt(Integer level, LivingEntity victim, DamageSource source, MutableFloat value) {
        modifyFilteredDamageValue(LivingEffectComponents.DEALING_DAMAGE.get(), level, victim, source, value);
    }

    public void modifyDamageTaken(Integer level, LivingEntity victim, DamageSource source, MutableFloat value) {
        modifyFilteredDamageValue(LivingEffectComponents.TAKING_DAMAGE.get(), level, victim, source, value);
    }

    public void reactToDamageDealt(Integer level, LivingEntity victim, DamageSource source, float damage) {
        modifyFilteredDamageValue(LivingEffectComponents.DAMAGE_DEALT_EXP.get(), level, victim, source, new MutableFloat(damage));
    }

    public void reactToDamageTaken(Integer level, Player victim, DamageSource source, float damage) {
        modifyFilteredDamageValue(LivingEffectComponents.DAMAGE_TAKEN_EXP.get(), level, victim, source, new MutableFloat(damage));
    }

    public void blockBroken(Integer level, Player player, BlockState state) {
        applyEffects(
                getEffects(LivingEffectComponents.BREAK_BLOCK.get()),
                LivingContextParamSets.breakBlock(player, state, player.getItemInHand(InteractionHand.MAIN_HAND), level),
                effect -> effect.apply(level, player)
        );
    }

    public void tick(Integer level, Player player) {
        applyEffects(
                getEffects(LivingEffectComponents.TICK.get()),
                LivingContextParamSets.tick(player, level),
                effect -> effect.apply(level, player)
        );
    }

    public void modifyProjectile(Integer level, Player player, Projectile projectile) {
        applyEffects(
                getEffects(LivingEffectComponents.PROJECTILE_SHOT.get()),
                LivingContextParamSets.projectile(player, projectile, level),
                effect -> effect.apply(level, projectile)
        );
    }

    public void collectAttributes(Integer level, TriConsumer<Holder<Attribute>, AttributeModifier, EquipmentSlotGroup> consumer) {
        for (AttributeEffect effect : getEffects(LivingEffectComponents.ATTRIBUTES.get())) {
            effect.getModifier(level, consumer);
        }
    }

    public record Levels(TreeMap<Integer, Integer> expToLevel, Map<Integer, Integer> levelToCost) {
        private static final Codec<Integer> INT_STRING = Codec.STRING.comapFlatMap(str -> {
           try {
               return DataResult.success(Integer.valueOf(str));
           } catch (NumberFormatException ex) {
               return DataResult.error(() -> str + "is not an integer");
           }
        }, i -> i.toString());
        public static final Codec<Levels> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                Codec.unboundedMap(INT_STRING, Codec.INT).xmap(TreeMap::new, Function.identity()).fieldOf("exp_to_level").forGetter(Levels::expToLevel),
                Codec.unboundedMap(INT_STRING, Codec.INT).fieldOf("level_to_cost").forGetter(Levels::levelToCost)
        ).apply(builder, Levels::new));
    }

    public <T> List<T> getEffects(DataComponentType<List<T>> component) {
        return this.effects.getOrDefault(component, List.of());
    }

    public static class Builder {
        private final Map<DataComponentType<?>, List<?>> effectLists = new HashMap<>();
        private final DataComponentMap.Builder effectMapBuilder = DataComponentMap.builder();
        private final TreeMap<Integer, Integer> expToLevel = new TreeMap<>();
        private final Map<Integer, Integer> levelToCost = new HashMap<>();
        private int level = 1;

        public Builder level(int xpNeeded, int cost) {
            expToLevel.put(xpNeeded, level);
            levelToCost.put(level, cost);
            level++;
            return this;
        }

        public Builder withEffect(DataComponentType<List<AttributeEffect>> type, AttributeEffect effect) {
            getEffectsList(type).add(effect);
            return this;
        }

        public Builder withEffect(DataComponentType<Unit> type) {
            effectMapBuilder.set(type, Unit.INSTANCE);
            return this;
        }

        public <E> Builder withEffect(DataComponentType<List<ConditionalEffect<E>>> type, E effect, LootItemCondition.Builder condition) {
            getEffectsList(type).add(new ConditionalEffect<>(effect, Optional.of(condition.build())));
            return this;
        }

        public <E> Builder withEffect(DataComponentType<List<ConditionalEffect<E>>> type, E effect) {
            getEffectsList(type).add(new ConditionalEffect<>(effect, Optional.empty()));
            return this;
        }

        public LivingUpgrade build() {
            return new LivingUpgrade(new Levels(expToLevel, levelToCost), effectMapBuilder.build());
        }

        private <E> List<E> getEffectsList(DataComponentType<List<E>> componentType) {
            return (List<E>) this.effectLists.computeIfAbsent(componentType, p_346247_ -> {
                ArrayList<E> arraylist = new ArrayList<>();
                this.effectMapBuilder.set(componentType, arraylist);
                return arraylist;
            });
        }
    }
}
