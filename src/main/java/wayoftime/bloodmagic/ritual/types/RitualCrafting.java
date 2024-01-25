package wayoftime.bloodmagic.ritual.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.common.item.inventory.InventoryFilter;
import wayoftime.bloodmagic.common.item.routing.IFilterKey;
import wayoftime.bloodmagic.common.item.routing.IRoutingFilterProvider;
import wayoftime.bloodmagic.common.item.routing.ItemItemRouterFilter;
import wayoftime.bloodmagic.common.routing.IRoutingFilter;
import wayoftime.bloodmagic.common.tile.TileAlchemyTable;
import wayoftime.bloodmagic.common.tile.TileSoulForge;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.impl.BloodMagicAPI;
import wayoftime.bloodmagic.recipe.RecipeAlchemyTable;
import wayoftime.bloodmagic.recipe.RecipeTartaricForge;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.IMasterRitualStone;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;
import wayoftime.bloodmagic.ritual.RitualRegister;
import wayoftime.bloodmagic.util.Utils;

@RitualRegister("crafting")
public class RitualCrafting extends Ritual
{
	public static final String OUTPUT_CHEST_RANGE = "outputRange";
	public static final String INPUT_CHEST_RANGE = "inputRange";

	public static final String RECIPE_CHEST_RANGE = "recipeRange";
	public static final String OUTPUT_FILTER_RANGE = "outputFilterRange";

	public static final String HELLFORGED_RANGE = "hellforgedRange";

	public static final int CRAFTING_MODE = 0;
	public static final int HELLFORGE_MODE = 1;
	public static final int ALCHEMY_TABLE_MODE = 2;

	public static final double MAX_STEADFAST_DRAIN = 0.1;
	public static final double steadfastWillDrain = 0.01;

	public static final double MAX_CORROSIVE_DRAIN = 0.1;
	public static final double corrosiveWillDrain = 0.01;

	public RitualCrafting()
	{
		super("ritualCrafting", 0, 15000, "ritual." + BloodMagic.MODID + ".craftingRitual");
//		addBlockRange(WATER_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));
//		addBlockRange(WATER_TANK_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));
		addBlockRange(OUTPUT_CHEST_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));
		addBlockRange(INPUT_CHEST_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));
		addBlockRange(RECIPE_CHEST_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 2, 0), 1));
		addBlockRange(OUTPUT_FILTER_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 0, 0), 1));
		addBlockRange(HELLFORGED_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 5, 0), 1));
