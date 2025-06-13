package wayoftime.bloodmagic.common.menu;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

public class GhostItemHandler extends ItemStackHandler {

    public GhostItemHandler(NonNullList<ItemStack> initial) {
        super(initial);
    }

    public GhostItemHandler(int size) {
        super(size);
    }
}
