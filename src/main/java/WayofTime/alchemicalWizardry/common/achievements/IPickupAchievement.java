package WayofTime.alchemicalWizardry.common.achievements;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;

public interface IPickupAchievement
{
    Achievement getAchievementOnPickup(ItemStack stack, EntityPlayer player, EntityItem item);
}
