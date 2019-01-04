package WayofTime.bloodmagic.ritual.types;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ritual.*;
import net.minecraft.util.math.BlockPos;

import java.util.function.Consumer;

@RitualRegister("veilofevil")
public class RitualVeilOfEvil extends Ritual {
    public static final String VEIL_RANGE = "veilRange";

    public RitualVeilOfEvil() {
        super("ritualVeilOfEvil", 0, 40000, "ritual." + BloodMagic.MODID + ".veiLOfEvilRitual");
        addBlockRange(VEIL_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-3, 2, -3), 7, 5, 7));

        setMaximumVolumeAndDistanceOfRange(VEIL_RANGE, 250, 5, 7);
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone) {

    }

    @Override
    public int getRefreshCost() {
        return 0;
    }

    @Override
    public void gatherComponents(Consumer<RitualComponent> components) {

    }

    @Override
    public Ritual getNewCopy() {
        return new RitualVeilOfEvil();
    }
}
