package com.wayoftime.bloodmagic.item.sigil;

import com.wayoftime.bloodmagic.core.network.Binding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class SigilHaste implements ISigil.Toggle {

    @Override
    public int getCost() {
        return 250;
    }

    @Override
    public void onUpdate(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, @Nonnull World world, int itemSlot, boolean isHeld, @Nonnull Binding binding) {
        player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 2, 0, true, false)); // TODO - RegistrarBloodMagic.BOOST
    }
}
