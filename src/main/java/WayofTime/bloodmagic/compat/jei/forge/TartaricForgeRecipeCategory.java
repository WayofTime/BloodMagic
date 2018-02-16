package WayofTime.bloodmagic.compat.jei.forge;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.compat.jei.BloodMagicJEIPlugin;
import WayofTime.bloodmagic.util.helper.TextHelper;
import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TartaricForgeRecipeCategory implements IRecipeCategory<TartaricForgeRecipeJEI> {
    private static final int OUTPUT_SLOT = 0;
    private static final int GEM_SLOT = 1;
    private static final int INPUT_SLOT = 2;

    @Nonnull
    private final IDrawable background = BloodMagicJEIPlugin.jeiHelper.getGuiHelper().createDrawable(new ResourceLocation(Constants.Mod.DOMAIN + "gui/jei/soulForge.png"), 0, 0, 100, 40);
    @Nonnull
    private final ICraftingGridHelper craftingGridHelper;

    public TartaricForgeRecipeCategory() {
        craftingGridHelper = BloodMagicJEIPlugin.jeiHelper.getGuiHelper().createCraftingGridHelper(INPUT_SLOT, OUTPUT_SLOT);
    }

    @Nonnull
    @Override
    public String getUid() {
        return Constants.Compat.JEI_CATEGORY_SOULFORGE;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return TextHelper.localize("jei.bloodmagic.recipe.soulForge");
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
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull TartaricForgeRecipeJEI recipeWrapper, @Nonnull IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(OUTPUT_SLOT, false, 73, 13);
        guiItemStacks.init(GEM_SLOT, true, 42, 0);

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 3; ++x) {
                int index = INPUT_SLOT + x + (y * 3);
                guiItemStacks.init(index, true, x * 18, y * 18);
            }
        }

        List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);

        guiItemStacks.set(GEM_SLOT, ingredients.getInputs(ItemStack.class).get(ingredients.getInputs(ItemStack.class).size() - 1));
        inputs.remove(ingredients.getInputs(ItemStack.class).size() - 1);
        guiItemStacks.set(OUTPUT_SLOT, ingredients.getOutputs(ItemStack.class).get(0));
        guiItemStacks.set(INPUT_SLOT, ingredients.getInputs(ItemStack.class).get(0));
        craftingGridHelper.setInputs(guiItemStacks, inputs);
    }

    @Nonnull
    @Override
    public String getModName() {
        return BloodMagic.NAME;
    }
}
