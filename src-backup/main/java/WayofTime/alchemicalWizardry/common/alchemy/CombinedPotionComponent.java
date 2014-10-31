package WayofTime.alchemicalWizardry.common.alchemy;

import net.minecraft.potion.Potion;

public class CombinedPotionComponent 
{
	public Potion result;
	public Potion pot1;
	public Potion pot2;
	
	public CombinedPotionComponent(Potion result, Potion pot1, Potion pot2)
	{
		this.result = result;
		this.pot1 = pot1;
		this.pot2 = pot2;
	}
	
	public boolean isRecipeValid(Potion test1, Potion test2)
	{
		return (test1 == pot1 && test2 == pot2) || (test1 == pot2 && test2 == pot1);
	}
	
	public boolean isRecipeValid(int test1, int test2)
	{
		return (test1 == pot1.id && test2 == pot2.id) || (test1 == pot2.id && test2 == pot1.id);
	}
}
