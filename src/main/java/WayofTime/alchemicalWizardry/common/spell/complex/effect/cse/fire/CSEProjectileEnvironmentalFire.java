package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.fire;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmProjectile;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.ProjectileEnvironmentalFire;

public class CSEProjectileEnvironmentalFire extends ComplexSpellEffect
{
	public CSEProjectileEnvironmentalFire() 
	{
		super(ComplexSpellType.FIRE, ComplexSpellModifier.ENVIRONMENTAL);
	}
	
	public CSEProjectileEnvironmentalFire(int power, int cost, int potency)
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
			((SpellParadigmProjectile) parad).addUpdateEffect(new ProjectileEnvironmentalFire(powerEnhancement, potencyEnhancement, costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEProjectileEnvironmentalFire(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (75 * (0.5 * this.powerEnhancement + 1) * (0.5 * this.potencyEnhancement + 1) * Math.pow(0.85, costEnhancement));
	}
}
