package WayofTime.bloodmagic.api.recipe;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import WayofTime.bloodmagic.api.iface.ICustomAlchemyConsumable;

public class AlchemyTableCustomRecipe extends AlchemyTableRecipe
{
    public AlchemyTableCustomRecipe(Block result, int lpDrained, int ticksRequired, int tierRequired, Object... recipe)
    {
        this(new ItemStack(result), lpDrained, ticksRequired, tierRequired, recipe);
    }

    public AlchemyTableCustomRecipe(Item result, int lpDrained, int ticksRequired, int tierRequired, Object... recipe)
    {
        this(new ItemStack(result), lpDrained, ticksRequired, tierRequired, recipe);
    }

    public AlchemyTableCustomRecipe(ItemStack result, int lpDrained, int ticksRequired, int tierRequired, Object... recipe)
    {
        super(result, lpDrained, ticksRequired, tierRequired, recipe);
    }

    @Override
    protected ItemStack getContainerItem(ItemStack stack)
    {
        if (stack == null)
        {
            return null;
        }

        ItemStack copyStack = stack.copy();

        if (copyStack.getItem() instanceof ICustomAlchemyConsumable)
        {
            return ((ICustomAlchemyConsumable) copyStack.getItem()).drainUseOnAlchemyCraft(copyStack);
        }

        if (copyStack.getItem().hasContainerItem(stack))
        {
            return copyStack.getItem().getContainerItem(copyStack);
        }

        copyStack.stackSize--;
        if (copyStack.stackSize <= 0)
        {
            return null;
        }

        return copyStack;
    }
}
