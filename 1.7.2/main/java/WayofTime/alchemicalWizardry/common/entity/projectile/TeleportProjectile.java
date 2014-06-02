package WayofTime.alchemicalWizardry.common.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.simple.SpellTeleport;

public class TeleportProjectile extends EnergyBlastProjectile
{
    private boolean isEntityTeleport; //True if the entity firing teleports on hit

    public TeleportProjectile(World par1World)
    {
        super(par1World);
        this.motionX *= 3;
        this.motionY *= 3;
        this.motionZ *= 3;
    }

    public TeleportProjectile(World par1World, double par2, double par4, double par6)
    {
        super(par1World, par2, par4, par6);
        this.motionX *= 3;
        this.motionY *= 3;
        this.motionZ *= 3;
    }

    public TeleportProjectile(World par1World, EntityLivingBase par2EntityPlayer, int damage, boolean flag)
    {
        super(par1World, par2EntityPlayer, damage);
        isEntityTeleport = flag;
        this.motionX *= 3;
        this.motionY *= 3;
        this.motionZ *= 3;
    }

    public TeleportProjectile(World par1World, EntityLivingBase par2EntityPlayer, int damage, int maxTicksInAir, double posX, double posY, double posZ, float rotationYaw, float rotationPitch, boolean flag)
    {
        super(par1World, par2EntityPlayer, damage, maxTicksInAir, posX, posY, posZ, rotationYaw, rotationPitch);
        isEntityTeleport = flag;
        this.motionX *= 3;
        this.motionY *= 3;
        this.motionZ *= 3;
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
            if (isEntityTeleport)
            {
                if (shootingEntity != null && shootingEntity instanceof EntityPlayerMP)
                {
                    EntityPlayerMP entityplayermp = (EntityPlayerMP) shootingEntity;

                    if(entityplayermp.worldObj == this.worldObj)
                    //if (!entityplayermp.playerNetServerHandler.connectionClosed && entityplayermp.worldObj == this.worldObj)
                    {
                        EnderTeleportEvent event = new EnderTeleportEvent(entityplayermp, this.posX, this.posY, this.posZ, 5.0F);

                        if (!MinecraftForge.EVENT_BUS.post(event))
                        {
                            if (shootingEntity.isRiding())
                            {
                                shootingEntity.mountEntity((Entity) null);
                            }

                            shootingEntity.setPositionAndUpdate(event.targetX, event.targetY, event.targetZ);
//                                this.getThrower().fallDistance = 0.0F;
//                                this.getThrower().attackEntityFrom(DamageSource.fall, event.attackDamage);
                        }
                    }
                }
            }
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
                if (isEntityTeleport)
                {
                    if (shootingEntity != null && shootingEntity instanceof EntityPlayerMP)
                    {
                        EntityPlayerMP entityplayermp = (EntityPlayerMP) shootingEntity;

                        if(entityplayermp.worldObj == this.worldObj)
                        //if (!entityplayermp.playerNetServerHandler.connectionClosed && entityplayermp.worldObj == this.worldObj)
                        {
                            EnderTeleportEvent event = new EnderTeleportEvent(entityplayermp, this.posX, this.posY, this.posZ, 5.0F);

                            if (!MinecraftForge.EVENT_BUS.post(event))
                            {
                                if (shootingEntity.isRiding())
                                {
                                    shootingEntity.mountEntity((Entity) null);
                                }

                                shootingEntity.setPositionAndUpdate(event.targetX, event.targetY, event.targetZ);
//                                this.getThrower().fallDistance = 0.0F;
//                                this.getThrower().attackEntityFrom(DamageSource.fall, event.attackDamage);
                            }
                        }
                    }
                } else
                {
//        			int x = (int)this.posX + mop.worldObj.rand.nextInt(100) - mop.worldObj.rand.nextInt(100);
//        			int y = (int)this.posY + mop.worldObj.rand.nextInt(10) - mop.worldObj.rand.nextInt(10);
//        			int z = (int)this.posZ + mop.worldObj.rand.nextInt(100) - mop.worldObj.rand.nextInt(100);
//
//        			boolean bool = false;
//        			int i = 0;
//
//        			while(!bool&&i<100)
//        			{
//	        			if(worldObj.isAirBlock(x, y, z)||worldObj.isAirBlock(x, y+1, z))
//	        			{
//	        				((EntityLivingBase) mop).setPositionAndUpdate(x, y, z);
//	        				bool=true;
//	        			}else
//	        			{
//	        				x = (int)this.posX + mop.worldObj.rand.nextInt(100) - mop.worldObj.rand.nextInt(100);
//	        				y = (int)this.posY + mop.worldObj.rand.nextInt(10) - mop.worldObj.rand.nextInt(10);
//	        				z = (int)this.posZ + mop.worldObj.rand.nextInt(100) - mop.worldObj.rand.nextInt(100);
//	        				i++;
//	        			}
//        			}
                    SpellTeleport.teleportRandomly((EntityLivingBase) mop, 64);
                }

                //doDamage(projectileDamage, mop);
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
        SpellHelper.sendParticleToAllAround(worldObj, posX, posY, posZ, 30, worldObj.provider.dimensionId, "mobSpellAmbient", posX + smallGauss(0.1D), posY + smallGauss(0.1D), posZ + smallGauss(0.1D), 0.5D, 0.5D, 0.5D);
        SpellHelper.sendParticleToAllAround(worldObj, posX, posY, posZ, 30, worldObj.provider.dimensionId, "portal", posX, posY, posZ, -motionX, -motionY, -motionZ);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setBoolean("isEntityTeleport", isEntityTeleport);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        isEntityTeleport = par1NBTTagCompound.getBoolean("isEntityTeleport");
    }
}
