package WayofTime.bloodmagic.tile.routing;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import WayofTime.bloodmagic.item.inventory.ItemInventory;
import WayofTime.bloodmagic.routing.IInputItemRoutingNode;
import WayofTime.bloodmagic.routing.IItemFilter;
import WayofTime.bloodmagic.routing.TestItemFilter;
import WayofTime.bloodmagic.util.GhostItemHelper;

public class TileInputRoutingNode extends TileFilteredRoutingNode implements IInputItemRoutingNode
{
    public TileInputRoutingNode()
    {
        super(7, "inputNode");
    }

    @Override
    public boolean isInput(EnumFacing side)
    {
        return true;
    }

    @Override
    public IItemFilter getInputFilterForSide(EnumFacing side)
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

                ItemStack ghostResult = GhostItemHelper.getStackFromGhost(stack);
//                if (ghostResult.stackSize == 0)
//                {
//                    ghostResult.stackSize = Int.MaxValue();
//                }

                filteredList.add(ghostResult);
            }

            testFilter.initializeFilter(filteredList, (IInventory) tile, side.getOpposite(), false);

            return testFilter;
        }

        return null;
    }
}
