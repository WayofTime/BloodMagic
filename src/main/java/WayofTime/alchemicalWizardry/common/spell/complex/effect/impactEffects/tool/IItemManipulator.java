package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.tool;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface IItemManipulator
{
    public List<ItemStack> handleItemsOnBlockBroken(ItemStack toolStack, List<ItemStack> itemList);
}
