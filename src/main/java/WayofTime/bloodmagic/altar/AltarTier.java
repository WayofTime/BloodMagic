package WayofTime.bloodmagic.altar;

import com.google.common.collect.Lists;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.function.Consumer;

public enum AltarTier {
    ONE() {
        @Override
        public void buildComponents(Consumer<AltarComponent> components) {
            // Nada
        }
    }, TWO() {
        @Override
        public void buildComponents(Consumer<AltarComponent> components) {
            components.accept(new AltarComponent(new BlockPos(-1, -1, -1), ComponentType.BLOODRUNE));
            components.accept(new AltarComponent(new BlockPos(0, -1, -1), ComponentType.BLOODRUNE).setUpgradeSlot());
            components.accept(new AltarComponent(new BlockPos(1, -1, -1), ComponentType.BLOODRUNE));
            components.accept(new AltarComponent(new BlockPos(-1, -1, 0), ComponentType.BLOODRUNE).setUpgradeSlot());
            components.accept(new AltarComponent(new BlockPos(1, -1, 0), ComponentType.BLOODRUNE).setUpgradeSlot());
            components.accept(new AltarComponent(new BlockPos(-1, -1, 1), ComponentType.BLOODRUNE));
            components.accept(new AltarComponent(new BlockPos(0, -1, 1), ComponentType.BLOODRUNE).setUpgradeSlot());
            components.accept(new AltarComponent(new BlockPos(1, -1, 1), ComponentType.BLOODRUNE));
        }
    },
    THREE() {
        @Override
        public void buildComponents(Consumer<AltarComponent> components) {
            // Doesn't pull from tier 2 because upgrades slots are different
            components.accept(new AltarComponent(new BlockPos(-1, -1, -1), ComponentType.BLOODRUNE).setUpgradeSlot());
            components.accept(new AltarComponent(new BlockPos(0, -1, -1), ComponentType.BLOODRUNE).setUpgradeSlot());
            components.accept(new AltarComponent(new BlockPos(1, -1, -1), ComponentType.BLOODRUNE).setUpgradeSlot());
            components.accept(new AltarComponent(new BlockPos(-1, -1, 0), ComponentType.BLOODRUNE).setUpgradeSlot());
            components.accept(new AltarComponent(new BlockPos(1, -1, 0), ComponentType.BLOODRUNE).setUpgradeSlot());
            components.accept(new AltarComponent(new BlockPos(-1, -1, 1), ComponentType.BLOODRUNE).setUpgradeSlot());
            components.accept(new AltarComponent(new BlockPos(0, -1, 1), ComponentType.BLOODRUNE).setUpgradeSlot());
            components.accept(new AltarComponent(new BlockPos(1, -1, 1), ComponentType.BLOODRUNE).setUpgradeSlot());
            components.accept(new AltarComponent(new BlockPos(-3, -1, -3)));
            components.accept(new AltarComponent(new BlockPos(-3, 0, -3)));
            components.accept(new AltarComponent(new BlockPos(3, -1, -3)));
            components.accept(new AltarComponent(new BlockPos(3, 0, -3)));
            components.accept(new AltarComponent(new BlockPos(-3, -1, 3)));
            components.accept(new AltarComponent(new BlockPos(-3, 0, 3)));
            components.accept(new AltarComponent(new BlockPos(3, -1, 3)));
            components.accept(new AltarComponent(new BlockPos(3, 0, 3)));
            components.accept(new AltarComponent(new BlockPos(-3, 1, -3), ComponentType.GLOWSTONE));
            components.accept(new AltarComponent(new BlockPos(3, 1, -3), ComponentType.GLOWSTONE));
            components.accept(new AltarComponent(new BlockPos(-3, 1, 3), ComponentType.GLOWSTONE));
            components.accept(new AltarComponent(new BlockPos(3, 1, 3), ComponentType.GLOWSTONE));

            for (int i = -2; i <= 2; i++) {
                components.accept(new AltarComponent(new BlockPos(3, -2, i), ComponentType.BLOODRUNE).setUpgradeSlot());
                components.accept(new AltarComponent(new BlockPos(-3, -2, i), ComponentType.BLOODRUNE).setUpgradeSlot());
                components.accept(new AltarComponent(new BlockPos(i, -2, 3), ComponentType.BLOODRUNE).setUpgradeSlot());
                components.accept(new AltarComponent(new BlockPos(i, -2, -3), ComponentType.BLOODRUNE).setUpgradeSlot());
            }
        }
    },
    FOUR() {
        @Override
        public void buildComponents(Consumer<AltarComponent> components) {
            THREE.getAltarComponents().forEach(components);

            for (int i = -3; i <= 3; i++) {
                components.accept(new AltarComponent(new BlockPos(5, -3, i), ComponentType.BLOODRUNE).setUpgradeSlot());
                components.accept(new AltarComponent(new BlockPos(-5, -3, i), ComponentType.BLOODRUNE).setUpgradeSlot());
                components.accept(new AltarComponent(new BlockPos(i, -3, 5), ComponentType.BLOODRUNE).setUpgradeSlot());
                components.accept(new AltarComponent(new BlockPos(i, -3, -5), ComponentType.BLOODRUNE).setUpgradeSlot());
            }

            for (int i = -2; i <= 1; i++) {
                components.accept(new AltarComponent(new BlockPos(5, i, 5)));
                components.accept(new AltarComponent(new BlockPos(5, i, -5)));
                components.accept(new AltarComponent(new BlockPos(-5, i, -5)));
                components.accept(new AltarComponent(new BlockPos(-5, i, 5)));
            }

            components.accept(new AltarComponent(new BlockPos(5, 2, 5), ComponentType.BLOODSTONE));
            components.accept(new AltarComponent(new BlockPos(5, 2, -5), ComponentType.BLOODSTONE));
            components.accept(new AltarComponent(new BlockPos(-5, 2, -5), ComponentType.BLOODSTONE));
            components.accept(new AltarComponent(new BlockPos(-5, 2, 5), ComponentType.BLOODSTONE));
        }
    },
    FIVE() {
        @Override
        public void buildComponents(Consumer<AltarComponent> components) {
            FOUR.getAltarComponents().forEach(components);
            components.accept(new AltarComponent(new BlockPos(-8, -3, 8), ComponentType.BEACON));
            components.accept(new AltarComponent(new BlockPos(-8, -3, -8), ComponentType.BEACON));
            components.accept(new AltarComponent(new BlockPos(8, -3, -8), ComponentType.BEACON));
            components.accept(new AltarComponent(new BlockPos(8, -3, 8), ComponentType.BEACON));

            for (int i = -6; i <= 6; i++) {
                components.accept(new AltarComponent(new BlockPos(8, -4, i), ComponentType.BLOODRUNE).setUpgradeSlot());
                components.accept(new AltarComponent(new BlockPos(-8, -4, i), ComponentType.BLOODRUNE).setUpgradeSlot());
                components.accept(new AltarComponent(new BlockPos(i, -4, 8), ComponentType.BLOODRUNE).setUpgradeSlot());
                components.accept(new AltarComponent(new BlockPos(i, -4, -8), ComponentType.BLOODRUNE).setUpgradeSlot());
            }
        }
    },
    SIX() {
        @Override
        public void buildComponents(Consumer<AltarComponent> components) {
            FIVE.getAltarComponents().forEach(components);

            for (int i = -4; i <= 2; i++) {
                components.accept(new AltarComponent(new BlockPos(11, i, 11)));
                components.accept(new AltarComponent(new BlockPos(-11, i, -11)));
                components.accept(new AltarComponent(new BlockPos(11, i, -11)));
                components.accept(new AltarComponent(new BlockPos(-11, i, 11)));
            }

            components.accept(new AltarComponent(new BlockPos(11, 3, 11), ComponentType.CRYSTAL));
            components.accept(new AltarComponent(new BlockPos(-11, 3, -11), ComponentType.CRYSTAL));
            components.accept(new AltarComponent(new BlockPos(11, 3, -11), ComponentType.CRYSTAL));
            components.accept(new AltarComponent(new BlockPos(-11, 3, 11), ComponentType.CRYSTAL));

            for (int i = -9; i <= 9; i++) {
                components.accept(new AltarComponent(new BlockPos(11, -5, i), ComponentType.BLOODRUNE).setUpgradeSlot());
                components.accept(new AltarComponent(new BlockPos(-11, -5, i), ComponentType.BLOODRUNE).setUpgradeSlot());
                components.accept(new AltarComponent(new BlockPos(i, -5, 11), ComponentType.BLOODRUNE).setUpgradeSlot());
                components.accept(new AltarComponent(new BlockPos(i, -5, -11), ComponentType.BLOODRUNE).setUpgradeSlot());
            }
        }
    };

    public static final int MAXTIERS = values().length;

    private List<AltarComponent> altarComponents;

    AltarTier() {
        this.altarComponents = Lists.newArrayList();

        buildComponents(altarComponents::add);
    }

    public abstract void buildComponents(Consumer<AltarComponent> components);

    public int toInt() {
        return ordinal() + 1;
    }

    public List<AltarComponent> getAltarComponents() {
        return altarComponents;
    }
}
