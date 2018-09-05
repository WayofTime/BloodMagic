package com.wayoftime.bloodmagic.item;

import com.wayoftime.bloodmagic.core.network.Binding;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Implement this interface on any Item that can be bound to a player.
 */
public interface IBindable {

    /**
     * Gets an object that stores who this item is bound to.
     * <p>
     * If the item is not bound, this will be null.
     *
     * @param stack - The owned ItemStack
     * @return - The binding object
     */
    @Nullable
    default Binding getBinding(ItemStack stack) {
        Binding binding = Binding.fromStack(stack);
        return !stack.isEmpty() && binding != null ? binding : null;
    }

    /**
     * Called when the player attempts to bind the item.
     *
     * @param player - The Player attempting to bind the item
     * @param stack  - The ItemStack to attempt binding
     * @return If binding was successful.
     */
    default boolean onBind(EntityPlayer player, ItemStack stack) {
        return true;
    }

    static void applyBinding(ItemStack stack, EntityPlayer player) {
        Binding binding = new Binding(player);
        applyBinding(stack, binding);
    }

    static void applyBinding(ItemStack stack, Binding binding) {
        if (!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());

        stack.getTagCompound().setTag("binding", binding.serializeNBT());
    }

    static void appendTooltip(Binding binding, List<String> tooltip) {
        if (binding != null)
            tooltip.add(I18n.format("tooltip.bloodmagic:bound_owner", binding.getOwnerName()));
    }
}