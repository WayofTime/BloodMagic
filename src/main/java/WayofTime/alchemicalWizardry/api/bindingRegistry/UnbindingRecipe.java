package WayofTime.alchemicalWizardry.api.bindingRegistry;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class UnbindingRecipe
{
    public ItemStack requiredItem;
    public List<ItemStack> outputItem;

    public UnbindingRecipe(ItemStack inputItem, List<ItemStack> outputItem)
    {
        this.requiredItem = inputItem;
        this.outputItem = outputItem;
    }

    public UnbindingRecipe(ItemStack inputItem, ItemStack outputItem)
    {
        this.requiredItem = inputItem;
        List<ItemStack> newList = new ArrayList<ItemStack>();
        newList.add(outputItem);
        this.outputItem = newList;
    }

    public boolean doesRequiredItemMatch(ItemStack testStack)
    {
        return !(testStack == null || this.requiredItem == null) && this.requiredItem.isItemEqual(testStack);
    }

    public List<ItemStack> getResult()
    {
        return this.outputItem;
    }
}
