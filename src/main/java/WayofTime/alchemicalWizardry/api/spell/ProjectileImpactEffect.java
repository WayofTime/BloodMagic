package WayofTime.alchemicalWizardry.api.spell;

public abstract class ProjectileImpactEffect implements IProjectileImpactEffect
{
    protected int powerUpgrades;
    protected int potencyUpgrades;
    protected int costUpgrades;

    public ProjectileImpactEffect(int power, int potency, int cost)
    {
        this.powerUpgrades = power;
        this.potencyUpgrades = potency;
        this.costUpgrades = cost;
    }
}
