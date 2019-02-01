package WayofTime.bloodmagic.ritual.types;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ritual.*;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Consumer;

@RitualRegister("water")
public class RitualWater extends Ritual {
    public static final String WATER_RANGE = "waterRange";

    public RitualWater() {
        super("ritualWater", 0, 500, "ritual." + BloodMagic.MODID + ".waterRitual");
        addBlockRange(WATER_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));
        setMaximumVolumeAndDistanceOfRange(WATER_RANGE, 9, 3, 3);
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone) {
        World world = masterRitualStone.getWorldObj();
        int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

        if (currentEssence < getRefreshCost()) {
            masterRitualStone.getOwnerNetwork().causeNausea();
            return;
        }

        int maxEffects = currentEssence / getRefreshCost();
        int totalEffects = 0;

        AreaDescriptor waterRange = masterRitualStone.getBlockRange(WATER_RANGE);

        for (BlockPos newPos : waterRange.getContainedPositions(masterRitualStone.getBlockPos())) {
            if (world.isAirBlock(newPos)) {
                world.setBlockState(newPos, Blocks.FLOWING_WATER.getDefaultState());
                totalEffects++;
            }

            if (totalEffects >= maxEffects) {
                break;
            }
        }

        masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost() * totalEffects));
    }

    @Override
    public int getRefreshTime() {
        return 1;
    }

    @Override
    public int getRefreshCost() {
        return 25;
    }

    @Override
    public void gatherComponents(Consumer<RitualComponent> components) {
        addCornerRunes(components, 1, 0, EnumRuneType.WATER);
    }

    @Override
    public Ritual getNewCopy() {
        return new RitualWater();
    }
}
