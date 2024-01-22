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
import wayoftime.bloodmagic.common.item.ITeleposerFocus;
import wayoftime.bloodmagic.common.tile.TileTeleposer;

public class ContainerTeleposer extends AbstractContainerMenu
{
	public final TileTeleposer tileTeleposer;

	public ContainerTeleposer(int windowId, Inventory playerInventory, FriendlyByteBuf extraData)
	{
		this((TileTeleposer) playerInventory.player.level().getBlockEntity(extraData.readBlockPos()), windowId, playerInventory);
	}

	public ContainerTeleposer(@Nullable TileTeleposer tile, int windowId, Inventory playerInventory)
	{
		super(BloodMagicBlocks.TELEPOSER_CONTAINER.get(), windowId);
		this.tileTeleposer = tile;
		this.setup(playerInventory, tile);
	}

	public void setup(Inventory inventory, Container tileARC)
	{
		this.addSlot(new SlotFocus(tileARC, TileTeleposer.FOCUS_SLOT, 80, 15));

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
	public ItemStack quickMoveStack(Player playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);

		if (slot != null && slot.hasItem())
		{
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();

			if (index == 0)// Attempting to transfer from output slots
							// or bucket slots
			{
				if (!this.moveItemStackTo(itemstack1, 1, 1 + 36, true))
				{
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(itemstack1, itemstack);
			} else if (index >= 1) // Attempting to transfer from main inventory
			{
				if (itemstack1.getItem() instanceof ITeleposerFocus) // Try the tool slot first
				{
					if (!this.moveItemStackTo(itemstack1, 0, 1, false))
					{
						return ItemStack.EMPTY;
					}
				}
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
		return this.tileTeleposer.stillValid(playerIn);
	}

	private class SlotFocus extends Slot
	{
		public SlotFocus(Container inventory, int slotIndex, int x, int y)
		{
			super(inventory, slotIndex, x, y);
		}

		@Override
		public boolean mayPlace(ItemStack itemStack)
		{
			return itemStack.getItem() instanceof ITeleposerFocus;
		}
	}
}