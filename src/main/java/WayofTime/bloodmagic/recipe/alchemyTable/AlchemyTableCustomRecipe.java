package WayofTime.bloodmagic.recipe.alchemyTable;

import WayofTime.bloodmagic.iface.ICustomAlchemyConsumable;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AlchemyTableCustomRecipe extends AlchemyTableRecipe {
    public AlchemyTableCustomRecipe(Block result, int lpDrained, int ticksRequired, int tierRequired, Object... recipe) {
        this(new ItemStack(result), lpDrained, ticksRequired, tierRequired, recipe);
    }

    public AlchemyTableCustomRecipe(Item result, int lpDrained, int ticksRequired, int tierRequired, Object... recipe) {
        this(new ItemStack(result), lpDrained, ticksRequired, tierRequired, recipe);
    }

    public AlchemyTableCustomRecipe(ItemStack result, int lpDrained, int ticksRequired, int tierRequired, Object... recipe) {
        super(result, lpDrained, ticksRequired, tierRequired, recipe);
    }

    @Override
    protected ItemStack getContainerItem(ItemStack stack) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack copyStack = stack.copy();

        if (copyStack.getItem() instanceof ICustomAlchemyConsumable) {
            return ((ICustomAlchemyConsumable) copyStack.getItem()).drainUseOnAlchemyCraft(copyStack);
        }

        if (copyStack.getItem().hasContainerItem(stack)) {
            return copyStack.getItem().getContainerItem(copyStack);
        }

        copyStack.shrink(1);
        if (copyStack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        return copyStack;
    }
}
