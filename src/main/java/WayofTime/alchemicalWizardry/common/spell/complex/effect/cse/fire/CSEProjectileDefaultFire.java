package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.fire;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmProjectile;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.ProjectileDefaultFire;

public class CSEProjectileDefaultFire extends ComplexSpellEffect
{
	public CSEProjectileDefaultFire() 
	{
		super(ComplexSpellType.FIRE, ComplexSpellModifier.DEFAULT);
	}
	
	public CSEProjectileDefaultFire(int power, int cost, int potency)
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
			((SpellParadigmProjectile) parad).addImpactEffect(new ProjectileDefaultFire(powerEnhancement, potencyEnhancement, costEnhancement));
			((SpellParadigmProjectile) parad).damage += this.potencyEnhancement;
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEProjectileDefaultFire(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) ((5 * Math.pow(1.5 * this.powerEnhancement + 1, 2) * (1.5 * this.potencyEnhancement + 1) + this.potencyEnhancement * 15) * Math.pow(0.85, costEnhancement));
	}
}
