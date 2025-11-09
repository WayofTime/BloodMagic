package wayoftime.bloodmagic.common.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class ItemBindEvent extends Event implements ICancellableEvent {
    private final Player player;
    private final ItemStack itemStack;

    public ItemBindEvent(Player player, ItemStack itemStack) {
        this.player = player;
        this.itemStack = itemStack;
    }

    public Player getNewOwner() {
        return player;
    }

    public ItemStack getBindingStack() {
        return itemStack;
    }
}
