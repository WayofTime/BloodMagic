package WayofTime.bloodmagic.routing;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictItemFilter extends TestItemFilter {
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
        if (filterStack.isEmpty() || testStack.isEmpty())
            return false;

        int[] filterIds = OreDictionary.getOreIDs(filterStack);
        int[] testIds = OreDictionary.getOreIDs(testStack);

        if (filterIds.length <= 0 || testIds.length <= 0) {
            return false;
        }

        for (int filterId : filterIds) {
            for (int testId : testIds) {
                if (filterId == testId) {
                    return true;
                }
            }
        }

        return false;
    }
}
