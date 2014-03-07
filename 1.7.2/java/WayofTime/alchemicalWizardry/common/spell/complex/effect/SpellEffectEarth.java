package WayofTime.alchemicalWizardry.common.spell.complex.effect;

import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmMelee;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmProjectile;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmSelf;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth.MeleeDefaultEarth;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth.ProjectileDefaultEarth;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth.ProjectileDefensiveEarth;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth.ProjectileEnvironmentalEarth;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth.ProjectileOffensiveEarth;

public class SpellEffectEarth extends SpellEffect 
{
	@Override
	public void defaultModificationProjectile(SpellParadigmProjectile parad) 
	{
		parad.addImpactEffect(new ProjectileDefaultEarth(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
	}

	@Override
	public void offensiveModificationProjectile(SpellParadigmProjectile parad) 
	{
		parad.addImpactEffect(new ProjectileOffensiveEarth(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
	}

	@Override
	public void defensiveModificationProjectile(SpellParadigmProjectile parad) 
	{
		parad.addImpactEffect(new ProjectileDefensiveEarth(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
	}

	@Override
	public void environmentalModificationProjectile(SpellParadigmProjectile parad)
	{
		parad.addUpdateEffect(new ProjectileEnvironmentalEarth(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
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
		// TODO Auto-generated method stub

	}

	@Override
	public void defaultModificationMelee(SpellParadigmMelee parad) 
	{
		parad.addWorldEffect(new MeleeDefaultEarth(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
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
		return (int)(10*Math.pow((0.5*(this.powerEnhancement)+1)*2 + 1,3)*Math.pow(0.8, costEnhancement));
	}

	@Override
	protected int getCostForOffenseProjectile()
	{
		
		return (int)(3*(1.5*this.potencyEnhancement+1)*(Math.pow(1*this.powerEnhancement+1,2))*Math.pow(0.8, costEnhancement));
	}

	@Override
	protected int getCostForDefenseProjectile() 
	{
		return (int)(3*Math.pow((this.powerEnhancement*2+1),2)*(this.potencyEnhancement*2+1)*Math.pow(0.8, costEnhancement));
	}

	@Override
	protected int getCostForEnvironmentProjectile() 
	{
		return (int)(10*2*(0.1d*(this.potencyEnhancement+1))*Math.pow(3.47,this.potencyEnhancement)*Math.pow(0.8, costEnhancement));
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
		// TODO Auto-generated method stub
		return 0;
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
