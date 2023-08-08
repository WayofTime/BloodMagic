package wayoftime.bloodmagic.ritual.types;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.common.registries.BloodMagicDamageTypes;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.potion.BloodMagicPotions;
import wayoftime.bloodmagic.ritual.*;
import wayoftime.bloodmagic.util.Utils;
import wayoftime.bloodmagic.will.DemonWillHolder;

import java.util.List;
import java.util.function.Consumer;

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
		Level world = masterRitualStone.getWorldObj();
		int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();
		int lpDrain = 0;

		if (currentEssence < getRefreshCost())
		{
			masterRitualStone.getOwnerNetwork().causeNausea();
			return;
		}

		BlockPos pos = masterRitualStone.getMasterBlockPos();
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
			if (world.isEmptyBlock(newPos) || Utils.isFlowingLiquid(world, newPos, state))
			{
				int lpCost = getLPCostForRawWill(rawWill);
				if (currentEssence < lpCost)
				{
					break;
				}
				world.setBlockAndUpdate(newPos, Blocks.LAVA.defaultBlockState());
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
			BlockEntity tile = world.getBlockEntity(chestRange.getContainedPositions(pos).get(0));
			double drain = getWillCostForRawWill(rawWill);
			int lpCost = getLPCostForRawWill(rawWill);

			if (rawWill >= drain && currentEssence >= lpCost)
			{
				if (tile != null)
				{
					LazyOptional<IFluidHandler> capability = tile.getCapability(ForgeCapabilities.FLUID_HANDLER, null);
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

			AABB fuseArea = fuseRange.getAABB(pos);
			List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, fuseArea);

			for (LivingEntity entity : entities)
			{
				if (vengefulWill < vengefulWillDrain)
				{
					break;
				}

				if (entity instanceof Player)
				{
					continue;
				}

				if (!entity.hasEffect(BloodMagicPotions.FIRE_FUSE.get()))
				{
					entity.addEffect(new MobEffectInstance(BloodMagicPotions.FIRE_FUSE.get(), 100, 0));

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

			AABB resistArea = resistRange.getAABB(pos);
			List<Player> entities = world.getEntitiesOfClass(Player.class, resistArea);

			for (Player entity : entities)
			{
				if (steadfastWill < steadfastWillDrain)
				{
					break;
				}
				if (!entity.hasEffect(MobEffects.FIRE_RESISTANCE) || (entity.getEffect(MobEffects.FIRE_RESISTANCE).getDuration() < 2))
				{
					entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 100, 0));

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

			AABB damageArea = resistRange.getAABB(pos);
			List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, damageArea);

			for (LivingEntity entity : entities)
			{
				if (corrosiveWill < corrosiveWillDrain)
				{
					break;
				}

				if (!entity.isAlive() && entity.hurtTime <= 0 && Utils.isImmuneToFireDamage(entity))
				{
					if (entity.hurt(entity.damageSources().source(BloodMagicDamageTypes.SACRIFICE), damage))
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
	public Component[] provideInformationOfRitualToPlayer(Player player)
	{
		return new Component[] { Component.translatable(this.getTranslationKey() + ".info"),
				Component.translatable(this.getTranslationKey() + ".default.info"),
				Component.translatable(this.getTranslationKey() + ".corrosive.info"),
				Component.translatable(this.getTranslationKey() + ".steadfast.info"),
				Component.translatable(this.getTranslationKey() + ".destructive.info"),
				Component.translatable(this.getTranslationKey() + ".vengeful.info") };
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
