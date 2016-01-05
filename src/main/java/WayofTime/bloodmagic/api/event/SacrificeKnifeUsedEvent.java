package WayofTime.bloodmagic.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class SacrificeKnifeUsedEvent extends Event
{
    public final EntityPlayer player;
    public final int healthDrained;
    public int lpAdded;
    public boolean shouldDrainHealth;
    public boolean shouldFillAltar;

    public SacrificeKnifeUsedEvent(EntityPlayer player, boolean shouldDrainHealth, boolean shouldFillAltar, int hp, int lpAdded)
    {
        this.player = player;
        this.shouldDrainHealth = shouldDrainHealth;
        this.shouldFillAltar = shouldFillAltar;
        this.healthDrained = hp;
        this.lpAdded = lpAdded;
    }
}