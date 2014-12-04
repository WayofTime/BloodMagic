package WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.entity.projectile.ExplosionProjectile;

public class EntityMinorDemonGruntEarth extends EntityMinorDemonGrunt
{
	public EntityMinorDemonGruntEarth(World par1World) 
	{
		super(par1World);
		this.setDemonID(AlchemicalWizardry.entityMinorDemonGruntEarthID);
	}
	
	@Override
    public boolean attackEntityAsMob(Entity par1Entity)
    {
        int i = this.isTamed() ? 20 : 20;
        if(par1Entity instanceof IHoardDemon && ((IHoardDemon) par1Entity).isSamePortal(this))
        {
        	return false;
        }
        
        if(par1Entity instanceof EntityLivingBase)
        {
        	((EntityLivingBase) par1Entity).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 200, 2));
        }
        
        return par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float) i);
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase par1EntityLivingBase, float par2)
    {
    	if(par1EntityLivingBase instanceof IHoardDemon && ((IHoardDemon) par1EntityLivingBase).isSamePortal(this))
        {
        	return;
        }
        double xCoord;
        double yCoord;
        double zCoord;
        ExplosionProjectile hol = new ExplosionProjectile(worldObj, this, par1EntityLivingBase, 1.8f, 0f, 15, 600, false);
        this.worldObj.spawnEntityInWorld(hol);
    }
}
