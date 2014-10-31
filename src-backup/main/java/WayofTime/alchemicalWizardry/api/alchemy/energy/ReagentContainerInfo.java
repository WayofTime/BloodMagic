package WayofTime.alchemicalWizardry.api.alchemy.energy;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

public final class ReagentContainerInfo
{
    public final ReagentStack reagent;
    public final int capacity;

    public ReagentContainerInfo(ReagentStack reagent, int capacity)
    {
        this.reagent = reagent;
        this.capacity = capacity;
    }

    public ReagentContainerInfo(IReagentContainer tank)
    {
        this.reagent = tank.getReagent();
        this.capacity = tank.getCapacity();
    }
}
