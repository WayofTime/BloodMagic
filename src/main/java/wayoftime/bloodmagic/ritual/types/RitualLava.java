package wayoftime.bloodmagic.ritual.types;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.potion.BloodMagicPotions;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.IMasterRitualStone;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;
import wayoftime.bloodmagic.ritual.RitualRegister;
import wayoftime.bloodmagic.util.DamageSourceBloodMagic;
import wayoftime.bloodmagic.util.Utils;
import wayoftime.bloodmagic.will.DemonWillHolder;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;

@RitualRegister("lava")
public class RitualLava extends Ritual
{
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

	public RitualLava()
	{
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
	public void performRitual(IMasterRitualStone masterRitualStone)
	{
		timer++;
		World world = masterRitualStone.getWorldObj();
		int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();
		int lpDrain = 0;

		if (currentEssence < getRefreshCost())
		{
			masterRitualStone.getOwnerNetwork().causeNausea();
			return;
		}

		BlockPos pos = masterRitualStone.getBlockPos();
		List<EnumDemonWillType> willConfig = masterRitualStone.getActiveWillConfig();

		double rawWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.DEFAULT, willConfig);
		double rawDrained = 0;

		DemonWillHolder holder = WorldDemonWillHandler.getWillHolder(world, pos);
		AreaDescriptor lavaRange = masterRitualStone.getBlockRange(LAVA_RANGE);

		int maxLavaVolume = getMaxVolumeForRange(LAVA_RANGE, willConfig, holder);
		if (!lavaRange.isWithinRange(getMaxVerticalRadiusForRange(LAVA_RANGE, willConfig, holder), getMaxHorizontalRadiusForRange(LAVA_RANGE, willConfig, holder)) || (maxLavaVolume != 0 && lavaRange.getVolume() > maxLavaVolume))
		{
			return;
		}

		for (BlockPos newPos : lavaRange.getContainedPositions(pos))
		{
			BlockState state = world.getBlockState(newPos);
			if (world.isAirBlock(newPos) || Utils.isFlowingLiquid(world, newPos, state))
			{
				int lpCost = getLPCostForRawWill(rawWill);
				if (currentEssence < lpCost)
				{
					break;
				}
				world.setBlockState(newPos, Blocks.LAVA.getDefaultState());
				currentEssence -= lpCost;
				lpDrain += lpCost;
				if (rawWill > 0)
				{
					double drain = getWillCostForRawWill(rawWill);
					rawWill -= drain;
					rawDrained += drain;
				}
			}
		}

		if (rawWill > 0)
		{
			AreaDescriptor chestRange = masterRitualStone.getBlockRange(LAVA_TANK_RANGE);
			TileEntity tile = world.getTileEntity(chestRange.getContainedPositions(pos).get(0));
			double drain = getWillCostForRawWill(rawWill);
			int lpCost = getLPCostForRawWill(rawWill);

			if (rawWill >= drain && currentEssence >= lpCost)
			{
				if (tile != null)
				{
					LazyOptional<IFluidHandler> capability = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
					if (capability.isPresent())
					{
						IFluidHandler handler = capability.resolve().get();
						double filled = handler.fill(new FluidStack(Fluids.LAVA, 1000), FluidAction.EXECUTE);

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

		if (vengefulWill >= vengefulWillDrain)
		{
			double vengefulDrained = 0;
			AreaDescriptor fuseRange = masterRitualStone.getBlockRange(FIRE_FUSE_RANGE);

			AxisAlignedBB fuseArea = fuseRange.getAABB(pos);
			List<LivingEntity> entities = world.getEntitiesWithinAABB(LivingEntity.class, fuseArea);

			for (LivingEntity entity : entities)
			{
				if (vengefulWill < vengefulWillDrain)
				{
					break;
				}

				if (entity instanceof PlayerEntity)
				{
					continue;
				}

				if (!entity.isPotionActive(BloodMagicPotions.FIRE_FUSE))
				{
					entity.addPotionEffect(new EffectInstance(BloodMagicPotions.FIRE_FUSE, 100, 0));

					vengefulDrained += vengefulWillDrain;
					vengefulWill -= vengefulWillDrain;
				}
			}

			if (vengefulDrained > 0)
			{
				WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.VENGEFUL, vengefulDrained, true);
			}
		}

		if (steadfastWill >= steadfastWillDrain)
		{
			double steadfastDrained = 0;
			AreaDescriptor resistRange = masterRitualStone.getBlockRange(FIRE_RESIST_RANGE);

			int duration = getFireResistForWill(steadfastWill);

			AxisAlignedBB resistArea = resistRange.getAABB(pos);
			List<PlayerEntity> entities = world.getEntitiesWithinAABB(PlayerEntity.class, resistArea);

			for (PlayerEntity entity : entities)
			{
				if (steadfastWill < steadfastWillDrain)
				{
					break;
				}
				if (!entity.isPotionActive(Effects.FIRE_RESISTANCE) || (entity.getActivePotionEffect(Effects.FIRE_RESISTANCE).getDuration() < 2))
				{
					entity.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 100, 0));

					steadfastDrained += steadfastWillDrain;
					steadfastWill -= steadfastWillDrain;
				}
			}

			if (steadfastDrained > 0)
			{
				WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.STEADFAST, steadfastDrained, true);
			}
		}

		if (timer % corrosiveRefreshTime == 0 && corrosiveWill >= corrosiveWillDrain)
		{
			double corrosiveDrained = 0;
			AreaDescriptor resistRange = masterRitualStone.getBlockRange(FIRE_DAMAGE_RANGE);

			float damage = getCorrosiveDamageForWill(corrosiveWill);

			AxisAlignedBB damageArea = resistRange.getAABB(pos);
			List<LivingEntity> entities = world.getEntitiesWithinAABB(LivingEntity.class, damageArea);

			for (LivingEntity entity : entities)
			{
				if (corrosiveWill < corrosiveWillDrain)
				{
					break;
				}

				if (!entity.isAlive() && entity.hurtTime <= 0 && Utils.isImmuneToFireDamage(entity))
				{
					if (entity.attackEntityFrom(DamageSourceBloodMagic.INSTANCE, damage))
					{
						corrosiveDrained += corrosiveWillDrain;
						corrosiveWill -= corrosiveWillDrain;
					}
				}
			}

			if (corrosiveDrained > 0)
			{
				WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.CORROSIVE, corrosiveDrained, true);
			}
		}

