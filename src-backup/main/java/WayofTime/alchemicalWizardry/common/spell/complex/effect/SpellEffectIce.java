package WayofTime.alchemicalWizardry.common.spell.complex.effect;

import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmMelee;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmProjectile;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmSelf;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmTool;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice.MeleeDefaultIce;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice.MeleeDefensiveIce;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice.MeleeEnvironmentalIce;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice.MeleeOffensiveIce;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice.ProjectileDefaultIce;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice.ProjectileDefensiveIce;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice.ProjectileEnvironmentalIce;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice.ProjectileOffensiveIce;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice.SelfDefaultIce;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice.SelfDefensiveIce;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice.SelfEnvironmentalIce;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice.SelfOffensiveIce;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice.ToolDefaultIce;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice.ToolDefensiveIce;

public class SpellEffectIce extends SpellEffect 
{
	@Override
	public void defaultModificationProjectile(SpellParadigmProjectile parad) 
	{
		parad.damage+=this.potencyEnhancement;
		parad.addImpactEffect(new ProjectileDefaultIce(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
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
		parad.addSelfSpellEffect(new SelfOffensiveIce(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
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
		parad.addEntityEffect(new MeleeEnvironmentalIce(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
	}

	@Override
	protected int getCostForDefaultProjectile() 
	{
		return (int)((30)*(this.potencyEnhancement+1)*Math.pow(0.85, costEnhancement));
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
		return (int)(100*(2*this.powerEnhancement+1)*(2*this.potencyEnhancement+1)*Math.pow(0.85, costEnhancement));
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
		return (int)(250*(potencyEnhancement+1)*(1.5*powerEnhancement+1)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForOffenseMelee() 
	{
		return (int)(40*(1.5*potencyEnhancement+1)*Math.pow(1.5, powerEnhancement)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForDefenseMelee() 
	{
		return (int)(50*(0.5*potencyEnhancement+1)*(0.7*powerEnhancement+1)*(0.5*powerEnhancement+1)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForEnvironmentMelee() 
	{
		return (int)(20*(0.5*potencyEnhancement+1)*(0*powerEnhancement+1)*Math.pow(0.85, costEnhancement));
	}

	@Override
	public void defaultModificationTool(SpellParadigmTool parad) 
	{
		parad.addLeftClickEffect(new ToolDefaultIce(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));


		parad.addToolString("FrostTouch", "FrostTouch" + " " + SpellHelper.getNumeralForInt((this.powerEnhancement+1)));


		parad.addCritChance("FrostCrit", this.potencyEnhancement * 0.5f);
	}


	@Override
	public void offensiveModificationTool(SpellParadigmTool parad) 
	{
		parad.addDamageToHash("Sharpness", (this.powerEnhancement+1)*1.5f);


		parad.addToolString("Sharpness", "Sharpness" + " " + SpellHelper.getNumeralForInt((this.powerEnhancement+1)));


		parad.addCritChance("SharpCrit", this.potencyEnhancement);
	}


	@Override
	public void defensiveModificationTool(SpellParadigmTool parad) 
	{
		parad.addToolSummonEffect(new ToolDefensiveIce(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
	}


	@Override
	public void environmentalModificationTool(SpellParadigmTool parad) 
	{
		parad.addToolString("SilkTouch", "Silk Touch" + " " + SpellHelper.getNumeralForInt((this.powerEnhancement+1)));


		parad.setSilkTouch(true);
	}


	@Override
	protected int getCostForDefaultTool() 
	{
		return (int)(500 * (1 + this.powerEnhancement*0.3f) * (1 + this.potencyEnhancement*0.1f) * Math.pow(0.85, costEnhancement));
	}


	@Override
	protected int getCostForOffenseTool() 
	{
		return (int)(1000 * (1 + this.powerEnhancement*0.3f) * (1 + this.potencyEnhancement*0.2f) * Math.pow(0.85, costEnhancement));
	}


	@Override
	protected int getCostForDefenseTool() 
	{
		return (int)(500 * (1 + this.powerEnhancement*0.2) * (1 + this.potencyEnhancement*0.5) * Math.pow(0.85, costEnhancement));
	}


	@Override
	protected int getCostForEnvironmentTool() 
	{
		return (int)(1000 * Math.pow(0.85, costEnhancement));
	}

}
