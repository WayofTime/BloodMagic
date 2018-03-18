package WayofTime.bloodmagic.ritual.types;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ritual.*;
import WayofTime.bloodmagic.util.DamageSourceBloodMagic;
import WayofTime.bloodmagic.soul.DemonWillHolder;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.core.RegistrarBloodMagic;
import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;
import WayofTime.bloodmagic.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.List;
import java.util.function.Consumer;

public class RitualLava extends Ritual {
    public static final String LAVA_RANGE = "lavaRange";
    public static final String FIRE_FUSE_RANGE = "fireFuse";
    public static final String FIRE_RESIST_RANGE = "fireResist";
    public static final String FIRE_DAMAGE_RANGE = "fireDamage";
    public static final String LAVA_TANK_RANGE = "lavaTank";

    public static final double vengefulWillDrain = 1;
    public static final double steadfastWillDrain = 0.5;
    public static final double corrosiveWillDrain = 0.2;
    public static final int corrosiveRefreshTime = 20;
    public int timer = 0;

    public RitualLava() {
        super("ritualLava", 0, 10000, "ritual." + BloodMagic.MODID + ".lavaRitual");
        addBlockRange(LAVA_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));
        addBlockRange(FIRE_FUSE_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-2, -2, -2), 5));
        addBlockRange(FIRE_RESIST_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 0, 0), 1));
        addBlockRange(FIRE_DAMAGE_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 0, 0), 1));
        addBlockRange(LAVA_TANK_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));

        setMaximumVolumeAndDistanceOfRange(LAVA_RANGE, 9, 3, 3);
        setMaximumVolumeAndDistanceOfRange(FIRE_FUSE_RANGE, 0, 10, 10);
        setMaximumVolumeAndDistanceOfRange(FIRE_RESIST_RANGE, 0, 10, 10);
        setMaximumVolumeAndDistanceOfRange(FIRE_DAMAGE_RANGE, 0, 10, 10);
        setMaximumVolumeAndDistanceOfRange(LAVA_TANK_RANGE, 1, 10, 10);
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone) {
        timer++;
        World world = masterRitualStone.getWorldObj();
        int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();
        int lpDrain = 0;

        if (currentEssence < getRefreshCost()) {
            masterRitualStone.getOwnerNetwork().causeNausea();
            return;
        }

        BlockPos pos = masterRitualStone.getBlockPos();
        List<EnumDemonWillType> willConfig = masterRitualStone.getActiveWillConfig();

        double rawWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.DEFAULT, willConfig);
        double rawDrained = 0;

        DemonWillHolder holder = WorldDemonWillHandler.getWillHolder(world, pos);
        AreaDescriptor lavaRange = getBlockRange(LAVA_RANGE);

        int maxLavaVolume = getMaxVolumeForRange(LAVA_RANGE, willConfig, holder);
        if (!lavaRange.isWithinRange(getMaxVerticalRadiusForRange(LAVA_RANGE, willConfig, holder), getMaxHorizontalRadiusForRange(LAVA_RANGE, willConfig, holder)) || (maxLavaVolume != 0 && lavaRange.getVolume() > maxLavaVolume)) {
            return;
        }

        for (BlockPos newPos : lavaRange.getContainedPositions(pos)) {
            IBlockState state = world.getBlockState(newPos);
            if (world.isAirBlock(newPos) || Utils.isFlowingLiquid(world, newPos, state)) {
                int lpCost = getLPCostForRawWill(rawWill);
                if (currentEssence < lpCost) {
                    break;
                }
                world.setBlockState(newPos, Blocks.FLOWING_LAVA.getDefaultState());
                currentEssence -= lpCost;
                lpDrain += lpCost;
                if (rawWill > 0) {
                    double drain = getWillCostForRawWill(rawWill);
                    rawWill -= drain;
                    rawDrained += drain;
                }
            }
        }

        if (rawWill > 0) {
            AreaDescriptor chestRange = getBlockRange(LAVA_TANK_RANGE);
            TileEntity tile = world.getTileEntity(chestRange.getContainedPositions(pos).get(0));
            double drain = getWillCostForRawWill(rawWill);
            int lpCost = getLPCostForRawWill(rawWill);

            if (rawWill >= drain && currentEssence >= lpCost) {
                if (tile != null) {
                    if (tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
                        IFluidHandler handler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
                        double filled = handler.fill(new FluidStack(FluidRegistry.LAVA, 1000), true);

                        double ratio = filled / 1000;

                        rawWill -= drain * ratio;
                        rawDrained += drain * ratio;

                        currentEssence -= Math.ceil(lpCost * ratio);
                        lpDrain += Math.ceil(lpCost * ratio);
                    }
                }
            }
        }

        double vengefulWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.VENGEFUL, willConfig);
        double steadfastWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.STEADFAST, willConfig);
        double corrosiveWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.CORROSIVE, willConfig);

        if (vengefulWill >= vengefulWillDrain) {
            double vengefulDrained = 0;
            AreaDescriptor fuseRange = getBlockRange(FIRE_FUSE_RANGE);

            AxisAlignedBB fuseArea = fuseRange.getAABB(pos);
            List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, fuseArea);

            for (EntityLivingBase entity : entities) {
                if (vengefulWill < vengefulWillDrain) {
                    break;
                }

                if (entity instanceof EntityPlayer) {
                    continue;
                }

                if (!entity.isPotionActive(RegistrarBloodMagic.FIRE_FUSE)) {
                    entity.addPotionEffect(new PotionEffect(RegistrarBloodMagic.FIRE_FUSE, 100, 0));

                    vengefulDrained += vengefulWillDrain;
                    vengefulWill -= vengefulWillDrain;
                }
            }

            if (vengefulDrained > 0) {
                WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.VENGEFUL, vengefulDrained, true);
            }
        }

        if (steadfastWill >= steadfastWillDrain) {
            double steadfastDrained = 0;
            AreaDescriptor resistRange = getBlockRange(FIRE_RESIST_RANGE);

            int duration = getFireResistForWill(steadfastWill);

            AxisAlignedBB resistArea = resistRange.getAABB(pos);
            List<EntityPlayer> entities = world.getEntitiesWithinAABB(EntityPlayer.class, resistArea);

            for (EntityPlayer entity : entities) {
                if (steadfastWill < steadfastWillDrain) {
                    break;
                }
                if (!entity.isPotionActive(MobEffects.FIRE_RESISTANCE) || (entity.getActivePotionEffect(MobEffects.FIRE_RESISTANCE).getDuration() < 2)) {
                    entity.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 100, 0));

                    steadfastDrained += steadfastWillDrain;
                    steadfastWill -= steadfastWillDrain;
                }
            }

            if (steadfastDrained > 0) {
                WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.STEADFAST, steadfastDrained, true);
            }
        }

        if (timer % corrosiveRefreshTime == 0 && corrosiveWill >= corrosiveWillDrain) {
            double corrosiveDrained = 0;
            AreaDescriptor resistRange = getBlockRange(FIRE_DAMAGE_RANGE);

            float damage = getCorrosiveDamageForWill(corrosiveWill);

            AxisAlignedBB damageArea = resistRange.getAABB(pos);
            List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, damageArea);

            for (EntityLivingBase entity : entities) {
                if (corrosiveWill < corrosiveWillDrain) {
                    break;
                }

                if (!entity.isDead && entity.hurtTime <= 0 && Utils.isImmuneToFireDamage(entity)) {
                    if (entity.attackEntityFrom(DamageSourceBloodMagic.INSTANCE, damage)) {
                        corrosiveDrained += corrosiveWillDrain;
                        corrosiveWill -= corrosiveWillDrain;
                    }
                }
            }

            if (corrosiveDrained > 0) {
                WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.CORROSIVE, corrosiveDrained, true);
            }
        }

        if (rawDrained > 0) {
            WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.DEFAULT, rawDrained, true);
        }

        masterRitualStone.getOwnerNetwork().syphon(lpDrain);
    }

    @Override
    public int getRefreshTime() {
        return 1;
    }

    @Override
    public int getRefreshCost() {
        return 500;
    }

    @Override
    public ITextComponent[] provideInformationOfRitualToPlayer(EntityPlayer player) {
        return new ITextComponent[]{new TextComponentTranslation(this.getUnlocalizedName() + ".info"), new TextComponentTranslation(this.getUnlocalizedName() + ".default.info"), new TextComponentTranslation(this.getUnlocalizedName() + ".corrosive.info"), new TextComponentTranslation(this.getUnlocalizedName() + ".steadfast.info"), new TextComponentTranslation(this.getUnlocalizedName() + ".destructive.info"), new TextComponentTranslation(this.getUnlocalizedName() + ".vengeful.info")};
    }

    @Override
    public void gatherComponents(Consumer<RitualComponent> components) {
        addParallelRunes(components, 1, 0, EnumRuneType.FIRE);
    }

    @Override
    public Ritual getNewCopy() {
        return new RitualLava();
    }

    @Override
    public int getMaxVolumeForRange(String range, List<EnumDemonWillType> activeTypes, DemonWillHolder holder) {
        if (LAVA_RANGE.equals(range) && activeTypes.contains(EnumDemonWillType.DESTRUCTIVE)) {
            double destructiveWill = holder.getWill(EnumDemonWillType.DESTRUCTIVE);
            if (destructiveWill > 0) {
                return 9 + (int) Math.pow(destructiveWill / 10, 1.5);
            }
        }

        return volumeRangeMap.get(range);
    }

    @Override
    public int getMaxVerticalRadiusForRange(String range, List<EnumDemonWillType> activeTypes, DemonWillHolder holder) {
        if (LAVA_RANGE.equals(range) && activeTypes.contains(EnumDemonWillType.DESTRUCTIVE)) {
            double destructiveWill = holder.getWill(EnumDemonWillType.DESTRUCTIVE);
            if (destructiveWill > 0) {
                return (int) (3 + destructiveWill / 10d);
            }
        }

        return verticalRangeMap.get(range);
    }

    @Override
    public int getMaxHorizontalRadiusForRange(String range, List<EnumDemonWillType> activeTypes, DemonWillHolder holder) {
        if (LAVA_RANGE.equals(range) && activeTypes.contains(EnumDemonWillType.DESTRUCTIVE)) {
            double destructiveWill = holder.getWill(EnumDemonWillType.DESTRUCTIVE);
            if (destructiveWill > 0) {
                return (int) (3 + destructiveWill / 10d);
            }
        }

        return horizontalRangeMap.get(range);
    }

    public int getFireResistForWill(double steadfastWill) {
        return (int) (200 + steadfastWill * 3);
    }

    public float getCorrosiveDamageForWill(double corrosiveWill) {
        return (float) (1 + corrosiveWill * 0.05);
    }

    public int getLPCostForRawWill(double raw) {
        return Math.max((int) (500 - raw), 0);
    }

    public double getWillCostForRawWill(double raw) {
        return Math.min(1, raw / 500);
    }
}
