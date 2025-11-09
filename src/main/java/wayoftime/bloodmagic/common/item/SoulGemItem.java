package wayoftime.bloodmagic.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.datacomponent.EnumWillType;
import wayoftime.bloodmagic.common.datamap.BMDataMaps;
import wayoftime.bloodmagic.util.ChatUtil;

import java.util.List;

public class SoulGemItem extends Item {

    public SoulGemItem() {
        super(new Properties().stacksTo(1).component(BMDataComponents.DEMON_WILL_AMOUNT, 0D).component(BMDataComponents.DEMON_WILL_TYPE, EnumWillType.DEFAULT));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        // TODO: implement
        return super.use(level, player, usedHand);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return stack.getItemHolder().getData(BMDataMaps.TARTARIC_GEM_MAX_AMOUNTS) != null;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        double currentWill = stack.getOrDefault(BMDataComponents.DEMON_WILL_AMOUNT, 0D);
        Holder<Item> holder = stack.getItemHolder();
        Double maxWill = holder.getData(BMDataMaps.TARTARIC_GEM_MAX_AMOUNTS); // should never be called if this is null since thats covered by isBarVisible
        return (int) ((currentWill / maxWill) * 13);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        double currentWill = stack.getOrDefault(BMDataComponents.DEMON_WILL_AMOUNT, 0D);
        Holder<Item> holder = stack.getItemHolder();
        Double maxWill = holder.getData(BMDataMaps.TARTARIC_GEM_MAX_AMOUNTS); // should never be called if this is null since thats covered by isBarVisible
        return Mth.hsvToRgb(Math.max(0.0F, (float) (currentWill / maxWill)) / 3.0F, 1.0F, 1.0F);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        EnumWillType type = stack.getOrDefault(BMDataComponents.DEMON_WILL_TYPE, EnumWillType.DEFAULT);
        double amount = stack.getOrDefault(BMDataComponents.DEMON_WILL_AMOUNT, 0D);
        ResourceLocation loc = stack.getItemHolder().getKey().location();

        tooltip.add(Component.translatable("tooltip.bloodmagic.soul_gem." + loc.getPath()).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.bloodmagic.will", ChatUtil.DECIMAL_FORMAT.format(amount)).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.bloodmagic.current_type." + type.getSerializedName()).withStyle(ChatFormatting.GRAY));

        super.appendHoverText(stack, context, tooltip, tooltipFlag);
    }
}
