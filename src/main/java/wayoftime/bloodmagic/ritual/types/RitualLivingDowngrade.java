package wayoftime.bloodmagic.ritual.types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.item.ILivingUpgradePointsProvider;
import wayoftime.bloodmagic.common.item.ItemLivingTomeScrap;
import wayoftime.bloodmagic.core.LivingArmorRegistrar;
import wayoftime.bloodmagic.core.living.LivingStats;
import wayoftime.bloodmagic.core.living.LivingUpgrade;
import wayoftime.bloodmagic.core.living.LivingUtil;
import wayoftime.bloodmagic.impl.BloodMagicAPI;
import wayoftime.bloodmagic.recipe.RecipeLivingDowngrade;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.IMasterRitualStone;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;
import wayoftime.bloodmagic.ritual.RitualRegister;
import wayoftime.bloodmagic.util.Utils;

@RitualRegister("downgrade")
public class RitualLivingDowngrade extends Ritual
{
	public static final String DOWNGRADE_RANGE = "containmentRange";

	public RitualLivingDowngrade()
	{
		super("ritualDowngrade", 0, 10000, "ritual." + BloodMagic.MODID + ".downgradeRitual");
		addBlockRange(DOWNGRADE_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-1, 0, -1), 3));
	}

	@Override
	public void performRitual(IMasterRitualStone masterRitualStone)
	{
		World world = masterRitualStone.getWorldObj();
		BlockPos masterPos = masterRitualStone.getMasterBlockPos();
		Direction direction = masterRitualStone.getDirection();

		PlayerEntity selectedPlayer = null;
		AreaDescriptor downgradeRange = masterRitualStone.getBlockRange(DOWNGRADE_RANGE);

		for (PlayerEntity player : world.getEntitiesWithinAABB(PlayerEntity.class, downgradeRange.getAABB(masterRitualStone.getMasterBlockPos())))
		{
			if (!player.isCrouching() && LivingUtil.hasFullSet(player))
			{
				selectedPlayer = player;
				break;
			}
		}

		if (selectedPlayer == null)
		{

			return;
		}

		LivingStats playerStats = LivingStats.fromPlayer(selectedPlayer, true);

//		System.out.println("Found a downgrade!");

		BlockPos chestOffsetPos = new BlockPos(0, 1, 0);
		chestOffsetPos = chestOffsetPos.offset(direction, 2);

		BlockPos chestPos = masterPos.add(chestOffsetPos);

		TileEntity tile = world.getTileEntity(chestPos);

		if (tile == null)
		{
			return;
		}

		// Contains the desired levels for each upgrade.
		Map<LivingUpgrade, Integer> downgradeMap = new HashMap<>();

		int availablePoints = 0;
//		int wantedLevel = 0;
		Direction accessDir = Direction.DOWN;

		Map<Integer, List<Integer>> priorityMap = new HashMap<>();

		LazyOptional<IItemHandler> capability = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, accessDir);
		if (capability.isPresent())
		{
			IItemHandler handler = capability.resolve().get();

			for (int i = 0; i < handler.getSlots(); i++)
			{
				ItemStack invStack = handler.getStackInSlot(i);
				availablePoints += getAvailablePointsFromStack(invStack);
				LivingUpgrade downgrade = getDowngradeFromStack(world, invStack);
				if (downgrade != null && downgrade != LivingUpgrade.DUMMY)
				{
					int wantedLevel = getLevelFromStack(invStack);
					downgradeMap.put(downgrade, downgradeMap.getOrDefault(downgrade, 0) + wantedLevel);
				}

				int priority = getPriorityFromStack(invStack);
				if (priority >= 0)
				{
					if (priorityMap.containsKey(priority))
					{
						priorityMap.get(priority).add(i);
					} else
					{
						ArrayList<Integer> priorityList = new ArrayList<Integer>();
						priorityList.add(i);
						priorityMap.put(priority, priorityList);
					}
				}
			}
		} else if (tile instanceof IInventory)
		{
			for (int i = 0; i < ((IInventory) tile).getSizeInventory(); i++)
			{
				ItemStack invStack = ((IInventory) tile).getStackInSlot(i);
				availablePoints += getAvailablePointsFromStack(invStack);
				LivingUpgrade downgrade = getDowngradeFromStack(world, invStack);
				if (downgrade != null && downgrade != LivingUpgrade.DUMMY)
				{
					int wantedLevel = getLevelFromStack(invStack);
					downgradeMap.put(downgrade, downgradeMap.getOrDefault(downgrade, 0) + wantedLevel);
				}

				int priority = getPriorityFromStack(invStack);
				if (priority >= 0)
				{
					if (priorityMap.containsKey(priority))
					{
						priorityMap.get(priority).add(i);
					} else
					{
						ArrayList<Integer> priorityList = new ArrayList<Integer>();
						priorityList.add(i);
						priorityMap.put(priority, priorityList);
					}
				}
			}
		}

		if (downgradeMap.isEmpty())
		{
			return;
		}

		// TODO: Change when chest logic is implemented.

		// Stores the difference in points between the player's armour and the requested
		// downgrade. 0 means nothing is added.
		Map<LivingUpgrade, Integer> pointDifferentialMap = new HashMap<LivingUpgrade, Integer>();
		int totalDifferentialPoints = 0;
		for (Entry<LivingUpgrade, Integer> entry : downgradeMap.entrySet())
		{
			LivingUpgrade downgrade = entry.getKey();
			int playerDowngradeLevel = playerStats.getLevel(downgrade.getKey());
			int wantedLevel = Math.min(entry.getValue(), downgrade.getLevel(Integer.MAX_VALUE));
			if (playerDowngradeLevel >= wantedLevel)
			{
				continue;
			}

			int playerInitialPoints = 0;
			if (playerDowngradeLevel > 0)
			{
				playerInitialPoints = playerStats.getUpgrades().getOrDefault(downgrade, 0d).intValue();
			}

			int totalRequiredPoints = Math.abs(downgrade.getLevelCost(wantedLevel));
			int upgradeRequiredPoints = totalRequiredPoints - playerInitialPoints;
			if (upgradeRequiredPoints > 0)
			{
				pointDifferentialMap.put(downgrade, upgradeRequiredPoints);
				totalDifferentialPoints += upgradeRequiredPoints;
			}
		}

		if (availablePoints < totalDifferentialPoints || priorityMap.isEmpty() || pointDifferentialMap.isEmpty())
		{
			// Can't upgrade! Not enough points
			// TODO: Add smoke particles to indicate this?

			return;
		}

		List<Integer> slotOrderList = new ArrayList<>();

		List<Integer> priorityList = new ArrayList<>(priorityMap.keySet());

		Collections.sort(priorityList);
		for (int priority : priorityList)
		{
			slotOrderList.addAll(priorityMap.get(priority));
		}

		// Consumption logic
		int requiredPoints = totalDifferentialPoints;
		int initialRequiredPoints = requiredPoints;
