package WayofTime.bloodmagic.compat.jei.alchemyArray;

import javax.annotation.Nonnull;

import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.compat.jei.BloodMagicPlugin;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class AlchemyArrayCraftingCategory implements IRecipeCategory
{
    private static final int INPUT_SLOT = 0;
    private static final int CATALYST_SLOT = 1;
    private static final int OUTPUT_SLOT = 2;

    @Nonnull
    private final IDrawable background = BloodMagicPlugin.jeiHelper.getGuiHelper().createDrawable(new ResourceLocation(Constants.Mod.DOMAIN + "gui/jei/binding.png"), 0, 0, 100, 30);
    @Nonnull
    private final String localizedName = TextHelper.localize("jei.BloodMagic.recipe.alchemyArrayCrafting");

    @Nonnull
    @Override
    public String getUid()
    {
        return Constants.Compat.JEI_CATEGORY_ALCHEMYARRAY;
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

        recipeLayout.getItemStacks().init(INPUT_SLOT, true, 0, 5);
        recipeLayout.getItemStacks().init(CATALYST_SLOT, true, 50, 5);
        recipeLayout.getItemStacks().init(OUTPUT_SLOT, false, 73, 5);

        if (recipeWrapper instanceof AlchemyArrayCraftingRecipeJEI)
        {
            AlchemyArrayCraftingRecipeJEI alchemyArrayWrapper = (AlchemyArrayCraftingRecipeJEI) recipeWrapper;
            recipeLayout.getItemStacks().set(INPUT_SLOT, (ItemStack) alchemyArrayWrapper.getInputs().get(0));
            recipeLayout.getItemStacks().set(CATALYST_SLOT, (ItemStack) alchemyArrayWrapper.getInputs().get(1));
            recipeLayout.getItemStacks().set(OUTPUT_SLOT, alchemyArrayWrapper.getOutputs());
        }
    }
}
