package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.earth;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmProjectile;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth.ProjectileEnvironmentalEarth;

public class CSEProjectileEnvironmentalEarth extends ComplexSpellEffect
{
	public CSEProjectileEnvironmentalEarth() 
	{
		super(ComplexSpellType.EARTH, ComplexSpellModifier.ENVIRONMENTAL);
	}
	
	public CSEProjectileEnvironmentalEarth(int power, int cost, int potency)
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
			((SpellParadigmProjectile)parad).addUpdateEffect(new ProjectileEnvironmentalEarth(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEProjectileEnvironmentalEarth(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (10 * 2 * (0.1d * (this.potencyEnhancement + 1)) * Math.pow(3.47, this.potencyEnhancement) * Math.pow(0.85, costEnhancement));
	}
}
