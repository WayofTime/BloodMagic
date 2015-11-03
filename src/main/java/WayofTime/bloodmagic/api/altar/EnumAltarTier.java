package WayofTime.bloodmagic.api.altar;

import WayofTime.bloodmagic.api.BlockStack;
import WayofTime.bloodmagic.registry.ModBlocks;
import lombok.Getter;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;

@Getter
public enum EnumAltarTier {
    ONE(),
    TWO() {
        @Override
        public void buildComponents() {
            altarComponents.add(new AltarComponent(new BlockPos(-1, -1, -1), ModBlocks.blood_rune).setBloodRune());
            altarComponents.add(new AltarComponent(new BlockPos(0, -1, -1), ModBlocks.blood_rune).setUpgradeSlot().setBloodRune());
            altarComponents.add(new AltarComponent(new BlockPos(1, -1, -1), ModBlocks.blood_rune).setBloodRune());
            altarComponents.add(new AltarComponent(new BlockPos(-1, -1, 0), ModBlocks.blood_rune).setUpgradeSlot().setBloodRune());
            altarComponents.add(new AltarComponent(new BlockPos(1, -1, 0), ModBlocks.blood_rune).setUpgradeSlot().setBloodRune());
            altarComponents.add(new AltarComponent(new BlockPos(-1, -1, 1), ModBlocks.blood_rune).setBloodRune());
            altarComponents.add(new AltarComponent(new BlockPos(0, -1, 1), ModBlocks.blood_rune).setUpgradeSlot().setBloodRune());
            altarComponents.add(new AltarComponent(new BlockPos(1, -1, 1), ModBlocks.blood_rune).setBloodRune());
        }
    },
    THREE() {
        @Override
        public void buildComponents() {
            altarComponents.addAll(TWO.getAltarComponents());
            altarComponents.add(new AltarComponent(new BlockPos(-1, -1, -1), ModBlocks.blood_rune).setUpgradeSlot().setBloodRune());
            altarComponents.add(new AltarComponent(new BlockPos(0, -1, -1), ModBlocks.blood_rune).setUpgradeSlot().setBloodRune());
            altarComponents.add(new AltarComponent(new BlockPos(1, -1, -1), ModBlocks.blood_rune).setUpgradeSlot().setBloodRune());
            altarComponents.add(new AltarComponent(new BlockPos(-1, -1, 0), ModBlocks.blood_rune).setUpgradeSlot().setBloodRune());
            altarComponents.add(new AltarComponent(new BlockPos(1, -1, 0), ModBlocks.blood_rune).setUpgradeSlot().setBloodRune());
            altarComponents.add(new AltarComponent(new BlockPos(-1, -1, 1), ModBlocks.blood_rune).setUpgradeSlot().setBloodRune());
            altarComponents.add(new AltarComponent(new BlockPos(0, -1, 1), ModBlocks.blood_rune).setUpgradeSlot().setBloodRune());
            altarComponents.add(new AltarComponent(new BlockPos(1, -1, 1), ModBlocks.blood_rune).setUpgradeSlot().setBloodRune());
            altarComponents.add(new AltarComponent(new BlockPos(-3, -1, -3)));
            altarComponents.add(new AltarComponent(new BlockPos(-3, 0, -3)));
            altarComponents.add(new AltarComponent(new BlockPos(3, -1, -3)));
            altarComponents.add(new AltarComponent(new BlockPos(3, 0, -3)));
            altarComponents.add(new AltarComponent(new BlockPos(-3, -1, 3)));
            altarComponents.add(new AltarComponent(new BlockPos(-3, 0, 3)));
            altarComponents.add(new AltarComponent(new BlockPos(3, -1, 3)));
            altarComponents.add(new AltarComponent(new BlockPos(3, 0, 3)));
            altarComponents.add(new AltarComponent(new BlockPos(-3, 1, -3), Blocks.glowstone));
            altarComponents.add(new AltarComponent(new BlockPos(3, 1, -3), Blocks.glowstone));
            altarComponents.add(new AltarComponent(new BlockPos(-3, 1, 3), Blocks.glowstone));
            altarComponents.add(new AltarComponent(new BlockPos(3, 1, 3), Blocks.glowstone));

            for (int i = -2; i <= 2; i++) {
                altarComponents.add(new AltarComponent(new BlockPos(3, -2, i), ModBlocks.blood_rune).setUpgradeSlot().setBloodRune());
                altarComponents.add(new AltarComponent(new BlockPos(-3, -2, i), ModBlocks.blood_rune).setUpgradeSlot().setBloodRune());
                altarComponents.add(new AltarComponent(new BlockPos(i, -2, 3), ModBlocks.blood_rune).setUpgradeSlot().setBloodRune());
                altarComponents.add(new AltarComponent(new BlockPos(i, -2, -3), ModBlocks.blood_rune).setUpgradeSlot().setBloodRune());
            }
        }
    },
    FOUR() {
        @Override
        public void buildComponents() {
            altarComponents.addAll(THREE.getAltarComponents());

            for (int i = -3; i <= 3; i++) {
                altarComponents.add(new AltarComponent(new BlockPos(5, -3, i), ModBlocks.blood_rune).setUpgradeSlot().setBloodRune());
                altarComponents.add(new AltarComponent(new BlockPos(-5, -3, i), ModBlocks.blood_rune).setUpgradeSlot().setBloodRune());
                altarComponents.add(new AltarComponent(new BlockPos(i, -3, 5), ModBlocks.blood_rune).setUpgradeSlot().setBloodRune());
                altarComponents.add(new AltarComponent(new BlockPos(i, -3, -5), ModBlocks.blood_rune).setUpgradeSlot().setBloodRune());
            }

            for (int i = -2; i <= 1; i++) {
                altarComponents.add(new AltarComponent(new BlockPos(5, i, 5)));
                altarComponents.add(new AltarComponent(new BlockPos(5, i, -5)));
                altarComponents.add(new AltarComponent(new BlockPos(-5, i, -5)));
                altarComponents.add(new AltarComponent(new BlockPos(-5, i, 5)));
            }

            altarComponents.add(new AltarComponent(new BlockPos(5, 2, 5), new BlockStack(ModBlocks.bloodStone, 1)));
            altarComponents.add(new AltarComponent(new BlockPos(5, 2, -5), new BlockStack(ModBlocks.bloodStone, 1)));
            altarComponents.add(new AltarComponent(new BlockPos(-5, 2, -5), new BlockStack(ModBlocks.bloodStone, 1)));
            altarComponents.add(new AltarComponent(new BlockPos(-5, 2, 5), new BlockStack(ModBlocks.bloodStone, 1)));
        }
    },
    FIVE() {
        @Override
        public void buildComponents() {
            altarComponents.addAll(FOUR.getAltarComponents());
            altarComponents.add(new AltarComponent(new BlockPos(-8, -3, 8), Blocks.beacon));
            altarComponents.add(new AltarComponent(new BlockPos(-8, -3, -8), Blocks.beacon));
            altarComponents.add(new AltarComponent(new BlockPos(8, -3, -8), Blocks.beacon));
            altarComponents.add(new AltarComponent(new BlockPos(8, -3, 8), Blocks.beacon));

            for (int i = -6; i <= 6; i++) {
                altarComponents.add(new AltarComponent(new BlockPos(8, -4, i), ModBlocks.blood_rune).setUpgradeSlot().setBloodRune());
                altarComponents.add(new AltarComponent(new BlockPos(-8, -4, i), ModBlocks.blood_rune).setUpgradeSlot().setBloodRune());
                altarComponents.add(new AltarComponent(new BlockPos(i, -4, 8), ModBlocks.blood_rune).setUpgradeSlot().setBloodRune());
                altarComponents.add(new AltarComponent(new BlockPos(i, -4, -8), ModBlocks.blood_rune).setUpgradeSlot().setBloodRune());
            }
        }
    },
    SIX() {
        @Override
        public void buildComponents() {
            altarComponents.addAll(FIVE.getAltarComponents());

            for(int i = -4; i <= 2; i++) {
                altarComponents.add(new AltarComponent(new BlockPos(11, i, 11)));
                altarComponents.add(new AltarComponent(new BlockPos(-11, i, -11)));
                altarComponents.add(new AltarComponent(new BlockPos(11, i, -11)));
                altarComponents.add(new AltarComponent(new BlockPos(-11, i, 11)));
            }

            altarComponents.add(new AltarComponent(new BlockPos(11, 3, 11), ModBlocks.crystal));
            altarComponents.add(new AltarComponent(new BlockPos(-11, 3, -11), ModBlocks.crystal));
            altarComponents.add(new AltarComponent(new BlockPos(11, 3, -11), ModBlocks.crystal));
            altarComponents.add(new AltarComponent(new BlockPos(-11, 3, 11), ModBlocks.crystal));

            for (int i = -9; i <= 9; i++) {
                altarComponents.add(new AltarComponent(new BlockPos(11, -5, i), ModBlocks.blood_rune).setUpgradeSlot().setBloodRune());
                altarComponents.add(new AltarComponent(new BlockPos(-11, -5, i), ModBlocks.blood_rune).setUpgradeSlot().setBloodRune());
                altarComponents.add(new AltarComponent(new BlockPos(i, -5, 11), ModBlocks.blood_rune).setUpgradeSlot().setBloodRune());
                altarComponents.add(new AltarComponent(new BlockPos(i, -5, -11), ModBlocks.blood_rune).setUpgradeSlot().setBloodRune());
            }
        }
    };

    public static final int MAXTIERS = values().length;

    ArrayList<AltarComponent> altarComponents = new ArrayList<AltarComponent>();

    public void buildComponents() {

    }
}
