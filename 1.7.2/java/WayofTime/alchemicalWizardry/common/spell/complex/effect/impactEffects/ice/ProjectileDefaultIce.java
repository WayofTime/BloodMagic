package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ProjectileImpactEffect;

public class ProjectileDefaultIce extends ProjectileImpactEffect 
{
	public ProjectileDefaultIce(int power, int potency, int cost) 
	{
		super(power, potency, cost);
	}

	@Override
	public void onEntityImpact(Entity mop, Entity proj) 
	{
		return;
	}

	@Override
	public void onTileImpact(World world, MovingObjectPosition mop) 
	{
		int horizRadius = this.powerUpgrades+1;
		int vertRadius = this.potencyUpgrades;
		
		ForgeDirection sideHit = ForgeDirection.getOrientation(mop.sideHit);
		
		int posX = mop.blockX + sideHit.offsetX;
		int posY = mop.blockY + sideHit.offsetY;
		int posZ = mop.blockZ + sideHit.offsetZ;
		
		if(world.isAirBlock(posX, posY, posZ))
		{
			world.setBlock(posX, posY, posZ, Blocks.ice);
		}
	}
}
