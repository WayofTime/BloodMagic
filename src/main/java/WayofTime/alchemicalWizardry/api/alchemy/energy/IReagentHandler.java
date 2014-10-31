package WayofTime.alchemicalWizardry.api.alchemy.energy;

import net.minecraftforge.common.util.ForgeDirection;

public interface IReagentHandler
{
    int fill(ForgeDirection from, ReagentStack resource, boolean doFill);

    ReagentStack drain(ForgeDirection from, ReagentStack resource, boolean doDrain);

    ReagentStack drain(ForgeDirection from, int maxDrain, boolean doDrain);

    boolean canFill(ForgeDirection from, Reagent reagent);

    boolean canDrain(ForgeDirection from, Reagent reagent);

    ReagentContainerInfo[] getContainerInfo(ForgeDirection from);
}
