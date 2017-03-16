package WayofTime.bloodmagic.compat.jei.alchemyTable;

import javax.annotation.Nonnull;

import WayofTime.bloodmagic.api.registry.OrbRegistry;
import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.compat.jei.BloodMagicPlugin;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class AlchemyTableRecipeCategory extends BlankRecipeCategory<AlchemyTableRecipeJEI>
{
    private static final int OUTPUT_SLOT = 0;
    private static final int ORB_SLOT = 1;
    private static final int INPUT_SLOT = 2;

    @Nonnull
    private final IDrawable background = BloodMagicPlugin.jeiHelper.getGuiHelper().createDrawable(new ResourceLocation(Constants.Mod.DOMAIN + "gui/jei/alchemyTable.png"), 0, 0, 118, 40);
    @Nonnull
    private final String localizedName = TextHelper.localize("jei.BloodMagic.recipe.alchemyTable");
    @Nonnull
    private final ICraftingGridHelper craftingGridHelper;

    public AlchemyTableRecipeCategory()
    {
        craftingGridHelper = BloodMagicPlugin.jeiHelper.getGuiHelper().createCraftingGridHelper(INPUT_SLOT, OUTPUT_SLOT);
    }

    @Nonnull
    @Override
    public String getUid()
    {
        return Constants.Compat.JEI_CATEGORY_ALCHEMYTABLE;
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
    public void setRecipe(IRecipeLayout recipeLayout, AlchemyTableRecipeJEI recipeWrapper, IIngredients ingredients)
    {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(OUTPUT_SLOT, false, 91, 13);
        guiItemStacks.init(ORB_SLOT, true, 60, 0);

        for (int y = 0; y < 3; ++y)
        {
            for (int x = 0; x < 3; ++x)
            {
                int index = INPUT_SLOT + x + (y * 3);
                guiItemStacks.init(index, true, x * 18, y * 18 - 18);
            }
        }

        guiItemStacks.set(ORB_SLOT, OrbRegistry.getOrbsDownToTier(recipeWrapper.getRecipe().getTierRequired()));
        craftingGridHelper.setOutput(guiItemStacks, ingredients.getOutputs(ItemStack.class));
        craftingGridHelper.setInputStacks(guiItemStacks, ingredients.getInputs(ItemStack.class), 3, 2);
    }
}
