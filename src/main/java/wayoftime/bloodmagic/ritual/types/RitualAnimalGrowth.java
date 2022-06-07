package wayoftime.bloodmagic.ritual.types;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.potion.BloodMagicPotions;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.IMasterRitualStone;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;
import wayoftime.bloodmagic.ritual.RitualRegister;
import wayoftime.bloodmagic.util.Utils;

@RitualRegister("animal_growth")
public class RitualAnimalGrowth extends Ritual
{
	public static final double rawWillDrain = 0.05;
	public static final double vengefulWillDrain = 0.02;
	public static final double steadfastWillDrain = 0.1;
	public static final double destructiveWillDrain = 1;

	public static final String GROWTH_RANGE = "growing";
	public static final String CHEST_RANGE = "chest";
	public static int defaultRefreshTime = 20;
	public int refreshTime = 20;

	public RitualAnimalGrowth()
	{
		super("ritualAnimalGrowth", 0, 10000, "ritual." + BloodMagic.MODID + ".animalGrowthRitual");
		addBlockRange(GROWTH_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-2, 1, -2), 5, 2, 5));
		addBlockRange(CHEST_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));

		setMaximumVolumeAndDistanceOfRange(GROWTH_RANGE, 0, 7, 7);
		setMaximumVolumeAndDistanceOfRange(CHEST_RANGE, 1, 3, 3);
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

		int maxGrowths = currentEssence / getRefreshCost();
		int totalGrowths = 0;
		BlockPos pos = masterRitualStone.getMasterBlockPos();

		AreaDescriptor chestRange = masterRitualStone.getBlockRange(CHEST_RANGE);
		TileEntity chest = world.getTileEntity(chestRange.getContainedPositions(pos).get(0));
		IItemHandler itemHandler = null;
		if (chest != null)
		{
			itemHandler = Utils.getInventory(chest, null);
		}

		List<EnumDemonWillType> willConfig = masterRitualStone.getActiveWillConfig();

		double rawWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.DEFAULT, willConfig);
		double steadfastWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.STEADFAST, willConfig);
		double corrosiveWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.CORROSIVE, willConfig);
		double destructiveWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.DESTRUCTIVE, willConfig);
		double vengefulWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.VENGEFUL, willConfig);

		refreshTime = getRefreshTimeForRawWill(rawWill);

		boolean consumeRawWill = rawWill >= rawWillDrain && refreshTime != defaultRefreshTime;

		double vengefulDrain = 0;
		double steadfastDrain = 0;
		double destructiveDrain = 0;

		boolean decreaseBreedTimer = vengefulWill >= vengefulWillDrain;
		boolean breedAnimals = steadfastWill >= steadfastWillDrain && itemHandler != null;
		boolean kamikaze = destructiveWill >= destructiveWillDrain;

		AreaDescriptor growingRange = masterRitualStone.getBlockRange(GROWTH_RANGE);
		AxisAlignedBB axis = growingRange.getAABB(masterRitualStone.getMasterBlockPos());
		List<AnimalEntity> animalList = world.getEntitiesWithinAABB(AnimalEntity.class, axis);

		boolean performedEffect = false;

		for (AnimalEntity animal : animalList)
		{
			if (animal.getGrowingAge() < 0)
			{
				animal.addGrowth(5);
				totalGrowths++;
				performedEffect = true;
			} else if (animal.getGrowingAge() > 0)
			{
				if (decreaseBreedTimer)
				{
					if (vengefulWill >= vengefulWillDrain)
					{
						animal.setGrowingAge(Math.max(0, animal.getGrowingAge() - getBreedingDecreaseForWill(vengefulWill)));
						vengefulDrain += vengefulWillDrain;
						vengefulWill -= vengefulWillDrain;
						performedEffect = true;
					} else
					{
						decreaseBreedTimer = false;
					}
				}
			} else
			{
				if (kamikaze)
				{
					if (destructiveWill >= destructiveWillDrain)
					{
						if (!animal.isPotionActive(BloodMagicPotions.SACRIFICIAL_LAMB))
						{
							animal.addPotionEffect(new EffectInstance(BloodMagicPotions.SACRIFICIAL_LAMB, 1200));
							destructiveDrain += destructiveWillDrain;
							destructiveWill -= destructiveWillDrain;
							performedEffect = true;
						}
					} else
					{
						kamikaze = false;
					}
				}

				if (breedAnimals)
				{
					if (steadfastWill >= steadfastWillDrain)
					{
						if (!animal.isInLove())
						{
							for (int slot = 0; slot < itemHandler.getSlots(); slot++)
							{
								ItemStack foodStack = itemHandler.getStackInSlot(slot);
								if (foodStack != null && animal.isBreedingItem(foodStack) && itemHandler.extractItem(slot, 1, true) != null)
								{
									animal.setInLove(null);
									itemHandler.extractItem(slot, 1, false);
									steadfastDrain += steadfastWillDrain;
									steadfastWill -= steadfastWillDrain;
									performedEffect = true;
									break;
								}
							}
						}
					} else
					{
						breedAnimals = false;
					}
				}
			}

			if (totalGrowths >= maxGrowths)
			{
				break;
			}
		}

		if (performedEffect && consumeRawWill)
		{
			WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.DEFAULT, rawWillDrain, true);
		}

		if (vengefulDrain > 0)
		{
			WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.VENGEFUL, vengefulDrain, true);
		}

		if (steadfastDrain > 0)
		{
			WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.STEADFAST, steadfastDrain, true);
		}

		if (destructiveDrain > 0)
		{
			WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.DESTRUCTIVE, destructiveDrain, true);
		}

		masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(totalGrowths * getRefreshCost()));
	}

	@Override
	public int getRefreshCost()
	{
		return 2;
	}

	@Override
	public void gatherComponents(Consumer<RitualComponent> components)
	{

		addParallelRunes(components, 2, 0, EnumRuneType.DUSK);
		addParallelRunes(components, 1, 0, EnumRuneType.WATER);
		components.accept(new RitualComponent(new BlockPos(1, 0, 2), EnumRuneType.EARTH));
		components.accept(new RitualComponent(new BlockPos(1, 0, -2), EnumRuneType.EARTH));
		components.accept(new RitualComponent(new BlockPos(-1, 0, 2), EnumRuneType.EARTH));
		components.accept(new RitualComponent(new BlockPos(-1, 0, -2), EnumRuneType.EARTH));
		components.accept(new RitualComponent(new BlockPos(2, 0, 1), EnumRuneType.AIR));
		components.accept(new RitualComponent(new BlockPos(2, 0, -1), EnumRuneType.AIR));
		components.accept(new RitualComponent(new BlockPos(-2, 0, 1), EnumRuneType.AIR));
		components.accept(new RitualComponent(new BlockPos(-2, 0, -1), EnumRuneType.AIR));
	}

	@Override
	public Ritual getNewCopy()
	{
		return new RitualAnimalGrowth();
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

	public int getBreedingDecreaseForWill(double vengefulWill)
	{
		return (int) (10 + vengefulWill / 5);
	}

	public int getRefreshTimeForRawWill(double rawWill)
	{
		if (rawWill >= rawWillDrain)
		{
			return (int) Math.max(defaultRefreshTime - rawWill / 10, 1);
		}

		return defaultRefreshTime;
	}

	@Override
	public int getRefreshTime()
	{
		return refreshTime;
	}
}