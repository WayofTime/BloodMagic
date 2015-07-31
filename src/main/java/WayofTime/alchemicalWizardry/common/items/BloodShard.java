package WayofTime.alchemicalWizardry.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;

public class BloodShard extends Item implements ArmourUpgrade
{
    public BloodShard()
    {
        super();
    }

    public int getBloodShardLevel()
    {
        if (this.equals(ModItems.weakBloodShard))
        {
            return 1;
        } else if (this.equals(ModItems.demonBloodShard))
        {
            return 2;
        }

        return 0;
    }

    @Override
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack) {}

    @Override
    public boolean isUpgrade()
    {
        return false;
    }

    @Override
    public int getEnergyForTenSeconds()
    {
        return 0;
    }
}
