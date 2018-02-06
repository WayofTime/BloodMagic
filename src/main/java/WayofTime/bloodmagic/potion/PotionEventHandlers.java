package WayofTime.bloodmagic.potion;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.apibutnotreally.event.SacrificeKnifeUsedEvent;
import WayofTime.bloodmagic.core.RegistrarBloodMagic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

@Mod.EventBusSubscriber(modid = BloodMagic.MODID)
public class PotionEventHandlers {

    @SubscribeEvent
    public static void onLivingJumpEvent(LivingEvent.LivingJumpEvent event) {
        if (event.getEntityLiving().isPotionActive(RegistrarBloodMagic.BOOST)) {
            int i = event.getEntityLiving().getActivePotionEffect(RegistrarBloodMagic.BOOST).getAmplifier();
            event.getEntityLiving().motionY += (0.1f) * (2 + i);
        }

        // if (event.getEntityLiving().isPotionActive(ModPotions.heavyHeart)) {
        // event.getEntityLiving().motionY = 0;
        // }
    }

    @SubscribeEvent
    public static void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
//        if (event.getEntityLiving().isPotionActive(ModPotions.boost))
//        {
//            int i = event.getEntityLiving().getActivePotionEffect(ModPotions.boost).getAmplifier();
//            {
//                float percentIncrease = (i + 1) * 0.05f;
//
//                if (event.getEntityLiving() instanceof EntityPlayer)
//                {
//                    EntityPlayer entityPlayer = (EntityPlayer) event.getEntityLiving();
//
//                    if ((entityPlayer.onGround || entityPlayer.capabilities.isFlying) && entityPlayer.moveForward > 0F)
//                        entityPlayer.moveFlying(0F, 1F, entityPlayer.capabilities.isFlying ? (percentIncrease / 2.0f) : percentIncrease);
//                }
//            }
//        }

        if (event.getEntityLiving().isPotionActive(RegistrarBloodMagic.WHIRLWIND)) {
            int d0 = 3;
            AxisAlignedBB axisAlignedBB = new AxisAlignedBB(event.getEntityLiving().posX - 0.5, event.getEntityLiving().posY - 0.5, event.getEntityLiving().posZ - 0.5, event.getEntityLiving().posX + 0.5, event.getEntityLiving().posY + 0.5, event.getEntityLiving().posZ + 0.5).expand(d0, d0, d0);
            List<Entity> entityList = event.getEntityLiving().getEntityWorld().getEntitiesWithinAABB(Entity.class, axisAlignedBB);

            for (Entity projectile : entityList) {
                if (projectile == null)
                    continue;
                if (!(projectile instanceof IProjectile))
                    continue;

                Entity throwingEntity = null;

                if (projectile instanceof EntityArrow)
                    throwingEntity = ((EntityArrow) projectile).shootingEntity;
                else if (projectile instanceof EntityThrowable)
                    throwingEntity = ((EntityThrowable) projectile).getThrower();

                if (throwingEntity != null && throwingEntity.equals(event.getEntityLiving()))
                    continue;

                double delX = projectile.posX - event.getEntityLiving().posX;
                double delY = projectile.posY - event.getEntityLiving().posY;
                double delZ = projectile.posZ - event.getEntityLiving().posZ;

                double angle = (delX * projectile.motionX + delY * projectile.motionY + delZ * projectile.motionZ) / (Math.sqrt(delX * delX + delY * delY + delZ * delZ) * Math.sqrt(projectile.motionX * projectile.motionX + projectile.motionY * projectile.motionY + projectile.motionZ * projectile.motionZ));

                angle = Math.acos(angle);

                if (angle < 3 * (Math.PI / 4))
                    continue; // angle is < 135 degrees

                if (throwingEntity != null) {
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

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.Clone event) {
        if (event.isWasDeath())
            event.getEntityPlayer().addPotionEffect(new PotionEffect(RegistrarBloodMagic.SOUL_FRAY, 400));
    }

    @SubscribeEvent
    public static void onSacrificeKnifeUsed(SacrificeKnifeUsedEvent event) {
        if (event.player.isPotionActive(RegistrarBloodMagic.SOUL_FRAY))
            event.lpAdded = (int) (event.lpAdded * 0.1D);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerDamageEvent(LivingAttackEvent event) {
        if (event.getEntityLiving().isPotionActive(RegistrarBloodMagic.WHIRLWIND) && event.isCancelable() && event.getSource().isProjectile())
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onEndermanTeleportEvent(EnderTeleportEvent event) {
        if (event.getEntityLiving().isPotionActive(RegistrarBloodMagic.PLANAR_BINDING) && event.isCancelable()) {
            event.setCanceled(true);
        }
    }
}
