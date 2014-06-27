package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.tool;

public abstract class RightClickEffect implements IRightClickEffect
{
	protected int powerUpgrades;
	protected int potencyUpgrades;
	protected int costUpgrades;
	
	public RightClickEffect(int power, int potency, int cost)
	{
		this.powerUpgrades = power;
		this.potencyUpgrades = potency;
		this.costUpgrades = cost;
	}
}
