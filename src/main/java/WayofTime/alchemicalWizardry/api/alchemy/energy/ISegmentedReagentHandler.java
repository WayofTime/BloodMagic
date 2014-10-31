package WayofTime.alchemicalWizardry.api.alchemy.energy;

import java.util.Map;

public interface ISegmentedReagentHandler extends IReagentHandler
{
    public int getNumberOfTanks();

    public int getTanksTunedToReagent(Reagent reagent);

    public void setTanksTunedToReagent(Reagent reagent, int total);

    public Map<Reagent, Integer> getAttunedTankMap();
}
