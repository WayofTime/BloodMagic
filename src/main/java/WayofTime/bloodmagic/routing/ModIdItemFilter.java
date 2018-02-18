package WayofTime.bloodmagic.routing;

import net.minecraft.item.ItemStack;

public class ModIdItemFilter extends TestItemFilter {

    @Override
    public boolean doStacksMatch(ItemStack filterStack, ItemStack testStack) {
        return getModID(filterStack).equalsIgnoreCase(getModID(testStack));
    }

    public String getModID(ItemStack stack) {
        String modid = stack.getItem().getCreatorModId(stack);
        return modid == null ? "" : modid;
    }
}
