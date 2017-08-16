package WayofTime.bloodmagic.item.routing;

import WayofTime.bloodmagic.routing.IItemFilter;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;

public interface IItemFilterProvider extends IRoutingFilterProvider {
    IItemFilter getInputItemFilter(ItemStack stack, TileEntity tile, IItemHandler handler);

    IItemFilter getOutputItemFilter(ItemStack stack, TileEntity tile, IItemHandler handler);
}
