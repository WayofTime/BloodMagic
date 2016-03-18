package WayofTime.bloodmagic.compat.jei.altar;

import javax.annotation.Nonnull;

import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
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

    @Override
    public void drawAnimations(Minecraft minecraft)
    {

    }

    @Override
    @SuppressWarnings("unchecked")
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper)
    {
        recipeLayout.getItemStacks().init(INPUT_SLOT, true, 31, 0);
        recipeLayout.getItemStacks().init(OUTPUT_SLOT, false, 125, 30);

        if (recipeWrapper instanceof AltarRecipeJEI)
        {
            AltarRecipeJEI altarRecipeWrapper = (AltarRecipeJEI) recipeWrapper;
            recipeLayout.getItemStacks().set(INPUT_SLOT, altarRecipeWrapper.getInputs());
            recipeLayout.getItemStacks().set(OUTPUT_SLOT, altarRecipeWrapper.getOutputs());
        }
    }
}
