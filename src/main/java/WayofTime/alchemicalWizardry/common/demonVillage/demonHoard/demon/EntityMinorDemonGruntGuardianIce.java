package WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.entity.projectile.IceProjectile;

public class EntityMinorDemonGruntGuardianIce extends EntityMinorDemonGruntGuardian
{
	public EntityMinorDemonGruntGuardianIce(World par1World) 
	{
		super(par1World);
		this.setDemonID(AlchemicalWizardry.entityMinorDemonGruntGuardianIceID);
	}

	@Override
    public boolean attackEntityAsMob(Entity par1Entity)
    {
        int i = this.isTamed() ? 25 : 25;
        
        if(par1Entity instanceof IHoardDemon && ((IHoardDemon) par1Entity).isSamePortal(this))
        {
        	return false;
        }
        
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
        IceProjectile hol = new IceProjectile(worldObj, this, par1EntityLivingBase, 1.8f, 0f, 20, 600);
        this.worldObj.spawnEntityInWorld(hol);
    }
}
