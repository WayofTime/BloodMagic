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
import wayoftime.bloodmagic.common.item.ITeleposerFocus;
import wayoftime.bloodmagic.tile.routing.TileMasterRoutingNode;

public class ContainerMasterRoutingNode extends Container
{
	public final TileMasterRoutingNode tileMasterRoutingNode;

	public ContainerMasterRoutingNode(int windowId, PlayerInventory playerInventory, PacketBuffer extraData)
	{
		this((TileMasterRoutingNode) playerInventory.player.world.getTileEntity(extraData.readBlockPos()), windowId, playerInventory);
	}

	public ContainerMasterRoutingNode(@Nullable TileMasterRoutingNode tile, int windowId, PlayerInventory playerInventory)
	{
		super(BloodMagicBlocks.MASTER_ROUTING_NODE_CONTAINER.get(), windowId);
		this.tileMasterRoutingNode = tile;
		this.setup(playerInventory, tile);
	}

	public void setup(PlayerInventory inventory, IInventory tileARC)
	{
		this.addSlot(new SlotFocus(tileARC, tileMasterRoutingNode.SLOT, 80, 15));

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 39 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++)
		{
			addSlot(new Slot(inventory, i, 8 + i * 18, 97));
		}
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

			if (index == 0)// Attempting to transfer from output slots
							// or bucket slots
			{
				if (!this.mergeItemStack(itemstack1, 1, 1 + 36, true))
				{
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (index >= 1) // Attempting to transfer from main inventory
			{
				if (itemstack1.getItem() instanceof ITeleposerFocus) // Try the tool slot first
				{
					if (!this.mergeItemStack(itemstack1, 0, 1, false))
					{
						return ItemStack.EMPTY;
					}
				}
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
		return this.tileMasterRoutingNode.isUsableByPlayer(playerIn);
	}

	private class SlotFocus extends Slot
	{
		public SlotFocus(IInventory inventory, int slotIndex, int x, int y)
		{
			super(inventory, slotIndex, x, y);
		}

		@Override
		public boolean isItemValid(ItemStack itemStack)
		{
			return itemStack.getItem() instanceof ITeleposerFocus;
		}
	}
}