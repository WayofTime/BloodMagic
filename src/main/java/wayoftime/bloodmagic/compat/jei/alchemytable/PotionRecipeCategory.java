package wayoftime.bloodmagic.compat.jei.alchemytable;

import com.google.common.collect.Lists;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.item.potion.ItemAlchemyFlask;
import wayoftime.bloodmagic.core.registry.OrbRegistry;
import wayoftime.bloodmagic.recipe.EffectHolder;
import wayoftime.bloodmagic.recipe.flask.RecipePotionFlaskBase;
import wayoftime.bloodmagic.util.ChatUtil;
import wayoftime.bloodmagic.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class PotionRecipeCategory implements IRecipeCategory<RecipePotionFlaskBase> {
    public static final RecipeType<RecipePotionFlaskBase> RECIPE_TYPE = RecipeType.create(BloodMagic.MODID, Constants.Compat.JEI_CATEGORY_POTION, RecipePotionFlaskBase.class);
    @Nonnull
    private final IDrawable background;
    private final IDrawable icon;

    public PotionRecipeCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableItemStack(new ItemStack(BloodMagicBlocks.ALCHEMY_TABLE.get()));
        background = guiHelper.createDrawable(BloodMagic.rl("gui/jei/alchemytable.png"), 0, 0, 118, 40);
    }

    @Override
    public List<Component> getTooltipStrings(RecipePotionFlaskBase recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
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
        return Component.translatable("jei.bloodmagic.recipe.potionflask");
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
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull RecipePotionFlaskBase recipe, @Nonnull IFocusGroup focuses) {
        List<ItemStack> validOrbs = OrbRegistry.getOrbsDownToTier(recipe.getMinimumTier());

        ItemStack[] validOrbStacks = new ItemStack[validOrbs.size()];
        for (int i = 0; i < validOrbStacks.length; i++) {
            validOrbStacks[i] = validOrbs.get(i);
        }

        Item[] flaskItems = new Item[]{BloodMagicItems.ALCHEMY_FLASK.get(),
                BloodMagicItems.ALCHEMY_FLASK_THROWABLE.get(), BloodMagicItems.ALCHEMY_FLASK_LINGERING.get()};

        ItemStack[] flaskStacks = new ItemStack[flaskItems.length];

        builder.addSlot(RecipeIngredientRole.INPUT, 61, 1).addIngredients(Ingredient.of(validOrbStacks)).setSlotName("orbs");

        ItemStack flaskStack = recipe.getExamplePotionFlask();

        for (int i = 0; i < flaskItems.length; i++) {
            Item inputFlask = flaskItems[i];
            ItemStack copyFlaskStack = new ItemStack(inputFlask);
            copyFlaskStack.setTag(flaskStack.getTag());

            flaskStacks[i] = copyFlaskStack;
        }

        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(Ingredient.of(flaskStacks)).setSlotName("flask");
        List<Ingredient> inputList = recipe.getInput();

        for (int index = 0; index < inputList.size(); index++) {
            int x = (index + 1) % 3;
            int y = (index + 1) / 3;
            builder.addSlot(RecipeIngredientRole.INPUT, x * 18 + 1, y * 18 + 1).addIngredients(inputList.get(index)).setSlotName("input");
        }

        List<EffectHolder> holderList = ((ItemAlchemyFlask) flaskStack.getItem()).getEffectHoldersOfFlask(flaskStack);

        ItemStack outputStack = recipe.getOutput(flaskStack, holderList);
        ((ItemAlchemyFlask) flaskStack.getItem()).resyncEffectInstances(outputStack);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 92, 14).addItemStack(outputStack).setSlotName("output");


    }


    @Override
    public RecipeType<RecipePotionFlaskBase> getRecipeType() {
        return RECIPE_TYPE;
    }
}
