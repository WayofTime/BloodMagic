package WayofTime.alchemicalWizardry.common.alchemy;

import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;

import java.util.ArrayList;

public class AlchemicalPotionCreationHandler {
    public static ArrayList<AlchemyPotionHandlerComponent> registeredPotionEffects = new ArrayList();

    public static void initializePotions()
    {
        addPotion(new ItemStack(Item.ghastTear), Potion.regeneration.id, 450);
        addPotion(new ItemStack(Item.goldenCarrot), Potion.nightVision.id, 2 * 60 * 20);
        addPotion(new ItemStack(Item.magmaCream), Potion.fireResistance.id, 2 * 60 * 20);
        addPotion(new ItemStack(Item.sugar), Potion.moveSpeed.id, 2 * 60 * 20);
        addPotion(new ItemStack(Item.speckledMelon), Potion.heal.id, 2 * 60 * 20);
        addPotion(new ItemStack(Item.spiderEye), Potion.poison.id, 450);
        addPotion(new ItemStack(Item.fermentedSpiderEye), Potion.weakness.id, 450);
        addPotion(new ItemStack(Item.blazePowder), Potion.damageBoost.id, 2 * 60 * 20);
        addPotion(new ItemStack(ModItems.aether), Potion.jump.id, 2 * 60 * 20);
        addPotion(new ItemStack(Item.clay), Potion.moveSlowdown.id, 450);
        addPotion(new ItemStack(Item.redstone), Potion.digSpeed.id, 2 * 60 * 20);
        addPotion(new ItemStack(Item.potion, 1, 0), AlchemicalWizardry.customPotionDrowning.id, 450);
        //addPotion(new ItemStack(Item.goldenCarrot),Potion.nightVision.id,2*60*20);
        addPotion(new ItemStack(Item.glassBottle), Potion.invisibility.id, 2 * 60 * 20);
        addPotion(new ItemStack(Item.diamond), Potion.resistance.id, 2 * 60 * 20);
        addPotion(new ItemStack(Item.poisonousPotato), Potion.field_76443_y.id, 2); //saturation
        addPotion(new ItemStack(ModItems.demonBloodShard), Potion.field_76434_w.id, 4 * 60 * 20); //health boost
        addPotion(new ItemStack(ModItems.weakBloodShard), Potion.field_76444_x.id, 4 * 60 * 20); //Absorption
        addPotion(new ItemStack(ModItems.terrae), AlchemicalWizardry.customPotionBoost.id, 1 * 60 * 20);
        addPotion(new ItemStack(Item.feather), AlchemicalWizardry.customPotionFlight.id, 1 * 60 * 20);
        addPotion(new ItemStack(Item.arrow), AlchemicalWizardry.customPotionReciprocation.id, 1 * 60 * 20);
    }

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
