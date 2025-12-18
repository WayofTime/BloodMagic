package wayoftime.bloodmagic.common.item.routing;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.tuple.Pair;
import wayoftime.bloodmagic.common.item.inventory.InventoryFilter;
import wayoftime.bloodmagic.common.item.inventory.ItemInventory;
import wayoftime.bloodmagic.common.routing.*;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.GhostItemHelper;

import java.util.ArrayList;
import java.util.List;

public class ItemCompositeFilter extends ItemRouterFilter implements MenuProvider, ICompositeItemFilterProvider
{
	public ItemCompositeFilter()
	{
		super();
	}

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        // pretty sure this isnt supposed to have a menu, so we stop this here
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.bloodmagic.filter.composite");
    }

    @Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack filterStack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
		tooltip.add(Component.translatable("tooltip.bloodmagic.compositefilter.desc").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));

		if (filterStack.getTag() == null)
		{
			return;
		}

		List<ItemStack> nestedFilters = getNestedFilters(filterStack);
		if (nestedFilters.size() > 0)
		{
            tooltip.add(Component.translatable("tooltip.bloodmagic.contained_filters").withStyle(ChatFormatting.BLUE));
            for (ItemStack nestedStack : nestedFilters)
            {
                tooltip.add(nestedStack.getHoverName());
                if (Screen.hasShiftDown() && nestedStack.getItem() instanceof ItemRouterFilter nestedFilter) {
                    List<Component> sublist = new ArrayList<>();
                    nestedFilter.appendHoverText(nestedStack, world, sublist, flag);
                    sublist.remove(0); // dont need description of filter
                    tooltip.addAll(sublist);
                }
            }

			boolean sneaking = Screen.hasShiftDown();
			if (!sneaking)
			{
				tooltip.add(Component.translatable("tooltip.bloodmagic.extraInfo").withStyle(ChatFormatting.BLUE));
			}
		}
	}

	protected IItemFilter getFilterTypeFromConfig(ItemStack filterStack)
	{
		return new BasicCompositeFilter();
	}

	@Override
	public IFilterKey getFilterKey(ItemStack filterStack, int slot, ItemStack ghostStack, int amount)
	{
		return null;
	}

	@Override
	public IItemFilter getInputItemFilter(ItemStack filterStack, BlockEntity tile, IItemHandler handler)
    {
        IItemFilter testFilter = getFilterTypeFromConfig(filterStack);
        List<IFilterKey> filteredList = new ArrayList<>();
        List<ItemStack> nestedList = getNestedFilters(filterStack);
        for (ItemStack containedStack : nestedList) {
            if (!containedStack.getOrCreateTag().contains(Constants.NBT.ITEM_INVENTORY)) {
                continue;
            }
            InventoryFilter containedInv = getInv(containedStack);
            CompositeFilterKey key = new CompositeFilterKey(0);
            for (int j = 0; j < containedInv.getSlots(); j++) {
                ItemStack ghostStack = containedInv.getStackInSlot(j);
                if (ghostStack.isEmpty()) {
                    continue;
                }

                int count = GhostItemHelper.getItemGhostAmount(ghostStack);
                ItemStack contentStack = GhostItemHelper.getSingleStackFromGhost(ghostStack);
                key.addFilterKey(((ItemRouterFilter) containedStack.getItem()).getFilterKey(containedStack, j, contentStack, count));
                key.setCount(Math.max(key.getCount(), count));
            }

            filteredList.add(key);
        }

        testFilter.initializeFilter(filteredList, tile, handler, false);
        return testFilter;
    }

	@Override
	public IItemFilter getOutputItemFilter(ItemStack filterStack, BlockEntity tile, IItemHandler handler)
    {
        IItemFilter testFilter = getFilterTypeFromConfig(filterStack);
        List<IFilterKey> filteredList = new ArrayList<>();
        List<ItemStack> nestedList = getNestedFilters(filterStack);
        for (ItemStack containedStack : nestedList) {
            if (!containedStack.getOrCreateTag().contains(Constants.NBT.ITEM_INVENTORY)) {
                continue;
            }
            InventoryFilter containedInv = getInv(containedStack);
            CompositeFilterKey key = new CompositeFilterKey(0);
            for (int j = 0; j < containedInv.getSlots(); j++) {
                ItemStack ghostStack = containedInv.getStackInSlot(j);
                if (ghostStack.isEmpty()) {
                    continue;
                }

                int count = GhostItemHelper.getItemGhostAmount(ghostStack);
                if (count == 0) {
                    count = Integer.MAX_VALUE;
                }
                ItemStack contentStack = GhostItemHelper.getSingleStackFromGhost(ghostStack);
                key.addFilterKey(((ItemRouterFilter) containedStack.getItem()).getFilterKey(containedStack, j, contentStack, count));
                key.setCount(Math.max(key.getCount(), count));
            }

            filteredList.add(key);
        }

        testFilter.initializeFilter(filteredList, tile, handler, true);

        return testFilter;
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
				componentList.add(Component.translatable("filter.bloodmagic.blacklist"));
				break;
			default:
				componentList.add(Component.translatable("filter.bloodmagic.whitelist"));
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

    /* this would be annoying to fix but it'll be part of ContainerFilter now anyways so its gone now
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
     */

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

            /* probably dont want that behaviour
			if (testStack.getItem().equals(nestedFilterStack.getItem()))
			{
				return false;
			}
             */
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
