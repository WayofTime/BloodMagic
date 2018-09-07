package com.wayoftime.bloodmagic.core.living;

import com.wayoftime.bloodmagic.BloodMagic;
import com.wayoftime.bloodmagic.core.RegistrarBloodMagicLivingArmor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = BloodMagic.MODID)
public class LivingStatusWatcher {

    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event) {
        if (!(event.getEntity() instanceof EntityPlayer))
            return;

        EntityPlayer player = (EntityPlayer) event.getEntity();
        LivingStats stats = LivingUtil.applyNewExperience(player, RegistrarBloodMagicLivingArmor.UPGRADE_JUMP, 1);
        if (stats == null)
            return;

        int level = stats.getLevel(RegistrarBloodMagicLivingArmor.UPGRADE_JUMP.getKey());
        player.motionY += 0.05 * level;

        if (level >= 3) {
            Vec3d lookVec = player.getLookVec();
            player.motionX += player.motionX == 0 ? 0 : lookVec.x * 0.07D * level;
            player.motionZ += player.motionZ == 0 ? 0 : lookVec.z * 0.07D * level;
        }
    }
}
