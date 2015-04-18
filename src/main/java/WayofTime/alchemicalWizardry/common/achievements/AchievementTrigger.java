package WayofTime.alchemicalWizardry.common.achievements;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;

public class AchievementTrigger
{
    @SubscribeEvent
    public void onItemPickedUp(PlayerEvent.ItemPickupEvent event)
    {
        ItemStack stack = event.pickedUp.getEntityItem();

        for (Item item : AchievementsRegistry.list)
        {

        }

        if (stack != null && stack.getItem() instanceof IPickupAchievement)
        {
            Achievement achievement = ((IPickupAchievement) stack.getItem()).getAchievementOnPickup(stack, event.player, event.pickedUp);

            if (achievement != null)
            {
                event.player.addStat(achievement, 1);
            }
        }
    }

    @SubscribeEvent
    public void onItemCrafted(PlayerEvent.ItemCraftedEvent event)
    {
        if (event.crafting != null && event.crafting.getItem() instanceof ICraftAchievement)
        {
            Achievement achievement = ((ICraftAchievement) event.crafting.getItem()).getAchievementOnCraft(event.crafting, event.player, event.craftMatrix);

            if (achievement != null)
            {
                event.player.addStat(achievement, 1);
            }
        }
    }
}
