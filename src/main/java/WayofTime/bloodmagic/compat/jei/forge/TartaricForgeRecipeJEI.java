package WayofTime.bloodmagic.compat.jei.forge;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import WayofTime.bloodmagic.compat.jei.BloodMagicPlugin;
import lombok.Getter;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;
import WayofTime.bloodmagic.api.recipe.TartaricForgeRecipe;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class TartaricForgeRecipeJEI extends BlankRecipeWrapper
{
    @Getter
    private TartaricForgeRecipe recipe;
    @Getter
    private List<ItemStack> validGems = new ArrayList<ItemStack>();

    public TartaricForgeRecipeJEI(TartaricForgeRecipe recipe)
    {
        this.recipe = recipe;

        for (DefaultWill will : DefaultWill.values())
            if (will.minSouls >= recipe.getMinimumSouls())
                this.validGems.add(will.willStack);
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        List<List<ItemStack>> expandedInputs = BloodMagicPlugin.jeiHelper.getStackHelper().expandRecipeItemStackInputs(recipe.getInput());
        expandedInputs.add(validGems);
        ingredients.setInputLists(ItemStack.class, expandedInputs);
        ingredients.setOutput(ItemStack.class, recipe.getRecipeOutput());
    }

    @Nullable
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY)
    {
        ArrayList<String> ret = new ArrayList<String>();
        if (mouseX >= 40 && mouseX <= 60 && mouseY >= 21 && mouseY <= 34)
        {
            ret.add(TextHelper.localize("jei.bloodmagic.recipe.minimumSouls", recipe.getMinimumSouls()));
            ret.add(TextHelper.localize("jei.bloodmagic.recipe.soulsDrained", recipe.getSoulsDrained()));
            return ret;
        }
        return null;
    }

    public enum DefaultWill
    {
        SOUL(new ItemStack(RegistrarBloodMagicItems.MONSTER_SOUL, 1, 0), 64),
        PETTY(new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 0), 64),
        LESSER(new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 1), 256),
        COMMON(new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 2), 1024),
        GREATER(new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 3), 4096),
        GRAND(new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 4), 16384);

        public final ItemStack willStack;
        public final double minSouls;

        DefaultWill(ItemStack willStack, double minSouls)
        {
            this.willStack = willStack;
            this.minSouls = minSouls;
        }
    }
}
