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
import wayoftime.bloodmagic.common.item.inventory.InventoryFilter;
import wayoftime.bloodmagic.common.item.routing.IRoutingFilterProvider;
import wayoftime.bloodmagic.util.GhostItemHelper;

public class ContainerFilter extends AbstractContainerMenu
{
	public final InventoryFilter inventoryFilter;
	private final int PLAYER_INVENTORY_ROWS = 3;
	private final int PLAYER_INVENTORY_COLUMNS = 9;
	public final Player player;
	public final ItemStack filterStack;

	public int lastGhostSlotClicked = -1;
	private int slotsOccupied = 9;

	public ContainerFilter(int windowId, Inventory playerInventory, FriendlyByteBuf extraData)
	{
		this(windowId, playerInventory.player, playerInventory, extraData.readItem());
	}

	public ContainerFilter(int windowId, Player player, Inventory playerInventory, ItemStack filterStack)
	{
		super(BloodMagicBlocks.FILTER_CONTAINER.get(), windowId);
		this.player = player;
		this.filterStack = filterStack;
		this.inventoryFilter = new InventoryFilter(filterStack);
		int currentSlotHeldIn = player.getInventory().selected;
		this.setup(playerInventory, currentSlotHeldIn);
	}

	public void setup(Inventory inventory, int currentSlotHeldIn)
	{
//		for (int columnIndex = 0; columnIndex < ItemRouterFilter.inventorySize; ++columnIndex)
//		{
//			this.addSlot(new SlotGhostItem(this, inventoryFilter, player, columnIndex, 8 + columnIndex * 36, 17));
//		}

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				this.addSlot(new SlotGhostItem(this, inventoryFilter, player, j + i * 3, 110 + j * 21, 15 + i * 21));
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

				if (slot instanceof SlotGhostItem) // TODO: make the slot clicking work!
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
								} else if (!heldStack.isEmpty() && slotStack.isEmpty())
								{
									if (!((SlotGhostItem) slot).canBeAccessed())
									{
										super.clicked(slotId, dragType, clickTypeIn, player);
										return;
									}

									ItemStack copyStack = heldStack.copy();
									GhostItemHelper.setItemGhostAmount(copyStack, 0);
									copyStack.setCount(1);
									slot.set(copyStack);

//									ItemStack filterStack = this.filterStack;
									if (filterStack.getItem() instanceof IRoutingFilterProvider)
									{
										ItemStack filterCopy = ((IRoutingFilterProvider) filterStack.getItem()).getContainedStackForItem(filterStack, heldStack);
										slot.set(filterCopy);
									}
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

	@Override
	public void removed(Player entityPlayer)
	{
		super.removed(entityPlayer);

		if (!entityPlayer.getCommandSenderWorld().isClientSide)
		{
			saveInventory(entityPlayer);
		}
	}

	@Override
	public void broadcastChanges()
	{
		super.broadcastChanges();

		if (!player.getCommandSenderWorld().isClientSide)
		{
			saveInventory(player);
		}
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

	public void saveInventory(Player entityPlayer)
	{
		inventoryFilter.onGuiSaved(entityPlayer);
	}

	public class SlotGhostItem extends Slot
	{
		private final Player player;
		private ContainerFilter containerHolding;

		public SlotGhostItem(ContainerFilter containerHolding, Container inventory, Player player, int slotIndex, int x, int y)
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
				containerHolding.saveInventory(player);
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
			return containerHolding.inventoryFilter.canInventoryBeManipulated();
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
