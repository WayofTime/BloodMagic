package wayoftime.bloodmagic.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;

import java.util.List;

public class ScrapItem extends Item {
    public ScrapItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        int scrap = stack.getOrDefault(BMDataComponents.UPGRADE_SCRAP, 0);
        tooltipComponents.add(Component.translatable("tooltip.bloodmagic.scrap", scrap));

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
