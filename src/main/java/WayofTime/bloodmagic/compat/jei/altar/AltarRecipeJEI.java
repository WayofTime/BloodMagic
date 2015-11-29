package WayofTime.bloodmagic.compat.jei.altar;

import WayofTime.bloodmagic.compat.jei.BloodMagicRecipeWrapper;
import WayofTime.bloodmagic.util.helper.TextHelper;
import com.google.common.base.Strings;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class AltarRecipeJEI extends BloodMagicRecipeWrapper {

    @Nonnull
    private final ItemStack input;

    @Nonnull
    private final ItemStack output;

    private final String[] infoString;

    public AltarRecipeJEI(@Nonnull ItemStack input, @Nonnull ItemStack output, int tier, int requiredLP) {
        this.input = input;
        this.output = output;

        this.infoString = new String[]{ TextHelper.localize("jei.BloodMagic.recipe.requiredTier", tier), TextHelper.localize("jei.BloodMagic.recipe.requiredLP", requiredLP) };
    }

    @Override
    public List getInputs() {
        return Collections.singletonList(input);
    }

    @Override
    public List getOutputs() {
        return Collections.singletonList(output);
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft) {
        minecraft.fontRendererObj.drawString(infoString[0], 90 - minecraft.fontRendererObj.getStringWidth(infoString[0]) / 2, 0, Color.gray.getRGB());
        minecraft.fontRendererObj.drawString(infoString[1], 90 - minecraft.fontRendererObj.getStringWidth(infoString[1]) / 2, 10, Color.gray.getRGB());
    }
}
