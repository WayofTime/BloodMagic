package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.tool;

import net.minecraft.entity.Entity;

public interface ISpecialDamageEffect 
{
	public float getDamageForEntity(Entity entity);
	
	public String getKey();
}
