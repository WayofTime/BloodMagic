package wayoftime.bloodmagic.api.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
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
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.datacomponent.Binding;
import wayoftime.bloodmagic.common.datacomponent.SoulNetwork;
import wayoftime.bloodmagic.api.sigil.SigilType;
import wayoftime.bloodmagic.util.SoulTicket;
import wayoftime.bloodmagic.util.helper.SoulNetworkHelper;

import java.util.List;

public class SigilItem extends Item {
    public SigilItem() {
        super(new Properties().stacksTo(1).component(BMDataComponents.BINDING, Binding.EMPTY));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (!stack.has(BMDataComponents.SIGIL_TYPE)) {
            tooltipComponents.add(Component.translatable("This sigil has no type set! It will work as a Divination Sigil but you should add a sigil_type data component"));
        } else {
            String name = stack.getOrDefault(BMDataComponents.SIGIL_TYPE, BMIdentifiers.Sigils.DIVINATION).location().getPath();
            tooltipComponents.add(Component.translatable("tooltip.bloodmagic.sigil." + name));
        }
        if (stack.has(BMDataComponents.SIGIL_ACTIVE)) {
            boolean isActive = stack.getOrDefault(BMDataComponents.SIGIL_ACTIVE, false);
            tooltipComponents.add(Component.translatable("tooltip.bloodmagic.sigil." + (isActive ? "activated" : "deactivated")).withStyle(ChatFormatting.GRAY));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        if (!stack.has(BMDataComponents.SIGIL_TYPE)) {
            return "item.bloodmagic.sigil.invalid";
        } else {
            String name = stack.getOrDefault(BMDataComponents.SIGIL_TYPE, BMIdentifiers.Sigils.DIVINATION).location().getPath();
            return "item.bloodmagic.sigil." + name;
        }
    }

    public static SigilType getType(ItemStack stack, RegistryAccess registries) {
        ResourceKey<SigilType> key = stack.getOrDefault(BMDataComponents.SIGIL_TYPE, BMIdentifiers.Sigils.DIVINATION);
        return registries.registryOrThrow(BMIdentifiers.RegistryKeys.SIGIL_TYPES).getOrThrow(key);
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

        SigilType type = getType(stack, context.getLevel().registryAccess());
        boolean result = type.effect().useOnBlock(stack, player, context);
        if (result) {
            network.syphonAndDamage(SoulTicket.item(stack, player.level(), player, type.blockCost()), player);
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

        if (stack.has(BMDataComponents.SIGIL_ACTIVE) && player.isShiftKeyDown()) {
            boolean state = stack.getOrDefault(BMDataComponents.SIGIL_ACTIVE, false); // using .get() makes it complain about it being null, so using getOrDefault anyways
            stack.set(BMDataComponents.SIGIL_ACTIVE, !state);
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }

        if (level.isClientSide) {
            return InteractionResultHolder.sidedSuccess(stack, true);
        }

        SigilType type = getType(stack, level.registryAccess());
        boolean result = type.effect().useOnAir(stack, player, usedHand);
        if (result) {
            network.syphonAndDamage(SoulTicket.item(stack, player.level(), player, type.airCost()), player);
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

        SigilType type = getType(stack, player.level().registryAccess());
        boolean result = type.effect().useOnEntity(stack, player, interactionTarget);
        if (result) {
            network.syphonAndDamage(SoulTicket.item(stack, player.level(), player, type.entityCost()), player);
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

        if (stack.getOrDefault(BMDataComponents.SIGIL_ACTIVE, false)) {
            SigilType type = getType(stack, level.registryAccess());
            if (player.tickCount % 100 == 0) { // this could be cheesed by switching it off just before the damage would occur and back on just after. not sure if this is an actual issue
                                               // if it is, switch to a data component for tracking time
                network.syphonAndDamage(SoulTicket.item(stack, level, player, type.refreshCost()), player);
            }
            type.effect().activeTick(stack, level, player); // slotId and isSelected? I dont think those are relevant ever for a sigil
        }
    }
}
