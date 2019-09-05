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

@RitualRegister("ward_of_sacrosanctity")
public class RitualWardOfSacrosanctity extends Ritual {
    public static final String SPAWN_WARD = "spawnWard";

    public RitualWardOfSacrosanctity() {
        super("ritualWardOfSacrosanctity", 0, 40000, "ritual." + BloodMagic.MODID + ".wardOfSacrosanctityRitual");
        addBlockRange(SPAWN_WARD, new AreaDescriptor.Rectangle(new BlockPos(-16, -10, -16), 33));

        setMaximumVolumeAndDistanceOfRange(SPAWN_WARD, 0, 256, 256);
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

        if (GenericHandler.preventSpawnMap.containsKey(world)) {
            Map<IMasterRitualStone, AreaDescriptor> preventSpawnMap = GenericHandler.preventSpawnMap.get(world);
            if (preventSpawnMap != null) {
                preventSpawnMap.put(masterRitualStone, masterRitualStone.getBlockRange(SPAWN_WARD));
            } else {
                preventSpawnMap = new HashMap<>();
                preventSpawnMap.put(masterRitualStone, masterRitualStone.getBlockRange(SPAWN_WARD));
                GenericHandler.preventSpawnMap.put(world, preventSpawnMap);
            }
        } else {
            HashMap<IMasterRitualStone, AreaDescriptor> preventSpawnMap = new HashMap<>();
            preventSpawnMap.put(masterRitualStone, masterRitualStone.getBlockRange(SPAWN_WARD));
            GenericHandler.preventSpawnMap.put(world, preventSpawnMap);
        }

        masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost()));
    }

    @Override
    public int getRefreshTime() {
        return 25;
    }

    @Override
    public int getRefreshCost() {
        return 2;
    }

    @Override
    public void gatherComponents(Consumer<RitualComponent> components) {
        for (int i = 2; i < 5; i++) {
            if (i < 4) {
                addParallelRunes(components, 1, 0, EnumRuneType.AIR);
            }
            addCornerRunes(components, i, 0, EnumRuneType.FIRE);
        }
        addParallelRunes(components, 5, 0, EnumRuneType.DUSK);
        addOffsetRunes(components, 5, 6, 0, EnumRuneType.WATER);
    }

    @Override
    public Ritual getNewCopy() {
        return new RitualWardOfSacrosanctity();
    }
}
