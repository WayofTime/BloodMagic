package WayofTime.bloodmagic.compat.jei.binding;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.util.helper.TextHelper;
import mezz.jei.api.JEIManager;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class BindingRecipeCategory implements IRecipeCategory {

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;

    @Nonnull
    private final IDrawable background = JEIManager.guiHelper.createDrawable(new ResourceLocation(Constants.Mod.DOMAIN + "gui/jei/binding.png"), 0, 0, 100, 30);
    @Nonnull
    private final String localizedName = TextHelper.localize("jei.BloodMagic.recipe.binding");

    @Nonnull
    @Override
    public String getUid() {
        return Constants.Compat.JEI_CATEGORY_BINDING;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return localizedName;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void init(@Nonnull IRecipeLayout recipeLayout) {
        recipeLayout.getItemStacks().init(INPUT_SLOT, true, 0, 5);
        recipeLayout.getItemStacks().init(OUTPUT_SLOT, false, 73, 5);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper) {
        if (recipeWrapper instanceof BindingRecipeJEI) {
            BindingRecipeJEI bindingRecipeWrapper = (BindingRecipeJEI) recipeWrapper;
            recipeLayout.getItemStacks().set(INPUT_SLOT, bindingRecipeWrapper.getInputs());
            recipeLayout.getItemStacks().set(OUTPUT_SLOT, bindingRecipeWrapper.getOutputs());
        }
    }
}
