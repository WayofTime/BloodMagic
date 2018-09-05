package com.wayoftime.bloodmagic.core.altar;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.function.Consumer;

import static com.wayoftime.bloodmagic.core.type.ComponentType.*;

public enum AltarTier {

    ONE {
        @Override
        public void buildComponents(Consumer<AltarComponent> components) {
            // No-op
        }
    },
    TWO {
        @Override
        public void buildComponents(Consumer<AltarComponent> components) {
            components.accept(new AltarComponent(new BlockPos(-1, -1, -1), BLOOD_RUNE));
            components.accept(new AltarComponent(new BlockPos(0, -1, -1), BLOOD_RUNE).asUpgradeSlot());
            components.accept(new AltarComponent(new BlockPos(1, -1, -1), BLOOD_RUNE));
            components.accept(new AltarComponent(new BlockPos(-1, -1, 0), BLOOD_RUNE).asUpgradeSlot());
            components.accept(new AltarComponent(new BlockPos(1, -1, 0), BLOOD_RUNE).asUpgradeSlot());
            components.accept(new AltarComponent(new BlockPos(-1, -1, 1), BLOOD_RUNE));
            components.accept(new AltarComponent(new BlockPos(0, -1, 1), BLOOD_RUNE).asUpgradeSlot());
            components.accept(new AltarComponent(new BlockPos(1, -1, 1), BLOOD_RUNE));
        }
    },
    THREE {
        @Override
        public void buildComponents(Consumer<AltarComponent> components) {
            // Re-list the tier 2 non-upgrade components. Leaves out the upgradeable components so they aren't double counted
            components.accept(new AltarComponent(new BlockPos(-1, -1, -1), BLOOD_RUNE).asUpgradeSlot());
            components.accept(new AltarComponent(new BlockPos(1, -1, -1), BLOOD_RUNE).asUpgradeSlot());
            components.accept(new AltarComponent(new BlockPos(-1, -1, 1), BLOOD_RUNE).asUpgradeSlot());
            components.accept(new AltarComponent(new BlockPos(1, -1, 1), BLOOD_RUNE).asUpgradeSlot());

            components.accept(new AltarComponent(new BlockPos(-3, -1, -3)));
            components.accept(new AltarComponent(new BlockPos(-3, 0, -3)));
            components.accept(new AltarComponent(new BlockPos(3, -1, -3)));
            components.accept(new AltarComponent(new BlockPos(3, 0, -3)));
            components.accept(new AltarComponent(new BlockPos(-3, -1, 3)));
            components.accept(new AltarComponent(new BlockPos(-3, 0, 3)));
            components.accept(new AltarComponent(new BlockPos(3, -1, 3)));
            components.accept(new AltarComponent(new BlockPos(3, 0, 3)));
            components.accept(new AltarComponent(new BlockPos(-3, 1, -3), GLOWSTONE));
            components.accept(new AltarComponent(new BlockPos(3, 1, -3), GLOWSTONE));
            components.accept(new AltarComponent(new BlockPos(-3, 1, 3), GLOWSTONE));
            components.accept(new AltarComponent(new BlockPos(3, 1, 3), GLOWSTONE));

            for (int i = -2; i <= 2; i++) {
                components.accept(new AltarComponent(new BlockPos(3, -2, i), BLOOD_RUNE).asUpgradeSlot());
                components.accept(new AltarComponent(new BlockPos(-3, -2, i), BLOOD_RUNE).asUpgradeSlot());
                components.accept(new AltarComponent(new BlockPos(i, -2, 3), BLOOD_RUNE).asUpgradeSlot());
                components.accept(new AltarComponent(new BlockPos(i, -2, -3), BLOOD_RUNE).asUpgradeSlot());
            }
        }
    },
    FOUR {
        @Override
        public void buildComponents(Consumer<AltarComponent> components) {
            for (int i = -3; i <= 3; i++) {
                components.accept(new AltarComponent(new BlockPos(5, -3, i), BLOOD_RUNE).asUpgradeSlot());
                components.accept(new AltarComponent(new BlockPos(-5, -3, i), BLOOD_RUNE).asUpgradeSlot());
                components.accept(new AltarComponent(new BlockPos(i, -3, 5), BLOOD_RUNE).asUpgradeSlot());
                components.accept(new AltarComponent(new BlockPos(i, -3, -5), BLOOD_RUNE).asUpgradeSlot());
            }

            for (int i = -2; i <= 1; i++) {
                components.accept(new AltarComponent(new BlockPos(5, i, 5)));
                components.accept(new AltarComponent(new BlockPos(5, i, -5)));
                components.accept(new AltarComponent(new BlockPos(-5, i, -5)));
                components.accept(new AltarComponent(new BlockPos(-5, i, 5)));
            }

            components.accept(new AltarComponent(new BlockPos(5, 2, 5), BLOODSTONE));
            components.accept(new AltarComponent(new BlockPos(5, 2, -5), BLOODSTONE));
            components.accept(new AltarComponent(new BlockPos(-5, 2, -5), BLOODSTONE));
            components.accept(new AltarComponent(new BlockPos(-5, 2, 5), BLOODSTONE));
        }
    },
    FIVE {
        @Override
        public void buildComponents(Consumer<AltarComponent> components) {
            components.accept(new AltarComponent(new BlockPos(-8, -3, 8), BEACON));
            components.accept(new AltarComponent(new BlockPos(-8, -3, -8), BEACON));
            components.accept(new AltarComponent(new BlockPos(8, -3, -8), BEACON));
            components.accept(new AltarComponent(new BlockPos(8, -3, 8), BEACON));

            for (int i = -6; i <= 6; i++) {
                components.accept(new AltarComponent(new BlockPos(8, -4, i), BLOOD_RUNE).asUpgradeSlot());
                components.accept(new AltarComponent(new BlockPos(-8, -4, i), BLOOD_RUNE).asUpgradeSlot());
                components.accept(new AltarComponent(new BlockPos(i, -4, 8), BLOOD_RUNE).asUpgradeSlot());
                components.accept(new AltarComponent(new BlockPos(i, -4, -8), BLOOD_RUNE).asUpgradeSlot());
            }
        }
    },
    SIX {
        @Override
        public void buildComponents(Consumer<AltarComponent> components) {
            for (int i = -4; i <= 2; i++) {
                components.accept(new AltarComponent(new BlockPos(11, i, 11)));
                components.accept(new AltarComponent(new BlockPos(-11, i, -11)));
                components.accept(new AltarComponent(new BlockPos(11, i, -11)));
                components.accept(new AltarComponent(new BlockPos(-11, i, 11)));
            }

            components.accept(new AltarComponent(new BlockPos(11, 3, 11), CRYSTAL));
            components.accept(new AltarComponent(new BlockPos(-11, 3, -11), CRYSTAL));
            components.accept(new AltarComponent(new BlockPos(11, 3, -11), CRYSTAL));
            components.accept(new AltarComponent(new BlockPos(-11, 3, 11), CRYSTAL));

            for (int i = -9; i <= 9; i++) {
                components.accept(new AltarComponent(new BlockPos(11, -5, i), BLOOD_RUNE).asUpgradeSlot());
                components.accept(new AltarComponent(new BlockPos(-11, -5, i), BLOOD_RUNE).asUpgradeSlot());
                components.accept(new AltarComponent(new BlockPos(i, -5, 11), BLOOD_RUNE).asUpgradeSlot());
                components.accept(new AltarComponent(new BlockPos(i, -5, -11), BLOOD_RUNE).asUpgradeSlot());
            }
        }
    };

    public static final AltarTier[] VALUES = values();

    private final List<AltarComponent> components;
    private final int displayInt;

    AltarTier() {
        List<AltarComponent> list = Lists.newArrayList();
        buildComponents(list::add);
        this.components = ImmutableList.copyOf(list);
        this.displayInt = ordinal() + 1;
    }

    public abstract void buildComponents(Consumer<AltarComponent> components);

    public List<AltarComponent> getComponents() {
        return components;
    }

    public int getDisplayNumber() {
        return displayInt;
    }
}
