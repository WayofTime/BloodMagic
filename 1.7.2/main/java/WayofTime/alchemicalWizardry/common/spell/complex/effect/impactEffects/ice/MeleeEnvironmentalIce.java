package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ExtrapolatedMeleeEntityEffect;

public class MeleeEnvironmentalIce extends ExtrapolatedMeleeEntityEffect 
{
	public MeleeEnvironmentalIce(int power, int potency, int cost) 
	{
		super(power, potency, cost);
		this.setMaxNumberHit(1+potency);
		this.setRadius(2);
		this.setRange(3);
	}

	@Override
	protected boolean entityEffect(World world, Entity entity, EntityPlayer entityPlayer) 
	{
		//TODO Change to an Ice Cage
		for(int i=0;i<=this.powerUpgrades;i++)
		{
			double randX = (world.rand.nextDouble()-world.rand.nextDouble())*3;
			double randY = -world.rand.nextDouble()*3;
			double randZ = (world.rand.nextDouble()-world.rand.nextDouble())*3;
			
			EntitySnowball snowball = new EntitySnowball(world, entity.posX-3*randX, entity.posY-3*randY, entity.posZ-3*randZ);
			snowball.motionX = randX;
			snowball.motionY = randY;
			snowball.motionZ = randZ;
			
			world.spawnEntityInWorld(snowball);
		}
		
		return true;
	}

}
