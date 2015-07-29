package WayofTime.alchemicalWizardry.api.alchemy.energy;

import net.minecraft.util.EnumFacing;

public interface IReagentHandler
{
    int fill(EnumFacing from, ReagentStack resource, boolean doFill);

    ReagentStack drain(EnumFacing from, ReagentStack resource, boolean doDrain);

    ReagentStack drain(EnumFacing from, int maxDrain, boolean doDrain);

    boolean canFill(EnumFacing from, Reagent reagent);

    boolean canDrain(EnumFacing from, Reagent reagent);

    ReagentContainerInfo[] getContainerInfo(EnumFacing from);
}
