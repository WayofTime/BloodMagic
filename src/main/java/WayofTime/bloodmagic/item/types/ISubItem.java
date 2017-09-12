package WayofTime.bloodmagic.item.types;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public interface ISubItem {

    @Nonnull
    String getInternalName();

    @Nonnull
    default ItemStack getStack() {
        return getStack(1);
    }

    @Nonnull
    ItemStack getStack(int count);
}