		if (rawDrained > 0)
		{
			WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.DEFAULT, rawDrained, true);
		}

		masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(lpDrain));
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
	public ITextComponent[] provideInformationOfRitualToPlayer(PlayerEntity player)
	{
		return new ITextComponent[] { new TranslationTextComponent(this.getTranslationKey() + ".info"),
				new TranslationTextComponent(this.getTranslationKey() + ".default.info"),
				new TranslationTextComponent(this.getTranslationKey() + ".corrosive.info"),
				new TranslationTextComponent(this.getTranslationKey() + ".steadfast.info"),
				new TranslationTextComponent(this.getTranslationKey() + ".destructive.info"),
				new TranslationTextComponent(this.getTranslationKey() + ".vengeful.info") };
	}

	@Override
	public void gatherComponents(Consumer<RitualComponent> components)
	{
		addParallelRunes(components, 1, 0, EnumRuneType.FIRE);
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

	public int getFireResistForWill(double steadfastWill)
	{
		return (int) (200 + steadfastWill * 3);
	}

	public float getCorrosiveDamageForWill(double corrosiveWill)
	{
		return (float) (1 + corrosiveWill * 0.05);
	}

	public int getLPCostForRawWill(double raw)
	{
		return Math.max((int) (500 - raw), 0);
	}

	public double getWillCostForRawWill(double raw)
	{
		return Math.min(1, raw / 500);
	}
}
