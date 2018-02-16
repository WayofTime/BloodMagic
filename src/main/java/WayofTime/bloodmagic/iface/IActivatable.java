package WayofTime.bloodmagic.iface;

import net.minecraft.item.ItemStack;

public interface IActivatable {
    boolean getActivated(ItemStack stack);

    ItemStack setActivatedState(ItemStack stack, boolean activated);
}
