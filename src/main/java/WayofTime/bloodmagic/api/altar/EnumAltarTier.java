package WayofTime.bloodmagic.api.altar;

import java.util.ArrayList;

import lombok.Getter;
import net.minecraft.util.BlockPos;

//@formatter:off
@Getter
public enum EnumAltarTier
{
    ONE(), TWO()
    {
        @Override
        public void buildComponents()
        {
            altarComponents.add(new AltarComponent(new BlockPos(-1, -1, -1), EnumAltarComponent.BLOODRUNE));
            altarComponents.add(new AltarComponent(new BlockPos(0, -1, -1), EnumAltarComponent.BLOODRUNE).setUpgradeSlot());
            altarComponents.add(new AltarComponent(new BlockPos(1, -1, -1), EnumAltarComponent.BLOODRUNE));
            altarComponents.add(new AltarComponent(new BlockPos(-1, -1, 0), EnumAltarComponent.BLOODRUNE).setUpgradeSlot());
            altarComponents.add(new AltarComponent(new BlockPos(1, -1, 0), EnumAltarComponent.BLOODRUNE).setUpgradeSlot());
            altarComponents.add(new AltarComponent(new BlockPos(-1, -1, 1), EnumAltarComponent.BLOODRUNE));
            altarComponents.add(new AltarComponent(new BlockPos(0, -1, 1), EnumAltarComponent.BLOODRUNE).setUpgradeSlot());
            altarComponents.add(new AltarComponent(new BlockPos(1, -1, 1), EnumAltarComponent.BLOODRUNE));
        }
    },
    THREE()
    {
        @Override
        public void buildComponents()
        {
            altarComponents.add(new AltarComponent(new BlockPos(-1, -1, -1), EnumAltarComponent.BLOODRUNE).setUpgradeSlot());
            altarComponents.add(new AltarComponent(new BlockPos(0, -1, -1), EnumAltarComponent.BLOODRUNE).setUpgradeSlot());
            altarComponents.add(new AltarComponent(new BlockPos(1, -1, -1), EnumAltarComponent.BLOODRUNE).setUpgradeSlot());
            altarComponents.add(new AltarComponent(new BlockPos(-1, -1, 0), EnumAltarComponent.BLOODRUNE).setUpgradeSlot());
            altarComponents.add(new AltarComponent(new BlockPos(1, -1, 0), EnumAltarComponent.BLOODRUNE).setUpgradeSlot());
            altarComponents.add(new AltarComponent(new BlockPos(-1, -1, 1), EnumAltarComponent.BLOODRUNE).setUpgradeSlot());
            altarComponents.add(new AltarComponent(new BlockPos(0, -1, 1), EnumAltarComponent.BLOODRUNE).setUpgradeSlot());
            altarComponents.add(new AltarComponent(new BlockPos(1, -1, 1), EnumAltarComponent.BLOODRUNE).setUpgradeSlot());
            altarComponents.add(new AltarComponent(new BlockPos(-3, -1, -3)));
            altarComponents.add(new AltarComponent(new BlockPos(-3, 0, -3)));
            altarComponents.add(new AltarComponent(new BlockPos(3, -1, -3)));
            altarComponents.add(new AltarComponent(new BlockPos(3, 0, -3)));
            altarComponents.add(new AltarComponent(new BlockPos(-3, -1, 3)));
            altarComponents.add(new AltarComponent(new BlockPos(-3, 0, 3)));
            altarComponents.add(new AltarComponent(new BlockPos(3, -1, 3)));
            altarComponents.add(new AltarComponent(new BlockPos(3, 0, 3)));
            altarComponents.add(new AltarComponent(new BlockPos(-3, 1, -3), EnumAltarComponent.GLOWSTONE));
            altarComponents.add(new AltarComponent(new BlockPos(3, 1, -3), EnumAltarComponent.GLOWSTONE));
            altarComponents.add(new AltarComponent(new BlockPos(-3, 1, 3), EnumAltarComponent.GLOWSTONE));
            altarComponents.add(new AltarComponent(new BlockPos(3, 1, 3), EnumAltarComponent.GLOWSTONE));

            for (int i = -2; i <= 2; i++)
            {
                altarComponents.add(new AltarComponent(new BlockPos(3, -2, i), EnumAltarComponent.BLOODRUNE).setUpgradeSlot());
                altarComponents.add(new AltarComponent(new BlockPos(-3, -2, i), EnumAltarComponent.BLOODRUNE).setUpgradeSlot());
                altarComponents.add(new AltarComponent(new BlockPos(i, -2, 3), EnumAltarComponent.BLOODRUNE).setUpgradeSlot());
                altarComponents.add(new AltarComponent(new BlockPos(i, -2, -3), EnumAltarComponent.BLOODRUNE).setUpgradeSlot());
            }
        }
    },
    FOUR()
    {
        @Override
        public void buildComponents()
        {
            altarComponents.addAll(THREE.getAltarComponents());

            for (int i = -3; i <= 3; i++)
            {
                altarComponents.add(new AltarComponent(new BlockPos(5, -3, i), EnumAltarComponent.BLOODRUNE).setUpgradeSlot());
                altarComponents.add(new AltarComponent(new BlockPos(-5, -3, i), EnumAltarComponent.BLOODRUNE).setUpgradeSlot());
                altarComponents.add(new AltarComponent(new BlockPos(i, -3, 5), EnumAltarComponent.BLOODRUNE).setUpgradeSlot());
                altarComponents.add(new AltarComponent(new BlockPos(i, -3, -5), EnumAltarComponent.BLOODRUNE).setUpgradeSlot());
            }

            for (int i = -2; i <= 1; i++)
            {
                altarComponents.add(new AltarComponent(new BlockPos(5, i, 5)));
                altarComponents.add(new AltarComponent(new BlockPos(5, i, -5)));
                altarComponents.add(new AltarComponent(new BlockPos(-5, i, -5)));
                altarComponents.add(new AltarComponent(new BlockPos(-5, i, 5)));
            }

            altarComponents.add(new AltarComponent(new BlockPos(5, 2, 5), EnumAltarComponent.BLOODSTONE));
            altarComponents.add(new AltarComponent(new BlockPos(5, 2, -5), EnumAltarComponent.BLOODSTONE));
            altarComponents.add(new AltarComponent(new BlockPos(-5, 2, -5), EnumAltarComponent.BLOODSTONE));
            altarComponents.add(new AltarComponent(new BlockPos(-5, 2, 5), EnumAltarComponent.BLOODSTONE));
        }
    },
    FIVE()
    {
        @Override
        public void buildComponents()
        {
            altarComponents.addAll(FOUR.getAltarComponents());
            altarComponents.add(new AltarComponent(new BlockPos(-8, -3, 8), EnumAltarComponent.BEACON));
            altarComponents.add(new AltarComponent(new BlockPos(-8, -3, -8), EnumAltarComponent.BEACON));
            altarComponents.add(new AltarComponent(new BlockPos(8, -3, -8), EnumAltarComponent.BEACON));
            altarComponents.add(new AltarComponent(new BlockPos(8, -3, 8), EnumAltarComponent.BEACON));

            for (int i = -6; i <= 6; i++)
            {
                altarComponents.add(new AltarComponent(new BlockPos(8, -4, i), EnumAltarComponent.BLOODRUNE).setUpgradeSlot());
                altarComponents.add(new AltarComponent(new BlockPos(-8, -4, i), EnumAltarComponent.BLOODRUNE).setUpgradeSlot());
                altarComponents.add(new AltarComponent(new BlockPos(i, -4, 8), EnumAltarComponent.BLOODRUNE).setUpgradeSlot());
                altarComponents.add(new AltarComponent(new BlockPos(i, -4, -8), EnumAltarComponent.BLOODRUNE).setUpgradeSlot());
            }
        }
    },
    SIX()
    {
        @Override
        public void buildComponents()
        {
            altarComponents.addAll(FIVE.getAltarComponents());

            for (int i = -4; i <= 2; i++)
            {
                altarComponents.add(new AltarComponent(new BlockPos(11, i, 11)));
                altarComponents.add(new AltarComponent(new BlockPos(-11, i, -11)));
                altarComponents.add(new AltarComponent(new BlockPos(11, i, -11)));
                altarComponents.add(new AltarComponent(new BlockPos(-11, i, 11)));
            }

            altarComponents.add(new AltarComponent(new BlockPos(11, 3, 11), EnumAltarComponent.CRYSTAL));
            altarComponents.add(new AltarComponent(new BlockPos(-11, 3, -11), EnumAltarComponent.CRYSTAL));
            altarComponents.add(new AltarComponent(new BlockPos(11, 3, -11), EnumAltarComponent.CRYSTAL));
            altarComponents.add(new AltarComponent(new BlockPos(-11, 3, 11), EnumAltarComponent.CRYSTAL));

            for (int i = -9; i <= 9; i++)
            {
                altarComponents.add(new AltarComponent(new BlockPos(11, -5, i), EnumAltarComponent.BLOODRUNE).setUpgradeSlot());
                altarComponents.add(new AltarComponent(new BlockPos(-11, -5, i), EnumAltarComponent.BLOODRUNE).setUpgradeSlot());
                altarComponents.add(new AltarComponent(new BlockPos(i, -5, 11), EnumAltarComponent.BLOODRUNE).setUpgradeSlot());
                altarComponents.add(new AltarComponent(new BlockPos(i, -5, -11), EnumAltarComponent.BLOODRUNE).setUpgradeSlot());
            }
        }
//@formatter:on
    };

    public static final int MAXTIERS = values().length;

    ArrayList<AltarComponent> altarComponents = new ArrayList<AltarComponent>();

    public void buildComponents()
    {

    }

    public int toInt()
    {
        return ordinal() + 1;
    }
}
