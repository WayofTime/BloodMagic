package wayoftime.bloodmagic.common.item;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.function.TriConsumer;
import org.apache.commons.lang3.function.TriFunction;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.datacomponent.UpgradeTome;
import wayoftime.bloodmagic.common.living.LivingHelper;
import wayoftime.bloodmagic.common.living.LivingUpgrade;

import java.util.List;

public class UpgradeTomeItem extends Item {
    public UpgradeTomeItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack tomeStack = player.getItemInHand(usedHand);
        UpgradeTome tome = tomeStack.get(BMDataComponents.UPGRADE_TOME_DATA);
        if (tome == null) {
            return InteractionResultHolder.pass(tomeStack);
        }

        XpFunc expAdder = LivingHelper::applyExp;
        if (player.isShiftKeyDown()) {
            expAdder = LivingHelper::applyExpToCap;
        }

        float consumed = expAdder.apply(player, tome.upgrade(), tome.exp(), true);
        if (player.hasInfiniteMaterials()) { // creative, no consume item/exp, only add >:
            return InteractionResultHolder.sidedSuccess(tomeStack, level.isClientSide);
        }

        if (consumed >= tome.exp()) {
            return InteractionResultHolder.sidedSuccess(ItemStack.EMPTY, level.isClientSide);
        }

        tomeStack.set(BMDataComponents.UPGRADE_TOME_DATA, new UpgradeTome(tome.upgrade(), tome.exp() - consumed));
        return InteractionResultHolder.sidedSuccess(tomeStack, level.isClientSide);
    }

    @FunctionalInterface
    public interface XpFunc {
        Float apply(Player player, Holder<LivingUpgrade> upgrade, Float exp, boolean fromTome);
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        UpgradeTome tome = stack.get(BMDataComponents.UPGRADE_TOME_DATA);
        return tome == null ? getDescriptionId() : getDescriptionId() + "." + tome.upgrade().getKey().location().getPath();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        UpgradeTome tome = stack.get(BMDataComponents.UPGRADE_TOME_DATA);
        if (tome != null) {
            tome.addToTooltip(context, tooltipComponents::add, tooltipFlag);
        }
    }

    // TODO display progress to next level?
    @Override
    public boolean isBarVisible(ItemStack stack) {
        return super.isBarVisible(stack);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return super.getBarWidth(stack);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return super.getBarColor(stack);
    }
}
