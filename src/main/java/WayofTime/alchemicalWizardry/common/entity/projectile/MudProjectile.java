package WayofTime.alchemicalWizardry.common.entity.projectile;

import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class MudProjectile extends EnergyBlastProjectile
{
    private boolean doesBlindness; //True for when it applies blindness, false for slowness

    public MudProjectile(World par1World)
    {
        super(par1World);
    }

    public MudProjectile(World par1World, double par2, double par4, double par6)
    {
        super(par1World, par2, par4, par6);
    }

    public MudProjectile(World par1World, EntityLivingBase par2EntityPlayer, int damage, boolean flag)
    {
        super(par1World, par2EntityPlayer, damage);
        doesBlindness = flag;
    }

    public MudProjectile(World par1World, EntityLivingBase par2EntityPlayer, int damage, int maxTicksInAir, double posX, double posY, double posZ, float rotationYaw, float rotationPitch, boolean flag)
    {
        super(par1World, par2EntityPlayer, damage, maxTicksInAir, posX, posY, posZ, rotationYaw, rotationPitch);
        doesBlindness = flag;
    }

    public MudProjectile(World par1World, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase, float par4, float par5, int damage, int maxTicksInAir, boolean flag)
    {
        super(par1World, par2EntityLivingBase, par3EntityLivingBase, par4, par5, damage, maxTicksInAir);
        doesBlindness = flag;
    }

    @Override
    public DamageSource getDamageSource()
    {
        return DamageSource.causeMobDamage(shootingEntity);
    }

    @Override
    public void onImpact(MovingObjectPosition mop)
    {
        if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit != null)
        {
            if (mop.entityHit == shootingEntity)
            {
                return;
            }

            this.onImpact(mop.entityHit);
        } else if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
        {
        }

        this.setDead();
    }

    @Override
    public void onImpact(Entity mop)
    {
        if (mop == shootingEntity && ticksInAir > 3)
        {
            this.setDead();
        } else
        {
            if (mop instanceof EntityLivingBase)
            {
                if (doesBlindness)
                {
                    ((EntityLivingBase) mop).addPotionEffect(new PotionEffect(Potion.blindness.id, 100, 0));
                } else
                {
                    ((EntityLivingBase) mop).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 100, 2));
                }
                doDamage(projectileDamage, mop);
            }
        }

        spawnHitParticles(EnumParticleTypes.CRIT_MAGIC, 8);
        this.setDead();
    }

    @Override
    public void doFiringParticles()
    {
        SpellHelper.sendParticleToAllAround(worldObj, posX, posY, posZ, 30, worldObj.provider.getDimensionId(), EnumParticleTypes.SPELL_MOB_AMBIENT, posX + smallGauss(0.1D), posY + smallGauss(0.1D), posZ + smallGauss(0.1D), 0.5D, 0.5D, 0.5D);
        SpellHelper.sendParticleToAllAround(worldObj, posX, posY, posZ, 30, worldObj.provider.getDimensionId(), EnumParticleTypes.SPELL_MOB, posX, posY, posZ, 0.5F, 0.297F, 0.0664F);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setBoolean("doesBlindness", doesBlindness);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        doesBlindness = par1NBTTagCompound.getBoolean("doesBlindness");
    }
}
