package WayofTime.bloodmagic.item.routing;

import net.minecraft.item.ItemStack;

public interface IRoutingFilterProvider {
    /**
     * Translates the inputed keyStack into the proper filtered key
     *
     * @param filterStack
     * @param keyStack
     * @return A new ItemStack which modifies the keyStack
     */
    ItemStack getContainedStackForItem(ItemStack filterStack, ItemStack keyStack);
}
