package wayoftime.bloodmagic.common.menu;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.items.ItemStackHandler;

public class GhostItemHandler extends ItemStackHandler {

    public GhostItemHandler(NonNullList<ItemStack> initial) {
        super(initial);
    }

    public GhostItemHandler(int size) {
        super(size);
    }

    public GhostItemHandler(ItemContainerContents contents) {
        super(contents.getSlots());
        contents.copyInto(this.stacks);
    }

    public void setChanged(int slot) {
        onContentsChanged(slot);
    }

    public ItemContainerContents toContents() {
        return ItemContainerContents.fromItems(this.stacks);
    }
}
