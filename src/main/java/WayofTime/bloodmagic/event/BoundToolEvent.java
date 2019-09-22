package WayofTime.bloodmagic.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class BoundToolEvent extends Event {
    public PlayerEntity player;

    public BoundToolEvent(PlayerEntity player) {
        this.player = player;
    }

    /**
     * This event is called when a
     * {@link WayofTime.bloodmagic.item.ItemBoundTool} is being charged.
     * <p>
     * If canceled, will result in the charging being canceled.
     */

    @Cancelable
    public static class Charge extends BoundToolEvent {
        public ItemStack result;

        public Charge(PlayerEntity player, ItemStack result) {
            super(player);
            this.result = result;
        }
    }

    /**
     * This event is called when a
     * {@link WayofTime.bloodmagic.item.ItemBoundTool}'s charge is released.
     * <p>
     * If canceled, will result in the charge not being released.
     */

    @Cancelable
    public static class Release extends BoundToolEvent {
        public final ItemStack boundTool;
        public int charge;

        public Release(PlayerEntity player, ItemStack boundTool, int charge) {
            super(player);
            this.boundTool = boundTool;
            this.charge = charge;
        }
    }
}
