package WayofTime.alchemicalWizardry.common.alchemy;

import WayofTime.alchemicalWizardry.api.alchemy.AlchemyPotionHelper;
import WayofTime.alchemicalWizardry.common.items.potion.AlchemyFlask;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public class CombinedPotionRegistry
{
    public static List<CombinedPotionComponent> potionList = new ArrayList<CombinedPotionComponent>();

    public static void registerCombinedPotionRecipe(Potion result, Potion pot1, Potion pot2)
    {
        potionList.add(new CombinedPotionComponent(result, pot1, pot2));
    }

    public static boolean isRecipeValid(Potion pot1, Potion pot2)
    {
        for (CombinedPotionComponent recipe : potionList)
        {
            if (recipe.isRecipeValid(pot1, pot2))
            {
                return true;
            }
        }

        return false;
    }

    public static boolean isRecipeValid(int pot1, int pot2)
    {
        for (CombinedPotionComponent recipe : potionList)
        {
            if (recipe.isRecipeValid(pot1, pot2))
            {
                return true;
            }
        }

        return false;
    }

    public static Potion getPotion(Potion pot1, Potion pot2)
    {
        for (CombinedPotionComponent recipe : potionList)
        {
            if (recipe.isRecipeValid(pot1, pot2))
            {
                return recipe.result;
            }
        }

        return null;
    }

    public static Potion getPotion(int pot1, int pot2)
    {
        for (CombinedPotionComponent recipe : potionList)
        {
            if (recipe.isRecipeValid(pot1, pot2))
            {
                return recipe.result;
            }
        }

        return null;
    }

    public static ItemStack applyPotionEffect(ItemStack stack)
    {
        if (stack == null || !(stack.getItem() instanceof AlchemyFlask))
        {
            return null;
        }

        List<AlchemyPotionHelper> list = AlchemyFlask.getEffects(stack);
        if (list == null)
        {
            return stack;
        }

        boolean isDone = false;

        for (AlchemyPotionHelper helper1 : list)
        {
            if (isDone)
            {
                continue;
            }

            for (int i = 0; i < list.size(); i++)
            {
                if (isDone)
                {
                    continue;
                }

                AlchemyPotionHelper helper2 = list.get(i);

                PotionEffect potEffect = getResultantPotion(helper1, helper2);

                if (potEffect != null)
                {
                    AlchemyPotionHelper potHelper = new AlchemyPotionHelper(potEffect.getPotionID(), potEffect.getDuration(), 0, potEffect.getAmplifier());

                    list.remove(helper1);
                    list.remove(helper2);

                    list.add(potHelper);

                    isDone = true;
                }
            }
        }

        if (isDone)
        {
            AlchemyFlask.setEffects(stack, list);

            return stack;
        }

        return null;
    }

    public static boolean hasCombinablePotionEffect(ItemStack stack)
    {
        if (stack == null || !(stack.getItem() instanceof AlchemyFlask))
        {
            return false;
        }

        List<AlchemyPotionHelper> list = AlchemyFlask.getEffects(stack);
        if (list == null)
        {
            return false;
        }

        for (AlchemyPotionHelper helper1 : list)
        {
            for (AlchemyPotionHelper helper2 : list)
            {
                int pot1 = helper1.getPotionID();
                int pot2 = helper2.getPotionID();

                if (isRecipeValid(pot1, pot2))
                {
                    return true;
                }
            }
        }

        return false;
    }

    public static PotionEffect getResultantPotion(AlchemyPotionHelper potE1, AlchemyPotionHelper potE2)
    {
        if (potE1 == null || potE2 == null)
        {
            return null;
        }

        int pot1 = potE1.getPotionID();
        int pot2 = potE2.getPotionID();

        if (isRecipeValid(pot1, pot2))
        {
            int duration = (int) ((potE1.getTickDuration() * Math.pow(8.0f / 3.0f, potE1.getdurationFactor()) + potE2.getdurationFactor() * Math.pow(8.0f / 3.0f, potE2.getdurationFactor())) / 2.0);
            int amplifier = (potE1.getConcentration() + potE2.getConcentration()) / 2;

            Potion pot = getPotion(pot1, pot2);

            return new PotionEffect(pot.id, duration, amplifier);
        }

        return null;
    }
}
