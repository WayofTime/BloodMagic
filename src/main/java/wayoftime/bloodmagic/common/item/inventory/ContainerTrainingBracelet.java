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
import wayoftime.bloodmagic.common.item.ItemLivingTome;
import wayoftime.bloodmagic.common.item.ItemLivingTrainer;

public class ContainerTrainingBracelet extends Container
{
	public final InventoryTrainingBracelet inventoryTrainer;
	private final int PLAYER_INVENTORY_ROWS = 3;
	private final int PLAYER_INVENTORY_COLUMNS = 9;
	public final PlayerEntity player;
	public final ItemStack trainerStack;

	public int lastGhostSlotClicked = -1;
	private int slotsOccupied = 16;

	public ContainerTrainingBracelet(int windowId, PlayerInventory playerInventory, PacketBuffer extraData)
	{
		this(windowId, playerInventory.player, playerInventory, extraData.readItemStack());
	}

	public ContainerTrainingBracelet(int windowId, PlayerEntity player, PlayerInventory playerInventory, ItemStack filterStack)
	{
		super(BloodMagicBlocks.TRAINING_BRACELET_CONTAINER.get(), windowId);
		this.player = player;
		this.trainerStack = filterStack;
		this.inventoryTrainer = ((ItemLivingTrainer) filterStack.getItem()).toInventory(filterStack);
		int currentSlotHeldIn = player.inventory.currentItem;
		this.setup(playerInventory, currentSlotHeldIn);
	}

	public void setup(PlayerInventory inventory, int currentSlotHeldIn)
	{
//		for (int columnIndex = 0; columnIndex < ItemRouterFilter.inventorySize; ++columnIndex)
//		{
//			this.addSlot(new SlotGhostItem(this, InventoryTrainingBracelet, player, columnIndex, 8 + columnIndex * 36, 17));
//		}

		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				this.addSlot(new SlotTomeItem(this, inventoryTrainer, player, j + i * 4, 110 + j * 21 - 21, 15 + i * 21));
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

				if (slot instanceof SlotTomeItem) // TODO: make the slot clicking work!
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
									// Return here to not save the server-side inventory
									return ItemStack.EMPTY;
								} else if (!heldStack.isEmpty() && slotStack.isEmpty() && heldStack.getItem() instanceof ItemLivingTome)
								{
									if (!((SlotTomeItem) slot).canBeAccessed())
									{
										return super.slotClick(slotId, dragType, clickTypeIn, player);
									}

									ItemStack copyStack = heldStack.copy();
//									GhostItemHelper.setItemGhostAmount(copyStack, 0);
									copyStack.setCount(1);
									slot.putStack(copyStack);

//									ItemStack filterStack = this.filterStack;
//									if (trainerStack.getItem() instanceof IRoutingFilterProvider)
//									{
//										ItemStack filterCopy = ((IRoutingFilterProvider) trainerStack.getItem()).getContainedStackForItem(trainerStack, heldStack);
//										slot.putStack(filterCopy);
//									}
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

//	@Override
//	public void onContainerClosed(PlayerEntity entityPlayer)
//	{
//		super.onContainerClosed(entityPlayer);
//
//		if (!entityPlayer.getEntityWorld().isRemote)
//		{
//			saveInventory(entityPlayer);
//		}
//	}
//
//	@Override
//	public void detectAndSendChanges()
//	{
//		super.detectAndSendChanges();
//
//		if (!player.getEntityWorld().isRemote)
//		{
//			saveInventory(player);
//		}
//	}

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
//				if (itemstack1.getItem() instanceof IRoutingFilterProvider) // Change to check item is a filter
//				{
//					if (!this.mergeItemStack(itemstack1, 0, 1, false))
//					{
//						return ItemStack.EMPTY;
//					}
//				}
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

//	public void saveInventory(PlayerEntity entityPlayer)
//	{
////		inventoryTrainer.onGuiSaved(entityPlayer);
//
//	}

	public void saveInventory(PlayerEntity player, int slot)
	{
		ItemStack masterStack = inventoryTrainer.findParentStack(player);
		InventoryTrainingBracelet storedInv = new InventoryTrainingBracelet(masterStack);
		storedInv.setInventorySlotContents(slot, getSlot(slot).getStack());
		storedInv.save();
	}

	private class SlotTomeItem extends Slot
	{
		private final PlayerEntity player;
		private ContainerTrainingBracelet containerHolding;

		public SlotTomeItem(ContainerTrainingBracelet containerHolding, IInventory inventory, PlayerEntity player, int slotIndex, int x, int y)
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
				containerHolding.saveInventory(player, slotNumber);
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
			return containerHolding.inventoryTrainer.canInventoryBeManipulated();
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
}
