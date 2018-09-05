package com.wayoftime.bloodmagic.item.sigil;

import com.wayoftime.bloodmagic.core.network.Binding;
import com.wayoftime.bloodmagic.core.network.SoulNetwork;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class SigilDivination implements ISigil {

    @Override
    public int getCost() {
        return 0;
    }

    @Nonnull
    @Override
    public EnumActionResult onRightClick(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull EnumHand hand, Binding binding) {
        player.sendStatusMessage(new TextComponentString("Current Essence: " + SoulNetwork.get(binding.getOwnerId()).getEssence()), true);
        return EnumActionResult.PASS;
    }
}
