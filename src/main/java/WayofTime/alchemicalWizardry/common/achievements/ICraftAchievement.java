package WayofTime.alchemicalWizardry.common.achievements;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;

public interface ICraftAchievement
{
    Achievement getAchievementOnCraft(ItemStack stack, EntityPlayer player, IInventory matrix);
}
