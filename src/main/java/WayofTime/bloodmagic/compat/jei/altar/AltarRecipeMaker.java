package WayofTime.bloodmagic.compat.jei.altar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.ItemStackWrapper;
import net.minecraft.item.ItemStack;
import WayofTime.bloodmagic.api.orb.IBloodOrb;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry;
import net.minecraftforge.common.ForgeModContainer;

public class AltarRecipeMaker
{
    @Nonnull
    public static List<AltarRecipeJEI> getRecipes()
    {
        Map<List<ItemStackWrapper>, AltarRecipeRegistry.AltarRecipe> altarMap = AltarRecipeRegistry.getRecipes();

        ArrayList<AltarRecipeJEI> recipes = new ArrayList<AltarRecipeJEI>();

        for (Map.Entry<List<ItemStackWrapper>, AltarRecipeRegistry.AltarRecipe> itemStackAltarRecipeEntry : altarMap.entrySet())
        {
            // Make sure input is not a Blood Orb. If it is, the recipe is for a filling orb, and we don't want that.
            if (!(itemStackAltarRecipeEntry.getKey().get(0).toStack().getItem() instanceof IBloodOrb))
            {
                List<ItemStack> input = ItemStackWrapper.toStackList(itemStackAltarRecipeEntry.getValue().getInput());
                ItemStack output = itemStackAltarRecipeEntry.getValue().getOutput();
                int requiredTier = itemStackAltarRecipeEntry.getValue().getMinTier().toInt();
                int requiredLP = itemStackAltarRecipeEntry.getValue().getSyphon();
                int consumptionRate = itemStackAltarRecipeEntry.getValue().getConsumeRate();
                int drainRate = itemStackAltarRecipeEntry.getValue().getDrainRate();

                if (output.getItem() == ForgeModContainer.getInstance().universalBucket && requiredLP == 1000)
                    output = BloodMagicAPI.getLifeEssenceBucket();

                AltarRecipeJEI recipe = new AltarRecipeJEI(input, output, requiredTier, requiredLP, consumptionRate, drainRate);
                recipes.add(recipe);
            }
        }

        return recipes;
    }
}
