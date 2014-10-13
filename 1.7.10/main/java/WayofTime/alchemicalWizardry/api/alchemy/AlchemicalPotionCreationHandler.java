package WayofTime.alchemicalWizardry.api.alchemy;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class AlchemicalPotionCreationHandler
{
    public static ArrayList<AlchemyPotionHandlerComponent> registeredPotionEffects = new ArrayList();

    public static void addPotion(ItemStack itemStack, int potionID, int tickDuration)
    {
        registeredPotionEffects.add(new AlchemyPotionHandlerComponent(itemStack, potionID, tickDuration));
    }

    public static int getPotionIDForStack(ItemStack itemStack)
    {
        for (AlchemyPotionHandlerComponent aphc : registeredPotionEffects)
        {
            if (aphc.compareItemStack(itemStack))
            {
                return aphc.getPotionID();
            }
        }

        return -1;
    }

    public static int getPotionTickDurationForStack(ItemStack itemStack)
    {
        {
            for (AlchemyPotionHandlerComponent aphc : registeredPotionEffects)
            {
                if (aphc.compareItemStack(itemStack))
                {
                    return aphc.getTickDuration();
                }
            }

            return -1;
        }
    }

    public static boolean containsRegisteredPotionIngredient(ItemStack[] stackList)
    {
        for (ItemStack is : stackList)
        {
            for (AlchemyPotionHandlerComponent aphc : registeredPotionEffects)
            {
                if (aphc.compareItemStack(is))
                {
                    return true;
                }
            }
        }

        return false;
    }

    public static int getRegisteredPotionIngredientPosition(ItemStack[] stackList)
    {
        int i = 0;

        for (ItemStack is : stackList)
        {
            for (AlchemyPotionHandlerComponent aphc : registeredPotionEffects)
            {
                if (aphc.compareItemStack(is))
                {
                    return i;
                }
            }

            i++;
        }

        return -1;
    }
}
