package wayoftime.bloodmagic.common.living;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;

public class LivingContextParamSets {
    public static final LootContextParamSet DAMAGE_BASED = LootContextParamSet.builder()
            .required(LootContextParams.ORIGIN)
            .required(LootContextParams.DAMAGE_SOURCE)
            .required(LootContextParams.THIS_ENTITY)
            .required(LootContextParams.ENCHANTMENT_LEVEL)
            .optional(LootContextParams.ATTACKING_ENTITY)
            .optional(LootContextParams.DIRECT_ATTACKING_ENTITY)
            .build();

    public static final LootContextParamSet BREAK_BLOCK = LootContextParamSet.builder()
            .required(LootContextParams.ORIGIN)
            .required(LootContextParams.BLOCK_STATE)
            .required(LootContextParams.THIS_ENTITY)
            .required(LootContextParams.ENCHANTMENT_LEVEL)
            .optional(LootContextParams.TOOL)
            .build();

    public static final LootContextParamSet TICK = LootContextParamSet.builder()
            .required(LootContextParams.THIS_ENTITY)
            .required(LootContextParams.ORIGIN)
            .required(LootContextParams.ENCHANTMENT_LEVEL)
            .build();

    public static final LootContextParamSet PROJECTILE = LootContextParamSet.builder()
            .required(LootContextParams.ATTACKING_ENTITY)
            .required(LootContextParams.DIRECT_ATTACKING_ENTITY)
            .required(LootContextParams.ENCHANTMENT_LEVEL)
            .build();

    public static LootContext projectile(Player player, Projectile projectile, int level) {
        return boiler(
                builder -> builder
                        .withParameter(LootContextParams.ATTACKING_ENTITY, player)
                        .withParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, projectile)
                        .withParameter(LootContextParams.ENCHANTMENT_LEVEL, level)
                        .create(PROJECTILE), player.level()
        );
    }

    public static LootContext damageBased(Entity entity, DamageSource damageSource, int level) {
        return boiler(
                builder -> builder
                        .withParameter(LootContextParams.THIS_ENTITY, entity)
                        .withParameter(LootContextParams.ORIGIN, entity.position())
                        .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
                        .withParameter(LootContextParams.ENCHANTMENT_LEVEL, level)
                        .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, damageSource.getEntity())
                        .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, damageSource.getDirectEntity())
                        .create(DAMAGE_BASED), entity.level()
        );
    }

    public static LootContext breakBlock(Player player, BlockState state, @Nullable ItemStack toolStack, int level) {
        return boiler(
                builder -> builder
                        .withParameter(LootContextParams.ORIGIN, player.position())
                        .withParameter(LootContextParams.BLOCK_STATE, state)
                        .withParameter(LootContextParams.THIS_ENTITY, player)
                        .withParameter(LootContextParams.ENCHANTMENT_LEVEL, level)
                        .withOptionalParameter(LootContextParams.TOOL, toolStack)
                        .create(BREAK_BLOCK), player.level()
        );
    }

    public static LootContext tick(Player player, int level) {
        return boiler(
                builder -> builder
                        .withParameter(LootContextParams.THIS_ENTITY, player)
                        .withParameter(LootContextParams.ORIGIN, player.position())
                        .withParameter(LootContextParams.ENCHANTMENT_LEVEL, level)
                        .create(TICK), player.level()
        );
    }

    private static LootContext boiler(Function<LootParams.Builder, LootParams> build, Level level) {
        return new LootContext.Builder(build.apply(new LootParams.Builder((ServerLevel) level))).create(Optional.empty());
    }
}
