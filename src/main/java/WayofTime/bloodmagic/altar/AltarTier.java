package WayofTime.bloodmagic.altar;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

//@formatter:off
public enum AltarTier {
    ONE(), TWO() {
        @Override
        public void buildComponents() {
            altarComponents.add(new AltarComponent(new BlockPos(-1, -1, -1), ComponentType.BLOODRUNE));
            altarComponents.add(new AltarComponent(new BlockPos(0, -1, -1), ComponentType.BLOODRUNE).setUpgradeSlot());
            altarComponents.add(new AltarComponent(new BlockPos(1, -1, -1), ComponentType.BLOODRUNE));
            altarComponents.add(new AltarComponent(new BlockPos(-1, -1, 0), ComponentType.BLOODRUNE).setUpgradeSlot());
            altarComponents.add(new AltarComponent(new BlockPos(1, -1, 0), ComponentType.BLOODRUNE).setUpgradeSlot());
            altarComponents.add(new AltarComponent(new BlockPos(-1, -1, 1), ComponentType.BLOODRUNE));
            altarComponents.add(new AltarComponent(new BlockPos(0, -1, 1), ComponentType.BLOODRUNE).setUpgradeSlot());
            altarComponents.add(new AltarComponent(new BlockPos(1, -1, 1), ComponentType.BLOODRUNE));
        }
    },
    THREE() {
        @Override
        public void buildComponents() {
            altarComponents.add(new AltarComponent(new BlockPos(-1, -1, -1), ComponentType.BLOODRUNE).setUpgradeSlot());
            altarComponents.add(new AltarComponent(new BlockPos(0, -1, -1), ComponentType.BLOODRUNE).setUpgradeSlot());
            altarComponents.add(new AltarComponent(new BlockPos(1, -1, -1), ComponentType.BLOODRUNE).setUpgradeSlot());
            altarComponents.add(new AltarComponent(new BlockPos(-1, -1, 0), ComponentType.BLOODRUNE).setUpgradeSlot());
            altarComponents.add(new AltarComponent(new BlockPos(1, -1, 0), ComponentType.BLOODRUNE).setUpgradeSlot());
            altarComponents.add(new AltarComponent(new BlockPos(-1, -1, 1), ComponentType.BLOODRUNE).setUpgradeSlot());
            altarComponents.add(new AltarComponent(new BlockPos(0, -1, 1), ComponentType.BLOODRUNE).setUpgradeSlot());
            altarComponents.add(new AltarComponent(new BlockPos(1, -1, 1), ComponentType.BLOODRUNE).setUpgradeSlot());
            altarComponents.add(new AltarComponent(new BlockPos(-3, -1, -3)));
            altarComponents.add(new AltarComponent(new BlockPos(-3, 0, -3)));
            altarComponents.add(new AltarComponent(new BlockPos(3, -1, -3)));
            altarComponents.add(new AltarComponent(new BlockPos(3, 0, -3)));
            altarComponents.add(new AltarComponent(new BlockPos(-3, -1, 3)));
            altarComponents.add(new AltarComponent(new BlockPos(-3, 0, 3)));
            altarComponents.add(new AltarComponent(new BlockPos(3, -1, 3)));
            altarComponents.add(new AltarComponent(new BlockPos(3, 0, 3)));
            altarComponents.add(new AltarComponent(new BlockPos(-3, 1, -3), ComponentType.GLOWSTONE));
            altarComponents.add(new AltarComponent(new BlockPos(3, 1, -3), ComponentType.GLOWSTONE));
            altarComponents.add(new AltarComponent(new BlockPos(-3, 1, 3), ComponentType.GLOWSTONE));
            altarComponents.add(new AltarComponent(new BlockPos(3, 1, 3), ComponentType.GLOWSTONE));

            for (int i = -2; i <= 2; i++) {
                altarComponents.add(new AltarComponent(new BlockPos(3, -2, i), ComponentType.BLOODRUNE).setUpgradeSlot());
                altarComponents.add(new AltarComponent(new BlockPos(-3, -2, i), ComponentType.BLOODRUNE).setUpgradeSlot());
                altarComponents.add(new AltarComponent(new BlockPos(i, -2, 3), ComponentType.BLOODRUNE).setUpgradeSlot());
                altarComponents.add(new AltarComponent(new BlockPos(i, -2, -3), ComponentType.BLOODRUNE).setUpgradeSlot());
            }
        }
    },
    FOUR() {
        @Override
        public void buildComponents() {
            altarComponents.addAll(THREE.getAltarComponents());

            for (int i = -3; i <= 3; i++) {
                altarComponents.add(new AltarComponent(new BlockPos(5, -3, i), ComponentType.BLOODRUNE).setUpgradeSlot());
                altarComponents.add(new AltarComponent(new BlockPos(-5, -3, i), ComponentType.BLOODRUNE).setUpgradeSlot());
                altarComponents.add(new AltarComponent(new BlockPos(i, -3, 5), ComponentType.BLOODRUNE).setUpgradeSlot());
                altarComponents.add(new AltarComponent(new BlockPos(i, -3, -5), ComponentType.BLOODRUNE).setUpgradeSlot());
            }

            for (int i = -2; i <= 1; i++) {
                altarComponents.add(new AltarComponent(new BlockPos(5, i, 5)));
                altarComponents.add(new AltarComponent(new BlockPos(5, i, -5)));
                altarComponents.add(new AltarComponent(new BlockPos(-5, i, -5)));
                altarComponents.add(new AltarComponent(new BlockPos(-5, i, 5)));
            }

            altarComponents.add(new AltarComponent(new BlockPos(5, 2, 5), ComponentType.BLOODSTONE));
            altarComponents.add(new AltarComponent(new BlockPos(5, 2, -5), ComponentType.BLOODSTONE));
            altarComponents.add(new AltarComponent(new BlockPos(-5, 2, -5), ComponentType.BLOODSTONE));
            altarComponents.add(new AltarComponent(new BlockPos(-5, 2, 5), ComponentType.BLOODSTONE));
        }
    },
    FIVE() {
        @Override
        public void buildComponents() {
            altarComponents.addAll(FOUR.getAltarComponents());
            altarComponents.add(new AltarComponent(new BlockPos(-8, -3, 8), ComponentType.BEACON));
            altarComponents.add(new AltarComponent(new BlockPos(-8, -3, -8), ComponentType.BEACON));
            altarComponents.add(new AltarComponent(new BlockPos(8, -3, -8), ComponentType.BEACON));
            altarComponents.add(new AltarComponent(new BlockPos(8, -3, 8), ComponentType.BEACON));

            for (int i = -6; i <= 6; i++) {
                altarComponents.add(new AltarComponent(new BlockPos(8, -4, i), ComponentType.BLOODRUNE).setUpgradeSlot());
                altarComponents.add(new AltarComponent(new BlockPos(-8, -4, i), ComponentType.BLOODRUNE).setUpgradeSlot());
                altarComponents.add(new AltarComponent(new BlockPos(i, -4, 8), ComponentType.BLOODRUNE).setUpgradeSlot());
                altarComponents.add(new AltarComponent(new BlockPos(i, -4, -8), ComponentType.BLOODRUNE).setUpgradeSlot());
            }
        }
    },
    SIX() {
        @Override
        public void buildComponents() {
            altarComponents.addAll(FIVE.getAltarComponents());

            for (int i = -4; i <= 2; i++) {
                altarComponents.add(new AltarComponent(new BlockPos(11, i, 11)));
                altarComponents.add(new AltarComponent(new BlockPos(-11, i, -11)));
                altarComponents.add(new AltarComponent(new BlockPos(11, i, -11)));
                altarComponents.add(new AltarComponent(new BlockPos(-11, i, 11)));
            }

            altarComponents.add(new AltarComponent(new BlockPos(11, 3, 11), ComponentType.CRYSTAL));
            altarComponents.add(new AltarComponent(new BlockPos(-11, 3, -11), ComponentType.CRYSTAL));
            altarComponents.add(new AltarComponent(new BlockPos(11, 3, -11), ComponentType.CRYSTAL));
            altarComponents.add(new AltarComponent(new BlockPos(-11, 3, 11), ComponentType.CRYSTAL));

            for (int i = -9; i <= 9; i++) {
                altarComponents.add(new AltarComponent(new BlockPos(11, -5, i), ComponentType.BLOODRUNE).setUpgradeSlot());
                altarComponents.add(new AltarComponent(new BlockPos(-11, -5, i), ComponentType.BLOODRUNE).setUpgradeSlot());
                altarComponents.add(new AltarComponent(new BlockPos(i, -5, 11), ComponentType.BLOODRUNE).setUpgradeSlot());
                altarComponents.add(new AltarComponent(new BlockPos(i, -5, -11), ComponentType.BLOODRUNE).setUpgradeSlot());
            }
        }
//@formatter:on
    };

    public static final int MAXTIERS = values().length;

    ArrayList<AltarComponent> altarComponents = new ArrayList<>();

    public void buildComponents() {

    }

    public int toInt() {
        return ordinal() + 1;
    }

    public ArrayList<AltarComponent> getAltarComponents() {
        return altarComponents;
    }
}
