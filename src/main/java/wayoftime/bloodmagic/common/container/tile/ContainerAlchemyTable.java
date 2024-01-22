package wayoftime.bloodmagic.common.container.tile;

import javax.annotation.Nullable;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.item.IBloodOrb;
import wayoftime.bloodmagic.common.tile.TileAlchemyTable;

public class ContainerAlchemyTable extends AbstractContainerMenu
{
	public final TileAlchemyTable tileTable;

//	public ContainerSoulForge(InventoryPlayer inventoryPlayer, IInventory tileForge)
//	{
//		this.tileForge = tileForge;
//
//	}

	public ContainerAlchemyTable(int windowId, Inventory playerInventory, FriendlyByteBuf extraData)
	{
		this((TileAlchemyTable) playerInventory.player.level().getBlockEntity(extraData.readBlockPos()), windowId, playerInventory);
	}

	public ContainerAlchemyTable(@Nullable TileAlchemyTable tile, int windowId, Inventory playerInventory)
	{
		super(BloodMagicBlocks.ALCHEMY_TABLE_CONTAINER.get(), windowId);
		this.tileTable = tile;
		this.setup(playerInventory, tile);
	}

	public void setup(Inventory inventory, Container tileForge)
	{
		this.addSlot(new Slot(tileTable, 0, 62, 15));
		this.addSlot(new Slot(tileTable, 1, 80, 51));
		this.addSlot(new Slot(tileTable, 2, 62, 87));
		this.addSlot(new Slot(tileTable, 3, 26, 87));
		this.addSlot(new Slot(tileTable, 4, 8, 51));
		this.addSlot(new Slot(tileTable, 5, 26, 15));
		this.addSlot(new SlotOrb(tileTable, TileAlchemyTable.orbSlot, 143, 24));
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
	public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player)
	{
		if (slotId <= TileAlchemyTable.outputSlot && slotId >= 0)
		{
			Slot slot = this.getSlot(slotId);
			if (!slot.hasItem() && this.getCarried().isEmpty())
			{
//				((TileAlchemyTable) tileTable).toggleInputSlotAccessible(slotId);
				if (tileTable.activeSlot == slotId)
				{
					tileTable.activeSlot = -1;
				} else
				{
					tileTable.activeSlot = slotId;
				}
			}
		}

		super.clicked(slotId, dragType, clickTypeIn, player);
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

			if (index == 7)
			{
				if (!this.moveItemStackTo(itemstack1, 8, 8 + 36, true))
				{
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(itemstack1, itemstack);
			} else if (index > 7)
			{
				if (itemstack1.getItem() instanceof IBloodOrb)
				{
					if (!this.moveItemStackTo(itemstack1, 6, 7, false))
					{
						return ItemStack.EMPTY;
					}
				} else if (!this.moveItemStackTo(itemstack1, 0, 6, false))
				{
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, 8, 8 + 36, false))
			{
				return ItemStack.EMPTY;
			}

			if (itemstack1.getCount() == 0)
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
	public boolean stillValid(Player playerIn)
	{
		return this.tileTable.stillValid(playerIn);
	}

	private class SlotOrb extends Slot
	{
		public SlotOrb(Container inventory, int slotIndex, int x, int y)
		{
			super(inventory, slotIndex, x, y);
		}

		@Override
		public boolean mayPlace(ItemStack itemStack)
		{
			return itemStack.getItem() instanceof IBloodOrb;
		}
	}

	private class SlotOutput extends Slot
	{
		public SlotOutput(Container inventory, int slotIndex, int x, int y)
		{
			super(inventory, slotIndex, x, y);
		}

		@Override
		public boolean mayPlace(ItemStack stack)
		{
			return false;
		}
	}
}