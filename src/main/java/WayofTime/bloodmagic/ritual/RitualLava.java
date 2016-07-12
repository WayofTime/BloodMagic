package WayofTime.bloodmagic.ritual;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.ritual.AreaDescriptor;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.api.ritual.IMasterRitualStone;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.RitualComponent;
import WayofTime.bloodmagic.api.saving.SoulNetwork;
import WayofTime.bloodmagic.api.soul.DemonWillHolder;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;
import WayofTime.bloodmagic.registry.ModPotions;
import WayofTime.bloodmagic.util.Utils;

public class RitualLava extends Ritual
{
    public static final String LAVA_RANGE = "lavaRange";
    public static final String FIRE_FUSE_RANGE = "fireFuse";
    public static final double vengefulWillDrain = 1;

    public RitualLava()
    {
        super("ritualLava", 0, 10000, "ritual." + Constants.Mod.MODID + ".lavaRitual");
        addBlockRange(LAVA_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));
        addBlockRange(FIRE_FUSE_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-2, -2, -2), 5));
        setMaximumVolumeAndDistanceOfRange(LAVA_RANGE, 9, 3, 3);
        setMaximumVolumeAndDistanceOfRange(FIRE_FUSE_RANGE, 0, 10, 10);
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone)
    {
        World world = masterRitualStone.getWorldObj();
        SoulNetwork network = NetworkHelper.getSoulNetwork(masterRitualStone.getOwner());
        int currentEssence = network.getCurrentEssence();

        if (currentEssence < getRefreshCost())
        {
            network.causeNausea();
            return;
        }

        BlockPos pos = masterRitualStone.getBlockPos();
        int maxEffects = currentEssence / getRefreshCost();
        int totalEffects = 0;

        List<EnumDemonWillType> willConfig = masterRitualStone.getActiveWillConfig();
        DemonWillHolder holder = WorldDemonWillHandler.getWillHolder(world, pos);
        AreaDescriptor lavaRange = getBlockRange(LAVA_RANGE);

        int maxLavaVolume = getMaxVolumeForRange(LAVA_RANGE, willConfig, holder);
        if (!lavaRange.isWithinRange(getMaxVerticalRadiusForRange(LAVA_RANGE, willConfig, holder), getMaxHorizontalRadiusForRange(LAVA_RANGE, willConfig, holder)) || (maxLavaVolume != 0 && lavaRange.getVolume() > maxLavaVolume))
        {
            return;
        }

        for (BlockPos newPos : lavaRange.getContainedPositions(pos))
        {
            IBlockState state = world.getBlockState(newPos);
            if (world.isAirBlock(newPos) || Utils.isFlowingLiquid(world, newPos, state))
            {
                world.setBlockState(newPos, Blocks.FLOWING_LAVA.getDefaultState());
                totalEffects++;
            }

            if (totalEffects >= maxEffects)
            {
                break;
            }
        }

        network.syphon(getRefreshCost() * totalEffects);

        double vengefulWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.VENGEFUL, willConfig);
        if (vengefulWill >= vengefulWillDrain)
        {
            double vengefulDrained = 0;
            AreaDescriptor fuseRange = getBlockRange(FIRE_FUSE_RANGE);

            AxisAlignedBB fuseArea = fuseRange.getAABB(pos);
            List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, fuseArea);

            for (EntityLivingBase entity : entities)
            {
                if (vengefulWill < vengefulWillDrain)
                {
                    break;
                }

                if (entity instanceof EntityPlayer)
                {
//                    continue;
                }

                if (!entity.isPotionActive(ModPotions.fireFuse))
                {
                    entity.addPotionEffect(new PotionEffect(ModPotions.fireFuse, 100, 0));

                    vengefulDrained += vengefulWillDrain;
                    vengefulWill -= vengefulWillDrain;
                }
            }

            if (vengefulDrained > 0)
            {
                WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.VENGEFUL, vengefulDrained, true);
            }
        }
    }

    @Override
    public int getRefreshTime()
    {
        return 1;
    }

    @Override
    public int getRefreshCost()
    {
        return 500;
    }

    @Override
    public ArrayList<RitualComponent> getComponents()
    {
        ArrayList<RitualComponent> components = new ArrayList<RitualComponent>();

        this.addParallelRunes(components, 1, 0, EnumRuneType.FIRE);

        return components;
    }

    @Override
    public Ritual getNewCopy()
    {
        return new RitualLava();
    }

    @Override
    public int getMaxVolumeForRange(String range, List<EnumDemonWillType> activeTypes, DemonWillHolder holder)
    {
        if (LAVA_RANGE.equals(range) && activeTypes.contains(EnumDemonWillType.DESTRUCTIVE))
        {
            double destructiveWill = holder.getWill(EnumDemonWillType.DESTRUCTIVE);
            if (destructiveWill > 0)
            {
                return 9 + (int) Math.pow(destructiveWill / 10, 1.5);
            }
        }

        return volumeRangeMap.get(range);
    }

    @Override
    public int getMaxVerticalRadiusForRange(String range, List<EnumDemonWillType> activeTypes, DemonWillHolder holder)
    {
        if (LAVA_RANGE.equals(range) && activeTypes.contains(EnumDemonWillType.DESTRUCTIVE))
        {
            double destructiveWill = holder.getWill(EnumDemonWillType.DESTRUCTIVE);
            if (destructiveWill > 0)
            {
                return (int) (3 + destructiveWill / 10d);
            }
        }

        return verticalRangeMap.get(range);
    }

    @Override
    public int getMaxHorizontalRadiusForRange(String range, List<EnumDemonWillType> activeTypes, DemonWillHolder holder)
    {
        if (LAVA_RANGE.equals(range) && activeTypes.contains(EnumDemonWillType.DESTRUCTIVE))
        {
            double destructiveWill = holder.getWill(EnumDemonWillType.DESTRUCTIVE);
            if (destructiveWill > 0)
            {
                return (int) (3 + destructiveWill / 10d);
            }
        }

        return horizontalRangeMap.get(range);
    }
}
