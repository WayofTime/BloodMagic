package wayoftime.bloodmagic.ritual.types;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.items.IItemHandler;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.potion.BloodMagicPotions;
import wayoftime.bloodmagic.ritual.*;
import wayoftime.bloodmagic.util.Utils;

import java.util.List;
import java.util.function.Consumer;

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
		Level world = masterRitualStone.getWorldObj();
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
		BlockEntity chest = world.getBlockEntity(chestRange.getContainedPositions(pos).get(0));
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
		AABB axis = growingRange.getAABB(masterRitualStone.getMasterBlockPos());
		List<Animal> animalList = world.getEntitiesOfClass(Animal.class, axis);

		boolean performedEffect = false;

		for (Animal animal : animalList)
		{
			if (animal.getAge() < 0)
			{
				animal.ageUp(5);
				totalGrowths++;
				performedEffect = true;
			} else if (animal.getAge() > 0)
			{
				if (decreaseBreedTimer)
				{
					if (vengefulWill >= vengefulWillDrain)
					{
						animal.setAge(Math.max(0, animal.getAge() - getBreedingDecreaseForWill(vengefulWill)));
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
						if (!animal.hasEffect(BloodMagicPotions.SACRIFICIAL_LAMB.get()))
						{
							animal.addEffect(new MobEffectInstance(BloodMagicPotions.SACRIFICIAL_LAMB.get(), 1200));
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
								if (foodStack != null && animal.isFood(foodStack) && itemHandler.extractItem(slot, 1, true) != null)
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
	public Component[] provideInformationOfRitualToPlayer(Player player)
	{
		return new Component[] { Component.translatable(this.getTranslationKey() + ".info"),
				Component.translatable(this.getTranslationKey() + ".default.info"),
				Component.translatable(this.getTranslationKey() + ".corrosive.info"),
				Component.translatable(this.getTranslationKey() + ".steadfast.info"),
				Component.translatable(this.getTranslationKey() + ".destructive.info"),
				Component.translatable(this.getTranslationKey() + ".vengeful.info") };
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