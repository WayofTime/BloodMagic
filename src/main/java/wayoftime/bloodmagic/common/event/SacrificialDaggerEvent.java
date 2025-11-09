package wayoftime.bloodmagic.common.event;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class SacrificialDaggerEvent extends Event implements ICancellableEvent {

    public Player player;
    public boolean shouldDrainHealth;
    public boolean shouldFillAltar;
    public int hpLost;
    public int lpAdded;

    public SacrificialDaggerEvent(Player player, boolean shouldDrainHealth, boolean shouldFillAltar, int hpLost, int lpAdded) {
        this.player = player;
        this.shouldDrainHealth = shouldDrainHealth;
        this.shouldFillAltar = shouldFillAltar;
        this.hpLost = hpLost;
        this.lpAdded = lpAdded;
    }
}
