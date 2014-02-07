package WayofTime.alchemicalWizardry.common.spell.complex.effect;

import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmMelee;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmProjectile;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmSelf;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice.MeleeDefaultIce;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice.MeleeDefensiveIce;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice.MeleeOffensiveIce;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice.ProjectileDefensiveIce;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice.ProjectileEnvironmentalIce;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice.ProjectileOffensiveIce;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice.SelfDefaultIce;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice.SelfDefensiveIce;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice.SelfEnvironmentalIce;

public class SpellEffectIce extends SpellEffect 
{
	@Override
	public void defaultModificationProjectile(SpellParadigmProjectile parad) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void offensiveModificationProjectile(SpellParadigmProjectile parad) 
	{
		parad.damage+=2;
		parad.addImpactEffect(new ProjectileOffensiveIce(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
	}

	@Override
	public void defensiveModificationProjectile(SpellParadigmProjectile parad) 
	{
		parad.addImpactEffect(new ProjectileDefensiveIce(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));

	}

	@Override
	public void environmentalModificationProjectile(SpellParadigmProjectile parad) 
	{
		parad.addUpdateEffect(new ProjectileEnvironmentalIce(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
	}

	@Override
	public void defaultModificationSelf(SpellParadigmSelf parad)
	{
		parad.addSelfSpellEffect(new SelfDefaultIce(this.powerEnhancement,this.potencyEnhancement, this.costEnhancement));
	}

	@Override
	public void offensiveModificationSelf(SpellParadigmSelf parad) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void defensiveModificationSelf(SpellParadigmSelf parad)
	{
		parad.addSelfSpellEffect(new SelfDefensiveIce(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));

	}

	@Override
	public void environmentalModificationSelf(SpellParadigmSelf parad) 
	{
		parad.addSelfSpellEffect(new SelfEnvironmentalIce(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
	}

	@Override
	public void defaultModificationMelee(SpellParadigmMelee parad) 
	{
		parad.addEntityEffect(new MeleeDefaultIce(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
	}

	@Override
	public void offensiveModificationMelee(SpellParadigmMelee parad)
	{
		parad.addEntityEffect(new MeleeOffensiveIce(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
	}

	@Override
	public void defensiveModificationMelee(SpellParadigmMelee parad) 
	{
		parad.addWorldEffect(new MeleeDefensiveIce(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
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
		return (int)((60)*(this.powerEnhancement+1)*(3*this.potencyEnhancement+1)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForDefenseProjectile() 
	{
		return (int)(75*(2*this.powerEnhancement+1)*(this.potencyEnhancement+1)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForEnvironmentProjectile() 
	{
		return (int)(200*(2*this.powerEnhancement+1)*(2*this.potencyEnhancement+1)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForDefaultSelf()
	{
		return (int)(20*(this.powerEnhancement+1)*Math.pow(0.85, costEnhancement));
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
		return (int)(200*(3*powerEnhancement+1)*(2*potencyEnhancement + 1)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForEnvironmentSelf() 
	{
		return (int)(10*(1.5*potencyEnhancement+1)*(3*powerEnhancement+1)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForDefaultMelee() 
	{
		return (int)(125*(potencyEnhancement+1)*(1.5*powerEnhancement+1)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForOffenseMelee() 
	{
		return (int)(25*(1.5*potencyEnhancement+1)*Math.pow(1.5, powerEnhancement)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForDefenseMelee() 
	{
		return (int)(10*(0.5*potencyEnhancement+1)*(0.7*powerEnhancement+1)*(0.5*powerEnhancement+1)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForEnvironmentMelee() 
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
