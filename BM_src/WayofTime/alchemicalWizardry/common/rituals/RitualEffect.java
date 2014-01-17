package WayofTime.alchemicalWizardry.common.rituals;

import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;

public abstract class RitualEffect {
    public abstract void performEffect(TEMasterStone ritualStone);

    public abstract int getCostPerRefresh();

    public int getInitialCooldown()
    {
        return 0;
    }
}
