package WayofTime.alchemicalWizardry.common.spell.complex;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public interface IMeleeSpellEntityEffect 
{
	public void onEntityImpact(World world, Entity entity);
}
