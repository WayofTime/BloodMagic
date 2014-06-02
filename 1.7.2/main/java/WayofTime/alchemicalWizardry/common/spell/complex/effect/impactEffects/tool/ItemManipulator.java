package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.tool;

import java.util.List;

import net.minecraft.item.ItemStack;

public abstract class ItemManipulator implements IItemManipulator
{
	protected int powerUpgrades;
	protected int potencyUpgrades;
	protected int costUpgrades;
	
	public ItemManipulator(int power, int potency, int cost)
	{
		this.powerUpgrades = power;
		this.potencyUpgrades = potency;
		this.costUpgrades = cost;
	}
	
	@Override
	public abstract List<ItemStack> handleItemsOnBlockBroken(ItemStack toolStack,List<ItemStack> itemList);
}
