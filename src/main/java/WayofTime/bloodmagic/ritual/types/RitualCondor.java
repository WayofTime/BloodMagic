package WayofTime.bloodmagic.ritual.types;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.core.RegistrarBloodMagic;
import WayofTime.bloodmagic.ritual.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Consumer;

@RitualRegister("condor")
public class RitualCondor extends Ritual {
    public static final String FLIGHT_RANGE = "flightRange";

    public RitualCondor() {
        super("ritualCondor", 0, 1000000, "ritual." + BloodMagic.MODID + ".condorRitual");
        addBlockRange(FLIGHT_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-10, 0, -10), new BlockPos(10, 30, 10)));
        setMaximumVolumeAndDistanceOfRange(FLIGHT_RANGE, 0, 100, 200);
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone) {
        AxisAlignedBB aabb = masterRitualStone.getBlockRange(FLIGHT_RANGE).getAABB(masterRitualStone.getBlockPos());
        World world = masterRitualStone.getWorldObj();

        int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

        List<EntityPlayer> entityPlayers = world.getEntitiesWithinAABB(EntityPlayer.class, aabb);
        int entityCount = entityPlayers.size();

        if (currentEssence < getRefreshCost() * entityCount) {
            masterRitualStone.getOwnerNetwork().causeNausea();
            return;
        } else {
            entityCount = 0;
            for (EntityPlayer player : entityPlayers) {
                player.addPotionEffect(new PotionEffect(RegistrarBloodMagic.FLIGHT, 20, 0));
            }
        }

        masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost() * entityCount));
    }

    @Override
    public int getRefreshTime() {
        return 10;
    }

    @Override
    public int getRefreshCost() {
        return 5;
    }

    @Override
    public void gatherComponents(Consumer<RitualComponent> components) {

        addParallelRunes(components, 1, 0, EnumRuneType.DUSK);
        addCornerRunes(components, 2, 0, EnumRuneType.AIR);
        addOffsetRunes(components, 1, 3, 0, EnumRuneType.EARTH);
        addParallelRunes(components, 3, 0, EnumRuneType.EARTH);
        addOffsetRunes(components, 3, 4, 0, EnumRuneType.WATER);
        addParallelRunes(components, 1, 1, EnumRuneType.FIRE);
        addParallelRunes(components, 2, 1, EnumRuneType.BLANK);
        addParallelRunes(components, 4, 1, EnumRuneType.BLANK);
        addParallelRunes(components, 5, 1, EnumRuneType.AIR);
        addParallelRunes(components, 5, 0, EnumRuneType.DUSK);

        for (int i = 2; i <= 4; i++) {
            addParallelRunes(components, i, 2, EnumRuneType.EARTH);
        }

        addOffsetRunes(components, 2, 1, 4, EnumRuneType.FIRE);
        addCornerRunes(components, 2, 4, EnumRuneType.AIR);
        addCornerRunes(components, 4, 2, EnumRuneType.FIRE);

        for (int i = -1; i <= 1; i++) {
            addOffsetRunes(components, 3, i, 4, EnumRuneType.EARTH);
        }
    }

    @Override
    public Ritual getNewCopy() {
        return new RitualCondor();
    }
}
