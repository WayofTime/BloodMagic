package WayofTime.bloodmagic.tile.container;

import javax.annotation.Nullable;

import WayofTime.bloodmagic.item.inventory.ItemInventory;
import WayofTime.bloodmagic.item.routing.IItemFilterProvider;
import WayofTime.bloodmagic.tile.routing.TileFilteredRoutingNode;
import WayofTime.bloodmagic.util.GhostItemHelper;
import WayofTime.bloodmagic.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerItemRoutingNode extends Container
{
    private final IInventory tileItemRoutingNode;
//    private final ItemInventory itemInventory;
    private int slotsOccupied;

    private final TileFilteredRoutingNode inventory;

    public int lastGhostSlotClicked = -1;

    public ContainerItemRoutingNode(InventoryPlayer inventoryPlayer, IInventory tileItemRoutingNode)
    {
        this.tileItemRoutingNode = tileItemRoutingNode;
        inventory = (TileFilteredRoutingNode) tileItemRoutingNode;

        this.addSlotToContainer(new SlotItemFilter(this, tileItemRoutingNode, 0, 8, 33));
        ItemInventory itemInventory = inventory.itemInventory;

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                addSlotToContainer(new SlotGhostItem(itemInventory, j + i * 3, 26 + j * 18, 15 + i * 18));
            }
        }

        slotsOccupied = 10;

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 87 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++)
        {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 145));
        }
    }

    public void resetItemInventory(ItemStack masterStack)
    {
        inventory.itemInventory.initializeInventory(masterStack);
    }

    /**
     * Overridden in order to handle ghost item slots.
     */
    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player)
    {
        InventoryPlayer inventoryPlayer = player.inventory;
//        if (!player.worldObj.isRemote)
        {
            if (slotId >= 0)
            {
                Slot slot = this.inventorySlots.get(slotId);

                if (slot instanceof SlotGhostItem) //TODO: make the slot clicking work!
                {
                    lastGhostSlotClicked = slot.getSlotIndex();
                    System.out.println(lastGhostSlotClicked);

                    if ((dragType == 0 || dragType == 1) && (clickTypeIn == ClickType.PICKUP || clickTypeIn == ClickType.QUICK_MOVE))
                    {
                        ItemStack slotStack = slot.getStack();
                        ItemStack heldStack = inventoryPlayer.getItemStack();

                        if (dragType == 0)
                        {
                            if (clickTypeIn == ClickType.PICKUP)
                            {
                                if (heldStack == null && slotStack != null)
                                {
                                    GhostItemHelper.incrementGhostAmout(slotStack, 1);
                                    slot.putStack(slotStack);
                                } else if (heldStack != null)
                                {
                                    if (!((SlotGhostItem) slot).canBeAccessed())
                                    {
                                        return super.slotClick(slotId, dragType, clickTypeIn, player);
                                    }
                                    if (slotStack != null && Utils.canCombine(slotStack, heldStack))
                                    {
                                        GhostItemHelper.incrementGhostAmout(slotStack, heldStack.stackSize);
                                        slot.putStack(slotStack);
                                    } else
                                    {
                                        ItemStack copyStack = heldStack.copy();
                                        GhostItemHelper.setItemGhostAmount(copyStack, copyStack.stackSize);
                                        copyStack.stackSize = 1;
                                        slot.putStack(copyStack);
                                    }
                                }
                            } else
                            {
                                if (slotStack != null)
                                {
                                    GhostItemHelper.setItemGhostAmount(slotStack, GhostItemHelper.getItemGhostAmount(slotStack) / 2);
                                    if (GhostItemHelper.getItemGhostAmount(slotStack) <= 0)
                                    {
                                        slot.putStack(null);
                                    } else
                                    {
                                        slot.putStack(slotStack);
                                    }
                                }
                            }
                        } else
                        {
                            if (clickTypeIn == ClickType.PICKUP)
                            {
                                if (slotStack != null)
                                {
                                    GhostItemHelper.decrementGhostAmount(slotStack, 1);
                                    if (GhostItemHelper.getItemGhostAmount(slotStack) < 0)
                                    {
                                        slot.putStack(null);
                                    } else
                                    {
                                        slot.putStack(slotStack);
                                    }
                                }
                            } else
                            {
                                slot.putStack(null);
                            }
                        }
                    }
                }
            }
        }

        return super.slotClick(slotId, dragType, clickTypeIn, player);
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
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

            if (index == 0)
            {
                if (!this.mergeItemStack(itemstack1, slotsOccupied, slotsOccupied + 36, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index > 0)
            {
//                return null;
                if (itemstack1.getItem() instanceof IItemFilterProvider) // Change to check item is a filter
                {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                    {
                        return null;
                    }
                }
            } else if (!this.mergeItemStack(itemstack1, 0 + slotsOccupied, 36 + slotsOccupied, false))
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
        return this.tileItemRoutingNode.isUseableByPlayer(playerIn);
    }

    private class SlotItemFilter extends Slot
    {
        public ContainerItemRoutingNode container;
        public TileFilteredRoutingNode inventory;

        public SlotItemFilter(ContainerItemRoutingNode container, IInventory inventory, int slotIndex, int x, int y)
        {
            super(inventory, slotIndex, x, y);
            this.container = container;
            this.inventory = (TileFilteredRoutingNode) inventory;
        }

        @Override
        public boolean isItemValid(ItemStack itemStack)
        {
            return itemStack.getItem() instanceof IItemFilterProvider; //TODO: Create a new Item that holds the filter.
        }

        @Override
        public void onSlotChanged()
        {
            super.onSlotChanged();
            container.resetItemInventory(getStack());
            for (int i = 1; i <= 9; i++)
            {
                Slot slot = container.getSlot(i);
                slot.onSlotChanged();
            }
        }

        @Override
        public ItemStack getStack()
        {
            return this.inventory.getStackInSlot(getActiveSlot());
        }

        @Override
        public void putStack(@Nullable ItemStack stack)
        {
            this.inventory.setInventorySlotContents(getActiveSlot(), stack);
            this.onSlotChanged();
        }

        @Override
        public ItemStack decrStackSize(int amount)
        {
            return this.inventory.decrStackSize(getActiveSlot(), amount);
        }

        public int getActiveSlot()
        {
            return inventory.currentActiveSlot;
        }
    }

    private class SlotGhostItem extends Slot
    {
        private ItemInventory itemInv;

        public SlotGhostItem(ItemInventory inventory, int slotIndex, int x, int y)
        {
            super(inventory, slotIndex, x, y);
            itemInv = inventory;
        }

        @Override
        public boolean isItemValid(ItemStack stack)
        {
            return false;
        }

        @Override
        public boolean canTakeStack(EntityPlayer playerIn)
        {
            return false;
        }

//        @Override
//        public boolean isHere(IInventory inv, int slotIn)
//        {
//            return itemInv.canInventoryBeManipulated() && super.isHere(inv, slotIn);
//        }

        public boolean canBeAccessed()
        {
            return itemInv.canInventoryBeManipulated();
        }
    }
}
