package WayofTime.alchemicalWizardry.api.spell;

public abstract class SelfSpellEffect implements ISelfSpellEffect
{
    protected int powerUpgrades;
    protected int potencyUpgrades;
    protected int costUpgrades;

    public SelfSpellEffect(int power, int potency, int cost)
    {
        this.powerUpgrades = power;
        this.potencyUpgrades = potency;
        this.costUpgrades = cost;
    }
}
