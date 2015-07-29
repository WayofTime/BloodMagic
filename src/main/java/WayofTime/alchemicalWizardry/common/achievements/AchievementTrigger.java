package WayofTime.alchemicalWizardry.common.achievements;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class AchievementTrigger
{
    @SubscribeEvent
    public void onItemPickedUp(PlayerEvent.ItemPickupEvent event)
    {
        for (Item item : AchievementsRegistry.pickupList)
        {
            ItemStack stack = event.pickedUp.getEntityItem();

            if (stack != null && stack.getItem() == item)
            {
                Achievement achievement = AchievementsRegistry.getAchievementForItem(item);

                if (achievement != null)
                {
                    event.player.addStat(achievement, 1);
                }
            }
        }
    }

    @SubscribeEvent
    public void onItemCrafted(PlayerEvent.ItemCraftedEvent event)
    {
        for (Item item : AchievementsRegistry.craftinglist)
        {
            if (event.crafting != null && event.crafting.getItem() == item)
            {
                Achievement achievement = AchievementsRegistry.getAchievementForItem(event.crafting.getItem());

                if (achievement != null)
                {
                    event.player.addStat(achievement, 1);
                }
            }
        }
    }
}