//
		setMaximumVolumeAndDistanceOfRange(OUTPUT_CHEST_RANGE, 1, 7, 7);
		setMaximumVolumeAndDistanceOfRange(INPUT_CHEST_RANGE, 1, 7, 7);
		setMaximumVolumeAndDistanceOfRange(RECIPE_CHEST_RANGE, 1, 7, 7);
		setMaximumVolumeAndDistanceOfRange(OUTPUT_FILTER_RANGE, 1, 7, 7);
		setMaximumVolumeAndDistanceOfRange(HELLFORGED_RANGE, 1, 7, 7);
	}

	@Override
	public void performRitual(IMasterRitualStone masterRitualStone)
	{

//		System.out.println(desc);
		Level level = masterRitualStone.getWorldObj();
		int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

		if (currentEssence < getRefreshCost())
		{
			masterRitualStone.getOwnerNetwork().causeNausea();
			return;
		}

		BlockPos pos = masterRitualStone.getMasterBlockPos();
		List<EnumDemonWillType> willConfig = masterRitualStone.getActiveWillConfig();

		int mode = CRAFTING_MODE;

		if (willConfig.contains(EnumDemonWillType.STEADFAST))
		{
			double steadfastWill = this.getWillRespectingConfig(level, pos, EnumDemonWillType.STEADFAST, willConfig);

			mode = HELLFORGE_MODE;
			if (steadfastWill < MAX_STEADFAST_DRAIN)
			{
				return;
			}
		} else if (willConfig.contains(EnumDemonWillType.CORROSIVE))
		{
			double corrosiveWill = this.getWillRespectingConfig(level, pos, EnumDemonWillType.CORROSIVE, willConfig);

			mode = ALCHEMY_TABLE_MODE;
			if (corrosiveWill < MAX_CORROSIVE_DRAIN)
			{
				return;
			}
		}

		if (mode == CRAFTING_MODE)
		{
			attemptVanillaCrafting(masterRitualStone, level, pos);
		}

		if (mode == HELLFORGE_MODE)
		{
			AreaDescriptor forgeDesc = masterRitualStone.getBlockRange(HELLFORGED_RANGE);
//			System.out.println("Desc: " + forgeDesc.getAABB(pos));
			List<BlockPos> forgePosList = forgeDesc.getContainedPositions(pos);
			if (!forgePosList.isEmpty())
			{
//				System.out.println("Pos: " + forgePosList.get(0));
				BlockEntity forgeTile = level.getBlockEntity(forgePosList.get(0));
				if (forgeTile instanceof TileSoulForge)
				{
//					System.out.println("Have a forge");
					int operations = attemptHellforgedCrafting(masterRitualStone, level, pos, (TileSoulForge) forgeTile);
					if (operations > 0)
					{
						double steadfastDrained = Math.min(operations * steadfastWillDrain, MAX_STEADFAST_DRAIN);
						WorldDemonWillHandler.drainWill(level, pos, EnumDemonWillType.STEADFAST, steadfastDrained, true);
					}
				}
			}
		}

		if (mode == ALCHEMY_TABLE_MODE)
		{
			AreaDescriptor forgeDesc = masterRitualStone.getBlockRange(HELLFORGED_RANGE);
//			System.out.println("Desc: " + forgeDesc.getAABB(pos));
			List<BlockPos> forgePosList = forgeDesc.getContainedPositions(pos);
			if (!forgePosList.isEmpty())
			{
//				System.out.println("Pos: " + forgePosList.get(0));
				BlockEntity alchemyTile = level.getBlockEntity(forgePosList.get(0));
				if (alchemyTile instanceof TileAlchemyTable)
				{
					if (((TileAlchemyTable) alchemyTile).isSlave())
					{
						BlockEntity tile = level.getBlockEntity(((TileAlchemyTable) alchemyTile).getConnectedPos());
						if (tile instanceof TileAlchemyTable && !((TileAlchemyTable) tile).isSlave)
						{
							alchemyTile = tile;
						} else
						{
							return;
						}
					}

//					System.out.println("Have a table!");
					int operations = attemptAlchemyTableCrafting(masterRitualStone, level, pos, (TileAlchemyTable) alchemyTile);
					if (operations > 0)
					{
						double corrosiveDrained = Math.min(operations * corrosiveWillDrain, MAX_CORROSIVE_DRAIN);
						WorldDemonWillHandler.drainWill(level, pos, EnumDemonWillType.CORROSIVE, corrosiveDrained, true);
					}
				}
			}
		}
	}

	public int attemptAlchemyTableCrafting(IMasterRitualStone masterRitualStone, Level level, BlockPos pos, TileAlchemyTable alchemyTable)
	{
		ItemStack filterStack = getRecipeFilterStack(masterRitualStone, level, pos);
		if (filterStack.isEmpty())
		{
			return 0;
		}

		IItemHandler inputInv = this.getInputInventory(masterRitualStone, level, pos);
		if (inputInv == null)
		{
			return 0;
		}

		InventoryFilter filterInv = new InventoryFilter(filterStack);

		List<ItemStack> inputList = new ArrayList<>(4);
		int slotsFilled = 0;

		for (int i = 0; i < filterInv.getContainerSize(); i++)
		{
			ItemStack containedStack = filterInv.getItem(i);
			if (!containedStack.isEmpty())
			{
				inputList.add(containedStack);
				if (slotsFilled >= 4)
				{
					break;
				}
			}
		}

		RecipeAlchemyTable recipe = BloodMagicAPI.INSTANCE.getRecipeRegistrar().getAlchemyTable(level, inputList);

		if (recipe == null)
		{
			return 0;
		}

		int operations = 0;

		// Need to take all of the "junk" before seeing if we can craft.

		AreaDescriptor outputDesc = masterRitualStone.getBlockRange(OUTPUT_CHEST_RANGE);
		List<BlockPos> outputPosList = outputDesc.getContainedPositions(pos);
		BlockEntity outputTile = null;
		IItemHandler outputInv = null;
		BlockPos outputPos = pos;

		if (!outputPosList.isEmpty())
		{
			outputPos = outputPosList.get(0);
			outputTile = level.getBlockEntity(outputPos);
		} else
		{
			outputPos = pos.above(2);
		}

		if (outputTile != null)
		{
			outputInv = Utils.getInventory(outputTile, null);
		}

		List<ItemStack> insertionList = new ArrayList<>();
//		insertionList.add(resultStack);

		IRoutingFilter<ItemStack> itemRecipeFilter = ((IRoutingFilterProvider) filterStack.getItem()).getUninitializedItemFilter(filterStack);
		List<IFilterKey> filterKeyList = itemRecipeFilter.getFilterList();
//		int filterKeyIndex = 0;

		for (int slot = 0; slot < 6; slot++)
		{
			boolean hasKey = filterKeyList.size() > slot;

			ItemStack containedStack = alchemyTable.getItem(slot);

			if (containedStack.isEmpty())
			{
				continue;
			}

			if (hasKey)
			{
				IFilterKey key = filterKeyList.get(slot);
				if (!key.doesStackMatch(containedStack))
				{
					insertionList.add(alchemyTable.removeItem(slot, containedStack.getCount()));
					operations++;
				}
			} else
			{
				insertionList.add(alchemyTable.removeItem(slot, containedStack.getCount()));
				operations++;
			}
		}

		ItemStack outputTableStack = alchemyTable.getItem(TileAlchemyTable.outputSlot);
		if (!outputTableStack.isEmpty())
		{
			insertionList.add(alchemyTable.removeItem(TileAlchemyTable.outputSlot, outputTableStack.getCount()));
		}

		ItemStack outputFilterStack = getOutputFilterStack(masterRitualStone, level, pos);
		ItemStack recipeOutputStack = recipe.getOutput();

		boolean doLimit = false;
		int craftLimit = 0;

		if (!outputFilterStack.isEmpty())
		{
			IRoutingFilter<ItemStack> outputFilter = ((IRoutingFilterProvider) outputFilterStack.getItem()).getUninitializedItemFilter(outputFilterStack);

			List<IFilterKey> keyList = outputFilter.getFilterList();
			for (IFilterKey outputKey : keyList)
			{
				int count = outputKey.getCount();
				if (count == 0)
				{
					doLimit = false;
					break;
				}

				doLimit = true;

				craftLimit += Math.abs(count);
			}

			if (doLimit)
			{
				craftLimit += (recipeOutputStack.getCount() - 2);
			}
		}

		boolean doCraft = true;
		if (outputInv != null)
		{
			doCraft = false;
			if (Utils.canInsertStackFullyIntoInventory(recipeOutputStack, outputInv, doLimit, craftLimit))
			{
				doCraft = true;
			}
		}

		if (outputInv != null)
		{
			for (ItemStack insertedStack : insertionList)
			{
				ItemStack remainder = Utils.insertStackIntoTile(insertedStack, outputInv, true);
				if (!remainder.isEmpty())
				{
					Utils.spawnStackAtBlock(level, outputPos, Direction.UP, remainder);
				}
			}
		} else
		{
			for (ItemStack insertedStack : insertionList)
			{
				Utils.spawnStackAtBlock(level, outputPos, Direction.UP, insertedStack);
			}
		}

		if (!doCraft)
		{
			return operations;
		}
//		if(!Utils.canInsertStackFullyIntoInventory(recipeOutputStack, itemHandler, fillToLimit, limit))

		// Map that stores the slots that are going to be syphoned from.
//		Map<Integer, Integer> selectedMap = new HashMap<>();

		int tableSlot = 0;
		for (IFilterKey filterKey : filterKeyList)
		{
			ItemStack forgeStack = alchemyTable.getItem(tableSlot);
			if (!forgeStack.isEmpty())
			{
				tableSlot++;
				continue;
			}

			boolean hasSucceeded = false;

			for (int invSlot = 0; invSlot < inputInv.getSlots(); invSlot++)
			{
				ItemStack invStack = inputInv.getStackInSlot(invSlot);

				if (filterKey.doesStackMatch(invStack))
				{
					ItemStack extractedStack = inputInv.extractItem(invSlot, 1, false);
					if (extractedStack.isEmpty())
					{
						continue;
					}

					alchemyTable.setItem(tableSlot, extractedStack);

					operations++;
					hasSucceeded = true;
					break;
				}

			}

			tableSlot++;

			if (!hasSucceeded)
			{
				return operations;
			}
		}

		return operations;
	}

	public int attemptHellforgedCrafting(IMasterRitualStone masterRitualStone, Level level, BlockPos pos, TileSoulForge soulForge)
	{
		ItemStack filterStack = getRecipeFilterStack(masterRitualStone, level, pos);
		if (filterStack.isEmpty())
		{
			return 0;
		}

		IItemHandler inputInv = this.getInputInventory(masterRitualStone, level, pos);
		if (inputInv == null)
		{
			return 0;
		}

		InventoryFilter filterInv = new InventoryFilter(filterStack);

		List<ItemStack> inputList = new ArrayList<>(4);
		int slotsFilled = 0;

		for (int i = 0; i < filterInv.getContainerSize(); i++)
		{
			ItemStack containedStack = filterInv.getItem(i);
			if (!containedStack.isEmpty())
			{
				inputList.add(containedStack);
				if (slotsFilled >= 4)
				{
					break;
				}
			}
		}

		RecipeTartaricForge recipe = BloodMagicAPI.INSTANCE.getRecipeRegistrar().getTartaricForge(level, inputList);

		if (recipe == null)
		{
			return 0;
		}

		int operations = 0;

		// Need to take all of the "junk" before seeing if we can craft.

		AreaDescriptor outputDesc = masterRitualStone.getBlockRange(OUTPUT_CHEST_RANGE);
		List<BlockPos> outputPosList = outputDesc.getContainedPositions(pos);
		BlockEntity outputTile = null;
		IItemHandler outputInv = null;
		BlockPos outputPos = pos;

		if (!outputPosList.isEmpty())
		{
			outputPos = outputPosList.get(0);
			outputTile = level.getBlockEntity(outputPos);
		} else
		{
			outputPos = pos.above(2);
		}

		if (outputTile != null)
		{
			outputInv = Utils.getInventory(outputTile, null);
		}

		List<ItemStack> insertionList = new ArrayList<>();
//		insertionList.add(resultStack);

		IRoutingFilter<ItemStack> itemRecipeFilter = ((IRoutingFilterProvider) filterStack.getItem()).getUninitializedItemFilter(filterStack);
		List<IFilterKey> filterKeyList = itemRecipeFilter.getFilterList();
//		int filterKeyIndex = 0;

		for (int slot = 0; slot < 4; slot++)
		{
			boolean hasKey = filterKeyList.size() > slot;

			ItemStack containedStack = soulForge.getItem(slot);

			if (containedStack.isEmpty())
			{
				continue;
			}

			if (hasKey)
			{
				IFilterKey key = filterKeyList.get(slot);
				if (!key.doesStackMatch(containedStack))
				{
					insertionList.add(soulForge.removeItem(slot, containedStack.getCount()));
					operations++;
				}
			} else
			{
				insertionList.add(soulForge.removeItem(slot, containedStack.getCount()));
				operations++;
			}
		}

		ItemStack outputForgeStack = soulForge.getItem(TileSoulForge.outputSlot);
		if (!outputForgeStack.isEmpty())
		{
			insertionList.add(soulForge.removeItem(TileSoulForge.outputSlot, outputForgeStack.getCount()));
		}

		ItemStack outputFilterStack = getOutputFilterStack(masterRitualStone, level, pos);
		ItemStack recipeOutputStack = recipe.getOutput();

		boolean doLimit = false;
		int craftLimit = 0;

		if (!outputFilterStack.isEmpty())
		{
			IRoutingFilter<ItemStack> outputFilter = ((IRoutingFilterProvider) outputFilterStack.getItem()).getUninitializedItemFilter(outputFilterStack);

			List<IFilterKey> keyList = outputFilter.getFilterList();
			for (IFilterKey outputKey : keyList)
			{
				int count = outputKey.getCount();
				if (count == 0)
				{
					doLimit = false;
					break;
				}

				doLimit = true;

				craftLimit += Math.abs(count);
			}

			if (doLimit)
			{
				craftLimit += (recipeOutputStack.getCount() - 2);
			}
		}

		boolean doCraft = true;
		if (outputInv != null)
		{
			doCraft = false;
			if (Utils.canInsertStackFullyIntoInventory(recipeOutputStack, outputInv, doLimit, craftLimit))
			{
				doCraft = true;
			}
		}

		if (outputInv != null)
		{
			for (ItemStack insertedStack : insertionList)
			{
				ItemStack remainder = Utils.insertStackIntoTile(insertedStack, outputInv, true);
				if (!remainder.isEmpty())
				{
					Utils.spawnStackAtBlock(level, outputPos, Direction.UP, remainder);
				}
			}
		} else
		{
			for (ItemStack insertedStack : insertionList)
			{
				Utils.spawnStackAtBlock(level, outputPos, Direction.UP, insertedStack);
			}
		}

		if (!doCraft)
		{
			return operations;
		}
//		if(!Utils.canInsertStackFullyIntoInventory(recipeOutputStack, itemHandler, fillToLimit, limit))

		// Map that stores the slots that are going to be syphoned from.
//		Map<Integer, Integer> selectedMap = new HashMap<>();

		int forgeSlot = 0;
		for (IFilterKey filterKey : filterKeyList)
		{
			ItemStack forgeStack = soulForge.getItem(forgeSlot);
			if (!forgeStack.isEmpty())
			{
				forgeSlot++;
				continue;
			}

			boolean hasSucceeded = false;

			for (int invSlot = 0; invSlot < inputInv.getSlots(); invSlot++)
			{
				ItemStack invStack = inputInv.getStackInSlot(invSlot);

				if (filterKey.doesStackMatch(invStack))
				{
					ItemStack extractedStack = inputInv.extractItem(invSlot, 1, false);
					if (extractedStack.isEmpty())
					{
						continue;
					}

					soulForge.setItem(forgeSlot, extractedStack);

					operations++;
					hasSucceeded = true;
					break;
				}

			}

			forgeSlot++;

			if (!hasSucceeded)
			{
				return operations;
			}
		}

		return operations;
	}

	public void attemptVanillaCrafting(IMasterRitualStone masterRitualStone, Level level, BlockPos pos)
	{
		ItemStack filterStack = getRecipeFilterStack(masterRitualStone, level, pos);
		if (filterStack.isEmpty())
		{
			return;
		}

		IItemHandler inputInv = this.getInputInventory(masterRitualStone, level, pos);
		if (inputInv == null)
		{
			return;
		}

		InventoryFilter filterInv = new InventoryFilter(filterStack);
		CraftingContainer craftingContainer = makeContainer();

		for (int i = 0; i < filterInv.getContainerSize(); i++)
		{
			craftingContainer.setItem(i, filterInv.getItem(i));
		}

		List<CraftingRecipe> craftingRecipes = level.getRecipeManager().getRecipesFor(RecipeType.CRAFTING, craftingContainer, level);

		if (craftingRecipes.isEmpty())
		{
//			System.out.println("Crafting recipe is not valid.");
			return;
		}

		CraftingRecipe recipe = craftingRecipes.get(0);

		ItemStack outputFilterStack = getOutputFilterStack(masterRitualStone, level, pos);

		boolean doLimit = false;
		int craftLimit = 0;

		if (!outputFilterStack.isEmpty())
		{
			IRoutingFilter<ItemStack> outputFilter = ((IRoutingFilterProvider) outputFilterStack.getItem()).getUninitializedItemFilter(outputFilterStack);

			if (craftingRecipes.size() > 1)
			{
				boolean foundRecipe = false;
				for (CraftingRecipe testRecipe : craftingRecipes)
				{
					ItemStack resultStack = testRecipe.getResultItem();
					if (outputFilter.doesStackPassFilter(resultStack))
					{
						recipe = testRecipe;
						foundRecipe = true;
						break;
					}
				}

				if (!foundRecipe)
				{
					return;
				}
			}

			List<IFilterKey> keyList = outputFilter.getFilterList();
			for (IFilterKey outputKey : keyList)
			{
				int count = outputKey.getCount();
				if (count == 0)
				{
					doLimit = false;
					break;
				}

				doLimit = true;

				craftLimit += Math.abs(count);
			}

			if (doLimit)
			{
				craftLimit += (recipe.getResultItem().getCount() - 1);
			}
		}

		IRoutingFilter<ItemStack> itemRecipeFilter = ((IRoutingFilterProvider) filterStack.getItem()).getUninitializedItemFilter(filterStack);

		// Map that stores the slots that are going to be syphoned from.
		Map<Integer, Integer> selectedMap = new HashMap<>();
		List<IFilterKey> filterKeyList = itemRecipeFilter.getFilterList();
		int filterKeyIndex = 0;

		for (int filterSlot = 0; filterSlot < Math.min(filterInv.getContainerSize(), 9); filterSlot++)
		{
			ItemStack filterKeyStack = filterInv.getItem(filterSlot);
			if (filterKeyStack.isEmpty())
			{
				continue;
			}

			if (filterKeyIndex >= filterKeyList.size())
			{
				// Broken filter!
				return;
			}

			boolean hasSucceeded = false;
			IFilterKey filterKey = filterKeyList.get(filterKeyIndex);

			for (int invSlot = 0; invSlot < inputInv.getSlots(); invSlot++)
			{
				ItemStack invStack = inputInv.getStackInSlot(invSlot);
				int claimedAmount = selectedMap.getOrDefault(invSlot, 0);
				if (invStack.getCount() < (claimedAmount + 1))
				{
					continue;
				}

				if (filterKey.doesStackMatch(invStack))
				{
					ItemStack extractedStack = inputInv.extractItem(invSlot, 1, true);
					if (extractedStack.isEmpty())
					{
						continue;
					}

					selectedMap.put(invSlot, 1 + claimedAmount);
					craftingContainer.setItem(filterSlot, extractedStack);

					hasSucceeded = true;
					break;
				}
			}

			filterKeyIndex++;

			if (!hasSucceeded)
			{
				// Didn't manage to find an available input item!
//				System.out.println("Didn't find the item");
				return;
			}
		}

		AreaDescriptor outputDesc = masterRitualStone.getBlockRange(OUTPUT_CHEST_RANGE);
		List<BlockPos> outputPosList = outputDesc.getContainedPositions(pos);
		BlockEntity outputTile = null;
		IItemHandler outputInv = null;
		BlockPos outputPos = pos;

		if (!outputPosList.isEmpty())
		{
			outputPos = outputPosList.get(0);
			outputTile = level.getBlockEntity(outputPos);
		} else
		{
			outputPos = pos.above(2);
		}

		if (outputTile != null)
		{
			outputInv = Utils.getInventory(outputTile, null);
		}

		ItemStack resultStack = recipe.assemble(craftingContainer);

		boolean doCraft = true;
		if (outputInv != null)
		{
			doCraft = false;
			if (Utils.canInsertStackFullyIntoInventory(resultStack, outputInv, doLimit, craftLimit))
			{
				doCraft = true;
			}
		}

		if (doCraft)
		{
			List<ItemStack> insertionList = new ArrayList<>();
			insertionList.add(resultStack);

			for (Entry<Integer, Integer> syphonEntry : selectedMap.entrySet())
			{
				int slot = syphonEntry.getKey();
				int syphonAmount = syphonEntry.getValue();
				ItemStack syphonStack = inputInv.extractItem(slot, syphonAmount, false);
				if (syphonStack.hasContainerItem())
				{
					ItemStack containedStack = syphonStack.getContainerItem();
					if (inputInv.isItemValid(slot, containedStack))
					{
						ItemStack remainderStack = inputInv.insertItem(slot, containedStack, false);
						if (!remainderStack.isEmpty())
						{
							insertionList.add(remainderStack);
						}
					} else
					{
						insertionList.add(containedStack);
					}
				}
			}

			if (outputInv != null)
			{
				for (ItemStack insertedStack : insertionList)
				{
					ItemStack remainder = Utils.insertStackIntoTile(insertedStack, outputInv, true);
					if (!remainder.isEmpty())
					{
						Utils.spawnStackAtBlock(level, outputPos, Direction.UP, remainder);
					}
				}
			} else
			{
				for (ItemStack insertedStack : insertionList)
				{
					Utils.spawnStackAtBlock(level, outputPos, Direction.UP, insertedStack);
				}
			}

			masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost()));

