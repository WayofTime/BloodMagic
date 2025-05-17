package wayoftime.bloodmagic.client.event;

import com.mojang.datafixers.util.Either;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.datacomponent.Binding;

import java.util.List;

@EventBusSubscriber(Dist.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public static void onHoverText(RenderTooltipEvent.GatherComponents event) {
        List<Either<FormattedText, TooltipComponent>> tooltipElements = event.getTooltipElements();
        ItemStack eventStack = event.getItemStack();

        Binding binding = eventStack.get(BMDataComponents.BINDING);
        if (binding != null) {
            if (binding.isEmpty()) {
                tooltipElements.add(tooltipElements.size() - 1, Either.left(Component.translatable("tooltip.bloodmagic.no_owner").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC)));
            } else {
                tooltipElements.add(tooltipElements.size() - 1, Either.left(binding.getHoverText()));
            }
        }
    }
}
