package WayofTime.alchemicalWizardry.common.spell.complex;

public class SpellModifier 
{
	public static final int DEFAULT = 0;
	public static final int OFFENSIVE = 1;
	public static final int DEFENSIVE = 2;
	public static final int ENVIRONMENTAL = 3;
	
	private int modifier;
	
	protected SpellModifier(int modifier)
	{
		this.modifier = modifier;
	}
	
	public int getModifier()
	{
		return this.modifier;
	}
}
