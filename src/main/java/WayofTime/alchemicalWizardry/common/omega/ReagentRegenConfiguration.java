package WayofTime.alchemicalWizardry.common.omega;

public class ReagentRegenConfiguration 
{
	public int tickRate;
	public int healPerTick;
	public float costPerPoint;
	
	public ReagentRegenConfiguration(int tickRate, int healPerTick, float costPerPoint)
	{
		this.tickRate = tickRate;
		this.healPerTick = healPerTick;
		this.costPerPoint = costPerPoint;
	}
}
