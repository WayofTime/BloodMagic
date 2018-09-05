package com.wayoftime.bloodmagic.core.altar;

import com.wayoftime.bloodmagic.core.type.ComponentType;
import net.minecraft.util.math.BlockPos;

public class AltarComponent {

    private final BlockPos offset;
    private final ComponentType type;
    private boolean upgradeSlot;

    public AltarComponent(BlockPos offset, ComponentType type) {
        this.offset = offset;
        this.type = type;
    }

    public AltarComponent(BlockPos offset) {
        this(offset, ComponentType.NOT_AIR);
    }

    public BlockPos getOffset() {
        return offset;
    }

    public ComponentType getType() {
        return type;
    }

    public AltarComponent asUpgradeSlot() {
        this.upgradeSlot = true;
        return this;
    }

    public boolean isUpgradeSlot() {
        return upgradeSlot;
    }
}