//		System.out.println("Initial required points: " + requiredPoints);

		List<ItemStack> excessStackList = new ArrayList<>();

		if (capability.isPresent())
		{
			IItemHandler handler = capability.resolve().get();

			for (int i : slotOrderList)
			{
				ItemStack invStack = handler.getStackInSlot(i);
				if (!invStack.isEmpty() && invStack.getItem() instanceof ILivingUpgradePointsProvider)
				{
					ItemStack simStack = handler.extractItem(i, invStack.getCount(), true);

					int containedPoints = ((ILivingUpgradePointsProvider) simStack.getItem()).getAvailableUpgradePoints(simStack, requiredPoints);
					int drainPoints = Math.min(containedPoints, requiredPoints);
					int remainingPointsInItem = ((ILivingUpgradePointsProvider) simStack.getItem()).getExcessUpgradePoints(simStack, drainPoints);
					ItemStack newItemStack = ((ILivingUpgradePointsProvider) simStack.getItem()).getResultingStack(simStack, drainPoints);

					if (newItemStack.isEmpty() || handler.isItemValid(i, newItemStack))
					{
						// Actual number of points syphoned from the item.
						int totalPoints = ((ILivingUpgradePointsProvider) simStack.getItem()).getTotalUpgradePoints(simStack);
						int totalDrainedPoints = (totalPoints - remainingPointsInItem);
//						System.out.println("Contained points: " + containedPoints + ", remaining points: " + remainingPointsInItem);
						requiredPoints -= totalDrainedPoints;
						handler.extractItem(i, simStack.getCount(), false);
						ItemStack remainingStack = handler.insertItem(i, newItemStack, false);

						if (!remainingStack.isEmpty())
						{
							excessStackList.add(remainingStack);
						}

						if (requiredPoints <= 0)
						{
							break;
						}
					}
				}
			}
		} else if (tile instanceof IInventory)
		{
			for (int i : slotOrderList)
			{
				ItemStack invStack = ((IInventory) tile).getStackInSlot(i);
				if (!invStack.isEmpty() && invStack.getItem() instanceof ILivingUpgradePointsProvider)
				{
					int drainPoints = Math.min(((ILivingUpgradePointsProvider) invStack.getItem()).getAvailableUpgradePoints(invStack, requiredPoints), requiredPoints);
					int remainingPointsInItem = ((ILivingUpgradePointsProvider) invStack.getItem()).getExcessUpgradePoints(invStack, drainPoints);
					ItemStack newItemStack = ((ILivingUpgradePointsProvider) invStack.getItem()).getResultingStack(invStack, drainPoints);

					requiredPoints -= (drainPoints - remainingPointsInItem);
					((IInventory) tile).setInventorySlotContents(i, newItemStack);

					if (requiredPoints <= 0)
					{
						break;
					}
				}
			}
		}

		if (requiredPoints < 0)
		{
			ItemStack scrapStack = new ItemStack(BloodMagicItems.UPGRADE_SCRAPS.get());
			((ItemLivingTomeScrap) BloodMagicItems.UPGRADE_SCRAPS.get()).setTotalUpgradePoints(scrapStack, Math.abs(requiredPoints));
			excessStackList.add(scrapStack);
		}

		// TODO: Apply ALL upgrades.
		if (requiredPoints <= 0)
		{
			for (Entry<LivingUpgrade, Integer> entry : pointDifferentialMap.entrySet())
			{
				LivingUtil.applyExperienceToUpgradeCap(selectedPlayer, entry.getKey(), entry.getValue());
			}

			masterRitualStone.setActive(false);

			LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(world);
			lightningboltentity.setPosition(masterPos.getX() + 0.5, masterPos.getY(), masterPos.getZ() + 0.5);
			world.addEntity(lightningboltentity);
		} else if (requiredPoints < initialRequiredPoints)
		{
			ItemStack scrapStack = new ItemStack(BloodMagicItems.UPGRADE_SCRAPS.get());
			((ItemLivingTomeScrap) BloodMagicItems.UPGRADE_SCRAPS.get()).setTotalUpgradePoints(scrapStack, Math.abs(initialRequiredPoints - requiredPoints));
			excessStackList.add(scrapStack);
		}

		for (ItemStack item : excessStackList)
		{
			ItemStack copyStack = item.copy();

			if (tile != null)
			{
				copyStack = Utils.insertStackIntoTile(copyStack, tile, Direction.DOWN);
			} else
			{
				Utils.spawnStackAtBlock(world, chestPos, Direction.UP, copyStack);
				continue;
			}
			if (!copyStack.isEmpty())
			{
				Utils.spawnStackAtBlock(world, chestPos, Direction.UP, copyStack);
			}
		}
	}

	public int getAvailablePointsFromStack(ItemStack stack)
	{
		if (stack.isEmpty())
		{
			return 0;
		}

		if (stack.getItem() instanceof ILivingUpgradePointsProvider)
		{
			return ((ILivingUpgradePointsProvider) stack.getItem()).getTotalUpgradePoints(stack);
		}

		return 0;
	}

	public int getPriorityFromStack(ItemStack stack)
	{
		if (stack.isEmpty())
		{
			return -1;
		}

		if (stack.getItem() instanceof ILivingUpgradePointsProvider)
		{
			return ((ILivingUpgradePointsProvider) stack.getItem()).getPriority(stack);
		}

		return 0;
	}

	public int getLevelFromStack(ItemStack stack)
	{
		if (stack.isEmpty())
		{
			return 0;
		}

		if (!(stack.getItem() instanceof ILivingUpgradePointsProvider))
		{
			return stack.getCount();
		}

		return 0;
	}

	public LivingUpgrade getDowngradeFromStack(World world, ItemStack focusStack)
	{
		if (focusStack.isEmpty())
		{
			return LivingUpgrade.DUMMY;
		}

		if (!(focusStack.getItem() instanceof ILivingUpgradePointsProvider))
		{
			RecipeLivingDowngrade downgradeRecipe = BloodMagicAPI.INSTANCE.getRecipeRegistrar().getLivingDowngrade(world, focusStack);
			if (downgradeRecipe != null)
			{
				return LivingArmorRegistrar.UPGRADE_MAP.getOrDefault(downgradeRecipe.getLivingArmourResource(), LivingUpgrade.DUMMY);
			}

//			LivingUpgrade downgrade = LivingArmorRegistrar.UPGRADE_MAP.get(downgradeRecipe.getLivingArmourResource());

		}

		return LivingUpgrade.DUMMY;
	}

