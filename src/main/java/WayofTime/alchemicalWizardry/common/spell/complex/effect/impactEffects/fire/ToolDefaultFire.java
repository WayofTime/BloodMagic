package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire;

import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.tool.ItemManipulator;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import java.util.LinkedList;
import java.util.List;

public class ToolDefaultFire extends ItemManipulator
{
    public ToolDefaultFire(int power, int potency, int cost)
    {
        super(power, potency, cost);
    }

    @Override
    public List<ItemStack> handleItemsOnBlockBroken(ItemStack toolStack, List<ItemStack> itemList)
    {
        LinkedList<ItemStack> newList = new LinkedList();
        for (ItemStack item : itemList)
        {
            ItemStack newItem = FurnaceRecipes.smelting().getSmeltingResult(item);
            if (newItem != null)
            {
                newList.add(newItem);
            } else
            {
                newList.add(item);
            }
        }

        return newList;
    }

}
