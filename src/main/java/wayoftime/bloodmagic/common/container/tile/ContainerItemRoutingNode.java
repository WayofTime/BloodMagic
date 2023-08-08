package wayoftime.bloodmagic.common.container.tile;

import javax.annotation.Nullable;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.item.routing.IRoutingFilterProvider;
import wayoftime.bloodmagic.common.tile.routing.TileFilteredRoutingNode;

public class ContainerItemRoutingNode extends AbstractContainerMenu
{
//	private final IInventory tileItemRoutingNode;
	public final TileFilteredRoutingNode tileNode;
	public int lastGhostSlotClicked = -1;
	// private final ItemInventory itemInventory;
	private int slotsOccupied = 1;

	public ContainerItemRoutingNode(int windowId, Inventory playerInventory, FriendlyByteBuf extraData)
	{
		this((TileFilteredRoutingNode) playerInventory.player.level().getBlockEntity(extraData.readBlockPos()), windowId, playerInventory);
	}

	public ContainerItemRoutingNode(@Nullable TileFilteredRoutingNode tile, int windowId, Inventory playerInventory)
	{
		super(BloodMagicBlocks.ROUTING_NODE_CONTAINER.get(), windowId);
		this.tileNode = tile;
		this.setup(playerInventory, tile);
	}

	public void setup(Inventory inventory, Container tileForge)
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
	public ItemStack quickMoveStack(Player playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);

		if (slot != null && slot.hasItem())
		{
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();

			if (index == 0)
			{
				if (!this.moveItemStackTo(itemstack1, slotsOccupied, slotsOccupied + 36, true))
				{
					return null;
				}

				slot.onQuickCraft(itemstack1, itemstack);
			} else if (index > 0)
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

			slot.onTake(playerIn, itemstack1);
		}

		return itemstack;
	}

	@Override
	public void broadcastChanges()
	{
		super.broadcastChanges();
	}

	@Override
	public boolean stillValid(Player playerIn)
	{
		return this.tileNode.stillValid(playerIn);
	}

	private class SlotItemFilter extends Slot
	{
		public ContainerItemRoutingNode container;
		public TileFilteredRoutingNode inventory;

		public SlotItemFilter(ContainerItemRoutingNode container, Container inventory, int slotIndex, int x, int y)
		{
			super(inventory, slotIndex, x, y);
			this.container = container;
			this.inventory = (TileFilteredRoutingNode) inventory;
		}

		@Override
		public boolean mayPlace(ItemStack itemStack)
		{
			return itemStack.getItem() instanceof IRoutingFilterProvider; // TODO: Create a new Item that holds the
																			// filter.
		}

		@Override
		public int getMaxStackSize()
		{
			return 1;
		}

		@Override
		public void setChanged()
		{
			super.setChanged();
			container.resetItemInventory(getItem());
			for (int i = 1; i <= 9; i++)
			{
				Slot slot = container.getSlot(i);
				slot.setChanged();
			}
		}

		@Override
		public ItemStack getItem()
		{
			return this.inventory.getItem(getActiveSlot());
		}

		@Override
		public void set(@Nullable ItemStack stack)
		{
			this.inventory.setItem(getActiveSlot(), stack);
			this.setChanged();
		}

		@Override
		public ItemStack remove(int amount)
		{
			return this.inventory.removeItem(getActiveSlot(), amount);
		}

		public int getActiveSlot()
		{
			return inventory.getCurrentActiveSlot();
		}
	}
}