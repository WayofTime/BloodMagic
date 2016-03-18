package WayofTime.bloodmagic.compat.jei.altar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import WayofTime.bloodmagic.api.orb.IBloodOrb;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry;

public class AltarRecipeMaker
{
    @Nonnull
    public static List<AltarRecipeJEI> getRecipes()
    {
        Map<ItemStack, AltarRecipeRegistry.AltarRecipe> altarMap = AltarRecipeRegistry.getRecipes();

        ArrayList<AltarRecipeJEI> recipes = new ArrayList<AltarRecipeJEI>();

        for (Map.Entry<ItemStack, AltarRecipeRegistry.AltarRecipe> itemStackAltarRecipeEntry : altarMap.entrySet())
        {
            if (!(itemStackAltarRecipeEntry.getKey().getItem() instanceof IBloodOrb))
            {
                // Make sure input is not a Blood Orb. If it is, the recipe is for a filling orb, and we don't want that.
                ItemStack input = itemStackAltarRecipeEntry.getKey();
                ItemStack output = itemStackAltarRecipeEntry.getValue().getOutput();
                int requiredTier = itemStackAltarRecipeEntry.getValue().getMinTier().toInt();
                int requiredLP = itemStackAltarRecipeEntry.getValue().getSyphon();
                int consumptionRate = itemStackAltarRecipeEntry.getValue().getConsumeRate();
                int drainRate = itemStackAltarRecipeEntry.getValue().getDrainRate();

                AltarRecipeJEI recipe = new AltarRecipeJEI(input, output, requiredTier, requiredLP, consumptionRate, drainRate);
                recipes.add(recipe);
            }
        }

        return recipes;
    }
}
