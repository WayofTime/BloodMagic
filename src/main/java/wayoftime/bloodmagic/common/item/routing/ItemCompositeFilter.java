package wayoftime.bloodmagic.common.item.routing;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Direction;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.client.button.FilterButtonTogglePress;
import wayoftime.bloodmagic.common.container.item.ContainerFilter;
import wayoftime.bloodmagic.common.item.inventory.InventoryFilter;
import wayoftime.bloodmagic.common.item.inventory.ItemInventory;
import wayoftime.bloodmagic.common.routing.IRoutingFilter;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.GhostItemHelper;

public class ItemCompositeFilter extends ItemItemRouterFilter implements MenuProvider, ICompositeItemFilterProvider
{
	public ItemCompositeFilter()
	{
		super();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack filterStack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
		tooltip.add(new TranslatableComponent("tooltip.bloodmagic.compositefilter.desc").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));

		if (filterStack.getTag() == null)
		{
			return;
		}

		List<ItemStack> nestedFilters = getNestedFilters(filterStack);
		if (nestedFilters.size() > 0)
		{
			boolean sneaking = Screen.hasShiftDown();
			if (!sneaking)
			{
				tooltip.add(new TranslatableComponent("tooltip.bloodmagic.extraInfo").withStyle(ChatFormatting.BLUE));
			} else
			{
				tooltip.add(new TranslatableComponent("tooltip.bloodmagic.contained_filters").withStyle(ChatFormatting.BLUE));
				for (ItemStack nestedStack : nestedFilters)
				{
					tooltip.add(nestedStack.getHoverName());
				}
			}
		}

		int whitelistState = this.getCurrentButtonState(filterStack, Constants.BUTTONID.BLACKWHITELIST, 0);
		boolean isWhitelist = whitelistState == 0;

		if (isWhitelist)
		{
			tooltip.add(new TranslatableComponent("tooltip.bloodmagic.filter.whitelist").withStyle(ChatFormatting.GRAY));
		} else
		{
			tooltip.add(new TranslatableComponent("tooltip.bloodmagic.filter.blacklist").withStyle(ChatFormatting.GRAY));
		}

		ItemInventory inv = new InventoryFilter(filterStack);
		for (int i = 0; i < inv.getContainerSize(); i++)
		{
			ItemStack stack = inv.getItem(i);
			if (stack.isEmpty())
			{
				continue;
			}

			if (isWhitelist)
			{
				int amount = GhostItemHelper.getItemGhostAmount(stack);
				if (amount > 0)
				{
					tooltip.add(new TranslatableComponent("tooltip.bloodmagic.filter.count", amount, stack.getHoverName()));
				} else
				{
					tooltip.add(new TranslatableComponent("tooltip.bloodmagic.filter.all", stack.getHoverName()));
				}
			} else
			{
				tooltip.add(stack.getHoverName());
			}
		}

//		super.addInformation(filterStack, world, tooltip, flag);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		List<ItemStack> nestedFilters = getNestedFilters(stack);
		if (nestedFilters.size() > 0)
		{
			return super.use(world, player, hand);
		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
	}

//	protected IRoutingFilter<ItemStack> getFilterTypeFromConfig(ItemStack filterStack)
//	{
//		int state = getCurrentButtonState(filterStack, Constants.BUTTONID.BLACKWHITELIST, 0);
//		if (state == 1)
//		{
//			return new BlacklistItemFilter();
//		}
//
//		return new BasicItemFilter();
//	}

	@Override
	public IFilterKey getFilterKey(ItemStack filterStack, int slot, ItemStack ghostStack, int amount)
	{
		return null;
	}

	@Override
	public List<IRoutingFilter> getInputFilter(ItemStack filterStack, BlockEntity tile, Direction side)
	{
		IRoutingFilter<ItemStack> testFilter = getFilterTypeFromConfig(filterStack);

		List<IFilterKey> filteredList = new ArrayList<>();
		ItemInventory inv = new InventoryFilter(filterStack);

		List<ItemStack> nestedList = getNestedFilters(filterStack);
		for (int i = 0; i < inv.getContainerSize(); i++)
		{
			ItemStack stack = inv.getItem(i);
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

		testFilter.initializeFilter(filteredList, tile, side, false);
		List<IRoutingFilter> list = Lists.newArrayList();
		list.add(testFilter);
		return list;
	}

	@Override
	public List<IRoutingFilter> getOutputFilter(ItemStack filterStack, BlockEntity tile, Direction side)
	{
		IRoutingFilter<ItemStack> testFilter = getFilterTypeFromConfig(filterStack);

		List<IFilterKey> filteredList = new ArrayList<>();
		ItemInventory inv = new InventoryFilter(filterStack); // TODO: Change to grab the filter from the Item

		List<ItemStack> nestedList = getNestedFilters(filterStack);
		// later.
		for (int i = 0; i < inv.getContainerSize(); i++)
		{
			ItemStack stack = inv.getItem(i);
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

		testFilter.initializeFilter(filteredList, tile, side, true);

		List<IRoutingFilter> list = Lists.newArrayList();
		list.add(testFilter);
		return list;
	}

	@Override
	public int receiveButtonPress(ItemStack filterStack, String buttonKey, int ghostItemSlot, int currentButtonState)
	{
		// Returns new state that the pressed button is in. -1 for an invalid button.
		CompoundTag tag = filterStack.getTag();
		if (tag == null)
		{
			filterStack.setTag(new CompoundTag());
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
		CompoundTag tag = filterStack.getTag();
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
	public List<Component> getTextForHoverItem(ItemStack filterStack, String buttonKey, int ghostItemSlot)
	{
		List<Component> componentList = new ArrayList<Component>();

		int currentState = getCurrentButtonState(filterStack, buttonKey, ghostItemSlot);
		if (buttonKey.equals(Constants.BUTTONID.BLACKWHITELIST))
		{
			switch (currentState)
			{
			case 1:
				componentList.add(new TranslatableComponent("filter.bloodmagic.blacklist"));
				break;
			default:
				componentList.add(new TranslatableComponent("filter.bloodmagic.whitelist"));
			}
			return componentList;
		}

		List<ItemStack> nestedList = getNestedFilters(filterStack);
		for (ItemStack nestedStack : nestedList)
		{
			componentList.addAll(((INestableItemFilterProvider) nestedStack.getItem()).getTextForHoverItem(filterStack, buttonKey, ghostItemSlot));
		}

		return componentList;
	}

	@OnlyIn(Dist.CLIENT)
	public List<Pair<String, Button.OnPress>> getButtonAction(ContainerFilter container)
	{
		List<Pair<String, Button.OnPress>> buttonList = new ArrayList<Pair<String, OnPress>>();

		buttonList.add(Pair.of(Constants.BUTTONID.BLACKWHITELIST, new FilterButtonTogglePress(Constants.BUTTONID.BLACKWHITELIST, container)));

		List<ItemStack> nestedList = getNestedFilters(container.filterStack);
		for (ItemStack nestedStack : nestedList)
		{
			List<Pair<String, Button.OnPress>> nestedButtonList = ((INestableItemFilterProvider) nestedStack.getItem()).getButtonAction(container);
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
			if (pair.getLeft() <= 0 && pair.getRight() <= 0)
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
			ItemStack testStack = inv.getItem(i);
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
			ItemStack testStack = inv.getItem(i);
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
				ItemStack testStack = inv.getItem(i);
				if (testStack.isEmpty())
				{
					inv.setItem(i, nestedFilterStack);
					inv.setChanged();
					return copyStack;
				}
			}
		}

		return ItemStack.EMPTY;
	}
}
