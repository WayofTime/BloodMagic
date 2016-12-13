package WayofTime.bloodmagic.compat.jei.armourDowngrade;

import lombok.Getter;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;
import WayofTime.bloodmagic.api.recipe.LivingArmourDowngradeRecipe;
import WayofTime.bloodmagic.api.util.helper.ItemHelper.LivingUpgrades;
import WayofTime.bloodmagic.registry.ModItems;

public class ArmourDowngradeRecipeJEI extends BlankRecipeWrapper
{
    @Getter
    private LivingArmourDowngradeRecipe recipe;

    public ArmourDowngradeRecipeJEI(LivingArmourDowngradeRecipe recipe)
    {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        // TODO - inputs
        ItemStack upgradeStack = new ItemStack(ModItems.UPGRADE_TOME);
        LivingUpgrades.setUpgrade(upgradeStack, recipe.getRecipeOutput());
        ingredients.setOutput(ItemStack.class, upgradeStack);
    }
}
