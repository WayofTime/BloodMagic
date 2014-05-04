package WayofTime.alchemicalWizardry.api.rituals;

import java.util.List;

public abstract class RitualEffect
{
    public abstract void performEffect(IMasterRitualStone ritualStone);

    public abstract int getCostPerRefresh();

    public int getInitialCooldown()
    {
        return 0;
    }
    
    public abstract List<RitualComponent> getRitualComponentList();
}
