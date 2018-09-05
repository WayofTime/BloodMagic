package com.wayoftime.bloodmagic.event;

import com.wayoftime.bloodmagic.core.network.Binding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class BindingEvent extends Event  {

    private final ItemStack stack;
    private final Binding binding;
    private final EntityPlayer player;

    public BindingEvent(ItemStack stack, Binding binding, EntityPlayer player) {
        this.stack = stack;
        this.binding = binding;
        this.player = player;
    }

    public ItemStack getStack() {
        return stack;
    }

    public Binding getBinding() {
        return binding;
    }

    public EntityPlayer getPlayer() {
        return player;
    }
}
