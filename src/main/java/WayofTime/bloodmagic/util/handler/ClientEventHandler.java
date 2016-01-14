package WayofTime.bloodmagic.util.handler;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import WayofTime.bloodmagic.util.GhostItemHelper;

public class ClientEventHandler
{
    @SubscribeEvent
    public void onTooltipEvent(ItemTooltipEvent event)
    {
        ItemStack stack = event.itemStack;
        if (stack == null)
        {
            return;
        }

        if (GhostItemHelper.hasGhostAmount(stack))
        {
            int amount = GhostItemHelper.getItemGhostAmount(stack);
            if (amount == 0)
            {
                event.toolTip.add("Everything");
            } else
            {
                event.toolTip.add("Ghost item amount: " + amount);
            }
        }
    }
}
