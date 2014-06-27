package WayofTime.alchemicalWizardry.common.spell.complex.effect;

import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmMelee;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmProjectile;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmSelf;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmTool;
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
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth.ToolEnvironmentalEarth;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.earth.ToolOffensiveEarth;

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
		return (int)(250*(1.2*this.potencyEnhancement+1)*(3*this.powerEnhancement+2.5)*Math.pow(0.85, costEnhancement));
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

	@Override
	public void defaultModificationTool(SpellParadigmTool parad) 
	{
		String toolClass = "pickaxe";


		float digSpeed = 7.0f;


		switch(this.powerEnhancement)
		{
		case 1:
			digSpeed = 9.0f;
			break;
		case 2:
			digSpeed = 12.0f;
			break;
		case 3:
			digSpeed = 16.0f;
			break;
		case 4:
			digSpeed = 21.0f;
			break;
		case 5:
			digSpeed = 27.0f;
			break;
		}


		parad.setDigSpeed(toolClass, digSpeed);


		int hlvl = this.potencyEnhancement + 2;
		parad.setHarvestLevel(toolClass, hlvl);
	}


	@Override
	public void offensiveModificationTool(SpellParadigmTool parad) 
	{
		parad.addItemManipulatorEffect(new ToolOffensiveEarth(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
	}

	@Override
	public void defensiveModificationTool(SpellParadigmTool parad) 
	{
		String toolClass = "shovel";


		float digSpeed = 7.0f;


		switch(this.powerEnhancement)
		{
		case 1:
			digSpeed = 9.0f;
			break;
		case 2:
			digSpeed = 12.0f;
			break;
		case 3:
			digSpeed = 16.0f;
			break;
		case 4:
			digSpeed = 21.0f;
			break;
		case 5:
			digSpeed = 27.0f;
			break;
		}


		parad.setDigSpeed(toolClass, digSpeed);


		int hlvl = this.potencyEnhancement + 2;
		parad.setHarvestLevel(toolClass, hlvl);
	}


	@Override
	public void environmentalModificationTool(SpellParadigmTool parad) 
	{
		parad.addDigAreaEffect(new ToolEnvironmentalEarth(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
	}


	@Override
	protected int getCostForDefaultTool() 
	{
		return (int)(1000 * (1 + this.potencyEnhancement*0.1f) * (1 + this.powerEnhancement*0.2f) * Math.pow(0.85, costEnhancement));
	}


	@Override
	protected int getCostForOffenseTool() 
	{
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	protected int getCostForDefenseTool() 
	{
		return (int)(1000 * (1 + this.potencyEnhancement*0.1f) * (1 + this.powerEnhancement*0.2f) * Math.pow(0.85, costEnhancement));
	}


	@Override
	protected int getCostForEnvironmentTool() 
	{
		return (int)(10 * (1+this.potencyEnhancement*0.8) * Math.pow(1.5*this.powerEnhancement + 3, 2) * Math.pow(0.85, this.costEnhancement));
	}

}
