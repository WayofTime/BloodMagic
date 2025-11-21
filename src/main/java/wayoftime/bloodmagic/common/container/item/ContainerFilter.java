package wayoftime.bloodmagic.common.container.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.item.inventory.DataFilter;
import wayoftime.bloodmagic.common.item.inventory.InventoryFilter;
import wayoftime.bloodmagic.common.item.routing.IRoutingFilterProvider;
import wayoftime.bloodmagic.common.item.routing.ItemRouterFilter;
import wayoftime.bloodmagic.util.BMLog;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.GhostItemHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContainerFilter extends AbstractContainerMenu
{
	public final InventoryFilter inventoryFilter;
	private final int PLAYER_INVENTORY_ROWS = 3;
	private final int PLAYER_INVENTORY_COLUMNS = 9;
	public final Player player;
    public final boolean isTag;
    public final boolean isEnchant;
    private final DataFilter data;
	private int slotsOccupied = 9;

	public ContainerFilter(int windowId, Inventory playerInventory, FriendlyByteBuf buf)
	{
		this(windowId, playerInventory.player, playerInventory, new InventoryFilter(9), new DataFilter(2 + 3 * 9) {
            @Override
            public void save(int index) {
                BMLog.DEFAULT.info("client says hi. also index {}", index);
            }
        }, buf.readBoolean(), buf.readBoolean());
	}

	public ContainerFilter(int windowId, Player player, Inventory playerInventory, InventoryFilter filterInv, DataFilter data, boolean isTag, boolean isEnchant)
	{
		super(BloodMagicBlocks.FILTER_CONTAINER.get(), windowId);
        this.addDataSlots(data);
		this.player = player;
		this.inventoryFilter = filterInv;
		int currentSlotHeldIn = player.getInventory().selected;
		this.setup(playerInventory, currentSlotHeldIn);
        this.isTag = isTag;
        this.isEnchant = isEnchant;
        this.data = data;
	}

	public void setup(Inventory playerInv, int currentSlotHeldIn)
	{

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				this.addSlot(new SlotGhostItem(inventoryFilter, j + i * 3, 110 + j * 21, 15 + i * 21));
			}
		}

		for (int rowIndex = 0; rowIndex < PLAYER_INVENTORY_ROWS; ++rowIndex)
		{
			for (int columnIndex = 0; columnIndex < PLAYER_INVENTORY_COLUMNS; ++columnIndex)
			{
				this.addSlot(new Slot(playerInv, columnIndex + rowIndex * 9 + 9, 8 + columnIndex * 18, 105 + rowIndex * 18));
			}
		}

		for (int actionBarIndex = 0; actionBarIndex < PLAYER_INVENTORY_COLUMNS; ++actionBarIndex)
		{
			if (actionBarIndex == currentSlotHeldIn)
			{
				this.addSlot(new SlotDisabled(playerInv, actionBarIndex, 8 + actionBarIndex * 18, 163));
			} else
			{
				this.addSlot(new Slot(playerInv, actionBarIndex, 8 + actionBarIndex * 18, 163));
			}
		}

	}

    public int getData(int index) {
        return data.get(index);
    }

    @Override
    public boolean clickMenuButton(Player player, int buttonId) {
        return switch (buttonId) {
            case ItemRouterFilter.BUTTON_BWLIST -> {
                int state = data.get(ItemRouterFilter.DATA_BWLIST);
                setData(ItemRouterFilter.DATA_BWLIST, state == 0 ? 1 : 0);
                yield true;
            }
            case ItemRouterFilter.BUTTON_ENCHANT_LEVEL -> {
                int slot = data.get(ItemRouterFilter.DATA_SLOT);
                int state = data.get(ItemRouterFilter.DATA_ENCHANT_LVL + slot);
                setData(ItemRouterFilter.DATA_ENCHANT_LVL + slot, state == 0 ? 1 : 0);
                yield true;
            }
            case ItemRouterFilter.BUTTON_TAG -> {
                int slot = data.get(ItemRouterFilter.DATA_SLOT);
                int state = data.get(ItemRouterFilter.DATA_TAG + slot) + 1;

                ItemStack tagStack = inventoryFilter.getStackInSlot(slot);
                if (tagStack.isEmpty()) {
                    yield false;
                }
                List<TagKey<Item>> tagList = new ArrayList<>();
                tagStack.getTags().forEach(tagList::add);

                if (state > tagList.size()) { // size 9, last index 8, would be 9 with 0 being any + 1 from above would be 10 -> 0. seems to check out
                    state = 0;
                }
                CompoundTag tag = tagStack.getOrCreateTag();
                if (state != 0) {
                    tag.putString(Constants.NBT.TAG, tagList.get(state - 1).location().toString());
                } else {
                    tag.remove(Constants.NBT.TAG);
                }
                tagStack.setTag(tag);
                getSlot(slot).set(tagStack);

                setData(ItemRouterFilter.DATA_TAG + slot, state);
                yield true;
            }
            case ItemRouterFilter.BUTTON_ENCHANT_KIND -> {
                int slot = data.get(ItemRouterFilter.DATA_SLOT);
                int state = data.get(ItemRouterFilter.DATA_ENCHANT + slot) + 1;
                ItemStack enchStack = inventoryFilter.getStackInSlot(slot);
                if (enchStack.isEmpty()) {
                    yield false;
                }
                Map<Enchantment, Integer> enchMap = EnchantmentHelper.getEnchantments(enchStack);
                if (state >= enchMap.size() + 2 || enchMap.isEmpty()) {
                    state = 0;
                }
                if (enchMap.size() == 1 && state == 1) { // state would have been 0 before, so changing from 1 to 2 means skipping over "any" if there is only 1 enchant since it'd be the same as "every"
                    state = 2;
                }

                setData(ItemRouterFilter.DATA_ENCHANT + slot, state);
                yield true;
            }
            default -> false;
        };
    }

    @Override
	public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player)
    {
        if (slotId >= 0)
        {
            Slot slot = this.slots.get(slotId);

            if (slot instanceof SlotGhostItem)
            {
                setData(ItemRouterFilter.DATA_SLOT, slot.getSlotIndex());

                if ((dragType == 0 || dragType == 1))
                {
                    ItemStack slotStack = slot.getItem();
                    ItemStack heldStack = this.getCarried();

                    if (dragType == 0) // Left mouse click-eth
                    {
                        if (heldStack.isEmpty() && !slotStack.isEmpty())
                        {
                            // I clicked on the slot with an empty hand. Selecting!
                            // Return here to not save the server-side inventory
//									return ItemStack.EMPTY;
                            return;
                        } else if (!heldStack.isEmpty() && slotStack.isEmpty())
                        {
                            ItemStack copyStack = heldStack.copy();
                            GhostItemHelper.setItemGhostAmount(copyStack, 0); // here we set to 0, but thats if the slot was previously empty
                            copyStack.setCount(1);
                            slot.set(copyStack);

//									ItemStack filterStack = this.filterStack;

                                    /* this does literally the same thing, so might as well cut it out
									if (filterStack.getItem() instanceof IRoutingFilterProvider)
									{
										ItemStack filterCopy = ((IRoutingFilterProvider) filterStack.getItem()).getContainedStackForItem(filterStack, heldStack);
										slot.set(filterCopy);
									}
                                     */
                        }
                    } else
                    // Right mouse click-eth away
                    {
                        slot.set(ItemStack.EMPTY);
                    }
                }
            }
        }

        super.clicked(slotId, dragType, clickTypeIn, player);
    }

    @Override
    public void setData(int p_38855_, int p_38856_) {
        super.setData(p_38855_, p_38856_);
        broadcastChanges();
    }

    @Override
	public boolean stillValid(Player entityPlayer)
	{
		return true;
	}

    @Override
	public ItemStack quickMoveStack(Player entityPlayer, int slotIndex)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(slotIndex);

		if (slot != null && slot.hasItem())
		{
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();

			if (slotIndex >= 0)
			{
//                return null;
				if (itemstack1.getItem() instanceof IRoutingFilterProvider) // Change to check item is a filter
				{
					if (!this.moveItemStackTo(itemstack1, 0, 1, false))
					{
						return ItemStack.EMPTY;
					}
				}
			} else if (!this.moveItemStackTo(itemstack1, slotsOccupied, 36 + slotsOccupied, false))
			{
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty())
			{
				slot.set(ItemStack.EMPTY);
			} else
			{
				slot.setChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount())
			{
				return ItemStack.EMPTY;
			}

			slot.onTake(entityPlayer, itemstack1);
		}

		return itemstack;
	}

	public class SlotGhostItem extends SlotItemHandler
	{
        public SlotGhostItem(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
		public boolean mayPlace(ItemStack stack)
		{
			return false;
		}

		@Override
		public boolean mayPickup(Player playerIn)
		{
			return false;
		}
	}

	private class SlotDisabled extends Slot
	{
		public SlotDisabled(Container inventory, int slotIndex, int x, int y)
		{
			super(inventory, slotIndex, x, y);
		}

		@Override
		public boolean mayPlace(ItemStack itemStack)
		{
			return false;
		}

		@Override
		public boolean mayPickup(Player player)
		{
			return false;
		}
	}
}
