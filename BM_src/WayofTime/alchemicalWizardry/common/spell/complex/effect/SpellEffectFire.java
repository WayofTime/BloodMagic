package WayofTime.alchemicalWizardry.common.spell.complex.effect;

import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmMelee;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmProjectile;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmSelf;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.SelfDefaultFire;

public class SpellEffectFire extends SpellEffect
{

	@Override
	public void defaultModificationProjectile(SpellParadigmProjectile parad) 
	{
		
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
		parad.addSelfSpellEffect(new SelfDefaultFire());
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
		// TODO Auto-generated method stub
		return 1000;
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
