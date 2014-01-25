package WayofTime.alchemicalWizardry.common.spell.complex.effect;

import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmMelee;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmProjectile;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmSelf;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ProjectileDefaultFire;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.SelfDefaultFire;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.SelfDefensiveFire;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.SelfEnvironmentalFire;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.SelfOffensiveFire;

public class SpellEffectFire extends SpellEffect
{
	@Override
	public void defaultModificationProjectile(SpellParadigmProjectile parad) 
	{
		parad.addImpactEffect(new ProjectileDefaultFire(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
		parad.damage+=this.potencyEnhancement;
	}

	@Override
	public void offensiveModificationProjectile(SpellParadigmProjectile parad) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void defensiveModificationProjectile(SpellParadigmProjectile parad) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void environmentalModificationProjectile(SpellParadigmProjectile parad) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void defaultModificationSelf(SpellParadigmSelf parad) 
	{
		parad.addSelfSpellEffect(new SelfDefaultFire(powerEnhancement, potencyEnhancement, costEnhancement));
	}

	@Override
	public void offensiveModificationSelf(SpellParadigmSelf parad) 
	{
		parad.addSelfSpellEffect(new SelfOffensiveFire(powerEnhancement,potencyEnhancement,costEnhancement));
	}

	@Override
	public void defensiveModificationSelf(SpellParadigmSelf parad) 
	{
		parad.addSelfSpellEffect(new SelfDefensiveFire(powerEnhancement,potencyEnhancement,costEnhancement));
	}

	@Override
	public void environmentalModificationSelf(SpellParadigmSelf parad) 
	{
		parad.addSelfSpellEffect(new SelfEnvironmentalFire(powerEnhancement, potencyEnhancement, costEnhancement));
	}

	@Override
	public void defaultModificationMelee(SpellParadigmMelee parad) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void offensiveModificationMelee(SpellParadigmMelee parad) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void defensiveModificationMelee(SpellParadigmMelee parad) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void environmentalModificationMelee(SpellParadigmMelee parad) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected int getCostForDefaultProjectile() 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getCostForOffenseProjectile() 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getCostForDefenseProjectile() 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getCostForEnvironmentProjectile() 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getCostForDefaultSelf() 
	{
		return 10*(int)(10*Math.pow(1.5, this.powerEnhancement+1.5*this.potencyEnhancement));
	}

	@Override
	protected int getCostForOffenseSelf() 
	{
		return 500*(int)((this.powerEnhancement+1)*Math.pow(2, potencyEnhancement));
	}

	@Override
	protected int getCostForDefenseSelf() 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getCostForEnvironmentSelf() 
	{
		return (int) ((15*Math.pow(1.7, powerEnhancement)+10*Math.pow(potencyEnhancement,1.8))*Math.pow(0.8, costEnhancement));
	}

	@Override
	protected int getCostForDefaultMelee() 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getCostForOffenseMelee() 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getCostForDefenseMelee() 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getCostForEnvironmentMelee() 
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
