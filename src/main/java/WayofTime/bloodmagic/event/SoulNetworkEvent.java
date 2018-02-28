package WayofTime.bloodmagic.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Base event class for Soul Network related events.
 * <p>
 * {@link #ownerUUID} contains the owner's UUID {@link #syphon} contains the
 * amount of LP to be drained
 */
public class SoulNetworkEvent extends Event {
    public final UUID ownerUUID;
    public int syphon;

    public SoulNetworkEvent(UUID ownerUUID, int syphon) {
        this.ownerUUID = ownerUUID;
        this.syphon = syphon;
    }

    /**
     * This event is called when an
     * {@link WayofTime.bloodmagic.iface.IBindable} is being drained
     * inside of a {@link net.minecraft.tileentity.TileEntity}.
     * <p>
     * If canceled, the drain will not be executed.
     */
    @Cancelable
    public static class ItemDrainInContainerEvent extends SoulNetworkEvent {
        public ItemStack stack;

        public ItemDrainInContainerEvent(ItemStack stack, UUID ownerId, int syphon) {
            super(ownerId, syphon);
            this.stack = stack;
        }
    }

    /**
     * This event is called when a {@link EntityPlayer} drains the Soul Network
     * <p>
     * If canceled, the drain will not be executed.
     */
    @Cancelable
    public static class PlayerDrainNetworkEvent extends SoulNetworkEvent {
        public final EntityPlayer player;
        // If true, will damage regardless of if the network had enough inside it
        public boolean shouldDamage;

        public PlayerDrainNetworkEvent(EntityPlayer player, UUID ownerId, int drainAmount) {
            super(ownerId, drainAmount);
            this.shouldDamage = false;
            this.player = player;
        }
    }

    @Cancelable
    public static class ItemDrainNetworkEvent extends PlayerDrainNetworkEvent {
        @Nullable
        public final ItemStack itemStack;
        /**
         * Amount of damage that would incur if the network could not drain
         * properly
         */
        public float damageAmount;

        /**
         * Set result to deny the action i.e. damage/drain anyways. Cancelling
         * event prevents action without penalties
         *
         * @param player       Player using the item
         * @param ownerId Network that the item is tied to
         * @param itemStack    Item used
         * @param drainAmount  Original drain amount - change to alter cost
         */
        public ItemDrainNetworkEvent(EntityPlayer player, UUID ownerId, @Nullable ItemStack itemStack, int drainAmount) {
            super(player, ownerId, drainAmount);
            this.itemStack = itemStack;
            this.damageAmount = (float) (drainAmount) / 100.0f;
        }
    }
}