//			return insertionList;
		}
	}

	public ItemStack getRecipeFilterStack(IMasterRitualStone masterRitualStone, Level level, BlockPos masterPos)
	{
		ItemStack filterStack = ItemStack.EMPTY;

		AreaDescriptor recipeDesc = masterRitualStone.getBlockRange(RECIPE_CHEST_RANGE);
		List<ItemFrame> itemFrames = level.getEntitiesOfClass(ItemFrame.class, recipeDesc.getAABB(masterPos));
		if (!itemFrames.isEmpty())
		{
			for (ItemFrame frame : itemFrames)
			{
				ItemStack frameStack = frame.getItem();
//				System.out.println("Frames aren't empty. Found: " + frameStack.getItem());
				if (!frameStack.isEmpty() && (ItemItemRouterFilter) frameStack.getItem() instanceof IRoutingFilterProvider)
				{
					filterStack = frameStack;
				}
			}
		} else
		{
			List<BlockPos> posList = recipeDesc.getContainedPositions(masterPos);
			if (!posList.isEmpty())
			{
				BlockEntity tile = level.getBlockEntity(posList.get(0));

				if (tile != null)
				{
					IItemHandler inv = Utils.getInventory(tile, null);
					if (inv != null)
					{
						for (int i = 0; i < inv.getSlots(); i++)
						{
							ItemStack frameStack = inv.getStackInSlot(i);
							if (!frameStack.isEmpty() && (ItemItemRouterFilter) frameStack.getItem() instanceof IRoutingFilterProvider)
							{
								filterStack = frameStack;
								break;
							}
						}
					}
				}
			}
		}

		return filterStack;
	}

	public IItemHandler getInputInventory(IMasterRitualStone masterRitualStone, Level level, BlockPos masterPos)
	{
		AreaDescriptor inputDesc = masterRitualStone.getBlockRange(INPUT_CHEST_RANGE);
		List<BlockPos> inputPosList = inputDesc.getContainedPositions(masterPos);
		BlockEntity inputTile = null;
		if (!inputPosList.isEmpty())
		{
			inputTile = level.getBlockEntity(inputPosList.get(0));
		}

		if (inputTile == null)
		{
			return null;
		}

		return Utils.getInventory(inputTile, null);
	}

	public ItemStack getOutputFilterStack(IMasterRitualStone masterRitualStone, Level level, BlockPos masterPos)
	{
		AreaDescriptor outputFilterDesc = masterRitualStone.getBlockRange(OUTPUT_FILTER_RANGE);
		ItemStack outputFilterStack = ItemStack.EMPTY;
		List<ItemFrame> outputFrames = level.getEntitiesOfClass(ItemFrame.class, outputFilterDesc.getAABB(masterPos));
		if (outputFrames != null)
		{
			for (ItemFrame frame : outputFrames)
			{
				ItemStack frameStack = frame.getItem();
				if (!frameStack.isEmpty() && (ItemItemRouterFilter) frameStack.getItem() instanceof IRoutingFilterProvider)
				{
					outputFilterStack = frameStack;
				}
			}
		} else
		{
			List<BlockPos> posList = outputFilterDesc.getContainedPositions(masterPos);
			if (!posList.isEmpty())
			{
				BlockEntity tile = level.getBlockEntity(posList.get(0));

				if (tile != null)
				{
					IItemHandler inv = Utils.getInventory(tile, null);
					if (inv != null)
					{
						for (int i = 0; i < inv.getSlots(); i++)
						{
							ItemStack frameStack = inv.getStackInSlot(i);
							if (!frameStack.isEmpty() && (ItemItemRouterFilter) frameStack.getItem() instanceof IRoutingFilterProvider)
							{
								outputFilterStack = frameStack;
								break;
							}
						}
					}
				}
			}
		}

		return outputFilterStack;
	}

	private static CraftingContainer makeContainer()
	{
		CraftingContainer craftingcontainer = new CraftingContainer(new AbstractContainerMenu((MenuType) null, -1)
		{
			public boolean stillValid(Player p_29888_)
			{
				return false;
			}
		}, 3, 3);

		return craftingcontainer;
	}

	@Override
	public int getRefreshTime()
	{
		return 4;
	}

	@Override
	public int getRefreshCost()
	{
		return 10;
	}

	@Override
	public void gatherComponents(Consumer<RitualComponent> components)
	{
		addCornerRunes(components, 1, 1, EnumRuneType.EARTH);

		addOffsetRunes(components, 1, 2, -1, EnumRuneType.FIRE);
		addCornerRunes(components, 1, -1, EnumRuneType.FIRE);
		addRune(components, -1, -1, 0, EnumRuneType.EARTH);
		addRune(components, 1, -1, 0, EnumRuneType.EARTH);
		addRune(components, 0, -1, -1, EnumRuneType.EARTH);
		addRune(components, 0, -1, 1, EnumRuneType.WATER);
		addRune(components, -1, 1, 0, EnumRuneType.EARTH);
		addRune(components, 1, 1, 0, EnumRuneType.EARTH);
		addRune(components, 0, 1, -1, EnumRuneType.EARTH);
		addRune(components, 0, 0, 1, EnumRuneType.EARTH);

		addRune(components, 0, 2, -1, EnumRuneType.DUSK);

	}

//	@Override
//	public void gatherComponents(Consumer<RitualComponent> components)
//	{
//		addCornerRunes(components, 1, 0, EnumRuneType.WATER);
//	}

	@Override
	public Ritual getNewCopy()
	{
		return new RitualCrafting();
	}

	@Override
	public Component[] provideInformationOfRitualToPlayer(Player player)
	{
		return new Component[] { new TranslatableComponent(this.getTranslationKey() + ".info"),
				new TranslatableComponent(this.getTranslationKey() + ".steadfast.info"),
				new TranslatableComponent(this.getTranslationKey() + ".corrosive.info") };
	}
}
