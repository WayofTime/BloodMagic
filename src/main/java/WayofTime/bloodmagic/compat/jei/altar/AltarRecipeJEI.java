package WayofTime.bloodmagic.compat.jei.altar;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import WayofTime.bloodmagic.util.helper.NumeralHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class AltarRecipeJEI extends BlankRecipeWrapper
{
    @Nonnull
    private final List<ItemStack> input;
    @Nonnull
    private final ItemStack output;

    private final String[] infoString;
    private final int consumptionRate;
    private final int drainRate;

    public AltarRecipeJEI(@Nonnull List<ItemStack> input, @Nonnull ItemStack output, int tier, int requiredLP, int consumptionRate, int drainRate)
    {
        this.input = input;
        this.output = output;

        this.infoString = new String[] { TextHelper.localize("jei.BloodMagic.recipe.requiredTier", NumeralHelper.toRoman(tier)), TextHelper.localize("jei.BloodMagic.recipe.requiredLP", requiredLP) };
        this.consumptionRate = consumptionRate;
        this.drainRate = drainRate;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(ItemStack.class, input);
        ingredients.setOutput(ItemStack.class, output);
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY)
    {
        ArrayList<String> ret = new ArrayList<String>();
        if (mouseX >= 13 && mouseX <= 64 && mouseY >= 27 && mouseY <= 58)
        {
            ret.add(TextHelper.localize("jei.BloodMagic.recipe.consumptionRate", consumptionRate));
            ret.add(TextHelper.localize("jei.BloodMagic.recipe.drainRate", drainRate));
        }
        return ret;
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
    {
        minecraft.fontRendererObj.drawString(infoString[0], 90 - minecraft.fontRendererObj.getStringWidth(infoString[0]) / 2, 0, Color.gray.getRGB());
        minecraft.fontRendererObj.drawString(infoString[1], 90 - minecraft.fontRendererObj.getStringWidth(infoString[1]) / 2, 10, Color.gray.getRGB());
    }
}
