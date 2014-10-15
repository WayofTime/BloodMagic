package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.tool;

import net.minecraft.item.ItemStack;

import java.util.List;

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
    public abstract List<ItemStack> handleItemsOnBlockBroken(ItemStack toolStack, List<ItemStack> itemList);
}
