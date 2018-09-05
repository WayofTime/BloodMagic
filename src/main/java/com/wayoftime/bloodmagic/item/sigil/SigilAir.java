package com.wayoftime.bloodmagic.item.sigil;

import com.wayoftime.bloodmagic.core.network.Binding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class SigilAir implements ISigil {

    @Override
    public int getCost() {
        return 50;
    }

    @Nonnull
    @Override
    public EnumActionResult onRightClick(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull EnumHand hand, Binding binding) {
        if (world.isRemote) {
            Vec3d vec = player.getLookVec();
            double wantedVelocity = 1.7;

            // TODO - Revisit after potions
//            if (player.isPotionActive(RegistrarBloodMagic.BOOST)) {
//                int amplifier = player.getActivePotionEffect(RegistrarBloodMagic.BOOST).getAmplifier();
//                wantedVelocity += (1 + amplifier) * (0.35);
//            }

            player.motionX = vec.x * wantedVelocity;
            player.motionY = vec.y * wantedVelocity;
            player.motionZ = vec.z * wantedVelocity;
            world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
        }

        if (!world.isRemote)
            player.fallDistance = 0;

        return EnumActionResult.SUCCESS;
    }
}
