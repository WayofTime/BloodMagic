package WayofTime.bloodmagic.potion;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import WayofTime.bloodmagic.registry.ModPotions;

public class PotionEventHandlers
{
    public PotionEventHandlers()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onLivingJumpEvent(LivingEvent.LivingJumpEvent event)
    {
        if (event.entityLiving.isPotionActive(ModPotions.boost))
        {
            int i = event.entityLiving.getActivePotionEffect(ModPotions.boost).getAmplifier();
            event.entityLiving.motionY += (0.1f) * (2 + i);
        }

        // if (event.entityLiving.isPotionActive(ModPotions.heavyHeart)) {
        // event.entityLiving.motionY = 0;
        // }
    }

    @SubscribeEvent
    public void onEntityUpdate(LivingEvent.LivingUpdateEvent event)
    {
//        if (event.entityLiving.isPotionActive(ModPotions.boost))
//        {
//            int i = event.entityLiving.getActivePotionEffect(ModPotions.boost).getAmplifier();
//            {
//                float percentIncrease = (i + 1) * 0.05f;
//
//                if (event.entityLiving instanceof EntityPlayer)
//                {
//                    EntityPlayer entityPlayer = (EntityPlayer) event.entityLiving;
//
//                    if ((entityPlayer.onGround || entityPlayer.capabilities.isFlying) && entityPlayer.moveForward > 0F)
//                        entityPlayer.moveFlying(0F, 1F, entityPlayer.capabilities.isFlying ? (percentIncrease / 2.0f) : percentIncrease);
//                }
//            }
//        }

        if (event.entityLiving.isPotionActive(ModPotions.whirlwind))
        {
            int d0 = 3;
            AxisAlignedBB axisAlignedBB = AxisAlignedBB.fromBounds(event.entityLiving.posX - 0.5, event.entityLiving.posY - 0.5, event.entityLiving.posZ - 0.5, event.entityLiving.posX + 0.5, event.entityLiving.posY + 0.5, event.entityLiving.posZ + 0.5).expand(d0, d0, d0);
            List entityList = event.entityLiving.worldObj.getEntitiesWithinAABB(Entity.class, axisAlignedBB);

            for (Object thing : entityList)
            {
                Entity projectile = (Entity) thing;

                if (projectile == null)
                    continue;
                if (!(projectile instanceof IProjectile))
                    continue;

                Entity throwingEntity = null;

                if (projectile instanceof EntityArrow)
                    throwingEntity = ((EntityArrow) projectile).shootingEntity;
                else if (projectile instanceof EntityThrowable)
                    throwingEntity = ((EntityThrowable) projectile).getThrower();

                if (throwingEntity != null && throwingEntity.equals(event.entityLiving))
                    continue;

                double delX = projectile.posX - event.entityLiving.posX;
                double delY = projectile.posY - event.entityLiving.posY;
                double delZ = projectile.posZ - event.entityLiving.posZ;

                double angle = (delX * projectile.motionX + delY * projectile.motionY + delZ * projectile.motionZ) / (Math.sqrt(delX * delX + delY * delY + delZ * delZ) * Math.sqrt(projectile.motionX * projectile.motionX + projectile.motionY * projectile.motionY + projectile.motionZ * projectile.motionZ));

                angle = Math.acos(angle);

                if (angle < 3 * (Math.PI / 4))
                    continue; // angle is < 135 degrees

                if (throwingEntity != null)
                {
                    delX = -projectile.posX + throwingEntity.posX;
                    delY = -projectile.posY + (throwingEntity.posY + throwingEntity.getEyeHeight());
                    delZ = -projectile.posZ + throwingEntity.posZ;
                }

                double curVel = Math.sqrt(delX * delX + delY * delY + delZ * delZ);

                delX /= curVel;
                delY /= curVel;
                delZ /= curVel;
                double newVel = Math.sqrt(projectile.motionX * projectile.motionX + projectile.motionY * projectile.motionY + projectile.motionZ * projectile.motionZ);
                projectile.motionX = newVel * delX;
                projectile.motionY = newVel * delY;
                projectile.motionZ = newVel * delZ;
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerDamageEvent(LivingAttackEvent event)
    {
        if (event.entityLiving.isPotionActive(ModPotions.whirlwind) && event.isCancelable() && event.source.isProjectile())
            event.setCanceled(true);
    }

    @SubscribeEvent
    public void onEndermanTeleportEvent(EnderTeleportEvent event)
    {
        if (event.entityLiving.isPotionActive(ModPotions.planarBinding) && event.isCancelable())
        {
            event.setCanceled(true);
        }
    }
}
