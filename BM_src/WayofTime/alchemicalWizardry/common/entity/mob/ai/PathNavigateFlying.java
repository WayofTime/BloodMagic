package WayofTime.alchemicalWizardry.common.entity.mob.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.world.World;

public class PathNavigateFlying extends PathNavigate 
{
	public PathNavigateFlying(EntityLiving par1EntityLiving, World par2World) 
	{
		super(par1EntityLiving, par2World);
	}
	
	public boolean canFlyingNavigate()
	{
		return true;
	}
}
