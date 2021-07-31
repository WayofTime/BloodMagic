package wayoftime.bloodmagic.common.item.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.thread.EffectiveSide;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.item.routing.IRoutingFilterProvider;
import wayoftime.bloodmagic.util.GhostItemHelper;

public class ContainerFilter extends Container
{
	public final InventoryFilter inventoryFilter;
	private final int PLAYER_INVENTORY_ROWS = 3;
	private final int PLAYER_INVENTORY_COLUMNS = 9;
	private final PlayerEntity player;

	public int lastGhostSlotClicked = -1;
	private int slotsOccupied = 9;

	public ContainerFilter(int windowId, PlayerInventory playerInventory, PacketBuffer extraData)
	{
		this(windowId, playerInventory.player, playerInventory, new InventoryFilter(extraData.readItemStack()));
	}

	public ContainerFilter(int windowId, PlayerEntity player, PlayerInventory playerInventory, InventoryFilter inventoryFilter)
	{
		super(BloodMagicBlocks.FILTER_CONTAINER.get(), windowId);
		this.player = player;
		this.inventoryFilter = inventoryFilter;
		int currentSlotHeldIn = player.inventory.currentItem;
		this.setup(playerInventory, currentSlotHeldIn);
	}

	public void setup(PlayerInventory inventory, int currentSlotHeldIn)
	{
//		for (int columnIndex = 0; columnIndex < ItemRouterFilter.inventorySize; ++columnIndex)
//		{
//			this.addSlot(new SlotGhostItem(this, inventoryFilter, player, columnIndex, 8 + columnIndex * 36, 17));
//		}

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				this.addSlot(new SlotGhostItem(this, inventoryFilter, player, j + i * 3, 116 + j * 18, 15 + i * 18));
//				addSlot(new SlotGhostItem(itemInventory, j + i * 3, 26 + j * 18, 15 + i * 18));
			}
		}

		for (int rowIndex = 0; rowIndex < PLAYER_INVENTORY_ROWS; ++rowIndex)
		{
			for (int columnIndex = 0; columnIndex < PLAYER_INVENTORY_COLUMNS; ++columnIndex)
			{
				this.addSlot(new Slot(player.inventory, columnIndex + rowIndex * 9 + 9, 8 + columnIndex * 18, 105 + rowIndex * 18));
			}
		}

		for (int actionBarIndex = 0; actionBarIndex < PLAYER_INVENTORY_COLUMNS; ++actionBarIndex)
		{
			if (actionBarIndex == currentSlotHeldIn)
			{
				this.addSlot(new SlotDisabled(player.inventory, actionBarIndex, 8 + actionBarIndex * 18, 163));
			} else
			{
				this.addSlot(new Slot(player.inventory, actionBarIndex, 8 + actionBarIndex * 18, 163));
			}
		}

	}

	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player)
	{
		PlayerInventory inventoryPlayer = player.inventory;
//      if (!player.worldObj.isRemote)
		{
			if (slotId >= 0)
			{
				Slot slot = this.inventorySlots.get(slotId);

				if (slot instanceof SlotGhostItem) // TODO: make the slot clicking work!
				{
					lastGhostSlotClicked = slot.getSlotIndex();
					if ((dragType == 0 || dragType == 1))
					{
						ItemStack slotStack = slot.getStack();
						ItemStack heldStack = inventoryPlayer.getItemStack();

						if (dragType == 0) // Left mouse click-eth
						{
							{
								if (heldStack.isEmpty() && !slotStack.isEmpty())
								{
									// I clicked on the slot with an empty hand. Selecting!
								} else if (!heldStack.isEmpty() && slotStack.isEmpty())
								{
									if (!((SlotGhostItem) slot).canBeAccessed())
									{
										return super.slotClick(slotId, dragType, clickTypeIn, player);
									}

									ItemStack copyStack = heldStack.copy();
									GhostItemHelper.setItemGhostAmount(copyStack, 0);
									copyStack.setCount(1);
									slot.putStack(copyStack);

									ItemStack filterStack = this.inventorySlots.get(0).getStack();
									if (filterStack.getItem() instanceof IRoutingFilterProvider)
									{
										ItemStack filterCopy = ((IRoutingFilterProvider) filterStack.getItem()).getContainedStackForItem(filterStack, heldStack);
										slot.putStack(filterCopy);
									}
								}
							}
						} else
						// Right mouse click-eth away
						{
							slot.putStack(ItemStack.EMPTY);
						}
					}
				}
			}
		}

		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}

	@Override
	public boolean canInteractWith(PlayerEntity entityPlayer)
	{
		return true;
	}

	@Override
	public void onContainerClosed(PlayerEntity entityPlayer)
	{
		super.onContainerClosed(entityPlayer);

		if (!entityPlayer.getEntityWorld().isRemote)
		{
			saveInventory(entityPlayer);
		}
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();

		if (!player.getEntityWorld().isRemote)
		{
			saveInventory(player);
		}
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity entityPlayer, int slotIndex)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(slotIndex);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (slotIndex >= 0)
			{
//                return null;
				if (itemstack1.getItem() instanceof IRoutingFilterProvider) // Change to check item is a filter
				{
					if (!this.mergeItemStack(itemstack1, 0, 1, false))
					{
						return ItemStack.EMPTY;
					}
				}
			} else if (!this.mergeItemStack(itemstack1, slotsOccupied, 36 + slotsOccupied, false))
			{
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty())
			{
				slot.putStack(ItemStack.EMPTY);
			} else
			{
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount())
			{
				return ItemStack.EMPTY;
			}

			slot.onTake(entityPlayer, itemstack1);
		}

		return itemstack;
	}

	public void saveInventory(PlayerEntity entityPlayer)
	{
		inventoryFilter.onGuiSaved(entityPlayer);
	}

	private class SlotGhostItem extends Slot
	{
		private final PlayerEntity player;
		private ContainerFilter containerHolding;

		public SlotGhostItem(ContainerFilter containerHolding, IInventory inventory, PlayerEntity player, int slotIndex, int x, int y)
		{
			super(inventory, slotIndex, x, y);
			this.player = player;
			this.containerHolding = containerHolding;
		}

		@Override
		public void onSlotChanged()
		{
			super.onSlotChanged();

			if (EffectiveSide.get().isServer())
			{
				containerHolding.saveInventory(player);
			}
		}

		@Override
		public boolean isItemValid(ItemStack stack)
		{
			return false;
		}

		@Override
		public boolean canTakeStack(PlayerEntity playerIn)
		{
			return false;
		}

		public boolean canBeAccessed()
		{
			return containerHolding.inventoryFilter.canInventoryBeManipulated();
		}
	}

	private class SlotDisabled extends Slot
	{
		public SlotDisabled(IInventory inventory, int slotIndex, int x, int y)
		{
			super(inventory, slotIndex, x, y);
		}

		@Override
		public boolean isItemValid(ItemStack itemStack)
		{
			return false;
		}

		@Override
		public boolean canTakeStack(PlayerEntity player)
		{
			return false;
		}
	}

//	private class SlotGhostItem extends Slot
//	{
//		private ItemInventory itemInv;
//
//		public SlotGhostItem(ItemInventory inventory, int slotIndex, int x, int y)
//		{
//			super(inventory, slotIndex, x, y);
//			itemInv = inventory;
//		}
//
//		@Override
//		public boolean isItemValid(ItemStack stack)
//		{
//			return false;
//		}
//
//		@Override
//		public boolean canTakeStack(PlayerEntity playerIn)
//		{
//			return false;
//		}
//
//		public boolean canBeAccessed()
//		{
//			return itemInv.canInventoryBeManipulated();
//		}
//	}
}
