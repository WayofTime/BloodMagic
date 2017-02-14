package WayofTime.bloodmagic.compat.jei.orb;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import WayofTime.bloodmagic.compat.jei.BloodMagicPlugin;
import WayofTime.bloodmagic.util.helper.NumeralHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import WayofTime.bloodmagic.api.registry.OrbRegistry;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class ShapelessOrbRecipeJEI extends BlankRecipeWrapper implements ICraftingRecipeWrapper
{

    @Nonnull
    private final List inputs;
    private final int tier;
    @Nonnull
    private final ItemStack output;

    @SuppressWarnings("unchecked")
    public ShapelessOrbRecipeJEI(@Nonnull List input, int tier, @Nonnull ItemStack output)
    {
        ArrayList inputList = new ArrayList(input);

        for (Object object : inputList)
            if (object instanceof Integer)
                inputList.set(inputList.indexOf(object), OrbRegistry.getOrbsDownToTier((Integer) object));

        this.inputs = inputList;
        this.tier = tier;
        this.output = output;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        List<List<ItemStack>> expanded = BloodMagicPlugin.jeiHelper.getStackHelper().expandRecipeItemStackInputs(inputs);
        ingredients.setInputLists(ItemStack.class, expanded);
        ingredients.setOutput(ItemStack.class, output);
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
    {
        String draw = TextHelper.localize("jei.BloodMagic.recipe.requiredTier", NumeralHelper.toRoman(tier));
        minecraft.fontRendererObj.drawString(draw, 72 - minecraft.fontRendererObj.getStringWidth(draw) / 2, 10, Color.gray.getRGB());
    }
}
