package com.wayoftime.bloodmagic.core.living;

import com.wayoftime.bloodmagic.BloodMagic;
import com.wayoftime.bloodmagic.core.RegistrarBloodMagicLivingArmor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
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
        double jumpBonus = RegistrarBloodMagicLivingArmor.UPGRADE_JUMP.getBonusValue("jump", level).doubleValue();
        player.motionY += jumpBonus;

        if (level >= 3) {
            Vec3d lookVec = player.getLookVec();
            player.motionX += player.motionX == 0 ? 0 : lookVec.x * jumpBonus;
            player.motionZ += player.motionZ == 0 ? 0 : lookVec.z * jumpBonus;
        }
    }

    @SubscribeEvent
    public static void onDamage(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof EntityPlayer))
            return;

        if (event.getSource() != DamageSource.FALL)
            return;

        EntityPlayer player = (EntityPlayer) event.getEntity();
        LivingStats stats = LivingUtil.applyNewExperience(player, RegistrarBloodMagicLivingArmor.UPGRADE_JUMP, 1);
        if (stats == null)
            return;

        int level = stats.getLevel(RegistrarBloodMagicLivingArmor.UPGRADE_JUMP.getKey());
        double fallBonus = RegistrarBloodMagicLivingArmor.UPGRADE_JUMP.getBonusValue("fall", level).doubleValue();
        float oldAmount = event.getAmount();
        float newAmount = oldAmount * Math.max(1F - (float) fallBonus, 0F);
        event.setAmount(newAmount);
    }
}
