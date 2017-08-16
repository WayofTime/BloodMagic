package WayofTime.bloodmagic.routing;

import net.minecraft.item.ItemStack;

public class IgnoreNBTItemFilter extends TestItemFilter {
    @Override
    public boolean doesStackMatchFilter(ItemStack testStack) {
        for (ItemStack filterStack : requestList) {
            if (doStacksMatch(filterStack, testStack)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean doStacksMatch(ItemStack filterStack, ItemStack testStack) {
        return ItemStack.areItemsEqual(filterStack, testStack);
    }
}
