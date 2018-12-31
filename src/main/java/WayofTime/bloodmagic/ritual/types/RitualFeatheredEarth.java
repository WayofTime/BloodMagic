package WayofTime.bloodmagic.ritual.types;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ritual.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Consumer;

@RitualRegister("feathered_earth")
public class RitualFeatheredEarth extends Ritual {
    public static final String FALL_PROTECTION_RANGE = "fallProtRange";

    public RitualFeatheredEarth() {
        super("ritualFeatheredEarth", 0, 5000, "ritual." + BloodMagic.MODID + ".featheredEarthRitual");
        addBlockRange(FALL_PROTECTION_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 0, 0), 25, 30, 25));
        setMaximumVolumeAndDistanceOfRange(FALL_PROTECTION_RANGE, 0, 5, 5);
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone) {
        World world = masterRitualStone.getWorldObj();
        int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();
        BlockPos pos = masterRitualStone.getBlockPos();
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();

        if (currentEssence < getRefreshCost()) {
            masterRitualStone.getOwnerNetwork().causeNausea();
            return;
        }

        int maxEffects = currentEssence / getRefreshCost();
        int totalEffects = 0;

        if (masterRitualStone.getCooldown() > 0) {
            world.addWeatherEffect(new EntityLightningBolt(world, x + 4, y + 5, z + 4, false));
            world.addWeatherEffect(new EntityLightningBolt(world, x + 4, y + 5, z - 4, false));
            world.addWeatherEffect(new EntityLightningBolt(world, x - 4, y + 5, z - 4, false));
            world.addWeatherEffect(new EntityLightningBolt(world, x - 4, y + 5, z + 4, false));
            masterRitualStone.setCooldown(0);
        }

        AreaDescriptor fallProtRange = getBlockRange(FALL_PROTECTION_RANGE);
        List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, fallProtRange.getAABB(masterRitualStone.getBlockPos()).expand(-25, 0, -25));
        for (EntityLivingBase entity : entities) {
            if (totalEffects >= maxEffects) {
                break;
            }

            entity.fallDistance = 0;
            totalEffects++;
        }

        masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost() * totalEffects));
    }

    @Override
    public int getRefreshTime() {
        return 1;
    }

    @Override
    public int getRefreshCost() {
        return 1;
    }

    @Override
    public void gatherComponents(Consumer<RitualComponent> components) {
        addParallelRunes(components, 1, 0, EnumRuneType.DUSK);
        addCornerRunes(components, 2, 0, EnumRuneType.AIR);
        addOffsetRunes(components, 1, 3, 0, EnumRuneType.EARTH);
        addParallelRunes(components, 3, 0, EnumRuneType.EARTH);
        addCornerRunes(components, 4, 4, EnumRuneType.FIRE);
        addOffsetRunes(components, 4, 5, 5, EnumRuneType.AIR);
        addOffsetRunes(components, 3, 4, 5, EnumRuneType.AIR);
    }


    @Override
    public Ritual getNewCopy() {
        return new RitualFeatheredEarth();
    }
}
