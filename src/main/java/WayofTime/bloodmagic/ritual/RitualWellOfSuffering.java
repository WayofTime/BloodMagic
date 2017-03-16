package WayofTime.bloodmagic.ritual;

import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.ritual.*;
import WayofTime.bloodmagic.tile.TileAltar;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class RitualWellOfSuffering extends Ritual
{
    public static final String ALTAR_RANGE = "altar";
    public static final String DAMAGE_RANGE = "damage";

    public static final int SACRIFICE_AMOUNT = 25;

    public BlockPos altarOffsetPos = new BlockPos(0, 0, 0); //TODO: Save!

    public RitualWellOfSuffering()
    {
        super("ritualWellOfSuffering", 0, 40000, "ritual." + Constants.Mod.MODID + ".wellOfSufferingRitual");
        addBlockRange(ALTAR_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-5, -10, -5), 11, 21, 11));
        addBlockRange(DAMAGE_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-10, -10, -10), 21));

        setMaximumVolumeAndDistanceOfRange(ALTAR_RANGE, 0, 10, 15);
        setMaximumVolumeAndDistanceOfRange(DAMAGE_RANGE, 0, 15, 15);
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone)
    {
        World world = masterRitualStone.getWorldObj();
        int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

        if (currentEssence < getRefreshCost())
        {
            masterRitualStone.getOwnerNetwork().causeNausea();
            return;
        }

        BlockPos pos = masterRitualStone.getBlockPos();

        int maxEffects = currentEssence / getRefreshCost();
        int totalEffects = 0;

        BlockPos altarPos = pos.add(altarOffsetPos);

        TileEntity tile = world.getTileEntity(altarPos);

        AreaDescriptor altarRange = getBlockRange(ALTAR_RANGE);

        if (!altarRange.isWithinArea(altarOffsetPos) || !(tile instanceof TileAltar))
        {
            for (BlockPos newPos : altarRange.getContainedPositions(pos))
            {
                TileEntity nextTile = world.getTileEntity(newPos);
                if (nextTile instanceof TileAltar)
                {
                    tile = nextTile;
                    altarOffsetPos = newPos.subtract(pos);

                    altarRange.resetCache();
                    break;
                }
            }
        }

        if (tile instanceof TileAltar)
        {
            TileAltar tileAltar = (TileAltar) tile;

            AreaDescriptor damageRange = getBlockRange(DAMAGE_RANGE);
            AxisAlignedBB range = damageRange.getAABB(pos);

            List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, range);

            for (EntityLivingBase entity : entities)
            {
                if (ConfigHandler.wellOfSufferingBlacklist.contains(entity.getClass().getSimpleName()))
                    continue;

                String simpleClassName = entity.getClass().getSimpleName();
                if (BloodMagicAPI.getEntitySacrificeValues().containsKey(simpleClassName) && BloodMagicAPI.getEntitySacrificeValues().get(simpleClassName) <= 0)
                    continue;

                if (entity.isEntityAlive() && !(entity instanceof EntityPlayer))
                {
                    if (entity.attackEntityFrom(DamageSource.outOfWorld, 1))
                    {
                        String entityName = entity.getClass().getSimpleName();

                        int lifeEssenceRatio = SACRIFICE_AMOUNT;

                        if (ConfigHandler.entitySacrificeValues.containsKey(entityName))
                            lifeEssenceRatio = ConfigHandler.entitySacrificeValues.get(entityName);

                        if (BloodMagicAPI.getEntitySacrificeValues().containsKey(entityName))
                            lifeEssenceRatio = BloodMagicAPI.getEntitySacrificeValues().get(entityName);

                        if (entity.isChild())
                            lifeEssenceRatio *= 0.5F;

                        tileAltar.sacrificialDaggerCall(lifeEssenceRatio, true);

                        totalEffects++;

                        if (totalEffects >= maxEffects)
                        {
                            break;
                        }
                    }
                }
            }
        }

        masterRitualStone.getOwnerNetwork().syphon(getRefreshCost() * totalEffects);
    }

    @Override
    public int getRefreshTime()
    {
        return 25;
    }

    @Override
    public int getRefreshCost()
    {
        return 2;
    }

    @Override
    public ArrayList<RitualComponent> getComponents()
    {
        ArrayList<RitualComponent> components = new ArrayList<RitualComponent>();

        this.addCornerRunes(components, 1, 0, EnumRuneType.FIRE);
        this.addCornerRunes(components, 2, -1, EnumRuneType.FIRE);
        this.addParallelRunes(components, 2, -1, EnumRuneType.EARTH);
        this.addCornerRunes(components, -3, -1, EnumRuneType.DUSK);
        this.addOffsetRunes(components, 2, 4, -1, EnumRuneType.WATER);
        this.addOffsetRunes(components, 1, 4, 0, EnumRuneType.WATER);
        this.addParallelRunes(components, 4, 1, EnumRuneType.AIR);

        return components;
    }

    @Override
    public Ritual getNewCopy()
    {
        return new RitualWellOfSuffering();
    }
}
