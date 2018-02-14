package WayofTime.bloodmagic.apibutnotreally.iface;

import WayofTime.bloodmagic.apibutnotreally.Constants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Implement this interface on any Item that can be bound to a player.
 */
public interface IBindable {

    /**
     * Gets the username of the Item's owner. Usually for display, such as in
     * the tooltip.
     * <p>
     * If the item is not bound, this will be null.
     *
     * @param stack - The owned ItemStack
     * @return - The username of the Item's owner
     */
    default String getOwnerName(ItemStack stack) {
        return !stack.isEmpty() && stack.hasTagCompound() ? stack.getTagCompound().getString(Constants.NBT.OWNER_NAME) : null;
    }

    /**
     * Gets the UUID of the Item's owner.
     * <p>
     * If the item is not bound, this will be null.
     *
     * @param stack - The owned ItemStack
     * @return - The UUID of the Item's owner
     */
    default String getOwnerUUID(ItemStack stack) {
        return !stack.isEmpty() && stack.hasTagCompound() ? stack.getTagCompound().getString(Constants.NBT.OWNER_UUID) : null;
    }

    /**
     * Called when the player attempts to bind the item.
     *
     * @param player - The Player attempting to bind the item
     * @param stack  - The ItemStack to attempt binding
     * @return If binding was successful.
     */
    default boolean onBind(EntityPlayer player, ItemStack stack) {
        return true;
    }
}
