package WayofTime.bloodmagic.compat.jei.alchemyTable;

import java.util.ArrayList;
import java.util.List;

import WayofTime.bloodmagic.compat.jei.BloodMagicPlugin;
import lombok.Getter;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;
import WayofTime.bloodmagic.api.recipe.AlchemyTableRecipe;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class AlchemyTableRecipeJEI extends BlankRecipeWrapper
{
    @Getter
    private AlchemyTableRecipe recipe;

    public AlchemyTableRecipeJEI(AlchemyTableRecipe recipe)
    {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        List<List<ItemStack>> expanded = BloodMagicPlugin.jeiHelper.getStackHelper().expandRecipeItemStackInputs(recipe.getInput());
        ingredients.setInputLists(ItemStack.class, expanded);
        ingredients.setOutput(ItemStack.class, recipe.getRecipeOutput(new ArrayList<ItemStack>()));
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY)
    {
        ArrayList<String> ret = new ArrayList<String>();
        if (mouseX >= 58 && mouseX <= 78 && mouseY >= 21 && mouseY <= 34)
        {
            ret.add(TextHelper.localize("jei.bloodmagic.recipe.lpDrained", recipe.getLpDrained()));
            ret.add(TextHelper.localize("jei.bloodmagic.recipe.ticksRequired", recipe.getTicksRequired()));
        }
        return ret;
    }
}
