package WayofTime.bloodmagic.compat.jei.altar;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.apibutnotreally.Constants;
import WayofTime.bloodmagic.compat.jei.BloodMagicPlugin;
import WayofTime.bloodmagic.util.helper.TextHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AltarRecipeCategory implements IRecipeCategory {
    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;

    @Nonnull
    private final IDrawable background = BloodMagicPlugin.jeiHelper.getGuiHelper().createDrawable(new ResourceLocation(Constants.Mod.DOMAIN + "gui/jei/altar.png"), 3, 4, 155, 65);
    @Nonnull
    private final String localizedName = TextHelper.localize("jei.bloodmagic.recipe.altar");

    @Nonnull
    @Override
    public String getUid() {
        return Constants.Compat.JEI_CATEGORY_ALTAR;
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
    public void drawExtras(Minecraft minecraft) {

    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(INPUT_SLOT, true, 31, 0);
        recipeLayout.getItemStacks().init(OUTPUT_SLOT, false, 125, 30);

        if (recipeWrapper instanceof AltarRecipeJEI) {
            recipeLayout.getItemStacks().set(INPUT_SLOT, ingredients.getInputs(ItemStack.class).get(0));
            recipeLayout.getItemStacks().set(OUTPUT_SLOT, ingredients.getOutputs(ItemStack.class).get(0));
        }
    }

    @Override
    public String getModName() {
        return BloodMagic.NAME;
    }
}
