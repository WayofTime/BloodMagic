package WayofTime.alchemicalWizardry.common.spell.complex.effect;

import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmMelee;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmProjectile;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmSelf;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.ProjectileDefaultFire;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.ProjectileDefensiveFire;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.ProjectileEnvironmentalFire;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.ProjectileOffensiveFire;
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
		parad.addImpactEffect(new ProjectileOffensiveFire(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
	}

	@Override
	public void defensiveModificationProjectile(SpellParadigmProjectile parad) 
	{
		parad.addImpactEffect(new ProjectileDefensiveFire(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
	}

	@Override
	public void environmentalModificationProjectile(SpellParadigmProjectile parad) 
	{
		parad.addUpdateEffect(new ProjectileEnvironmentalFire(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
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
		return (int)((5*Math.pow(1.5*this.powerEnhancement+1, 2)*(1.5*this.potencyEnhancement+1)+this.potencyEnhancement*15)*Math.pow(0.8, costEnhancement));
	}

	@Override
	protected int getCostForOffenseProjectile() 
	{
		return (int)(10*Math.pow((this.powerEnhancement)*1.3+1,2)*((1.5*this.potencyEnhancement+1))*Math.pow(0.8, costEnhancement));
	}

	@Override
	protected int getCostForDefenseProjectile() 
	{
		return (int)(25*Math.pow(1*this.powerEnhancement+1,2)*(1*this.potencyEnhancement+1)*Math.pow(0.8, costEnhancement));
	}

	@Override
	protected int getCostForEnvironmentProjectile() 
	{
		return (int)(75*(0.5*this.powerEnhancement+1)*(0.5*this.potencyEnhancement+1)*Math.pow(0.8, costEnhancement));
	}

	@Override
	protected int getCostForDefaultSelf() 
	{
		return 10*(int)(10*Math.pow(1.5, this.powerEnhancement+1.5*this.potencyEnhancement)*Math.pow(0.8, costEnhancement));
	}

	@Override
	protected int getCostForOffenseSelf() 
	{
		return 500*(int)((this.powerEnhancement+1)*Math.pow(2, potencyEnhancement)*Math.pow(0.8, costEnhancement));
	}

	@Override
	protected int getCostForDefenseSelf() 
	{
		return (int)(25*(3*this.potencyEnhancement+1)*(2*this.powerEnhancement+1)*Math.pow(0.8, costEnhancement));
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
