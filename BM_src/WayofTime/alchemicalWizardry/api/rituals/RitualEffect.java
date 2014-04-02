package WayofTime.alchemicalWizardry.api.rituals;

public abstract class RitualEffect
{
    public abstract void performEffect(IMasterRitualStone ritualStone);

    public abstract int getCostPerRefresh();

    public int getInitialCooldown()
    {
        return 0;
    }
}
