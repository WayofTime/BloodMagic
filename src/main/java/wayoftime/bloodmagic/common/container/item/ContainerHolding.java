package wayoftime.bloodmagic.common.container.item;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.item.inventory.InventoryHolding;
import wayoftime.bloodmagic.common.item.sigil.ISigil;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilHolding;

public class ContainerHolding extends AbstractContainerMenu
{
	public final InventoryHolding inventoryHolding;
	private final int PLAYER_INVENTORY_ROWS = 3;
	private final int PLAYER_INVENTORY_COLUMNS = 9;
	private final Player player;

	public ContainerHolding(int windowId, Inventory playerInventory, FriendlyByteBuf extraData)
	{
		this(windowId, playerInventory.player, playerInventory, new InventoryHolding(extraData.readItem()));
	}

	public ContainerHolding(int windowId, Player player, Inventory playerInventory, InventoryHolding inventoryHolding)
	{
		super(BloodMagicBlocks.HOLDING_CONTAINER.get(), windowId);
		this.player = player;
		this.inventoryHolding = inventoryHolding;
		int currentSlotHeldIn = player.getInventory().selected;
		this.setup(playerInventory, currentSlotHeldIn);
	}

	public void setup(Inventory inventory, int currentSlotHeldIn)
	{
		for (int columnIndex = 0; columnIndex < ItemSigilHolding.inventorySize; ++columnIndex)
		{
			this.addSlot(new SlotHolding(this, inventoryHolding, player, columnIndex, 8 + columnIndex * 36, 17));
		}

		for (int rowIndex = 0; rowIndex < PLAYER_INVENTORY_ROWS; ++rowIndex)
		{
			for (int columnIndex = 0; columnIndex < PLAYER_INVENTORY_COLUMNS; ++columnIndex)
			{
				this.addSlot(new Slot(player.getInventory(), columnIndex + rowIndex * 9 + 9, 8 + columnIndex * 18, 41 + rowIndex * 18));
			}
		}

		for (int actionBarIndex = 0; actionBarIndex < PLAYER_INVENTORY_COLUMNS; ++actionBarIndex)
		{
			if (actionBarIndex == currentSlotHeldIn)
			{
				this.addSlot(new SlotDisabled(player.getInventory(), actionBarIndex, 8 + actionBarIndex * 18, 99));
			} else
			{
				this.addSlot(new Slot(player.getInventory(), actionBarIndex, 8 + actionBarIndex * 18, 99));
			}
		}
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
		ItemStack stack = ItemStack.EMPTY;
		Slot slotObject = slots.get(slotIndex);
		int slotCount = slots.size();

		if (slotObject != null && slotObject.hasItem())
		{
			ItemStack stackInSlot = slotObject.getItem();
			stack = stackInSlot.copy();

			if (stack.getItem() instanceof ISigil)
			{
				if (slotIndex < ItemSigilHolding.inventorySize)
				{
					if (!this.moveItemStackTo(stackInSlot, ItemSigilHolding.inventorySize, slotCount, false))
					{
						return ItemStack.EMPTY;
					}
				} else if (!this.moveItemStackTo(stackInSlot, 0, ItemSigilHolding.inventorySize, false))
				{
					return ItemStack.EMPTY;
				}
			} else if (stack.getItem() instanceof ItemSigilHolding)
			{
				if (slotIndex < ItemSigilHolding.inventorySize + (PLAYER_INVENTORY_ROWS * PLAYER_INVENTORY_COLUMNS))
				{
					if (!this.moveItemStackTo(stackInSlot, ItemSigilHolding.inventorySize + (PLAYER_INVENTORY_ROWS * PLAYER_INVENTORY_COLUMNS), slots.size(), false))
					{
						return ItemStack.EMPTY;
					}
				} else if (!this.moveItemStackTo(stackInSlot, ItemSigilHolding.inventorySize, ItemSigilHolding.inventorySize + (PLAYER_INVENTORY_ROWS * PLAYER_INVENTORY_COLUMNS), false))
				{
					return ItemStack.EMPTY;
				}
			}

			if (stackInSlot.isEmpty())
			{
				slotObject.set(ItemStack.EMPTY);
			} else
			{
				slotObject.setChanged();
			}

			if (stackInSlot.getCount() == stack.getCount())
			{
				return ItemStack.EMPTY;
			}

			slotObject.onTake(player, stackInSlot);
		}

		return stack;
	}

	public void saveInventory(Player entityPlayer)
	{
		inventoryHolding.onGuiSaved(entityPlayer);
	}

	private class SlotHolding extends Slot
	{
		private final Player player;
		private ContainerHolding containerHolding;

		public SlotHolding(ContainerHolding containerHolding, Container inventory, Player player, int slotIndex, int x, int y)
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
		public boolean mayPlace(ItemStack itemStack)
		{
			return itemStack.getItem() instanceof ISigil && !(itemStack.getItem() instanceof ItemSigilHolding);
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
