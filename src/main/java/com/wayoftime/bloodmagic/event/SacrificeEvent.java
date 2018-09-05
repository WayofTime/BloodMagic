package com.wayoftime.bloodmagic.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

public class SacrificeEvent extends Event {

    protected final EntityLivingBase sacrificed;
    private final int baseAmount;
    private int modifiedAmount;

    public SacrificeEvent(EntityLivingBase sacrificed, int baseAmount, int modifiedAmount) {
        this.sacrificed = sacrificed;
        this.baseAmount = baseAmount;
        this.modifiedAmount = modifiedAmount;
    }

    public EntityLivingBase getSacrificed() {
        return sacrificed;
    }

    public int getBaseAmount() {
        return baseAmount;
    }

    public int getModifiedAmount() {
        return modifiedAmount;
    }

    public void setModifiedAmount(int modifiedAmount) {
        this.modifiedAmount = modifiedAmount;
    }

    public static class SelfSacrifice extends SacrificeEvent {

        public SelfSacrifice(EntityLivingBase sacrificed, int baseAmount, int modifiedAmount) {
            super(sacrificed, baseAmount, modifiedAmount);
        }

        public EntityPlayer getPlayer() {
            return (EntityPlayer) sacrificed;
        }
    }
}
