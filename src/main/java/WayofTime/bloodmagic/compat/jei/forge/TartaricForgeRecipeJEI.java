package WayofTime.bloodmagic.compat.jei.forge;

import WayofTime.bloodmagic.api.impl.recipe.RecipeTartaricForge;
import WayofTime.bloodmagic.compat.jei.BloodMagicJEIPlugin;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.util.helper.TextHelper;
import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class TartaricForgeRecipeJEI implements IRecipeWrapper {
    private RecipeTartaricForge recipe;
    private List<ItemStack> validGems = Lists.newArrayList();

    public TartaricForgeRecipeJEI(RecipeTartaricForge recipe) {
        this.recipe = recipe;

        for (DefaultWill will : DefaultWill.values())
            if (will.minSouls >= recipe.getMinimumSouls())
                this.validGems.add(will.willStack);
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        List<List<ItemStack>> expandedInputs = BloodMagicJEIPlugin.jeiHelper.getStackHelper().expandRecipeItemStackInputs(recipe.getInput());
        expandedInputs.add(validGems);
        ingredients.setInputLists(ItemStack.class, expandedInputs);
        ingredients.setOutput(ItemStack.class, recipe.getOutput());
    }

    @Nonnull
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        List<String> tooltip = Lists.newArrayList();
        if (mouseX >= 40 && mouseX <= 60 && mouseY >= 21 && mouseY <= 34) {
            tooltip.add(TextHelper.localize("jei.bloodmagic.recipe.minimumSouls", recipe.getMinimumSouls()));
            tooltip.add(TextHelper.localize("jei.bloodmagic.recipe.soulsDrained", recipe.getSoulDrain()));
        }
        return tooltip;
    }

    public RecipeTartaricForge getRecipe() {
        return recipe;
    }

    public enum DefaultWill {
        SOUL(new ItemStack(RegistrarBloodMagicItems.MONSTER_SOUL, 1, 0), 64),
        PETTY(new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 0), 64),
        LESSER(new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 1), 256),
        COMMON(new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 2), 1024),
        GREATER(new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 3), 4096),
        GRAND(new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 4), 16384);

        public final ItemStack willStack;
        public final double minSouls;

        DefaultWill(ItemStack willStack, double minSouls) {
            this.willStack = willStack;
            this.minSouls = minSouls;
        }
    }
}
