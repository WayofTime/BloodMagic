package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public abstract class ExtrapolatedMeleeEntityEffect implements IMeleeSpellEntityEffect
{
	protected float range;
	protected float radius;
	
	public ExtrapolatedMeleeEntityEffect(int range, int radius)
	{
		this.range = range;
		this.radius = radius;
	}
	
	@Override
	public void onEntityImpact(World world, EntityPlayer entityPlayer) 
	{
		Vec3 lookVec = entityPlayer.getLook(range);
		double x = entityPlayer.posX + lookVec.xCoord;
		double y = entityPlayer.posY + entityPlayer.getEyeHeight() + lookVec.yCoord;
		double z = entityPlayer.posZ + lookVec.zCoord;
		
        List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(x-0.5f, y-0.5f, z-0.5f, x + 0.5f, y + 0.5f, z + 0.5f).expand(radius, radius, radius));
        
        if(entities!=null)
        {
        	for(Entity entity : entities)
            {
            	this.entityEffect(world, entity);
            }	
        }
	}
	
	protected abstract void entityEffect(World world, Entity entity);
	
	public void setRange(float range)
	{
		this.range = range;
	}
	
	public void setRadius(float radius)
	{
		this.radius = radius;
	}
}
