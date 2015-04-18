package WayofTime.alchemicalWizardry.client.renderer;

import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.rituals.Rituals;

public class Helper
{
    public static RitualEffect getEffectFromString(String name)
    {
        Rituals ritual = Rituals.ritualMap.get(name);

        if (ritual == null)
            return null;

        return ritual.effect;
    }
}
