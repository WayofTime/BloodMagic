package WayofTime.bloodmagic.ritual.types;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ritual.*;
import WayofTime.bloodmagic.tile.TileDemonCrystal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Consumer;

@RitualRegister("crystal_harvest")
public class RitualCrystalHarvest extends Ritual {
    public static final String CRYSTAL_RANGE = "crystal";

    public RitualCrystalHarvest() {
        super("ritualCrystalHarvest", 0, 40000, "ritual." + BloodMagic.MODID + ".crystalHarvestRitual");
        addBlockRange(CRYSTAL_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-3, 2, -3), 7, 5, 7));

        setMaximumVolumeAndDistanceOfRange(CRYSTAL_RANGE, 250, 5, 7);
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone) {
        World world = masterRitualStone.getWorldObj();
        int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();
        BlockPos pos = masterRitualStone.getBlockPos();

        if (currentEssence < getRefreshCost()) {
            masterRitualStone.getOwnerNetwork().causeNausea();
            return;
        }

        int maxEffects = 1;
        int totalEffects = 0;

        AreaDescriptor crystalRange = masterRitualStone.getBlockRange(CRYSTAL_RANGE);

        crystalRange.resetIterator();
        while (crystalRange.hasNext()) {
            BlockPos nextPos = crystalRange.next().add(pos);
            TileEntity tile = world.getTileEntity(nextPos);
            if (tile instanceof TileDemonCrystal) {
                TileDemonCrystal demonCrystal = (TileDemonCrystal) tile;
                if (demonCrystal.dropSingleCrystal()) {
                    IBlockState state = world.getBlockState(nextPos);
                    world.notifyBlockUpdate(nextPos, state, state, 3);
                    totalEffects++;
                    if (totalEffects >= maxEffects) {
                        break;
                    }
                }
            }
        }

        masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost() * totalEffects));
    }

    @Override
    public int getRefreshTime() {
        return 25;
    }

    @Override
    public int getRefreshCost() {
        return 50;
    }

    @Override
    public void gatherComponents(Consumer<RitualComponent> components) {
        addCornerRunes(components, 1, 0, EnumRuneType.AIR);
        addParallelRunes(components, 1, 1, EnumRuneType.DUSK);
        addParallelRunes(components, 1, -1, EnumRuneType.FIRE);
        addParallelRunes(components, 2, -1, EnumRuneType.FIRE);
        addParallelRunes(components, 3, -1, EnumRuneType.FIRE);
        addOffsetRunes(components, 3, 1, -1, EnumRuneType.FIRE);
        addCornerRunes(components, 3, -1, EnumRuneType.EARTH);
        addCornerRunes(components, 3, 0, EnumRuneType.EARTH);
        addOffsetRunes(components, 3, 2, 0, EnumRuneType.DUSK);
    }

    @Override
    public Ritual getNewCopy() {
        return new RitualCrystalHarvest();
    }
}
