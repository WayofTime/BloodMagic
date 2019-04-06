package WayofTime.bloodmagic.ritual.types;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;
import WayofTime.bloodmagic.ritual.*;
import WayofTime.bloodmagic.soul.DemonWillHolder;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.util.handler.event.GenericHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@RitualRegister("veil_of_evil")
public class RitualVeilOfEvil extends Ritual {
    public static final String VEIL_RANGE = "veilRange";

    public RitualVeilOfEvil() {
        super("ritualVeilOfEvil", 0, 40000, "ritual." + BloodMagic.MODID + ".veilOfEvilRitual");
        addBlockRange(VEIL_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-16, 0, -16), 33));
        setMaximumVolumeAndDistanceOfRange(VEIL_RANGE, 0, 256, 256);
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

        // int maxEffects = currentEssence / getRefreshCost();
        // int totalEffects = 0;

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

        if (GenericHandler.forceSpawnMap.containsKey(world)) {
            Map<IMasterRitualStone, AreaDescriptor> forceSpawnMap = GenericHandler.forceSpawnMap.get(world);
            if (forceSpawnMap != null) {
                forceSpawnMap.put(masterRitualStone, masterRitualStone.getBlockRange(VEIL_RANGE));
            } else {
                forceSpawnMap = new HashMap<>();
                forceSpawnMap.put(masterRitualStone, masterRitualStone.getBlockRange(VEIL_RANGE));
                GenericHandler.forceSpawnMap.put(world, forceSpawnMap);
            }
        } else {
            HashMap<IMasterRitualStone, AreaDescriptor> forceSpawnMap = new HashMap<>();
            forceSpawnMap.put(masterRitualStone, masterRitualStone.getBlockRange(VEIL_RANGE));
            GenericHandler.forceSpawnMap.put(world, forceSpawnMap);
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
