package wayoftime.bloodmagic.common.item.routing;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.Button.IPressable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import wayoftime.bloodmagic.client.button.FilterButtonTogglePress;
import wayoftime.bloodmagic.common.item.inventory.ContainerFilter;
import wayoftime.bloodmagic.common.item.inventory.InventoryFilter;
import wayoftime.bloodmagic.common.item.inventory.ItemInventory;
import wayoftime.bloodmagic.common.routing.BasicItemFilter;
import wayoftime.bloodmagic.common.routing.BlacklistItemFilter;
import wayoftime.bloodmagic.common.routing.IItemFilter;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.GhostItemHelper;

public class ItemCompositeFilter extends ItemRouterFilter implements INamedContainerProvider, ICompositeItemFilterProvider
{
	public ItemCompositeFilter()
	{
		super();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack filterStack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.compositefilter.desc").mergeStyle(TextFormatting.ITALIC).mergeStyle(TextFormatting.GRAY));

		super.addInformation(filterStack, world, tooltip, flag);
	}

	protected IItemFilter getFilterTypeFromConfig(ItemStack filterStack)
	{
		int state = getCurrentButtonState(filterStack, Constants.BUTTONID.BLACKWHITELIST, 0);
		if (state == 1)
		{
			return new BlacklistItemFilter();
		}

		return new BasicItemFilter();
	}

	@Override
	public IFilterKey getFilterKey(ItemStack filterStack, int slot, ItemStack ghostStack, int amount)
	{
		return null;
	}

	@Override
	public IItemFilter getInputItemFilter(ItemStack filterStack, TileEntity tile, IItemHandler handler)
	{
		IItemFilter testFilter = getFilterTypeFromConfig(filterStack);

		List<IFilterKey> filteredList = new ArrayList<>();
		ItemInventory inv = new InventoryFilter(filterStack);

		List<ItemStack> nestedList = getNestedFilters(filterStack);
		for (int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if (stack.isEmpty())
			{
				continue;
			}

			int amount = GhostItemHelper.getItemGhostAmount(stack);
			ItemStack ghostStack = GhostItemHelper.getSingleStackFromGhost(stack);

			IFilterKey mainKey = getFilterKey(filterStack, i, ghostStack, amount);

			if (nestedList.size() > 0)
			{
				CompositeFilterKey compositeKey = new CompositeFilterKey(amount);
				if (mainKey != null)
				{
					compositeKey.addFilterKey(mainKey);
				}

				for (ItemStack nestedStack : nestedList)
				{
					compositeKey.addFilterKey(((INestableItemFilterProvider) nestedStack.getItem()).getFilterKey(filterStack, i, ghostStack, amount));
				}

				filteredList.add(compositeKey);
			} else if (mainKey != null)
			{
				filteredList.add(mainKey);
			}
		}

		testFilter.initializeFilter(filteredList, tile, handler, false);
		return testFilter;
	}

	@Override
	public IItemFilter getOutputItemFilter(ItemStack filterStack, TileEntity tile, IItemHandler handler)
	{
		IItemFilter testFilter = getFilterTypeFromConfig(filterStack);

		List<IFilterKey> filteredList = new ArrayList<>();
		ItemInventory inv = new InventoryFilter(filterStack); // TODO: Change to grab the filter from the Item

		List<ItemStack> nestedList = getNestedFilters(filterStack);
		// later.
		for (int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if (stack.isEmpty())
			{
				continue;
			}

			int amount = GhostItemHelper.getItemGhostAmount(stack);
			ItemStack ghostStack = GhostItemHelper.getSingleStackFromGhost(stack);
			if (amount == 0)
			{
				amount = Integer.MAX_VALUE;
			}

			IFilterKey mainKey = getFilterKey(filterStack, i, ghostStack, amount);

			if (nestedList.size() > 0)
			{
				CompositeFilterKey compositeKey = new CompositeFilterKey(amount);
				if (mainKey != null)
				{
					compositeKey.addFilterKey(mainKey);
				}

				for (ItemStack nestedStack : nestedList)
				{
					compositeKey.addFilterKey(((INestableItemFilterProvider) nestedStack.getItem()).getFilterKey(filterStack, i, ghostStack, amount));
				}

				filteredList.add(compositeKey);
			} else if (mainKey != null)
			{
				filteredList.add(mainKey);
			}
		}

		testFilter.initializeFilter(filteredList, tile, handler, true);

		return testFilter;
	}

	@Override
	public int receiveButtonPress(ItemStack filterStack, String buttonKey, int ghostItemSlot, int currentButtonState)
	{
		// Returns new state that the pressed button is in. -1 for an invalid button.
		CompoundNBT tag = filterStack.getTag();
		if (tag == null)
		{
			filterStack.setTag(new CompoundNBT());
			tag = filterStack.getTag();
		}

		if (buttonKey.equals(Constants.BUTTONID.BLACKWHITELIST))
		{
			int nextState = 0;
			switch (currentButtonState)
			{
			case 0:
				nextState = 1;
				break;
			default:
				nextState = 0;
			}

			tag.putInt(Constants.NBT.BLACKWHITELIST, nextState);

			return nextState;
		}

		List<ItemStack> nestedList = getNestedFilters(filterStack);
		for (ItemStack nestedStack : nestedList)
		{
			int nextState = ((INestableItemFilterProvider) nestedStack.getItem()).receiveButtonPress(filterStack, buttonKey, ghostItemSlot, currentButtonState);
			if (nextState != -1)
			{
				return nextState;
			}
		}

		return -1;
	}

