package com.wayoftime.bloodmagic.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class DemonicWillEvent extends Event  {

    @Cancelable
    public static class GatherWillDrops extends DemonicWillEvent {

        private final EntityPlayer player;
        private final EntityLivingBase attacked;
        private final ItemStack stack;
        private final int looting;
        private double droppedWill;

        public GatherWillDrops(EntityPlayer player, EntityLivingBase attacked, ItemStack stack, int looting, double droppedWill) {
            this.player = player;
            this.attacked = attacked;
            this.stack = stack;
            this.looting = looting;
            this.droppedWill = droppedWill;
        }

        public EntityPlayer getPlayer() {
            return player;
        }

        public EntityLivingBase getAttacked() {
            return attacked;
        }

        public ItemStack getStack() {
            return stack;
        }

        public int getLooting() {
            return looting;
        }

        public double getDroppedWill() {
            return droppedWill;
        }

        public void setDroppedWill(double droppedWill) {
            this.droppedWill = droppedWill;
        }
    }
}
