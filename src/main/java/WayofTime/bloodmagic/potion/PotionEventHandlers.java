package WayofTime.bloodmagic.potion;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.core.RegistrarBloodMagic;
import WayofTime.bloodmagic.event.SacrificeKnifeUsedEvent;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = BloodMagic.MODID)
public class PotionEventHandlers {
    public static Map<World, List<PlayerEntity>> flightListMap = new HashMap<>();
    public static Map<World, List<LivingEntity>> noGravityListMap = new HashMap<>();

    @SubscribeEvent
    public static void onLivingJumpEvent(LivingEvent.LivingJumpEvent event) {
        LivingEntity eventEntityLiving = event.getEntityLiving();

        if (eventEntityLiving.isPotionActive(RegistrarBloodMagic.BOOST)) {
            int i = eventEntityLiving.getActivePotionEffect(RegistrarBloodMagic.BOOST).getAmplifier();
            eventEntityLiving.motionY += (0.1f) * (2 + i);
        }

        if (eventEntityLiving.isPotionActive(RegistrarBloodMagic.GROUNDED))
            eventEntityLiving.motionY = 0;
    }

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        LivingEntity eventEntityLiving = event.getEntityLiving();

        if (eventEntityLiving.isPotionActive(RegistrarBloodMagic.HEAVY_HEART)) {
            int i = eventEntityLiving.getActivePotionEffect(RegistrarBloodMagic.HEAVY_HEART).getAmplifier() + 1;
            event.setDamageMultiplier(event.getDamageMultiplier() + i);
            event.setDistance(event.getDistance() + i);
        }
    }

    @SubscribeEvent
    public static void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
        LivingEntity eventEntityLiving = event.getEntityLiving();
        List<PlayerEntity> flightList = flightListMap.getOrDefault(eventEntityLiving.getEntityWorld(), Lists.newArrayList());

        if (eventEntityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) eventEntityLiving;
            if (!player.world.isRemote) {
                if (player.isPotionActive(RegistrarBloodMagic.FLIGHT)) {
                    if (!player.isSpectator() && !player.capabilities.allowFlying) {
                        player.capabilities.allowFlying = true;
                        player.sendPlayerAbilities();
                        flightList.add(player);
                    }
                } else {
                    if (flightList.contains(player)) {
                        player.capabilities.allowFlying = false;
                        player.capabilities.isFlying = false;
                        player.sendPlayerAbilities();
                        flightList.remove(player);
                    }
                }
            }
        }
//        if (eventEntityLiving.isPotionActive(ModPotions.boost))
//        {
//            int i = eventEntityLiving.getActivePotionEffect(ModPotions.boost).getAmplifier();
//            {
//                float percentIncrease = (i + 1) * 0.05f;
//
//                if (eventEntityLiving instanceof EntityPlayer)
//                {
//                    EntityPlayer entityPlayer = (EntityPlayer) eventEntityLiving;
//
//                    if ((entityPlayer.onGround || entityPlayer.capabilities.isFlying) && entityPlayer.moveForward > 0F)
//                        entityPlayer.moveFlying(0F, 1F, entityPlayer.capabilities.isFlying ? (percentIncrease / 2.0f) : percentIncrease);
//                }
//            }
//        }
        List<LivingEntity> noGravityList = noGravityListMap.getOrDefault(event.getEntityLiving().getEntityWorld(), Lists.newArrayList());
        if (eventEntityLiving.isPotionActive(RegistrarBloodMagic.SUSPENDED) && !eventEntityLiving.hasNoGravity()) {
            eventEntityLiving.setNoGravity(true);
            noGravityList.add(eventEntityLiving);
        } else if (noGravityList.contains(eventEntityLiving)) {
            eventEntityLiving.setNoGravity(false);
            noGravityList.remove(eventEntityLiving);
        }

        if (eventEntityLiving.isPotionActive(RegistrarBloodMagic.GROUNDED))
            if (eventEntityLiving instanceof PlayerEntity && ((PlayerEntity) eventEntityLiving).capabilities.isFlying)
                eventEntityLiving.motionY -= (0.05D * (double) (eventEntityLiving.getActivePotionEffect(RegistrarBloodMagic.GROUNDED).getAmplifier() + 1) - eventEntityLiving.motionY) * 0.2D;
            else
                eventEntityLiving.motionY -= (0.1D * (double) (eventEntityLiving.getActivePotionEffect(RegistrarBloodMagic.GROUNDED).getAmplifier() + 1) - eventEntityLiving.motionY) * 0.2D;

        if (eventEntityLiving.isPotionActive(RegistrarBloodMagic.WHIRLWIND)) {
            int d0 = 3;
            AxisAlignedBB axisAlignedBB = new AxisAlignedBB(eventEntityLiving.posX - 0.5, eventEntityLiving.posY - 0.5, eventEntityLiving.posZ - 0.5, eventEntityLiving.posX + 0.5, eventEntityLiving.posY + 0.5, eventEntityLiving.posZ + 0.5).expand(d0, d0, d0);
            List<Entity> entityList = eventEntityLiving.getEntityWorld().getEntitiesWithinAABB(Entity.class, axisAlignedBB);

            for (Entity projectile : entityList) {
                if (projectile == null)
                    continue;
                if (!(projectile instanceof IProjectile))
                    continue;

                Entity throwingEntity = null;

                if (projectile instanceof AbstractArrowEntity)
                    throwingEntity = ((AbstractArrowEntity) projectile).shootingEntity;
                else if (projectile instanceof ThrowableEntity)
                    throwingEntity = ((ThrowableEntity) projectile).getThrower();

                if (throwingEntity != null && throwingEntity.equals(eventEntityLiving))
                    continue;

                double delX = projectile.posX - eventEntityLiving.posX;
                double delY = projectile.posY - eventEntityLiving.posY;
                double delZ = projectile.posZ - eventEntityLiving.posZ;

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
            event.getEntityPlayer().addPotionEffect(new EffectInstance(RegistrarBloodMagic.SOUL_FRAY, 400));
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

    @SubscribeEvent
    public static void onEntityHurtEvent(LivingDamageEvent event) {
        if (event.getSource() == DamageSource.FALL)
            if (event.getEntityLiving().isPotionActive(RegistrarBloodMagic.FEATHERED))
                event.setCanceled(true);
    }
}
