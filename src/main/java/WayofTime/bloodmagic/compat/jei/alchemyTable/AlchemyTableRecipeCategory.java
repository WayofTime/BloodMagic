package WayofTime.bloodmagic.compat.jei.alchemyTable;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.core.registry.OrbRegistry;
import WayofTime.bloodmagic.compat.jei.BloodMagicJEIPlugin;
import WayofTime.bloodmagic.util.helper.TextHelper;
import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class AlchemyTableRecipeCategory implements IRecipeCategory<AlchemyTableRecipeJEI> {

    private static final int OUTPUT_SLOT = 0;
    private static final int ORB_SLOT = 1;
    private static final int INPUT_SLOT = 2;

    @Nonnull
    private final IDrawable background = BloodMagicJEIPlugin.jeiHelper.getGuiHelper().createDrawable(new ResourceLocation(Constants.Mod.DOMAIN + "gui/jei/alchemyTable.png"), 0, 0, 118, 40);
    @Nonnull
    private final ICraftingGridHelper craftingGridHelper;

    public AlchemyTableRecipeCategory() {
        craftingGridHelper = BloodMagicJEIPlugin.jeiHelper.getGuiHelper().createCraftingGridHelper(INPUT_SLOT, OUTPUT_SLOT);
    }

    @Nonnull
    @Override
    public String getUid() {
        return Constants.Compat.JEI_CATEGORY_ALCHEMYTABLE;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return TextHelper.localize("jei.bloodmagic.recipe.alchemyTable");
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, AlchemyTableRecipeJEI recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(OUTPUT_SLOT, false, 91, 13);
        guiItemStacks.init(ORB_SLOT, true, 60, 0);

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 3; ++x) {
                int index = INPUT_SLOT + x + (y * 3);
                guiItemStacks.init(index, true, x * 18, y * 18 - 18);
            }
        }

        guiItemStacks.set(ORB_SLOT, OrbRegistry.getOrbsDownToTier(recipeWrapper.getTier()));
        guiItemStacks.set(OUTPUT_SLOT, ingredients.getOutputs(ItemStack.class).get(0));
        craftingGridHelper.setInputs(guiItemStacks, ingredients.getInputs(ItemStack.class), 3, 2);
    }

    @Override
    public String getModName() {
        return BloodMagic.NAME;
    }
}
