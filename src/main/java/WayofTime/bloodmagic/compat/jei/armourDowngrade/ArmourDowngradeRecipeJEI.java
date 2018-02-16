package WayofTime.bloodmagic.compat.jei.armourDowngrade;

import WayofTime.bloodmagic.recipe.LivingArmourDowngradeRecipe;
import WayofTime.bloodmagic.util.helper.ItemHelper.LivingUpgrades;
import WayofTime.bloodmagic.compat.jei.BloodMagicJEIPlugin;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ArmourDowngradeRecipeJEI extends BlankRecipeWrapper {
    private LivingArmourDowngradeRecipe recipe;

    public ArmourDowngradeRecipeJEI(LivingArmourDowngradeRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        List<List<ItemStack>> expanded = BloodMagicJEIPlugin.jeiHelper.getStackHelper().expandRecipeItemStackInputs(recipe.getInput());
        expanded.add(Lists.newArrayList(recipe.getKey()));
        ingredients.setInputLists(ItemStack.class, expanded);
        ItemStack upgradeStack = new ItemStack(RegistrarBloodMagicItems.UPGRADE_TOME);
        LivingUpgrades.setUpgrade(upgradeStack, recipe.getRecipeOutput());
        ingredients.setOutput(ItemStack.class, upgradeStack);
    }

    public LivingArmourDowngradeRecipe getRecipe() {
        return recipe;
    }
}
