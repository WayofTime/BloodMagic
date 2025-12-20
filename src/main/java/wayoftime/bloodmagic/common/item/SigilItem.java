package wayoftime.bloodmagic.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.api.BMIdentifiers;
import wayoftime.bloodmagic.api.BMIdentifiers.Sigils;
import wayoftime.bloodmagic.api.sigil.SigilEffect;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.datacomponent.Binding;
import wayoftime.bloodmagic.common.datacomponent.SoulNetwork;
import wayoftime.bloodmagic.util.SoulTicket;
import wayoftime.bloodmagic.util.helper.SoulNetworkHelper;

import java.util.List;

public class SigilItem extends Item {
    public SigilItem() {
        super(new Properties().stacksTo(1).component(BMDataComponents.BINDING, Binding.EMPTY));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (!stack.has(BMDataComponents.SIGIL_EFFECT)) {
            tooltipComponents.add(Component.translatable("This sigil has no effect set! It will work as a Divination Sigil but you should add a sigil_effect data component"));
        }
        ResourceKey<SigilEffect> key = stack.getOrDefault(BMDataComponents.SIGIL_EFFECT, Sigils.DIVINATION);
        tooltipComponents.add(Component.translatable("tooltip.bloodmagic.sigil." + key.location().getPath()));
        SigilEffect effect = context.level().registryAccess().registryOrThrow(BMIdentifiers.RegistryKeys.SIGIL_EFFECT).getOrThrow(key);
        if (effect.isActivatable()) {
            boolean isActive = stack.has(BMDataComponents.SIGIL_ACTIVE);
            tooltipComponents.add(Component.translatable(effect.getActiveTooltip(isActive)).withStyle(ChatFormatting.GRAY));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        if (!stack.has(BMDataComponents.SIGIL_EFFECT)) {
            return "item.bloodmagic.sigil.invalid";
        } else {
            String name = stack.getOrDefault(BMDataComponents.SIGIL_EFFECT, BMIdentifiers.Sigils.DIVINATION).location().getPath();
            return "item.bloodmagic.sigil." + name;
        }
    }

    public static SigilEffect getEffect(ItemStack stack, RegistryAccess registries) {
        ResourceKey<SigilEffect> key = stack.getOrDefault(BMDataComponents.SIGIL_EFFECT, BMIdentifiers.Sigils.DIVINATION);
        return registries.registryOrThrow(BMIdentifiers.RegistryKeys.SIGIL_EFFECT).getOrThrow(key);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        if (player == null || player.isFakePlayer()) {
            return InteractionResult.PASS;
        }

        Binding binding = stack.getOrDefault(BMDataComponents.BINDING, Binding.EMPTY);
        if (binding.isEmpty()) {
            return InteractionResult.PASS;
        }
        SoulNetwork network = SoulNetworkHelper.getSoulNetwork(binding.uuid());
        if (network == null) {
            return InteractionResult.PASS;
        }

        if (context.getLevel().isClientSide) {
            return InteractionResult.sidedSuccess(true);
        }

        SigilEffect effect = getEffect(stack, context.getLevel().registryAccess());
        int cost = effect.useOnBlock(stack, player, context);
        if (cost > 0) {
            network.syphonAndDamage(SoulTicket.item(stack, player.level(), player, cost), player);
            return InteractionResult.sidedSuccess(false);
        }

        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (player.isFakePlayer()) {
            return InteractionResultHolder.pass(stack);
        }

        Binding binding = stack.getOrDefault(BMDataComponents.BINDING, Binding.EMPTY);
        if (binding.isEmpty()) {
            return InteractionResultHolder.pass(stack);
        }
        SoulNetwork network = SoulNetworkHelper.getSoulNetwork(binding.uuid());
        if (network == null) {
            return InteractionResultHolder.pass(stack);
        }

        SigilEffect effect = getEffect(stack, level.registryAccess());
        if (effect.isActivatable() && player.isShiftKeyDown()) {
            if (stack.has(BMDataComponents.SIGIL_ACTIVE)) {
                stack.remove(BMDataComponents.SIGIL_ACTIVE);
            } else {
                stack.set(BMDataComponents.SIGIL_ACTIVE, Unit.INSTANCE);
            }

            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }

        if (level.isClientSide) {
            return InteractionResultHolder.sidedSuccess(stack, true);
        }

        int cost = effect.useOnAir(stack, player, usedHand);
        if (cost > 0) {
            network.syphonAndDamage(SoulTicket.item(stack, player.level(), player, cost), player);
            return InteractionResultHolder.sidedSuccess(stack, false);
        }

        return super.use(level, player, usedHand);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        if (player.isFakePlayer()) {
            return InteractionResult.PASS;
        }

        Binding binding = stack.getOrDefault(BMDataComponents.BINDING, Binding.EMPTY);
        if (binding.isEmpty()) {
            return InteractionResult.PASS;
        }
        SoulNetwork network = SoulNetworkHelper.getSoulNetwork(binding.uuid());
        if (network == null) {
            return InteractionResult.PASS;
        }

        if (player.level().isClientSide) {
            return InteractionResult.sidedSuccess(true);
        }

        SigilEffect effect = getEffect(stack, player.level().registryAccess());
        int cost = effect.useOnEntity(stack, player, interactionTarget);
        if (cost > 0) {
            network.syphonAndDamage(SoulTicket.item(stack, player.level(), player, cost), player);
            return InteractionResult.sidedSuccess(true);
        }

        return InteractionResult.PASS;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (level.isClientSide) {
            return;
        }

        Binding binding = stack.getOrDefault(BMDataComponents.BINDING, Binding.EMPTY);
        if (binding.isEmpty()) {
            return;
        }
        SoulNetwork network = SoulNetworkHelper.getSoulNetwork(binding.uuid());
        if (network == null) {
            return;
        }

        if (!(entity instanceof Player player)) {
            return;
        }

        if (player.isFakePlayer()) {
            return;
        }

        if (stack.has(BMDataComponents.SIGIL_ACTIVE)) {
            SigilEffect effect = getEffect(stack, level.registryAccess());
            int cost = effect.activeTick(stack, level, player); // slotId and isSelected? I dont think those are relevant ever for a sigil
            if (player.tickCount % 100 == 0) { // this could be cheesed by switching it off just before the damage would occur and back on just after. not sure if this is an actual issue
                                               // if it is, switch to a data component for tracking time
                network.syphonAndDamage(SoulTicket.item(stack, level, player, cost), player);
            }
        }
    }
}
