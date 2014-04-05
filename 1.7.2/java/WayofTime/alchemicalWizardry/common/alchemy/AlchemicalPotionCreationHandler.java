package WayofTime.alchemicalWizardry.common.alchemy;

import java.util.ArrayList;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModItems;

public class AlchemicalPotionCreationHandler
{
    public static ArrayList<AlchemyPotionHandlerComponent> registeredPotionEffects = new ArrayList();

    public static void initializePotions()
    {
        addPotion(new ItemStack(Items.ghast_tear), Potion.regeneration.id, 450);
        addPotion(new ItemStack(Items.golden_carrot), Potion.nightVision.id, 2 * 60 * 20);
        addPotion(new ItemStack(Items.magma_cream), Potion.fireResistance.id, 2 * 60 * 20);
        addPotion(new ItemStack(Items.water_bucket), Potion.waterBreathing.id, 2 * 60 * 20);
        addPotion(new ItemStack(Items.sugar), Potion.moveSpeed.id, 2 * 60 * 20);
        addPotion(new ItemStack(Items.speckled_melon), Potion.heal.id, 2 * 60 * 20);
        addPotion(new ItemStack(Items.spider_eye), Potion.poison.id, 450);
        addPotion(new ItemStack(Items.fermented_spider_eye), Potion.weakness.id, 450);
        addPotion(new ItemStack(Items.blaze_powder), Potion.damageBoost.id, 2 * 60 * 20);
        addPotion(new ItemStack(ModItems.aether), Potion.jump.id, 2 * 60 * 20);
        addPotion(new ItemStack(Items.clay_ball), Potion.moveSlowdown.id, 450);
        addPotion(new ItemStack(Items.redstone), Potion.digSpeed.id, 2 * 60 * 20);
        addPotion(new ItemStack(Items.potionitem, 1, 0), AlchemicalWizardry.customPotionDrowning.id, 450);
        //addPotion(new ItemStack(Item.goldenCarrot),Potion.nightVision.id,2*60*20);
        addPotion(new ItemStack(Items.glass_bottle), Potion.invisibility.id, 2 * 60 * 20);
        addPotion(new ItemStack(Items.diamond), Potion.resistance.id, 2 * 60 * 20);
        addPotion(new ItemStack(Items.poisonous_potato), Potion.field_76443_y.id, 2); //saturation
        addPotion(new ItemStack(ModItems.demonBloodShard), Potion.field_76434_w.id, 4 * 60 * 20); //health boost
        addPotion(new ItemStack(ModItems.weakBloodShard), Potion.field_76444_x.id, 4 * 60 * 20); //Absorption
        addPotion(new ItemStack(ModItems.terrae), AlchemicalWizardry.customPotionBoost.id, 1 * 60 * 20);
        addPotion(new ItemStack(Items.feather), AlchemicalWizardry.customPotionFlight.id, 1 * 60 * 20);
        addPotion(new ItemStack(Items.arrow), AlchemicalWizardry.customPotionReciprocation.id, 1 * 60 * 20);
        addPotion(new ItemStack(Items.ender_pearl),AlchemicalWizardry.customPotionPlanarBinding.id,1*60*20);
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
