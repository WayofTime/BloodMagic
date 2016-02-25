package WayofTime.bloodmagic.compat.jei.altar;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class AltarRecipeJEI extends BlankRecipeWrapper
{
    @Nonnull
    private final ItemStack input;

    @Nonnull
    private final ItemStack output;

    private final String[] infoString;
    private final int consumptionRate;
    private final int drainRate;

    public AltarRecipeJEI(@Nonnull ItemStack input, @Nonnull ItemStack output, int tier, int requiredLP, int consumptionRate, int drainRate)
    {
        this.input = input;
        this.output = output;

        this.infoString = new String[] { TextHelper.localize("jei.BloodMagic.recipe.requiredTier", tier), TextHelper.localize("jei.BloodMagic.recipe.requiredLP", requiredLP) };
        this.consumptionRate = consumptionRate;
        this.drainRate = drainRate;
    }

    @Override
    public List getInputs()
    {
        return Collections.singletonList(input);
    }

    @Override
    public List getOutputs()
    {
        return Collections.singletonList(output);
    }

    @Nullable
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY)
    {
        ArrayList<String> ret = new ArrayList<String>();
        if (mouseX >= 13 && mouseX <= 64 && mouseY >= 27 && mouseY <= 58)
        {
            ret.add(TextHelper.localize("jei.BloodMagic.recipe.consumptionRate", consumptionRate));
            ret.add(TextHelper.localize("jei.BloodMagic.recipe.drainRate", drainRate));
            return ret;
        }
        return null;
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight)
    {
        minecraft.fontRendererObj.drawString(infoString[0], 90 - minecraft.fontRendererObj.getStringWidth(infoString[0]) / 2, 0, Color.gray.getRGB());
        minecraft.fontRendererObj.drawString(infoString[1], 90 - minecraft.fontRendererObj.getStringWidth(infoString[1]) / 2, 10, Color.gray.getRGB());
    }
}
