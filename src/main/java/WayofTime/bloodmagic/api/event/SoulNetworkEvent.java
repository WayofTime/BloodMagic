package WayofTime.bloodmagic.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class SoulNetworkEvent extends Event
{
    public final String ownerName;
    public int syphon;

    public SoulNetworkEvent(String ownerName, int syphon)
    {
        this.ownerName = ownerName;
        this.syphon = syphon;
    }

    @Cancelable
    public static class ItemDrainInContainerEvent extends SoulNetworkEvent
    {

        public ItemStack stack;

        public ItemDrainInContainerEvent(ItemStack stack, String ownerName, int syphon)
        {
            super(ownerName, syphon);
            this.stack = stack;
        }
    }

    @Cancelable
    public static class PlayerDrainNetworkEvent extends SoulNetworkEvent
    {

        public final EntityPlayer player;
        // If true, will damage regardless of if the network had enough inside it
        public boolean shouldDamage;

        public PlayerDrainNetworkEvent(EntityPlayer player, String ownerNetwork, int drainAmount)
        {
            super(ownerNetwork, drainAmount);
            this.shouldDamage = false;
            this.player = player;
        }
    }

    @Cancelable
    public static class ItemDrainNetworkEvent extends PlayerDrainNetworkEvent
    {

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
         * @param player
         *        Player using the item
         * @param ownerNetwork
         *        Network that the item is tied to
         * @param itemStack
         *        Item used
         * @param drainAmount
         *        Original drain amount - change to alter cost
         */
        public ItemDrainNetworkEvent(EntityPlayer player, String ownerNetwork, ItemStack itemStack, int drainAmount)
        {
            super(player, ownerNetwork, drainAmount);
            this.itemStack = itemStack;
            this.damageAmount = (float) (drainAmount) / 100.0f;
        }
    }
}