//	public int consumeAvailablePointsFromStack(ItemStack stack)
//	{
//		if (!stack.isEmpty() && stack.getItem() instanceof ILivingUpgradePointsProvider)
//		{
//			return 0;
//		}
//
//		if ()
//		{
//			return ((ILivingUpgradePointsProvider) stack.getItem()).
//		}
//
//		return 0;
//	}

	public ItemStack getStackFromItemFrame(World world, BlockPos masterPos, Direction direction)
	{
		BlockPos offsetPos = new BlockPos(0, 3, 0);
		offsetPos = offsetPos.offset(direction, 2);

		AxisAlignedBB bb = new AxisAlignedBB(masterPos.add(offsetPos));
		List<ItemFrameEntity> frames = world.getEntitiesWithinAABB(ItemFrameEntity.class, bb);
		for (ItemFrameEntity frame : frames)
		{
			if (!frame.getDisplayedItem().isEmpty())
			{
				return frame.getDisplayedItem();
			}
		}

		return ItemStack.EMPTY;
	}

	@Override
	public int getRefreshCost()
	{
		return 10;// Temporary
	}

	@Override
	public int getRefreshTime()
	{
		return 1;
	}

//    @Override
//    public void readFromNBT(NBTTagCompound tag)
//    {
//        super.readFromNBT(tag);
//        tag
//    }

	@Override
	public void gatherComponents(Consumer<RitualComponent> components)
	{
		addRune(components, 0, 0, -1, EnumRuneType.AIR);
		addRune(components, 0, 0, -2, EnumRuneType.DUSK);
		addRune(components, 0, 1, -3, EnumRuneType.DUSK);
		addRune(components, 0, 2, -3, EnumRuneType.BLANK);
		addRune(components, 0, 3, -3, EnumRuneType.BLANK);
		addRune(components, 0, 1, -4, EnumRuneType.FIRE);

		for (int i = 1; i <= 3; i++)
		{
			addRune(components, 0, 0, i, EnumRuneType.AIR);
		}

		for (int sgn = -1; sgn <= 1; sgn += 2)
		{
			addRune(components, sgn, 0, 4, EnumRuneType.AIR);
			addRune(components, sgn * 2, 0, 2, EnumRuneType.AIR);
			addRune(components, sgn * 3, 0, 2, EnumRuneType.AIR);
			addRune(components, sgn * 3, 0, 3, EnumRuneType.AIR);
			addRune(components, sgn, 0, 0, EnumRuneType.EARTH);
			addRune(components, sgn, 0, 1, EnumRuneType.EARTH);
			addRune(components, sgn * 2, 0, -1, EnumRuneType.FIRE);
			addRune(components, sgn * 2, 0, -2, EnumRuneType.FIRE);
			addRune(components, sgn * 3, 0, -2, EnumRuneType.FIRE);
			addRune(components, sgn * 3, 0, -3, EnumRuneType.FIRE);
			addRune(components, sgn * 3, 0, -4, EnumRuneType.FIRE);
			addRune(components, sgn, 1, -1, EnumRuneType.AIR);
			addRune(components, sgn, 1, -2, EnumRuneType.AIR);
			addRune(components, sgn, 1, -4, EnumRuneType.FIRE);
			addRune(components, sgn * 2, 1, -4, EnumRuneType.FIRE);
			addRune(components, sgn, 0, -3, EnumRuneType.EARTH);
			addRune(components, sgn, 0, -4, EnumRuneType.EARTH);
			addRune(components, sgn, 0, -5, EnumRuneType.EARTH);
			addRune(components, sgn, 1, -5, EnumRuneType.EARTH);
			addRune(components, sgn, 2, -5, EnumRuneType.EARTH);
			addRune(components, sgn, 3, -5, EnumRuneType.EARTH);
			addRune(components, sgn, 3, -4, EnumRuneType.EARTH);
		}
	}

	@Override
	public Ritual getNewCopy()
	{
		return new RitualLivingDowngrade();
	}
}