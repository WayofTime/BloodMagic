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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BMBlocks;
import wayoftime.bloodmagic.common.item.BMItems;
import wayoftime.bloodmagic.common.recipe.forge.ForgeRecipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.List;

public class SoulForgeRecipeCategory implements IRecipeCategory<ForgeRecipe> {

    public static final RecipeType<ForgeRecipe> RECIPE_TYPE = RecipeType.create(BloodMagic.MODID, "soul_forge", ForgeRecipe.class);
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.#");

    @Nonnull
    private final IDrawable background;
    private final IDrawable icon;

    public SoulForgeRecipeCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableItemStack(new ItemStack(BMBlocks.HELLFIRE_FORGE.block().get()));
        background = guiHelper.createDrawable(BloodMagic.rl("gui/jei/soulforge.png"), 0, 0, 100, 40);
    }

    @Override
    public RecipeType<ForgeRecipe> getRecipeType() {
        return RECIPE_TYPE;
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
    public List<Component> getTooltipStrings(ForgeRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        List<Component> tooltip = Lists.newArrayList();
        if (mouseX >= 40 && mouseX <= 60 && mouseY >= 21 && mouseY <= 34) {
            tooltip.add(Component.translatable("jei.bloodmagic.recipe.minimumsouls", DECIMAL_FORMAT.format(recipe.getMinWill())));
            tooltip.add(Component.translatable("jei.bloodmagic.recipe.soulsdrained", DECIMAL_FORMAT.format(recipe.getDrain())));
        }
        return tooltip;
    }

    @Override
    public void draw(ForgeRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        var poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(45, 23, 0);
        poseStack.scale(0.5f, 0.5f, 1f);
        guiGraphics.drawString(Minecraft.getInstance().font, Component.translatable("jei.bloodmagic.recipe.will"), 0, 0, 0x8b8b8b, false);
        poseStack.popPose();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ForgeRecipe recipe, IFocusGroup focuses) {
        // Soul gems that have enough will
        List<ItemStack> validGems = Lists.newArrayList();
        for (DefaultWill will : DefaultWill.values()) {
            if (will.minSouls >= recipe.getMinWill()) {
                validGems.add(will.willStack);
            }
        }
        IRecipeSlotBuilder gems = builder.addSlot(RecipeIngredientRole.CATALYST, 43, 1);
        gems.addItemStacks(validGems);

        // Output
        IRecipeSlotBuilder output = builder.addSlot(RecipeIngredientRole.OUTPUT, 74, 14);
        output.addItemStack(recipe.getOutput());

        // Inputs (up to 4 in a 2x2 grid)
        List<? extends net.minecraft.world.item.crafting.Ingredient> inputs = recipe.getCraftingIngredients();
        for (int index = 0; index < inputs.size(); index++) {
            int x = index % 2;
            int y = index / 2;
            IRecipeSlotBuilder input = builder.addSlot(RecipeIngredientRole.INPUT, x * 18 + 1, y * 18 + 1);
            input.addIngredients(inputs.get(index));
        }
    }

    public enum DefaultWill {
        RAW(new ItemStack(BMItems.RAW_WILL.get()), 16),
        PETTY(new ItemStack(BMItems.SOUL_GEM_PETTY.get()), 64),
        LESSER(new ItemStack(BMItems.SOUL_GEM_LESSER.get()), 256),
        COMMON(new ItemStack(BMItems.SOUL_GEM_COMMON.get()), 1024),
        GREATER(new ItemStack(BMItems.SOUL_GEM_GREATER.get()), 4096),
        GRAND(new ItemStack(BMItems.SOUL_GEM_GRAND.get()), 16384);

        public final ItemStack willStack;
        public final double minSouls;

        DefaultWill(ItemStack willStack, double minSouls) {
            this.willStack = willStack;
            this.minSouls = minSouls;
        }
    }
}
