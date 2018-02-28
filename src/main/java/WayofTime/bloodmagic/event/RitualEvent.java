package WayofTime.bloodmagic.event;

import WayofTime.bloodmagic.ritual.data.IMasterRitualStone;
import WayofTime.bloodmagic.ritual.data.Ritual;
import WayofTime.bloodmagic.ritual.data.imperfect.IImperfectRitualStone;
import WayofTime.bloodmagic.ritual.data.imperfect.ImperfectRitual;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.UUID;

public class RitualEvent extends Event {
    public final IMasterRitualStone mrs;
    public final UUID ownerId;
    public final Ritual ritual;

    private RitualEvent(IMasterRitualStone mrs, UUID ownerId, Ritual ritual) {
        this.mrs = mrs;
        this.ownerId = ownerId;
        this.ritual = ritual;
    }

    /**
     * This event is called when a ritual is activated. If cancelled, it will
     * not activate.
     * <p>
     * {@link WayofTime.bloodmagic.tile.TileMasterRitualStone#activateRitual(ItemStack, EntityPlayer, Ritual)}
     */
    @Cancelable
    public static class RitualActivatedEvent extends RitualEvent {
        public final EntityPlayer player;
        public final ItemStack crystalStack;
        public int crystalTier;

        public RitualActivatedEvent(IMasterRitualStone mrs, UUID ownerId, Ritual ritual, EntityPlayer player, ItemStack activationCrystal, int crystalTier) {
            super(mrs, ownerId, ritual);

            this.player = player;
            this.crystalStack = activationCrystal;
            this.crystalTier = crystalTier;
        }
    }

    /**
     * This event is called when a Ritual effect is performed. If cancelled, the
     * effect will not happen.
     * <p>
     * {@link WayofTime.bloodmagic.tile.TileMasterRitualStone#performRitual(World, net.minecraft.util.math.BlockPos)}
     */
    @Cancelable
    public static class RitualRunEvent extends RitualEvent {
        public RitualRunEvent(IMasterRitualStone mrs, UUID ownerId, Ritual ritual) {
            super(mrs, ownerId, ritual);
        }
    }

    /**
     * This event is called when a Ritual is stopped by a
     * {@link Ritual.BreakType}.
     * <p>
     * {@link WayofTime.bloodmagic.tile.TileMasterRitualStone#stopRitual(Ritual.BreakType)}
     */
    public static class RitualStopEvent extends RitualEvent {

        public final Ritual.BreakType method;

        public RitualStopEvent(IMasterRitualStone mrs, UUID ownerId, Ritual ritual, Ritual.BreakType method) {
            super(mrs, ownerId, ritual);

            this.method = method;
        }
    }

    @Cancelable
    public static class ImperfectRitualActivatedEvent extends Event {

        public final IImperfectRitualStone ims;
        public final UUID ownerId;
        public final ImperfectRitual imperfectRitual;

        public ImperfectRitualActivatedEvent(IImperfectRitualStone ims, UUID ownerId, ImperfectRitual imperfectRitual) {
            this.ims = ims;
            this.ownerId = ownerId;
            this.imperfectRitual = imperfectRitual;
        }
    }
}
