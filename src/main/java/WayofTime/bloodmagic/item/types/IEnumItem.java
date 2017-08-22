package WayofTime.bloodmagic.item.types;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public interface IEnumItem {

    @Nonnull
    String getInternalName();

    @Nonnull
    ItemStack getStack();

    @Nonnull
    ItemStack getStack(int count);
}
