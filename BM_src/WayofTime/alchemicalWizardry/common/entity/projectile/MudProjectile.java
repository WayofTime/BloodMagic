package WayofTime.alchemicalWizardry.common.entity.projectile;

import WayofTime.alchemicalWizardry.common.PacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class MudProjectile extends EnergyBlastProjectile {
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
        if (mop.typeOfHit == EnumMovingObjectType.ENTITY && mop.entityHit != null)
        {
            if (mop.entityHit == shootingEntity)
            {
                return;
            }

            this.onImpact(mop.entityHit);
        } else if (mop.typeOfHit == EnumMovingObjectType.TILE)
        {
        }

        this.setDead();
    }

    @Override
    public void onImpact(Entity mop)
    {
        if (mop == shootingEntity && ticksInAir > 3)
        {
            //shootingEntity.attackEntityFrom(DamageSource.causePlayerDamage(shootingEntity), 1);
            this.setDead();
        } else
        {
            //doDamage(8 + d6(), mop);
            if (mop instanceof EntityLivingBase)
            {
                //((EntityLivingBase)mop).addPotionEffect(new PotionEffect(Potion.weakness.id, 60,2));
                //((EntityLivingBase)mop).setFire(50);
                //((EntityLivingBase)mop).setRevengeTarget(shootingEntity);
//        		if(((EntityLivingBase)mop).isEntityUndead())
//        		{
//        			doDamage((int)(projectileDamage*2),mop);
//        		}else
//        		{
//        			doDamage(projectileDamage, mop);
//        		}
                if (doesBlindness)
                {
                    ((EntityLivingBase) mop).addPotionEffect(new PotionEffect(Potion.blindness.id, 100, 0));
                } else
                {
                    ((EntityLivingBase) mop).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 100, 2));
                }

                doDamage(projectileDamage, mop);
                //((EntityLivingBase)mop).setVelocity(this.motionX*2, ((EntityLivingBase)mop).motionY+1.5, this.motionZ*2);
            }

            //worldObj.createExplosion(this, this.posX, this.posY, this.posZ, (float)(0.1), true);
        }

        spawnHitParticles("magicCrit", 8);
        this.setDead();
    }

    @Override
    public void doFiringParticles()
    {
        PacketDispatcher.sendPacketToAllAround(posX, posY, posZ, 30, worldObj.provider.dimensionId, PacketHandler.getCustomParticlePacket("mobSpellAmbient", posX + smallGauss(0.1D), posY + smallGauss(0.1D), posZ + smallGauss(0.1D), 0.5D, 0.5D, 0.5D));
        PacketDispatcher.sendPacketToAllAround(posX, posY, posZ, 30, worldObj.provider.dimensionId, PacketHandler.getCustomParticlePacket("mobSpell", posX, posY, posZ, 0.5F, 0.297F, 0.0664F));
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
