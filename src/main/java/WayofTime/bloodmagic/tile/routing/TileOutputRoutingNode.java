package WayofTime.bloodmagic.tile.routing;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import WayofTime.bloodmagic.item.inventory.ItemInventory;
import WayofTime.bloodmagic.routing.IItemFilter;
import WayofTime.bloodmagic.routing.IOutputItemRoutingNode;
import WayofTime.bloodmagic.routing.TestItemFilter;
import WayofTime.bloodmagic.util.GhostItemHelper;

public class TileOutputRoutingNode extends TileFilteredRoutingNode implements IOutputItemRoutingNode
{
    public TileOutputRoutingNode()
    {
        super(7, "outputNode");
    }

    @Override
    public boolean isOutput(EnumFacing side)
    {
        return true;
    }

    @Override
    public IItemFilter getOutputFilterForSide(EnumFacing side)
    {
        ItemStack filterStack = this.getFilterStack(side);

        if (filterStack == null)
        {
            return null;
        }

        TileEntity tile = worldObj.getTileEntity(pos.offset(side));
        if (tile instanceof IInventory)
        {
            IItemFilter testFilter = new TestItemFilter();
            List<ItemStack> filteredList = new LinkedList<ItemStack>();
            ItemInventory inv = new ItemInventory(filterStack, 9, ""); //TODO: Change to grab the filter from the Item later.
            for (int i = 0; i < inv.getSizeInventory(); i++)
            {
                ItemStack stack = inv.getStackInSlot(i);
                if (stack == null)
                {
                    continue;
                }

                filteredList.add(GhostItemHelper.getStackFromGhost(stack));
            }

            testFilter.initializeFilter(filteredList, (IInventory) tile, side, true);

            return testFilter;
        }

        return null;
    }
}
