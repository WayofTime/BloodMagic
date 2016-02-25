package WayofTime.bloodmagic.compat.jei.forge;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.compat.jei.BloodMagicPlugin;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class TartaricForgeRecipeCategory implements IRecipeCategory
{
    private static final int OUTPUT_SLOT = 0;
    private static final int GEM_SLOT = 1;
    private static final int INPUT_SLOT = 2;

    @Nonnull
    private final IDrawable background = BloodMagicPlugin.jeiHelper.getGuiHelper().createDrawable(new ResourceLocation(Constants.Mod.DOMAIN + "gui/jei/soulForge.png"), 0, 0, 100, 40);
    @Nonnull
    private final String localizedName = TextHelper.localize("jei.BloodMagic.recipe.soulForge");
    @Nonnull
    private final ICraftingGridHelper craftingGridHelper;

    public TartaricForgeRecipeCategory()
    {
        craftingGridHelper = BloodMagicPlugin.jeiHelper.getGuiHelper().createCraftingGridHelper(INPUT_SLOT, OUTPUT_SLOT);
    }

    @Nonnull
    @Override
    public String getUid()
    {
        return Constants.Compat.JEI_CATEGORY_SOULFORGE;
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
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(OUTPUT_SLOT, false, 73, 13);
        guiItemStacks.init(GEM_SLOT, true, 42, 0);

        for (int y = 0; y < 3; ++y)
        {
            for (int x = 0; x < 3; ++x)
            {
                int index = INPUT_SLOT + x + (y * 3);
                guiItemStacks.init(index, true, x * 18, y * 18);
            }
        }

        if (recipeWrapper instanceof TartaricForgeRecipeJEI)
        {
            TartaricForgeRecipeJEI recipe = (TartaricForgeRecipeJEI) recipeWrapper;
            guiItemStacks.set(GEM_SLOT, (ArrayList<ItemStack>) recipe.getInputs().get(1));
            craftingGridHelper.setOutput(guiItemStacks, recipe.getOutputs());
            craftingGridHelper.setInput(guiItemStacks, (List) recipe.getInputs().get(0), 2, 3);
        }
    }
}
