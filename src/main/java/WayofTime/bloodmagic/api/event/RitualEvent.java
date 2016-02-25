package WayofTime.bloodmagic.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import WayofTime.bloodmagic.api.ritual.IMasterRitualStone;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.imperfect.IImperfectRitualStone;
import WayofTime.bloodmagic.api.ritual.imperfect.ImperfectRitual;

public class RitualEvent extends Event
{
    public final IMasterRitualStone mrs;
    public final String ownerName;
    public final Ritual ritual;

    private RitualEvent(IMasterRitualStone mrs, String ownerName, Ritual ritual)
    {
        this.mrs = mrs;
        this.ownerName = ownerName;
        this.ritual = ritual;
    }

    /**
     * This event is called when a ritual is activated. If cancelled, it will
     * not activate.
     * 
     * {@link WayofTime.bloodmagic.tile.TileMasterRitualStone#activateRitual(ItemStack, EntityPlayer, Ritual)}
     */
    @Cancelable
    public static class RitualActivatedEvent extends RitualEvent
    {
        public final EntityPlayer player;
        public final ItemStack crystalStack;
        public int crystalTier;

        public RitualActivatedEvent(IMasterRitualStone mrs, String owner, Ritual ritual, EntityPlayer player, ItemStack activationCrystal, int crystalTier)
        {
            super(mrs, owner, ritual);

            this.player = player;
            this.crystalStack = activationCrystal;
            this.crystalTier = crystalTier;
        }
    }

    /**
     * This event is called when a Ritual effect is performed. If cancelled, the
     * effect will not happen.
     * 
     * {@link WayofTime.bloodmagic.tile.TileMasterRitualStone#performRitual(World, BlockPos)}
     */
    @Cancelable
    public static class RitualRunEvent extends RitualEvent
    {

        public RitualRunEvent(IMasterRitualStone mrs, String owner, Ritual ritual)
        {
            super(mrs, owner, ritual);
        }
    }

    /**
     * This event is called when a Ritual is stopped by a
     * {@link Ritual.BreakType}.
     * 
     * {@link WayofTime.bloodmagic.tile.TileMasterRitualStone#stopRitual(Ritual.BreakType)}
     * */
    public static class RitualStopEvent extends RitualEvent
    {

        public final Ritual.BreakType method;

        public RitualStopEvent(IMasterRitualStone mrs, String owner, Ritual ritual, Ritual.BreakType method)
        {
            super(mrs, owner, ritual);

            this.method = method;
        }
    }

    @Cancelable
    public static class ImperfectRitualActivatedEvent extends Event
    {

        public final IImperfectRitualStone ims;
        public final String ownerName;
        public final ImperfectRitual imperfectRitual;

        public ImperfectRitualActivatedEvent(IImperfectRitualStone ims, String ownerName, ImperfectRitual imperfectRitual)
        {
            this.ims = ims;
            this.ownerName = ownerName;
            this.imperfectRitual = imperfectRitual;
        }
    }
}
