package WayofTime.alchemicalWizardry.common.spell.complex.effect;

import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmMelee;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmProjectile;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmSelf;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind.MeleeDefaultWind;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind.MeleeDefensiveWind;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind.MeleeEnvironmentalWind;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind.MeleeOffensiveWind;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind.ProjectileDefaultWind;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind.ProjectileEnvironmentalWind;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind.ProjectileOffensiveWind;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind.SelfDefaultWind;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind.SelfDefensiveWind;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind.SelfEnvironmentalWind;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind.SelfOffensiveWind;

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
		parad.isSilkTouch = true;
	}

	@Override
	public void environmentalModificationProjectile(SpellParadigmProjectile parad)
	{
		parad.addUpdateEffect(new ProjectileEnvironmentalWind(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
	}

	@Override
	public void defaultModificationSelf(SpellParadigmSelf parad) 
	{
		parad.addSelfSpellEffect(new SelfDefaultWind(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
	}

	@Override
	public void offensiveModificationSelf(SpellParadigmSelf parad) 
	{
		parad.addSelfSpellEffect(new SelfOffensiveWind(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
	}

	@Override
	public void defensiveModificationSelf(SpellParadigmSelf parad)
	{
		parad.addSelfSpellEffect(new SelfDefensiveWind(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
	}

	@Override
	public void environmentalModificationSelf(SpellParadigmSelf parad) 
	{
		parad.addSelfSpellEffect(new SelfEnvironmentalWind(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
	}

	@Override
	public void defaultModificationMelee(SpellParadigmMelee parad) 
	{
		parad.addEntityEffect(new MeleeDefaultWind(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
	}

	@Override
	public void offensiveModificationMelee(SpellParadigmMelee parad)
	{
		parad.addEntityEffect(new MeleeOffensiveWind(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
	}

	@Override
	public void defensiveModificationMelee(SpellParadigmMelee parad) 
	{
		parad.addEntityEffect(new MeleeDefensiveWind(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
	}

	@Override
	public void environmentalModificationMelee(SpellParadigmMelee parad) 
	{
		parad.addWorldEffect(new MeleeEnvironmentalWind(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
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
		return (int)(100*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForOffenseSelf()
	{
		return (int)(100*(0.5*this.powerEnhancement+1)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForDefenseSelf() 
	{
		return (int)(500*(0.7d*this.powerEnhancement+1)*(0.8*this.potencyEnhancement+1)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForEnvironmentSelf()
	{
		return (int)(500*(0.7d*this.powerEnhancement+1)*(0.2*this.potencyEnhancement+1)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForDefaultMelee()
	{
		return (int)(350*(1.0*this.potencyEnhancement+1)*(1.2*this.powerEnhancement+1)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForOffenseMelee()
	{
		return (int)(250*(1.0*this.potencyEnhancement+1)*(0.7*this.powerEnhancement+1)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForDefenseMelee() 
	{
		return (int)(150*(1.0*this.potencyEnhancement+1)*(0.7*this.powerEnhancement+1)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForEnvironmentMelee() 
	{
		return (int)(100*(1.0*this.potencyEnhancement+1)*(0.7*this.powerEnhancement+1)*Math.pow(0.85, costEnhancement));
	}
}
