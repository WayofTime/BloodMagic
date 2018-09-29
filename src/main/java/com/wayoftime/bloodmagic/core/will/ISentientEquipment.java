package com.wayoftime.bloodmagic.core.will;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

public interface ISentientEquipment {

    @Nullable
    default DemonWillHolder getWillDrop(EntityPlayer player, EntityLivingBase attacked, ItemStack stack, int looting) {
        double willModifier = attacked instanceof EntitySlime ? 0.67 : 1;
        DemonWill type = getDemonWill(stack);
        DemonWillHolder ret = null;
        for (int i = 0; i <= looting; i++) {
            if (i == 0 || player.getEntityWorld().rand.nextDouble() <= 0.4) {
                if (ret == null)
                    ret = new DemonWillHolder(type, 0);

                ret.modifyAmount(willModifier * (getWillDropAmount(stack) * player.getEntityWorld().rand.nextDouble() * (attacked.getMaxHealth() / 20D)));
            }
        }

        return ret;
    }

    double getWillDropAmount(ItemStack stack);

    default boolean isActivated(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().getBoolean("sentientActivated");
    }

    default DemonWill getDemonWill(ItemStack stack) {
        NBTTagCompound tagCompound = stack.getTagCompound();
        if (tagCompound == null || !tagCompound.hasKey("willType"))
            return DemonWill.RAW;

        return DemonWill.valueOf(tagCompound.getString("willType"));
    }
}
