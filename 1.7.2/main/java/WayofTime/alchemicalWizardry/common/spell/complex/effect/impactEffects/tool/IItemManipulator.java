package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.tool;

import java.util.List;

import net.minecraft.item.ItemStack;

public interface IItemManipulator 
{
	public List<ItemStack> handleItemsOnBlockBroken(ItemStack toolStack, List<ItemStack> itemList);
}
