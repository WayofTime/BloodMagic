package WayofTime.bloodmagic.compat.jei.armourDowngrade;

import WayofTime.bloodmagic.compat.jei.BloodMagicPlugin;
import com.google.common.collect.Lists;
import lombok.Getter;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;
import WayofTime.bloodmagic.api.recipe.LivingArmourDowngradeRecipe;
import WayofTime.bloodmagic.api.util.helper.ItemHelper.LivingUpgrades;
import WayofTime.bloodmagic.registry.RegistrarBloodMagicItems;

import java.util.List;

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
        List<List<ItemStack>> expanded = BloodMagicPlugin.jeiHelper.getStackHelper().expandRecipeItemStackInputs(recipe.getInput());
        expanded.add(Lists.newArrayList(recipe.getKey()));
        ingredients.setInputLists(ItemStack.class, expanded);
        ItemStack upgradeStack = new ItemStack(RegistrarBloodMagicItems.UPGRADE_TOME);
        LivingUpgrades.setUpgrade(upgradeStack, recipe.getRecipeOutput());
        ingredients.setOutput(ItemStack.class, upgradeStack);
    }
}
