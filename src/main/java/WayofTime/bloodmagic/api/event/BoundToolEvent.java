package WayofTime.bloodmagic.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class BoundToolEvent extends Event
{
    public EntityPlayer player;

    public BoundToolEvent(EntityPlayer player)
    {
        this.player = player;
    }

    @Cancelable
    public static class Charge extends BoundToolEvent
    {

        public ItemStack result;

        public Charge(EntityPlayer player, ItemStack result)
        {
            super(player);
            this.result = result;
        }
    }

    @Cancelable
    public static class Release extends BoundToolEvent
    {

        public final ItemStack boundTool;
        public int charge;

        public Release(EntityPlayer player, ItemStack boundTool, int charge)
        {
            super(player);
            this.boundTool = boundTool;
            this.charge = charge;
        }
    }
}
