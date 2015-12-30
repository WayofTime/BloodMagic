package WayofTime.bloodmagic.compat.jei.binding;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import WayofTime.bloodmagic.compat.jei.BloodMagicRecipeWrapper;

public class BindingRecipeJEI extends BloodMagicRecipeWrapper
{

    @Nonnull
    private final List<ItemStack> inputs;

    @Nonnull
    private final ItemStack output;

    @SuppressWarnings("unchecked")
    public BindingRecipeJEI(@Nonnull ItemStack input, @Nonnull ItemStack catalyst, @Nonnull ItemStack output)
    {
        this.inputs = Arrays.asList(new ItemStack[] { input, catalyst });
        this.output = output;
    }

    @Override
    public List getInputs()
    {
        return inputs;
    }

    @Override
    public List getOutputs()
    {
        return Collections.singletonList(output);
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight)
    {

    }

    @Override
    public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight)
    {

    }
}