	@Override
	public int getCurrentButtonState(ItemStack filterStack, String buttonKey, int ghostItemSlot)
	{
		CompoundNBT tag = filterStack.getTag();
		if (tag != null)
		{
			if (buttonKey.equals(Constants.BUTTONID.BLACKWHITELIST))
			{
				int state = tag.getInt(Constants.NBT.BLACKWHITELIST);
				return state;
			}
		}

		List<ItemStack> nestedList = getNestedFilters(filterStack);
		for (ItemStack nestedStack : nestedList)
		{
			int currentState = ((INestableItemFilterProvider) nestedStack.getItem()).getCurrentButtonState(filterStack, buttonKey, ghostItemSlot);
			if (currentState != -1)
			{
				return currentState;
			}
		}

		return -1;
	}

	@Override
	public List<ITextComponent> getTextForHoverItem(ItemStack filterStack, String buttonKey, int ghostItemSlot)
	{
		List<ITextComponent> componentList = new ArrayList<ITextComponent>();

		int currentState = getCurrentButtonState(filterStack, buttonKey, ghostItemSlot);
		if (buttonKey.equals(Constants.BUTTONID.BLACKWHITELIST))
		{
			switch (currentState)
			{
			case 1:
				componentList.add(new TranslationTextComponent("filter.bloodmagic.blacklist"));
				break;
			default:
				componentList.add(new TranslationTextComponent("filter.bloodmagic.whitelist"));
			}
		}

		List<ItemStack> nestedList = getNestedFilters(filterStack);
		for (ItemStack nestedStack : nestedList)
		{
			componentList.addAll(((INestableItemFilterProvider) nestedStack.getItem()).getTextForHoverItem(filterStack, buttonKey, ghostItemSlot));
		}

		return componentList;
	}

	@OnlyIn(Dist.CLIENT)
	public List<Pair<String, Button.IPressable>> getButtonAction(ContainerFilter container)
	{
		List<Pair<String, Button.IPressable>> buttonList = new ArrayList<Pair<String, IPressable>>();

		buttonList.add(Pair.of(Constants.BUTTONID.BLACKWHITELIST, new FilterButtonTogglePress(Constants.BUTTONID.BLACKWHITELIST, container)));

		List<ItemStack> nestedList = getNestedFilters(container.filterStack);
		for (ItemStack nestedStack : nestedList)
		{
			List<Pair<String, Button.IPressable>> nestedButtonList = ((INestableItemFilterProvider) nestedStack.getItem()).getButtonAction(container);
			buttonList.addAll(nestedButtonList);
		}

		return buttonList;
	}

	@Override
	public Pair<Integer, Integer> getTexturePositionForState(ItemStack filterStack, String buttonKey, int currentButtonState)
	{
		if (buttonKey.equals(Constants.BUTTONID.BLACKWHITELIST))
		{
			switch (currentButtonState)
			{
			case 1:
				return Pair.of(176, 20);
			default:
				return Pair.of(176, 0);
			}
		}

		List<ItemStack> nestedList = getNestedFilters(filterStack);
		for (ItemStack nestedStack : nestedList)
		{
			Pair<Integer, Integer> pair = ((INestableItemFilterProvider) nestedStack.getItem()).getTexturePositionForState(filterStack, buttonKey, currentButtonState);
			if (pair.getLeft() < 0 || pair.getRight() < 0)
			{
				continue;
			}

			return pair;
		}

		return Pair.of(0, 0);
	}

	@Override
	public boolean isButtonGlobal(ItemStack filterStack, String buttonKey)
	{
		return buttonKey.equals(Constants.BUTTONID.BLACKWHITELIST);
	}

	public List<ItemStack> getNestedFilters(ItemStack mainFilterStack)
	{
		List<ItemStack> nestedFilters = new ArrayList<ItemStack>();
		ItemInventory inv = new ItemInventory(mainFilterStack, maxUpgrades, FILTER_INV);
		for (int i = 0; i < maxUpgrades; i++)
		{
			ItemStack testStack = inv.getStackInSlot(i);
			if (testStack.isEmpty())
			{
				continue;
			}

			if (testStack.getItem() instanceof INestableItemFilterProvider)
			{
				nestedFilters.add(testStack);
			}
		}

		return nestedFilters;
	}

	@Override
	public boolean canReceiveNestedFilter(ItemStack mainFilterStack, ItemStack nestedFilterStack)
	{
		if (nestedFilterStack.isEmpty())
		{
			return false;
		} else if (!(nestedFilterStack.getItem() instanceof INestableItemFilterProvider))
		{
			return false;
		}

		boolean hasEmpty = false;

		ItemInventory inv = new ItemInventory(mainFilterStack, maxUpgrades, FILTER_INV);
		for (int i = 0; i < maxUpgrades; i++)
		{
			ItemStack testStack = inv.getStackInSlot(i);
			if (testStack.isEmpty())
			{
				hasEmpty = true;
				continue;
			}

			if (testStack.getItem().equals(nestedFilterStack.getItem()))
			{
				return false;
			}
		}

		return hasEmpty;
	}

	@Override
	public ItemStack nestFilter(ItemStack mainFilterStack, ItemStack nestedFilterStack)
	{
		if (canReceiveNestedFilter(mainFilterStack, nestedFilterStack))
		{
			ItemStack copyStack = mainFilterStack.copy();

			ItemInventory inv = new ItemInventory(copyStack, maxUpgrades, FILTER_INV);
			for (int i = 0; i < maxUpgrades; i++)
			{
				ItemStack testStack = inv.getStackInSlot(i);
				if (testStack.isEmpty())
				{
					inv.setInventorySlotContents(i, nestedFilterStack);
					inv.markDirty();
					return copyStack;
				}
			}
		}

		return ItemStack.EMPTY;
	}
}
