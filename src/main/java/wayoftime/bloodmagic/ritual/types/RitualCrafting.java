package wayoftime.bloodmagic.ritual.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import wayoftime.bloodmagic.common.item.routing.IItemFilterProvider;
import wayoftime.bloodmagic.common.routing.IItemFilter;
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

	public RitualCrafting()
	{
		super("ritualCrafting", 0, 15000, "ritual." + BloodMagic.MODID + ".craftingRitual");
//		addBlockRange(WATER_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));
//		addBlockRange(WATER_TANK_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));
		addBlockRange(OUTPUT_CHEST_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));
		addBlockRange(INPUT_CHEST_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));
		addBlockRange(RECIPE_CHEST_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 2, 0), 1));
		addBlockRange(OUTPUT_FILTER_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 0, 0), 1));
//
		setMaximumVolumeAndDistanceOfRange(OUTPUT_CHEST_RANGE, 1, 7, 7);
		setMaximumVolumeAndDistanceOfRange(INPUT_CHEST_RANGE, 1, 7, 7);
		setMaximumVolumeAndDistanceOfRange(RECIPE_CHEST_RANGE, 1, 7, 7);
		setMaximumVolumeAndDistanceOfRange(OUTPUT_FILTER_RANGE, 1, 7, 7);
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

		ItemStack filterStack = ItemStack.EMPTY;

		AreaDescriptor recipeDesc = this.getBlockRange(RECIPE_CHEST_RANGE);
		List<ItemFrame> itemFrames = level.getEntitiesOfClass(ItemFrame.class, recipeDesc.getAABB(pos));
		if (!itemFrames.isEmpty())
		{
			for (ItemFrame frame : itemFrames)
			{
				ItemStack frameStack = frame.getItem();
//				System.out.println("Frames aren't empty. Found: " + frameStack.getItem());
				if (!frameStack.isEmpty() && frameStack.getItem() instanceof IItemFilterProvider)
				{
					filterStack = frameStack;
				}
			}
		} else
		{
			List<BlockPos> posList = recipeDesc.getContainedPositions(pos);
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
							if (!frameStack.isEmpty() && frameStack.getItem() instanceof IItemFilterProvider)
							{
								filterStack = frameStack;
								break;
							}
						}
					}
				}
			}
		}

		if (filterStack.isEmpty())
		{
			return;
		}

		AreaDescriptor inputDesc = getBlockRange(INPUT_CHEST_RANGE);
		List<BlockPos> inputPosList = inputDesc.getContainedPositions(pos);
		BlockEntity inputTile = null;
		if (!inputPosList.isEmpty())
		{
			inputTile = level.getBlockEntity(inputPosList.get(0));
		}

		if (inputTile == null)
		{
			return;
		}

		IItemHandler inputInv = Utils.getInventory(inputTile, null);
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

//		InventoryWrapper invWrapper = new InventoryWrapper(9);

		List<CraftingRecipe> craftingRecipes = level.getRecipeManager().getRecipesFor(RecipeType.CRAFTING, craftingContainer, level);

		if (craftingRecipes.isEmpty())
		{
//			System.out.println("Crafting recipe is not valid.");
			return;
		}

		CraftingRecipe recipe = craftingRecipes.get(0);

		AreaDescriptor outputFilterDesc = this.getBlockRange(OUTPUT_FILTER_RANGE);
		ItemStack outputFilterStack = ItemStack.EMPTY;
		List<ItemFrame> outputFrames = level.getEntitiesOfClass(ItemFrame.class, outputFilterDesc.getAABB(pos));
		if (outputFrames != null)
		{
			for (ItemFrame frame : outputFrames)
			{
				ItemStack frameStack = frame.getItem();
				if (!frameStack.isEmpty() && frameStack.getItem() instanceof IItemFilterProvider)
				{
					outputFilterStack = frameStack;
				}
			}
		} else
		{
			List<BlockPos> posList = outputFilterDesc.getContainedPositions(pos);
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
							if (!frameStack.isEmpty() && frameStack.getItem() instanceof IItemFilterProvider)
							{
								outputFilterStack = frameStack;
								break;
							}
						}
					}
				}
			}
		}

		boolean doLimit = false;
		int craftLimit = 0;

		if (!outputFilterStack.isEmpty())
		{
			IItemFilter outputFilter = ((IItemFilterProvider) outputFilterStack.getItem()).getUninitializedItemFilter(outputFilterStack);

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
//			outputFilter.
		}

//		System.out.println("Craft limit: " + craftLimit);

//		System.out.println("Valid recipe! Result is: " + recipe.assemble(craftingContainer));

		// TODO: Eventually select recipe based on output filter...
		IItemFilter itemRecipeFilter = ((IItemFilterProvider) filterStack.getItem()).getUninitializedItemFilter(filterStack);

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

			for (int invSlot = 0; invSlot < inputInv.getSlots(); invSlot++)
			{
				ItemStack invStack = inputInv.getStackInSlot(invSlot);
				int claimedAmount = selectedMap.getOrDefault(invSlot, 0);
				if (invStack.getCount() < (claimedAmount + 1))
				{
					continue;
				}

				IFilterKey filterKey = filterKeyList.get(filterKeyIndex);
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

		AreaDescriptor outputDesc = getBlockRange(OUTPUT_CHEST_RANGE);
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
		}

//		
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
}
