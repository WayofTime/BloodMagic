package WayofTime.alchemicalWizardry.api.items.interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ArmourUpgrade
{
    //Called when the armour ticks
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack);

    public boolean isUpgrade();

    public int getEnergyForTenSeconds();
}
