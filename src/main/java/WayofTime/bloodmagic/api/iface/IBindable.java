package WayofTime.bloodmagic.api.iface;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Implement this interface on any Item that can be bound to a player.
 */
public interface IBindable
{
    /**
     * Called when the player attempts to bind the item.
     *
     * @param player
     *          - The Player attempting to bind the item
     * @param stack
     *          - The ItemStack to attempt binding
     *
     * @return If binding was successful.
     */
    boolean onBind(EntityPlayer player, ItemStack stack);
}
