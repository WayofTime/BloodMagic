package WayofTime.bloodmagic.apibutnotreally.impl;

import WayofTime.bloodmagic.apibutnotreally.Constants;
import WayofTime.bloodmagic.apibutnotreally.iface.IBindable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Base class for all bindable items.
 */
public class ItemBindable extends Item implements IBindable {
    public ItemBindable() {
        super();

        setMaxStackSize(1);
    }

    // IBindable

    @Override
    public boolean onBind(EntityPlayer player, ItemStack stack) {
        return true;
    }

    @Override
    public String getOwnerName(ItemStack stack) {
        return !stack.isEmpty() && stack.hasTagCompound() ? stack.getTagCompound().getString(Constants.NBT.OWNER_NAME) : null;
    }

    @Override
    public String getOwnerUUID(ItemStack stack) {
        return !stack.isEmpty() && stack.hasTagCompound()  ? stack.getTagCompound().getString(Constants.NBT.OWNER_UUID) : null;
    }
}
