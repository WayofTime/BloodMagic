package pneumaticCraft.api.item;

import java.util.List;

import net.minecraft.item.ItemStack;

/**
 *  Implement this interface for your items that have an inventory. When you don't have access to the item, just create any old class
 *  that implements this interface and register an instance of it in PneumaticRegistry.
 *  This will then will be used in the Pneumatic Helmet's item search.
 *
 */
public interface IInventoryItem{

    /**
     *  @parm stack: Item that potentially has an inventory.
     *  @parm curStacks: List of all currently added stacks for this item. Add more stacks in here in your implementation when found the right item.
     */
    public void getStacksInItem(ItemStack stack, List<ItemStack> curStacks);
}
