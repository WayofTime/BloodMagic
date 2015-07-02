package WayofTime.alchemicalWizardry.api.alchemy.energy;

public interface IReagentContainer
{
    ReagentStack getReagent();

    int getReagentStackAmount();

    int getCapacity();

    int fill(ReagentStack resource, boolean doFill);

    ReagentStack drain(int maxDrain, boolean doDrain);

    ReagentContainerInfo getInfo();
}
