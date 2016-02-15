package WayofTime.bloodmagic.api.iface;

import java.util.List;

import net.minecraft.item.ItemStack;

/**
 * This interface is used for items intended to train specific upgrades while
 * held in the player's inventory.
 */
public interface IUpgradeTrainer
{
    public List<String> getTrainedUpgrades(ItemStack stack);

    public boolean setTrainedUpgrades(ItemStack stack, List<String> keys);
}
