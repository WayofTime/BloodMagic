package wayoftime.bloodmagic.common.container.item;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.item.inventory.InventoryFilter;
import wayoftime.bloodmagic.common.item.routing.IRoutingFilterProvider;
import wayoftime.bloodmagic.util.GhostItemHelper;

public class ContainerFilter extends AbstractContainerMenu
{
	public final InventoryFilter inventoryFilter;
	private final int PLAYER_INVENTORY_ROWS = 3;
	private final int PLAYER_INVENTORY_COLUMNS = 9;
	public final Player player;

	public int lastGhostSlotClicked = -1;
	private int slotsOccupied = 9;

	public ContainerFilter(int windowId, Inventory playerInventory)
	{
		this(windowId, playerInventory.player, playerInventory, new InventoryFilter(9));
	}

	public ContainerFilter(int windowId, Player player, Inventory playerInventory, InventoryFilter filterInv)
	{
		super(BloodMagicBlocks.FILTER_CONTAINER.get(), windowId);
		this.player = player;
		this.inventoryFilter = filterInv;
		int currentSlotHeldIn = player.getInventory().selected;
		this.setup(playerInventory, currentSlotHeldIn);
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

	@Override
	public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player)
    {
        if (slotId >= 0)
        {
            Slot slot = this.slots.get(slotId);

            if (slot instanceof SlotGhostItem)
            {
                lastGhostSlotClicked = slot.getSlotIndex();
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
	public boolean stillValid(Player entityPlayer)
	{
		return true;
	}

    @Override
    public void removed(Player p_38940_) {
        super.removed(p_38940_);
        player.getMainHandItem().setTag(inventoryFilter.serializeNBT()); // can only ever be in this menu if item is in main hand -> this wont work in 1.21 due to the edit button btw
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
