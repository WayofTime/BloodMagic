package WayofTime.bloodmagic.tile.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import WayofTime.bloodmagic.item.ItemTelepositionFocus;

public class ContainerTeleposer extends Container
{
    private final IInventory tileTeleposer;

    public ContainerTeleposer(InventoryPlayer inventoryPlayer, IInventory tileTeleposer)
    {
        this.tileTeleposer = tileTeleposer;
        this.addSlotToContainer(new SlotTeleposer(tileTeleposer, 0, 80, 33));

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 57 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++)
        {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 115));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot)
    {
        ItemStack stack = null;
        Slot slotObject = inventorySlots.get(slot);
        int slots = inventorySlots.size();

        if (slotObject != null && slotObject.getHasStack())
        {
            ItemStack stackInSlot = slotObject.getStack();
            stack = stackInSlot.copy();

            if (stack.getItem() instanceof ItemTelepositionFocus)
            {
                if (slot <= slots)
                {
                    if (!this.mergeItemStack(stackInSlot, 0, slots, false))
                    {
                        return null;
                    }
                } else if (!this.mergeItemStack(stackInSlot, slots, 36 + slots, false))
                {
                    return null;
                }
            }

            if (stackInSlot.stackSize == 0)
            {
                slotObject.putStack(null);
            } else
            {
                slotObject.onSlotChanged();
            }

            if (stackInSlot.stackSize == stack.stackSize)
            {
                return null;
            }

            slotObject.onPickupFromSlot(player, stackInSlot);
        }

        return stack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.tileTeleposer.isUseableByPlayer(playerIn);
    }

    private class SlotTeleposer extends Slot
    {
        public SlotTeleposer(IInventory inventory, int slotIndex, int x, int y)
        {
            super(inventory, slotIndex, x, y);
        }

        @Override
        public boolean isItemValid(ItemStack itemStack)
        {
            return itemStack.getItem() instanceof ItemTelepositionFocus;
        }
    }
}
