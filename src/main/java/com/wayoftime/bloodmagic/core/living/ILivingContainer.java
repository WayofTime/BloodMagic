package com.wayoftime.bloodmagic.core.living;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface ILivingContainer {

    @Nullable
    default LivingStats getLivingStats(@Nonnull ItemStack stack) {
        if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey("livingStats"))
            return null;

        return LivingStats.fromNBT(stack.getTagCompound().getCompoundTag("livingStats"));
    }

    default void updateLivingStates(@Nonnull ItemStack stack, @Nullable LivingStats stats) {
        if (stats == null) {
            if (stack.hasTagCompound())
                stack.getTagCompound().removeTag("livingStats");
            return;
        }

        if (!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());

        stack.getTagCompound().setTag("livingStats", stats.serializeNBT());
    }

    @SideOnly(Side.CLIENT)
    static void appendLivingTooltip(LivingStats stats, List<String> tooltip, boolean trainable) {
        if (stats != null) {
            if (trainable)
                tooltip.add(I18n.format("tooltip.bloodmagic:living_points", stats.getUsedPoints(), stats.getMaxPoints()));

            stats.getUpgrades().forEach((k, v) -> {
                if (k.getLevel(v) <= 0)
                    return;

                if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || k.getNextRequirement(v) == 0)
                    tooltip.add(I18n.format(k.getUnlocalizedName()) + " " + I18n.format("enchantment.level." + k.getLevel(v)));
                else
                    tooltip.add(I18n.format(k.getUnlocalizedName()) + ": " + v + "/" + k.getNextRequirement(v));
            });
        }
    }
}
