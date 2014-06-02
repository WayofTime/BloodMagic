package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.tool;

public abstract class SummonToolEffect implements IOnSummonTool
{
	protected int powerUpgrades;
	protected int potencyUpgrades;
	protected int costUpgrades;
	
	public SummonToolEffect(int power, int potency, int cost)
	{
		this.powerUpgrades = power;
		this.potencyUpgrades = potency;
		this.costUpgrades = cost;
	}
}
