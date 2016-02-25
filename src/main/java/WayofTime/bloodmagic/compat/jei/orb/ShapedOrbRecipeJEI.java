package WayofTime.bloodmagic.compat.jei.orb;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import WayofTime.bloodmagic.api.registry.OrbRegistry;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class ShapedOrbRecipeJEI implements IShapedCraftingRecipeWrapper
{

    @Nonnull
    private final List inputs;

    private final int tier;

    @Nonnull
    private final ItemStack output;

    @SuppressWarnings("unchecked")
    public ShapedOrbRecipeJEI(@Nonnull List input, int tier, @Nonnull ItemStack output)
    {
        ArrayList inputList = new ArrayList(input);
        int replaceIndex = 0;
        Object toReplace = null;

        for (Object object : inputList)
        {
            if (object instanceof Integer)
            {
                replaceIndex = inputList.indexOf(object);
                toReplace = object;
            }
        }

        if (toReplace != null)
        {
            inputList.remove(replaceIndex);
            inputList.add(replaceIndex, OrbRegistry.getOrbsDownToTier((Integer) toReplace));
        }

        this.inputs = inputList;
        this.tier = tier;
        this.output = output;
    }

    @Override
    public int getWidth()
    {
        return 3;
    }

    @Override
    public int getHeight()
    {
        return 3;
    }

    @Override
    public List getInputs()
    {
        return inputs;
    }

    @Override
    public List<ItemStack> getOutputs()
    {
        return Collections.singletonList(output);
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight)
    {
        String draw = TextHelper.localize("jei.BloodMagic.recipe.requiredTier", tier);
        minecraft.fontRendererObj.drawString(draw, 72 - minecraft.fontRendererObj.getStringWidth(draw) / 2, 10, Color.gray.getRGB());
    }

    @Override
    public List<FluidStack> getFluidInputs()
    {
        return null;
    }

    @Override
    public List<FluidStack> getFluidOutputs()
    {
        return null;
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
    {

    }

    @Override
    public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight)
    {

    }

    @Nullable
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY)
    {
        return null;
    }

    @Override
    public boolean handleClick(@Nonnull Minecraft minecraft, int mouseX, int mouseY, int mouseButton)
    {
        return false;
    }
}
