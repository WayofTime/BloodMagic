package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.tool;

import WayofTime.alchemicalWizardry.api.spell.IOnBreakBlock;

public abstract class OnBreakBlockEffect implements IOnBreakBlock
{
    protected int powerUpgrades;
    protected int potencyUpgrades;
    protected int costUpgrades;

    public OnBreakBlockEffect(int power, int potency, int cost)
    {
        this.powerUpgrades = power;
        this.potencyUpgrades = potency;
        this.costUpgrades = cost;
    }
}
