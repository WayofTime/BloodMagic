package WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.entity.projectile.FireProjectile;

public class EntityMinorDemonGruntGuardianFire extends EntityMinorDemonGruntGuardian
{
	public EntityMinorDemonGruntGuardianFire(World par1World) 
	{
		super(par1World);
		this.setDemonID(AlchemicalWizardry.entityMinorDemonGruntGuardianFireID);
		this.isImmuneToFire = true;
	}

	@Override
    public boolean attackEntityAsMob(Entity par1Entity)
    {
        int i = this.isTamed() ? 25 : 25;
        
        if(par1Entity instanceof IHoardDemon && ((IHoardDemon) par1Entity).isSamePortal(this))
        {
        	return false;
        }
        
        par1Entity.setFire(15);
        
        return par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float) i);
    }
    
    /**
     * Attack the specified entity using a ranged attack.
     */
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
        FireProjectile hol = new FireProjectile(worldObj, this, par1EntityLivingBase, 1.8f, 0f, 20, 600);
        this.worldObj.spawnEntityInWorld(hol);
    }
}
