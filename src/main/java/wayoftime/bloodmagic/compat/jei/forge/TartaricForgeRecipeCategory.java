package wayoftime.bloodmagic.compat.jei.forge;

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
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.recipe.RecipeTartaricForge;
import wayoftime.bloodmagic.util.ChatUtil;
import wayoftime.bloodmagic.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TartaricForgeRecipeCategory implements IRecipeCategory<RecipeTartaricForge> {
    public static final RecipeType<RecipeTartaricForge> RECIPE_TYPE = RecipeType.create(BloodMagic.MODID, Constants.Compat.JEI_CATEGORY_SOULFORGE, RecipeTartaricForge.class);

    @Nonnull
    private final IDrawable background;
    private final IDrawable icon;

    public TartaricForgeRecipeCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableItemStack(new ItemStack(BloodMagicBlocks.SOUL_FORGE.get()));
        background = guiHelper.createDrawable(BloodMagic.rl("gui/jei/soulforge.png"), 0, 0, 100, 40);
    }

    @Override
    public RecipeType<RecipeTartaricForge> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public List<Component> getTooltipStrings(RecipeTartaricForge recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        List<Component> tooltip = Lists.newArrayList();
        if (mouseX >= 40 && mouseX <= 60 && mouseY >= 21 && mouseY <= 34) {
            tooltip.add(Component.translatable("jei.bloodmagic.recipe.minimumsouls", ChatUtil.DECIMAL_FORMAT.format(recipe.getMinimumSouls())));
            tooltip.add(Component.translatable("jei.bloodmagic.recipe.soulsdrained", ChatUtil.DECIMAL_FORMAT.format(recipe.getSoulDrain())));
        }
        return tooltip;
    }

    @Nonnull
    @Override
    public Component getTitle() {
        return Component.translatable("jei.bloodmagic.recipe.soulforge");
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
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeTartaricForge recipe, IFocusGroup focuses) {
        List<ItemStack> validGems = Lists.newArrayList();
        for (DefaultWill will : DefaultWill.values()) {
            if (will.minSouls >= recipe.getMinimumSouls()) {
                validGems.add(will.willStack);
            }
        }
        IRecipeSlotBuilder gems = builder.addSlot(RecipeIngredientRole.CATALYST, 43, 1);
        gems.addItemStacks(validGems);

        IRecipeSlotBuilder output = builder.addSlot(RecipeIngredientRole.OUTPUT, 74, 14);
        output.addItemStack(recipe.getOutput());

        for (int index = 0; index < recipe.getInput().size(); index++) {
           int x = index % 2 ;
           int y = index / 2;
            IRecipeSlotBuilder input = builder.addSlot(RecipeIngredientRole.INPUT, x * 18 + 1, y * 18 + 1);
            input.addIngredients(recipe.getInput().get(index));
        }

    }


    public enum DefaultWill {
        SOUL(new ItemStack(BloodMagicItems.MONSTER_SOUL_RAW.get()), 16),
        PETTY(new ItemStack(BloodMagicItems.PETTY_GEM.get()), 64),
        LESSER(new ItemStack(BloodMagicItems.LESSER_GEM.get()), 256),
        COMMON(new ItemStack(BloodMagicItems.COMMON_GEM.get()), 1024),
        GREATER(new ItemStack(BloodMagicItems.GREATER_GEM.get()), 4096);
//		GRAND(new ItemStack(RegistrarBloodMagicItems.SOUL_GEM, 1, 4), 16384);

        public final ItemStack willStack;
        public final double minSouls;

        DefaultWill(ItemStack willStack, double minSouls) {
            this.willStack = willStack;
            this.minSouls = minSouls;
        }
    }
}
