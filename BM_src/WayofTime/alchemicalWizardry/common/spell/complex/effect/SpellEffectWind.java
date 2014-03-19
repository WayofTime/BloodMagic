package WayofTime.alchemicalWizardry.common.spell.complex.effect;

import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmMelee;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmProjectile;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmSelf;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind.MeleeOffensiveWind;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind.ProjectileDefaultWind;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind.ProjectileEnvironmentalWind;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind.ProjectileOffensiveWind;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind.SelfEnvironmentalWind;

public class SpellEffectWind extends SpellEffect 
{
	@Override
	public void defaultModificationProjectile(SpellParadigmProjectile parad) 
	{
		parad.addImpactEffect(new ProjectileDefaultWind(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
	}

	@Override
	public void offensiveModificationProjectile(SpellParadigmProjectile parad) 
	{
		parad.addImpactEffect(new ProjectileOffensiveWind(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
	}

	@Override
	public void defensiveModificationProjectile(SpellParadigmProjectile parad) 
	{
		parad.ricochetMax+=this.potencyEnhancement;
	}

	@Override
	public void environmentalModificationProjectile(SpellParadigmProjectile parad)
	{
		parad.addUpdateEffect(new ProjectileEnvironmentalWind(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
	}

	@Override
	public void defaultModificationSelf(SpellParadigmSelf parad) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void offensiveModificationSelf(SpellParadigmSelf parad) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void defensiveModificationSelf(SpellParadigmSelf parad)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void environmentalModificationSelf(SpellParadigmSelf parad) 
	{
		parad.addSelfSpellEffect(new SelfEnvironmentalWind(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
	}

	@Override
	public void defaultModificationMelee(SpellParadigmMelee parad) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void offensiveModificationMelee(SpellParadigmMelee parad)
	{
		parad.addEntityEffect(new MeleeOffensiveWind(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
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
		return (int)(100*(this.potencyEnhancement+1)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForOffenseProjectile()
	{
		return (int)(100*(0.5*this.potencyEnhancement+1)*(this.powerEnhancement+1)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForDefenseProjectile() 
	{
		return (int)(100*(this.potencyEnhancement+1));
	}

	@Override
	protected int getCostForEnvironmentProjectile() 
	{
		return (int)(50*(this.powerEnhancement+1)*(this.potencyEnhancement+1)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForDefaultSelf()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getCostForOffenseSelf()
	{
		// TODO Auto-generated method stub
		return 0;
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
		return (int)(500*(0.7d*this.powerEnhancement+1)*(0.2*this.potencyEnhancement+1)*Math.pow(0.85, costEnhancement));
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
		return (int)(250*(1.0*this.potencyEnhancement+1)*(0.7*this.powerEnhancement+1)*Math.pow(0.85, costEnhancement));
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
