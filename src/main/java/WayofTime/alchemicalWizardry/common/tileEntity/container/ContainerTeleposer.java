package WayofTime.alchemicalWizardry.common.tileEntity.container;

import WayofTime.alchemicalWizardry.common.tileEntity.TETeleposer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerTeleposer extends Container
{
    protected TETeleposer tileEntity;

    public ContainerTeleposer(InventoryPlayer inventoryPlayer, TETeleposer te)
    {
        tileEntity = te;
        addSlotToContainer(new Slot(tileEntity, 0, 80, 67));
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

            if (slot == 7)
            {
                if (!this.mergeItemStack(stackInSlot, 7, 35, true))
                {
                    return null;
                }

                slotObject.onSlotChange(stackInSlot, stack);
            }
            if (slot < 1)
            {
                if (!this.mergeItemStack(stackInSlot, 7, 35, true))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(stackInSlot, 0, 0, false))
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