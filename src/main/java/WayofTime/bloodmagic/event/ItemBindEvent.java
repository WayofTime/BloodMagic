package WayofTime.bloodmagic.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class ItemBindEvent extends Event {

    private final EntityPlayer player;
    private final ItemStack itemStack;

    /**
     * This event is called whenever a player attempts to bind a
     * {@link WayofTime.bloodmagic.iface.IBindable} item.
     *
     * @param player    The player doing the binding
     * @param itemStack The {@link ItemStack} that the player is binding
     *                  <p>
     *                  This event is {@link Cancelable}.<br>
     */
    public ItemBindEvent(EntityPlayer player, ItemStack itemStack) {
        this.player = player;
        this.itemStack = itemStack;
    }

    public EntityPlayer getNewOwner() {
        return player;
    }

    public ItemStack getBindingStack() {
        return itemStack;
    }
}
