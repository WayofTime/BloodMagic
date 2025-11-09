package wayoftime.bloodmagic.common.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import org.jetbrains.annotations.Nullable;
import wayoftime.bloodmagic.common.living.LivingEffectComponents;
import wayoftime.bloodmagic.common.living.LivingHelper;

import java.util.function.Consumer;

public interface UpgradeHolderBase extends IItemExtension {

    @Override
    default <T extends LivingEntity> int damageItem(ItemStack stack, int amount, @Nullable T entity, Consumer<Item> onBroken) {
        if (LivingHelper.isNeverValid(stack)) {
            return IItemExtension.super.damageItem(stack, amount, entity, onBroken);
        }

        int durRemaining = (stack.getMaxDamage() - 1 - stack.getDamageValue());
        return Math.max(Math.min(durRemaining, amount), 0);
    }

    @Override
    default boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
        if (!(wearer instanceof Player player)) {
            return false;
        }
        return LivingHelper.hasFullSet(player) && LivingHelper.has(player, LivingEffectComponents.GILDED.get());
    }

    @Override
    default boolean canElytraFly(ItemStack stack, LivingEntity wearer) {
        if (!(wearer instanceof Player player)) {
            return false;
        }
        return LivingHelper.hasFullSet(player) && LivingHelper.has(player, LivingEffectComponents.ELYTRA.get());
    }

    @Override
    default boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        // TODO reduce damage sustained with higher levels?
        if (!entity.level().isClientSide) {
            int nextFlightTick = flightTicks + 1;
            if (nextFlightTick % 10 == 0) {
                if (nextFlightTick % 20 == 0) {
                    stack.hurtAndBreak(1, entity, EquipmentSlot.CHEST);
                }
                entity.gameEvent(GameEvent.ELYTRA_GLIDE);
            }
        }
        return true;
    }

    // currently unused but why the heck not provide the option if its the easiest thing ever
    @Override
    default boolean canWalkOnPowderedSnow(ItemStack stack, LivingEntity wearer) {
        if (!(wearer instanceof Player player)) {
            return false;
        }
        return LivingHelper.hasFullSet(player) && LivingHelper.has(player, LivingEffectComponents.WALK_ON_POWDERED_SNOW.get());
    }

    @Override
    default boolean isEnderMask(ItemStack stack, Player player, EnderMan endermanEntity) {
        return LivingHelper.hasFullSet(player) && LivingHelper.has(player, LivingEffectComponents.IS_ENDER_MASK.get());
    }
}
