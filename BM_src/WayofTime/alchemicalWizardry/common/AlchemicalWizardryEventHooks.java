package WayofTime.alchemicalWizardry.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.entity.projectile.EnergyBlastProjectile;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class AlchemicalWizardryEventHooks
{
    public static Map<String,Boolean> playerFlightBuff = new HashMap();
    public static Map<String,Boolean> playerBoostStepHeight = new HashMap();
    public static List<String> playersWith1Step = new ArrayList();

    @ForgeSubscribe
    public void onLivingJumpEvent(LivingJumpEvent event)
    {
        if (event.entityLiving.isPotionActive(AlchemicalWizardry.customPotionBoost))
        {
            int i = event.entityLiving.getActivePotionEffect(AlchemicalWizardry.customPotionBoost).getAmplifier();
            event.entityLiving.motionY += (0.1f) * (2 + i);
        }
    }
    
    @ForgeSubscribe
    public void onEndermanTeleportEvent(EnderTeleportEvent event)
    {
    	if(event.entityLiving.isPotionActive(AlchemicalWizardry.customPotionPlanarBinding) && event.isCancelable())
    	{
    		event.setCanceled(true);
    	}
    }

    @ForgeSubscribe
    public void onEntityDamaged(LivingAttackEvent event)
    {
        EntityLivingBase entityAttacked = event.entityLiving;
        EntityLivingBase entity = event.entityLiving;
        double posX = entity.posX;
        double posY = entity.posY;
        double posZ = entity.posZ;

        if (entityAttacked.isPotionActive(AlchemicalWizardry.customPotionReciprocation))
        {
            Entity entityAttacking = event.source.getSourceOfDamage();

            if (entityAttacking != null && entityAttacking instanceof EntityLivingBase)
            {
                int i = event.entityLiving.getActivePotionEffect(AlchemicalWizardry.customPotionReciprocation).getAmplifier();
                float damageRecieve = event.ammount / 2 * (i + 1);
                ((EntityLivingBase) entityAttacking).attackEntityFrom(DamageSource.generic, damageRecieve);
            }
        }
        
        if(entityAttacked.isPotionActive(AlchemicalWizardry.customPotionFlameCloak))
        {
            int i = event.entityLiving.getActivePotionEffect(AlchemicalWizardry.customPotionFlameCloak).getAmplifier();

        	Entity entityAttacking = event.source.getSourceOfDamage();
        	
        	if(entityAttacking != null && entityAttacking instanceof EntityLivingBase && !entityAttacking.isImmuneToFire() && !((EntityLivingBase)entityAttacking).isPotionActive(Potion.fireResistance))
        	{
        		entityAttacking.attackEntityFrom(DamageSource.inFire, 2*i+2);
        		entityAttacking.setFire(3);
        	}
        }
        
        //TODO Make this work for a different potion
//        if(event.entityLiving.isPotionActive(AlchemicalWizardry.customPotionPlanarBinding) && event.isCancelable())
//    	{
//        	if(event.ammount>event.entityLiving.getHealth())
//        	{
//        		int i = event.entityLiving.getActivePotionEffect(AlchemicalWizardry.customPotionPlanarBinding).getAmplifier();
//        		
//        		event.setCanceled(true);
//        		
//        		if(event.entityLiving.worldObj.rand.nextFloat()>((i+1)/6))
//        		{
//        			event.entityLiving.removePotionEffect(AlchemicalWizardry.customPotionPlanarBinding.id);
//        		}
//        		
//        		for(int j=0; j<10; j++)
//        		{
//            		PacketDispatcher.sendPacketToAllAround(posX, posY, posZ, 20, entity.worldObj.provider.dimensionId, TEAltar.getParticlePacket(posX, posY, posZ, (short) 4));
//        		}
//                event.entityLiving.worldObj.playSoundEffect((double) ((float) posX + 0.5F), (double) ((float) posY + 0.5F), (double) ((float) posZ + 0.5F), "random.fizz", 0.5F, 2.6F + (entity.worldObj.rand.nextFloat() - entity.worldObj.rand.nextFloat()) * 0.8F);
//        		
//        		event.entityLiving.heal(20);	
//        	}	
//    	}
    }

//	@ForgeSubscribe
//	public void onFOVUpdate(FOVUpdateEvent event)
//	{
//		event.setResult(Result.DEFAULT);
//	}

    @ForgeSubscribe
    public void onEntityUpdate(LivingUpdateEvent event)
    {
        EntityLivingBase entityLiving = event.entityLiving;
        double x = entityLiving.posX;
        double y = entityLiving.posY;
        double z = entityLiving.posZ;
        
        Vec3 blockVector = SpellHelper.getEntityBlockVector(entityLiving);
        int xPos = (int)(blockVector.xCoord);
        int yPos = (int)(blockVector.yCoord);
        int zPos = (int)(blockVector.zCoord);

        if (entityLiving instanceof EntityPlayer && entityLiving.worldObj.isRemote)
        {
            EntityPlayer entityPlayer = (EntityPlayer) entityLiving;
            boolean highStepListed = playersWith1Step.contains(entityPlayer.username);
            boolean hasHighStep = entityPlayer.isPotionActive(AlchemicalWizardry.customPotionBoost);

            if (hasHighStep && !highStepListed)
            {
                playersWith1Step.add(entityPlayer.username);
            }

            if (!hasHighStep && highStepListed)
            {
                playersWith1Step.remove(entityPlayer.username);
                entityPlayer.stepHeight = 0.5F;
            }
        }

        if (event.entityLiving.isPotionActive(AlchemicalWizardry.customPotionDrowning))
        {
            int i = event.entityLiving.getActivePotionEffect(AlchemicalWizardry.customPotionDrowning).getAmplifier();

            if (event.entityLiving.worldObj.getWorldTime() % ((int) (20 / (i + 1))) == 0 && !event.entityLiving.isPotionActive(Potion.waterBreathing))
            {
                event.entityLiving.attackEntityFrom(DamageSource.drown, 2);
                event.entityLiving.hurtResistantTime = Math.min(event.entityLiving.hurtResistantTime, 20 / (i + 1));
            }
        }

        if (event.entityLiving.isPotionActive(AlchemicalWizardry.customPotionBoost))
        {
            int i = event.entityLiving.getActivePotionEffect(AlchemicalWizardry.customPotionBoost).getAmplifier();
            
            //Testing code
            List<Entity> entities = SpellHelper.getEntitiesInRange(event.entity.worldObj, xPos, yPos, zPos, 10, 10);
            for(Entity entity : entities)
            {
            	if(entity instanceof EntityZombie)
            	{
            		((EntityZombie) entity).setAttackTarget(null);
            	}
            }
            //
            
            EntityLivingBase entity = event.entityLiving;
            //if(!entity.isSneaking())
            {
                float percentIncrease = (i + 1) * 0.1f;

                if (event.entityLiving instanceof EntityPlayer)
                {
                    EntityPlayer entityPlayer = (EntityPlayer) event.entityLiving;
                    entityPlayer.stepHeight = 1.0f;

                    if((entityPlayer.onGround || entityPlayer.capabilities.isFlying) && entityPlayer.moveForward > 0F)
            			entityPlayer.moveFlying(0F, 1F, entityPlayer.capabilities.isFlying ? (percentIncrease/2.0f) : percentIncrease);
                }
            }
        }

        if (event.entityLiving.isPotionActive(AlchemicalWizardry.customPotionProjProt))
        {
            int i = event.entityLiving.getActivePotionEffect(AlchemicalWizardry.customPotionProjProt).getAmplifier();
            EntityLivingBase entity = event.entityLiving;
            int posX = (int) Math.round(entity.posX - 0.5f);
            int posY = (int) Math.round(entity.posY);
            int posZ = (int) Math.round(entity.posZ - 0.5f);
            int d0 = i;
            AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB(posX - 0.5, posY - 0.5, posZ - 0.5, posX + 0.5, posY + 0.5, posZ + 0.5).expand(d0, d0, d0);
            List list = event.entityLiving.worldObj.getEntitiesWithinAABB(Entity.class, axisalignedbb);
            Iterator iterator = list.iterator();
            EntityLivingBase livingEntity;

            while (iterator.hasNext())
            {
                Entity projectile = (Entity) iterator.next();

                if (projectile == null)
                {
                    continue;
                }

                if (!(projectile instanceof IProjectile))
                {
                    continue;
                }

                if (projectile instanceof EntityArrow)
                {
                    if (((EntityArrow) projectile).shootingEntity == null)
                    {
                    } else if (!(((EntityArrow) projectile).shootingEntity == null) && ((EntityArrow) projectile).shootingEntity.equals(entity))
                    {
                        break;
                    }
                } else if (projectile instanceof EnergyBlastProjectile)
                {
                    if (!(((EnergyBlastProjectile) projectile).shootingEntity == null) && ((EnergyBlastProjectile) projectile).shootingEntity.equals(entity))
                    {
                        break;
                    }
                }

                double delX = projectile.posX - entity.posX;
                double delY = projectile.posY - entity.posY;
                double delZ = projectile.posZ - entity.posZ;
                double curVel = Math.sqrt(delX * delX + delY * delY + delZ * delZ);
                //NOTE: It appears that it constantly reverses the direction.
                //Any way to do it only once? Or find the shooting entity?
                delX /= curVel;
                delY /= curVel;
                delZ /= curVel;
                double newVel = Math.sqrt(projectile.motionX * projectile.motionX + projectile.motionY * projectile.motionY + projectile.motionZ * projectile.motionZ);
                projectile.motionX = newVel * delX;
                projectile.motionY = newVel * delY;
                projectile.motionZ = newVel * delZ;
                //TODO make this not affect player's projectiles
            }
        }

        if (event.entityLiving.isPotionActive(AlchemicalWizardry.customPotionFlight))
        {
            if (event.entityLiving instanceof EntityPlayer)
            {
                EntityPlayer entityPlayer = (EntityPlayer) event.entityLiving;
                String ownerName = entityPlayer.username;
                playerFlightBuff.put(ownerName, true);
                entityPlayer.capabilities.allowFlying = true;
                //entityPlayer.sendPlayerAbilities();
            }
        } else
        {
            if (event.entityLiving instanceof EntityPlayer)
            {
                EntityPlayer entityPlayer = (EntityPlayer) event.entityLiving;
                String ownerName = entityPlayer.username;

                if (!playerFlightBuff.containsKey(ownerName))
                {
                    playerFlightBuff.put(ownerName, false);
                }

                if (playerFlightBuff.get(ownerName))
                {
                    playerFlightBuff.put(ownerName, false);

                    if (!entityPlayer.capabilities.isCreativeMode)
                    {
                        entityPlayer.capabilities.allowFlying = false;
                        entityPlayer.capabilities.isFlying = false;
                        entityPlayer.sendPlayerAbilities();
                    }
                }
            }
        }
        
        if(entityLiving.isPotionActive(AlchemicalWizardry.customPotionFlameCloak))
        {
        	entityLiving.worldObj.spawnParticle("flame", x+SpellHelper.gaussian(1),y-1.3+SpellHelper.gaussian(0.3),z+SpellHelper.gaussian(1), 0, 0.06d, 0);
        	
            int i = event.entityLiving.getActivePotionEffect(AlchemicalWizardry.customPotionFlameCloak).getAmplifier();
        	double range = i*0.5;
        	
        	List<Entity> entities = SpellHelper.getEntitiesInRange(entityLiving.worldObj, x, y, z, range, range);
        	if(entities!=null)
        	{
        		for(Entity entity : entities)
        		{
        			if(!entity.equals(entityLiving)&&!entity.isImmuneToFire()&&!(entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(Potion.fireResistance)))
        			{
        				entity.setFire(3);
        			}
        		}
        	}
        }
        
        if(entityLiving.isPotionActive(AlchemicalWizardry.customPotionIceCloak))
        {
        	if(entityLiving.worldObj.getWorldTime()%2==0)
        		entityLiving.worldObj.spawnParticle("reddust", x+SpellHelper.gaussian(1),y-1.3+SpellHelper.gaussian(0.3),z+SpellHelper.gaussian(1), 0x74,0xbb,0xfb);
        	
            int r = event.entityLiving.getActivePotionEffect(AlchemicalWizardry.customPotionIceCloak).getAmplifier();
        	int horizRange = r+1;
        	int vertRange = 1;
        	
        	if(!entityLiving.worldObj.isRemote)
        	{
	        	for(int i=-horizRange; i<=horizRange;i++)
	        	{
	        		for(int k=-horizRange; k<=horizRange;k++)
	        		{
	        			for(int j=-vertRange-1; j<=vertRange-1; j++)
	        			{
	        				SpellHelper.freezeWaterBlock(entityLiving.worldObj, xPos+i, yPos+j, zPos+k);
	        			}
	        		}
	        	}
        	}
        }  
        
        if(entityLiving.isPotionActive(AlchemicalWizardry.customPotionHeavyHeart))
        {
        	entityLiving.worldObj.spawnParticle("flame", x+SpellHelper.gaussian(1),y-1.3+SpellHelper.gaussian(0.3),z+SpellHelper.gaussian(1), 0, 0.06d, 0);
        	
            int i = event.entityLiving.getActivePotionEffect(AlchemicalWizardry.customPotionHeavyHeart).getAmplifier();
        	double decrease = 0.025*(i+1);
        	
        	if(entityLiving.motionY>-0.9)
        	{
        		entityLiving.motionY-=decrease;
        	}
        }
        
        if(entityLiving.isPotionActive(AlchemicalWizardry.customPotionFireFuse))
        {
        	entityLiving.worldObj.spawnParticle("flame", x+SpellHelper.gaussian(1),y-1.3+SpellHelper.gaussian(0.3),z+SpellHelper.gaussian(1), 0, 0.06d, 0);

            int r = event.entityLiving.getActivePotionEffect(AlchemicalWizardry.customPotionFireFuse).getAmplifier();
        	float radius = 0.5f*r+0.5f;
        	
        	if(entityLiving.getActivePotionEffect(AlchemicalWizardry.customPotionFireFuse).getDuration()<=2)
        	{
        		entityLiving.worldObj.createExplosion(null, x, y, z, radius, false);
        	}
        }
    }
}
