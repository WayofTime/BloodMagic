package wayoftime.bloodmagic.common.item.routing;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;
import wayoftime.bloodmagic.common.routing.IItemFilter;

public interface IItemFilterProvider extends IRoutingFilterProvider
{
	IItemFilter getInputItemFilter(ItemStack stack, TileEntity tile, IItemHandler handler);

	IItemFilter getOutputItemFilter(ItemStack stack, TileEntity tile, IItemHandler handler);

	void setGhostItemAmount(ItemStack filterStack, int ghostItemSlot, int amount);
}
