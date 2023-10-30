package wayoftime.bloodmagic.common.item.routing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.network.NetworkHooks;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.client.button.FilterButtonTogglePress;
import wayoftime.bloodmagic.common.container.item.ContainerFilter;
import wayoftime.bloodmagic.common.item.inventory.InventoryFilter;
import wayoftime.bloodmagic.common.item.inventory.ItemInventory;
import wayoftime.bloodmagic.common.routing.BasicFluidFilter;
import wayoftime.bloodmagic.common.routing.BlacklistFluidFilter;
import wayoftime.bloodmagic.common.routing.IFluidFilter;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.GhostItemHelper;
import wayoftime.bloodmagic.util.Utils;

public class ItemFluidRouterFilter extends Item implements MenuProvider, IFluidFilterProvider
{
	public static final int inventorySize = 9;
	public static final int maxUpgrades = 9;

	public static final String FILTER_INV = "filterInventory";

	public ItemFluidRouterFilter()
	{
		super(new Item.Properties().stacksTo(16).tab(BloodMagic.TAB));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		if (!world.isClientSide)
		{
			Utils.setUUID(stack);

			if (player instanceof ServerPlayer)
			{
				NetworkHooks.openGui((ServerPlayer) player, this, buf -> buf.writeItemStack(stack, false));
			}
		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
	}

	@Override
	public AbstractContainerMenu createMenu(int p_createMenu_1_, Inventory p_createMenu_2_, Player player)
	{
		assert player.getCommandSenderWorld() != null;
		return new ContainerFilter(p_createMenu_1_, player, p_createMenu_2_, player.getMainHandItem());
	}

	@Override
	public Component getDisplayName()
	{
		return new TextComponent("Fluid Filter");
	}

	@Override
	public ItemStack getContainedStackForItem(ItemStack filterStack, ItemStack keyStack)
	{
		ItemStack copyStack = keyStack.copy();
		GhostItemHelper.setItemGhostAmount(copyStack, 0);
		copyStack.setCount(1);
		return copyStack;
	}

	protected IFluidFilter getFilterTypeFromConfig(ItemStack filterStack)
	{
		int state = getCurrentButtonState(filterStack, Constants.BUTTONID.BLACKWHITELIST, 0);
		if (state == 1)
		{
			return new BlacklistFluidFilter();
		}

		return new BasicFluidFilter();
	}

	@Override
	public IFilterKey getFilterKey(ItemStack filterStack, int slot, FluidStack ghostStack, int amount)
	{
		return new FluidFilterKey(ghostStack, amount);
	}

	@Override
	public IFluidFilter getInputFluidFilter(ItemStack filterStack, BlockEntity tile, IFluidHandler handler)
	{
		IFluidFilter testFilter = getFilterTypeFromConfig(filterStack);

		List<IFilterKey> filteredList = new ArrayList<>();
		ItemInventory inv = new InventoryFilter(filterStack);

		for (int i = 0; i < inv.getContainerSize(); i++)
		{
			ItemStack stack = inv.getItem(i);
			if (stack.isEmpty())
			{
				continue;
			}

			Optional<FluidStack> optionalStack = FluidUtil.getFluidContained(stack);
			FluidStack fluidStack = FluidStack.EMPTY;
			if (optionalStack.isPresent())
			{
				fluidStack = optionalStack.get();
			}

			int amount = GhostItemHelper.getItemGhostAmount(stack);
			IFilterKey key = getFilterKey(filterStack, i, fluidStack, amount);

			filteredList.add(key);
		}

		testFilter.initializeFilter(filteredList, tile, handler, false);
		return testFilter;
	}

	@Override
	public IFluidFilter getOutputFluidFilter(ItemStack filterStack, BlockEntity tile, IFluidHandler handler)
	{
		IFluidFilter testFilter = getFilterTypeFromConfig(filterStack);

		List<IFilterKey> filteredList = new ArrayList<>();
		ItemInventory inv = new InventoryFilter(filterStack);

		for (int i = 0; i < inv.getContainerSize(); i++)
		{
			ItemStack stack = inv.getItem(i);
			if (stack.isEmpty())
			{
				continue;
			}

			Optional<FluidStack> optionalStack = FluidUtil.getFluidContained(stack);
			FluidStack ghostStack = FluidStack.EMPTY;
			if (optionalStack.isPresent())
			{
				ghostStack = optionalStack.get();
			}

			int amount = GhostItemHelper.getItemGhostAmount(stack);
			if (amount == 0)
			{
				amount = Integer.MAX_VALUE;
			}
			IFilterKey key = getFilterKey(filterStack, i, ghostStack, amount);

			filteredList.add(key);
		}

		testFilter.initializeFilter(filteredList, tile, handler, true);

		return testFilter;
	}

	@Override
	public void setGhostItemAmount(ItemStack filterStack, int ghostItemSlot, int amount)
	{
		ItemInventory inv = new InventoryFilter(filterStack);
		ItemStack stack = inv.getItem(ghostItemSlot);
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
		}

		return componentList;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public List<Pair<String, Button.OnPress>> getButtonAction(ContainerFilter container)
	{
		List<Pair<String, Button.OnPress>> buttonList = new ArrayList<Pair<String, OnPress>>();

		buttonList.add(Pair.of(Constants.BUTTONID.BLACKWHITELIST, new FilterButtonTogglePress(Constants.BUTTONID.BLACKWHITELIST, container)));

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

		return Pair.of(0, 0);
	}

	@Override
	public boolean isButtonGlobal(ItemStack filterStack, String buttonKey)
	{
		return buttonKey.equals(Constants.BUTTONID.BLACKWHITELIST);
	}

	@Override
	public IFluidFilter getUninitializedFluidFilter(ItemStack filterStack)
	{
		IFluidFilter testFilter = getFilterTypeFromConfig(filterStack);

		List<IFilterKey> filteredList = new ArrayList<>();
		ItemInventory inv = new InventoryFilter(filterStack);

		for (int i = 0; i < inv.getContainerSize(); i++)
		{
			ItemStack stack = inv.getItem(i);
			if (stack.isEmpty())
			{
				continue;
			}

			Optional<FluidStack> optionalStack = FluidUtil.getFluidContained(stack);
			FluidStack ghostStack = FluidStack.EMPTY;
			if (optionalStack.isPresent())
			{
				ghostStack = optionalStack.get();
			}

			int amount = GhostItemHelper.getItemGhostAmount(stack);
			ghostStack.setAmount(amount);

			IFilterKey key = getFilterKey(filterStack, i, ghostStack, amount);

			filteredList.add(key);
		}

		testFilter.initializeFilter(filteredList);

		return testFilter;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack filterStack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
		tooltip.add(new TranslatableComponent("tooltip.bloodmagic.fluidfilter.desc").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));

		if (filterStack.getTag() == null)
		{
			return;
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
	}
}
