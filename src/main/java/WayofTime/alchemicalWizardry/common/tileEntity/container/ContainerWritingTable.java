package WayofTime.alchemicalWizardry.common.tileEntity.container;

import WayofTime.alchemicalWizardry.api.items.interfaces.IBloodOrb;
import WayofTime.alchemicalWizardry.common.tileEntity.TEWritingTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerWritingTable extends Container
{
    protected TEWritingTable tileEntity;

    public ContainerWritingTable(InventoryPlayer inventoryPlayer, TEWritingTable te)
    {
        tileEntity = te;
        addSlotToContainer(new Slot(tileEntity, 0, 152, 110));
        addSlotToContainer(new Slot(tileEntity, 1, 80, 18));
        addSlotToContainer(new Slot(tileEntity, 2, 33, 52));
        addSlotToContainer(new Slot(tileEntity, 3, 51, 110));
        addSlotToContainer(new Slot(tileEntity, 4, 109, 110));
        addSlotToContainer(new Slot(tileEntity, 5, 127, 52));
        addSlotToContainer(new Slot(tileEntity, 6, 80, 67));
        bindPlayerInventory(inventoryPlayer);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return tileEntity.isUseableByPlayer(player);
    }

    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer)
    {
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 140 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++)
        {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 198));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot)
    {
        ItemStack stack = null;
        Slot slotObject = (Slot) inventorySlots.get(slot);
        if (slotObject != null && slotObject.getHasStack())
        {
            ItemStack stackInSlot = slotObject.getStack();
            stack = stackInSlot.copy();
            if (slot <= 6)
            {
                if (!this.mergeItemStack(stackInSlot, 7, 43, true))
                {
                    return null;
                }
            } else if (stack.getItem() instanceof IBloodOrb)
            {
                if (!this.mergeItemStack(stackInSlot, 0, 1, false))
                {
                    return null;
                }
            } else if (!this.mergeItemStack(stackInSlot, 1, 6, false))
            {
                return null;
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
}