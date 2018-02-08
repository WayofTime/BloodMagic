package WayofTime.bloodmagic.compat.jei.altar;

import WayofTime.bloodmagic.api.impl.recipe.RecipeBloodAltar;
import WayofTime.bloodmagic.util.helper.NumeralHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;
import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.util.List;

public class AltarRecipeJEI implements IRecipeWrapper {
    @Nonnull
    private final List<ItemStack> input;
    @Nonnull
    private final ItemStack output;

    private final String[] infoString;
    private final int consumptionRate;
    private final int drainRate;

    public AltarRecipeJEI(RecipeBloodAltar recipe) {
        this.input = NonNullList.from(ItemStack.EMPTY, recipe.getInput().getMatchingStacks());
        this.output = recipe.getOutput();

        this.infoString = new String[]{TextHelper.localize("jei.bloodmagic.recipe.requiredTier", NumeralHelper.toRoman(recipe.getMinimumTier().toInt())), TextHelper.localize("jei.bloodmagic.recipe.requiredLP", recipe.getSyphon())};
        this.consumptionRate = recipe.getConsumeRate();
        this.drainRate = recipe.getDrainRate();
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInputs(ItemStack.class, input);
        ingredients.setOutput(ItemStack.class, output);
    }

    @Nonnull
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        List<String> tooltip = Lists.newArrayList();
        if (mouseX >= 13 && mouseX <= 64 && mouseY >= 27 && mouseY <= 58) {
            tooltip.add(TextHelper.localize("jei.bloodmagic.recipe.consumptionRate", consumptionRate));
            tooltip.add(TextHelper.localize("jei.bloodmagic.recipe.drainRate", drainRate));
        }
        return tooltip;
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        minecraft.fontRenderer.drawString(infoString[0], 90 - minecraft.fontRenderer.getStringWidth(infoString[0]) / 2, 0, Color.gray.getRGB());
        minecraft.fontRenderer.drawString(infoString[1], 90 - minecraft.fontRenderer.getStringWidth(infoString[1]) / 2, 10, Color.gray.getRGB());
    }
}
