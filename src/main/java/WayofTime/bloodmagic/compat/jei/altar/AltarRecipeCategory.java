package WayofTime.bloodmagic.compat.jei.altar;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.util.helper.TextHelper;
import mezz.jei.api.JEIManager;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidTanks;
import mezz.jei.api.gui.IGuiItemStacks;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class AltarRecipeCategory implements IRecipeCategory {

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;

    @Nonnull
    private final IDrawable background = JEIManager.guiHelper.createDrawable(new ResourceLocation(Constants.Mod.DOMAIN + "gui/jei/altar.png"), 3, 4, 155, 65);
    @Nonnull
    private final String localizedName = TextHelper.localize("jei.BloodMagic.recipe.altar");

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
    public void init(@Nonnull IGuiItemStacks guiItemStacks, @Nonnull IGuiFluidTanks guiFluidTanks) {
        guiItemStacks.init(INPUT_SLOT, 31, 0);
        guiItemStacks.init(OUTPUT_SLOT, 125, 30);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setRecipe(@Nonnull IGuiItemStacks guiItemStacks, @Nonnull IGuiFluidTanks guiFluidTanks, @Nonnull IRecipeWrapper recipeWrapper) {
        if (recipeWrapper instanceof AltarRecipeJEI) {
            AltarRecipeJEI altarRecipeWrapper = (AltarRecipeJEI) recipeWrapper;
            guiItemStacks.set(INPUT_SLOT, altarRecipeWrapper.getInputs());
            guiItemStacks.set(OUTPUT_SLOT, altarRecipeWrapper.getOutputs());
        }
    }
}
