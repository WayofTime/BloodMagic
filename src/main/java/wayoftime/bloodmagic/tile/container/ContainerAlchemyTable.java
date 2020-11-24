package wayoftime.bloodmagic.tile.container;

import javax.annotation.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.item.IBloodOrb;
import wayoftime.bloodmagic.tile.TileAlchemyTable;

public class ContainerAlchemyTable extends Container
{
	public final TileAlchemyTable tileTable;

//	public ContainerSoulForge(InventoryPlayer inventoryPlayer, IInventory tileForge)
//	{
//		this.tileForge = tileForge;
//
//	}

	public ContainerAlchemyTable(int windowId, PlayerInventory playerInventory, PacketBuffer extraData)
	{
		this((TileAlchemyTable) playerInventory.player.world.getTileEntity(extraData.readBlockPos()), windowId, playerInventory);
	}

	public ContainerAlchemyTable(@Nullable TileAlchemyTable tile, int windowId, PlayerInventory playerInventory)
	{
		super(BloodMagicBlocks.ALCHEMY_TABLE_CONTAINER.get(), windowId);
		this.tileTable = tile;
		this.setup(playerInventory, tile);
	}

	public void setup(PlayerInventory inventory, IInventory tileForge)
	{
		this.addSlot(new Slot(tileTable, 0, 62, 15));
		this.addSlot(new Slot(tileTable, 1, 80, 51));
		this.addSlot(new Slot(tileTable, 2, 62, 87));
		this.addSlot(new Slot(tileTable, 3, 26, 87));
		this.addSlot(new Slot(tileTable, 4, 8, 51));
		this.addSlot(new Slot(tileTable, 5, 26, 15));
		this.addSlot(new SlotOrb(tileTable, TileAlchemyTable.orbSlot, 152, 51));
		this.addSlot(new SlotOutput(tileTable, TileAlchemyTable.outputSlot, 44, 51));

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 123 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++)
		{
			addSlot(new Slot(inventory, i, 8 + i * 18, 181));
		}
	}

	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player)
	{
		PlayerInventory inventoryPlayer = player.inventory;

		if (slotId < 6 && slotId >= 0)
		{
			Slot slot = this.getSlot(slotId);
			if (!slot.getHasStack() && inventoryPlayer.getItemStack().isEmpty())
			{
				((TileAlchemyTable) tileTable).toggleInputSlotAccessible(slotId);
			}
		}

		return super.slotClick(slotId, dragType, clickTypeIn, player);
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

			if (index == 7)
			{
				if (!this.mergeItemStack(itemstack1, 8, 8 + 36, true))
				{
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (index > 7)
			{
				if (itemstack1.getItem() instanceof IBloodOrb)
				{
					if (!this.mergeItemStack(itemstack1, 6, 7, false))
					{
						return ItemStack.EMPTY;
					}
				} else if (!this.mergeItemStack(itemstack1, 0, 6, false))
				{
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 8, 8 + 36, false))
			{
				return ItemStack.EMPTY;
			}

			if (itemstack1.getCount() == 0)
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
	public boolean canInteractWith(PlayerEntity playerIn)
	{
		return this.tileTable.isUsableByPlayer(playerIn);
	}

	private class SlotOrb extends Slot
	{
		public SlotOrb(IInventory inventory, int slotIndex, int x, int y)
		{
			super(inventory, slotIndex, x, y);
		}

		@Override
		public boolean isItemValid(ItemStack itemStack)
		{
			return itemStack.getItem() instanceof IBloodOrb;
		}
	}

	private class SlotOutput extends Slot
	{
		public SlotOutput(IInventory inventory, int slotIndex, int x, int y)
		{
			super(inventory, slotIndex, x, y);
		}

		@Override
		public boolean isItemValid(ItemStack stack)
		{
			return false;
		}
	}
}