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
     * If false, binding fails.
     */
    boolean onBind(EntityPlayer player, ItemStack stack);
}
