package wayoftime.bloodmagic.common.container.tile;

import javax.annotation.Nullable;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import wayoftime.bloodmagic.api.compat.IDemonWill;
import wayoftime.bloodmagic.api.compat.IDemonWillGem;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.tile.TileSoulForge;

public class ContainerSoulForge extends AbstractContainerMenu
{
	public final Container tileForge;
	public final ContainerData data;

//	public ContainerSoulForge(InventoryPlayer inventoryPlayer, IInventory tileForge)
//	{
//		this.tileForge = tileForge;
//
//	}

	public ContainerSoulForge(int windowId, Inventory playerInventory, FriendlyByteBuf extraData)
	{
		this((TileSoulForge) playerInventory.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(5), windowId, playerInventory);
	}

	public ContainerSoulForge(@Nullable TileSoulForge tile, ContainerData data, int windowId, Inventory playerInventory)
	{
		super(BloodMagicBlocks.SOUL_FORGE_CONTAINER.get(), windowId);
		this.tileForge = tile;
		this.setup(playerInventory, tile);
		this.data = data;
		this.addDataSlots(data);
	}

	public void setup(Inventory inventory, Container tileForge)
	{
		this.addSlot(new Slot(tileForge, 0, 8, 15));
		this.addSlot(new Slot(tileForge, 1, 80, 15));
		this.addSlot(new Slot(tileForge, 2, 8, 87));
		this.addSlot(new Slot(tileForge, 3, 80, 87));
		this.addSlot(new SlotSoul(tileForge, TileSoulForge.soulSlot, 152, 51));
		this.addSlot(new SlotOutput(tileForge, TileSoulForge.outputSlot, 44, 51));

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
	public ItemStack quickMoveStack(Player playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);

		if (slot != null && slot.hasItem())
		{
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();

			if (index == 5)
			{
				if (!this.moveItemStackTo(itemstack1, 6, 6 + 36, true))
				{
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(itemstack1, itemstack);
			} else if (index > 5)
			{
				if (itemstack1.getItem() instanceof IDemonWill || itemstack1.getItem() instanceof IDemonWillGem)
				{
					if (!this.moveItemStackTo(itemstack1, 4, 5, false))
					{
						return ItemStack.EMPTY;
					}
				} else if (!this.moveItemStackTo(itemstack1, 0, 4, false))
				{
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, 6, 42, false))
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
		return this.tileForge.stillValid(playerIn);
	}

	private class SlotSoul extends Slot
	{
		public SlotSoul(Container inventory, int slotIndex, int x, int y)
		{
			super(inventory, slotIndex, x, y);
		}

		@Override
		public boolean mayPlace(ItemStack itemStack)
		{
			return itemStack.getItem() instanceof IDemonWillGem || itemStack.getItem() instanceof IDemonWill;
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
