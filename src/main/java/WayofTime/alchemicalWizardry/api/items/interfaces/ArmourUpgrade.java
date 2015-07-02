package WayofTime.alchemicalWizardry.api.items.interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ArmourUpgrade
{
    //Called when the armour ticks
    void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack);

    boolean isUpgrade();

    int getEnergyForTenSeconds();
}
