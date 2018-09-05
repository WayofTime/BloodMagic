package com.wayoftime.bloodmagic.core.network;

import com.wayoftime.bloodmagic.core.util.OrbUtil;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class BloodOrb {

    private final ResourceLocation name;
    private final int tier;
    private final int capacity;
    private final int fillRate;

    public BloodOrb(ResourceLocation name, int tier, int capacity, int fillRate) {
        this.name = name;
        this.tier = tier;
        this.capacity = capacity;
        this.fillRate = fillRate;

        OrbUtil.addOrb(this);
    }

    public ResourceLocation getName() {
        return name;
    }

    public int getTier() {
        return tier;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getFillRate() {
        return fillRate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("tier", tier)
                .append("capacity", capacity)
                .append("fillRate", fillRate)
                .toString();
    }
}
