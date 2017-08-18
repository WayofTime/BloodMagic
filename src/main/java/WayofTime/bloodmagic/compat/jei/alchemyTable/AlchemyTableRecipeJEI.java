package WayofTime.bloodmagic.compat.jei.alchemyTable;

import WayofTime.bloodmagic.api_impl.recipe.RecipeAlchemyTable;
import WayofTime.bloodmagic.compat.jei.BloodMagicPlugin;
import WayofTime.bloodmagic.util.helper.TextHelper;
import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.List;

public class AlchemyTableRecipeJEI implements IRecipeWrapper {
    private RecipeAlchemyTable recipe;

    public AlchemyTableRecipeJEI(RecipeAlchemyTable recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        List<List<ItemStack>> expanded = BloodMagicPlugin.jeiHelper.getStackHelper().expandRecipeItemStackInputs(recipe.getInput());
        ingredients.setInputLists(ItemStack.class, expanded);
        ingredients.setOutput(ItemStack.class, recipe.getOutput());
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        List<String> tooltip = Lists.newArrayList();
        if (mouseX >= 58 && mouseX <= 78 && mouseY >= 21 && mouseY <= 34) {
            tooltip.add(TextHelper.localize("tooltip.bloodmagic.tier", recipe.getMinimumTier()));
            tooltip.add(TextHelper.localize("jei.bloodmagic.recipe.lpDrained", recipe.getSyphon()));
            tooltip.add(TextHelper.localize("jei.bloodmagic.recipe.ticksRequired", recipe.getTicks()));
        }
        return tooltip;
    }

    public RecipeAlchemyTable getRecipe() {
        return recipe;
    }
}
