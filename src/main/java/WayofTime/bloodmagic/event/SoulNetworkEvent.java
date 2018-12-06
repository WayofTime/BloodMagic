package WayofTime.bloodmagic.event;

import WayofTime.bloodmagic.core.data.SoulNetwork;
import WayofTime.bloodmagic.core.data.SoulTicket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class SoulNetworkEvent extends Event {

    private final SoulNetwork network;
    private SoulTicket ticket;

    public SoulNetworkEvent(SoulNetwork network, SoulTicket ticket) {
        this.network = network;
        this.ticket = ticket;
    }

    public SoulNetwork getNetwork() {
        return network;
    }

    public SoulTicket getTicket() {
        return ticket;
    }

    public void setTicket(SoulTicket ticket) {
        this.ticket = ticket;
    }

    @Cancelable
    public static class Syphon extends SoulNetworkEvent {

        private boolean shouldDamage;

        public Syphon(SoulNetwork network, SoulTicket ticket) {
            super(network, ticket);
        }

        public boolean shouldDamage() {
            return shouldDamage;
        }

        public void setShouldDamage(boolean shouldDamage) {
            this.shouldDamage = shouldDamage;
        }

        public static class Item extends Syphon {

            private final ItemStack stack;

            public Item(SoulNetwork network, SoulTicket ticket, ItemStack stack) {
                super(network, ticket);

                this.stack = stack;
            }

            public ItemStack getStack() {
                return stack;
            }
        }

        public static class User extends Syphon {

            private final EntityPlayer user;

            public User(SoulNetwork network, SoulTicket ticket, EntityPlayer user) {
                super(network, ticket);

                this.user = user;
            }

            public EntityPlayer getUser() {
                return user;
            }
        }
    }

    @Cancelable
    public static class Fill extends SoulNetworkEvent {

        private int maximum;

        public Fill(SoulNetwork network, SoulTicket ticket, int maximum) {
            super(network, ticket);

            this.maximum = maximum;
        }

        public int getMaximum() {
            return maximum;
        }

        public void setMaximum(int maximum) {
            this.maximum = maximum;
        }
    }
}
