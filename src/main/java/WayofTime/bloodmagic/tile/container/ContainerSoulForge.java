package WayofTime.bloodmagic.tile.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import WayofTime.bloodmagic.api.soul.IDemonWill;
import WayofTime.bloodmagic.api.soul.IDemonWillGem;
import WayofTime.bloodmagic.tile.TileSoulForge;

public class ContainerSoulForge extends Container
{
    private final IInventory tileForge;

    public ContainerSoulForge(InventoryPlayer inventoryPlayer, IInventory tileForge)
    {
        this.tileForge = tileForge;
        this.addSlotToContainer(new Slot(tileForge, 0, 8, 15));
        this.addSlotToContainer(new Slot(tileForge, 1, 80, 15));
        this.addSlotToContainer(new Slot(tileForge, 2, 80, 87));
        this.addSlotToContainer(new Slot(tileForge, 3, 8, 87));
        this.addSlotToContainer(new SlotSoul(tileForge, TileSoulForge.soulSlot, 152, 51));
        this.addSlotToContainer(new SlotOutput(tileForge, TileSoulForge.outputSlot, 44, 51));

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 123 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++)
        {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 181));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = null;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 5)
            {
                if (!this.mergeItemStack(itemstack1, 6, 6 + 36, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index > 5)
            {
                if (itemstack1.getItem() instanceof IDemonWill || itemstack1.getItem() instanceof IDemonWillGem)
                {
                    if (!this.mergeItemStack(itemstack1, 4, 5, false))
                    {
                        return null;
                    }
                } else if (!this.mergeItemStack(itemstack1, 0, 4, false))
                {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 6, 42, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack(null);
            } else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(playerIn, itemstack1);
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.tileForge.isUseableByPlayer(playerIn);
    }

    private class SlotSoul extends Slot
    {
        public SlotSoul(IInventory inventory, int slotIndex, int x, int y)
        {
            super(inventory, slotIndex, x, y);
        }

        @Override
        public boolean isItemValid(ItemStack itemStack)
        {
            return itemStack.getItem() instanceof IDemonWillGem || itemStack.getItem() instanceof IDemonWill;
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
