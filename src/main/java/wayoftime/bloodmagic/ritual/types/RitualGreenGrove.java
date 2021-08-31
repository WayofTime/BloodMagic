package wayoftime.bloodmagic.ritual.types;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CactusBlock;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.impl.BloodMagicAPI;
import wayoftime.bloodmagic.potion.BloodMagicPotions;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.IMasterRitualStone;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;
import wayoftime.bloodmagic.ritual.RitualRegister;
import wayoftime.bloodmagic.util.Utils;
import wayoftime.bloodmagic.will.DemonWillHolder;

@RitualRegister("green_grove")
public class RitualGreenGrove extends Ritual
{
	public static final String GROW_RANGE = "growing";
	public static final String LEECH_RANGE = "leech";
	public static final String HYDRATE_RANGE = "hydrate";

	public static double corrosiveWillDrain = 0.2;
	public static double rawWillDrain = 0.05;
	public static double vengefulWillDrain = 0.05;
	public static double steadfastWillDrain = 0.05;
	public static int defaultRefreshTime = 20;
	public static double defaultGrowthChance = 0.3;
	public static BlockState farmlandState = Blocks.FARMLAND.getDefaultState().with(FarmlandBlock.MOISTURE, 7);
	public int refreshTime = 20;

	public RitualGreenGrove()
	{
		super("ritualGreenGrove", 0, 5000, "ritual." + BloodMagic.MODID + ".greenGroveRitual");
		addBlockRange(GROW_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-1, 2, -1), 3, 1, 3));
		addBlockRange(LEECH_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 0, 0), 1));
		addBlockRange(HYDRATE_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 0, 0), 1));
		setMaximumVolumeAndDistanceOfRange(GROW_RANGE, 81, 4, 4);
		setMaximumVolumeAndDistanceOfRange(LEECH_RANGE, 0, 15, 15);
		setMaximumVolumeAndDistanceOfRange(HYDRATE_RANGE, 0, 15, 15);
	}

	@Override
	public void performRitual(IMasterRitualStone masterRitualStone)
	{
		World world = masterRitualStone.getWorldObj();
		if (!(world instanceof ServerWorld))
		{
			return;
		}

		ServerWorld serverWorld = (ServerWorld) world;
		BlockPos pos = masterRitualStone.getMasterBlockPos();
		int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

		if (currentEssence < getRefreshCost())
		{
			masterRitualStone.getOwnerNetwork().causeNausea();
			return;
		}

		int maxGrowths = currentEssence / getRefreshCost();
		int totalGrowths = 0;

		List<EnumDemonWillType> willConfig = masterRitualStone.getActiveWillConfig();

		DemonWillHolder holder = WorldDemonWillHandler.getWillHolder(world, pos);
		double corrosiveWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.CORROSIVE, willConfig);
		double rawWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.DEFAULT, willConfig);
		double steadfastWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.STEADFAST, willConfig);
		double vengefulWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.VENGEFUL, willConfig);

		refreshTime = getRefreshTimeForRawWill(rawWill);
		double growthChance = getPlantGrowthChanceForWill(vengefulWill);

		boolean consumeRawWill = rawWill >= rawWillDrain && refreshTime != defaultRefreshTime;
		boolean consumeVengefulWill = vengefulWill >= vengefulWillDrain && growthChance != defaultGrowthChance;

		double rawDrain = 0;
		double vengefulDrain = 0;

		AreaDescriptor growingRange = masterRitualStone.getBlockRange(GROW_RANGE);

		int maxGrowthVolume = getMaxVolumeForRange(GROW_RANGE, willConfig, holder);
		if (!growingRange.isWithinRange(getMaxVerticalRadiusForRange(GROW_RANGE, willConfig, holder), getMaxHorizontalRadiusForRange(GROW_RANGE, willConfig, holder)) || (maxGrowthVolume != 0 && growingRange.getVolume() > maxGrowthVolume))
		{
			return;
		}

		for (BlockPos newPos : growingRange.getContainedPositions(pos))
		{
			BlockState state = world.getBlockState(newPos);

			if (!BloodMagicAPI.INSTANCE.getBlacklist().getGreenGrove().contains(state))
			{
				boolean flag = state.getBlock() instanceof IGrowable || state.getBlock() instanceof CactusBlock || state.getBlock() instanceof SugarCaneBlock;
				if (flag)
				{
					if (world.rand.nextDouble() < growthChance)
					{
						state.getBlock().randomTick(state, serverWorld, newPos, new Random());
						BlockState newState = world.getBlockState(newPos);
						if (!newState.equals(state))
						{
							world.playEvent(2005, newPos, 0);
							totalGrowths++;
							if (consumeRawWill)
							{
								rawWill -= rawWillDrain;
								rawDrain += rawWillDrain;
							}

							if (consumeVengefulWill)
							{
								vengefulWill -= vengefulWillDrain;
								vengefulDrain += vengefulWillDrain;
							}
						}
					}
				}
			}

			if (totalGrowths >= maxGrowths || (consumeRawWill && rawWill < rawWillDrain) || (consumeVengefulWill && vengefulWill < vengefulWillDrain))
			{
				break;
			}
		}

		if (rawDrain > 0)
		{
			WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.DEFAULT, rawDrain, true);
		}

		if (vengefulDrain > 0)
		{
			WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.VENGEFUL, vengefulDrain, true);
		}

		AreaDescriptor hydrateRange = masterRitualStone.getBlockRange(HYDRATE_RANGE);

		double steadfastDrain = 0;
		if (steadfastWill > steadfastWillDrain)
		{
			AxisAlignedBB aabb = hydrateRange.getAABB(pos);
			steadfastDrain += steadfastWillDrain * Utils.plantSeedsInArea(world, aabb, 2, 1);
			steadfastWill -= steadfastDrain;

			for (BlockPos newPos : hydrateRange.getContainedPositions(pos))
			{
				if (steadfastWill < steadfastWillDrain)
				{
					break;
				}

				BlockState state = world.getBlockState(newPos);
				Block block = state.getBlock();

				boolean hydratedBlock = false;
				if (block == Blocks.DIRT || block == Blocks.GRASS)
				{
					world.setBlockState(newPos, farmlandState);
					hydratedBlock = true;
				} else if (block == Blocks.FARMLAND)
				{
					int meta = state.get(FarmlandBlock.MOISTURE);
					if (meta < 7)
					{
						world.setBlockState(newPos, farmlandState);
						hydratedBlock = true;
					}
				}

				if (hydratedBlock)
				{
					steadfastWill -= steadfastWillDrain;
					steadfastDrain += steadfastWillDrain;
				}
			}
		}

		if (steadfastDrain > 0)
		{
			WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.STEADFAST, steadfastDrain, true);
		}

		double corrosiveDrain = 0;
		if (corrosiveWill > corrosiveWillDrain)
		{
			AreaDescriptor leechRange = masterRitualStone.getBlockRange(LEECH_RANGE);
			AxisAlignedBB mobArea = leechRange.getAABB(pos);
			List<LivingEntity> entityList = world.getEntitiesWithinAABB(LivingEntity.class, mobArea);
			for (LivingEntity entityLiving : entityList)
			{
				if (corrosiveWill < corrosiveWillDrain)
				{
					break;
				}

				if (entityLiving instanceof PlayerEntity)
				{
					continue;
				}

				if (entityLiving.isPotionActive(BloodMagicPotions.PLANT_LEECH) || !entityLiving.isPotionApplicable(new EffectInstance(BloodMagicPotions.PLANT_LEECH)))
				{
					continue;
				}

				entityLiving.addPotionEffect(new EffectInstance(BloodMagicPotions.PLANT_LEECH, 200, 0));

				corrosiveWill -= corrosiveWillDrain;
				corrosiveDrain += corrosiveWillDrain;
			}

			if (corrosiveDrain > 0)
			{
				WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.CORROSIVE, corrosiveDrain, true);
			}
		}

		masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(totalGrowths * getRefreshCost()));
	}

	public double getPlantGrowthChanceForWill(double will)
	{
		if (will > 0)
		{
			return 0.3 + will / 200;
		}

		return defaultGrowthChance;
	}

	public int getRefreshTimeForRawWill(double rawWill)
	{
		if (rawWill > 0)
		{
			return 10;
		}

		return defaultRefreshTime;
	}

	@Override
	public int getRefreshTime()
	{
		return refreshTime;
	}

	@Override
	public int getMaxVolumeForRange(String range, List<EnumDemonWillType> activeTypes, DemonWillHolder holder)
	{
		if (GROW_RANGE.equals(range) && activeTypes.contains(EnumDemonWillType.DESTRUCTIVE))
		{
			double destructiveWill = holder.getWill(EnumDemonWillType.DESTRUCTIVE);
			if (destructiveWill > 0)
			{
				return 81 + (int) Math.pow(destructiveWill / 4, 1.5);
			}
		}

		return volumeRangeMap.get(range);
	}

	@Override
	public int getMaxVerticalRadiusForRange(String range, List<EnumDemonWillType> activeTypes, DemonWillHolder holder)
	{
		if (GROW_RANGE.equals(range) && activeTypes.contains(EnumDemonWillType.DESTRUCTIVE))
		{
			double destructiveWill = holder.getWill(EnumDemonWillType.DESTRUCTIVE);
			if (destructiveWill > 0)
			{
				return (int) (4 + destructiveWill / 10d);
			}
		}

		return verticalRangeMap.get(range);
	}

	@Override
	public int getMaxHorizontalRadiusForRange(String range, List<EnumDemonWillType> activeTypes, DemonWillHolder holder)
	{
		if (GROW_RANGE.equals(range) && activeTypes.contains(EnumDemonWillType.DESTRUCTIVE))
		{
			double destructiveWill = holder.getWill(EnumDemonWillType.DESTRUCTIVE);
			if (destructiveWill > 0)
			{
				return (int) (4 + destructiveWill / 10d);
			}
		}

		return horizontalRangeMap.get(range);
	}

	@Override
	public int getRefreshCost()
	{
		return 20; // TODO: Need to find a way to balance this
	}

	@Override
	public void gatherComponents(Consumer<RitualComponent> components)
	{
		addCornerRunes(components, 1, 0, EnumRuneType.EARTH);
		addParallelRunes(components, 1, 0, EnumRuneType.WATER);
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
	public Ritual getNewCopy()
	{
		return new RitualGreenGrove();
	}
}
