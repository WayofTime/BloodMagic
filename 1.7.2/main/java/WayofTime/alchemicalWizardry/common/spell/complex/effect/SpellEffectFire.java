package WayofTime.alchemicalWizardry.common.spell.complex.effect;

import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmMelee;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmProjectile;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmSelf;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmTool;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.MeleeDefaultFire;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.MeleeDefensiveFire;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.MeleeEnvironmentalFire;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.MeleeOffensiveFire;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.ProjectileDefaultFire;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.ProjectileDefensiveFire;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.ProjectileEnvironmentalFire;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.ProjectileOffensiveFire;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.SelfDefaultFire;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.SelfDefensiveFire;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.SelfEnvironmentalFire;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.SelfOffensiveFire;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.ToolDefaultFire;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire.ToolOffensiveFire;

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
		parad.addEntityEffect(new MeleeDefaultFire(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));		
	}

	@Override
	public void offensiveModificationMelee(SpellParadigmMelee parad) 
	{
		parad.addEntityEffect(new MeleeOffensiveFire(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
	}

	@Override
	public void defensiveModificationMelee(SpellParadigmMelee parad) 
	{
		parad.addWorldEffect(new MeleeDefensiveFire(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
	}

	@Override
	public void environmentalModificationMelee(SpellParadigmMelee parad) 
	{
		parad.addWorldEffect(new MeleeEnvironmentalFire(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
	}

	@Override
	protected int getCostForDefaultProjectile() 
	{
		return (int)((5*Math.pow(1.5*this.powerEnhancement+1, 2)*(1.5*this.potencyEnhancement+1)+this.potencyEnhancement*15)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForOffenseProjectile() 
	{
		return (int)(10*Math.pow((this.powerEnhancement)*1.3+1,2)*((1.5*this.potencyEnhancement+1))*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForDefenseProjectile() 
	{
		return (int)(25*Math.pow(1*this.powerEnhancement+1,2)*(1*this.potencyEnhancement+1)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForEnvironmentProjectile() 
	{
		return (int)(75*(0.5*this.powerEnhancement+1)*(0.5*this.potencyEnhancement+1)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForDefaultSelf() 
	{
		return 10*(int)(10*Math.pow(1.5, this.powerEnhancement+1.5*this.potencyEnhancement)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForOffenseSelf() 
	{
		return (int)(300*(3*powerEnhancement+1)*(2*potencyEnhancement + 1)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForDefenseSelf() 
	{
		return (int)(25*(3*this.potencyEnhancement+1)*(2*this.powerEnhancement+1)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForEnvironmentSelf() 
	{
		return (int)((15*Math.pow(1.7, powerEnhancement)+10*Math.pow(potencyEnhancement,1.8))*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForDefaultMelee() 
	{
		return (int)(25*(1.2*this.potencyEnhancement+1)*(2.5*this.powerEnhancement+2)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForOffenseMelee() 
	{
		return (int)(500*(1+this.potencyEnhancement)*(this.powerEnhancement+1)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForDefenseMelee() 
	{
		return (int)(30*(1.5*potencyEnhancement+1)*(3*powerEnhancement+1)*Math.pow(0.85, costEnhancement));
	}

	@Override
	protected int getCostForEnvironmentMelee() 
	{
		return (int)(25*Math.pow(1.5*this.powerEnhancement+1,3)*(0.25*this.powerEnhancement+1)*Math.pow(0.85, costEnhancement));
	}

	@Override
	public void defaultModificationTool(SpellParadigmTool parad) 
	{
		parad.addItemManipulatorEffect(new ToolDefaultFire(this.powerEnhancement,this.potencyEnhancement,this.costEnhancement));
	}

	@Override
	public void offensiveModificationTool(SpellParadigmTool parad) 
	{
		parad.addLeftClickEffect(new ToolOffensiveFire(this.powerEnhancement, this.potencyEnhancement, this.costEnhancement));
		
		parad.addToolString("offFire", "Fire Aspect" + " " + SpellHelper.getNumeralForInt(this.powerEnhancement + 1));
	}

	@Override
	public void defensiveModificationTool(SpellParadigmTool parad) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void environmentalModificationTool(SpellParadigmTool parad) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected int getCostForDefaultTool() 
	{
		return 1000;
	}

	@Override
	protected int getCostForOffenseTool() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getCostForDefenseTool() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getCostForEnvironmentTool() {
		// TODO Auto-generated method stub
		return 0;
	}
}
