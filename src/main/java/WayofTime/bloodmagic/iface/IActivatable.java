package WayofTime.bloodmagic.iface;

import WayofTime.bloodmagic.util.Constants;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public interface IActivatable {

    default boolean getActivated(ItemStack stack) {
        return !stack.isEmpty() && stack.hasTagCompound() && stack.getTagCompound().getBoolean(Constants.NBT.ACTIVATED);
    }

    @Nonnull
    default ItemStack setActivatedState(ItemStack stack, boolean activated) {
        if (!stack.isEmpty()) {
            if (!stack.hasTagCompound())
                stack.setTagCompound(new NBTTagCompound());

            stack.getTagCompound().setBoolean(Constants.NBT.ACTIVATED, activated);
        }

        return stack;
    }
}
