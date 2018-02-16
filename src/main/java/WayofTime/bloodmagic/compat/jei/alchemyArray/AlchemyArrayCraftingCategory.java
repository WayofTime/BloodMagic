package WayofTime.bloodmagic.compat.jei.alchemyArray;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.compat.jei.BloodMagicJEIPlugin;
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

public class AlchemyArrayCraftingCategory implements IRecipeCategory {
    private static final int INPUT_SLOT = 0;
    private static final int CATALYST_SLOT = 1;
    private static final int OUTPUT_SLOT = 2;

    @Nonnull
    private final IDrawable background = BloodMagicJEIPlugin.jeiHelper.getGuiHelper().createDrawable(new ResourceLocation(Constants.Mod.DOMAIN + "gui/jei/binding.png"), 0, 0, 100, 30);
    @Nonnull
    private final String localizedName = TextHelper.localize("jei.bloodmagic.recipe.alchemyArrayCrafting");

    @Nonnull
    @Override
    public String getUid() {
        return Constants.Compat.JEI_CATEGORY_ALCHEMYARRAY;
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
        recipeLayout.getItemStacks().init(INPUT_SLOT, true, 0, 5);
        recipeLayout.getItemStacks().init(CATALYST_SLOT, true, 29, 3);
        recipeLayout.getItemStacks().init(OUTPUT_SLOT, false, 73, 5);

        if (recipeWrapper instanceof AlchemyArrayCraftingRecipeJEI) {
            recipeLayout.getItemStacks().set(INPUT_SLOT, ingredients.getInputs(ItemStack.class).get(0));
            recipeLayout.getItemStacks().set(CATALYST_SLOT, ingredients.getInputs(ItemStack.class).get(1));
            recipeLayout.getItemStacks().set(OUTPUT_SLOT, ingredients.getOutputs(ItemStack.class).get(0));
        }
    }

    @Override
    public String getModName() {
        return BloodMagic.NAME;
    }
}
