package WayofTime.bloodmagic.iface;

import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * This interface is used for items intended to train specific upgrades while
 * held in the player's inventory.
 */
public interface IUpgradeTrainer {
    List<String> getTrainedUpgrades(ItemStack stack);

    boolean setTrainedUpgrades(ItemStack stack, List<String> keys);
}
