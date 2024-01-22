package wayoftime.bloodmagic.compat.jei.alchemytable;

import com.google.common.collect.Lists;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.core.registry.OrbRegistry;
import wayoftime.bloodmagic.recipe.RecipeAlchemyTable;
import wayoftime.bloodmagic.util.ChatUtil;
import wayoftime.bloodmagic.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class AlchemyTableRecipeCategory implements IRecipeCategory<RecipeAlchemyTable> {
    public static final RecipeType<RecipeAlchemyTable> RECIPE_TYPE = RecipeType.create(BloodMagic.MODID, Constants.Compat.JEI_CATEGORY_ALCHEMYTABLE, RecipeAlchemyTable.class);

    @Nonnull
    private final IDrawable background;
    private final IDrawable icon;

    public AlchemyTableRecipeCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableItemStack(new ItemStack(BloodMagicBlocks.ALCHEMY_TABLE.get()));
        background = guiHelper.createDrawable(BloodMagic.rl("gui/jei/alchemytable.png"), 0, 0, 118, 40);
    }


    @Override
    public List<Component> getTooltipStrings(RecipeAlchemyTable recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        List<Component> tooltip = Lists.newArrayList();

        if (mouseX >= 58 && mouseX <= 78 && mouseY >= 21 && mouseY <= 34) {
            tooltip.add(Component.translatable("tooltip.bloodmagic.tier", ChatUtil.DECIMAL_FORMAT.format(recipe.getMinimumTier())));
            tooltip.add(Component.translatable("jei.bloodmagic.recipe.lpDrained", ChatUtil.DECIMAL_FORMAT.format(recipe.getSyphon())));
            tooltip.add(Component.translatable("jei.bloodmagic.recipe.ticksRequired", ChatUtil.DECIMAL_FORMAT.format(recipe.getTicks())));
        }

        return tooltip;
    }

    @Nonnull
    @Override
    public Component getTitle() {
        return Component.translatable("jei.bloodmagic.recipe.alchemytable");
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeAlchemyTable recipe, IFocusGroup focuses) {
        IRecipeSlotBuilder output = builder.addSlot(RecipeIngredientRole.OUTPUT, 92, 14);
        output.addItemStack(recipe.getOutput());

        IRecipeSlotBuilder orb = builder.addSlot(RecipeIngredientRole.CATALYST, 61, 1);
        orb.addItemStacks(OrbRegistry.getOrbsDownToTier(recipe.getMinimumTier()));

        for (int index = 0; index < recipe.getInput().size(); index++) {
            int x = index % 3;
            int y = index / 3;
            IRecipeSlotBuilder input = builder.addSlot(RecipeIngredientRole.INPUT, x * 18 + 1, y * 18 + 1);
            input.addIngredients(recipe.getInput().get(index));
        }
    }

    @Override
    public RecipeType<RecipeAlchemyTable> getRecipeType() {
        return RECIPE_TYPE;
    }

}
