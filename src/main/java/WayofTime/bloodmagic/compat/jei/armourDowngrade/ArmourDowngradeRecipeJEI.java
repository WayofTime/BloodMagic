package WayofTime.bloodmagic.compat.jei.armourDowngrade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Getter;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;
import WayofTime.bloodmagic.api.recipe.LivingArmourDowngradeRecipe;
import WayofTime.bloodmagic.api.util.helper.ItemHelper.LivingUpgrades;
import WayofTime.bloodmagic.registry.ModItems;

import com.google.common.collect.Lists;

public class ArmourDowngradeRecipeJEI extends BlankRecipeWrapper
{
    @Getter
    private LivingArmourDowngradeRecipe recipe;

//    @Getter
//    private ArrayList<ItemStack> validGems = new ArrayList<ItemStack>();

    public ArmourDowngradeRecipeJEI(LivingArmourDowngradeRecipe recipe)
    {
        this.recipe = recipe;
    }

    @Override
    @Nonnull
    public List<Collection> getInputs()
    {
        ArrayList<Collection> ret = new ArrayList<Collection>();
        ret.add(recipe.getInput());
        ret.add(Lists.newArrayList(recipe.getKey()));
        return ret;
    }

    @Override
    @Nonnull
    public List<ItemStack> getOutputs()
    {
        ItemStack upgradeStack = new ItemStack(ModItems.UPGRADE_TOME);
        LivingUpgrades.setUpgrade(upgradeStack, recipe.getRecipeOutput());
        return Collections.singletonList(upgradeStack);
    }

    @Nullable
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY)
    {
//        ArrayList<String> ret = new ArrayList<String>();
//        if (mouseX >= 58 && mouseX <= 78 && mouseY >= 21 && mouseY <= 34)
//        {
//            ret.add(TextHelper.localize("jei.BloodMagic.recipe.lpDrained", recipe.getLpDrained()));
//            ret.add(TextHelper.localize("jei.BloodMagic.recipe.ticksRequired", recipe.getTicksRequired()));
//            return ret;
//        }
        return null;
    }
}
