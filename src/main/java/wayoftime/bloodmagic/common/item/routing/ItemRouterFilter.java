package wayoftime.bloodmagic.common.item.routing;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.Button.IPressable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.IItemHandler;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.client.button.FilterButtonTogglePress;
import wayoftime.bloodmagic.common.item.inventory.ContainerFilter;
import wayoftime.bloodmagic.common.item.inventory.InventoryFilter;
import wayoftime.bloodmagic.common.item.inventory.ItemInventory;
import wayoftime.bloodmagic.common.routing.BasicItemFilter;
import wayoftime.bloodmagic.common.routing.BlacklistItemFilter;
import wayoftime.bloodmagic.common.routing.IItemFilter;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.GhostItemHelper;
import wayoftime.bloodmagic.util.Utils;

public class ItemRouterFilter extends Item implements INamedContainerProvider, IItemFilterProvider
{
	public static final int inventorySize = 9;
	public static final int maxUpgrades = 9;

	public static final String FILTER_INV = "filterInventory";

	public ItemRouterFilter()
	{
		super(new Item.Properties().maxStackSize(1).group(BloodMagic.TAB));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack filterStack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.basicfilter.desc").mergeStyle(TextFormatting.ITALIC).mergeStyle(TextFormatting.GRAY));

		int whitelistState = this.getCurrentButtonState(filterStack, Constants.BUTTONID.BLACKWHITELIST, 0);
		boolean isWhitelist = whitelistState == 0;

		if (isWhitelist)
		{
			tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.filter.whitelist"));
		} else
		{
			tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.filter.blacklist"));
		}

		ItemInventory inv = new InventoryFilter(filterStack);
		for (int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if (stack.isEmpty())
			{
				continue;
			}

			if (isWhitelist)
			{
				int amount = GhostItemHelper.getItemGhostAmount(stack);
				if (amount > 0)
				{
					tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.filter.count", amount, stack.getDisplayName()));
				} else
				{
					tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.filter.all", stack.getDisplayName()));
				}
			} else
			{
				tooltip.add(stack.getDisplayName());
			}
		}

		super.addInformation(filterStack, world, tooltip, flag);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		if (!world.isRemote)
		{
			Utils.setUUID(stack);

			if (player instanceof ServerPlayerEntity)
			{
				NetworkHooks.openGui((ServerPlayerEntity) player, this, buf -> buf.writeItemStack(stack, false));
			}
		}

		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}

	@Override
	public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity player)
	{
		// TODO Auto-generated method stub
		assert player.getEntityWorld() != null;
		return new ContainerFilter(p_createMenu_1_, player, p_createMenu_2_, player.getHeldItemMainhand());
	}

	@Override
	public ITextComponent getDisplayName()
	{
		// TODO Auto-generated method stub
		return new StringTextComponent("Filter");
	}

	@Override
	public ItemStack getContainedStackForItem(ItemStack filterStack, ItemStack keyStack)
	{
		ItemStack copyStack = keyStack.copy();
		GhostItemHelper.setItemGhostAmount(copyStack, 0);
		copyStack.setCount(1);
		return copyStack;
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
		return new BasicFilterKey(ghostStack, amount);
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

			if (nestedList.size() > 0)
			{
				CompositeFilterKey compositeKey = new CompositeFilterKey(amount);
				filteredList.add(getFilterKey(filterStack, i, ghostStack, amount));
				for (ItemStack nestedStack : nestedList)
				{
					compositeKey.addFilterKey(((INestableItemFilterProvider) nestedStack.getItem()).getFilterKey(filterStack, i, ghostStack, amount));
					filteredList.add(compositeKey);
				}
			} else
			{
				filteredList.add(getFilterKey(filterStack, i, ghostStack, amount));
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

			if (nestedList.size() > 0)
			{
				CompositeFilterKey compositeKey = new CompositeFilterKey(amount);
				filteredList.add(getFilterKey(filterStack, i, ghostStack, amount));
				for (ItemStack nestedStack : nestedList)
				{
					compositeKey.addFilterKey(((INestableItemFilterProvider) nestedStack.getItem()).getFilterKey(filterStack, i, ghostStack, amount));
					filteredList.add(compositeKey);
				}
			} else
			{
				filteredList.add(getFilterKey(filterStack, i, ghostStack, amount));
			}
		}

		testFilter.initializeFilter(filteredList, tile, handler, true);

		return testFilter;
	}

	@Override
	public void setGhostItemAmount(ItemStack filterStack, int ghostItemSlot, int amount)
	{
		ItemInventory inv = new InventoryFilter(filterStack);
		ItemStack stack = inv.getStackInSlot(ghostItemSlot);
		if (!stack.isEmpty())
		{
			GhostItemHelper.setItemGhostAmount(stack, amount);

			inv.writeToStack(filterStack);
		}
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

		if (this instanceof INestableItemFilterProvider)
		{
			return -1;
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

		if (this instanceof INestableItemFilterProvider)
		{
			return -1;
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

		if (this instanceof INestableItemFilterProvider)
		{
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
	public List<Pair<String, Button.IPressable>> getButtonAction(ContainerFilter container)
	{
		List<Pair<String, Button.IPressable>> buttonList = new ArrayList<Pair<String, IPressable>>();

		buttonList.add(Pair.of(Constants.BUTTONID.BLACKWHITELIST, new FilterButtonTogglePress(Constants.BUTTONID.BLACKWHITELIST, container)));

		if (this instanceof INestableItemFilterProvider)
		{
			return buttonList;
		}

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

		if (this instanceof INestableItemFilterProvider)
		{
			return Pair.of(0, 0);
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
