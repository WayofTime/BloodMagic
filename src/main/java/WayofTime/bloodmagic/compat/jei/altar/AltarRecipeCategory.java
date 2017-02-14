package WayofTime.bloodmagic.compat.jei.altar;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;

import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.compat.jei.BloodMagicPlugin;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class AltarRecipeCategory implements IRecipeCategory
{
    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;

    @Nonnull
    private final IDrawable background = BloodMagicPlugin.jeiHelper.getGuiHelper().createDrawable(new ResourceLocation(Constants.Mod.DOMAIN + "gui/jei/altar.png"), 3, 4, 155, 65);
    @Nonnull
    private final String localizedName = TextHelper.localize("jei.BloodMagic.recipe.altar");

    @Nonnull
    @Override
    public String getUid()
    {
        return Constants.Compat.JEI_CATEGORY_ALTAR;
    }

    @Nonnull
    @Override
    public String getTitle()
    {
        return localizedName;
    }

    @Nonnull
    @Override
    public IDrawable getBackground()
    {
        return background;
    }

    @Override
    public void drawExtras(Minecraft minecraft)
    {

    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients)
    {
        recipeLayout.getItemStacks().init(INPUT_SLOT, true, 31, 0);
        recipeLayout.getItemStacks().init(OUTPUT_SLOT, false, 125, 30);

        if (recipeWrapper instanceof AltarRecipeJEI)
        {
            recipeLayout.getItemStacks().set(INPUT_SLOT, ingredients.getInputs(ItemStack.class).get(0));
            recipeLayout.getItemStacks().set(OUTPUT_SLOT, ingredients.getOutputs(ItemStack.class).get(0));
        }
    }

    @Override
    public void drawAnimations(Minecraft minecraft) {

    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper) {

    }
}
