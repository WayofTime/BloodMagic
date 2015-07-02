package WayofTime.alchemicalWizardry.api.alchemy.energy;

import java.util.Map;

public interface ISegmentedReagentHandler extends IReagentHandler
{
    int getNumberOfTanks();

    int getTanksTunedToReagent(Reagent reagent);

    void setTanksTunedToReagent(Reagent reagent, int total);

    Map<Reagent, Integer> getAttunedTankMap();
}
