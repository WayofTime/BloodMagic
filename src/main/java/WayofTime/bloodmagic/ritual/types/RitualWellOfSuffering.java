package WayofTime.bloodmagic.ritual.types;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import WayofTime.bloodmagic.ritual.*;
import WayofTime.bloodmagic.tile.TileAltar;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.util.List;
import java.util.function.Consumer;

@RitualRegister("well_of_suffering")
public class RitualWellOfSuffering extends Ritual {
    public static final String ALTAR_RANGE = "altar";
    public static final String DAMAGE_RANGE = "damage";

    public static final int SACRIFICE_AMOUNT = 25;

    public BlockPos altarOffsetPos = new BlockPos(0, 0, 0); //TODO: Save!

    public RitualWellOfSuffering() {
        super("ritualWellOfSuffering", 0, 40000, "ritual." + BloodMagic.MODID + ".wellOfSufferingRitual");
        addBlockRange(ALTAR_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-5, -10, -5), 11, 21, 11));
        addBlockRange(DAMAGE_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-10, -10, -10), 21));

        setMaximumVolumeAndDistanceOfRange(ALTAR_RANGE, 0, 10, 15);
        setMaximumVolumeAndDistanceOfRange(DAMAGE_RANGE, 0, 15, 15);
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone) {
        World world = masterRitualStone.getWorldObj();
        int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

        if (currentEssence < getRefreshCost()) {
            masterRitualStone.getOwnerNetwork().causeNausea();
            return;
        }

        BlockPos pos = masterRitualStone.getBlockPos();

        int maxEffects = currentEssence / getRefreshCost();
        int totalEffects = 0;

        BlockPos altarPos = pos.add(altarOffsetPos);

        TileEntity tile = world.getTileEntity(altarPos);

        AreaDescriptor altarRange = masterRitualStone.getBlockRange(ALTAR_RANGE);

        if (!altarRange.isWithinArea(altarOffsetPos) || !(tile instanceof TileAltar)) {
            for (BlockPos newPos : altarRange.getContainedPositions(pos)) {
                TileEntity nextTile = world.getTileEntity(newPos);
                if (nextTile instanceof TileAltar) {
                    tile = nextTile;
                    altarOffsetPos = newPos.subtract(pos);

                    altarRange.resetCache();
                    break;
                }
            }
        }

        if (tile instanceof TileAltar) {
            TileAltar tileAltar = (TileAltar) tile;

            AreaDescriptor damageRange = masterRitualStone.getBlockRange(DAMAGE_RANGE);
            AxisAlignedBB range = damageRange.getAABB(pos);

            List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, range);

            for (EntityLivingBase entity : entities) {
                EntityEntry entityEntry = EntityRegistry.getEntry(entity.getClass());

                if (entityEntry == null || BloodMagicAPI.INSTANCE.getBlacklist().getSacrifice().contains(entityEntry.getRegistryName()))
                    continue;

                int lifeEssenceRatio = BloodMagicAPI.INSTANCE.getValueManager().getSacrificial().getOrDefault(entityEntry.getRegistryName(), SACRIFICE_AMOUNT);

                if (lifeEssenceRatio <= 0)
                    continue;

                if (entity.isEntityAlive() && !(entity instanceof EntityPlayer)) {
                    if (entity.attackEntityFrom(RitualManager.RITUAL_DAMAGE, 1)) {
                        if (entity.isChild())
                            lifeEssenceRatio *= 0.5F;

                        tileAltar.sacrificialDaggerCall(lifeEssenceRatio, true);

                        totalEffects++;

                        if (totalEffects >= maxEffects) {
                            break;
                        }
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
        return 2;
    }

    @Override
    public void gatherComponents(Consumer<RitualComponent> components) {
        addCornerRunes(components, 1, 0, EnumRuneType.FIRE);
        addCornerRunes(components, 2, -1, EnumRuneType.FIRE);
        addParallelRunes(components, 2, -1, EnumRuneType.EARTH);
        addCornerRunes(components, -3, -1, EnumRuneType.DUSK);
        addOffsetRunes(components, 2, 4, -1, EnumRuneType.WATER);
        addOffsetRunes(components, 1, 4, 0, EnumRuneType.WATER);
        addParallelRunes(components, 4, 1, EnumRuneType.AIR);
    }

    @Override
    public Ritual getNewCopy() {
        return new RitualWellOfSuffering();
    }
}
