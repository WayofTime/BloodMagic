package WayofTime.alchemicalWizardry.api.rituals;

public enum RitualBreakMethod 
{
	REDSTONE,
	BREAK_MRS,
	BREAK_STONE,
	ACTIVATE,		//When an activation crystal activates the MRS, overwriting the current ritual
	DEACTIVATE, 
	EXPLOSION,		//When the MRS is destroyed by an explosion
}
