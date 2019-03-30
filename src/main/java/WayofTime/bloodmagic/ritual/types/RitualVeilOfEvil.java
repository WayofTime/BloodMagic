package WayofTime.bloodmagic.ritual.types;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;
import WayofTime.bloodmagic.ritual.*;
import WayofTime.bloodmagic.soul.DemonWillHolder;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Consumer;

@RitualRegister("veilofevil")
public class RitualVeilOfEvil extends Ritual {
    public static final String VEIL_RANGE = "veilRange";

    public RitualVeilOfEvil() {
        super("ritualVeilOfEvil", 0, 40000, "ritual." + BloodMagic.MODID + ".veiLOfEvilRitual");
        addBlockRange(VEIL_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-16, 0, -16), 32));
        setMaximumVolumeAndDistanceOfRange(VEIL_RANGE, 2500, 50, 50);
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone) {
        /* Default Ritual Stuff */
        World world = masterRitualStone.getWorldObj();
        int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();
        BlockPos pos = masterRitualStone.getBlockPos();

        if (currentEssence < getRefreshCost()) {
            masterRitualStone.getOwnerNetwork().causeNausea();
            return;
        }

        int maxEffects = currentEssence / getRefreshCost();
        int totalEffects = 0;

        /* Default will augment stuff */
        List<EnumDemonWillType> willConfig = masterRitualStone.getActiveWillConfig();
        DemonWillHolder holder = WorldDemonWillHandler.getWillHolder(world, pos);

        double rawWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.DEFAULT, willConfig);
        double corrosiveWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.CORROSIVE, willConfig);
        double destructiveWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.DESTRUCTIVE, willConfig);
        double steadfastWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.STEADFAST, willConfig);
        double vengefulWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.VENGEFUL, willConfig);

        double rawDrained = 0;
        double corrosiveDrained = 0;
        double destructiveDrained = 0;
        double steadfastDrained = 0;
        double vengefulDrained = 0;

        /* Actual ritual stuff begins here */

        int dimension = world.provider.getDimension();

        if (AlchemicalWizardryEventHooks.forceSpawnMap.containsKey(new Integer(dimension))) {
            List<CoordAndRange> list = AlchemicalWizardryEventHooks.forceSpawnMap.get(new Integer(dimension));
            if (list != null) {
                if (!list.contains(new CoordAndRange(x, y, z, horizRange, vertRange))) {
                    boolean hasFoundAndRemoved = false;
                    for (CoordAndRange coords : list) {
                        int xLocation = coords.xCoord;
                        int yLocation = coords.yCoord;
                        int zLocation = coords.zCoord;

                        if (xLocation == x && yLocation == y && zLocation == z) {
                            list.remove(coords);
                            hasFoundAndRemoved = true;
                            break;
                        }
                    }
                    list.add(new CoordAndRange(x, y, z, horizRange, vertRange));
                }
            } else {
                list = new LinkedList();
                list.add(new CoordAndRange(x, y, z, horizRange, vertRange));
                AlchemicalWizardryEventHooks.forceSpawnMap.put(new Integer(dimension), list);
            }
        } else {
            List<CoordAndRange> list = new LinkedList();
            list.add(new CoordAndRange(x, y, z, horizRange, vertRange));
            AlchemicalWizardryEventHooks.forceSpawnMap.put(new Integer(dimension), list);
        }


        masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost()));

    }

    @Override
    public int getRefreshCost() {
        return 0;
    }

    @Override
    public void gatherComponents(Consumer<RitualComponent> components) {

        addOffsetRunes(components, 1, 0, 2, EnumRuneType.DUSK);
        addCornerRunes(components, 3, 0, EnumRuneType.FIRE);

        for (int i = 0; i <= 1; i++) {
            addParallelRunes(components, (4 + i), i, EnumRuneType.DUSK);
            addOffsetRunes(components, (4 + i), i, -1, EnumRuneType.BLANK);
            addOffsetRunes(components, 4, 5, i, EnumRuneType.EARTH);
        }

        addCornerRunes(components, 5, 1, EnumRuneType.BLANK);
    }

    @Override
    public Ritual getNewCopy() {
        return new RitualVeilOfEvil();
    }
}
