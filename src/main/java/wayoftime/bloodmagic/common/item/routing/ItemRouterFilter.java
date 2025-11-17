package wayoftime.bloodmagic.common.item.routing;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.client.button.FilterButtonTogglePress;
import wayoftime.bloodmagic.common.container.item.ContainerFilter;
import wayoftime.bloodmagic.common.item.inventory.DataFilter;
import wayoftime.bloodmagic.common.item.inventory.InventoryFilter;
import wayoftime.bloodmagic.common.item.inventory.ItemInventory;
import wayoftime.bloodmagic.common.routing.BasicItemFilter;
import wayoftime.bloodmagic.common.routing.BlacklistItemFilter;
import wayoftime.bloodmagic.common.routing.IItemFilter;
import wayoftime.bloodmagic.util.BMLog;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.GhostItemHelper;
import wayoftime.bloodmagic.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class ItemRouterFilter extends Item implements MenuProvider, IItemFilterProvider
{
	public static final int inventorySize = 9;
	public static final int maxUpgrades = 9;

	public static final String FILTER_INV = "filterInventory";

    public boolean HAS_TAG = false;
    public boolean HAS_ENCHANT_KIND = false;
    public boolean HAS_ENCHANT_LEVEL = false;

    public static final int DATA_BWLIST = 0;
    public static final int DATA_TAG = 1; // + slot (0-8)
    public static final int DATA_ENCHANT = DATA_TAG + 9; // + slot (0-8)
    public static final int DATA_ENCHANT_LVL = DATA_ENCHANT + 9; // + slot (0-8)

    public static final int BUTTON_BWLIST = 0;
    public static final int BUTTON_TAG = 1;
    public static final int BUTTON_ENCHANT_KIND = 2;
    public static final int BUTTON_ENCHANT_LEVEL = 3;

	public ItemRouterFilter()
	{
		super(new Item.Properties().stacksTo(16));
	}

    // dont override on children, or make sure its
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
	{
        ItemStack stack = player.getItemInHand(hand);
        if (hand == InteractionHand.OFF_HAND) {
            return InteractionResultHolder.pass(stack); // we always assume main hand item, offhand no good
        }

		if (!world.isClientSide)
		{
			if (player instanceof ServerPlayer)
			{
				NetworkHooks.openScreen((ServerPlayer) player, this, buf -> {
                    buf.writeBoolean(HAS_TAG);
                    buf.writeBoolean(HAS_ENCHANT_KIND);
                    buf.writeBoolean(HAS_ENCHANT_LEVEL);
                });
			}
		}

		return new InteractionResultHolder<>(InteractionResult.sidedSuccess(world.isClientSide), stack);
	}

	@Override
	public AbstractContainerMenu createMenu(int containerId, Inventory playerInv, Player player)
    {
        assert player.getCommandSenderWorld() != null;
        ItemStack stack = player.getMainHandItem();
        List<Integer> content = new ArrayList<>(1 + 3 * 9);
        CompoundTag tag = stack.getOrCreateTag();

        InventoryFilter filterInv = new InventoryFilter(9) {
            @Override
            public void setStackInSlot(int slot, @NotNull ItemStack contentStack) {
                super.setStackInSlot(slot, stack);
                // save NBT here, ALSO print the NBT here to make sure it gets the ghost size on server side!
                tag.put(Constants.NBT.ITEM_INVENTORY, this.serializeNBT());
                stack.setTag(tag);
                BMLog.DEFAULT.info("Stack saved in slot {} has the following tag: '{}'\nNew filter NBT: '{}'", slot, contentStack.getTag(), stack.getTag()); // dont create it here, if its null I wanna know
            }
        };

        filterInv.deserializeNBT(tag.getCompound(Constants.NBT.ITEM_INVENTORY));
        if (tag.contains(Constants.BUTTONID.BLACKWHITELIST)) {
            content.set(0, tag.getInt(Constants.BUTTONID.BLACKWHITELIST));
        }
        for (int i = 0; i < 9; i++) {
            if (tag.contains(Constants.BUTTONID.ITEMTAG + i)) {
                content.set(DATA_TAG + i, tag.getInt(Constants.BUTTONID.ITEMTAG));
            }
            if (tag.contains(Constants.BUTTONID.ENCHANT + i)) {
                content.set(DATA_ENCHANT + i, tag.getInt(Constants.BUTTONID.ENCHANT));
            }
            if (tag.contains(Constants.BUTTONID.ENCHANT_LVL + i)) {
                content.set(DATA_ENCHANT_LVL + i, tag.getInt(Constants.BUTTONID.ENCHANT_LVL));
            }
        }

        DataFilter data = new DataFilter(content) {
            @Override
            public void save(int index) {
                if (index == DATA_BWLIST) {
                    tag.putInt(Constants.BUTTONID.BLACKWHITELIST, content.get(index));
                    stack.setTag(tag);
                    return;
                }

                String nbt = "";
                int offset = -1;
                if (index >= DATA_ENCHANT_LVL) {
                    nbt = Constants.BUTTONID.ENCHANT_LVL;
                    offset = index - DATA_ENCHANT_LVL;
                } else if (index >= DATA_ENCHANT) {
                    nbt = Constants.BUTTONID.ENCHANT;
                    offset = index - DATA_ENCHANT;
                } else if (index >= DATA_TAG) {
                    nbt = Constants.BUTTONID.ITEMTAG;
                    offset = index - DATA_TAG;
                }

                if (nbt.equalsIgnoreCase("") || offset == -1) {
                    BMLog.DEFAULT.warn("Trying to save index {} but its unknown", index);
                    return;
                }

                tag.putInt(nbt + offset, content.get(index));
                stack.setTag(tag);
            }
        };

        return new ContainerFilter(containerId, player, playerInv, filterInv, HAS_TAG, HAS_ENCHANT_KIND, HAS_ENCHANT_LEVEL);
    }

	@Override
	public Component getDisplayName()
	{
        // this should be overridden by the actual filter classes and return a Component#translatable instead
		return Component.literal("Whoops, forgot to override getDisplayName here I guess");
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

    public static InventoryFilter getInv(ItemStack filterStack) {
        InventoryFilter ret = new InventoryFilter(9);
        ret.deserializeNBT(filterStack.getOrCreateTag().getCompound(Constants.NBT.ITEM_INVENTORY));
        return ret;
    }

	@Override
	public IItemFilter getInputItemFilter(ItemStack filterStack, BlockEntity tile, IItemHandler handler)
	{
		IItemFilter testFilter = getFilterTypeFromConfig(filterStack);

		List<IFilterKey> filteredList = new ArrayList<>();
		InventoryFilter inv = getInv(filterStack);

		for (int i = 0; i < inv.getSlots(); i++)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if (stack.isEmpty())
			{
				continue;
			}

			int amount = GhostItemHelper.getItemGhostAmount(stack);
			ItemStack ghostStack = GhostItemHelper.getSingleStackFromGhost(stack);

			IFilterKey key = getFilterKey(filterStack, i, ghostStack, amount);

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
		InventoryFilter inv = getInv(filterStack);

		for (int i = 0; i < inv.getSlots(); i++)
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

			IFilterKey key = getFilterKey(filterStack, i, ghostStack, amount);

			filteredList.add(key);
		}

		testFilter.initializeFilter(filteredList, tile, handler, true);

		return testFilter;
	}

	@Override
	public void setGhostItemAmount(ItemStack filterStack, int ghostItemSlot, int amount)
	{
		InventoryFilter inv = getInv(filterStack);
		ItemStack stack = inv.getStackInSlot(ghostItemSlot);
		if (!stack.isEmpty())
		{
			GhostItemHelper.setItemGhostAmount(stack, amount);

			CompoundTag tag = filterStack.getTag();

            tag.put(Constants.NBT.ITEM_INVENTORY, inv.serializeNBT());
            filterStack.setTag(tag);
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
				componentList.add(Component.translatable("filter.bloodmagic.blacklist"));
				break;
			default:
				componentList.add(Component.translatable("filter.bloodmagic.whitelist"));
			}
		}

		return componentList;
	}

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
	public IItemFilter getUninitializedItemFilter(ItemStack filterStack)
	{
		IItemFilter testFilter = getFilterTypeFromConfig(filterStack);

		List<IFilterKey> filteredList = new ArrayList<>();
		InventoryFilter inv = getInv(filterStack);

		for (int i = 0; i < inv.getSlots(); i++)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if (stack.isEmpty())
			{
				continue;
			}

			int amount = GhostItemHelper.getItemGhostAmount(stack);
			ItemStack ghostStack = GhostItemHelper.getSingleStackFromGhost(stack);

			IFilterKey key = getFilterKey(filterStack, i, ghostStack, amount);

			filteredList.add(key);
		}

		testFilter.initializeFilter(filteredList);

		return testFilter;
	}
}
