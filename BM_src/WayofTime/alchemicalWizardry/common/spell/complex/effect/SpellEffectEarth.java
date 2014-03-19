package WayofTime.alchemicalWizardry.common.spell.complex.effect;

import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmMelee;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmProjectile;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmSelf;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth.MeleeDefaultEarth;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth.MeleeDefensiveEarth;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth.MeleeEnvironmentalEarth;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth.MeleeOffensiveEarth;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth.ProjectileDefaultEarth;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth.ProjectileDefensiveEarth;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth.ProjectileEnvironmentalEarth;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth.ProjectileOffensiveEarth;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth.SelfDefaultEarth;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth.SelfDefensiveEarth;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth.SelfEnvironmentalEarth;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth.SelfOffensiveEarth;

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
		parad.addSelfSpellEffect(new SelfDefaultEarth(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
	}

	@Override
	public void offensiveModificationSelf(SpellParadigmSelf parad) 
	{
		parad.addSelfSpellEffect(new SelfOffensiveEarth(this.powerEnhancement,this.potencyEnhancement, this.costEnhancement));
	}

	@Override
	public void defensiveModificationSelf(SpellParadigmSelf parad)
	{
		parad.addSelfSpellEffect(new SelfDefensiveEarth(this.powerEnhancement,this.potencyEnhancement, this.costEnhancement));
	}

	@Override
	public void environmentalModificationSelf(SpellParadigmSelf parad) 
	{
		parad.addSelfSpellEffect(new SelfEnvironmentalEarth(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
	}

	@Override
	public void defaultModificationMelee(SpellParadigmMelee parad) 
	{
		parad.addWorldEffect(new MeleeDefaultEarth(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
	}

	@Override
	public void offensiveModificationMelee(SpellParadigmMelee parad)
	{
		parad.addWorldEffect(new MeleeOffensiveEarth(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
	}

	@Override
	public void defensiveModificationMelee(SpellParadigmMelee parad) 
	{
		parad.addWorldEffect(new MeleeDefensiveEarth(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
	}

	@Override
	public void environmentalModificationMelee(SpellParadigmMelee parad) 
	{
		parad.addWorldEffect(new MeleeEnvironmentalEarth(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
	}

	@Override
	protected int getCostForDefaultProjectile() 
	{
		return (int)(10*Math.pow((0.5*(this.powerEnhancement)+1)*2 + 1,3)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForOffenseProjectile()
	{
		
		return (int)(10*(1.5*this.potencyEnhancement+1)*(Math.pow(1*this.powerEnhancement+1,2))*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForDefenseProjectile() 
	{
		return (int)(3*Math.pow((this.powerEnhancement*2+1),2)*(this.potencyEnhancement*2+1)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForEnvironmentProjectile() 
	{
		return (int)(10*2*(0.1d*(this.potencyEnhancement+1))*Math.pow(3.47,this.potencyEnhancement)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForDefaultSelf()
	{
		return (int)(20*Math.pow(1.5*powerEnhancement+1,2)*(2*this.potencyEnhancement+1)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForOffenseSelf()
	{
		return (int)(10*Math.pow(2*this.powerEnhancement+1,2)*(this.potencyEnhancement+1)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForDefenseSelf() 
	{
		return (int)(750*(1.1*this.powerEnhancement+1)*(0.5*this.potencyEnhancement+1)*Math.pow(0.85, costEnhancement));
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
		return (int)(50*Math.pow(1.5*this.potencyEnhancement + 1,3)*(0.5*this.powerEnhancement + 1)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForOffenseMelee()
	{
		return (int)(20*Math.pow(1.5*this.powerEnhancement+1,3)*(0.25*this.powerEnhancement+1)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForDefenseMelee() 
	{
		return (int)(5*(1.2*this.powerEnhancement+1)*(1.0f/3.0f*Math.pow(this.potencyEnhancement,2)+2+1.0f/2.0f*this.potencyEnhancement)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForEnvironmentMelee() 
	{
		return (int)(500*Math.pow(2*this.potencyEnhancement+1, 3)*(0.25*this.powerEnhancement+1)*Math.pow(0.85, costEnhancement));
	}
}
