package WayofTime.bloodmagic.potion;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.core.RegistrarBloodMagic;
import WayofTime.bloodmagic.event.SacrificeKnifeUsedEvent;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.potion.PotionEffect;
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
    public static Map<World, List<EntityPlayer>> flightListMap = new HashMap<>();
    public static Map<World, List<EntityLivingBase>> noGravityListMap = new HashMap<>();

    @SubscribeEvent
    public static void onLivingJumpEvent(LivingEvent.LivingJumpEvent event) {
        EntityLivingBase eventEntityLiving = event.getEntityLiving();

        if (eventEntityLiving.isPotionActive(RegistrarBloodMagic.BOOST)) {
            int i = eventEntityLiving.getActivePotionEffect(RegistrarBloodMagic.BOOST).getAmplifier();
            eventEntityLiving.motionY += (0.1f) * (2 + i);
        }

        if (eventEntityLiving.isPotionActive(RegistrarBloodMagic.GROUNDED))
            eventEntityLiving.motionY = 0;
    }

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        EntityLivingBase eventEntityLiving = event.getEntityLiving();

        if (eventEntityLiving.isPotionActive(RegistrarBloodMagic.HEAVY_HEART)) {
            int i = eventEntityLiving.getActivePotionEffect(RegistrarBloodMagic.HEAVY_HEART).getAmplifier() + 1;
            event.setDamageMultiplier(event.getDamageMultiplier() + i);
            event.setDistance(event.getDistance() + i);
        }
    }

    @SubscribeEvent
    public static void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase eventEntityLiving = event.getEntityLiving();
        List<EntityPlayer> flightList = flightListMap.getOrDefault(eventEntityLiving.getEntityWorld(), Lists.newArrayList());

        if (eventEntityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) eventEntityLiving;
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

        if (event.getEntityLiving().isPotionActive(RegistrarBloodMagic.BOOST)) {
            int amplifier = event.getEntityLiving().getActivePotionEffect(RegistrarBloodMagic.BOOST).getAmplifier();
            float percentIncrease = (amplifier + 1) * 0.5F;

            boolean isPlayerAndFlying = eventEntityLiving instanceof EntityPlayer && ((EntityPlayer) eventEntityLiving).capabilities.isFlying;
            if (percentIncrease != 0 && (eventEntityLiving.onGround || isPlayerAndFlying) &&
                    (eventEntityLiving.moveForward != 0 || eventEntityLiving.moveStrafing != 0 || eventEntityLiving.motionY != 0)) {

                eventEntityLiving.travel(eventEntityLiving.moveStrafing * percentIncrease,
                        isPlayerAndFlying ? eventEntityLiving.moveVertical * percentIncrease : 0, // TODO: Vertical movement doesn't seem to be impacted even with excessive values
                        eventEntityLiving.moveForward * percentIncrease);

                if (isPlayerAndFlying && eventEntityLiving.motionY != 0) // TODO: remove when entity.travel() works with vertical movement or a better solution exists.
                    eventEntityLiving.motionY *= (1 + Math.min(percentIncrease, 0.75F)); // if this goes too high, it escalates
            }
        }

        List<EntityLivingBase> noGravityList = noGravityListMap.getOrDefault(event.getEntityLiving().getEntityWorld(), Lists.newArrayList());
        if (noGravityList != null) {
            if (eventEntityLiving.isPotionActive(RegistrarBloodMagic.SUSPENDED) && !eventEntityLiving.hasNoGravity()) {
                eventEntityLiving.setNoGravity(true);
                noGravityList.add(eventEntityLiving);
            } else if (noGravityList.contains(eventEntityLiving)) {
                eventEntityLiving.setNoGravity(false);
                noGravityList.remove(eventEntityLiving);
            }
        }
        
        if (eventEntityLiving.isPotionActive(RegistrarBloodMagic.GROUNDED)) {
            PotionEffect activeEffect = eventEntityLiving.getActivePotionEffect(RegistrarBloodMagic.GROUNDED);
            if (activeEffect != null) {
                if (eventEntityLiving instanceof EntityPlayer && ((EntityPlayer) eventEntityLiving).capabilities.isFlying)
                    eventEntityLiving.motionY -= (0.05D * (double) activeEffect.getAmplifier() + 1 - eventEntityLiving.motionY) * 0.2D;
                else
                    eventEntityLiving.motionY -= (0.1D * (double) activeEffect.getAmplifier() + 1 - eventEntityLiving.motionY) * 0.2D;
            }
        }

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

                if (projectile instanceof EntityArrow)
                    throwingEntity = ((EntityArrow) projectile).shootingEntity;
                else if (projectile instanceof EntityThrowable)
                    throwingEntity = ((EntityThrowable) projectile).getThrower();

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

    @SubscribeEvent
    public static void onEntityHurtEvent(LivingDamageEvent event) {
        if (event.getSource() == DamageSource.FALL)
            if (event.getEntityLiving().isPotionActive(RegistrarBloodMagic.FEATHERED))
                event.setCanceled(true);
    }
}
