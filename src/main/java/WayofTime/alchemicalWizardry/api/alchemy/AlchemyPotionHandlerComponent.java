package WayofTime.alchemicalWizardry.api.alchemy;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class AlchemyPotionHandlerComponent
{
    private ItemStack itemStack;
    private int potionID;
    private int tickDuration;

    public AlchemyPotionHandlerComponent(ItemStack itemStack, int potionID, int tickDuration)
    {
        this.itemStack = itemStack;
        this.potionID = potionID;
        this.tickDuration = tickDuration;
    }

    public boolean compareItemStack(ItemStack comparedStack)
    {
        if (comparedStack != null && itemStack != null)
        {
            if (comparedStack.getItem() instanceof ItemBlock)
            {
                if (itemStack.getItem() instanceof ItemBlock)
                {
                    return comparedStack.getItem().equals(itemStack.getItem()) && comparedStack.getItemDamage() == itemStack.getItemDamage();
                }
            } else if (!(itemStack.getItem() instanceof ItemBlock))
            {
                return comparedStack.getItem().equals(itemStack.getItem()) && comparedStack.getItemDamage() == itemStack.getItemDamage();
            }
        }

        return false;
    }

    public ItemStack getItemStack()
    {
        return itemStack;
    }

    public int getPotionID()
    {
        return this.potionID;
    }

    public int getTickDuration()
    {
        return this.tickDuration;
    }
}
