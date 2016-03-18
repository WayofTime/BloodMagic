package WayofTime.bloodmagic.compat.jei.binding;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;

public class BindingRecipeJEI extends BlankRecipeWrapper
{
    @Nonnull
    private final List<ItemStack> inputs;

    @Nonnull
    private final ItemStack output;

    @SuppressWarnings("unchecked")
    public BindingRecipeJEI(@Nonnull ItemStack input, @Nonnull ItemStack catalyst, @Nonnull ItemStack output)
    {
        this.inputs = Arrays.asList(input, catalyst);
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
}
