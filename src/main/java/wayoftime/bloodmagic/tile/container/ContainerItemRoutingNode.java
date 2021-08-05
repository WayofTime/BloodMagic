package wayoftime.bloodmagic.tile.container;

import javax.annotation.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.item.routing.IRoutingFilterProvider;
import wayoftime.bloodmagic.tile.routing.TileFilteredRoutingNode;

public class ContainerItemRoutingNode extends Container
{
//	private final IInventory tileItemRoutingNode;
	public final TileFilteredRoutingNode tileNode;
	public int lastGhostSlotClicked = -1;
	// private final ItemInventory itemInventory;
	private int slotsOccupied = 1;

	public ContainerItemRoutingNode(int windowId, PlayerInventory playerInventory, PacketBuffer extraData)
	{
		this((TileFilteredRoutingNode) playerInventory.player.world.getTileEntity(extraData.readBlockPos()), windowId, playerInventory);
	}

	public ContainerItemRoutingNode(@Nullable TileFilteredRoutingNode tile, int windowId, PlayerInventory playerInventory)
	{
		super(BloodMagicBlocks.ROUTING_NODE_CONTAINER.get(), windowId);
		this.tileNode = tile;
		this.setup(playerInventory, tile);
	}

	public void setup(PlayerInventory inventory, IInventory tileForge)
	{
//		this.addSlot(new Slot(tileTable, 0, 62, 15));
//		this.addSlot(new Slot(tileTable, 1, 80, 51));
//		this.addSlot(new Slot(tileTable, 2, 62, 87));
//		this.addSlot(new Slot(tileTable, 3, 26, 87));
//		this.addSlot(new Slot(tileTable, 4, 8, 51));
//		this.addSlot(new Slot(tileTable, 5, 26, 15));
//		this.addSlot(new SlotOrb(tileTable, TileAlchemyTable.orbSlot, 143, 24));
//		this.addSlot(new SlotOutput(tileTable, TileAlchemyTable.outputSlot, 44, 51));

		addSlot(new SlotItemFilter(this, tileNode, 0, 71, 33));

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 87 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++)
		{
			addSlot(new Slot(inventory, i, 8 + i * 18, 145));
		}
	}

	public void resetItemInventory(ItemStack masterStack)
	{
//		tileNode.itemInventory.initializeInventory(masterStack);
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index == 0)
			{
				if (!this.mergeItemStack(itemstack1, slotsOccupied, slotsOccupied + 36, true))
				{
					return null;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (index > 0)
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

			slot.onTake(playerIn, itemstack1);
		}

		return itemstack;
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn)
	{
		return this.tileNode.isUsableByPlayer(playerIn);
	}

	private class SlotItemFilter extends Slot
	{
		public ContainerItemRoutingNode container;
		public TileFilteredRoutingNode inventory;

		public SlotItemFilter(ContainerItemRoutingNode container, IInventory inventory, int slotIndex, int x, int y)
		{
			super(inventory, slotIndex, x, y);
			this.container = container;
			this.inventory = (TileFilteredRoutingNode) inventory;
		}

		@Override
		public boolean isItemValid(ItemStack itemStack)
		{
			return itemStack.getItem() instanceof IRoutingFilterProvider; // TODO: Create a new Item that holds the
																			// filter.
		}

		@Override
		public void onSlotChanged()
		{
			super.onSlotChanged();
			container.resetItemInventory(getStack());
			for (int i = 1; i <= 9; i++)
			{
				Slot slot = container.getSlot(i);
				slot.onSlotChanged();
			}
		}

		@Override
		public ItemStack getStack()
		{
			return this.inventory.getStackInSlot(getActiveSlot());
		}

		@Override
		public void putStack(@Nullable ItemStack stack)
		{
			this.inventory.setInventorySlotContents(getActiveSlot(), stack);
			this.onSlotChanged();
		}

		@Override
		public ItemStack decrStackSize(int amount)
		{
			return this.inventory.decrStackSize(getActiveSlot(), amount);
		}

		public int getActiveSlot()
		{
			return inventory.currentActiveSlot;
		}
	}
}