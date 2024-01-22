package wayoftime.bloodmagic.compat.jei.arc;

import com.mojang.blaze3d.vertex.PoseStack;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraftforge.fluids.FluidStack;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.tags.BloodMagicTags;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.handler.event.ClientHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ARCFurnaceRecipeCategory implements IRecipeCategory<SmeltingRecipe> {
    public static final RecipeType<SmeltingRecipe> RECIPE_TYPE = RecipeType.create(BloodMagic.MODID, Constants.Compat.JEI_CATEGORY_ARC + "furnace", SmeltingRecipe.class);
    public static final ResourceLocation BACKGROUNDRL = BloodMagic.rl("gui/jei/arc.png");

    @Nonnull
    private final IDrawable background;
    private final IDrawable icon;

    public ARCFurnaceRecipeCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableItemStack(new ItemStack(BloodMagicBlocks.ALCHEMICAL_REACTION_CHAMBER.get()));
        background = guiHelper.createDrawable(BACKGROUNDRL, 0, 0, 157, 43);
    }


    @Nonnull
    @Override
    public Component getTitle() {
        return Component.translatable("jei.bloodmagic.recipe.arcfurnace");
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
    public void setRecipe(IRecipeLayoutBuilder builder, SmeltingRecipe recipe, IFocusGroup focuses) {
        IRecipeSlotBuilder output = builder.addSlot(RecipeIngredientRole.OUTPUT, 54, 17);
        output.addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));

        IRecipeSlotBuilder input = builder.addSlot(RecipeIngredientRole.INPUT, 1, 6);
        input.addIngredients(recipe.getIngredients().get(0));

        IRecipeSlotBuilder catalyst = builder.addSlot(RecipeIngredientRole.CATALYST, 22, 17);
        catalyst.addIngredients(Ingredient.of(BloodMagicTags.ARC_TOOL_FURNACE));
    }


    @Override
    public RecipeType<SmeltingRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }


    @Override
    public void draw(SmeltingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        FluidStack outputStack = FluidStack.EMPTY;
        ClientHandler.handleGuiTank(guiGraphics, outputStack, outputStack.getAmount(), 140, 7, 16, 36, 157, 6, 18, 38, (int) mouseX, (int) mouseY, BACKGROUNDRL, null);

        FluidStack inputStack = FluidStack.EMPTY;
        ClientHandler.handleGuiTank(guiGraphics, inputStack, inputStack.getAmount(), 1, 26, 16, 16, 175, 26, 18, 18, (int) mouseX, (int) mouseY, BACKGROUNDRL, null);
    }
}
