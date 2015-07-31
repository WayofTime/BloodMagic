package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.earth;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmProjectile;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth.ProjectileDefaultEarth;

public class CSEProjectileDefaultEarth extends ComplexSpellEffect
{
	public CSEProjectileDefaultEarth() 
	{
		super(ComplexSpellType.EARTH, ComplexSpellModifier.DEFAULT);
	}
	
	public CSEProjectileDefaultEarth(int power, int cost, int potency)
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
			((SpellParadigmProjectile)parad).addImpactEffect(new ProjectileDefaultEarth(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
		}
	}

	@Override
	public ComplexSpellEffect copy(int power, int cost, int potency) 
	{
		return new CSEProjectileDefaultEarth(power, cost, potency);
	}

	@Override
	public int getCostOfEffect() 
	{
        return (int) (10 * Math.pow((0.5 * (this.powerEnhancement) + 1) * 2 + 1, 3) * Math.pow(0.85, costEnhancement));
	}
}
