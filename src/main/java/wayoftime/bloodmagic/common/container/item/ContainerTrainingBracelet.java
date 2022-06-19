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
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.item.ItemLivingTome;
import wayoftime.bloodmagic.common.item.ItemLivingTrainer;
import wayoftime.bloodmagic.common.item.inventory.InventoryTrainingBracelet;

public class ContainerTrainingBracelet extends AbstractContainerMenu
{
	public final InventoryTrainingBracelet inventoryTrainer;
	private final int PLAYER_INVENTORY_ROWS = 3;
	private final int PLAYER_INVENTORY_COLUMNS = 9;
	public final Player player;
	public final ItemStack trainerStack;

	public int lastGhostSlotClicked = -1;
	private int slotsOccupied = 16;

	public ContainerTrainingBracelet(int windowId, Inventory playerInventory, FriendlyByteBuf extraData)
	{
		this(windowId, playerInventory.player, playerInventory, extraData.readItem());
	}

	public ContainerTrainingBracelet(int windowId, Player player, Inventory playerInventory, ItemStack filterStack)
	{
		super(BloodMagicBlocks.TRAINING_BRACELET_CONTAINER.get(), windowId);
		this.player = player;
		this.trainerStack = filterStack;
		this.inventoryTrainer = ((ItemLivingTrainer) filterStack.getItem()).toInventory(filterStack);
		int currentSlotHeldIn = player.getInventory().selected;
		this.setup(playerInventory, currentSlotHeldIn);
	}

	public void setup(Inventory inventory, int currentSlotHeldIn)
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
				this.addSlot(new Slot(player.getInventory(), columnIndex + rowIndex * 9 + 9, 8 + columnIndex * 18, 105 + rowIndex * 18));
			}
		}

		for (int actionBarIndex = 0; actionBarIndex < PLAYER_INVENTORY_COLUMNS; ++actionBarIndex)
		{
			if (actionBarIndex == currentSlotHeldIn)
			{
				this.addSlot(new SlotDisabled(player.getInventory(), actionBarIndex, 8 + actionBarIndex * 18, 163));
			} else
			{
				this.addSlot(new Slot(player.getInventory(), actionBarIndex, 8 + actionBarIndex * 18, 163));
			}
		}

	}

	@Override
	public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player)
	{
		Inventory inventoryPlayer = player.getInventory();
//      if (!player.worldObj.isRemote)
		{
			if (slotId >= 0)
			{
				Slot slot = this.slots.get(slotId);

				if (slot instanceof SlotTomeItem) // TODO: make the slot clicking work!
				{
					lastGhostSlotClicked = slot.getSlotIndex();
					if ((dragType == 0 || dragType == 1))
					{
						ItemStack slotStack = slot.getItem();
						ItemStack heldStack = this.getCarried();

						if (dragType == 0) // Left mouse click-eth
						{
							{
								if (heldStack.isEmpty() && !slotStack.isEmpty())
								{
									// I clicked on the slot with an empty hand. Selecting!
									// Return here to not save the server-side inventory
//									return ItemStack.EMPTY;
									return;
								} else if (!heldStack.isEmpty() && slotStack.isEmpty() && heldStack.getItem() instanceof ItemLivingTome)
								{
									if (!((SlotTomeItem) slot).canBeAccessed())
									{
										super.clicked(slotId, dragType, clickTypeIn, player);
										return;
									}

									ItemStack copyStack = heldStack.copy();
//									GhostItemHelper.setItemGhostAmount(copyStack, 0);
									copyStack.setCount(1);
									slot.set(copyStack);

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
							slot.set(ItemStack.EMPTY);
						}
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
//				if (itemstack1.getItem() instanceof IRoutingFilterProvider) // Change to check item is a filter
//				{
//					if (!this.mergeItemStack(itemstack1, 0, 1, false))
//					{
//						return ItemStack.EMPTY;
//					}
//				}
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

//	public void saveInventory(PlayerEntity entityPlayer)
//	{
////		inventoryTrainer.onGuiSaved(entityPlayer);
//
//	}

	public void saveInventory(Player player, int slot)
	{
		ItemStack masterStack = inventoryTrainer.findParentStack(player);
		InventoryTrainingBracelet storedInv = new InventoryTrainingBracelet(masterStack);
		storedInv.setItem(slot, getSlot(slot).getItem());
		storedInv.save();
	}

	private class SlotTomeItem extends Slot
	{
		private final Player player;
		private ContainerTrainingBracelet containerHolding;

		public SlotTomeItem(ContainerTrainingBracelet containerHolding, Container inventory, Player player, int slotIndex, int x, int y)
		{
			super(inventory, slotIndex, x, y);
			this.player = player;
			this.containerHolding = containerHolding;
		}

		@Override
		public void setChanged()
		{
			super.setChanged();

			if (EffectiveSide.get().isServer())
			{
				containerHolding.saveInventory(player, index);
			}
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

		public boolean canBeAccessed()
		{
			return containerHolding.inventoryTrainer.canInventoryBeManipulated();
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
