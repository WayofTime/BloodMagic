package WayofTime.bloodmagic.iface;

import net.minecraft.item.ItemStack;

/**
 * Interface used for any item that can store LP in itself
 */
public interface IItemLPContainer {
    int getCapacity();

    void setStoredLP(ItemStack stack, int lp);

    int getStoredLP(ItemStack stack);
}
