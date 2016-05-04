package WayofTime.bloodmagic.item.routing;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;
import WayofTime.bloodmagic.routing.IItemFilter;

public interface IItemFilterProvider
{
    IItemFilter getInputItemFilter(ItemStack stack, TileEntity tile, IItemHandler handler);

    IItemFilter getOutputItemFilter(ItemStack stack, TileEntity tile, IItemHandler handler);
}
