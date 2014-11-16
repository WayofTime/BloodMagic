package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.ice;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmProjectile;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice.ProjectileDefaultIce;

public class CSEProjectileDefaultIce extends ComplexSpellEffect
{
	public CSEProjectileDefaultIce() 
	{
		super(ComplexSpellType.ICE, ComplexSpellModifier.DEFAULT);
	}
	
	public CSEProjectileDefaultIce(int power, int cost, int potency)
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
			((SpellParadigmProjectile)parad).damage += this.potencyEnhancement;
			((SpellParadigmProjectile)parad).addImpactEffect(new ProjectileDefaultIce(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEProjectileDefaultIce(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) ((30) * (this.potencyEnhancement + 1) * Math.pow(0.85, costEnhancement));
	}
}
