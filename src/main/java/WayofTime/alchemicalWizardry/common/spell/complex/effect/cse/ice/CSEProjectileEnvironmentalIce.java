package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.ice;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmProjectile;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice.ProjectileEnvironmentalIce;

public class CSEProjectileEnvironmentalIce extends ComplexSpellEffect
{
	public CSEProjectileEnvironmentalIce() 
	{
		super(ComplexSpellType.ICE, ComplexSpellModifier.ENVIRONMENTAL);
	}
	
	public CSEProjectileEnvironmentalIce(int power, int cost, int potency)
	{
		this();
		
		this.powerEnhancement = power;
		this.costEnhancement = cost;
		this.potencyEnhancement = potency;
	}

	@Override
	public void modifyParadigm(SpellParadigm parad) 
	{
		if(parad instanceof SpellParadigmProjectile)
		{
			((SpellParadigmProjectile)parad).addUpdateEffect(new ProjectileEnvironmentalIce(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEProjectileEnvironmentalIce(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (200 * (2 * this.powerEnhancement + 1) * (2 * this.potencyEnhancement + 1) * Math.pow(0.85, costEnhancement));
	}
}
