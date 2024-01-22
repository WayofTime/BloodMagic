package wayoftime.bloodmagic.common.container.tile;

import java.util.Optional;

import javax.annotation.Nullable;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.tags.BloodMagicTags;
import wayoftime.bloodmagic.common.tile.TileAlchemicalReactionChamber;

public class ContainerAlchemicalReactionChamber extends AbstractContainerMenu
{
	public final TileAlchemicalReactionChamber tileARC;

//	public ContainerSoulForge(InventoryPlayer inventoryPlayer, IInventory tileARC)
//	{
//		this.tileARC = tileARC;
//
//	}

	public ContainerAlchemicalReactionChamber(int windowId, Inventory playerInventory, FriendlyByteBuf extraData)
	{
		this((TileAlchemicalReactionChamber) playerInventory.player.level().getBlockEntity(extraData.readBlockPos()), windowId, playerInventory);
	}

	public ContainerAlchemicalReactionChamber(@Nullable TileAlchemicalReactionChamber tile, int windowId, Inventory playerInventory)
	{
		super(BloodMagicBlocks.ARC_CONTAINER.get(), windowId);
		this.tileARC = tile;
		this.setup(playerInventory, tile);
	}

	public void setup(Inventory inventory, Container tileARC)
	{
		this.addSlot(new SlotARCTool(tileARC, TileAlchemicalReactionChamber.ARC_TOOL_SLOT, 35, 51));
		for (int i = 0; i < TileAlchemicalReactionChamber.NUM_OUTPUTS; i++)
		{
			this.addSlot(new SlotOutput(tileARC, TileAlchemicalReactionChamber.OUTPUT_SLOT + i, 116, 15 + i * 18));
		}
		this.addSlot(new Slot(tileARC, TileAlchemicalReactionChamber.INPUT_SLOT, 71, 15));
		this.addSlot(new SlotBucket(tileARC, TileAlchemicalReactionChamber.INPUT_BUCKET_SLOT, 8, 15, true));
		this.addSlot(new SlotBucket(tileARC, TileAlchemicalReactionChamber.OUTPUT_BUCKET_SLOT, 152, 87, false));

//		this.addSlot(new SlotSoul(tileARC, TileSoulForge.soulSlot, 152, 51));

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

			if ((index >= 1 && index < 1 + 5) || (index == 7 || index == 8))// Attempting to transfer from output slots
																			// or bucket slots
			{
				if (!this.moveItemStackTo(itemstack1, 9, 9 + 36, true))
				{
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(itemstack1, itemstack);
			} else if (index >= 9) // Attempting to transfer from main inventory
			{
				if (itemstack1.is(BloodMagicTags.ARC_TOOL)) // Try the tool slot first
				{
					if (!this.moveItemStackTo(itemstack1, 0, 1, false))
					{
						return ItemStack.EMPTY;
					}
				} else if (isBucket(itemstack1, true)) // If it's a full bucket, transfer to tank filler slot.
				{
					if (!this.moveItemStackTo(itemstack1, 7, 8, false))
					{
						return ItemStack.EMPTY;
					}
				} else if (isBucket(itemstack1, false)) // If it's an empty bucket, transfer to tank emptier slot.
				{
					if (!this.moveItemStackTo(itemstack1, 8, 9, false))
					{
						return ItemStack.EMPTY;
					}
				} else if (!this.moveItemStackTo(itemstack1, 6, 7, false))
				{
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, 9, 45, false)) // Attempting to transfer from input slots
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
		return this.tileARC.stillValid(playerIn);
	}

	private class SlotARCTool extends Slot
	{
		public SlotARCTool(Container inventory, int slotIndex, int x, int y)
		{
			super(inventory, slotIndex, x, y);
		}

		@Override
		public boolean mayPlace(ItemStack itemStack)
		{
			return itemStack.is(BloodMagicTags.ARC_TOOL);
		}
	}

	private class SlotBucket extends Slot
	{
		private final boolean needsFullBucket;

		public SlotBucket(Container inventory, int slotIndex, int x, int y, boolean needsFullBucket)
		{
			super(inventory, slotIndex, x, y);
			this.needsFullBucket = needsFullBucket;
		}

		@Override
		public boolean mayPlace(ItemStack itemStack)
		{
			Optional<FluidStack> fluidStackOptional = FluidUtil.getFluidContained(itemStack);

			return fluidStackOptional.isPresent() && ((needsFullBucket && !fluidStackOptional.get().isEmpty()) || (!needsFullBucket && fluidStackOptional.get().isEmpty()));
		}
	}

	private static boolean isBucket(ItemStack stack, boolean requiredFull)
	{
		Optional<FluidStack> fluidStackOptional = FluidUtil.getFluidContained(stack);

		return fluidStackOptional.isPresent() && ((requiredFull && !fluidStackOptional.get().isEmpty()) || (!requiredFull && fluidStackOptional.get().isEmpty()));
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