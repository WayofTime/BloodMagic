package WayofTime.bloodmagic.item.routing;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import WayofTime.bloodmagic.routing.IItemFilter;

public interface IItemFilterProvider
{
    public IItemFilter getInputItemFilter(ItemStack stack, IInventory inventory, EnumFacing syphonDirection);

    public IItemFilter getOutputItemFilter(ItemStack stack, IInventory inventory, EnumFacing syphonDirection);
}
