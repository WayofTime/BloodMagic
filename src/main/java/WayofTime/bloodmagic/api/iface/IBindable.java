package WayofTime.bloodmagic.api.iface;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Implement this interface on any Item that can be bound to a player.
 */
public interface IBindable
{
    /**
     * Gets the username of the Item's owner. Usually for display, such as in
     * the tooltip.
     * 
     * If the item is not bound, this will be null.
     * 
     * @param stack
     *        - The owned ItemStack
     * 
     * @return - The username of the Item's owner
     */
    String getOwnerName(ItemStack stack);

    /**
     * Gets the UUID of the Item's owner.
     * 
     * If the item is not bound, this will be null.
     * 
     * @param stack
     *        - The owned ItemStack
     * 
     * @return - The UUID of the Item's owner
     */
    String getOwnerUUID(ItemStack stack);

    /**
     * Called when the player attempts to bind the item.
     * 
     * @param player
     *        - The Player attempting to bind the item
     * @param stack
     *        - The ItemStack to attempt binding
     * 
     * @return If binding was successful.
     */
    boolean onBind(EntityPlayer player, ItemStack stack);
}
