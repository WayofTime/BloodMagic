package WayofTime.bloodmagic.potion;

import WayofTime.bloodmagic.registry.ModPotions;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class PotionEventHandlers {

    public PotionEventHandlers() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onLivingJumpEvent(LivingEvent.LivingJumpEvent event) {
        if (event.entityLiving.isPotionActive(ModPotions.boost)) {
            int i = event.entityLiving.getActivePotionEffect(ModPotions.boost).getAmplifier();
            event.entityLiving.motionY += (0.1f) * (2 + i);
        }

//        if (event.entityLiving.isPotionActive(ModPotions.heavyHeart)) {
//            event.entityLiving.motionY = 0;
//        }
    }

    @SubscribeEvent
    public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
//        EntityLivingBase entityLiving = event.entityLiving;
//        double x = entityLiving.posX;
//        double y = entityLiving.posY;
//        double z = entityLiving.posZ;


        if (event.entityLiving.isPotionActive(ModPotions.boost)) {
            int i = event.entityLiving.getActivePotionEffect(ModPotions.boost).getAmplifier();
            {
                float percentIncrease = (i + 1) * 0.05f;

                if (event.entityLiving instanceof EntityPlayer) {
                    EntityPlayer entityPlayer = (EntityPlayer) event.entityLiving;
                    entityPlayer.stepHeight = 1.0f;

                    if ((entityPlayer.onGround || entityPlayer.capabilities.isFlying) && entityPlayer.moveForward > 0F)
                        entityPlayer.moveFlying(0F, 1F, entityPlayer.capabilities.isFlying ? (percentIncrease / 2.0f) : percentIncrease);
                }
            }
        }
    }
}
